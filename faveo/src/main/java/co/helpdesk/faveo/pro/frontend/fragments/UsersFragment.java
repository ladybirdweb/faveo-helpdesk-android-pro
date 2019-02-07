package co.helpdesk.faveo.pro.frontend.fragments;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    public String mParam1;
    public String mParam2;
    static String nextPageURL = "";
    TextView empty_view;
    TextView noInternet_view;
    SwipeRefreshLayout swipeRefresh;
    ClientOverviewAdapter clientOverviewAdapter;
    TextView textView;
    List<ClientOverview> clientOverviewList = new ArrayList<>();
    ProgressDialog progressDialog;
    String querry;
    LinearLayout toolbarView;
    boolean loading = true;

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
        empty_view.setVisibility(View.VISIBLE);
        noInternet_view= (TextView) rootView.findViewById(R.id.noiternet_view);
        swipeRefresh= (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefresh);
        swipeRefresh.setRefreshing(false);
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait");
        querry= Prefs.getString("querry",null);
        swipeRefresh.setColorSchemeResources(R.color.faveo_blue);
//        if (!isViewShown) {
//                    if (InternetReceiver.isConnected()) {
//            if (querry.equals("")||querry.equals("null")){
//                Log.d("QUERRY","No Querry");
//                recyclerView.setVisibility(View.GONE);
//                empty_view.setVisibility(View.VISIBLE);
//                empty_view.setText(getString(R.string.noUser));
//            }
//            else{
//                noInternet_view.setVisibility(View.GONE);
//                recyclerView.setVisibility(View.VISIBLE);
//                empty_view.setVisibility(View.GONE);
//                swipeRefresh.setRefreshing(true);
//                //progressDialog.show();
//                getActivity().getWindow().setSoftInputMode(
//                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
//                );
//                new FetchClients(getActivity(),querry).execute();
//            }
////                noInternet_view.setVisibility(View.GONE);
////                // swipeRefresh.setRefreshing(true);
////                progressDialog.show();
////                new FetchFirst(getActivity(),querry).execute();
//        } else {
//            noInternet_view.setVisibility(View.VISIBLE);
//            recyclerView.setVisibility(View.INVISIBLE);
//            empty_view.setVisibility(View.GONE);
//        }
//        }else {
//            Log.d("noneedtoloaddata","true");
//        }
//        if (InternetReceiver.isConnected()) {
//            if (querry.equals("")||querry.equals("null")){
//                Log.d("QUERRY","No Querry");
//                recyclerView.setVisibility(View.GONE);
//                empty_view.setVisibility(View.VISIBLE);
//                empty_view.setText(getString(R.string.noUser));
//            }
//            else{
//                noInternet_view.setVisibility(View.GONE);
//                recyclerView.setVisibility(View.VISIBLE);
//                empty_view.setVisibility(View.GONE);
//                //progressDialog.show();
//                getActivity().getWindow().setSoftInputMode(
//                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
//                );
//                new FetchClients(getActivity(),querry).execute();
//            }
////                noInternet_view.setVisibility(View.GONE);
////                // swipeRefresh.setRefreshing(true);
////                progressDialog.show();
////                new FetchFirst(getActivity(),querry).execute();
//        } else {
//            noInternet_view.setVisibility(View.VISIBLE);
//            recyclerView.setVisibility(View.INVISIBLE);
//            empty_view.setVisibility(View.GONE);
//        }

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (InternetReceiver.isConnected()) {
                    if (querry.equals("")||querry.equals("null")){
                        Log.d("QUERRY","No Querry");
                        recyclerView.setVisibility(View.GONE);
                        empty_view.setVisibility(View.VISIBLE);
                        //empty_view.setText(getString(R.string.noUser));
                        swipeRefresh.setRefreshing(false);
                    }
                    else{
                        noInternet_view.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        //progressDialog.show();
                        empty_view.setVisibility(View.GONE);
                        getActivity().getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                        );
                        new FetchClients(getActivity(),querry).execute();
                    }
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

