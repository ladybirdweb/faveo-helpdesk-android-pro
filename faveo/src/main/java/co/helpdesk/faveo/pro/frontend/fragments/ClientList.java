package co.helpdesk.faveo.pro.frontend.fragments;

import android.app.ActivityOptions;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import co.helpdesk.faveo.pro.frontend.activities.ClientDetailActivity;
import co.helpdesk.faveo.pro.frontend.activities.MainActivity;
import co.helpdesk.faveo.pro.frontend.activities.NotificationActivity;
import co.helpdesk.faveo.pro.frontend.activities.RegisterUser;
import co.helpdesk.faveo.pro.frontend.activities.SearchActivity;
import co.helpdesk.faveo.pro.frontend.adapters.ClientOverviewAdapter;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.pro.model.ClientOverview;
import es.dmoral.toasty.Toasty;

public class ClientList extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    static String nextPageURL = "";

    @BindView(R.id.cardList)
    ShimmerRecyclerView recyclerView;

    @BindView(R.id.empty_view)
    TextView empty_view;
    @BindView(R.id.noiternet_view)
    TextView noInternet_view;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    ClientOverviewAdapter clientOverviewAdapter;
    int total;
    String cameFromFilter;
    @BindView(R.id.totalcount)
            TextView textView;
    List<ClientOverview> clientOverviewList = new ArrayList<>();
    View rootView;
    ProgressDialog progressDialog;
    Toolbar toolbar;
    View view;
    int page=1;
    private boolean loading = true;
    int pastVisibleItems, visibleItemCount, totalItemCount;
    LinearLayout linearLayout;
    public String mParam1;
    public String mParam2;
    View view1;
    TextView toolbarTextview;
    Toolbar toolbar1,toolbar2;
    String title;
    String heading;
    private OnFragmentInteractionListener mListener;
    String url;
    String condition;
    public static ClientList newInstance(String param1, String param2) {
        ClientList fragment = new ClientList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ClientList() {
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
        if (InternetReceiver.isConnected()){
            new FetchClients(getActivity()).execute();
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
            heading=Prefs.getString("filtercustomer",null);
            condition=Prefs.getString("normalclientlist",null);
            rootView = inflater.inflate(R.layout.fragment_recycler, container, false);
            toolbar= (Toolbar) rootView.findViewById(R.id.toolbar2);
            toolbarTextview= (TextView) toolbar.findViewById(R.id.toolbartextview);

             if (heading.equals("active")){
                toolbarTextview.setText(getString(R.string.activeUser));
            }
            else if (heading.equals("inactive")){
                toolbarTextview.setText(getString(R.string.inactiveUser));
            }
            else if (heading.equals("banned")){
                toolbarTextview.setText(getString(R.string.bannedUser));
            }
            else if (heading.equals("deleted")){
                toolbarTextview.setText(getString(R.string.deleteduser));
            }
            else if (heading.equals("admin")){
                toolbarTextview.setText(getString(R.string.roleAdmin));
            }
             else if (heading.equals("agent")){
                 toolbarTextview.setText(getString(R.string.roleAgent));
             }
             else if (heading.equals("user")){
                 toolbarTextview.setText(getString(R.string.roleUser));
             }
             else{
                toolbarTextview.setText(getString(R.string.customerFilter));
            }

            view1=rootView.findViewById(R.id.separationView);
            view1.setVisibility(View.GONE);
             toolbar1= (Toolbar) rootView.findViewById(R.id.toolbarfilteration);
            toolbar1.setVisibility(View.GONE);

            linearLayout= (LinearLayout) rootView.findViewById(R.id.toolbarview);
            linearLayout.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
            view=rootView.findViewById(R.id.separationview);
            view.setVisibility(View.VISIBLE);
            ButterKnife.bind(this, rootView);
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait");
            toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_sort_black_24dp));
            toolbar.inflateMenu(R.menu.menu_customer_filtration);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    try {
                        if (item != null) {
                            item.getSubMenu().clearHeader();
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    Fragment fragment = null;
                    title = getString(R.string.app_name);
                    if (item.getItemId() == R.id.active) {
                        Prefs.putString("filtercustomer","active");
                        Prefs.putString("normalclientlist","false");
                        url="active=1";
                        Prefs.putString("customerfilter",url);
                        title = getString(R.string.client_list);
                        fragmentController(title);
                        return true;
                    }

                    if (item.getItemId() == R.id.banned) {
                        Prefs.putString("filtercustomer","banned");
                        Prefs.putString("normalclientlist","false");
                        url="ban=1";
                        Prefs.putString("customerfilter",url);
                        title = getString(R.string.client_list);
                        fragmentController(title);
                        return true;
                    }

                    if (item.getItemId() == R.id.deleted) {
                        Prefs.putString("filtercustomer","deleted");
                        Prefs.putString("normalclientlist","false");
                        url="deleted=1";
                        Prefs.putString("customerfilter",url);
                        title = getString(R.string.client_list);
                        fragmentController(title);
                        return true;
                    }
                    if (item.getItemId() == R.id.inactive) {
                        Prefs.putString("filtercustomer","inactive");
                        Prefs.putString("normalclientlist","false");
                        url="active=0";
                        Prefs.putString("customerfilter",url);
                        title = getString(R.string.client_list);
                        fragmentController(title);
                        return true;
                    }


                    if (item.getItemId() == R.id.admin) {
                        Prefs.putString("filtercustomer","admin");
                        Prefs.putString("normalclientlist","false");
                        url="role=admin";
                        Prefs.putString("customerfilter",url);
                        title = getString(R.string.client_list);
                        fragmentController(title);
                        return true;
                    }
                    if (item.getItemId() == R.id.agent) {
                        Prefs.putString("filtercustomer","agent");
                        Prefs.putString("normalclientlist","false");
                        url="role=agent";
                        Prefs.putString("customerfilter",url);
                        title = getString(R.string.client_list);
                        fragmentController(title);
                        return true;
                    }
                    if (item.getItemId() == R.id.user) {
                        Prefs.putString("filtercustomer","user");
                        Prefs.putString("normalclientlist","false");
                        url="role=user";
                        Prefs.putString("customerfilter",url);
                        title = getString(R.string.client_list);
                        fragmentController(title);
                        return true;
                    }

                    return true;
                }
            });

            swipeRefresh.setColorSchemeResources(R.color.faveo_blue);
            if (InternetReceiver.isConnected()) {


                try {
                    url=Prefs.getString("customerfilter",null);
                    if (condition.equals("true")) {
                        noInternet_view.setVisibility(View.GONE);
                        //swipeRefresh.setRefreshing(true);
                        swipeRefresh.setRefreshing(true);
                        new FetchClients(getActivity()).execute();
                    } else if (condition.equals("false")){
                        swipeRefresh.setRefreshing(true);
                        new FetchClientsFilter(getActivity(), url, page).execute();
                    }

                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }else {
                noInternet_view.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                empty_view.setVisibility(View.GONE);
            }

            swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (InternetReceiver.isConnected()) {
                        loading = true;

                        try {
                            url=Prefs.getString("customerfilter",null);
                            if (condition.equals("true")) {
                                noInternet_view.setVisibility(View.GONE);
                                new FetchClients(getActivity()).execute();
                            } else if (condition.equals("false")){
                                new FetchClientsFilter(getActivity(), url, page).execute();
                            }

                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }
                    }else {
                        recyclerView.setVisibility(View.INVISIBLE);
                        swipeRefresh.setRefreshing(false);
                        empty_view.setVisibility(View.GONE);
                        noInternet_view.setVisibility(View.VISIBLE);
                    }
                }
            });

             RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
                boolean hideToolBar = false;
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                }
            };

             recyclerView.addOnScrollListener(onScrollListener);

            empty_view.setText(R.string.no_clients);
        }
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.client_list));

        return rootView;
    }

    public void fragmentController(String title){
        Fragment fragment;
        fragment =getActivity().getSupportFragmentManager().findFragmentByTag(title);
        if (fragment == null)
            fragment = new ClientList();
        if (fragment != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
        }
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.search_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id == R.id.action_search) {
            Prefs.putString("cameFromClientList","true");
            Prefs.putString("cameFromNotification","fromClient");
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
            //getActivity().finish();
        }
        else if (id==R.id.action_noti){
            Intent intent=new Intent(getActivity(),NotificationActivity.class);
            startActivity(intent);
        }
        else if (id==R.id.action_add){
            Intent intent=new Intent(getActivity(),RegisterUser.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private class FetchClients extends AsyncTask<String, Void, String> {
        Context context;

        FetchClients(Context context) {
            this.context = context;
        }

        protected String doInBackground(String... urls) {
            String result = new Helpdesk().getCustomersOverview();
            if (result == null)
                return null;
            String data;
            clientOverviewList.clear();
            try {
                JSONObject jsonObject = new JSONObject(result);
                total=jsonObject.getInt("total");
                data = jsonObject.getString("data");
                nextPageURL = jsonObject.getString("next_page_url");
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    ClientOverview clientOverview = Helper.parseClientOverview(jsonArray, i);
                    if (clientOverview != null)
                        clientOverviewList.add(clientOverview);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "success";
        }

        protected void onPostExecute(String result) {
            swipeRefresh.setRefreshing(false);
            url = "null";
            //Prefs.putString("customerfilter","null");
            textView.setText("" + total + " clients");
            if (swipeRefresh.isRefreshing())
                swipeRefresh.setRefreshing(false);
            if (isAdded()) {
                if (result == null) {
                    Toasty.error(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    return;
                }

                if (result.equals("all done")) {

                    Toasty.info(context, getString(R.string.all_caught_up), Toast.LENGTH_SHORT).show();
                    //return;
                }
                // recyclerView = (ShimmerRecyclerView) rootView.findViewById(R.id.cardList);
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
                                    new FetchNextPage(getActivity()).execute();
                                    //Toast.makeText(getActivity(), "Loading!", Toast.LENGTH_SHORT).show();

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
                clientOverviewAdapter = new ClientOverviewAdapter(getContext(), clientOverviewList);
                recyclerView.setAdapter(clientOverviewAdapter);
                if (clientOverviewAdapter.getItemCount() == 0) {
                    empty_view.setVisibility(View.VISIBLE);
                } else empty_view.setVisibility(View.GONE);
            }
        }
    }

    private class FetchNextPage extends AsyncTask<String, Void, String> {
        Context context;

        FetchNextPage(Context context) {
            this.context = context;
        }

        protected String doInBackground(String... urls) {
            if (nextPageURL.equals("null")) {
                return "all done";
            }
            String result = new Helpdesk().nextPageURL(nextPageURL);
            if (result == null)
                return null;
            String data;
            try {
                JSONObject jsonObject = new JSONObject(result);
                data = jsonObject.getString("data");
                nextPageURL = jsonObject.getString("next_page_url");
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    ClientOverview clientOverview = Helper.parseClientOverview(jsonArray, i);
                    if (clientOverview != null)
                        clientOverviewList.add(clientOverview);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "success";
        }

        protected void onPostExecute(String result) {
            if (result == null)
                return;
            if (result.equals("all done")) {
                Toasty.info(context, getString(R.string.allClientsLoaded), Toast.LENGTH_SHORT).show();
                return;
            }
            clientOverviewAdapter.notifyDataSetChanged();
            loading = true;
        }
    }
    private class FetchClientsFilter extends AsyncTask<String, Void, String> {
        Context context;
        String url;
        int page;
        FetchClientsFilter(Context context,String url,int page) {
            this.context = context;
            this.url=url;
            this.page=page;
        }

        protected String doInBackground(String... urls) {
            String result = new Helpdesk().customerFiltration(page,url);
            if (result == null)
                return null;
            String data;
            clientOverviewList.clear();
            try {
                JSONObject jsonObject = new JSONObject(result);
                total=jsonObject.getInt("total");
                data = jsonObject.getString("data");
                nextPageURL = jsonObject.getString("next_page_url");
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    ClientOverview clientOverview = Helper.parseClientOverview(jsonArray, i);
                    if (clientOverview != null)
                        clientOverviewList.add(clientOverview);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "success";
        }

        protected void onPostExecute(String result) {
            swipeRefresh.setRefreshing(false);
            textView.setText(""+total+" clients");
            if (swipeRefresh.isRefreshing())
                swipeRefresh.setRefreshing(false);

            if (result == null) {
                Toasty.error(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }
            if (result.equals("all done")) {

                Toasty.info(context, getString(R.string.allClientsLodaded), Toast.LENGTH_SHORT).show();
                //return;
            }
            // recyclerView = (ShimmerRecyclerView) rootView.findViewById(R.id.cardList);
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
                                page++;
                                //new FetchNextPage(getActivity()).execute();
                                //Toast.makeText(getActivity(), "Loading!", Toast.LENGTH_SHORT).show();
                                new FetchNextPageFilter(getActivity(),url,page).execute();
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
            clientOverviewAdapter = new ClientOverviewAdapter(getContext(),clientOverviewList);
            recyclerView.setAdapter(clientOverviewAdapter);
            if (clientOverviewAdapter.getItemCount() == 0) {
                empty_view.setVisibility(View.VISIBLE);
            } else empty_view.setVisibility(View.GONE);
        }
    }

    private class FetchNextPageFilter extends AsyncTask<String, Void, String> {
        Context context;
        String url;
        int page;
        FetchNextPageFilter(Context context,String url,int page) {
            this.context = context;
            this.url=url;
            this.page=page;
        }

        protected String doInBackground(String... urls) {
            if (nextPageURL.equals("null")) {
                return "all done";
            }
            String result = new Helpdesk().nextPagecustomerFiltration(page,url);
            if (result == null)
                return null;
            String data;
            try {
                JSONObject jsonObject = new JSONObject(result);
                data = jsonObject.getString("data");
                nextPageURL = jsonObject.getString("next_page_url");
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    ClientOverview clientOverview = Helper.parseClientOverview(jsonArray, i);
                    if (clientOverview != null)
                        clientOverviewList.add(clientOverview);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "success";
        }

        protected void onPostExecute(String result) {
            if (result == null)
                return;
            if (result.equals("all done")) {
                Toasty.info(context, getString(R.string.allClientsLodaded), Toast.LENGTH_SHORT).show();
                return;
            }
            clientOverviewAdapter.notifyDataSetChanged();
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

    /**
     * Here we are handling the click event .
     * @param v is the view.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.client4:
                Intent intent = new Intent(getActivity(), ClientDetailActivity.class);
                View sharedView = v.findViewById(R.id.imageView_default_profile);
                String transitionName = getString(R.string.blue_name);

                ActivityOptions transitionActivityOptions = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(), sharedView, transitionName);
                    startActivity(intent, transitionActivityOptions.toBundle());
                } else startActivity(intent);

                break;
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
