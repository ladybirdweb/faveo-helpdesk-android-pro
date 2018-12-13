package co.helpdesk.faveo.pro.frontend.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.pro.model.Data;
import es.dmoral.toasty.Toasty;

public class MultiAssigningActivity extends AppCompatActivity {

    AutoCompleteTextView autoCompleteTextViewselectAssignee;
    ImageView imageViewback,buttonAssign;
    ArrayList<Data> staffItems;
    ArrayAdapter<Data> autoAssignAdapyter;
    int agentId;
    ProgressDialog progressDialog;
    public static String
            keyDepartment = "", valueDepartment = "",
            keySLA = "", valueSLA = "",
            keyStatus = "", valueStatus = "",
            keyStaff = "", valueStaff = "",
            keyName="",
            keyPriority = "", valuePriority = "",
            keyTopic = "", valueTopic = "",
            keySource = "", valueSource = "",
            keyType = "", valueType = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Window window = MultiAssigningActivity.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(MultiAssigningActivity.this,R.color.faveo));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        staffItems=new ArrayList<>();
        if (InternetReceiver.isConnected()){
            new FetchDependency().execute();
        }
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
                if (jsonArrayStaffs.getJSONObject(i).getString("first_name").equals("") && jsonArrayStaffs.getJSONObject(i).getString("last_name").equals("")) {
                    Log.d("cameHere", "TRUE");
                    data = new Data(Integer.parseInt(jsonArrayStaffs.getJSONObject(i).getString("id")), jsonArrayStaffs.getJSONObject(i).getString("email"));
                } else {
                    data = new Data(Integer.parseInt(jsonArrayStaffs.getJSONObject(i).getString("id")), jsonArrayStaffs.getJSONObject(i).getString("first_name") + " " + jsonArrayStaffs.getJSONObject(i).getString("last_name"));
                }
                staffItems.add(data);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        buttonAssign= (ImageView) findViewById(R.id.buttonAssign);
        autoCompleteTextViewselectAssignee= (AutoCompleteTextView) findViewById(R.id.selectAssignee);
        imageViewback= (ImageView) findViewById(R.id.imageViewBack);
        autoAssignAdapyter=new ArrayAdapter<Data>(this, android.R.layout.simple_dropdown_item_1line,staffItems);
        autoCompleteTextViewselectAssignee.setAdapter(autoAssignAdapyter);
        autoCompleteTextViewselectAssignee.setThreshold(0);
        autoCompleteTextViewselectAssignee.setDropDownWidth(1500);
        autoCompleteTextViewselectAssignee.addTextChangedListener(passwordWatcheredittextSubject);
        imageViewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MultiAssigningActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        buttonAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer stringBuffer = new StringBuffer();
                final String ticket;
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


                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MultiAssigningActivity.this);

                        // Setting Dialog Title
                        alertDialog.setTitle(getString(R.string.assigningTickets));

                        // Setting Dialog Message
                        alertDialog.setMessage(getString(R.string.assigningConfirmation));

                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.mipmap.ic_launcher);

                        // Setting Positive "Yes" Button
                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to invoke YES event
                                //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                                if (InternetReceiver.isConnected()){
                                    progressDialog = new ProgressDialog(MultiAssigningActivity.this);
                                    progressDialog.setMessage("Please wait");
                                    progressDialog.show();
                                    new MultipleAssign(ticket,""+agentId).execute();


                                }
                            }
                        });

                        // Setting Negative "NO" Button
                        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to invoke NO event
                                //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();



                    } else {
                        Toasty.info(MultiAssigningActivity.this, getString(R.string.noticket), Toast.LENGTH_LONG).show();

                    }
                } catch (NullPointerException e) {
                    Toasty.info(MultiAssigningActivity.this, getString(R.string.noticket), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        });

        autoCompleteTextViewselectAssignee.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name1=autoCompleteTextViewselectAssignee.getText().toString();
                for (int j = 0; j < staffItems.size(); j++) {
                    if (staffItems.get(j).getName().equalsIgnoreCase(name1)) {
                        Data data = staffItems.get(j);
                        id = data.getID();
                        agentId=data.getID();
                        buttonAssign.setVisibility(View.VISIBLE);
                        //Toast.makeText(MultiAssigningActivity.this, "id:" + id, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(MultiAssigningActivity.this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
//        if (InternetReceiver.isConnected()){
//            new FetchDependency().execute();
//        }
        super.onResume();
    }

    private class FetchDependency extends AsyncTask<String, Void, String> {
        String unauthorized;

        protected String doInBackground(String... urls) {

            return new Helpdesk().getDependency();

        }

        protected void onPostExecute(String result) {
            Log.d("Depen Response : ", result + "");
            Log.d("cameHere","True");

            if (result==null) {
            }


            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                Prefs.putString("DEPENDENCY", jsonObject1.toString());
                // Preference.setDependencyObject(jsonObject1, "dependency");
                JSONArray jsonArrayDepartments = jsonObject1.getJSONArray("departments");
                for (int i = 0; i < jsonArrayDepartments.length(); i++) {
                    keyDepartment += jsonArrayDepartments.getJSONObject(i).getString("id") + ",";
                    valueDepartment += jsonArrayDepartments.getJSONObject(i).getString("name") + ",";
                }
                Prefs.putString("keyDept", keyDepartment);
                Prefs.putString("valueDept", valueDepartment);


                JSONArray jsonArraySla = jsonObject1.getJSONArray("sla");
                for (int i = 0; i < jsonArraySla.length(); i++) {
                    keySLA += jsonArraySla.getJSONObject(i).getString("id") + ",";
                    valueSLA += jsonArraySla.getJSONObject(i).getString("name") + ",";
                }
                Prefs.putString("keySLA", keySLA);
                Prefs.putString("valueSLA", valueSLA);

                JSONArray jsonArrayStaffs = jsonObject1.getJSONArray("staffs");
                for (int i = 0; i < jsonArrayStaffs.length(); i++) {
                    keyName +=jsonArrayStaffs.getJSONObject(i).getString("first_name") + jsonArrayStaffs.getJSONObject(i).getString("last_name") +",";
                    keyStaff += jsonArrayStaffs.getJSONObject(i).getString("id") + ",";
                    valueStaff += jsonArrayStaffs.getJSONObject(i).getString("email") + ",";
                }
                Prefs.putString("keyName",keyName);
                Prefs.putString("keyStaff", keyStaff);
                Prefs.putString("valueStaff", valueStaff);

                JSONArray jsonArrayType = jsonObject1.getJSONArray("type");
                for (int i = 0; i < jsonArrayType.length(); i++) {
                    keyType += jsonArrayType.getJSONObject(i).getString("id") + ",";
                    valueType += jsonArrayType.getJSONObject(i).getString("name") + ",";
                }
                Prefs.putString("keyType", keyType);
                Prefs.putString("valueType", valueType);
                JSONArray jsonArrayPriorities = jsonObject1.getJSONArray("priorities");
                for (int i = 0; i < jsonArrayPriorities.length(); i++) {
                    // keyPri.add(jsonArrayPriorities.getJSONObject(i).getString("priority_id"));
                    //valuePri.add(jsonArrayPriorities.getJSONObject(i).getString("priority"));
                    keyPriority += jsonArrayPriorities.getJSONObject(i).getString("priority_id") + ",";
                    valuePriority += jsonArrayPriorities.getJSONObject(i).getString("priority") + ",";
                }
                Prefs.putString("keyPri", keyPriority);
                Prefs.putString("valuePri", valuePriority);
                //Prefs.putOrderedStringSet("keyPri", keyPri);
                // Prefs.putOrderedStringSet("valuePri", valuePri);
                //Log.d("Testtttttt", Prefs.getOrderedStringSet("keyPri", keyPri) + "   " + Prefs.getOrderedStringSet("valuePri", valuePri));


                JSONArray jsonArrayHelpTopics = jsonObject1.getJSONArray("helptopics");
                for (int i = 0; i < jsonArrayHelpTopics.length(); i++) {

                    keyTopic += jsonArrayHelpTopics.getJSONObject(i).getString("id") + ",";
                    valueTopic += jsonArrayHelpTopics.getJSONObject(i).getString("topic") + ",";
                }

                Prefs.putString("keyHelpTopic", keyTopic);
                Prefs.putString("valueHelptopic", valueTopic);

                JSONArray jsonArrayStatus = jsonObject1.getJSONArray("status");
                for (int i = 0; i < jsonArrayStatus.length(); i++) {

                    keyStatus += jsonArrayStatus.getJSONObject(i).getString("id") + ",";
                    valueStatus += jsonArrayStatus.getJSONObject(i).getString("name") + ",";

                }
                Prefs.putString("keyStatus", keyStatus);
                Prefs.putString("valueStatus", valueStatus);

                JSONArray jsonArraySources = jsonObject1.getJSONArray("sources");
                for (int i = 0; i < jsonArraySources.length(); i++) {
                    keySource += jsonArraySources.getJSONObject(i).getString("id") + ",";
                    valueSource += jsonArraySources.getJSONObject(i).getString("name") + ",";
                }

                Prefs.putString("keySource", keySource);
                Prefs.putString("valueSource", valueSource);

                int open = 0, closed = 0, trash = 0, unasigned = 0, my_tickets = 0;
                JSONArray jsonArrayTicketsCount = jsonObject1.getJSONArray("tickets_count");
                for (int i = 0; i < jsonArrayTicketsCount.length(); i++) {
                    String name = jsonArrayTicketsCount.getJSONObject(i).getString("name");
                    String count = jsonArrayTicketsCount.getJSONObject(i).getString("count");

                    switch (name) {
                        case "Open":
                            open = Integer.parseInt(count);
                            break;
                        case "Closed":
                            closed = Integer.parseInt(count);
                            break;
                        case "Deleted":
                            trash = Integer.parseInt(count);
                            break;
                        case "unassigned":
                            unasigned = Integer.parseInt(count);
                            break;
                        case "mytickets":
                            my_tickets = Integer.parseInt(count);
                            break;
                        default:
                            break;

                    }
                }


                if (open > 999)
                    Prefs.putString("inboxTickets", "999+");
                else
                    Prefs.putString("inboxTickets", open + "");

                if (closed > 999)
                    Prefs.putString("closedTickets", "999+");
                else
                    Prefs.putString("closedTickets", closed + "");

                if (my_tickets > 999)
                    Prefs.putString("myTickets", "999+");
                else
                    Prefs.putString("myTickets", my_tickets + "");

                if (trash > 999)
                    Prefs.putString("trashTickets", "999+");
                else
                    Prefs.putString("trashTickets", trash + "");

                if (unasigned > 999)
                    Prefs.putString("unassignedTickets", "999+");
                else
                    Prefs.putString("unassignedTickets", unasigned + "");

            } catch (JSONException | NullPointerException e) {
                //Toasty.error(SplashActivity.this, "Parsing Error!", Toast.LENGTH_LONG).show();
                Prefs.putString("unauthorized", "false");
                Prefs.putString("401","false");
                e.printStackTrace();
            }

        }
    }
    @SuppressLint("StaticFieldLeak")
    private class MultipleAssign extends AsyncTask<String, Void, String> {
         private String ticketid,assignid;

        MultipleAssign(String ticketid, String assignid) {
            this.ticketid = ticketid;
            this.assignid = assignid;
        }

        protected String doInBackground(String... urls) {

            return new Helpdesk().multipleTicketAssign(ticketid,assignid);
        }
        protected void onPostExecute(String result) {
            Log.d("Depen Response : ", result + "");
            progressDialog.dismiss();

            if (result == null) {
                Toasty.error(MultiAssigningActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }
            String state=Prefs.getString("403",null);

            try {
                if (state.equals("403") && !state.equals(null)) {
                    Toasty.warning(MultiAssigningActivity.this, getString(R.string.permission), Toast.LENGTH_LONG).show();
                    Prefs.putString("403", "null");
                    return;
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }


            try {

                JSONObject jsonObject = new JSONObject(result);
                String response=jsonObject.getString("message");
                if (response.equals("Assigned successfully")){
                    Toasty.success(MultiAssigningActivity.this, getString(R.string.successfullyAssigned), Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(MultiAssigningActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            } catch (JSONException e) {
                Toasty.error(MultiAssigningActivity.this, "Parsing Error!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }
    TextWatcher passwordWatcheredittextSubject = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        public void afterTextChanged(Editable s) {
            if (autoCompleteTextViewselectAssignee.getText().toString().equals("")){
                buttonAssign.setVisibility(View.GONE);
            }
        }
    };

}