//    @Override
//    public void setMenuVisibility(final boolean visible) {
////        try {
////            if (visible) {
////                //Do your stuff here
////                //Log.d("visible","true");
////                if (InternetReceiver.isConnected()) {
////                    if (querry.equals("") || querry.equals("null")) {
////                        Log.d("QUERRY", "No Querry");
////                        recyclerView.setVisibility(View.GONE);
////                        empty_view.setVisibility(View.VISIBLE);
////                        empty_view.setText(getString(R.string.noUser));
////                    } else {
////                        noInternet_view.setVisibility(View.GONE);
////                        recyclerView.setVisibility(View.VISIBLE);
////                        empty_view.setVisibility(View.GONE);
////                        //progressDialog.show();
////                        getActivity().getWindow().setSoftInputMode(
////                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
////                        );
////                        new FetchClients(getActivity(), querry).execute();
////                    }
//////                noInternet_view.setVisibility(View.GONE);
//////                // swipeRefresh.setRefreshing(true);
//////                progressDialog.show();
//////                new FetchFirst(getActivity(),querry).execute();
////                } else {
////                    noInternet_view.setVisibility(View.VISIBLE);
////                    recyclerView.setVisibility(View.INVISIBLE);
////                    empty_view.setVisibility(View.GONE);
////                }
////            } else {
////
////                if (InternetReceiver.isConnected()) {
////                    if (querry.equals("") || querry.equals("null")) {
////                        Log.d("QUERRY", "No Querry");
////                        recyclerView.setVisibility(View.GONE);
////                        empty_view.setVisibility(View.VISIBLE);
////                        empty_view.setText(getString(R.string.noUser));
////                    } else {
////                        noInternet_view.setVisibility(View.GONE);
////                        recyclerView.setVisibility(View.VISIBLE);
////                        empty_view.setVisibility(View.GONE);
////                        //progressDialog.show();
////                        getActivity().getWindow().setSoftInputMode(
////                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
////                        );
////                        new FetchClients(getActivity(), querry).execute();
////                    }
//////                noInternet_view.setVisibility(View.GONE);
//////                // swipeRefresh.setRefreshing(true);
//////                progressDialog.show();
//////                new FetchFirst(getActivity(),querry).execute();
////                } else {
////                    noInternet_view.setVisibility(View.VISIBLE);
////                    recyclerView.setVisibility(View.INVISIBLE);
////                    empty_view.setVisibility(View.GONE);
////                }
////                //Log.d("visible","false");
////            }
////
////            super.setMenuVisibility(visible);
////        }catch (NullPointerException e){
////            e.printStackTrace();
////        }
//    }
    private class FetchClients extends AsyncTask<String, Void, String> {
        Context context;
        String querry;
        int total=0;
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
                Prefs.putString("searchUser",jsonObject.toString());
                //JSONObject jsonObject1=jsonObject.getJSONObject("result");
                //total=jsonObject1.getInt("total");
                data = jsonObject.getString("users");
                //nextPageURL = jsonObject1.getString("next_page_url");
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    total++;
                    ClientOverview clientOverview = Helper.parseSearchClientOverview(jsonArray, i);
                    if (clientOverview != null)
                        clientOverviewList.add(clientOverview);
                }

                Log.d("total",""+total);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "success";
        }

        protected void onPostExecute(String result) {
           // progressDialog.dismiss();
            swipeRefresh.setRefreshing(false);
            textView.setVisibility(View.VISIBLE);
            textView.setText(total+" users");
            //url="null";
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
            runLayoutAnimation(recyclerView);
            recyclerView.setAdapter(clientOverviewAdapter);
            if (clientOverviewAdapter.getItemCount() == 0) {
                empty_view.setVisibility(View.VISIBLE);
            } else empty_view.setVisibility(View.GONE);
        }
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_right);
        recyclerView.setLayoutAnimation(controller);
        clientOverviewAdapter.notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }



//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (getView() != null) {
//            isViewShown = true;
//             //fetchdata() contains logic to show data when page is selected mostly asynctask to fill the data
//                    if (InternetReceiver.isConnected()) {
//            if (querry.equals("")||querry.equals("null")){
//                Log.d("QUERRY","No Querry");
//                recyclerView.setVisibility(View.GONE);
//                empty_view.setVisibility(View.VISIBLE);
//                empty_view.setText(getString(R.string.noUser));
//            }
//            else{
//                noInternet_view.setVisibility(View.GONE);
//                recyclerView.setVisibility(View.VISIBLE);
//                empty_view.setVisibility(View.GONE);
//                swipeRefresh.setRefreshing(true);
//                //progressDialog.show();
//                getActivity().getWindow().setSoftInputMode(
//                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
//                );
//                new FetchClients(getActivity(),querry).execute();
//            }
////                noInternet_view.setVisibility(View.GONE);
////                // swipeRefresh.setRefreshing(true);
////                progressDialog.show();
////                new FetchFirst(getActivity(),querry).execute();
//        } else {
//            noInternet_view.setVisibility(View.VISIBLE);
//            recyclerView.setVisibility(View.INVISIBLE);
//            empty_view.setVisibility(View.GONE);
//        }
//
//        } else {
//            isViewShown = false;
//        }
//    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        try {
            if (visible) {
                Log.d("visibilty", "visible");
                if (InternetReceiver.isConnected()) {
                    if (querry.equals("") || querry.equals("null")) {
                        Log.d("QUERRY", "No Querry");
                        recyclerView.setVisibility(View.GONE);
                        empty_view.setVisibility(View.GONE);
                        //empty_view.setText(getString(R.string.noUser));
                    } else {
                        noInternet_view.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        empty_view.setVisibility(View.GONE);
                        swipeRefresh.setRefreshing(true);
                        //progressDialog.show();
                        getActivity().getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                        );
                        new FetchClients(getActivity(), querry).execute();
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
            } else {
                Log.d("noneedtoloaddata", "true");
            }
            super.setMenuVisibility(visible);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        //Toast.makeText(getActivity(), "onResume", Toast.LENGTH_SHORT).show();
        Log.d("onResumeOfUsers","called");
//        try {
//            lastQuerry = Prefs.getString("searchUser", null);
//            if (InternetReceiver.isConnected()) {
//                if (querry.equals("") || querry.equals("null")) {
//                    Log.d("QUERRY", "No Querry");
//                    recyclerView.setVisibility(View.GONE);
//                    empty_view.setVisibility(View.VISIBLE);
//                    empty_view.setText(getString(R.string.noUser));
//                } else {
//                    if (lastQuerry.equals(null) || lastQuerry.equals("")) {
//                        noInternet_view.setVisibility(View.GONE);
//                        recyclerView.setVisibility(View.VISIBLE);
//                        empty_view.setVisibility(View.GONE);
//                        //progressDialog.show();
//                        getActivity().getWindow().setSoftInputMode(
//                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
//                        );
//                        new FetchClients(getActivity(), querry).execute();
//                    }
//                    else{
//                        Log.d("lastQuerry",lastQuerry);
//                        String result=Prefs.getString("searchUser",null);
//                        String data;
//                        clientOverviewList.clear();
//                        try {
//                            JSONObject jsonObject = new JSONObject(result);
//                            data = jsonObject.getString("users");
//                            JSONArray jsonArray = new JSONArray(data);
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                total++;
//                                ClientOverview clientOverview = Helper.parseSearchClientOverview(jsonArray, i);
//                                if (clientOverview != null)
//                                    clientOverviewList.add(clientOverview);
//                            }
//
//                            Log.d("total",""+total);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        //progressDialog.dismiss();
//                        textView.setVisibility(View.VISIBLE);
//                        textView.setText(total+" users");
//                        //url="null";
//                        //Prefs.putString("customerfilter","null");
//                        //textView.setText(""+total+" clients");
//                        if (swipeRefresh.isRefreshing())
//                            swipeRefresh.setRefreshing(false);
//
//                        if (result == null) {
//                            Toasty.error(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
//                            return;
//                        }
//                        if (result.equals("all done")) {
//
//                            Toasty.info(getActivity(), getString(R.string.all_caught_up), Toast.LENGTH_SHORT).show();
//                            //return;
//                        }
//                        // recyclerView = (ShimmerRecyclerView) rootView.findViewById(R.id.cardList);
//                        recyclerView.setHasFixedSize(false);
//                        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//                        recyclerView.setLayoutManager(linearLayoutManager);
//                        clientOverviewAdapter = new ClientOverviewAdapter(getContext(),clientOverviewList);
//                        recyclerView.setAdapter(clientOverviewAdapter);
//                        if (clientOverviewAdapter.getItemCount() == 0) {
//                            empty_view.setVisibility(View.VISIBLE);
//                        } else empty_view.setVisibility(View.GONE);
//            }
//        }
//            }
//            else {
//                noInternet_view.setVisibility(View.VISIBLE);
//                recyclerView.setVisibility(View.INVISIBLE);
//                empty_view.setVisibility(View.GONE);
//            }
//        }catch (NullPointerException e){
//            e.printStackTrace();
//        }
        super.onResume();
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.d("visible","TRUE");
    }
}
