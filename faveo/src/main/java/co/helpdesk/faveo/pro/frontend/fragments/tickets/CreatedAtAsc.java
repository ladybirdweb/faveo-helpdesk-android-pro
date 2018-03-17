package co.helpdesk.faveo.pro.frontend.fragments.tickets;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
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
import co.helpdesk.faveo.pro.frontend.activities.TicketMergeActtivity;
import co.helpdesk.faveo.pro.frontend.adapters.TicketOverviewAdapter;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.pro.model.TicketOverview;
import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreatedAtAsc extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.cardList)
    ShimmerRecyclerView recyclerView;
    ProgressDialog progressDialog;
    private android.support.v7.view.ActionMode mActionMode;
    @BindView(R.id.empty_view)
    TextView empty_view;
    @BindView(R.id.noiternet_view)
    TextView noInternet_view;
    int currentPage = 1;
    static String nextPageURL = "";
    View rootView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.totalcount)
    TextView textView;
    String title;
    TicketOverviewAdapter ticketOverviewAdapter;
    List<TicketOverview> ticketOverviewList = new ArrayList<>();
    String show,dept;
    private boolean loading = true;
    int total;
    int assigned=1;
    int pageNo=1;
    int pastVisibleItems, visibleItemCount, totalItemCount;
    String url;
    public String mParam1;
    public String mParam2;
    String ticket;
    int pageno=1;

    private CreatedAtAsc.OnFragmentInteractionListener mListener;

    public static CreatedAtAsc newInstance(String param1, String param2) {
        CreatedAtAsc fragment = new CreatedAtAsc();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public CreatedAtAsc() {
        // Required empty public constructor
    }


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
            Prefs.putString("cameFromNotification","false");
            rootView = inflater.inflate(R.layout.fragment_recycler, container, false);
            ButterKnife.bind(this, rootView);
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait");
            url=Prefs.getString("URLFiltration",null);
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
            Toolbar toolbar1 = (Toolbar) rootView.findViewById(R.id.toolbarfilteration);
            toolbar1.setVisibility(View.VISIBLE);
            toolbar1.setOverflowIcon(getResources().getDrawable(R.drawable.ic_if_filter_383135));

            toolbar1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(getActivity(), "clicked on toolbar", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), TicketFilter.class);
                    startActivity(intent);

                }
            });
            Toolbar toolbar= (Toolbar) rootView.findViewById(R.id.toolbar2);
