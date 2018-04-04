package co.helpdesk.faveo.pro.frontend.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
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
import android.view.Window;
import android.view.WindowManager;
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
import co.helpdesk.faveo.pro.backend.api.v1.Authenticate;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.pro.model.Data;
import es.dmoral.toasty.Toasty;

public class TicketSaveActivity extends AppCompatActivity {
    Spinner  spinnerType, spinnerStatus, spinnerSource,
            spinnerPriority, spinnerHelpTopics;
    ProgressDialog progressDialog;
    AsyncTask<String, Void, String> task;
    @BindView(R.id.spinner_staffs)
    Spinner spinnerStaffs;
    EditText edittextsubject;
    Button buttonsave;
    ImageView imageView;
    Spinner autoCompleteTextViewstaff;
    ArrayList<Data> helptopicItems, priorityItems, typeItems, sourceItems, staffItems;
    ArrayAdapter<Data> spinnerPriArrayAdapter, spinnerHelpArrayAdapter, spinnerTypeArrayAdapter, spinnerSourceArrayAdapter, staffArrayAdapter;
    int id,id1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_save);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                //
                // Do the stuff
                //
                String result= new Authenticate().postAuthenticateUser(Prefs.getString("USERNAME", null), Prefs.getString("PASSWORD", null));
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject jsonObject1=jsonObject.getJSONObject("data");
                    JSONObject jsonObject2=jsonObject1.getJSONObject("user");
                    String role1=jsonObject2.getString("role");
                    if (role1.equals("user")){
                        Prefs.clear();
                        //Prefs.putString("role",role);
                        Intent intent=new Intent(TicketSaveActivity.this,LoginActivity.class);
                        Toasty.info(TicketSaveActivity.this,getString(R.string.roleChanged), Toast.LENGTH_LONG).show();
                        startActivity(intent);


                    }


                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
                }

                handler.postDelayed(this, 30000);
            }
        };
        runnable.run();
        setUpViews();
        if (InternetReceiver.isConnected()) {
            progressDialog=new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.pleasewait));
            progressDialog.show();
            Log.d("FromTicketSave","true");
            task = new FetchTicketDetail(Prefs.getString("TICKETid",null));
            task.execute();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarsave);
        TextView textView = (TextView) toolbar.findViewById(R.id.titlesave);
        imageView= (ImageView) toolbar.findViewById(R.id.imageViewBackTicketSave);

        textView.setText(getString(R.string.ticketProperties));

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TicketSaveActivity.this,TicketDetailActivity.class);
                startActivity(intent);
