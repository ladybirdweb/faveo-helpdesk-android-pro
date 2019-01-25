package co.helpdesk.faveo.pro.frontend.fragments.tickets;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.helpdesk.faveo.pro.CircleTransform;
import co.helpdesk.faveo.pro.Helper;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.pro.frontend.activities.MainActivity;
import co.helpdesk.faveo.pro.frontend.activities.MultiAssigningActivity;
import co.helpdesk.faveo.pro.frontend.activities.NotificationActivity;
import co.helpdesk.faveo.pro.frontend.activities.SearchActivity;
import co.helpdesk.faveo.pro.frontend.activities.TicketDetailActivity;
import co.helpdesk.faveo.pro.frontend.activities.TicketFilter;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.pro.model.Data;
import co.helpdesk.faveo.pro.model.TicketOverview;
import es.dmoral.toasty.Toasty;

//import co.helpdesk.faveo.pro.frontend.activities.TicketMergeActtivity;

public class TrashTickets extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private android.support.v7.view.ActionMode mActionMode;
    @BindView(R.id.cardList)
    ShimmerRecyclerView recyclerView;

    @BindView(R.id.empty_view)
    TextView empty_view;
    @BindView(R.id.noiternet_view)
    TextView noInternet_view;
    //int currentPage = 1;
    static String nextPageURL = "";
    View rootView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    int total;
    String ticket;
    @BindView(R.id.totalcount)
    TextView textView;

    TicketOverviewAdapter ticketOverviewAdapter;
    List<TicketOverview> ticketOverviewList = new ArrayList<>();
    ArrayList<Data> statusItems;
    private boolean loading = true;
    ProgressDialog progressDialog;
    int pastVisibleItems, visibleItemCount, totalItemCount;
    String status;
    public String mParam1;
    public String mParam2;
    String title;
    int page;
    int pageno=1;
    int id=0;
    private OnFragmentInteractionListener mListener;
    ImageView imageViewssignTicket,imageViewChangingStatus;
    public TextView textViewShowingCount;
    public static TrashTickets newInstance(String param1, String param2) {
        TrashTickets fragment = new TrashTickets();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public TrashTickets() {
    }
    /**
     *
     * @param savedInstanceState under special circumstances, to restore themselves to a previous
     * state using the data stored in this bundle.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        JSONObject jsonObject;
        String json = Prefs.getString("DEPENDENCY", "");
        try {
            jsonObject=new JSONObject(json);
            JSONArray jsonArrayStaffs=jsonObject.getJSONArray("status");
            for (int i=0;i<jsonArrayStaffs.length();i++){
                if (jsonArrayStaffs.getJSONObject(i).getString("name").equals("Open")){
                    Prefs.putString("openid",jsonArrayStaffs.getJSONObject(i).getString("id"));
                }
                else if (jsonArrayStaffs.getJSONObject(i).getString("name").equals("Resolved")){
                    Prefs.putString("resolvedid",jsonArrayStaffs.getJSONObject(i).getString("id"));
                }
                else if (jsonArrayStaffs.getJSONObject(i).getString("name").equals("Closed")){
                    Prefs.putString("closedid",jsonArrayStaffs.getJSONObject(i).getString("id"));
                }
                else if (jsonArrayStaffs.getJSONObject(i).getString("name").equals("Deleted")){
                    Prefs.putString("deletedid",jsonArrayStaffs.getJSONObject(i).getString("id"));
                }
                else if (jsonArrayStaffs.getJSONObject(i).getString("name").equals("Archived")){
                    Prefs.putString("archivedid",jsonArrayStaffs.getJSONObject(i).getString("id"));
                }
                else if (jsonArrayStaffs.getJSONObject(i).getString("name").equals("Verified")){
                    Prefs.putString("verifiedid",jsonArrayStaffs.getJSONObject(i).getString("id"));
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * @param inflater for loading the fragment.
     * @param container where the fragment is going to be load.
     * @param savedInstanceState
     * @return after initializing returning the rootview
     * which is having the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            Prefs.putString("cameFromSearch","false");
            Prefs.putString("querry","null");
            Prefs.putString("cameFromNotification","false");
            rootView = inflater.inflate(R.layout.fragment_recycler, container, false);
            ButterKnife.bind(this, rootView);
            Prefs.putString("source","4");
            Prefs.putString("Show","trash");
            statusItems=new ArrayList<>();
            JSONObject jsonObject1;
            String json1 = Prefs.getString("DEPENDENCY", "");
            //statusItems.add(new Data(0, "Please select help topic"));
            try {
                jsonObject1 = new JSONObject(json1);
                JSONArray jsonArrayHelpTopics = jsonObject1.getJSONArray("status");
                for (int i = 0; i < jsonArrayHelpTopics.length(); i++) {
                    Data data1 = new Data(Integer.parseInt(jsonArrayHelpTopics.getJSONObject(i).getString("id")), jsonArrayHelpTopics.getJSONObject(i).getString("name"));
                    statusItems.add(data1);
                    //menu.add("First Menu");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            Toolbar toolbar1= (Toolbar) rootView.findViewById(R.id.toolbar3);
//            toolbar1.setVisibility(View.VISIBLE);
//            toolbar1.setOverflowIcon(getResources().getDrawable(R.drawable.ic_filter_list_black_24dp));
//            toolbar1.setTitle(getString(R.string.filter));
//            toolbar1.setTitleTextColor(Color.parseColor("#3da6d7"));
//
//            toolbar1.inflateMenu(R.menu.menu_for_filtering);
//            toolbar1.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(MenuItem item) {
//
//                    return false;
//                }
//            });
            Toolbar toolbar= (Toolbar) rootView.findViewById(R.id.toolbar2);
//        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            toolbar.setVisibility(View.VISIBLE);
            toolbar.setTitle(getString(R.string.sortbytitle));
            Toolbar toolbar1= (Toolbar) rootView.findViewById(R.id.toolbarfilteration);
            //toolbar1.setVisibility(View.VISIBLE);
            toolbar1.setOverflowIcon(getResources().getDrawable(R.drawable.ic_if_filter_383135));
            //toolbar1.setOverflowIcon(getResources().getDrawable(R.drawable.ic_if_filter_383135));

//            toolbar1.inflateMenu(R.menu.menu_for_filtering);
//            toolbar1.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(MenuItem item) {
//
//                    return false;
//                }
//            });

            toolbar1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(getActivity(), "clicked on toolbar", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getActivity(), TicketFilter.class);
                    startActivity(intent);

                }
            });

            toolbar.setTitleTextColor(Color.parseColor("#3da6d7"));
            toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_sort_black_24dp));
//        mTitle.setText("Sort By");
            toolbar.inflateMenu(R.menu.menu_for_sorting);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    try {
                        if (item != null) {
                            item.getSubMenu().clearHeader();
                        }
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                    title = getString(R.string.app_name);
                    if (item.getItemId() == R.id.due_ascending) {
                        Prefs.putString("dueasc","1");
                        //Toast.makeText(getActivity(), "due in ascending", Toast.LENGTH_SHORT).show();
                        title = getString(R.string.duebyasc);
                        fragmentController(title);
                        return true;
                    }
                    if (item.getItemId() == R.id.due_descending) {
                        Prefs.putString("dueasc","2");
                        title = getString(R.string.duebydesc);
                        fragmentController(title);
                        return true;
                    }
                    if (item.getItemId()==R.id.created_ascending){
                        title = getString(R.string.createdat);
                        fragmentController(title);
                        return true;
                    }
                    if (item.getItemId()==R.id.created_descending){
                        title = getString(R.string.createdat);
                        fragmentController(title);
                        return true;
                    }
                    if (item.getItemId()==R.id.ticketnumber_ascending){
                        title = getString(R.string.sortbyticketnoasc);
                        fragmentController(title);
                        return true;
                    }
                    if (item.getItemId()==R.id.ticketnumber_descending){
                        title = getString(R.string.sortbyticketnodesc);
                        fragmentController(title);
                        return true;
                    }
                    if (item.getItemId()==R.id.priorityasc){
                        title = getString(R.string.sortbypriorityasc);
                        fragmentController(title);
                        return true;
                    }
                    if (item.getItemId()==R.id.prioritydesc){
                        title = getString(R.string.sortbyprioritydesc);
                        fragmentController(title);
                        return true;
                    }
                    if (item.getItemId()==R.id.updatedatasc){
                        title = getString(R.string.updatedat);
                        fragmentController(title);
                        return true;
                    }
                    if (item.getItemId()==R.id.updatedatdesc){
                        title = getString(R.string.updatedat);
                        fragmentController(title);
                        return true;
                    }


                    return true;
                }
            });
            swipeRefresh.setColorSchemeResources(R.color.faveo_blue);
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait");

            if (InternetReceiver.isConnected()) {
                noInternet_view.setVisibility(View.GONE);
                // swipeRefresh.setRefreshing(true);
                swipeRefresh.setRefreshing(true);
                new FetchFirst(getActivity(),page).execute();
            } else {
                noInternet_view.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                empty_view.setVisibility(View.GONE);
            }

            swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (InternetReceiver.isConnected()) {
                        loading = true;
                        recyclerView.setVisibility(View.VISIBLE);
                        noInternet_view.setVisibility(View.GONE);
                        try {
                            mActionMode.finish();
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }
                        new FetchFirst(getActivity(),page).execute();
                    } else {
                        recyclerView.setVisibility(View.INVISIBLE);
                        swipeRefresh.setRefreshing(false);
                        empty_view.setVisibility(View.GONE);
                        noInternet_view.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.trash));
        return rootView;
    }

    public void fragmentController(String title){
        Fragment fragment;
        fragment =getActivity().getSupportFragmentManager().findFragmentByTag(title);
        if (fragment == null)
            fragment = new TrashTickets();
        if (fragment != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
        }
    }
    public void setNullToActionMode() {
        Log.d("Inbox Ticket","Came from toolbar action mode");
        if (mActionMode != null)
            mActionMode = null;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_trash, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            if (item != null) {
                item.getSubMenu().clearHeader();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        int id = item.getItemId();
        if (id == R.id.action_noti) {
            Intent intent = new Intent(getActivity(), NotificationActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.actionsearch) {
            Prefs.putString("cameFromClientList","false");
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




    //    public class ReadFromDatabase extends AsyncTask<String, Void, String> {
//        Context context;
//
//        public ReadFromDatabase(Context context) {
//            this.context = context;
//        }
//
//        protected String doInBackground(String... urls) {
//            DatabaseHandler databaseHandler = new DatabaseHandler(context);
//            ticketOverviewList = databaseHandler.getTicketOverview();
//            databaseHandler.close();
//            return "success";
//        }
//
//        protected void onPostExecute(String result) {
//            if (swipeRefresh.isRefreshing())
//                swipeRefresh.setRefreshing(false);
//            if (progressDialog.isShowing())
//                progressDialog.dismiss();
//            ticketOverviewAdapter = new TicketOverviewAdapter(ticketOverviewList);
//            recyclerView.setAdapter(ticketOverviewAdapter);
//            if (ticketOverviewAdapter.getItemCount() == 0) {
//                tv.setVisibility(View.VISIBLE);
//            } else tv.setVisibility(View.GONE);
//        }
//    }

    private class StatusChange extends AsyncTask<String, Void, String> {
        int statusId;
        String ticketId;

        StatusChange(String ticketId, int statusId) {

            this.ticketId=ticketId;
            this.statusId=statusId;

        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().postStatusChangedMultiple(ticketId,statusId);
            //return new Helpdesk().postStatusChanged(ticketId,statusId);
        }

        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            String state=Prefs.getString("403",null);
            //progressDialog.dismiss();
            if (result == null) {
                Toasty.error(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }
            try {
                if (state.equals("403") && !state.equals("null")) {
                    Toasty.warning(getActivity(), getString(R.string.permission), Toast.LENGTH_LONG).show();
                    Prefs.putString("403", "null");
                    return;
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }

            try{
                JSONObject jsonObject=new JSONObject(result);
                JSONArray jsonArray=jsonObject.getJSONArray("message");
                for (int i=0;i<jsonArray.length();i++){
                    String message=jsonArray.getString(i);
                    if (message.contains("Permission denied")){
                        Toasty.warning(getActivity(), getString(R.string.permission), Toast.LENGTH_LONG).show();
                        Prefs.putString("403", "null");
                        return;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                JSONArray jsonArray=jsonObject1.getJSONArray("message");
                for (int i=0;i<jsonArray.length();i++){
                    String message=jsonArray.getString(i);
                    if (message.equals("Permission denied, you do not have permission to access the requested page.")){
                        Toasty.warning(getActivity(), getString(R.string.permission), Toast.LENGTH_LONG).show();
                        Prefs.putString("403", "null");
                        return;
                    }
                }

            }catch (JSONException e){
                e.printStackTrace();
            }


            try {

                JSONObject jsonObject = new JSONObject(result);
                //JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                JSONObject jsonObject2=jsonObject.getJSONObject("error");
                JSONArray jsonArray=jsonObject2.getJSONArray("ticket_id");
                String value = jsonArray.getString(0);
                Log.d("VALUE",value);
                //String message1=jsonObject2.getString("ticket_id");
                //String message2 = jsonObject1.getString("message");


//                if (message2.contains("permission denied")&&Prefs.getString("403",null).equals("403")){
//
//                }
                if (value.contains("The ticket id field is required.")){
                    Toasty.info(getActivity(), getString(R.string.noticket), Toast.LENGTH_LONG).show();
                    return;
                }
//               else if (message2.contains("Status changed to Deleted")) {
//                    Toasty.success(getActivity(), getString(R.string.status_deleted), Toast.LENGTH_LONG).show();
//                    Prefs.putString("tickets", null);
//                    //Prefs.putString("ticketstatus", "Deleted");
//                    getActivity().finish();
//                    startActivity(new Intent(getActivity(), MainActivity.class));
//                } else if (message2.contains("Status changed to Open")) {
//                    Toasty.success(getActivity(), getString(R.string.status_opened), Toast.LENGTH_LONG).show();
//                    Prefs.putString("tickets", null);
//                    getActivity().finish();
//                    startActivity(new Intent(getActivity(), MainActivity.class));
//                } else if (message2.contains("Status changed to Closed")) {
//                    Toasty.success(getActivity(), getString(R.string.status_closed), Toast.LENGTH_LONG).show();
//                    Prefs.putString("tickets", null);
//                    getActivity().finish();
//                    startActivity(new Intent(getActivity(), MainActivity.class));
//                } else if (message2.contains("Status changed to Resolved")) {
//                    Toasty.success(getActivity(), getString(R.string.status_resolved), Toast.LENGTH_LONG).show();
//                    Prefs.putString("tickets", null);
//                    getActivity().finish();
//                    startActivity(new Intent(getActivity(), MainActivity.class));
//                }


            }catch (JSONException | NullPointerException e) {
                e.printStackTrace();

            }
            try {

                JSONObject jsonObject = new JSONObject(result);
                //JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                //String message1=jsonObject2.getString("ticket_id");
                String message2 = jsonObject.getString("message");
                if (!message2.equals("null")||!message2.equals("")){
                    Toasty.success(getActivity(),getString(R.string.successfullyChanged),Toast.LENGTH_LONG).show();
                    Prefs.putString("tickets", null);
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }
                else{
                    Toasty.error(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    return;
                }

//                if (message2.contains("Status changed to Deleted")) {
//                    Toasty.success(getActivity(), getString(R.string.status_deleted), Toast.LENGTH_LONG).show();
//                    Prefs.putString("tickets", null);
//                    //Prefs.putString("ticketstatus", "Deleted");
//                    getActivity().finish();
//                    startActivity(new Intent(getActivity(), MainActivity.class));
//                } else if (message2.contains("Status changed to Open")) {
//                    Toasty.success(getActivity(), getString(R.string.status_opened), Toast.LENGTH_LONG).show();
//                    Prefs.putString("tickets", null);
//                    getActivity().finish();
//                    startActivity(new Intent(getActivity(), MainActivity.class));
//                } else if (message2.contains("Status changed to Closed")) {
//                    Toasty.success(getActivity(), getString(R.string.status_closed), Toast.LENGTH_LONG).show();
//                    Prefs.putString("tickets", null);
//                    getActivity().finish();
//                    startActivity(new Intent(getActivity(), MainActivity.class));
//                } else if (message2.contains("Status changed to Resolved")) {
//                    Toasty.success(getActivity(), getString(R.string.status_resolved), Toast.LENGTH_LONG).show();
//                    Prefs.putString("tickets", null);
//                    getActivity().finish();
//                    startActivity(new Intent(getActivity(), MainActivity.class));
//                }


            }catch (JSONException | NullPointerException e) {
                e.printStackTrace();

            }




        }


    }
    private class DeleteTicket extends AsyncTask<String, Void, String> {
        String ticketId;
        DeleteTicket(String ticketId) {
            this.ticketId=ticketId;
            }
            protected String doInBackground(String... urls) {
            return new Helpdesk().ticketDeleteForever(ticketId);
            //return new Helpdesk().postStatusChanged(ticketId,statusId);
        }

        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            String state=Prefs.getString("403",null);
            //progressDialog.dismiss();
            if (result == null) {
                Toasty.error(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }
            try {
                if (state.equals("403") && !state.equals("null")) {
                    Toasty.warning(getActivity(), getString(R.string.permission), Toast.LENGTH_LONG).show();
                    Prefs.putString("403", "null");
                    return;
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }

//            try {
//                JSONObject jsonObject = new JSONObject(result);
//                JSONObject jsonObject1 = jsonObject.getJSONObject("response");
//                JSONArray jsonArray=jsonObject1.getJSONArray("message");
//                for (int i=0;i<jsonArray.length();i++){
//                    String message=jsonArray.getString(i);
//                    if (message.equals("Permission denied, you do not have permission to access the requested page.")){
//                        Toasty.warning(getActivity(), getString(R.string.permission), Toast.LENGTH_LONG).show();
//                        Prefs.putString("403", "null");
//                        return;
//                    }
//                }
//
//            }catch (JSONException e){
//                e.printStackTrace();
//            }


            try {

                JSONObject jsonObject = new JSONObject(result);
                //JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                String response=jsonObject.getString("success");
                if (response.contains("deleted successfully")){
                    Toasty.success(getActivity(),"Tickets are succesfully deleted",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getActivity(),MainActivity.class);
                    startActivity(intent);
                    return;
                }


            }catch (JSONException | NullPointerException e) {
                e.printStackTrace();

            }
        }


    }


    /**
     * Async task for getting the my tickets.
     */
    private class FetchFirst extends AsyncTask<String, Void, String> {
        Context context;
        int page;
        FetchFirst(Context context,int page) {
            this.context = context;
            this.page=page;
        }

        protected String doInBackground(String... urls) {
//            if (nextPageURL.equals("null")) {
//                return "all done";
//            }
            String result = new Helpdesk().getTrashTickets(page);
            if (result == null)
                return null;
            String data;
            ticketOverviewList.clear();
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                total=jsonObject1.getInt("total");
                try {
                    data = jsonObject1.getString("data");
                    int trash = jsonObject1.getInt("total");
                    if (trash > 999)
                        Prefs.putString("trashTickets", "999+");
                    else
                        Prefs.putString("trashTickets", trash + "");

                    nextPageURL = jsonObject1.getString("next_page_url");
                } catch (JSONException e) {
                    data = jsonObject.getString("result");
                }
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    TicketOverview ticketOverview = Helper.parseTicketOverview(jsonArray, i);
                    if (ticketOverview != null)
                        ticketOverviewList.add(ticketOverview);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "success";
        }

        protected void onPostExecute(String result) {
            swipeRefresh.setRefreshing(false);
            textView.setText(""+total+" Tickets");
            if (swipeRefresh.isRefreshing())
                swipeRefresh.setRefreshing(false);

            if (result == null) {
                Toasty.error(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }
            if (result.equals("all done")) {

                Toasty.info(context, getString(R.string.all_caught_up), Toast.LENGTH_SHORT).show();
                //return;
            }
            //recyclerView = (ShimmerRecyclerView) rootView.findViewById(R.id.cardList);
            recyclerView.setHasFixedSize(false);
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (dy > 0) {
                        visibleItemCount = linearLayoutManager.getChildCount();
                        totalItemCount = linearLayoutManager.getItemCount();
                        pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();
                        if (loading) {
                            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                loading = false;
                                pageno++;
                                new FetchNextPage(getActivity(),pageno,"trash").execute();
                                StyleableToast st = new StyleableToast(getContext(), getString(R.string.loading), Toast.LENGTH_SHORT);
                                st.setBackgroundColor(Color.parseColor("#3da6d7"));
                                st.setTextColor(Color.WHITE);
                                st.setIcon(R.drawable.ic_autorenew_black_24dp);
                                st.spinIcon();
                                st.setMaxAlpha();
                                st.show();
                            }
                        }
                    }
                }
            });

            ticketOverviewAdapter = new TicketOverviewAdapter(getContext(),ticketOverviewList);
            runLayoutAnimation(recyclerView);
            recyclerView.setAdapter(ticketOverviewAdapter);
            if (ticketOverviewAdapter.getItemCount() == 0) {
                empty_view.setVisibility(View.VISIBLE);
            } else empty_view.setVisibility(View.GONE);

        }
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom);

        recyclerView.setLayoutAnimation(controller);
        ticketOverviewAdapter.notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
    /**
     * Async task for next page details.
     */
    private class FetchNextPage extends AsyncTask<String, Void, String> {
        Context context;
        int page;
        String show;
        FetchNextPage(Context context,int page,String show) {
            this.context = context;
            this.page=page;
            this.show=show;
        }

        protected String doInBackground(String... urls) {
            if (nextPageURL.equals("null")) {
                pageno=1;
                return "all done";
            }
            String result = new Helpdesk().nextpageurl(show,page);
            if (result == null)
                return null;
            // DatabaseHandler databaseHandler = new DatabaseHandler(context);
            // databaseHandler.recreateTable();
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                nextPageURL = jsonObject1.getString("next_page_url");
                String data = jsonObject1.getString("data");
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    TicketOverview ticketOverview = Helper.parseTicketOverview(jsonArray, i);
                    if (ticketOverview != null) {
                        ticketOverviewList.add(ticketOverview);
                        //databaseHandler.addTicketOverview(ticketOverview);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // databaseHandler.close();
            return "success";
        }

        protected void onPostExecute(String result) {
            if (result == null)
                return;
            if (result.equals("all done")) {
                pageno=1;
                Toasty.info(context, getString(R.string.all_caught_up), Toast.LENGTH_SHORT).show();
                return;
            }
            ticketOverviewAdapter.notifyDataSetChanged();
            loading = true;
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    /**
     * Async task for getting the my tickets.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    /**
     * Once the fragment is going to be detached then
     * this method is going to be called.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        nextPageURL = "";
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
    public class TicketOverviewAdapter extends RecyclerView.Adapter<TicketOverviewAdapter.TicketViewHolder> {
        private List<TicketOverview> ticketOverviewList;
        String subject;
        int length = 0;
        private Context context;
        ArrayList<Integer> checked_items = new ArrayList<>();
        ArrayList<String> ticketSubject = new ArrayList<>();
        private SparseBooleanArray mSelectedItemsIds;
        private List<Integer> selectedIds = new ArrayList<>();


        public TicketOverviewAdapter(Context context, List<TicketOverview> ticketOverviewList) {
            this.ticketOverviewList = ticketOverviewList;
            this.context = context;
            mSelectedItemsIds = new SparseBooleanArray();
        }

        @Override
        public int getItemCount() {
            return ticketOverviewList.size();
        }
        @Override
        public int getItemViewType(int position) {
            return position;
        }
        @Override
        public void onBindViewHolder(final TicketOverviewAdapter.TicketViewHolder ticketViewHolder, final int i) {
            final TicketOverview ticketOverview = ticketOverviewList.get(i);
            String letter = String.valueOf(ticketOverview.clientName.charAt(0)).toUpperCase();

            Log.d("letter", letter);
            int id = ticketOverviewList.get(i).getTicketID();
            subject = ticketOverview.ticketSubject;
            if (subject.startsWith("=?UTF-8?Q?") && subject.endsWith("?=")) {
                String first = subject.replace("=?UTF-8?Q?", "");
                String second = first.replace("_", " ");
                String third = second.replace("=C2=A0", "");
                String fourth = third.replace("?=", "");
                String fifth = fourth.replace("=E2=80=99", "'");
                ticketViewHolder.textViewSubject.setText(fifth);
            } else {
                ticketViewHolder.textViewSubject.setText(ticketOverview.ticketSubject);
            }

            if (ticketOverview.ticketAttachments.equals("0")) {
                ticketViewHolder.attachementView.setVisibility(View.GONE);
            } else {
                int color = Color.parseColor("#808080");
                ticketViewHolder.attachementView.setVisibility(View.VISIBLE);
                ticketViewHolder.attachementView.setColorFilter(color);
            }
            if (ticketOverview.dueDate != null && !ticketOverview.dueDate.equals("null"))
//            if (Helper.compareDates(ticketOverview.dueDate) == 1) {
//                ticketViewHolder.textViewOverdue.setVisibility(View.VISIBLE);
//            } else ticketViewHolder.textViewOverdue.setVisibility(View.GONE);

                if (Helper.compareDates(ticketOverview.dueDate) == 2) {
                    ticketViewHolder.textViewduetoday.setVisibility(View.VISIBLE);
                    ticketViewHolder.textViewduetoday.setText(R.string.due_today);
                    //ticketViewHolder.textViewOverdue.setBackgroundColor(Color.parseColor("#FFD700"));
                    ((GradientDrawable) ticketViewHolder.textViewduetoday.getBackground()).setColor(Color.parseColor("#3da6d7"));
                    ticketViewHolder.textViewduetoday.setTextColor(Color.parseColor("#ffffff"));
                    //ticketViewHolder.textViewOverdue.setBackgroundColor();

                } else if (Helper.compareDates(ticketOverview.dueDate) == 1) {
                    ticketViewHolder.textViewOverdue.setVisibility(View.VISIBLE);
                    ticketViewHolder.textViewOverdue.setText(R.string.overdue);
                    //ticketViewHolder.textViewOverdue.setBackgroundColor(Color.parseColor("#ef9a9a"));
//                GradientDrawable drawable = (GradientDrawable) context.getDrawable(ticketViewHolder.textViewOverdue);
//
////set color
//                 drawable.setColor(color);
                    ((GradientDrawable) ticketViewHolder.textViewOverdue.getBackground()).setColor(Color.parseColor("#3da6d7"));
                    ticketViewHolder.textViewOverdue.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    ticketViewHolder.textViewOverdue.setVisibility(View.GONE);
                }


            ticketViewHolder.textViewTicketID.setText(ticketOverview.ticketID + "");

            ticketViewHolder.textViewTicketNumber.setText(ticketOverview.ticketNumber);
            if (ticketOverview.getClientName().startsWith("=?")) {
                String clientName = ticketOverview.getClientName().replaceAll("=?UTF-8?Q?", "");
                String newClientName = clientName.replaceAll("=E2=84=A2", "");
                String finalName = newClientName.replace("=??Q?", "");
                String name = finalName.replace("?=", "");
                String newName = name.replace("_", " ");
                Log.d("new name", newName);
                ticketViewHolder.textViewClientName.setText(newName);
            } else {
                ticketViewHolder.textViewClientName.setText(ticketOverview.clientName);

            }
            if (ticketOverview.ticketPriorityColor.equals("null")) {
                ticketViewHolder.ticketPriority.setBackgroundColor(Color.parseColor("#3da6d7"));
                ticketViewHolder.ticketPriority.setColorFilter(Color.parseColor("#3da6d7"));
            } else if (ticketOverview.ticketPriorityColor != null) {
                //ticketViewHolder.ticketPriority.setBackgroundColor(Color.parseColor(ticketOverview.ticketPriorityColor));
                ticketViewHolder.ticketPriority.setColorFilter(Color.parseColor(ticketOverview.ticketPriorityColor));
            }


//        else if (ticketOverview.ticketPriorityColor.equals("null")){
//            ticketViewHolder.ticketPriority.setBackgroundColor(Color.parseColor("#3da6d7"));
//        }
            if (!ticketOverview.ticketTime.equals("null")) {
                ticketViewHolder.textViewTime.setReferenceTime(Helper.relativeTime(ticketOverview.ticketTime));
            } else {
                ticketViewHolder.textViewTime.setVisibility(View.GONE);
            }

            if (!ticketOverview.countthread.equals("0")) {
                ticketViewHolder.countThread.setText("(" + ticketOverview.getCountthread() + ")");
            } else {
                ticketViewHolder.countThread.setVisibility(View.GONE);
            }

            switch (ticketOverview.sourceTicket) {
                case "Chat": {
                    int color = Color.parseColor("#808080");
                    ticketViewHolder.source.setImageResource(R.drawable.ic_chat_bubble_outline_black_24dp);
                    ticketViewHolder.source.setColorFilter(color);
                    break;
                }
                case "Web": {
                    int color = Color.parseColor("#808080");
                    ticketViewHolder.source.setImageResource(R.drawable.web_design);
                    ticketViewHolder.source.setColorFilter(color);
                    break;
                }
                case "Agent": {
                    int color = Color.parseColor("#808080");
                    ticketViewHolder.source.setImageResource(R.drawable.mail);
                    ticketViewHolder.source.setColorFilter(color);
                    break;
                }
                case "Email": {
                    int color = Color.parseColor("#808080");
                    ticketViewHolder.source.setImageResource(R.drawable.mail);
                    ticketViewHolder.source.setColorFilter(color);
                    break;
                }
                case "Facebook": {
                    int color = Color.parseColor("#808080");
                    ticketViewHolder.source.setImageResource(R.drawable.facebook);
                    ticketViewHolder.source.setColorFilter(color);
                    break;
                }
                case "Twitter": {
                    int color = Color.parseColor("#808080");
                    ticketViewHolder.source.setImageResource(R.drawable.twitter);
                    ticketViewHolder.source.setColorFilter(color);
                    break;
                }
                case "Call": {
                    int color = Color.parseColor("#808080");
                    ticketViewHolder.source.setImageResource(R.drawable.phone);
                    ticketViewHolder.source.setColorFilter(color);
                    break;
                }
                default:
                    ticketViewHolder.source.setVisibility(View.GONE);
                    break;
            }
            if (!ticketOverview.agentName.equals("Unassigned")) {
                ticketViewHolder.agentAssigned.setText(ticketOverview.getAgentName());
            } else {
                ticketViewHolder.agentAssigned.setText("Unassigned");
            }
//            if (checked_items.contains(id)){
//                ticketViewHolder.ticket.setCardBackgroundColor(Color.parseColor("#d6d6d6"));
//            }
//            else{
//                ticketViewHolder.ticket.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
//            }


            if (checked_items.contains(id)) {
//                AnimatorSet shrinkSet = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), R.anim.grow_from_middle);
//                shrinkSet.setTarget(ticketViewHolder.roundedImageViewProfilePic);
//                shrinkSet.start();
//                ticketViewHolder.roundedImageViewProfilePic.setImageResource(R.drawable.ic_check_circle_black_24dp);
                ticketViewHolder.imageButtonSelected.setVisibility(View.VISIBLE);
                ticketViewHolder.ticket.setCardBackgroundColor(Color.parseColor("#ededed"));
//                notifyDataSetChanged();
            } else {
                ticketViewHolder.imageButtonSelected.setVisibility(View.GONE);
                ticketViewHolder.ticket.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                if (ticketOverview.clientPicture.equals("")) {
                    ticketViewHolder.roundedImageViewProfilePic.setVisibility(View.GONE);

                } else if (ticketOverview.clientPicture.contains(".jpg") || ticketOverview.clientPicture.contains(".jpeg") || ticketOverview.clientPicture.contains(".png")) {
//    TextDrawable drawable1 = mDrawableBuilder.build(generator.getRandomColor());
                    Picasso.with(context).load(ticketOverview.getClientPicture()).transform(new CircleTransform()).into(ticketViewHolder.roundedImageViewProfilePic);


                } else {

                    ColorGenerator generator = ColorGenerator.MATERIAL;
                    TextDrawable drawable = TextDrawable.builder()
                            .buildRound(letter, generator.getRandomColor());
                    //ticketViewHolder.roundedImageViewProfilePic.setAlpha(0.6f);
                    ticketViewHolder.roundedImageViewProfilePic.setImageDrawable(drawable);
                }

            }
            if (!ticketOverview.departmentname.equals("")){
                ticketViewHolder.textViewDepartment.setText(ticketOverview.getDepartmentname());
            }

            if (ticketOverview.clientPicture.equals("")){
                ticketViewHolder.roundedImageViewProfilePic.setVisibility(View.GONE);

            }
            else if (ticketOverview.clientPicture.contains(".jpg")){
                Picasso.with(context).load(ticketOverview.getClientPicture()).transform(new CircleTransform()).into(ticketViewHolder.roundedImageViewProfilePic);


            }
            else{
                ColorGenerator generator = ColorGenerator.MATERIAL;
                TextDrawable drawable = TextDrawable.builder()
                        .buildRound(letter,generator.getRandomColor());
                //ticketViewHolder.roundedImageViewProfilePic.setAlpha(0.6f);
                ticketViewHolder.roundedImageViewProfilePic.setImageDrawable(drawable);

            }
            if (!ticketOverview.priorityName.equals("")){
                ticketViewHolder.textViewpriorityName.setText(ticketOverview.priorityName);
            }
            else{
                ticketViewHolder.textViewpriorityName.setText(R.string.not_available);
            }

            ticketViewHolder.ticket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mActionMode != null) {
                        onListItemSelect(i);
                    }
                    else {
                        Intent intent = new Intent(v.getContext(), TicketDetailActivity.class);
                        Prefs.putString("cameFromNotification", "none");
                        Prefs.putString("ticketThread", "");
                        intent.putExtra("ticket_id", ticketOverview.ticketID + "");
                        Prefs.putString("TICKETid", ticketOverview.ticketID + "");
                        Prefs.putString("ticketId", ticketOverview.ticketID + "");
                        Prefs.putString("ticketstatus", ticketOverview.getTicketStatus());
                        intent.putExtra("ticket_number", ticketOverview.ticketNumber);
                        intent.putExtra("ticket_opened_by", ticketOverview.clientName);
                        intent.putExtra("ticket_subject", ticketOverview.ticketSubject);
                        Log.d("clicked", "onRecyclerView");
                        v.getContext().startActivity(intent);
                    }
                }
            });


            ticketViewHolder.ticket.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Prefs.putString("ticketstatus", ticketOverview.ticketStatus);
                    onListItemSelect(i);
                    length++;
                    Log.d("noofitems", "" + length);
                    Prefs.putInt("NoOfItems", length);

                    return true;
                }
            });

        }

        private void onListItemSelect(int position) {
            ticketOverviewAdapter.toggleSelection(position);//Toggle the selection

            boolean hasCheckedItems = ticketOverviewAdapter.getSelectedCount() > 0;//Check if any items are already selected or not


            if (hasCheckedItems && mActionMode == null)
                // there are some selected items, start the actionMode
                mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new Toolbar_ActionMode_Callback(getActivity(), ticketOverviewAdapter, null, ticketOverviewList, false));
            else if (!hasCheckedItems && mActionMode != null)
                // there no selected items, finish the actionMode
                mActionMode.finish();

            if (mActionMode != null)
                //set action mode title on item selection
                mActionMode.setTitle(String.valueOf(ticketOverviewAdapter
                        .getSelectedCount()) + " ticket selected");
            textViewShowingCount.setText(ticketOverviewAdapter.getSelectedCount() + " ticket selected");


        }

        public void toggleSelection(int position) {
            selectView(position, !mSelectedItemsIds.get(position));
        }


        //Remove selected selections


        //Put or delete selected position into SparseBooleanArray
        public void selectView(int position, boolean value) {
            TicketOverview ticketOverview = ticketOverviewList.get(position);
            if (value) {
                ticketSubject.add(ticketOverview.ticketSubject);
                checked_items.add(ticketOverview.getTicketID());
                status=ticketOverview.getTicketStatus();
                Log.d("status",status);
                Log.d("ticketsubject", ticketSubject.toString());
                Log.d("checkeditems", checked_items.toString().replace(" ", ""));
                Prefs.putString("tickets", checked_items.toString().replace(" ", ""));
                Prefs.putString("TicketSubject", ticketSubject.toString());
                mSelectedItemsIds.put(position, value);
            } else {
                int pos = checked_items.indexOf(ticketOverview.getTicketID());
                int pos1 = ticketSubject.indexOf(ticketOverview.getTicketSubject());
                try {
                    checked_items.remove(pos);
                    ticketSubject.remove(pos1);
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                Log.d("ticketsubject", ticketSubject.toString());
                Log.d("checkeditems", checked_items.toString().replace(" ", ""));
                Prefs.putInt("totalticketselected", length);
                Log.d("checkeditems", "" + checked_items);
                Prefs.putInt("NoOfItems", length);
                Prefs.putString("tickets", checked_items.toString().replace(" ", ""));
                Prefs.putString("TicketSubject", ticketSubject.toString());
                mSelectedItemsIds.delete(position);
            }

            notifyDataSetChanged();
        }

        public void setSelectedIds(List<Integer> selectedIds) {
            this.selectedIds = selectedIds;
            notifyDataSetChanged();
        }

        public void removeSelection() {
            mSelectedItemsIds = new SparseBooleanArray();
            notifyDataSetChanged();
        }

        //Get total selected count
        public int getSelectedCount() {
            return mSelectedItemsIds.size();
        }
        public TicketOverview getItem(int position) {
            return ticketOverviewList.get(position);
        }

        @Override
        public TicketOverviewAdapter.TicketViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.card_ticket, viewGroup, false);
            return new TicketOverviewAdapter.TicketViewHolder(itemView);
        }
        public class TicketViewHolder extends RecyclerView.ViewHolder {
            CardView ticket;
            ImageView roundedImageViewProfilePic;
            TextView textViewTicketID;
            TextView textViewTicketNumber;
            TextView textViewClientName;
            TextView textViewSubject;
            RelativeTimeTextView textViewTime;
            TextView textViewOverdue;
            ImageView ticketPriority;
            ImageView attachementView;
            CheckBox checkBox1;
            ImageView countCollaborator;
            ImageView source;
            TextView countThread;
            TextView agentAssigned;
            TextView textViewduetoday;
            TextView textViewpriorityName;
            ImageButton imageButtonSelected;
            TextView textViewDepartment;
            TicketViewHolder(View v) {
                super(v);
                ticket = v.findViewById(R.id.ticket);
                attachementView = (ImageView) v.findViewById(R.id.attachment_icon);
                textViewpriorityName=v.findViewById(R.id.priority_viewText);
                ticketPriority = (ImageView) v.findViewById(R.id.priority_view);
                roundedImageViewProfilePic = (ImageView) v.findViewById(R.id.imageView_default_profile);
                textViewTicketID = (TextView) v.findViewById(R.id.textView_ticket_id);
                textViewTicketNumber = (TextView) v.findViewById(R.id.textView_ticket_number);
                textViewClientName = (TextView) v.findViewById(R.id.textView_client_name);
                textViewSubject = (TextView) v.findViewById(R.id.textView_ticket_subject);
                textViewTime = (RelativeTimeTextView) v.findViewById(R.id.textView_ticket_time);
                textViewOverdue = (TextView) v.findViewById(R.id.overdue_view);
                checkBox1 = (CheckBox) v.findViewById(R.id.checkbox);
                countCollaborator = (ImageView) v.findViewById(R.id.collaborator);
                countThread = (TextView) v.findViewById(R.id.countthread);
                source = (ImageView) v.findViewById(R.id.source);
                agentAssigned = (TextView) v.findViewById(R.id.agentassigned);
                //agentAssignedImage = (ImageView) v.findViewById(R.id.agentAssigned);
                textViewduetoday = (TextView) v.findViewById(R.id.duetoday);
                imageButtonSelected=v.findViewById(R.id.selectedTicket);
                textViewDepartment=v.findViewById(R.id.textViewDepartmentName);

            }

        }

    }
    public class Toolbar_ActionMode_Callback implements android.support.v7.view.ActionMode.Callback {

        private Context context;
        private TicketOverviewAdapter recyclerView_adapter;
        private ArrayList<TicketOverview> message_models;
        private boolean isListViewFragment;


        public Toolbar_ActionMode_Callback(Context context, TicketOverviewAdapter ticketOverviewAdapter, TicketOverviewAdapter recyclerView_adapter, List<TicketOverview> message_models, boolean b) {
            this.context = context;
            this.recyclerView_adapter = recyclerView_adapter;
            this.message_models = (ArrayList<TicketOverview>) message_models;
            this.isListViewFragment = isListViewFragment;
        }

        @Override
        public boolean onCreateActionMode(android.support.v7.view.ActionMode mode, Menu menu) {
            RelativeLayout layout = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.actionmode_view, null);
            mode.setCustomView(layout);
            textViewShowingCount=layout.findViewById(R.id.ticketcount);
            imageViewssignTicket=layout.findViewById(R.id.assignTickets);
            imageViewChangingStatus=layout.findViewById(R.id.changingStatus);
            imageViewssignTicket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (Prefs.getString("tickets", null).equals("null") || Prefs.getString("tickets", null).equals("[]")) {
                            Toasty.info(getActivity(), getString(R.string.noticket), Toast.LENGTH_LONG).show();

                        }
                        String ticketId = Prefs.getString("tickets", null);
                        List<String> items = new ArrayList<String>(Arrays.asList(ticketId.split("\\s*,\\s*")));
                        int itemCount = items.size();
                        Intent intent = new Intent(getActivity(), MultiAssigningActivity.class);
                        startActivity(intent);

                    } catch (NullPointerException e) {
                        Toasty.info(getActivity(), getString(R.string.noticket), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            });

            imageViewChangingStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyBottomSheetDialogChangeStatus myBottomSheetDialog = new MyBottomSheetDialogChangeStatus(getActivity());
                    myBottomSheetDialog.show();
                }
            });
            return true;
        }

        @Override
        public boolean onPrepareActionMode(android.support.v7.view.ActionMode mode, Menu menu) {

            //Sometimes the meu will not be visible so for that we need to set their visibility manually in this method
            //So here show action menu according to SDK Levels
            if (Build.VERSION.SDK_INT < 11) {
                //MenuItemCompat.setShowAsAction(menu.findItem(R.id.mergeticket), MenuItemCompat.SHOW_AS_ACTION_NEVER);

//            MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_copy), MenuItemCompat.SHOW_AS_ACTION_NEVER);
//            MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_forward), MenuItemCompat.SHOW_AS_ACTION_NEVER);
            }

//
            return true;
        }

        @Override
        public boolean onActionItemClicked(android.support.v7.view.ActionMode mode, MenuItem item) {
            StringBuffer stringBuffer = new StringBuffer();
            try {
                if (item != null) {
                    item.getSubMenu().clearHeader();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            for (int i=0;i<statusItems.size();i++) {
                Data data = statusItems.get(i);
                if (data.getName().equals(item.toString())) {
                    id = data.getID();
                    Log.d("ID", "" + id);

                    if (status.equalsIgnoreCase(item.toString())) {
                        Toasty.warning(getActivity(), "Ticket is already in " + item.toString() + " state", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            if (!Prefs.getString("tickets", null).isEmpty()) {
                                String tickets = Prefs.getString("tickets", null);
                                int pos = tickets.indexOf("[");
                                int pos1 = tickets.lastIndexOf("]");
                                String text1 = tickets.substring(pos + 1, pos1);
                                String[] namesList = text1.split(",");
                                for (String name : namesList) {
                                    stringBuffer.append(name + ",");
                                }
                                int pos2 = stringBuffer.toString().lastIndexOf(",");
                                ticket = stringBuffer.toString().substring(0, pos2);

                                Log.d("tickets", ticket);
                                android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(getActivity());

                                // Setting Dialog Title
                                alertDialog.setTitle(getString(R.string.changingStatus));

                                // Setting Dialog Message
                                alertDialog.setMessage(getString(R.string.statusConfirmation));
                                // Setting Icon to Dialog
                                alertDialog.setIcon(R.mipmap.ic_launcher);

                                // Setting Positive "Yes" Button
                                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to invoke YES event
                                        //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                                        progressDialog.show();
                                        progressDialog.setMessage(getString(R.string.pleasewait));
                                        new StatusChange(ticket, id).execute();

                                    }
                                });

                                // Setting Negative "NO" Button
                                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to invoke NO event
                                        //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                        dialog.cancel();
                                    }
                                });

                                // Showing Alert Message
                                alertDialog.show();
                                return true;
                            } else {
                                Toasty.info(getActivity(), getString(R.string.noticket), Toast.LENGTH_LONG).show();
                                return false;
                            }
                        } catch (NullPointerException e) {
                            Toasty.info(getActivity(), getString(R.string.noticket), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                }
            }

            switch (item.getItemId()) {
//                case R.id.mergeticket:
////                Toast.makeText(, "You selected close menu.", Toast.LENGTH_SHORT).show();//Show toast
//                    try {
//                        if (Prefs.getString("tickets", null).equals("null") || Prefs.getString("tickets", null).equals("[]")) {
//                            Toasty.info(getActivity(), getString(R.string.noticket), Toast.LENGTH_LONG).show();
//                            return false;
//                        }
//                        String ticketId = Prefs.getString("tickets", null);
//                        List<String> items = new ArrayList<String>(Arrays.asList(ticketId.split("\\s*,\\s*")));
//                        int itemCount = items.size();
//                        if (itemCount == 1) {
//                            Toasty.info(getActivity(), getString(R.string.selectMultipleTicket), Toast.LENGTH_LONG).show();
//                            return false;
//                        } else {
//                            Intent intent = new Intent(getActivity(), TicketMergeActtivity.class);
//                            startActivity(intent);
//                        }
//
////            Intent intent = new Intent(getActivity(), TicketMergeActtivity.class);
////            startActivity(intent);
//
//                    } catch (NullPointerException e) {
//                        Toasty.info(getActivity(), getString(R.string.noticket), Toast.LENGTH_LONG).show();
//                        e.printStackTrace();
//                    }
////                    Log.d("clicked on closed","closed");
//                    setNullToActionMode();
//                    mode.finish();
//
//                    //mode.finish();
//                    break;
//                case R.id.action_statusClosed:
//                    try {
//                        if (!Prefs.getString("tickets", null).isEmpty()) {
//                            String tickets = Prefs.getString("tickets", null);
//                            int pos = tickets.indexOf("[");
//                            int pos1 = tickets.lastIndexOf("]");
//                            String text1 = tickets.substring(pos + 1, pos1);
//                            String[] namesList = text1.split(",");
//                            for (String name : namesList) {
//                                stringBuffer.append(name + ",");
//                            }
//                            int pos2 = stringBuffer.toString().lastIndexOf(",");
//                            ticket = stringBuffer.toString().substring(0, pos2);
//
//                            Log.d("tickets", ticket);
//                            try {
//                                new StatusChange(ticket, Integer.parseInt(Prefs.getString("closedid", null))).execute();
//                                Prefs.putString("tickets", null);
//                                progressDialog.show();
//                                progressDialog.setMessage(getString(R.string.pleasewait));
//                            } catch (NumberFormatException e) {
//                                e.printStackTrace();
//
//                            }
//                            return true;
//                        } else {
//                            Toasty.info(getActivity(), getString(R.string.noticket), Toast.LENGTH_LONG).show();
//                            return false;
//                        }
//                    } catch (NullPointerException e) {
//                        Toasty.info(getActivity(), getString(R.string.noticket), Toast.LENGTH_LONG).show();
//                        e.printStackTrace();
//                    }
//                    if (!Prefs.getString("tickets", null).equals("") || !Prefs.getString("tickets", null).equals("null") || !Prefs.getString("tickets", null).equals(null)) {
//
//
//                        Log.d("tickets", ticket);
//                        if (ticket.equals("") || ticket.equals(null)) {
//                            Toasty.warning(getActivity(), getString(R.string.noticket), Toast.LENGTH_SHORT).show();
//                            return false;
//                        } else {
//
//
//                        }
//
//                    }else {
//
//                    }
//                    break;
//                case R.id.action_statusResolved:
//                    try {
//                        if (!Prefs.getString("tickets", null).isEmpty()) {
//                            String tickets = Prefs.getString("tickets", null);
//                            int pos = tickets.indexOf("[");
//                            int pos1 = tickets.lastIndexOf("]");
//                            String text1 = tickets.substring(pos + 1, pos1);
//                            String[] namesList = text1.split(",");
//                            for (String name : namesList) {
//                                stringBuffer.append(name + ",");
//                            }
//                            int pos2 = stringBuffer.toString().lastIndexOf(",");
//                            ticket = stringBuffer.toString().substring(0, pos2);
//
//                            Log.d("tickets", ticket);
//                            try {
//                                new StatusChange(ticket, Integer.parseInt(Prefs.getString("resolvedid", null))).execute();
//                                Prefs.putString("tickets", null);
//                                progressDialog.show();
//                                progressDialog.setMessage(getString(R.string.pleasewait));
//                            } catch (NumberFormatException e) {
//                                e.printStackTrace();
//
//                            }
//                            return true;
//                        } else {
//                            Toasty.info(getActivity(), getString(R.string.noticket), Toast.LENGTH_LONG).show();
//                            return false;
//                        }
//                    } catch (NullPointerException e) {
//                        Toasty.info(getActivity(), getString(R.string.noticket), Toast.LENGTH_LONG).show();
//                        e.printStackTrace();
//                    }
//                    break;
                case R.id.deleteticket:
                    try {
                        if (!Prefs.getString("tickets", null).isEmpty()) {
                            String tickets = Prefs.getString("tickets", null);
                            int pos = tickets.indexOf("[");
                            int pos1 = tickets.lastIndexOf("]");
                            String text1 = tickets.substring(pos + 1, pos1);
                            Log.d("TEXT1", text1);
                            String[] namesList = text1.split(",");
                            for (String name : namesList) {
                                stringBuffer.append("&id[]=").append(name);
                            }
//                    int pos2 = stringBuffer.toString().lastIndexOf(",");
//                    ticket = stringBuffer.toString().substring(0, pos2);
                            ticket = stringBuffer.toString();
                            Log.d("tickets", ticket);
                            try {
                                if (ticket.equals("&id[]=")){
                                    Toasty.info(getActivity(), getString(R.string.noticket), Toast.LENGTH_LONG).show();
                                    return false;
                                }
                                else{

                                    android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(getActivity());

                                    // Setting Dialog Title
                                    alertDialog.setTitle(getString(R.string.deleteTickets));

                                    // Setting Dialog Message
                                    alertDialog.setMessage(getString(R.string.deleteConfirm));
                                    // Setting Icon to Dialog
                                    alertDialog.setIcon(R.mipmap.ic_launcher);

                                    // Setting Positive "Yes" Button
                                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Write your code here to invoke YES event
                                            //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                                            new DeleteTicket(ticket.trim()).execute();
                                            progressDialog.show();
                                            progressDialog.setMessage(getString(R.string.pleasewait));
                                        }
                                    });

                                    // Setting Negative "NO" Button
                                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Write your code here to invoke NO event
                                            //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                            dialog.cancel();
                                        }
                                    });

                                    // Showing Alert Message
                                    alertDialog.show();

//                                    new DeleteTicket(ticket.trim()).execute();
//                                    progressDialog.show();
//                                    progressDialog.setMessage(getString(R.string.pleasewait));
                                }

                            } catch (NumberFormatException e) {
                                e.printStackTrace();

                            }
                            return true;
                        } else {
                            Toasty.info(getActivity(), getString(R.string.noticket), Toast.LENGTH_LONG).show();
                            return false;
                        }
                    } catch (NullPointerException e) {
                        Toasty.info(getActivity(), getString(R.string.noticket), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                case R.id.assignticket:
                    try {
                        if (Prefs.getString("tickets", null).equals("null") || Prefs.getString("tickets", null).equals("[]")) {
                            Toasty.info(getActivity(), getString(R.string.noticket), Toast.LENGTH_LONG).show();
                            return false;
                        }
                        String ticketId = Prefs.getString("tickets", null);
                        List<String> items = new ArrayList<String>(Arrays.asList(ticketId.split("\\s*,\\s*")));
                        int itemCount = items.size();
                        if (itemCount == 1) {
                            Toasty.info(getActivity(), getString(R.string.multiAssign), Toast.LENGTH_LONG).show();
                            return false;
                        } else {
                            Intent intent = new Intent(getActivity(), MultiAssigningActivity.class);
                            startActivity(intent);
                        }

//            Intent intent = new Intent(getActivity(), TicketMergeActtivity.class);
//            startActivity(intent);

                    } catch (NullPointerException e) {
                        Toasty.info(getActivity(), getString(R.string.noticket), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

//                case R.id.action_statusOpen:
//                    try {
//                        if (!Prefs.getString("tickets", null).isEmpty()) {
//                            String tickets = Prefs.getString("tickets", null);
//                            int pos = tickets.indexOf("[");
//                            int pos1 = tickets.lastIndexOf("]");
//                            String text1 = tickets.substring(pos + 1, pos1);
//                            String[] namesList = text1.split(",");
//                            for (String name : namesList) {
//                                stringBuffer.append(name + ",");
//                            }
//                            int pos2 = stringBuffer.toString().lastIndexOf(",");
//                            ticket = stringBuffer.toString().substring(0, pos2);
//
//                            Log.d("tickets", ticket);
//                            try {
//                                new StatusChange(ticket, Integer.parseInt(Prefs.getString("openid", null))).execute();
//                                progressDialog.show();
//                                progressDialog.setMessage(getString(R.string.pleasewait));
//                            } catch (NumberFormatException e) {
//                                e.printStackTrace();
//
//                            }
//                            return true;
//                        } else {
//                            Toasty.info(getActivity(), getString(R.string.noticket), Toast.LENGTH_LONG).show();
//                            return false;
//                        }
//                    } catch (NullPointerException e) {
//                        Toasty.info(getActivity(), getString(R.string.noticket), Toast.LENGTH_LONG).show();
//                        e.printStackTrace();
//                    }


            }
            return false;
        }


        @Override
        public void onDestroyActionMode(android.support.v7.view.ActionMode mode) {

            //When action mode destroyed remove selected selections and set action mode to null
            //First check current fragment action mode
            Log.d("onDestroyActionMode","CAME HERE");
            TrashTickets trashTickets=new TrashTickets();
            //recyclerView_adapter.removeSelection();
            trashTickets.setNullToActionMode();
            ticketOverviewAdapter = new TicketOverviewAdapter(getContext(), ticketOverviewList);
            recyclerView.setAdapter(ticketOverviewAdapter);
            ticketOverviewAdapter.removeSelection();
            setNullToActionMode();
//        ((InboxTickets) inboxTickets).setNullToActionMode();
            mode.finish();

            // remove selection
//            Fragment recyclerFragment = new MainActivity().getFragment(1);//Get recycler fragment
//            if (recyclerFragment != null)
//                ((RecyclerView_Fragment) recyclerFragment).setNullToActionMode();//Set action mode null

        }
    }
    @Override
    public void onResume() {
        super.onResume();
        new FetchFirst(getActivity(), page).execute();
        try {
            mActionMode.finish();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    public class MyBottomSheetDialogChangeStatus extends BottomSheetDialog {

        Context context;
        ListView listView;
        MyBottomSheetDialogChangeStatus(@NonNull Context context) {
            super(context);
            this.context = context;
            createChange();
        }

        public void createChange() {
            View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_change_status, null);
            setContentView(bottomSheetView);
            listView=bottomSheetView.findViewById(R.id.listViewStatus);
            BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
            BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    // do something
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    // do something
                }
            };
            ArrayAdapter adapter = new ArrayAdapter<Data>(getActivity(),
                    R.layout.listview_row_status,statusItems);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    status = Prefs.getString("ticketstatus", null);
                    Data data=statusItems.get(i);
                    if (data.getName().equalsIgnoreCase(status)){
                        Toasty.warning(getActivity(), "Ticket is already in "+listView.getAdapter().getItem(i).toString()+" state", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else{
                        StringBuffer stringBuffer=new StringBuffer();
                        String tickets = Prefs.getString("tickets", null);
                        int pos = tickets.indexOf("[");
                        int pos1 = tickets.lastIndexOf("]");
                        String text1 = tickets.substring(pos + 1, pos1);
                        String[] namesList = text1.split(",");
                        for (String name : namesList) {
                            stringBuffer.append(name + ",");
                        }
                        int pos2 = stringBuffer.toString().lastIndexOf(",");
                        ticket = stringBuffer.toString().substring(0, pos2);

                        Log.d("tickets", ticket);
                        id=data.getID();
                        new BottomDialog.Builder(getActivity())
                                .setTitle(getString(R.string.changingStatus))
                                .setContent(getString(R.string.statusConfirmation))
                                .setPositiveText("YES")
                                .setNegativeText("NO")
                                .setPositiveBackgroundColorResource(R.color.white)
                                //.setPositiveBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary)
                                .setPositiveTextColorResource(R.color.faveo)
                                .setNegativeTextColor(R.color.black)
                                //.setPositiveTextColor(ContextCompat.getColor(this, android.R.color.colorPrimary)
                                .onPositive(new BottomDialog.ButtonCallback() {
                                    @Override
                                    public void onClick(BottomDialog dialog) {
                                        if (InternetReceiver.isConnected()){
                                            progressDialog.show();
                                            progressDialog.setMessage(getString(R.string.pleasewait));
                                            new StatusChange(ticket, id).execute();
                                        }
                                    }
                                }).onNegative(new BottomDialog.ButtonCallback() {
                            @Override
                            public void onClick(@NonNull BottomDialog bottomDialog) {
                                bottomDialog.dismiss();
                            }
                        })
                                .show();
                    }
                }
            });

        }

    }

}
