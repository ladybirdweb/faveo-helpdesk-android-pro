package co.helpdesk.faveo.pro.frontend.fragments.ticketDetail;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import co.helpdesk.faveo.pro.Helper;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.pro.frontend.activities.TicketDetailActivity;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.pro.model.Data;
import es.dmoral.toasty.Toasty;

/**
 * This is the Fragment for showing the ticket details.
 */
public class Detail extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView tv_helpTopic, tv_dept;
    AsyncTask<String, Void, String> task;
    int paddingTop, paddingBottom;
    EditText editTextSubject, editTextFirstName, editTextEmail,
            editTextDueDate, editTextCreatedDate;

    Spinner spinnerSLAPlans, spinnerType, spinnerStatus, spinnerSource,
            spinnerPriority, spinnerHelpTopics;
    ProgressDialog progressDialog;
    ArrayList<Data> helptopicItems, priorityItems, typeItems, sourceItems,staffItems;
    ArrayAdapter<Data> spinnerPriArrayAdapter, spinnerHelpArrayAdapter, spinnerTypeArrayAdapter, spinnerSourceArrayAdapter,staffArrayAdapter;
    Animation animation;
    @BindView(R.id.spinner_staffs)
    Spinner spinnerStaffs;
    String ticketId;
    public String mParam1;
    public String mParam2;
    String ticketID;
    private OnFragmentInteractionListener mListener;

    public static Detail newInstance(String param1, String param2) {
        Detail fragment = new Detail();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Detail() {
    }

    @Override
    public void onPause() {
        if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
            task.cancel(true);
            Log.d("Async Detail", "Cancelled");
        }
        super.onPause();
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
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        setUpViews(rootView);
        spinnerStaffs.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        final Intent intent = getActivity().getIntent();
        ticketID=intent.getStringExtra("ticket_id");
        Prefs.putString("TICKETid",ticketID);
        Prefs.putString("ticketId",ticketID);
        ticketId=Prefs.getString("TICKETid",null);
        animation= AnimationUtils.loadAnimation(getActivity(),R.anim.shake_error);
        // progressDialog.show();
        if (InternetReceiver.isConnected()) {
            task = new FetchTicketDetail(Prefs.getString("TICKETid", null));
            task.execute();
        }
        return rootView;
    }

    private class FetchTicketDetail extends AsyncTask<String, Void, String> {
        String ticketID;

        FetchTicketDetail(String ticketID) {

            this.ticketID = ticketID;
        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().getTicketDetail(ticketID);
        }

        @SuppressLint("SetTextI18n")
        protected void onPostExecute(String result) {
            if (isCancelled()) return;
            if (result == null) {
                Log.d("thisSomething","true");
                Toasty.error(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                JSONObject jsonObject2=jsonObject1.getJSONObject("ticket");
                String title=jsonObject2.getString("title");
                Log.d("Title",title);
                if (title.startsWith("=?UTF-8?Q?") && title.endsWith("?=")) {
                    String first = title.replace("=?UTF-8?Q?", "");
                    String second = first.replace("_", " ");
                    String second1=second.replace("=C3=BA","");
                    String third = second1.replace("=C2=A0", "");
                    String fourth = third.replace("?=", "");
                    String fifth = fourth.replace("=E2=80=99", "'");
                    String sixth=fifth.replace("=3F","?");
                    editTextSubject.setText(sixth);

                } else {
                    editTextSubject.setText(title);
                }
                //editTextSubject.setText(title);
                String statusName=jsonObject2.getString("status_name");
                String ticketNumber = jsonObject2.getString("ticket_number");
                String assignee=jsonObject2.getString("assignee");
                JSONObject jsonObject4=jsonObject2.getJSONObject("from");
                if (assignee.equals(null)||assignee.equals("null")||assignee.equals("")){
                    spinnerStaffs.setSelection(0);
                }
                else{
                    JSONObject jsonObject3=jsonObject2.getJSONObject("assignee");
                    try {
                        if (jsonObject3.getString("first_name") != null&&jsonObject3.getString("last_name") != null) {
                            //spinnerHelpTopics.setSelection(getIndex(spinnerHelpTopics, jsonObject1.getString("helptopic_name")));
                            for (int j=0;j<spinnerStaffs.getCount();j++){
                                if (spinnerStaffs.getItemAtPosition(j).toString().equalsIgnoreCase(jsonObject3.getString("first_name")+" "+jsonObject3.getString("last_name"))) {
                                    spinnerStaffs.setSelection(j);
                                }

                            }
                            //spinnerStaffs.setSelection(staffItems.indexOf("assignee_email"));
                        }
                        //spinnerHelpTopics.setSelection(Integer.parseInt(jsonObject1.getString("helptopic_id")));
                    } catch (ArrayIndexOutOfBoundsException e){
                        e.printStackTrace();
                    } catch (Exception e) {
//                    spinnerHelpTopics.setVisibility(View.GONE);
//                    tv_helpTopic.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                }


//                String name=jsonObject4.getString("first_name ")+jsonObject4.getString("last_name");
                //Log.d("name",name);

                //String ticketStatus=jsonObject1.getString("status_name");
                if (statusName.equals("Open")){
                    Prefs.putString("status_name","Open");
                }
                else if (statusName.equals("Closed")){
                    Prefs.putString("status_name","Closed");
                }
                else  if (statusName.equals("Deleted")){
                    Prefs.putString("status_name","Deleted");
                }
                // textViewTicketNumber.setText(ticketNumber);
                try {
                    ActionBar actionBar = ((TicketDetailActivity) getActivity()).getSupportActionBar();
                    if (actionBar != null) {
                        actionBar.setTitle(ticketNumber == null ? "TicketDetail" : ticketNumber);

                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
                try {
                    if (jsonObject2.getString("priority_name") != null) {
                        // spinnerPriority.setSelection(Integer.parseInt(jsonObject1.getString("priority_id")) - 1);

                        spinnerPriority.setSelection(getIndex(spinnerPriority, jsonObject2.getString("priority_name")));
                        spinnerPriority.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                return true;
                            }
                        });


                    }
                } catch (JSONException | NumberFormatException e) {
                    e.printStackTrace();
                }
                catch (ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                }

                try {
                    if (jsonObject2.getString("type_name") != null) {
                        spinnerType.setSelection(getIndex(spinnerType, jsonObject2.getString("type_name")));
                        spinnerType.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                return true;
                            }
                        });
                    }
                } catch (JSONException | NumberFormatException e) {
                    e.printStackTrace();
                }
                catch (ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                }
                try {
                    if (jsonObject2.getString("helptopic_name") != null)
                        //spinnerHelpTopics.setSelection(getIndex(spinnerHelpTopics, jsonObject1.getString("helptopic_name")));
//                    for (int j=0;j<spinnerHelpTopics.getCount();j++){
//                        if (spinnerHelpTopics.getItemAtPosition(j).toString().equalsIgnoreCase(jsonObject1.getString("helptopic_id"))){
//                            spinnerHelpTopics.setSelection(j);
//                        }
//                    }
                        spinnerHelpTopics.setSelection(getIndex(spinnerHelpTopics, jsonObject2.getString("helptopic_name")));


                    spinnerHelpTopics.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            return true;
                        }
                    });
                } catch (ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                } catch (Exception e) {

                    e.printStackTrace();
                }
                try {
                    if (jsonObject2.getString("source_name") != null)
                        //spinnerSource.setSelection(Integer.parseInt(jsonObject1.getString("source")) - 1);

                        spinnerSource.setSelection(getIndex(spinnerSource, jsonObject2.getString("source_name")));
                    spinnerSource.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            return true;
                        }
                    });
                } catch (JSONException | NumberFormatException e) {
                    e.printStackTrace();
                }
                catch (ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                }
                if (jsonObject2.getString("duedate").equals("") || jsonObject2.getString("duedate") == null || jsonObject2.getString("duedate").equals("null")) {
                    editTextDueDate.setText(getString(R.string.not_available));
                } else {
                    editTextDueDate.setText(Helper.parseDate(jsonObject2.getString("duedate")));
                }

                if (jsonObject2.getString("created_at").equals("") || jsonObject2.getString("created_at") == null) {
                    editTextCreatedDate.setText(getString(R.string.not_available));
                } else {
                    editTextCreatedDate.setText(Helper.parseDate(jsonObject2.getString("created_at")));
                }

