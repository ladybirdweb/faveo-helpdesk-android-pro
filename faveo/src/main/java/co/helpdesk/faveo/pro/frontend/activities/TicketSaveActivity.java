package co.helpdesk.faveo.pro.frontend.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.pro.model.Data;
import es.dmoral.toasty.Toasty;

public class TicketSaveActivity extends AppCompatActivity {
    Spinner spinnerSLAPlans, spinnerType, spinnerStatus, spinnerSource,
            spinnerPriority, spinnerHelpTopics;
    ProgressDialog progressDialog;
    AsyncTask<String, Void, String> task;
    @BindView(R.id.spinner_staffs)
    Spinner spinnerStaffs;
    EditText edittextsubject;
    Button buttonsave;
//    Spinner autoCompleteTextViewPriority,autoCompleteTextViewType,autoCompleteTextViewHelpTopic,autoCompleteTextViewSource;
    AutoCompleteTextView autoCompleteTextViewstaff;
    ArrayList<Data> helptopicItems, priorityItems, typeItems, sourceItems, staffItems;
//    MultiAutoCompleteTextView autoCompleteTextViewCC;
    ArrayAdapter<Data> spinnerPriArrayAdapter, spinnerHelpArrayAdapter, spinnerTypeArrayAdapter, spinnerSourceArrayAdapter, staffArrayAdapter;
    ArrayList<Data> stringArrayList;
//    ArrayAdapter<Data> stringArrayAdapterCC;
    Set<String> hs = new HashSet<>();
//    TextView addCc;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_save);
        setUpViews();
        if (InternetReceiver.isConnected()) {
            task = new FetchTicketDetail(Prefs.getString("TICKETid",null));
            task.execute();


        }
//        helptopicid=Prefs.getInt("helptopicid",0);
//        priorityid=Prefs.getInt("priorityid",0);
//        typeid=Prefs.getInt("typeid",0);
//        sourceid=Prefs.getInt("sourceid",0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarsave);
        TextView textView = (TextView) toolbar.findViewById(R.id.titlesave);
        ImageView imageView= (ImageView) toolbar.findViewById(R.id.imageView);

        textView.setText(getString(R.string.ticketProperties));

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TicketSaveActivity.this, TicketDetailActivity.class);
                startActivity(intent);
            }
        });
        setSupportActionBar(toolbar);
        //autoCompleteTextViewCC.setOnItemClickListener(onItemClickListener);
//        autoCompleteTextViewCC.addTextChangedListener(passwordWatcheredittextSubject);
//        new FetchCollaborator("s").execute();
//        autoCompleteTextViewCC.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                stringArrayAdapterCC = new ArrayAdapter<Data>(TicketSaveActivity.this, android.R.layout.simple_dropdown_item_1line, stringArrayList);
//                String name1=autoCompleteTextViewCC.getText().toString();
//                for (int j = 0; j < stringArrayList.size(); j++) {
//                    if (stringArrayList.get(j).getName().equalsIgnoreCase(name1)) {
//                        Data data = stringArrayList.get(j);
//                        id = data.getID();
//                        Toast.makeText(TicketSaveActivity.this, "clicked on item", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });
//        autoCompleteTextViewCC.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                stringArrayAdapterCC=new ArrayAdapter<String>(TicketSaveActivity.this,android.R.layout.simple_dropdown_item_1line,stringArrayList);
//                autoCompleteTextViewCC.setAdapter(stringArrayAdapterCC);
//                autoCompleteTextViewCC.showDropDown();
//                return false;
//            }
//        });
//        addCc= (TextView) findViewById(R.id.addcc);
//        addCc.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent=new Intent(TicketSaveActivity.this,collaboratorAdd.class);
//                    startActivity(intent);
//                }
//            });
        spinnerPriority.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                buttonsave.setVisibility(View.VISIBLE);
                return false;
            }
        });
        spinnerSource.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
            buttonsave.setVisibility(View.VISIBLE);
                return false;
            }
        });
        spinnerHelpTopics.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                buttonsave.setVisibility(View.VISIBLE);
                return false;
            }
        });
        autoCompleteTextViewstaff.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                buttonsave.setVisibility(View.VISIBLE);
                return false;
            }
        });
