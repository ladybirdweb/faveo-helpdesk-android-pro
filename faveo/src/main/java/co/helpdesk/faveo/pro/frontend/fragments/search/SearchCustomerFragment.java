package co.helpdesk.faveo.pro.frontend.fragments.search;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import co.helpdesk.faveo.pro.frontend.adapters.ClientOverviewAdapter;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.pro.model.ClientOverview;

/**
 * Created by narendra on 21/02/17.
 */

public class SearchCustomerFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.cardList)
    RecyclerView recyclerView;
    @BindView(R.id.empty_view)
    TextView empty_view;
    @BindView(R.id.noiternet_view)
    TextView noInternet_view;
    View rootView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    private boolean loading = true;
    private OnFragmentInteractionListener mListener;
    ClientOverviewAdapter clientOverviewAdapter;
    List<ClientOverview> clientOverviewList = new ArrayList<>();

    private String mParam1;
    private String mParam2;

    public static SearchCustomerFragment newInstance() {
        SearchCustomerFragment fragment = new SearchCustomerFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public SearchCustomerFragment() {
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_recycler, container, false);
            ButterKnife.bind(this, rootView);

            swipeRefresh.setColorSchemeResources(R.color.faveo_blue);
            if (InternetReceiver.isConnected()) {
                noInternet_view.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(true);
                new FetchClients(getActivity()).execute();
            } else
                noInternet_view.setVisibility(View.VISIBLE);

            swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (InternetReceiver.isConnected()) {
                        loading = true;
                        recyclerView.setVisibility(View.VISIBLE);
                        noInternet_view.setVisibility(View.GONE);
                        new FetchClients(getActivity()).execute();
                    } else {
                        recyclerView.setVisibility(View.INVISIBLE);
                        swipeRefresh.setRefreshing(false);
                        empty_view.setVisibility(View.GONE);
                        noInternet_view.setVisibility(View.VISIBLE);
                    }
                }
            });

            empty_view.setText(R.string.no_results_found);
        }
        return rootView;
    }

    public class FetchClients extends AsyncTask<String, Void, String> {
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
                data = jsonObject.getString("data");
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
            if (swipeRefresh.isRefreshing())
                swipeRefresh.setRefreshing(false);

            if (result == null) {
                Toast.makeText(getActivity(),getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }
            if (result.equals("all done")) {

                Toast.makeText(context, getString(R.string.all_caught_up), Toast.LENGTH_SHORT).show();
                //return;
            }
            recyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);
            recyclerView.setHasFixedSize(false);
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);

            clientOverviewAdapter = new ClientOverviewAdapter(getContext(),clientOverviewList);
            recyclerView.setAdapter(clientOverviewAdapter);
            if (clientOverviewAdapter.getItemCount() == 0) {
                empty_view.setVisibility(View.VISIBLE);
            } else empty_view.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.client4:
                Intent intent = new Intent(getActivity(), ClientDetailActivity.class);
                startActivity(intent);
                break;
        }
    }

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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