//                if (!TicketDetailActivity.isShowing) {
//                    Log.d("isShowing", "false");
//                    Intent intent = new Intent(TicketSaveActivity.this, TicketDetailActivity.class);
//                    startActivity(intent);
//                }else {
//                    Log.d("isShowing", "true");
//                }
            }
        });
        setSupportActionBar(toolbar);
        spinnerPriority.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                buttonsave.setVisibility(View.VISIBLE);
                return false;
            }
        });
       autoCompleteTextViewstaff.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               Data data=staffItems.get(position);
               id1=data.getID();
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {
            id1=0;
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

        edittextsubject.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                edittextsubject.setCursorVisible(true);
                buttonsave.setVisibility(View.VISIBLE);
                return false;
            }
        });
        buttonsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean allCorrect = true;
                String subject = edittextsubject.getText().toString();
                Data helpTopic = (Data) spinnerHelpTopics.getSelectedItem();
                Data source = (Data) spinnerSource.getSelectedItem();
                Data priority = (Data) spinnerPriority.getSelectedItem();
                Data type = (Data) spinnerType.getSelectedItem();


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
                                    priority.ID, type.ID,id1)
                                    .execute();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (InternetReceiver.isConnected()){
            new FetchTicketDetail(Prefs.getString("TICKETid",null)).execute();
        }
    }
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
            Log.d("Depen Response : ", result + "");
            if (result == null) {
                Toasty.error(TicketSaveActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }
            String state=Prefs.getString("403",null);

            try {
                if (state.equals("403") && !state.equals(null)) {
                    Toasty.warning(TicketSaveActivity.this, getString(R.string.permission), Toast.LENGTH_LONG).show();
                    Prefs.putString("403", "null");
                    return;
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            try {
                JSONObject jsonObject = new JSONObject(result);
                String message = jsonObject.getString("message");
                if (message.equals("Permission denied, you do not have permission to access the requested page.")) {
                    Toasty.warning(TicketSaveActivity.this, getString(R.string.permission), Toast.LENGTH_LONG).show();
                    Prefs.putString("403", "null");
                    return;
                }
            }catch (JSONException e){
                e.printStackTrace();
            }


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
                progressDialog.dismiss();
                if (isCancelled()) return;


                if (result == null) {
                    Toasty.error(TicketSaveActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    JSONObject jsonObject2=jsonObject1.getJSONObject("ticket");
                    String title=jsonObject2.getString("title");
                    edittextsubject.setText(title);
                    String assignee=jsonObject2.getString("assignee");
                    if (assignee.equals(null)||assignee.equals("null")||assignee.equals("")){
                        autoCompleteTextViewstaff.setSelection(0);
                    }
                    else{
                        JSONObject jsonObject3=jsonObject2.getJSONObject("assignee");
                        try {
                            if (jsonObject3.getString("first_name") != null&&jsonObject3.getString("last_name") != null) {
                                //spinnerHelpTopics.setSelection(getIndex(spinnerHelpTopics, jsonObject1.getString("helptopic_name")));
                                id1= Integer.parseInt(jsonObject3.getString("id"));
                                Log.d("id of the assignee",""+id1);
                                for (int j = 0; j < staffItems.size(); j++) {
                                    Data data=staffItems.get(j);
                                    if (data.getID()==id1) {
                                        Log.d("cameHere","True");
                                        Log.d("position",""+j);
                                        autoCompleteTextViewstaff.setSelection(j);
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
//                    try {
//                        if (!jsonObject1.getString("assignee_first_name").equals("null")&&!jsonObject1.getString("assignee_last_name").equals("null")) {
//                            id1= Integer.parseInt(jsonObject1.getString("assignee_id"));
//                            Log.d("id of the assignee",""+id1);
//                            for (int j = 0; j < staffItems.size(); j++) {
//                                Data data=staffItems.get(j);
//                                if (data.getID()==id1) {
//                                    Log.d("cameHere","True");
//                                    Log.d("position",""+j);
//                                   autoCompleteTextViewstaff.setSelection(j);
//
//
//
//                                }
//                            }
//
//                        }
//                        else if (jsonObject1.getString("assignee_first_name").equals("null")&&jsonObject1.getString("assignee_last_name").equals("null")){
//                            autoCompleteTextViewstaff.setSelection(0);
//                        }
//
//                    } catch (ArrayIndexOutOfBoundsException e) {
//                        e.printStackTrace();
//                    } catch (Exception e) {
//
//                        e.printStackTrace();
//                    }

                    try {
                        if (jsonObject2.getString("priority_name") != null) {

                            spinnerPriority.setSelection(getIndex(spinnerPriority, jsonObject2.getString("priority_name")));


                        }
                    } catch (JSONException | NumberFormatException e) {
                        e.printStackTrace();
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }

                    try {
                        if (jsonObject2.getString("type_name") != null) {

                            for (int j = 0; j < spinnerType.getCount(); j++) {
                                if (spinnerType.getItemAtPosition(j).toString().equalsIgnoreCase(jsonObject2.getString("type_name"))) {
                                    spinnerType.setSelection(j);
                                }
                            }
                        }
                    } catch (JSONException | NumberFormatException e) {
                        e.printStackTrace();
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (jsonObject2.getString("helptopic_name") != null)
                            spinnerHelpTopics.setSelection(getIndex(spinnerHelpTopics, jsonObject2.getString("helptopic_name")));


                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    } catch (Exception e) {

                        e.printStackTrace();
                    }



                    try {
                        if (jsonObject2.getString("source_name") != null)
                            //spinnerSource.setSelection(Integer.parseInt(jsonObject1.getString("source")) - 1);

                            spinnerSource.setSelection(getIndex(spinnerSource, jsonObject2.getString("source_name")));
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
        Data data;
        JSONObject jsonObject;
        String json = Prefs.getString("DEPENDENCY", "");
        try {
            staffItems = new ArrayList<>();
            jsonObject = new JSONObject(json);
            staffItems.add(new Data(0, "--"));
            JSONArray jsonArrayStaffs = jsonObject.getJSONArray("staffs");
            for (int i = 0; i < jsonArrayStaffs.length(); i++) {
                if (jsonArrayStaffs.getJSONObject(i).getString("first_name").equals("")&&jsonArrayStaffs.getJSONObject(i).getString("last_name").equals("")){
                    Log.d("cameHere","TRUE");
                    data = new Data(Integer.parseInt(jsonArrayStaffs.getJSONObject(i).getString("id")), jsonArrayStaffs.getJSONObject(i).getString("email"));
                }
                else {
                    data = new Data(Integer.parseInt(jsonArrayStaffs.getJSONObject(i).getString("id")), jsonArrayStaffs.getJSONObject(i).getString("first_name")+" "+jsonArrayStaffs.getJSONObject(i).getString("last_name"));
                }
                staffItems.add(data);
            }
            helptopicItems = new ArrayList<>();
            helptopicItems.add(new Data(0, "--"));
            jsonObject = new JSONObject(json);
            JSONArray jsonArrayHelpTopics = jsonObject.getJSONArray("helptopics");
            for (int i = 0; i < jsonArrayHelpTopics.length(); i++) {
                Data data1 = new Data(Integer.parseInt(jsonArrayHelpTopics.getJSONObject(i).getString("id")), jsonArrayHelpTopics.getJSONObject(i).getString("topic"));
                helptopicItems.add(data1);
            }

            JSONArray jsonArrayPriorities = jsonObject.getJSONArray("priorities");
            priorityItems = new ArrayList<>();
            priorityItems.add(new Data(0, "--"));
            for (int i = 0; i < jsonArrayPriorities.length(); i++) {
                Data data2 = new Data(Integer.parseInt(jsonArrayPriorities.getJSONObject(i).getString("priority_id")), jsonArrayPriorities.getJSONObject(i).getString("priority"));
                priorityItems.add(data2);
            }

            JSONArray jsonArrayType = jsonObject.getJSONArray("type");
            typeItems = new ArrayList<>();
            typeItems.add(new Data(0, "--"));
            for (int i = 0; i < jsonArrayType.length(); i++) {
                Data data3 = new Data(Integer.parseInt(jsonArrayType.getJSONObject(i).getString("id")), jsonArrayType.getJSONObject(i).getString("name"));
                typeItems.add(data3);

            }

            JSONArray jsonArraySources = jsonObject.getJSONArray("sources");
            sourceItems = new ArrayList<>();
            sourceItems.add(new Data(0, "--"));
            for (int i = 0; i < jsonArraySources.length(); i++) {
                Data data4 = new Data(Integer.parseInt(jsonArraySources.getJSONObject(i).getString("id")), jsonArraySources.getJSONObject(i).getString("name"));
                sourceItems.add(data4);
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

        autoCompleteTextViewstaff= (Spinner) findViewById(R.id.spinner_staffs);
        staffArrayAdapter=new ArrayAdapter<>(TicketSaveActivity.this,android.R.layout.simple_dropdown_item_1line,staffItems);
        //staffArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoCompleteTextViewstaff.setAdapter(staffArrayAdapter);
//        autoCompleteTextViewstaff.setThreshold(1);
//        autoCompleteTextViewstaff.setDropDownWidth(1000);

        spinnerSource= (Spinner) findViewById(R.id.spinner_source);
        spinnerSourceArrayAdapter = new ArrayAdapter<>(TicketSaveActivity.this, android.R.layout.simple_spinner_dropdown_item, sourceItems);
        //selected item will look like a spinner set from XML
        spinnerSourceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSource.setAdapter(spinnerSourceArrayAdapter);


    }

    @Override
    public void onBackPressed() {
        if (!TicketDetailActivity.isShowing) {
            Log.d("isShowing", "false");
            Intent intent = new Intent(this, TicketDetailActivity.class);
            startActivity(intent);
        } else Log.d("isShowing", "true");
        super.onBackPressed();

    }
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent=new Intent(TicketSaveActivity.this,TicketDetailActivity.class);
//        startActivity(intent);
//    }
}
