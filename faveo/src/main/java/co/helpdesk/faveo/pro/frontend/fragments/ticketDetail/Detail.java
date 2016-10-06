package co.helpdesk.faveo.pro.frontend.fragments.ticketDetail;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import co.helpdesk.faveo.pro.Helper;
import co.helpdesk.faveo.pro.Preference;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.pro.frontend.activities.TicketDetailActivity;

public class Detail extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView tv_helpTopic, tv_dept;
    TextView textViewOpenedBy;

    EditText editTextSubject, editTextName, editTextEmail,
            editTextLastMessage, editTextDueDate, editTextCreatedDate, editTextLastResponseDate;

    Spinner spinnerSLAPlans, spinnerDepartment, spinnerStatus, spinnerSource,
            spinnerPriority, spinnerHelpTopics, spinnerAssignTo;
    ProgressDialog progressDialog;

    Button buttonSave;

    private String mParam1;
    private String mParam2;

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
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetching detail");
        progressDialog.show();
        new FetchTicketDetail(getActivity(), TicketDetailActivity.ticketID).execute();
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Updating ticket");
                progressDialog.show();
                new SaveTicket(getActivity(),
                        Integer.parseInt(TicketDetailActivity.ticketID),
                        editTextSubject.getText().toString(),
                        Integer.parseInt(Preference.getKeySLA().split(",")[spinnerSLAPlans.getSelectedItemPosition()]),
                        Integer.parseInt(Preference.getKeyTopic().split(",")[spinnerHelpTopics.getSelectedItemPosition()]),
                        Integer.parseInt(Preference.getKeySource().split(",")[spinnerSource.getSelectedItemPosition()]),
                        Integer.parseInt(Preference.getKeyPriority().split(",")[spinnerPriority.getSelectedItemPosition()]),
                        Integer.parseInt(Preference.getKeyStatus().split(",")[spinnerStatus.getSelectedItemPosition()]))
                        .execute();
            }
        });
        return rootView;
    }

    public class FetchTicketDetail extends AsyncTask<String, Void, String> {
        Context context;
        String ticketID;

        public FetchTicketDetail(Context context, String ticketID) {
            this.context = context;
            this.ticketID = ticketID;
        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().getTicketDetail(ticketID);
        }

        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            if (result == null) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject jsonObject1 = jsonObject.getJSONObject("result");

                try {
                    if (jsonObject1.getString("sla") != null) {
                        spinnerSLAPlans.setSelection(Integer.parseInt(jsonObject1.getString("sla")) - 1);
                    }
                } catch (JSONException | NumberFormatException e) {
                    e.printStackTrace();
                }
                try {
                    if (jsonObject1.getString("status") != null) {
                        spinnerStatus.setSelection(Integer.parseInt(jsonObject1.getString("status")) - 1);
                    }
                } catch (JSONException | NumberFormatException e) {
                    e.printStackTrace();
                }
                try {
                    if (jsonObject1.getString("priority_id") != null) {
                        spinnerPriority.setSelection(Integer.parseInt(jsonObject1.getString("priority_id")) - 1);
                    }
                } catch (JSONException | NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    spinnerDepartment.setSelection(Integer.parseInt(jsonObject1.getString("dept_id")) - 1);
                } catch (Exception e) {
                    tv_dept.setVisibility(View.GONE);
                    spinnerDepartment.setVisibility(View.GONE);
                    e.printStackTrace();
                }
                try {
                    spinnerHelpTopics.setSelection(Integer.parseInt(jsonObject1.getString("help_topic_id")) - 1);
                } catch (Exception e) {
                    spinnerHelpTopics.setVisibility(View.GONE);
                    tv_helpTopic.setVisibility(View.GONE);
                    e.printStackTrace();
                }
                try {
                    if (jsonObject1.getString("source") != null)
                        spinnerSource.setSelection(Integer.parseInt(jsonObject1.getString("source")) - 1);
                } catch (JSONException | NumberFormatException e) {
                    e.printStackTrace();
                }

                if (jsonObject1.getString("duedate").equals("null")) {
                    editTextDueDate.setText("Not available");
                } else {
                    editTextDueDate.setText(Helper.parseDate(jsonObject1.getString("duedate")));
                }

                if (jsonObject1.getString("created_at").equals("null")) {
                    editTextCreatedDate.setText("Not available");
                } else {
                    editTextCreatedDate.setText(Helper.parseDate(jsonObject1.getString("created_at")));
                }

                if (jsonObject1.getString("updated_at").equals("null")) {
                    editTextLastResponseDate.setText("Not available");
                } else {
                    editTextLastResponseDate.setText(Helper.parseDate(jsonObject1.getString("updated_at")));
                }

//                if (jsonObject1.getString("last_message").equals("null")) {
//                    editTextLastMessage.setText("Not available");
//                } else
//                    editTextLastMessage.setText(jsonObject1.getString("last_message"));

                if (jsonObject1.getString("user_name").equals("null")) {
                    editTextName.setText("Not available");
                } else
                    editTextName.setText(jsonObject1.getString("user_name"));

                if (jsonObject1.getString("email").equals("null")) {
                    editTextEmail.setText("Not available");
                } else
                    editTextEmail.setText(jsonObject1.getString("email"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public class SaveTicket extends AsyncTask<String, Void, String> {
        Context context;
        int ticketNumber;
        String subject;
        int slaPlan;
        int helpTopic;
        int ticketSource;
        int ticketPriority;
        int ticketStatus;

        public SaveTicket(Context context, int ticketNumber, String subject,
                          int slaPlan, int helpTopic, int ticketSource, int ticketPriority, int ticketStatus) {
            this.context = context;
            this.ticketNumber = ticketNumber;
            this.subject = subject;
            this.slaPlan = slaPlan;
            this.helpTopic = helpTopic;
            this.ticketSource = ticketSource;
            this.ticketPriority = ticketPriority;
            this.ticketStatus = ticketStatus;
        }

        protected String doInBackground(String... urls) {
            if (subject.equals("Not available"))
                subject = "";
            return new Helpdesk().postEditTicket(ticketNumber, subject, slaPlan,
                    helpTopic, ticketSource, ticketPriority, ticketStatus);
        }

        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            if (result == null) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                return;
            }
            if (result.contains("ticket_id"))
                Toast.makeText(getActivity(), "Update successful", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getActivity(), "Failed to update ticket", Toast.LENGTH_LONG).show();
        }
    }

    private void setUpViews(View rootView) {
        textViewOpenedBy = (TextView) rootView.findViewById(R.id.textView_opened_by);
        textViewOpenedBy.setText(TicketDetailActivity.ticketOpenedBy);

        editTextSubject = (EditText) rootView.findViewById(R.id.editText_subject);
        editTextSubject.setText(TicketDetailActivity.ticketSubject);

        spinnerSLAPlans = (Spinner) rootView.findViewById(R.id.spinner_sla_plans);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Preference.getValueSLA().split(",")); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSLAPlans.setAdapter(spinnerArrayAdapter);

        spinnerStatus = (Spinner) rootView.findViewById(R.id.spinner_status);
        spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Preference.getValueStatus().split(",")); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(spinnerArrayAdapter);

        spinnerPriority = (Spinner) rootView.findViewById(R.id.spinner_priority);
        spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Preference.getValuePriority().split(",")); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(spinnerArrayAdapter);

        spinnerDepartment = (Spinner) rootView.findViewById(R.id.spinner_department);
        spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Preference.getValueDept().split(",")); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDepartment.setAdapter(spinnerArrayAdapter);

        spinnerHelpTopics = (Spinner) rootView.findViewById(R.id.spinner_help_topics);
        spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Preference.getValueTopic().split(",")); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHelpTopics.setAdapter(spinnerArrayAdapter);

        editTextName = (EditText) rootView.findViewById(R.id.editText_name);
        editTextEmail = (EditText) rootView.findViewById(R.id.editText_email);

        spinnerSource = (Spinner) rootView.findViewById(R.id.spinner_source);
        spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Preference.getValueSource().split(",")); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSource.setAdapter(spinnerArrayAdapter);

        //editTextLastMessage = (EditText) rootView.findViewById(R.id.editText_last_message);
        editTextDueDate = (EditText) rootView.findViewById(R.id.editText_due_date);
        editTextCreatedDate = (EditText) rootView.findViewById(R.id.editText_created_date);
        editTextLastResponseDate = (EditText) rootView.findViewById(R.id.editText_last_response_date);
        spinnerAssignTo = (Spinner) rootView.findViewById(R.id.spinner_assign_to);
        buttonSave = (Button) rootView.findViewById(R.id.button_save);
        tv_dept = (TextView) rootView.findViewById(R.id.tv_dept);
        tv_helpTopic = (TextView) rootView.findViewById(R.id.tv_helpTopic);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
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

}
