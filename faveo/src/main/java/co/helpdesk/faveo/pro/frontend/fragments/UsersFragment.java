package co.helpdesk.faveo.pro.frontend.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import co.helpdesk.faveo.pro.Helper;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.pro.frontend.adapters.ClientOverviewAdapter;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.pro.model.ClientOverview;
import es.dmoral.toasty.Toasty;

/**
 * Created by Amal on 27/03/2017.
 */

public class UsersFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View view1;
    Toolbar toolbar,toolbar1;
    TextView toolbarTextview;
    ShimmerRecyclerView recyclerView;
    private String mParam1;
    private String mParam2;
    static String nextPageURL = "";
    TextView empty_view;
    TextView noInternet_view;
    SwipeRefreshLayout swipeRefresh;
    ClientOverviewAdapter clientOverviewAdapter;
    int total;
    String cameFromFilter;
    TextView textView;
    List<ClientOverview> clientOverviewList = new ArrayList<>();
    View rootView;
    ProgressDialog progressDialog;
    String querry;
    LinearLayout toolbarView;
    int pastVisibleItems, visibleItemCount, totalItemCount;
    private boolean loading = true;

    public static UsersFragment newInstance(int page, String title) {
        UsersFragment fragmentFirst = new UsersFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }
    public UsersFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler, container, false);
        toolbarView= (LinearLayout) rootView.findViewById(R.id.toolbarview);
        toolbarView.setVisibility(View.GONE);
        toolbar= (Toolbar) rootView.findViewById(R.id.toolbar2);
        toolbar.setVisibility(View.GONE);
        toolbarTextview= (TextView) toolbar.findViewById(R.id.toolbartextview);
        view1=rootView.findViewById(R.id.separationView);
        view1.setVisibility(View.GONE);
        toolbar1= (Toolbar) rootView.findViewById(R.id.toolbarfilteration);
        toolbar1.setVisibility(View.GONE);
        textView= (TextView) rootView.findViewById(R.id.totalcount);
        textView.setVisibility(View.GONE);
        recyclerView= (ShimmerRecyclerView) rootView.findViewById(R.id.cardList);
        recyclerView.setVisibility(View.GONE);
        empty_view= (TextView) rootView.findViewById(R.id.empty_view);
        noInternet_view= (TextView) rootView.findViewById(R.id.noiternet_view);
        swipeRefresh= (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefresh);
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait");
        querry= Prefs.getString("querry1",null);


        swipeRefresh.setColorSchemeResources(R.color.faveo_blue);
        if (InternetReceiver.isConnected()) {
            if (querry.equals("")||querry.equals("null")){
                Log.d("QUERRY","No Querry");
                recyclerView.setVisibility(View.GONE);
            }
            else{
                noInternet_view.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                progressDialog.show();
                getActivity().getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                );
                new FetchClients(getActivity(),querry).execute();
            }
//                noInternet_view.setVisibility(View.GONE);
//                // swipeRefresh.setRefreshing(true);
//                progressDialog.show();
//                new FetchFirst(getActivity(),querry).execute();
        } else {
            noInternet_view.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            empty_view.setVisibility(View.GONE);
        }
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (InternetReceiver.isConnected()) {
                    if (querry.equals("")||querry.equals("null")){
                        Log.d("QUERRY","No Querry");
                        recyclerView.setVisibility(View.GONE);
                        swipeRefresh.setRefreshing(false);
                    }
                    else{
                        noInternet_view.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        progressDialog.show();
                        getActivity().getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                        );
                        new FetchClients(getActivity(),querry).execute();
                    }
//                        loading = true;
//                        recyclerView.setVisibility(View.VISIBLE);
//                        noInternet_view.setVisibility(View.GONE);
//                        if (ticketOverviewList.size() != 0) {
////                            ticketThreadList.clear();
////                            ticketThreadAdapter.notifyDataSetChanged();
////                            task = new Conversation.FetchTicketThreads(getActivity());
////                            task.execute();
//                        }
                } else {
                    recyclerView.setVisibility(View.INVISIBLE);
                    swipeRefresh.setRefreshing(false);
                    empty_view.setVisibility(View.GONE);
                    noInternet_view.setVisibility(View.VISIBLE);
                }

            }
        });
        return rootView;
    }


    private class FetchClients extends AsyncTask<String, Void, String> {
        Context context;
        String querry;

        FetchClients(Context context,String querry) {
            this.context = context;
            this.querry=querry;
        }

        protected String doInBackground(String... urls) {
            String result = new Helpdesk().getUser(querry);
            if (result == null)
                return null;
            String data;
            clientOverviewList.clear();
            try {
                JSONObject jsonObject = new JSONObject(result);
                //JSONObject jsonObject1=jsonObject.getJSONObject("result");
                //total=jsonObject1.getInt("total");
                data = jsonObject.getString("users");
                //nextPageURL = jsonObject1.getString("next_page_url");
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    ClientOverview clientOverview = Helper.parseSearchClientOverview(jsonArray, i);
                    if (clientOverview != null)
                        clientOverviewList.add(clientOverview);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "success";
        }

        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            //url="null";
            textView.setVisibility(View.GONE);
            //Prefs.putString("customerfilter","null");
            //textView.setText(""+total+" clients");
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
            // recyclerView = (ShimmerRecyclerView) rootView.findViewById(R.id.cardList);
            recyclerView.setHasFixedSize(false);
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
//            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                    if (dy > 0) {
//                        visibleItemCount = linearLayoutManager.getChildCount();
//                        totalItemCount = linearLayoutManager.getItemCount();
//                        pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();
//                        if (loading) {
//                            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
//                                loading = false;
//                                new FetchNextPage(getActivity(),nextPageURL,querry).execute();
//                                //Toast.makeText(getActivity(), "Loading!", Toast.LENGTH_SHORT).show();
//
//                                StyleableToast st = new StyleableToast(getContext(), getString(R.string.loading), Toast.LENGTH_SHORT);
//                                st.setBackgroundColor(Color.parseColor("#3da6d7"));
//                                st.setTextColor(Color.WHITE);
//                                st.setIcon(R.drawable.ic_autorenew_black_24dp);
//                                st.spinIcon();
//                                st.setMaxAlpha();
//                                st.show();
//                            }
//                        }
//                    }
//                }
//            });
            clientOverviewAdapter = new ClientOverviewAdapter(getContext(),clientOverviewList);
            recyclerView.setAdapter(clientOverviewAdapter);
            if (clientOverviewAdapter.getItemCount() == 0) {
                empty_view.setVisibility(View.VISIBLE);
            } else empty_view.setVisibility(View.GONE);
        }
    }

    private class FetchNextPage extends AsyncTask<String, Void, String> {
        Context context;
        String querry;
        String url;

        FetchNextPage(Context context,String url,String querry) {
            this.context = context;
            this.url=url;
            this.querry=querry;
        }

        protected String doInBackground(String... urls) {
            if (nextPageURL.equals("null")) {
                return "all done";
            }
            String result = new Helpdesk().nextPageURL(nextPageURL,querry);
            if (result == null)
                return null;
            String data;
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject jsonObject1=jsonObject.getJSONObject("result");
                data = jsonObject1.getString("data");
                nextPageURL = jsonObject1.getString("next_page_url");
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
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        //Toast.makeText(getActivity(), "onResume", Toast.LENGTH_SHORT).show();
        super.onResume();
    }
    @Override
    public void onStart() {
        //Toast.makeText(getActivity(), "onStart", Toast.LENGTH_SHORT).show();
        super.onStart();
    }
}
