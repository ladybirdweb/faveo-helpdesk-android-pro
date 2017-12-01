package co.helpdesk.faveo.pro.frontend.fragments.tickets;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.helpdesk.faveo.pro.Helper;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.pro.frontend.activities.MainActivity;
import co.helpdesk.faveo.pro.frontend.activities.NotificationActivity;
import co.helpdesk.faveo.pro.frontend.activities.TicketFilter;
import co.helpdesk.faveo.pro.frontend.adapters.TicketOverviewAdapter;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.pro.model.TicketOverview;
import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class DueByDesc extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.cardList)
    ShimmerRecyclerView recyclerView;
    ProgressDialog progressDialog;

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
    int assigned=0;
    TicketOverviewAdapter ticketOverviewAdapter;
    List<TicketOverview> ticketOverviewList = new ArrayList<>();
    String url;
    private boolean loading = true;
    int total;
    int pastVisibleItems, visibleItemCount, totalItemCount;

    public String mParam1;
    public String mParam2;
    String ticket;
    int pageno=1;
    String show,dept;
    int pageNo=1;

    private DueByDesc.OnFragmentInteractionListener mListener;

    public static DueByDesc newInstance(String param1, String param2) {
        DueByDesc fragment = new DueByDesc();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public DueByDesc() {
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
            rootView = inflater.inflate(R.layout.fragment_recycler, container, false);
            ButterKnife.bind(this, rootView);
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait");
            url=Prefs.getString("URLFiltration",null);
//          Toolbar toolbar1= (Toolbar) rootView.findViewById(R.id.toolbar3);
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
            TextView textView= (TextView) toolbar.findViewById(R.id.toolbartextview);
            Toolbar toolbar1= (Toolbar) rootView.findViewById(R.id.toolbarfilteration);
            toolbar1.setVisibility(View.VISIBLE);
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

//        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            textView.setText(getString(R.string.dueby));
//        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            toolbar.setVisibility(View.VISIBLE);
            toolbar.setTitle(getString(R.string.dueby));
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
                        Toasty.info(getActivity(),getString(R.string.alreadysorted),Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    if (item.getItemId()==R.id.created_ascending){
                        title = getString(R.string.createdat);
                        fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
                        if (fragment == null)
                            fragment = new CreatedAtAsc();
                        if (fragment != null) {
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.container_body, fragment);
                            // fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
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
                // swipeRefresh.setRefreshing(true);
                show =Prefs.getString("source",null);
                if (show.equals("1")){
                    dept="mytickets";
                    progressDialog.show();
                    new FetchFirstAscending(getActivity(),dept+"&assigned=0","due","DESC",1).execute();
                    ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.my_tickets));
                }
                else if (show.equals("2")){
                    dept="inbox";
                    progressDialog.show();
                    new FetchFirstAscending(getActivity(),dept+"&assigned=0","due","DESC",1).execute();
                    ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.unassigned_tickets));
                }
                else if (show.equals("3")){
                    dept="closed";
                    progressDialog.show();
                    new FetchFirstAscending(getActivity(),dept,"due","DESC",1).execute();
                    ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.closed_ticket));
                }
                else if (show.equals("4")){
                    dept="trash";
                    progressDialog.show();
                    new FetchFirstAscending(getActivity(),dept,"due","DESC",1).execute();
                    ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.trash));
                }
                else if (show.equals("5")){
                    dept="inbox";
                    progressDialog.show();
                    new FetchFirstAscending(getActivity(),dept,"due","DESC",1).execute();
                    ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.inbox));
                }
                else if (show.equals("6")&&!url.equals(null)){

                    progressDialog.show();
                    new FetchFirstFilter(getActivity(),url+"&sort-by=due&order=DESC",pageNo).execute();
                    //Toast.makeText(getActivity(), "came here", Toast.LENGTH_SHORT).show();
                }


//                progressDialog.show();
//                new FetchFirstAscending(getActivity(),"inbox","due","DESC",1).execute();
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
                        show =Prefs.getString("source",null);
                        if (show.equals("1")){
                            dept="mytickets";
                            progressDialog.show();
                            new FetchFirstAscending(getActivity(),dept,"due","DESC",1).execute();
                            ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.my_tickets));
                        }
                        else if (show.equals("2")){
                            dept="inbox";
                            progressDialog.show();
                            new FetchFirstAscending(getActivity(),dept+"&assigned=0","due","DESC",1).execute();
                            ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.unassigned_tickets));
                        }
                        else if (show.equals("3")){
                            dept="closed";
                            progressDialog.show();
                            new FetchFirstAscending(getActivity(),dept,"due","DESC",1).execute();
                            ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.closed_ticket));
                        }
                        else if (show.equals("4")){
                            dept="trash";
                            progressDialog.show();
                            new FetchFirstAscending(getActivity(),dept,"due","DESC",1).execute();
                            ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.trash));
                        }
                        else if (show.equals("5")){
                            dept="inbox";
                            progressDialog.show();
                            new FetchFirstAscending(getActivity(),dept,"due","DESC",1).execute();
                            ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.inbox));
                        }
                        else if (show.equals("6")&&!url.equals(null)){

                            progressDialog.show();
                            new FetchFirstFilter(getActivity(),url+"&sort-by=due&order=DESC",pageNo).execute();
                            //Toast.makeText(getActivity(), "came here", Toast.LENGTH_SHORT).show();
                        }
                        //new FetchFirstAscending(getActivity(),dept,"due","DESC",1).execute();
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
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        int id=item.getItemId();
        StringBuffer stringBuffer=new StringBuffer();
        if (id==R.id.action_statusClosed) {

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
            }catch (NullPointerException e){
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
        }
        else if (id==R.id.action_statusResolved) {
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
        }

        else if (id==R.id.action_statusDeleted) {
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
        }
        else if (id==R.id.action_noti){
            Intent intent = new Intent(getActivity(), NotificationActivity.class);
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
            String result = new Helpdesk().nextPageSorting(dept,"due","DESC",page);
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

}