//        autoCompleteTextViewType.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                buttonsave.setVisibility(View.VISIBLE);
//                return false;
//            }
//        });

        edittextsubject.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                edittextsubject.setCursorVisible(true);
                buttonsave.setVisibility(View.VISIBLE);
                return false;
            }
        });



//        autoCompleteTextViewstaff.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                buttonsave.setVisibility(View.VISIBLE);
//                staffArrayAdapter = new ArrayAdapter<>(TicketSaveActivity.this, android.R.layout.simple_dropdown_item_1line, staffItems);
////                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
////                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
//                //autoCompleteTextView.setThreshold(1);
//                autoCompleteTextViewstaff.showDropDown();
//                autoCompleteTextViewstaff.setDropDownWidth(1000);
////                Data data=staffItems.get(pos);
////                id=data.getID();
//                //Toast.makeText(TicketSaveActivity.this, ""+id, Toast.LENGTH_SHORT).show();
//////                if (name.equals(autocompletetextview.getItem(pos).getName())){
//////                    Toast.makeText(CreateTicketActivity.this, ""+autocompletetextview.getItem(pos).getID(), Toast.LENGTH_SHORT).show();
//////                }
//////                id=autocompletetextview.getItem(pos).getID();
//////                String name=autocompletetextview.getItem(i).getName();
//////                Log.d("ID",""+id);
////
////                Log.d("id",""+id);
//                return false;
//            }
//        });



        autoCompleteTextViewstaff.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                staffArrayAdapter = new ArrayAdapter<>(TicketSaveActivity.this, android.R.layout.simple_dropdown_item_1line, staffItems);
                //Data data= (Data) adapterView.getAdapter().getItem(i);
                String name1=autoCompleteTextViewstaff.getText().toString();
                for (int j = 0; j < staffItems.size(); j++) {
                    if (staffItems.get(j).getName().equalsIgnoreCase(name1)) {
                        Data data = staffItems.get(j);
                        id = data.getID();
                        //Toast.makeText(CreateTicketActivity.this, "id:" + id, Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });














        buttonsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean allCorrect = true;
                String subject = edittextsubject.getText().toString();
                // int SLAPlans = spinnerSLAPlans.getSelectedItemPosition();
                Data helpTopic = (Data) spinnerHelpTopics.getSelectedItem();
                Data source = (Data) spinnerSource.getSelectedItem();
                Data priority = (Data) spinnerPriority.getSelectedItem();
                Data type = (Data) spinnerType.getSelectedItem();

                //Data  staff= (Data) spinnerStaffs.getSelectedItem();

//                helptopicid=Prefs.getInt("helptopicid",0);
//                priorityid=Prefs.getInt("priorityid",0);
//                sourceid=Prefs.getInt("sourceid",0);
//                typeid=Prefs.getInt("typeid",0);


                //int status = Integer.parseInt(Utils.removeDuplicates(SplashActivity.keyStatus.split(","))[spinnerStatus.getSelectedItemPosition()]);

//                if (SLAPlans == 0) {
//                    allCorrect = false;
//                    Toasty.warning(getContext(), "Please select some SLA plan", Toast.LENGTH_SHORT).show();
//                } else

                if (subject.trim().length() == 0) {
                    Toasty.warning(TicketSaveActivity.this, getString(R.string.sub_must_not_be_empty), Toast.LENGTH_SHORT).show();
                    allCorrect = false;
                } else if (subject.trim().length() < 5) {
                    Toasty.warning(TicketSaveActivity.this, getString(R.string.sub_minimum_char), Toast.LENGTH_SHORT).show();
                    allCorrect = false;
                }
                else if (helpTopic.ID==0) {
                    allCorrect = false;
                    Toasty.warning(TicketSaveActivity.this, getString(R.string.select_some_helptopic), Toast.LENGTH_SHORT).show();
                } else if (priority.ID==0) {
                    allCorrect = false;
                    Toasty.warning(TicketSaveActivity.this, getString(R.string.please_select_some_priority), Toast.LENGTH_SHORT).show();
                } else if (source.ID==0) {
                    allCorrect = false;
                    Toasty.warning(TicketSaveActivity.this, getString(R.string.select_source), Toast.LENGTH_SHORT).show();
                }

//
//
//
                if (allCorrect) {
                    if (InternetReceiver.isConnected()) {
                        progressDialog=new ProgressDialog(TicketSaveActivity.this);
                        progressDialog.setMessage(getString(R.string.updating_ticket));
                        progressDialog.show();
                        try {
                            new SaveTicket(
                                    Integer.parseInt(Prefs.getString("TICKETid",null)),
                                    URLEncoder.encode(subject.trim(), "utf-8"),
                                    helpTopic.ID,
                                    source.ID,
                                    priority.ID, type.ID,id)
                                    .execute();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
//            }
//        });
            }
        });
//
//
    }
//    AdapterView.OnItemClickListener onItemClickListener =
//            new AdapterView.OnItemClickListener(){
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                    stringArrayAdapterCC = new ArrayAdapter<>(TicketSaveActivity.this, android.R.layout.simple_dropdown_item_1line, stringArrayList);
//                    //Data data= (Data) adapterView.getAdapter().getItem(i);
//                    String email=autoCompleteTextViewstaff.getText().toString();
//                    for (int j = 0; j < stringArrayList.size(); j++) {
//                        if (staffItems.get(j).getName().equalsIgnoreCase(email)) {
//                            Data data = stringArrayList.get(j);
//                            id = data.getID();
//                            Toast.makeText(TicketSaveActivity.this, "id:" + id, Toast.LENGTH_SHORT).show();
//                        }
//                    }
////                    Toast.makeText(TicketSaveActivity.this,
////                            "Clicked item from auto completion list "
////                                    + email
////                            , Toast.LENGTH_SHORT).show();
//                    Log.d("collaborators",email);
////                    autoCompleteTextViewCC.setText(email);
//                }
//            };

//    final TextWatcher passwordWatcheredittextSubject = new TextWatcher() {
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            //Toast.makeText(TicketSaveActivity.this, "API called", Toast.LENGTH_SHORT).show();
//        }
//
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            String term=autoCompleteTextViewCC.getText().toString();
////            if (term.equals("")){
////                stringArrayAdapterCC=new ArrayAdapter<Data>(TicketSaveActivity.this,android.R.layout.simple_dropdown_item_1line,stringArrayList);
////                new FetchCollaborator("s").execute();
////                Data data=new Data(0,"No result found");
////                stringArrayList.add(data);
//////                autoCompleteTextViewCC.setAdapter(stringArrayAdapterCC);
//////                stringArrayAdapterCC.notifyDataSetChanged();
//////                autoCompleteTextViewCC.setThreshold(0);
//////                autoCompleteTextViewCC.setDropDownWidth(1000);
////                autoCompleteTextViewCC.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
////            }else
//                stringArrayAdapterCC=new ArrayAdapter<Data>(TicketSaveActivity.this,android.R.layout.simple_dropdown_item_1line,stringArrayList);
//                new FetchCollaborator("s").execute();
//                autoCompleteTextViewCC.setAdapter(stringArrayAdapterCC);
//                //stringArrayAdapterCC.notifyDataSetChanged();
////                autoCompleteTextViewCC.setThreshold(0);
////                autoCompleteTextViewCC.setDropDownWidth(1000);
//                autoCompleteTextViewCC.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
//
//
//
//
//
//            //buttonsave.setEnabled(true);
//        }
//
//        public void afterTextChanged(Editable s) {
//
//            //stringArrayList.clear();
//            //new FetchCollaborator(Prefs.getString("ticketID",null)).execute();
////                if (s.length() == 0) {
////                    edittextsubject.setVisibility(View.GONE);
////                } else{
////                    textView.setText("You have entered : " + passwordEditText.getText());
////                }
//        }
//    };
    private class SaveTicket extends AsyncTask<String, Void, String> {
        int ticketNumber;
        String subject;
        //int slaPlan;
        int helpTopic;
        int ticketSource;
        int ticketPriority;
        int ticketStatus;
        int ticketType;
        int staff;

        SaveTicket(int ticketNumber, String subject, int helpTopic, int ticketSource, int ticketPriority, int ticketType,int staff) {
            this.ticketNumber = ticketNumber;
            this.subject = subject;
            // this.slaPlan = slaPlan;
            this.helpTopic = helpTopic;
            this.ticketSource = ticketSource;
            this.ticketPriority = ticketPriority;
            // this.ticketStatus = ticketStatus;
            this.ticketType = ticketType;
            this.staff=staff;
        }

        protected String doInBackground(String... urls) {
            if (subject.equals("Not available"))
                subject = "";
            return new Helpdesk().postEditTicket(ticketNumber, subject,
                    helpTopic, ticketSource, ticketPriority, ticketType,staff);
        }

        protected void onPostExecute(String result) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            if (result == null) {
                Toasty.error(TicketSaveActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }
            String state=Prefs.getString("403",null);
//                if (message1.contains("The ticket id field is required.")){
//                    Toasty.warning(TicketDetailActivity.this, getString(R.string.please_select_ticket), Toast.LENGTH_LONG).show();
//                }
//                else if (message1.contains("The status id field is required.")){
//                    Toasty.warning(TicketDetailActivity.this, getString(R.string.please_select_status), Toast.LENGTH_LONG).show();
//                }
//               else
//            try {
//                if (state.equals("403") && !state.equals(null)) {
//                    Toasty.warning(TicketSaveActivity.this, getString(R.string.permission), Toast.LENGTH_LONG).show();
//                    Prefs.putString("403", "null");
//                    return;
//                }
//            }catch (NullPointerException e){
//                e.printStackTrace();
//            }
            try {
                JSONObject jsonObject = new JSONObject(result);
                //JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                String message = jsonObject.getString("message");
                if (message.equals("Permission denied, you do not have permission to access the requested page.")) {
                    Toasty.warning(TicketSaveActivity.this, getString(R.string.permission), Toast.LENGTH_LONG).show();
                    Prefs.putString("403", "null");
                    return;
                }
            }catch (JSONException e){
                e.printStackTrace();
            }

//            switch (result) {
//                case "":
//
//            }

            if (result.contains("Edited successfully")) {
                Toasty.success(TicketSaveActivity.this, getString(R.string.update_success), Toast.LENGTH_LONG).show();
                Intent intent=new Intent(TicketSaveActivity.this, MainActivity.class);
                startActivity(intent);
            } else
                Toasty.error(TicketSaveActivity.this, getString(R.string.failed_to_update_ticket), Toast.LENGTH_LONG).show();
        }
    }
    class SpaceTokenizer implements MultiAutoCompleteTextView.Tokenizer {

        public int findTokenStart(CharSequence text, int cursor) {
            int i = cursor;

            while (i > 0 && text.charAt(i - 1) != ' ') {
                i--;
            }
            while (i < cursor && text.charAt(i) == ' ') {
                i++;
            }

            return i;
        }

        public int findTokenEnd(CharSequence text, int cursor) {
            int i = cursor;
            int len = text.length();

            while (i < len) {
                if (text.charAt(i) == ' ') {
                    return i;
                } else {
                    i++;
                }
            }

            return len;
        }

        public CharSequence terminateToken(CharSequence text) {
            int i = text.length();

            while (i > 0 && text.charAt(i - 1) == ' ') {
                i--;
            }

            if (i > 0 && text.charAt(i - 1) == ' ') {
                return text;
            } else {
                if (text instanceof Spanned) {
                    SpannableString sp = new SpannableString(text + " ");
                    TextUtils.copySpansFrom((Spanned) text, 0, text.length(),
                            Object.class, sp, 0);
                    return sp;
                } else {
                    return text + " ";
                }
            }
        }
    }
    private class FetchTicketDetail extends AsyncTask<String, Void, String> {
            String ticketID;

            FetchTicketDetail(String ticketID) {

                this.ticketID = ticketID;
            }

            protected String doInBackground(String... urls) {
                return new Helpdesk().getTicketDetail(ticketID);
            }

            protected void onPostExecute(String result) {
                if (isCancelled()) return;
//            if (progressDialog.isShowing())
//                progressDialog.dismiss();

                if (result == null) {
                    Toasty.error(TicketSaveActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                    String title=jsonObject1.getString("title");
                    edittextsubject.setText(title);
                    try {
                        if (jsonObject1.getString("priority_name") != null) {
                            // spinnerPriority.setSelection(Integer.parseInt(jsonObject1.getString("priority_id")) - 1);

                            spinnerPriority.setSelection(getIndex(spinnerPriority, jsonObject1.getString("priority_name")));
//                            Prefs.putInt("priorityid", Integer.parseInt(jsonObject1.getString("priority_id")));
//                            autoCompleteTextViewPriority.setText(jsonObject1.getString("priority_name"));
//                        priorityid=Integer.parseInt(jsonObject1.getString("priority_id"));


                        }
                    } catch (JSONException | NumberFormatException e) {
                        e.printStackTrace();
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }

                    try {
                        if (jsonObject1.getString("type_name") != null) {
                            // spinnerDepartment.setSelection(Integer.parseInt(jsonObject1.getString("dept_id")) - 1);
                            //spinnerType.setSelection(getIndex(spinnerType, jsonObject1.getString("type_name")));
//                            Prefs.putInt("typeid", Integer.parseInt(jsonObject1.getString("type")));
                            //autoCompleteTextViewType.setText(jsonObject1.getString("type_name"));
                            //typeid=Integer.parseInt(jsonObject1.getString("type"));
                            spinnerType.setSelection(Integer.parseInt(jsonObject1.getString("type")));
                        }
                    } catch (JSONException | NumberFormatException e) {
                        e.printStackTrace();
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (jsonObject1.getString("helptopic_name") != null)
                            spinnerHelpTopics.setSelection(getIndex(spinnerHelpTopics, jsonObject1.getString("helptopic_name")));
                            //spinnerHelpTopics.setSelection(Integer.parseInt(jsonObject1.getString("helptopic_id")));
//                        Prefs.putInt("helptopicid", Integer.parseInt(jsonObject1.getString("helptopic_id")));
                        //autoCompleteTextViewHelpTopic.setText(jsonObject1.getString("helptopic_name"));
                        //helptopicid=Integer.parseInt(jsonObject1.getString("helptopic_id"));

                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
//                    spinnerHelpTopics.setVisibility(View.GONE);
//                    tv_helpTopic.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                    try {
                        if (!jsonObject1.getString("assignee_first_name").equals("null")&&!jsonObject1.getString("assignee_last_name").equals("null")) {
                            //spinnerHelpTopics.setSelection(getIndex(spinnerHelpTopics, jsonObject1.getString("helptopic_name")));
//                            for (int j = 0; j < spinnerStaffs.getCount(); j++) {
//                                if (spinnerStaffs.getItemAtPosition(j).toString().equalsIgnoreCase(jsonObject1.getString("assignee_first_name")+" "+jsonObject1.getString("assignee_last_name"))) {
//                                    spinnerStaffs.setSelection(j);
//                                }
//                            }
                            autoCompleteTextViewstaff.setText(jsonObject1.getString("assignee_first_name")+" "+jsonObject1.getString("assignee_last_name"));
                            //spinnerStaffs.setSelection(staffItems.indexOf("assignee_email"));
                            id= Integer.parseInt(jsonObject1.getString("assignee_id"));
                            //Toast.makeText(TicketSaveActivity.this, "id:"+id, Toast.LENGTH_SHORT).show();
                        }
                        else if (jsonObject1.getString("assignee_first_name").equals("null")&&jsonObject1.getString("assignee_last_name").equals("null")){
                            autoCompleteTextViewstaff.setText("");
                        }
                        //spinnerHelpTopics.setSelection(Integer.parseInt(jsonObject1.getString("helptopic_id")));
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
//                    spinnerHelpTopics.setVisibility(View.GONE);
//                    tv_helpTopic.setVisibility(View.GONE);
                        e.printStackTrace();
                    }


                    try {
                        if (jsonObject1.getString("source_name") != null)
                            //spinnerSource.setSelection(Integer.parseInt(jsonObject1.getString("source")) - 1);

                            spinnerSource.setSelection(getIndex(spinnerSource, jsonObject1.getString("source_name")));
//                        Prefs.putInt("sourceid", Integer.parseInt(jsonObject1.getString("source")));
                        //autoCompleteTextViewSource.setText(jsonObject1.getString("source_name"));
                        //sourceid=Integer.parseInt(jsonObject1.getString("source"));

                    } catch (JSONException | NumberFormatException e) {
                        e.printStackTrace();
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }


//                if (jsonObject1.getString("last_message").equals("null")) {
//                    editTextLastMessage.setText("Not available");
//                } else
//                    editTextLastMessage.setText(jsonObject1.getString("last_message"));


                } catch (JSONException | IllegalStateException e) {
                    e.printStackTrace();
                }
            }

        }

    private class FetchCollaborator extends AsyncTask<String, Void, String> {
        String term;

        FetchCollaborator(String term) {

            this.term = term;
        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().getUser(term);
        }

        protected void onPostExecute(String result) {
            if (isCancelled()) return;
            stringArrayList.clear();
//            if (progressDialog.isShowing())
//                progressDialog.dismiss();

            if (result == null) {
                Toasty.error(TicketSaveActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }

            try {
                JSONObject jsonObject=new JSONObject(result);
                JSONArray jsonArray=jsonObject.getJSONArray("users");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    String email=jsonObject1.getString("email");
                    int id= Integer.parseInt(jsonObject1.getString("id"));
                    //Toast.makeText(TicketSaveActivity.this, "email:"+email, Toast.LENGTH_SHORT).show();
                    Data data=new Data(id,email);
                    stringArrayList.add(data);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


//                if (jsonObject1.getString("last_message").equals("null")) {
//                    editTextLastMessage.setText("Not available");
//                } else
//                    editTextLastMessage.setText(jsonObject1.getString("last_message"));


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

    public void setUpViews() {
        Prefs.getString("keyStaff", null);

        JSONObject jsonObject;
        String json = Prefs.getString("DEPENDENCY", "");
        try {
            staffItems = new ArrayList<>();
            jsonObject = new JSONObject(json);
            staffItems.add(new Data(0, "--"));
            JSONArray jsonArrayStaffs = jsonObject.getJSONArray("staffs");
            for (int i = 0; i < jsonArrayStaffs.length(); i++) {
                Data data = new Data(Integer.parseInt(jsonArrayStaffs.getJSONObject(i).getString("id")), jsonArrayStaffs.getJSONObject(i).getString("first_name")+" "+jsonArrayStaffs.getJSONObject(i).getString("last_name"));
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

//        autoCompleteTextViewCC= (MultiAutoCompleteTextView) findViewById(R.id.autocompletecc);
//        stringArrayList=new ArrayList<Data>();
//        autoCompleteTextViewCC.setThreshold(1);
//        autoCompleteTextViewCC.setDropDownWidth(3000);
//        autoCompleteTextViewCC.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        edittextsubject= (EditText) findViewById(R.id.editTextsubject);
        buttonsave= (Button) findViewById(R.id.buttonsave);



//        spinnerPriority = (Spinner) findViewById(R.id.spinner_priority);
//        spinnerPriArrayAdapter = new ArrayAdapter<>(TicketSaveActivity.this, android.R.layout.simple_spinner_item, priorityItems); //selected item will look like a spinner set from XML
//        spinnerPriArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerPriority.setAdapter(spinnerPriArrayAdapter);
        spinnerPriority= (Spinner) findViewById(R.id.spinner_priority);
        spinnerPriArrayAdapter = new ArrayAdapter<>(TicketSaveActivity.this, android.R.layout.simple_spinner_dropdown_item, priorityItems);
        //selected item will look like a spinner set from XML
        spinnerPriArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(spinnerPriArrayAdapter);


//        spinnerType = (Spinner) findViewById(R.id.spinner_type);
//        spinnerTypeArrayAdapter = new ArrayAdapter<>(TicketSaveActivity.this, android.R.layout.simple_spinner_item, typeItems); //selected item will look like a spinner set from XML
//        spinnerTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerType.setAdapter(spinnerTypeArrayAdapter);
        spinnerType= (Spinner) findViewById(R.id.spinner_type);
        spinnerTypeArrayAdapter = new ArrayAdapter<>(TicketSaveActivity.this, android.R.layout.simple_spinner_dropdown_item, typeItems);
        //selected item will look like a spinner set from XML
        spinnerTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(spinnerTypeArrayAdapter);


        spinnerHelpTopics= (Spinner) findViewById(R.id.spinner_help_topics);
        spinnerHelpArrayAdapter = new ArrayAdapter<>(TicketSaveActivity.this, android.R.layout.simple_spinner_dropdown_item, helptopicItems);
        //selected item will look like a spinner set from XML
        spinnerHelpArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHelpTopics.setAdapter(spinnerHelpArrayAdapter);

//        spinnerHelpTopics = (Spinner) findViewById(R.id.spinner_help_topics);
//        spinnerHelpArrayAdapter = new ArrayAdapter<>(TicketSaveActivity.this, android.R.layout.simple_spinner_item, helptopicItems); //selected item will look like a spinner set from XML
//        spinnerHelpArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerHelpTopics.setAdapter(spinnerHelpArrayAdapter);

        autoCompleteTextViewstaff= (AutoCompleteTextView) findViewById(R.id.spinner_staffs);
        staffArrayAdapter=new ArrayAdapter<>(TicketSaveActivity.this,android.R.layout.simple_dropdown_item_1line,staffItems);
        //staffArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoCompleteTextViewstaff.setAdapter(staffArrayAdapter);
        autoCompleteTextViewstaff.setThreshold(1);
        autoCompleteTextViewstaff.setDropDownWidth(1000);

        spinnerSource= (Spinner) findViewById(R.id.spinner_source);
        spinnerSourceArrayAdapter = new ArrayAdapter<>(TicketSaveActivity.this, android.R.layout.simple_spinner_dropdown_item, sourceItems);
        //selected item will look like a spinner set from XML
        spinnerSourceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSource.setAdapter(spinnerSourceArrayAdapter);


//        spinnerSource = (Spinner) findViewById(R.id.spinner_source);
//        spinnerSourceArrayAdapter = new ArrayAdapter<>(TicketSaveActivity.this, android.R.layout.simple_spinner_item, sourceItems); //selected item will look like a spinner set from XML
//        spinnerSourceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerSource.setAdapter(spinnerSourceArrayAdapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(TicketSaveActivity.this,TicketDetailActivity.class);
        startActivity(intent);
    }
}