//        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            TextView textView= (TextView) toolbar.findViewById(R.id.toolbartextview);
//        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            textView.setText(getString(R.string.createdat));
            toolbar.setVisibility(View.VISIBLE);
            //toolbar.setTitle(getString(R.string.createdat));
            toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_sort_black_24dp));
            toolbar.setTitleTextColor(Color.parseColor("#3da6d7"));
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
                    Fragment fragment = null;
                    title = getString(R.string.app_name);
                    if (item.getItemId()==R.id.due_ascending){
                        title = getString(R.string.duebyasc);
                        fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
                        if (fragment == null)
                            fragment = new DueByAsc();
                        if (fragment != null) {
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.container_body, fragment);
                            // fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                        return true;
                    }
                    if (item.getItemId()==R.id.due_descending){
                        Toasty.info(getActivity(),getString(R.string.alreadysorted), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    if (item.getItemId()==R.id.created_ascending){
                        Toasty.info(getActivity(),getString(R.string.alreadysorted),Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    if (item.getItemId()==R.id.created_descending){
                        title = getString(R.string.createdat);
                        fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
                        if (fragment == null)
                            fragment = new CreatedAtDesc();
                        if (fragment != null) {
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.container_body, fragment);
                            // fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                        return true;
                    }
                    if (item.getItemId()==R.id.ticketnumber_ascending){
                        //Toast.makeText(getActivity(), "due in ascending", Toast.LENGTH_SHORT).show();
                        title = getString(R.string.sortbyticketnoasc);
                        fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
                        if (fragment == null)
                            fragment = new SortByTicketNumberAscending();
                        if (fragment != null) {
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.container_body, fragment);
                            // fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                        return true;
                    }
                    if (item.getItemId()==R.id.ticketnumber_descending){
                        title = getString(R.string.sortbyticketnodesc);
                        fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
                        if (fragment == null)
                            fragment = new SortByTicketNumberAscending();
                        if (fragment != null) {
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.container_body, fragment);
                            // fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                        return true;
                    }
                    if (item.getItemId()==R.id.priorityasc){
                        title = getString(R.string.sortbypriorityasc);
                        fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
                        if (fragment == null)
                            fragment = new SortByTicketPriorityAsc();
                        if (fragment != null) {
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.container_body, fragment);
                            // fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                        return true;
                    }
                    if (item.getItemId()==R.id.prioritydesc){
                        title = getString(R.string.sortbyprioritydesc);
                        fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
                        if (fragment == null)
                            fragment = new SortByTicketPriorityDesc();
                        if (fragment != null) {
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.container_body, fragment);
                            // fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                        return true;
                    }
                    if (item.getItemId()==R.id.updatedatasc){
                        title = getString(R.string.updatedat);
                        fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
                        if (fragment == null)
                            fragment = new UpdatedAtAsc();
                        if (fragment != null) {
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.container_body, fragment);
                            // fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                        return true;
                    }
                    if (item.getItemId()==R.id.updatedatdesc){
                        title = getString(R.string.updatedat);
                        fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
                        if (fragment == null)
                            fragment = new UpdatedAtDesc();
                        if (fragment != null) {
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.container_body, fragment);
                            // fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                        return true;
                    }



                    return false;
                }
            });


            swipeRefresh.setColorSchemeResources(R.color.faveo_blue);
            if (InternetReceiver.isConnected()) {
                noInternet_view.setVisibility(View.GONE);
                show =Prefs.getString("source",null);
                if (show.equals("1")){
                    dept="mytickets";
                    progressDialog.show();
                    new FetchFirstAscending(getActivity(),dept,"created_at","ASC",1).execute();
                }
                else if (show.equals("2")){
                    dept="inbox";
                    assigned=0;
                    progressDialog.show();
                    new FetchFirstAscending(getActivity(),dept+"&assigned=0","created_at","ASC",1).execute();
                }
                else if (show.equals("3")){
                    dept="closed";
                    progressDialog.show();
                    new FetchFirstAscending(getActivity(),dept,"created_at","ASC",1).execute();
                }
                else if (show.equals("4")){
                    dept="trash";
                    progressDialog.show();
                    new FetchFirstAscending(getActivity(),dept,"created_at","ASC",1).execute();
                }
                else if (show.equals("5")){
                    dept="inbox";
                    progressDialog.show();
                    new FetchFirstAscending(getActivity(),dept,"created_at","ASC",1).execute();
                }
                else if (show.equals("6")&&!url.equals(null)){

                    progressDialog.show();
                    new FetchFirstFilter(getActivity(),url+"&sort-by=created_at&order=ASC",pageNo).execute();
                    //Toast.makeText(getActivity(), "came here", Toast.LENGTH_SHORT).show();
                }


                // swipeRefresh.setRefreshing(true);
//                progressDialog.show();
//                new FetchFirstAscending(getActivity(),"inbox","created_at","ASC",1).execute();
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
                        show =Prefs.getString("source",null);
                        if (show.equals("1")){
                            dept="mytickets";
                            //progressDialog.show();
                            new FetchFirstAscending(getActivity(),dept,"created_at","ASC",1).execute();
                        }
                        else if (show.equals("2")){
                            dept="inbox";
                            assigned=0;
                            //progressDialog.show();
                            new FetchFirstAscending(getActivity(),dept+"&assigned=0","created_at","ASC",1).execute();
                        }
                        else if (show.equals("3")){
                            dept="closed";
                            //progressDialog.show();
                            new FetchFirstAscending(getActivity(),dept,"created_at","ASC",1).execute();
                        }
                        else if (show.equals("4")){
                            dept="trash";
                            //progressDialog.show();
                            new FetchFirstAscending(getActivity(),dept,"created_at","ASC",1).execute();
                        }
                        else if (show.equals("5")){
                            dept="inbox";
                            //progressDialog.show();
                            new FetchFirstAscending(getActivity(),dept,"created_at","ASC",1).execute();
                        }
                        else if (show.equals("6")&&!url.equals(null)){

                            //progressDialog.show();
                            new FetchFirstFilter(getActivity(),url+"&sort-by=created_at&order=ASC",pageNo).execute();
                            //Toast.makeText(getActivity(), "came here", Toast.LENGTH_SHORT).show();
                        }

                        //new FetchFirstAscending(getActivity(),dept,"created_at","ASC",1).execute();
                    } else {
                        recyclerView.setVisibility(View.INVISIBLE);
                        swipeRefresh.setRefreshing(false);
                        empty_view.setVisibility(View.GONE);
                        noInternet_view.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
//        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.inbox));
        return rootView;
    }
    public void setNullToActionMode() {
        Log.d("Inbox Ticket","Came from toolbar action mode");
        if (mActionMode != null)
            mActionMode = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_inbox, menu);

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
        StringBuffer stringBuffer = new StringBuffer();
        if (id == R.id.action_statusClosed) {

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
                    try {
                        new StatusChange(ticket, Integer.parseInt(Prefs.getString("closedid", null))).execute();
                        progressDialog.show();
                        progressDialog.setMessage(getString(R.string.pleasewait));
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
//            if (!Prefs.getString("tickets", null).equals("") || !Prefs.getString("tickets", null).equals("null") || !Prefs.getString("tickets", null).equals(null)) {
//
//
//                Log.d("tickets", ticket);
//                if (ticket.equals("") || ticket.equals(null)) {
//                    Toasty.warning(getActivity(), getString(R.string.noticket), Toast.LENGTH_SHORT).show();
//                    return false;
//                } else {
//
//
//
//                }
//
//            }
        } else if (id == R.id.action_statusResolved) {
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
                    try {
                        new StatusChange(ticket, Integer.parseInt(Prefs.getString("resolvedid", null))).execute();
                        progressDialog.show();
                        progressDialog.setMessage(getString(R.string.pleasewait));
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
        } else if (id == R.id.action_statusDeleted) {
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
                    try {
                        new StatusChange(ticket, Integer.parseInt(Prefs.getString("closedid", null))).execute();
                        progressDialog.show();
                        progressDialog.setMessage(getString(R.string.pleasewait));
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
        } else if (id == R.id.action_noti) {
            Intent intent = new Intent(getActivity(), NotificationActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.actionsearch) {

            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.mergeticket) {
            try {
                if (Prefs.getString("tickets", null).equals("null") || Prefs.getString("tickets", null).equals("[]")) {
                    Toasty.info(getActivity(), getString(R.string.noticket), Toast.LENGTH_LONG).show();
                    return false;
                }
                String ticketId = Prefs.getString("tickets", null);
                List<String> items = new ArrayList<String>(Arrays.asList(ticketId.split("\\s*,\\s*")));
                int itemCount = items.size();
                if (itemCount == 1) {
                    Toasty.info(getActivity(), getString(R.string.selectMultipleTicket), Toast.LENGTH_LONG).show();
                    return false;
                } else {
                    Intent intent = new Intent(getActivity(), TicketMergeActtivity.class);
                    startActivity(intent);
                }

//            Intent intent = new Intent(getActivity(), TicketMergeActtivity.class);
//            startActivity(intent);

            } catch (NullPointerException e) {
                Toasty.info(getActivity(), getString(R.string.noticket), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }


        }
        else if (id==R.id.assignticket){
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
                JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                //String message1=jsonObject2.getString("ticket_id");
                String message2 = jsonObject1.getString("message");


                if (message2.contains("Status changed to Deleted")) {
                    Toasty.success(getActivity(), getString(R.string.status_deleted), Toast.LENGTH_LONG).show();
                    Prefs.putString("tickets", null);
                    //Prefs.putString("ticketstatus", "Deleted");
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                } else if (message2.contains("Status changed to Open")) {
                    Toasty.success(getActivity(), getString(R.string.status_opened), Toast.LENGTH_LONG).show();
                    Prefs.putString("tickets", null);
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                } else if (message2.contains("Status changed to Closed")) {
                    Toasty.success(getActivity(), getString(R.string.status_closed), Toast.LENGTH_LONG).show();
                    Prefs.putString("tickets", null);
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                } else if (message2.contains("Status changed to Resolved")) {
                    Toasty.success(getActivity(), getString(R.string.status_resolved), Toast.LENGTH_LONG).show();
                    Prefs.putString("tickets", null);
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }


            }catch (JSONException | NullPointerException e) {
                e.printStackTrace();

            }




        }


    }
    /**
     * Async task for getting the my tickets.
     */
    private class FetchFirstAscending extends AsyncTask<String, Void, String> {
        Context context;
        int page;
        String show;
        String title;
        String order;
//        int assigned;
        FetchFirstAscending(Context context,String show,String title,String order,int page) {
            this.context = context;
            this.page=page;
            this.show=show;
            this.title=title;
            this.order=order;
            //this.assigned=assigned;
        }

        protected String doInBackground(String... urls) {
//            if (nextPageURL.equals("null")) {
//                return "all done";
//            }
            String result = new Helpdesk().getSortByTicketWithTitle(show,title,order);
            if (result == null)
                return null;
            String data;
            ticketOverviewList.clear();
            try {
                JSONObject jsonObject = new JSONObject(result);
                total=jsonObject.getInt("total");
                nextPageURL = jsonObject.getString("next_page_url");
//                try {
//                    data = jsonObject.getString("data");
//
//                } catch (JSONException e) {
//                    data = jsonObject.getString("result");
//                }
                JSONArray jsonArray =jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    TicketOverview ticketOverview = Helper.parseTicketOverviewSort(jsonArray, i);
                    if (ticketOverview != null)
                        ticketOverviewList.add(ticketOverview);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "success";
        }

        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            textView.setText(""+total+" tickets");
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
                                new FetchNextPage(getActivity(),pageno,"DESC").execute();
                                StyleableToast st = new StyleableToast(getContext(), getString(R.string.loading), Toast.LENGTH_SHORT);
                                st.setBackgroundColor(Color.parseColor("#3da6d7"));
                                st.setTextColor(Color.WHITE);
                                st.setIcon(R.drawable.ic_autorenew_black_24dp);
                                st.spinIcon();
                                st.setMaxAlpha();
                                st.show();
                                //Toast.makeText(getActivity(), "Loading!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });

            ticketOverviewAdapter = new TicketOverviewAdapter(getContext(),ticketOverviewList);
            recyclerView.setAdapter(ticketOverviewAdapter);

            if (ticketOverviewAdapter.getItemCount() == 0) {
                empty_view.setVisibility(View.VISIBLE);
            } else empty_view.setVisibility(View.GONE);
        }
    }

    /**
     * Async task for next page details.
     */
    private class FetchNextPage extends AsyncTask<String, Void, String> {
        Context context;
        int page;
        String order;

        FetchNextPage(Context context,int page,String order) {
            this.context = context;
            this.page=page;
            this.order=order;
        }

        protected String doInBackground(String... urls) {
            if (nextPageURL.equals("null")) {
                pageno=1;
                return "all done";
            }
            String result = new Helpdesk().nextPageSorting(dept,"created_at","ASC",page);
            if (result == null)
                return null;
            // DatabaseHandler databaseHandler = new DatabaseHandler(context);
            // databaseHandler.recreateTable();
            try {
                JSONObject jsonObject = new JSONObject(result);
                nextPageURL = jsonObject.getString("next_page_url");
                String data = jsonObject.getString("data");
                int my_tickets = jsonObject.getInt("total");
                if (my_tickets > 999)
                    Prefs.putString("myTickets", "999+");
                else
                    Prefs.putString("myTickets", my_tickets + "");
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    TicketOverview ticketOverview = Helper.parseTicketOverviewSort(jsonArray, i);
                    if (ticketOverview != null) {
                        ticketOverviewList.add(ticketOverview);
                        // databaseHandler.addTicketOverview(ticketOverview);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //databaseHandler.close();
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

    private class FetchFirstFilter extends AsyncTask<String, Void, String> {
        Context context;
        int page;
        String url;

        FetchFirstFilter(Context context,String url,int page) {
            this.context = context;
            this.url=url;
            this.page=page;

        }

        protected String doInBackground(String... urls) {
//            if (nextPageURL.equals("null")) {
//                return "all done";
//            }

            String result = new Helpdesk().ticketFiltration(url,page);
            if (result == null)
                return null;

            ticketOverviewList.clear();
            try {
                JSONObject jsonObject = new JSONObject(result);
                total=jsonObject.getInt("total");
                nextPageURL = jsonObject.getString("next_page_url");
//                try {
//                    data = jsonObject.getString("data");
//
//                } catch (JSONException e) {
//                    data = jsonObject.getString("result");
//                }
                JSONArray jsonArray =jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    TicketOverview ticketOverview = Helper.parseTicketOverviewSort(jsonArray, i);
                    if (ticketOverview != null)
                        ticketOverviewList.add(ticketOverview);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "success";
        }

        protected void onPostExecute(String result) {
            Prefs.putString("came from filter","false");
            Prefs.putString("filterwithsorting","true");
            Log.d("filterwithsorting","true");
            //Prefs.putString("URLFiltration","null");
            progressDialog.dismiss();
            textView.setText(""+total+" tickets");
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
                                pageNo++;
                                new FetchNextPageFilter(getActivity(),pageNo,url).execute();
                                StyleableToast st = new StyleableToast(getContext(), getString(R.string.loading), Toast.LENGTH_SHORT);
                                st.setBackgroundColor(Color.parseColor("#3da6d7"));
                                st.setTextColor(Color.WHITE);
                                st.setIcon(R.drawable.ic_autorenew_black_24dp);
                                st.spinIcon();
                                st.setMaxAlpha();
                                st.show();
                                //Toast.makeText(getActivity(), "Loading!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });

            ticketOverviewAdapter = new TicketOverviewAdapter(getContext(),ticketOverviewList);
            recyclerView.setAdapter(ticketOverviewAdapter);

            if (ticketOverviewAdapter.getItemCount() == 0) {
                empty_view.setVisibility(View.VISIBLE);
            } else empty_view.setVisibility(View.GONE);
        }
    }
    private class FetchNextPageFilter extends AsyncTask<String, Void, String> {
        Context context;
        int page;
        String url;

        FetchNextPageFilter(Context context,int page,String url) {
            this.context = context;
            this.page=page;
            this.url=url;
        }

        protected String doInBackground(String... urls) {
            if (nextPageURL.equals("null")) {
                pageNo=1;
                return "all done";
            }
            String result = new Helpdesk().nextPageUrlFilter(url,page);
            if (result == null)
                return null;
            // DatabaseHandler databaseHandler = new DatabaseHandler(context);
            // databaseHandler.recreateTable();
            try {
                JSONObject jsonObject = new JSONObject(result);
                nextPageURL = jsonObject.getString("next_page_url");
                String data = jsonObject.getString("data");
                int my_tickets = jsonObject.getInt("total");
                if (my_tickets > 999)
                    Prefs.putString("myTickets", "999+");
                else
                    Prefs.putString("myTickets", my_tickets + "");
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    TicketOverview ticketOverview = Helper.parseTicketOverviewSort(jsonArray, i);
                    if (ticketOverview != null) {
                        ticketOverviewList.add(ticketOverview);
                        // databaseHandler.addTicketOverview(ticketOverview);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //databaseHandler.close();
            return "success";
        }

        protected void onPostExecute(String result) {
            if (result == null)
                return;
            if (result.equals("all done")) {
                pageNo=1;
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
     * When the fragment is going to be attached
     * this life cycle method is going to be called.
     * @param context refers to the current fragment.
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
        int length=0;
        private Context context;
        ArrayList<Integer> checked_items= new ArrayList<>();
        ArrayList<String> ticketSubject=new ArrayList<>();
        private SparseBooleanArray mSelectedItemsIds;
        private List<Integer> selectedIds = new ArrayList<>();


        public TicketOverviewAdapter(Context context,List<TicketOverview> ticketOverviewList) {
            this.ticketOverviewList = ticketOverviewList;
            this.context=context;
            mSelectedItemsIds = new SparseBooleanArray();
        }

        @Override
        public int getItemCount() {
            return ticketOverviewList.size();
        }

        @Override
        public void onBindViewHolder(final TicketOverviewAdapter.TicketViewHolder ticketViewHolder, final int i) {
            final TicketOverview ticketOverview = ticketOverviewList.get(i);
            String letter = String.valueOf(ticketOverview.clientName.charAt(0)).toUpperCase();
            int id=ticketOverviewList.get(i).getTicketID();
            TextDrawable.IBuilder mDrawableBuilder;
            if (selectedIds.contains(id)){
                //if item is selected then,set foreground color of FrameLayout.
                ticketViewHolder.ticket.setBackgroundColor(Color.parseColor("#bdbdbd"));
            }
            else {
                //else remove selected item color.
                //holder.rootView.setForeground(new ColorDrawable(ContextCompat.getColor(context,android.R.color.transparent)));
            }
            subject=ticketOverview.ticketSubject;
            if (subject.startsWith("=?UTF-8?Q?")&&subject.endsWith("?=")){
                String first=subject.replace("=?UTF-8?Q?","");
                String second=first.replace("_"," ");
                String third=second.replace("=C2=A0","");
                String fourth=third.replace("?=","");
                String fifth=fourth.replace("=E2=80=99","'");
                ticketViewHolder.textViewSubject.setText(fifth);
            }
            else{
                ticketViewHolder.textViewSubject.setText(ticketOverview.ticketSubject);
            }

//            ticketViewHolder.ticket
//                    .setBackgroundColor(mSelectedItemsIds.get(i) ? 0x9934B5E4
//                            : Color.TRANSPARENT);
            if (checked_items.contains(id)){
                ticketViewHolder.ticket.setBackgroundColor(Color.parseColor("#d6d6d6"));
            }
            else {
                if (ticketOverview.lastReply.equals("client")){
                    int color=Color.parseColor("#ededed");
                    ticketViewHolder.ticket.setBackgroundColor(color);
                }
                else{
                    ticketViewHolder.ticket.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                //ticketViewHolder.ticket.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }

//        if (checked_items.contains(id)){
//            //if item is selected then,set foreground color of FrameLayout.
//            ticketViewHolder.ticket.setBackgroundColor(Color.parseColor("#B9FCFC"));
//        }
//        else {
//            //else remove selected item color.
//            ticketViewHolder.ticket.setBackgroundColor(Color.parseColor("#FFFFFF"));
//        }

            //Toast.makeText(context, "no of items"+checked_items.toString(), Toast.LENGTH_SHORT).show();

//            ticketViewHolder.checkBox1.setOnCheckedChangeListener(null);
//
//            //ticketViewHolder.checkBox1.setVisibility(View.VISIBLE);
//            ticketViewHolder.checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//
//
//                    if (b){
//                        ticketViewHolder.checkBox1.setChecked(true);
//                        ticketOverview.setChecked(true);
//                        //stringBuffer.append(""+ticketOverview.getTicketID()+",");
//                        if (subject.startsWith("=?UTF-8?Q?")&&subject.endsWith("?=")){
//                            String first=subject.replace("=?UTF-8?Q?","");
//                            String second=first.replace("_"," ");
//                            String third=second.replace("=C2=A0","");
//                            String fourth=third.replace("?=","");
//                            String fifth=fourth.replace("=E2=80=99","'");
//                            ticketViewHolder.textViewSubject.setText(fifth);
//                            ticketSubject.add(fifth);
//                        }
//                        else{
//                            ticketSubject.add(ticketOverview.ticketSubject);
//                        }
//                        //ticketSubject.add(ticketOverview.ticketSubject);
//                        checked_items.add(ticketOverview.getTicketID());
////                  length=checked_items.size();
//                        Log.d("ticketsubject",ticketSubject.toString());
//                        Log.d("checkeditems",checked_items.toString().replace(" ",""));
//                        Prefs.putString("tickets",checked_items.toString().replace(" ",""));
//                        Prefs.putString("TicketSubject",ticketSubject.toString());
//                        ticketViewHolder.ticket.setBackgroundColor(Color.parseColor("#bdbdbd"));
//
//                    }
//                    else{
//                        ticketOverview.setChecked(false);
//                        int pos=checked_items.indexOf(ticketOverview.getTicketID());
//                        int pos1=ticketSubject.indexOf(ticketOverview.getTicketSubject());
//                        try {
//                            checked_items.remove(pos);
//                            ticketSubject.remove(pos1);
//                        }
//                        catch (ArrayIndexOutOfBoundsException e){
//                            e.printStackTrace();
//                        }
//                        Log.d("Position",""+pos);
//                        //checked_items.remove(checked_items.indexOf(ticketOverview.getTicketID()));
//                        length--;
//                        Log.d("NoOfItems",""+length);
//                        Prefs.putInt("totalticketselected",length);
//                        Log.d("checkeditems", "" + checked_items);
//                        Prefs.putInt("NoOfItems",length);
//                        Prefs.putString("tickets", checked_items.toString().replace(" ", ""));
//                        Prefs.putString("TicketSubject",ticketSubject.toString());
//                        Log.d("ticketsubject",ticketSubject.toString());
//                        ticketViewHolder.checkBox1.setVisibility(View.GONE);
//                        ticketViewHolder.checkBox1.setChecked(false);
//                        if (!ticketOverview.lastReply.equals("client")){
//                            int color=Color.parseColor("#e9e9e9");
//                            ticketViewHolder.ticket.setBackgroundColor(color);
//                        }
//                        else{
//                            ticketViewHolder.ticket.setBackgroundColor(Color.parseColor("#FFFFFF"));
//                        }
//
//
//                        //notifyDataSetChanged();
//
//                    }
//
//
//                }
//            });
//
//
//
//
//            if (ticketOverview.getChecked()){
//                ticketViewHolder.checkBox1.setVisibility(View.VISIBLE);
//                ticketViewHolder.checkBox1.setChecked(true);
//                ticketViewHolder.ticket.setBackgroundColor(Color.parseColor("#bdbdbd"));
//            }
//            else{
//                ticketViewHolder.checkBox1.setVisibility(View.GONE);
//                ticketViewHolder.checkBox1.setChecked(false);
//                ticketViewHolder.ticket.setBackgroundColor(Color.parseColor("#FFFFFF"));
//            }

            if (ticketOverview.ticketAttachments.equals("0")) {
                ticketViewHolder.attachementView.setVisibility(View.GONE);
            } else {
                ticketViewHolder.attachementView.setVisibility(View.VISIBLE);
            }
            if (ticketOverview.dueDate != null && !ticketOverview.dueDate.equals("null"))
//            if (Helper.compareDates(ticketOverview.dueDate) == 1) {
//                ticketViewHolder.textViewOverdue.setVisibility(View.VISIBLE);
//            } else ticketViewHolder.textViewOverdue.setVisibility(View.GONE);

                if (Helper.compareDates(ticketOverview.dueDate) == 2) {
                    ticketViewHolder.textViewduetoday.setVisibility(View.VISIBLE);
                    ticketViewHolder.textViewduetoday.setText(R.string.due_today);
                    //ticketViewHolder.textViewOverdue.setBackgroundColor(Color.parseColor("#FFD700"));
                    ((GradientDrawable)ticketViewHolder.textViewduetoday.getBackground()).setColor(Color.parseColor("#ffff00"));
                    ticketViewHolder.textViewduetoday.setTextColor(Color.parseColor("#A0A0A0"));
                    //ticketViewHolder.textViewOverdue.setBackgroundColor();

                }
                else  if (Helper.compareDates(ticketOverview.dueDate) == 1) {
                    ticketViewHolder.textViewOverdue.setVisibility(View.VISIBLE);
                    ticketViewHolder.textViewOverdue.setText(R.string.overdue);
                    //ticketViewHolder.textViewOverdue.setBackgroundColor(Color.parseColor("#ef9a9a"));
//                GradientDrawable drawable = (GradientDrawable) context.getDrawable(ticketViewHolder.textViewOverdue);
//
////set color
//                 drawable.setColor(color);
                    ((GradientDrawable)ticketViewHolder.textViewOverdue.getBackground()).setColor(Color.parseColor("#FF9999"));
                    ticketViewHolder.textViewOverdue.setTextColor(Color.parseColor("#ffffff"));
                }
                else {
                    ticketViewHolder.textViewOverdue.setVisibility(View.GONE);
                }


            ticketViewHolder.textViewTicketID.setText(ticketOverview.ticketID + "");

            ticketViewHolder.textViewTicketNumber.setText(ticketOverview.ticketNumber);
            if (ticketOverview.getClientName().startsWith("=?")){
                String clientName=ticketOverview.getClientName().replaceAll("=?UTF-8?Q?","");
                String newClientName=clientName.replaceAll("=E2=84=A2","");
                String finalName=newClientName.replace("=??Q?","");
                String name=finalName.replace("?=","");
                String newName=name.replace("_"," ");
                Log.d("new name",newName);
                ticketViewHolder.textViewClientName.setText(newName);
            }
            else{
                ticketViewHolder.textViewClientName.setText(ticketOverview.clientName);

            }
            if (ticketOverview.ticketPriorityColor.equals("null")){
                ticketViewHolder.ticketPriority.setBackgroundColor(Color.parseColor("#3da6d7"));
            }
            else if (ticketOverview.ticketPriorityColor != null) {
                ticketViewHolder.ticketPriority.setBackgroundColor(Color.parseColor(ticketOverview.ticketPriorityColor));
            }


//        else if (ticketOverview.ticketPriorityColor.equals("null")){
//            ticketViewHolder.ticketPriority.setBackgroundColor(Color.parseColor("#3da6d7"));
//        }
            ticketViewHolder.textViewTime.setReferenceTime(Helper.relativeTime(ticketOverview.ticketTime));

            if (!ticketOverview.countthread.equals("0")){
                ticketViewHolder.countThread.setText("("+ticketOverview.getCountthread()+")");
            }
            else{
                ticketViewHolder.countThread.setVisibility(View.GONE);
            }

            if (ticketOverview.sourceTicket.equals("chat")){
                int color=Color.parseColor("#3da6d7");
                ticketViewHolder.source.setImageResource(R.drawable.chat);
                //ticketViewHolder.source.setColorFilter(color);
            }
            else if (ticketOverview.sourceTicket.equals("web")){
                int color=Color.parseColor("#3da6d7");
                ticketViewHolder.source.setImageResource(R.drawable.web);
                //ticketViewHolder.source.setColorFilter(color);
            }
            else if (ticketOverview.sourceTicket.equals("agent")){
                int color=Color.parseColor("#3da6d7");
                ticketViewHolder.source.setImageResource(R.drawable.ic_email_black_24dp);
                //ticketViewHolder.source.setColorFilter(color);
            }
            else if (ticketOverview.sourceTicket.equals("email")){
                int color=Color.parseColor("#3da6d7");
                ticketViewHolder.source.setImageResource(R.drawable.ic_email_black_24dp);
                //ticketViewHolder.source.setColorFilter(color);
            }
            else if (ticketOverview.sourceTicket.equals("facebook")){
                int color=Color.parseColor("#3da6d7");
                ticketViewHolder.source.setImageResource(R.drawable.facebook);
                //ticketViewHolder.source.setColorFilter(color);
            }
            else if (ticketOverview.sourceTicket.equals("twitter")){
                int color=Color.parseColor("#3da6d7");
                ticketViewHolder.source.setImageResource(R.drawable.twitter);
                //ticketViewHolder.source.setColorFilter(color);
            }
            else if (ticketOverview.sourceTicket.equals("call")){
                int color=Color.parseColor("#3da6d7");
                ticketViewHolder.source.setImageResource(R.drawable.ic_call_black_24dp);
                //ticketViewHolder.source.setColorFilter(color);
            }
            else{
                ticketViewHolder.source.setVisibility(View.GONE);
            }

            if (!ticketOverview.countcollaborator.equals("0")){

                ticketViewHolder.countCollaborator.setImageResource(R.drawable.ic_group_black_24dp);
            }
            else if (ticketOverview.countcollaborator.equals("0")){
                ticketViewHolder.countCollaborator.setVisibility(View.GONE);
            }

            if (!ticketOverview.agentName.equals("Unassigned")){
                ticketViewHolder.agentAssignedImage.setVisibility(View.VISIBLE);
                ticketViewHolder.agentAssigned.setText(ticketOverview.getAgentName());
            }
            else{
                ticketViewHolder.agentAssigned.setText("Unassigned");
                ticketViewHolder.agentAssignedImage.setVisibility(View.GONE);
            }

//else if (ticketOverview.getAgentName().equals("Unassigned")){
//    ticketViewHolder.agentAssignedImage.setVisibility(View.GONE);
//}


//            if (!ticketOverview.lastReply.equals("client")){
//                int color=Color.parseColor("#e9e9e9");
//                ticketViewHolder.ticket.setBackgroundColor(color);
//            }

            if (ticketOverview.clientPicture.equals("")){
                ticketViewHolder.roundedImageViewProfilePic.setVisibility(View.GONE);

            }
            else if (ticketOverview.clientPicture.contains(".jpg")){
                mDrawableBuilder = TextDrawable.builder()
                        .round();
//    TextDrawable drawable1 = mDrawableBuilder.build(generator.getRandomColor());
                Picasso.with(context).load(ticketOverview.getClientPicture()).transform(new CircleTransform()).into(ticketViewHolder.roundedImageViewProfilePic);
//        Glide.with(context)
//            .load(ticketOverview.getClientPicture())
//            .into(ticketViewHolder.roundedImageViewProfilePic);

                //ticketViewHolder.roundedImageViewProfilePic.setImageDrawable(drawable);

            }
            else{
                ColorGenerator generator = ColorGenerator.MATERIAL;
                TextDrawable drawable = TextDrawable.builder()
                        .buildRound(letter, generator.getRandomColor());
                ticketViewHolder.roundedImageViewProfilePic.setImageDrawable(drawable);
            }

//            if (ticketOverview.clientPicture.equals("")){
//                ticketViewHolder.roundedImageViewProfilePic.setVisibility(View.GONE);
//            }
//            else if (ticketOverview.clientPicture.startsWith("")){
//                IImageLoader imageLoader = new PicassoLoader();
//                imageLoader.loadImage(ticketViewHolder.roundedImageViewProfilePic, ticketOverview.clientPicture, ticketOverview.placeholder);
//                //imageLoader.loadImage(ticketViewHolder.roundedImageViewProfilePic, ticketOverview.clientPicture, ticketOverview.placeholder);
//            }

//        if (ticketOverview.clientPicture != null && ticketOverview.clientPicture.trim().length() != 0)
//            Picasso.with(ticketViewHolder.roundedImageViewProfilePic.getContext())
//                    .load(ticketOverview.clientPicture)
//                    .placeholder(R.drawable.default_pic)
//                    .error(R.drawable.default_pic)
//                    .into(ticketViewHolder.roundedImageViewProfilePic);

            ticketViewHolder.ticket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mActionMode != null) {
                        onListItemSelect(i);
                    }

                    else{
                        Intent intent = new Intent(v.getContext(), TicketDetailActivity.class);
                        intent.putExtra("ticket_id", ticketOverview.ticketID + "");
                        Prefs.putString("TICKETid",ticketOverview.ticketID+"");
                        Prefs.putString("ticketstatus",ticketOverview.getTicketStatus());
                        intent.putExtra("ticket_number", ticketOverview.ticketNumber);
                        intent.putExtra("ticket_opened_by", ticketOverview.clientName);
                        intent.putExtra("ticket_subject", ticketOverview.ticketSubject);
                        Log.d("clicked","onRecyclerView");
                        v.getContext().startActivity(intent);
                    }

                }
            });
            ticketViewHolder.ticket.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onListItemSelect(i);
                    length++;
                    Log.d("noofitems",""+length);
                    Prefs.putInt("NoOfItems",length);

//                ticketOverviewList.get(i).getTicketID();
//                Log.d("position",""+ticketOverviewList.get(i).getTicketID());
//                if (ticketViewHolder.checkBox1.isEnabled()){
//
//                }
//                else{
//                    ticketViewHolder.checkBox1.setVisibility(View.GONE);
//                }
//                if (ticketViewHolder.checkBox1.isChecked()){
//
//                }else{
//                    ticketViewHolder.checkBox1.setVisibility(View.GONE);
//                }
                    return true;
                }
            });



//        ticketViewHolder.checkBox1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (ticketViewHolder.checkBox1.isChecked())
//                    ticketViewHolder.checkBox1.setChecked(false);
//                else
//                    ticketViewHolder.checkBox1.setChecked(true);
//            }
//        });



//        ticketViewHolder.checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//
//                if (isChecked){
////                    ticketViewHolder.checkBox1.setChecked(true);
////                    Prefs.putString("checkboxstate","checked");
//
//                    stringBuffer.append(ticketOverview.getTicketID()+",");
//                    Log.d("ids",stringBuffer.toString());
//
//                }
//                else{
//                    Prefs.putString("checkboxstate","unchecked");
//                    stringBuffer.toString().replace(""+ticketOverview.getTicketID(),"");
//                    Log.d("ids",stringBuffer.toString());
//                    ticketViewHolder.checkBox1.setVisibility(View.GONE);
//                    notifyDataSetChanged();
//                }
////
////                String state=Prefs.getString("checkboxstate",null);
////
////                if (state.equals("checked")){
////                    ticketViewHolder.checkBox1.setSelected(true);
////                    notifyDataSetChanged();
////                }
////                else{
////                    ticketViewHolder.checkBox1.setSelected(false);
////                    notifyDataSetChanged();
////                }
//
//
//            }
//        });

        }
        private void onListItemSelect(int position) {
            ticketOverviewAdapter.toggleSelection(position);//Toggle the selection

            boolean hasCheckedItems = ticketOverviewAdapter.getSelectedCount() > 0;//Check if any items are already selected or not


            if (hasCheckedItems && mActionMode == null)
                // there are some selected items, start the actionMode
                mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new Toolbar_ActionMode_Callback(getActivity(),ticketOverviewAdapter, null, ticketOverviewList, false));
            else if (!hasCheckedItems && mActionMode != null)
                // there no selected items, finish the actionMode
                mActionMode.finish();

            if (mActionMode != null)
                //set action mode title on item selection
                mActionMode.setTitle(String.valueOf(ticketOverviewAdapter
                        .getSelectedCount()) + " ticket selected");


        }

        public void toggleSelection(int position) {
            selectView(position, !mSelectedItemsIds.get(position));
        }


        //Remove selected selections



        //Put or delete selected position into SparseBooleanArray
        public void selectView(int position, boolean value) {
            TicketOverview ticketOverview=ticketOverviewList.get(position);
            if (value) {
                ticketSubject.add(ticketOverview.ticketSubject);
                checked_items.add(ticketOverview.getTicketID());
                Log.d("ticketsubject",ticketSubject.toString());
                Log.d("checkeditems",checked_items.toString().replace(" ",""));
                Prefs.putString("tickets",checked_items.toString().replace(" ",""));
                Prefs.putString("TicketSubject",ticketSubject.toString());
                mSelectedItemsIds.put(position, value);
            }
            else {

                int pos=checked_items.indexOf(ticketOverview.getTicketID());
                int pos1=ticketSubject.indexOf(ticketOverview.getTicketSubject());
                try {
                    checked_items.remove(pos);
                    ticketSubject.remove(pos1);
                }catch (ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                }
                Log.d("ticketsubject",ticketSubject.toString());
                Log.d("checkeditems",checked_items.toString().replace(" ",""));
                Prefs.putInt("totalticketselected",length);
                Log.d("checkeditems", "" + checked_items);
                Prefs.putInt("NoOfItems",length);
                Prefs.putString("tickets", checked_items.toString().replace(" ", ""));
                Prefs.putString("TicketSubject",ticketSubject.toString());
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

        //        //Return all selected ids
//        public SparseBooleanArray getSelectedIds() {
//            return mSelectedItemsIds;
//        }
//        public void setSelectedIds(ArrayList<Integer> checked_items) {
//            this.checked_items = checked_items;
//            notifyDataSetChanged();
//        }
        public TicketOverview getItem(int position){
            return ticketOverviewList.get(position);
        }
        @Override
        public TicketOverviewAdapter.TicketViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.card_ticket, viewGroup, false);
            return new TicketOverviewAdapter.TicketViewHolder(itemView);
        }

        public  class TicketViewHolder extends RecyclerView.ViewHolder {

            protected View ticket;
            ImageView roundedImageViewProfilePic;
            TextView textViewTicketID;
            TextView textViewTicketNumber;
            TextView textViewClientName;
            TextView textViewSubject;
            RelativeTimeTextView textViewTime;
            TextView textViewOverdue;
            View ticketPriority;
            // TextView ticketStatus;
            ImageView attachementView;
            CheckBox checkBox1;
            ImageView countCollaborator;
            ImageView source;
            TextView countThread;
            TextView agentAssigned;
            ImageView agentAssignedImage;
            TextView textViewduetoday;
            TicketViewHolder(View v) {
                super(v);
                ticket = v.findViewById(R.id.ticket);
                attachementView = (ImageView) v.findViewById(R.id.attachment_icon);
                ticketPriority = v.findViewById(R.id.priority_view);
                roundedImageViewProfilePic = (ImageView) v.findViewById(R.id.imageView_default_profile);
                textViewTicketID = (TextView) v.findViewById(R.id.textView_ticket_id);
                textViewTicketNumber = (TextView) v.findViewById(R.id.textView_ticket_number);
                textViewClientName = (TextView) v.findViewById(R.id.textView_client_name);
                textViewSubject = (TextView) v.findViewById(R.id.textView_ticket_subject);
                textViewTime = (RelativeTimeTextView) v.findViewById(R.id.textView_ticket_time);
                textViewOverdue = (TextView) v.findViewById(R.id.overdue_view);
                checkBox1= (CheckBox) v.findViewById(R.id.checkbox);
                countCollaborator= (ImageView) v.findViewById(R.id.collaborator);
                countThread= (TextView) v.findViewById(R.id.countthread);
                source= (ImageView) v.findViewById(R.id.source);
                agentAssigned= (TextView) v.findViewById(R.id.agentassigned);
                agentAssignedImage= (ImageView) v.findViewById(R.id.agentAssigned);
                textViewduetoday= (TextView) v.findViewById(R.id.duetoday);


            }

        }

    }
    public class Toolbar_ActionMode_Callback implements android.support.v7.view.ActionMode.Callback {

        private Context context;
        private InboxTickets.TicketOverviewAdapter recyclerView_adapter;
        private ArrayList<TicketOverview> message_models;
        private boolean isListViewFragment;


        public Toolbar_ActionMode_Callback(Context context, TicketOverviewAdapter ticketOverviewAdapter, InboxTickets.TicketOverviewAdapter recyclerView_adapter, List<TicketOverview> message_models, boolean b) {
            this.context = context;
            this.recyclerView_adapter = recyclerView_adapter;
            this.message_models = (ArrayList<TicketOverview>) message_models;
            this.isListViewFragment = isListViewFragment;
        }

        @Override
        public boolean onCreateActionMode(android.support.v7.view.ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.multiplesorting, menu);//Inflate the menu over action mode
            return true;
        }

        @Override
        public boolean onPrepareActionMode(android.support.v7.view.ActionMode mode, Menu menu) {

            //Sometimes the meu will not be visible so for that we need to set their visibility manually in this method
            //So here show action menu according to SDK Levels
            if (Build.VERSION.SDK_INT < 11) {
                MenuItemCompat.setShowAsAction(menu.findItem(R.id.mergeticket), MenuItemCompat.SHOW_AS_ACTION_NEVER);

//            MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_copy), MenuItemCompat.SHOW_AS_ACTION_NEVER);
//            MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_forward), MenuItemCompat.SHOW_AS_ACTION_NEVER);
            } else {
                menu.findItem(R.id.mergeticket).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//            menu.findItem(R.id.action_copy).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//            menu.findItem(R.id.action_forward).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
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
            switch (item.getItemId()) {
                case R.id.mergeticket:
//                Toast.makeText(, "You selected close menu.", Toast.LENGTH_SHORT).show();//Show toast
                    try {
                        if (Prefs.getString("tickets", null).equals("null") || Prefs.getString("tickets", null).equals("[]")) {
                            Toasty.info(getActivity(), getString(R.string.noticket), Toast.LENGTH_LONG).show();
                            return false;
                        }
                        String ticketId = Prefs.getString("tickets", null);
                        List<String> items = new ArrayList<String>(Arrays.asList(ticketId.split("\\s*,\\s*")));
                        int itemCount = items.size();
                        if (itemCount == 1) {
                            Toasty.info(getActivity(), getString(R.string.selectMultipleTicket), Toast.LENGTH_LONG).show();
                            return false;
                        } else {
                            Intent intent = new Intent(getActivity(), TicketMergeActtivity.class);
                            startActivity(intent);
                        }

//            Intent intent = new Intent(getActivity(), TicketMergeActtivity.class);
//            startActivity(intent);

                    } catch (NullPointerException e) {
                        Toasty.info(getActivity(), getString(R.string.noticket), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
//                    Log.d("clicked on closed","closed");
                    setNullToActionMode();
                    mode.finish();

                    //mode.finish();
                    break;
                case R.id.action_statusClosed:
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
                            try {
                                new StatusChange(ticket, Integer.parseInt(Prefs.getString("closedid", null))).execute();
                                Prefs.putString("tickets", null);
                                progressDialog.show();
                                progressDialog.setMessage(getString(R.string.pleasewait));
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
                    if (!Prefs.getString("tickets", null).equals("") || !Prefs.getString("tickets", null).equals("null") || !Prefs.getString("tickets", null).equals(null)) {


                        Log.d("tickets", ticket);
                        if (ticket.equals("") || ticket.equals(null)) {
                            Toasty.warning(getActivity(), getString(R.string.noticket), Toast.LENGTH_SHORT).show();
                            return false;
                        } else {


                        }

                    }else {

                    }
                    break;
                case R.id.action_statusResolved:
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
                            try {
                                new StatusChange(ticket, Integer.parseInt(Prefs.getString("resolvedid", null))).execute();
                                Prefs.putString("tickets", null);
                                progressDialog.show();
                                progressDialog.setMessage(getString(R.string.pleasewait));
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
                    break;
                case R.id.action_statusDeleted:
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
                            try {
                                new StatusChange(ticket, Integer.parseInt(Prefs.getString("deletedid", null))).execute();
                                Prefs.putString("tickets", null);
                                progressDialog.show();
                                progressDialog.setMessage(getString(R.string.pleasewait));
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
                case R.id.action_statusOpen:
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
                            try {
                                new StatusChange(ticket, Integer.parseInt(Prefs.getString("openid", null))).execute();
                                progressDialog.show();
                                progressDialog.setMessage(getString(R.string.pleasewait));
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



            }
            return false;
        }


        @Override
        public void onDestroyActionMode(android.support.v7.view.ActionMode mode) {

            //When action mode destroyed remove selected selections and set action mode to null
            //First check current fragment action mode
            Log.d("onDestroyActionMode","CAME HERE");
            CreatedAtAsc createdAtAsc=new CreatedAtAsc();
            //recyclerView_adapter.removeSelection();
            createdAtAsc.setNullToActionMode();
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


}