//                if (jsonObject2.getString("updated_at").equals("") || jsonObject2.getString("updated_at") == null) {
//                    editTextLastResponseDate.setText(getString(R.string.not_available));
//                } else {
//                    editTextLastResponseDate.setText(Helper.parseDate(jsonObject2.getString("updated_at")));
//                }
                if (jsonObject4.getString("email").equals("") || jsonObject4.getString("email") == null) {
                    editTextEmail.setText(getString(R.string.not_available));
                } else
                    editTextEmail.setText(jsonObject4.getString("email"));


                if (jsonObject4.getString("first_name").equals("") || jsonObject4.getString("last_name") == null) {
                    editTextFirstName.setText(getString(R.string.not_available));
                } else
                    editTextFirstName.setText(jsonObject4.getString("first_name")+" "+jsonObject4.getString("last_name"));




//                if (jsonObject1.getString("last_message").equals("null")) {
//                    editTextLastMessage.setText("Not available");
//                } else
//                    editTextLastMessage.setText(jsonObject1.getString("last_message"));


            } catch (JSONException | IllegalStateException e) {
                e.printStackTrace();
            }
        }

    }

    private int getIndex(Spinner spinner, String myString) {

        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            Log.d("item ", spinner.getItemAtPosition(i).toString());
            if (spinner.getItemAtPosition(i).toString().equals(myString.trim())) {
                index = i;
                break;
            }
        }
        return index;
    }
    private void setUpViews(View rootView) {
        Prefs.getString("keyStaff", null);

        JSONObject jsonObject;
        String json = Prefs.getString("DEPENDENCY", "");
        try {
            staffItems=new ArrayList<>();
            jsonObject=new JSONObject(json);
            staffItems.add(new Data(0, "--"));
            JSONArray jsonArrayStaffs=jsonObject.getJSONArray("staffs");
            for (int i=0;i<jsonArrayStaffs.length();i++){
                Data data=new Data(Integer.parseInt(jsonArrayStaffs.getJSONObject(i).getString("id")),jsonArrayStaffs.getJSONObject(i).getString("first_name")+" "+jsonArrayStaffs.getJSONObject(i).getString("last_name"));
                staffItems.add(data);
            }
            helptopicItems = new ArrayList<>();
            helptopicItems.add(new Data(0, "--"));
            jsonObject = new JSONObject(json);
            JSONArray jsonArrayHelpTopics = jsonObject.getJSONArray("helptopics");
            for (int i = 0; i < jsonArrayHelpTopics.length(); i++) {
                Data data = new Data(Integer.parseInt(jsonArrayHelpTopics.getJSONObject(i).getString("id")), jsonArrayHelpTopics.getJSONObject(i).getString("topic"));
                helptopicItems.add(data);
            }

            JSONArray jsonArrayPriorities = jsonObject.getJSONArray("priorities");
            priorityItems = new ArrayList<>();
            priorityItems.add(new Data(0, "--"));
            for (int i = 0; i < jsonArrayPriorities.length(); i++) {
                Data data = new Data(Integer.parseInt(jsonArrayPriorities.getJSONObject(i).getString("priority_id")), jsonArrayPriorities.getJSONObject(i).getString("priority"));
                priorityItems.add(data);
            }

            JSONArray jsonArrayType = jsonObject.getJSONArray("type");
            typeItems = new ArrayList<>();
            typeItems.add(new Data(0, "--"));
            for (int i = 0; i < jsonArrayType.length(); i++) {
                Data data = new Data(Integer.parseInt(jsonArrayType.getJSONObject(i).getString("id")), jsonArrayType.getJSONObject(i).getString("name"));
                typeItems.add(data);

            }

            JSONArray jsonArraySources = jsonObject.getJSONArray("sources");
            sourceItems = new ArrayList<>();
            sourceItems.add(new Data(0, "--"));
            for (int i = 0; i < jsonArraySources.length(); i++) {
                Data data = new Data(Integer.parseInt(jsonArraySources.getJSONObject(i).getString("id")), jsonArraySources.getJSONObject(i).getString("name"));
                sourceItems.add(data);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }




        // textViewTicketNumber = (TextView) rootView.findViewById(R.id.textView_ticket_number);
        //textViewOpenedBy.setText(TicketDetailActivity.ticketOpenedBy);

        editTextSubject = (EditText) rootView.findViewById(R.id.editText_subject);
        //editTextSubject.setText(TicketDetailActivity.ticketSubject);
        //textViewErrorSubject = (TextView) rootView.findViewById(co.helpdesk.faveo.pro.R.id.textView_error_subject);

        spinnerPriority = (Spinner) rootView.findViewById(R.id.spinner_priority);
        spinnerPriArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, priorityItems); //selected item will look like a spinner set from XML
        spinnerPriArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(spinnerPriArrayAdapter);

        spinnerType = (Spinner) rootView.findViewById(R.id.spinner_type);
        spinnerTypeArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, typeItems); //selected item will look like a spinner set from XML
        spinnerTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(spinnerTypeArrayAdapter);

        spinnerHelpTopics = (Spinner) rootView.findViewById(R.id.spinner_help_topics);
        spinnerHelpArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, helptopicItems); //selected item will look like a spinner set from XML
        spinnerHelpArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHelpTopics.setAdapter(spinnerHelpArrayAdapter);

        spinnerStaffs= (Spinner) rootView.findViewById(R.id.spinner_staffs);
        staffArrayAdapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item,staffItems);
        staffArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStaffs.setAdapter(staffArrayAdapter);

        editTextFirstName = (EditText) rootView.findViewById(R.id.editText_ticketDetail_firstname);
        //editTextLastName = (EditText) rootView.findViewById(R.id.editText_ticketDetail_lastname);
        editTextEmail = (EditText) rootView.findViewById(R.id.editText_email);

        spinnerSource = (Spinner) rootView.findViewById(R.id.spinner_source);
        spinnerSourceArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, sourceItems); //selected item will look like a spinner set from XML
        spinnerSourceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSource.setAdapter(spinnerSourceArrayAdapter);

        //editTextLastMessage = (EditText) rootView.findViewById(R.id.editText_last_message);
        editTextDueDate = (EditText) rootView.findViewById(R.id.editText_due_date);
        editTextCreatedDate = (EditText) rootView.findViewById(R.id.editText_created_date);
        //tv_helpTopic = (TextView) rootView.findViewById(R.id.tv_helpTopic);

        paddingTop = editTextEmail.getPaddingTop();
        paddingBottom = editTextEmail.getPaddingBottom();
        editTextSubject.setFilters(new InputFilter[]{
                new InputFilter() {
                    public CharSequence filter(CharSequence src, int start,
                                               int end, Spanned dst, int dstart, int dend) {
                        if (src.equals("")) { // for backspace
                            return src;
                        }
                        if (src.toString().matches("[\\x00-\\x7F]+")) {
                            return src;
                        }
                        return "";
                    }
                }
        });
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

    @Override
    public void onStart() {
        super.onStart();
        if (InternetReceiver.isConnected()) {
            task = new FetchTicketDetail(Prefs.getString("TICKETid", null));
            task.execute();
        }
    }
}