package co.helpdesk.faveo.pro.frontend.fragments.ticketDetail;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializer;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.pro.frontend.activities.TicketDetailActivity;
import co.helpdesk.faveo.pro.frontend.adapters.TicketThreadAdapter;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.pro.model.TicketThread;
import es.dmoral.toasty.Toasty;

/**
 *This is the Fragment for showing the conversation details.
 */
public class Conversation extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    AsyncTask<String, Void, String> task;
    @BindView(R.id.cardList)
    ShimmerRecyclerView recyclerView;

    @BindView(R.id.empty_view)
    TextView empty_view;

    @BindView(R.id.noiternet_view)
    TextView noInternet_view;
    @BindView(R.id.totalcount)
    TextView textView;

    View rootView, view;
    LinearLayout linearLayout;
    ProgressDialog progressDialog;
    TicketThreadAdapter ticketThreadAdapter;
    List<TicketThread> ticketThreadList = new ArrayList<>();
    String ticketId;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    String file;
    String type;
    String noOfAttachment;
    StringBuilder stringBuilderName;
    StringBuilder stringBuilderFile;
    String name;
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static Conversation newInstance(String param1, String param2) {
        Conversation fragment = new Conversation();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Conversation() {

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
            Log.d("calledOnCreate", "true");
            rootView = inflater.inflate(R.layout.fragment_recycler, container, false);
            ButterKnife.bind(this, rootView);
            linearLayout = (LinearLayout) rootView.findViewById(R.id.toolbarview);
            linearLayout.setVisibility(View.GONE);
            //ticketId=Prefs.getString("TICKETid",null);
            view = rootView.findViewById(R.id.separationview);
            swipeRefresh.setColorSchemeResources(R.color.faveo_blue);
            swipeRefresh.setRefreshing(false);
            swipeRefresh.setEnabled(false);
            Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar2);
//        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            toolbar.setVisibility(View.GONE);

            //            swipeRefresh.setRefreshing(true);
//            new FetchFirst(getActivity()).execute();
            if (InternetReceiver.isConnected()) {
                if (Prefs.getString("ticketThread", null).equals("")) {
                    noInternet_view.setVisibility(View.GONE);
                    // swipeRefresh.setRefreshing(true);
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage(getString(R.string.pleasewait));
                    swipeRefresh.setRefreshing(true);
                    task = new FetchTicketThreads(getActivity(), Prefs.getString("TICKETid", null));
                    task.execute();
                }
                else {
                    try {
                        progressDialog.dismiss();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    if (InternetReceiver.isConnected()) {
                        String jsonObject2 = Prefs.getString("ticketThread", null);
                        textView.setVisibility(View.GONE);
                        Log.d("jsonObject2", jsonObject2);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(jsonObject2);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONObject jsonObject1 = null;
                        try {
                            jsonObject1 = jsonObject.getJSONObject("data");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = jsonObject1.getJSONArray("threads");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            TicketThread ticketThread = null;
                            try {
                                String clientPicture = null;
                                try {
                                    clientPicture = jsonArray.getJSONObject(i).getJSONObject("user").getString("profile_pic");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
/*                        String clientName = jsonArray.getJSONObject(i).getString("poster");
                        if (clientName.equals("null") || clientName.equals(""))
                            clientName = "NOTE";*/
                                String firstName = jsonArray.getJSONObject(i).getJSONObject("user").getString("first_name");
                                String userName = jsonArray.getJSONObject(i).getJSONObject("user").getString("user_name");
                                String lastName = jsonArray.getJSONObject(i).getJSONObject("user").getString("last_name");
                                JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("attach");
                                stringBuilderName = new StringBuilder();
                                stringBuilderFile = new StringBuilder();
                                if (jsonArray1.length() == 0) {
                                    Prefs.putString("imageBase64", "no attachment");
                                    Log.d("FileBase64", "no attachment");
                                    name = "";
                                } else {
                                    for (int j = 0; j < jsonArray1.length(); j++) {
                                        JSONObject jsonObject5 = jsonArray1.getJSONObject(j);
                                        file = jsonObject5.getString("file");
                                        name = jsonObject5.getString("name");
                                        //stringBuilderFile=new StringBuilder(file);
                                        stringBuilderName.append(name + ",");
                                        stringBuilderFile.append(file + ",");
                                        //stringBuilderFile.append(file+",");
                                        type = jsonObject5.getString("type");
                                    }
                                    //Log.d("FileBase64", file);
                                    name = stringBuilderName.toString();
                                    file = stringBuilderFile.toString();
                                    Log.d("MultipleFileName", stringBuilderName.toString());
                                    noOfAttachment = "" + jsonArray1.length();
                                    Log.d("Total Attachments", "" + jsonArray1.length());
                                }

                                String clientName = firstName + " " + lastName;
                                String f = "", l = "";
                                if (firstName.trim().length() != 0) {
                                    f = firstName.substring(0, 1);
                                }
                                if (lastName.trim().length() != 0) {
                                    l = lastName.substring(0, 1);
                                }
//                        if ((clientName.equals("null null") || clientName.equals(""))&&userName.equals("")){
//                            clientName="system";
//                        }else
                                if (firstName.equals("null") && lastName.equals("null") && userName.equals("null")) {
                                    clientName = "System Generated";
                                } else if (clientName.equals("") && userName.equals("null") && userName.equals("null")) {
                                    clientName = "System Generated";
                                } else if ((firstName.equals("null")) && (lastName.equals("null")) && (userName != null)) {
                                    clientName = userName;
                                } else if (firstName.equals("") && (lastName.equals("")) && (userName != null)) {
                                    clientName = userName;
                                } else if (firstName != null || lastName != null) {
                                    clientName = firstName + " " + lastName;
                                }
                                String messageTime = jsonArray.getJSONObject(i).getString("created_at");
                                String messageTitle = jsonArray.getJSONObject(i).getString("title");
                                String message = jsonArray.getJSONObject(i).getString("body");
                                Log.d("body:", message);
                                //String isReply = jsonArray.getJSONObject(i).getString("is_internal").equals("0") ? "false" : "true";
                                String isReply = "0";
                                ticketThread = new TicketThread(clientPicture, clientName, messageTime, messageTitle, message, isReply, f + l, name, file, type, noOfAttachment);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (ticketThread != null)
                                ticketThreadList.add(ticketThread);
                        }
                    }
                    recyclerView.setHasFixedSize(false);
                    final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(linearLayoutManager);

                    //Collections.reverse(ticketThreadList);
                    ticketThreadAdapter = new TicketThreadAdapter(getContext(), ticketThreadList);
                    recyclerView.setAdapter(ticketThreadAdapter);
                }
            } else {
                noInternet_view.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                empty_view.setVisibility(View.GONE);
            }

//            swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//                @Override
//                public void onRefresh() {
//                    if (InternetReceiver.isConnected()) {
////                        loading = true;
//                        recyclerView.setVisibility(View.VISIBLE);
//                        noInternet_view.setVisibility(View.GONE);
//                        if (ticketThreadList.size() != 0) {
//                            ticketThreadList.clear();
//                            ticketThreadAdapter.notifyDataSetChanged();
////                            progressDialog=new ProgressDialog(getActivity());
////                            progressDialog.setMessage(getString(R.string.pleasewait));
////                            progressDialog.show();
//                            task = new FetchTicketThreads(getActivity(),Prefs.getString("TICKETid", null));
//                            task.execute();
//                        }
//                    } else {
//                        recyclerView.setVisibility(View.INVISIBLE);
//                        swipeRefresh.setRefreshing(false);
//                        empty_view.setVisibility(View.GONE);
//                        noInternet_view.setVisibility(View.VISIBLE);
//                    }
//
//                }
//            });
        }

        return rootView;
    }


    class FetchTicketThreads extends AsyncTask<String, Void, String> {
        Context context;
        String ticketID;


        FetchTicketThreads(Context context, String ticketID) {
            this.context = context;
            this.ticketID = ticketID;
        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().getTicketThread(ticketID);
        }

        protected void onPostExecute(String result) {
            try {
                swipeRefresh.setRefreshing(false);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            textView.setVisibility(View.GONE);
            if (swipeRefresh.isRefreshing())
                swipeRefresh.setRefreshing(false);
            if (result == null) {
                //Toasty.error(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(result);
                Prefs.putString("ticketThread", jsonObject.toString());
//                PreferenceManager.getDefaultSharedPreferences(context).edit()
//                        .putString("theJson",jsonObject.toString()).apply();
//                String jsonObject2 = PreferenceManager.
//                        getDefaultSharedPreferences(getActivity()).getString("theJson","");
//                Log.d("jsonObject2",jsonObject2);
                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                JSONArray jsonArray = jsonObject1.getJSONArray("threads");
                for (int i = 0; i < jsonArray.length(); i++) {
                    TicketThread ticketThread = null;
                    try {
                        String clientPicture = null;
                        try {
                            clientPicture = jsonArray.getJSONObject(i).getJSONObject("user").getString("profile_pic");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
/*                        String clientName = jsonArray.getJSONObject(i).getString("poster");
                        if (clientName.equals("null") || clientName.equals(""))
                            clientName = "NOTE";*/
                        String firstName = jsonArray.getJSONObject(i).getJSONObject("user").getString("first_name");
                        String userName = jsonArray.getJSONObject(i).getJSONObject("user").getString("user_name");
                        String lastName = jsonArray.getJSONObject(i).getJSONObject("user").getString("last_name");
                        JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("attach");
                        stringBuilderName = new StringBuilder();
                        stringBuilderFile = new StringBuilder();
                        if (jsonArray1.length() == 0) {
                            Prefs.putString("imageBase64", "no attachment");
                            Log.d("FileBase64", "no attachment");
                            name = "";
                        } else {
                            for (int j = 0; j < jsonArray1.length(); j++) {
                                JSONObject jsonObject5 = jsonArray1.getJSONObject(j);
                                file = jsonObject5.getString("file");
                                name = jsonObject5.getString("name");
                                //stringBuilderFile=new StringBuilder(file);
                                stringBuilderName.append(name + ",");
                                stringBuilderFile.append(file + ",");
                                //stringBuilderFile.append(file+",");
                                type = jsonObject5.getString("type");


                            }
                            //Log.d("FileBase64", file);
                            name = stringBuilderName.toString();
                            file = stringBuilderFile.toString();
                            Log.d("MultipleFileName", stringBuilderName.toString());
                            noOfAttachment = "" + jsonArray1.length();
                            Log.d("Total Attachments", "" + jsonArray1.length());
                        }

                        String clientName = firstName + " " + lastName;
                        String f = "", l = "";
                        if (firstName.trim().length() != 0) {
                            f = firstName.substring(0, 1);
                        }
                        if (lastName.trim().length() != 0) {
                            l = lastName.substring(0, 1);
                        }
//                        if ((clientName.equals("null null") || clientName.equals(""))&&userName.equals("")){
//                            clientName="system";
//                        }else
                        if (firstName.equals("null") && lastName.equals("null") && userName.equals("null")) {
                            clientName = "System Generated";
                        } else if (clientName.equals("") && userName.equals("null") && userName.equals("null")) {
                            clientName = "System Generated";
                        } else if ((firstName.equals("null")) && (lastName.equals("null")) && (userName != null)) {
                            clientName = userName;
                        } else if (firstName.equals("") && (lastName.equals("")) && (userName != null)) {
                            clientName = userName;
                        } else if (firstName != null || lastName != null) {
                            clientName = firstName + " " + lastName;
                        }
                        String messageTime = jsonArray.getJSONObject(i).getString("created_at");
                        String messageTitle = jsonArray.getJSONObject(i).getString("title");
                        String message = jsonArray.getJSONObject(i).getString("body");
                        Log.d("body:", message);
                        //String isReply = jsonArray.getJSONObject(i).getString("is_internal").equals("0") ? "false" : "true";
                        String isReply = "0";
                        ticketThread = new TicketThread(clientPicture, clientName, messageTime, messageTitle, message, isReply, f + l, name, file, type, noOfAttachment);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (ticketThread != null)
                        ticketThreadList.add(ticketThread);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            recyclerView.setHasFixedSize(false);
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            //Collections.reverse(ticketThreadList);
            ticketThreadAdapter = new TicketThreadAdapter(getContext(), ticketThreadList);
            recyclerView.setAdapter(ticketThreadAdapter);
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

//    public void addThreadAndUpdate(TicketThread thread) {
//        ticketThreadList.add(0, thread);
//        ticketThreadAdapter.notifyDataSetChanged();
//    }

//    public void refresh() {
//        if (InternetReceiver.isConnected()) {
////                        loading = true;
//            recyclerView.setVisibility(View.VISIBLE);
//            noInternet_view.setVisibility(View.GONE);
//            if (ticketThreadList.size() != 0) {
//                ticketThreadList.clear();
//                ticketThreadAdapter.notifyDataSetChanged();
//                progressDialog = new ProgressDialog(getActivity());
//                progressDialog.setMessage(getString(R.string.pleasewait));
//                progressDialog.show();
//                if (Prefs.getString("ticketThread", null).equals("")) {
//                    noInternet_view.setVisibility(View.GONE);
//                    task = new FetchTicketThreads(getActivity(), Prefs.getString("TICKETid", null));
//                    task.execute();
//                }
//            }
//        }
//    }

    public void refresh() {

        if (InternetReceiver.isConnected()) {
//                        loading = true;
            recyclerView.setVisibility(View.VISIBLE);
            noInternet_view.setVisibility(View.GONE);
            if (ticketThreadList.size() != 0) {
                ticketThreadList.clear();
                ticketThreadAdapter.notifyDataSetChanged();
                progressDialog=new ProgressDialog(getActivity());
                progressDialog.setMessage(getString(R.string.pleasewait));
                swipeRefresh.setRefreshing(true);
                if (Prefs.getString("ticketThread", null).equals("")) {
                    noInternet_view.setVisibility(View.GONE);
                    task = new FetchTicketThreads(getActivity(), Prefs.getString("TICKETid", null));
                    task.execute();
                }
                else {
                    try {
                        swipeRefresh.setRefreshing(false);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                        ticketThreadList.clear();
                        ticketThreadAdapter.notifyDataSetChanged();
                        String jsonObject2 = Prefs.getString("ticketThread", null);
                        textView.setVisibility(View.GONE);
                        Log.d("ThreadConversation", jsonObject2);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(jsonObject2);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONObject jsonObject1 = null;
                        try {
                            jsonObject1 = jsonObject.getJSONObject("data");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = jsonObject1.getJSONArray("threads");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            TicketThread ticketThread = null;
                            try {
                                String clientPicture = null;
                                try {
                                    clientPicture = jsonArray.getJSONObject(i).getJSONObject("user").getString("profile_pic");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
/*                        String clientName = jsonArray.getJSONObject(i).getString("poster");
                        if (clientName.equals("null") || clientName.equals(""))
                            clientName = "NOTE";*/
                                String firstName = jsonArray.getJSONObject(i).getJSONObject("user").getString("first_name");
                                String userName = jsonArray.getJSONObject(i).getJSONObject("user").getString("user_name");
                                String lastName = jsonArray.getJSONObject(i).getJSONObject("user").getString("last_name");
                                JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("attach");
                                stringBuilderName = new StringBuilder();
                                stringBuilderFile = new StringBuilder();
                                if (jsonArray1.length() == 0) {
                                    Prefs.putString("imageBase64", "no attachment");
                                    Log.d("FileBase64", "no attachment");
                                    name = "";
                                } else {
                                    for (int j = 0; j < jsonArray1.length(); j++) {
                                        JSONObject jsonObject5 = jsonArray1.getJSONObject(j);
                                        file = jsonObject5.getString("file");
                                        name = jsonObject5.getString("name");
                                        //stringBuilderFile=new StringBuilder(file);
                                        stringBuilderName.append(name + ",");
                                        stringBuilderFile.append(file + ",");
                                        //stringBuilderFile.append(file+",");
                                        type = jsonObject5.getString("type");
                                    }
                                    //Log.d("FileBase64", file);
                                    name = stringBuilderName.toString();
                                    file = stringBuilderFile.toString();
                                    Log.d("MultipleFileName", stringBuilderName.toString());
                                    noOfAttachment = "" + jsonArray1.length();
                                    Log.d("Total Attachments", "" + jsonArray1.length());
                                }

                                String clientName = firstName + " " + lastName;
                                String f = "", l = "";
                                if (firstName.trim().length() != 0) {
                                    f = firstName.substring(0, 1);
                                }
                                if (lastName.trim().length() != 0) {
                                    l = lastName.substring(0, 1);
                                }
//                        if ((clientName.equals("null null") || clientName.equals(""))&&userName.equals("")){
//                            clientName="system";
//                        }else
                                if (firstName.equals("null") && lastName.equals("null") && userName.equals("null")) {
                                    clientName = "System Generated";
                                } else if (clientName.equals("") && userName.equals("null") && userName.equals("null")) {
                                    clientName = "System Generated";
                                } else if ((firstName.equals("null")) && (lastName.equals("null")) && (userName != null)) {
                                    clientName = userName;
                                } else if (firstName.equals("") && (lastName.equals("")) && (userName != null)) {
                                    clientName = userName;
                                } else if (firstName != null || lastName != null) {
                                    clientName = firstName + " " + lastName;
                                }
                                String messageTime = jsonArray.getJSONObject(i).getString("created_at");
                                String messageTitle = jsonArray.getJSONObject(i).getString("title");
                                String message = jsonArray.getJSONObject(i).getString("body");
                                Log.d("body:", message);
                                //String isReply = jsonArray.getJSONObject(i).getString("is_internal").equals("0") ? "false" : "true";
                                String isReply = "0";
                                ticketThread = new TicketThread(clientPicture, clientName, messageTime, messageTitle, message, isReply, f + l, name, file, type, noOfAttachment);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (ticketThread != null)
                                ticketThreadList.add(ticketThread);
                        }
                    }
                    recyclerView.setHasFixedSize(false);
                    final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(linearLayoutManager);

                    //Collections.reverse(ticketThreadList);
                    ticketThreadAdapter = new TicketThreadAdapter(getContext(), ticketThreadList);
                    recyclerView.setAdapter(ticketThreadAdapter);
                }

        } else {
            recyclerView.setVisibility(View.INVISIBLE);
            swipeRefresh.setRefreshing(false);
            empty_view.setVisibility(View.GONE);
            noInternet_view.setVisibility(View.VISIBLE);
        }
    }

//    @Override
//    public void onPause() {
//        if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
//            task.cancel(true);
//            Log.d("Async Detail", "Cancelled");
//        }
//        super.onPause();
//    }



    @Override
    public void onStart() {
        Log.d("calledOnStart", "true");
        super.onStart();
//            noInternet_view.setVisibility(View.GONE);
        // swipeRefresh.setRefreshing(true);
        //Log.d("TICKETid",Prefs.getString("TICKETid", null));
        //refresh();
//            task = new FetchTicketThreads(getActivity(),Prefs.getString("TICKETid", null));
//            task.execute();
    }

    @Override
    public void onResume() {
        Log.d("calledOnResume", "true");
        refresh();
        super.onResume();
    }
//            noInternet_view.setVisibility(View.GONE);
        // swipeRefresh.setRefreshing(true);
        //Log.d("TICKETid",Prefs.getString("TICKETid", null));
        //refresh();
//            task = new FetchTicketThreads(getActivity(),Prefs.getString("TICKETid", null));
//            task.execute();
    }


