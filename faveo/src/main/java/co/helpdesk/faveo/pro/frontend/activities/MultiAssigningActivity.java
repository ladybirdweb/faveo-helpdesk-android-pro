package co.helpdesk.faveo.pro.frontend.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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
import co.helpdesk.faveo.pro.backend.api.v1.Authenticate;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.pro.frontend.fragments.tickets.InboxTickets;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.pro.model.Data;
import es.dmoral.toasty.Toasty;

public class MultiAssigningActivity extends AppCompatActivity {

    AutoCompleteTextView autoCompleteTextViewselectAssignee;
    ImageView imageViewback,buttonAssign;
    ArrayList<Data> staffItems;
    ArrayList<Data> staffitemsauto;
    ArrayAdapter<Data> autoAssignAdapyter;
    int agentId;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        staffItems=new ArrayList<>();
        staffitemsauto=new ArrayList<>();
        buttonAssign= (ImageView) findViewById(R.id.buttonAssign);
        autoCompleteTextViewselectAssignee= (AutoCompleteTextView) findViewById(R.id.selectAssignee);
        imageViewback= (ImageView) findViewById(R.id.imageViewBack);
        autoAssignAdapyter=new ArrayAdapter<Data>(this, android.R.layout.simple_dropdown_item_1line,staffItems);
        autoCompleteTextViewselectAssignee.setAdapter(autoAssignAdapyter);
        autoCompleteTextViewselectAssignee.setThreshold(0);
        autoCompleteTextViewselectAssignee.setDropDownWidth(1500);
        autoCompleteTextViewselectAssignee.addTextChangedListener(passwordWatcheredittextSubject);
        if (InternetReceiver.isConnected()){
            new FetchDependency().execute();
        }
        imageViewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
                        alertDialog.setTitle("Assigning tickets...");

                        // Setting Dialog Message
                        alertDialog.setMessage("Are you sure you want to assign these tickets?");

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
        if (!MainActivity.isShowing) {
            Log.d("isShowing", "false");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else Log.d("isShowing", "true");


            super.onBackPressed();

//        if (fabExpanded)
//            exitReveal();
//        else super.onBackPressed();
    }

   private class FetchDependency extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {

            return new Helpdesk().getDependency();

        }

        protected void onPostExecute(String result) {
            //Log.d("Depen Response : ", result + "");

            if (result == null) {
                Toasty.error(MultiAssigningActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                JSONArray jsonArrayStaffs = jsonObject1.getJSONArray("staffs");
                for (int i = 0; i < jsonArrayStaffs.length(); i++) {
                    Data data=new Data(Integer.parseInt(jsonArrayStaffs.getJSONObject(i).getString("id")),jsonArrayStaffs.getJSONObject(i).getString("first_name")+" "+jsonArrayStaffs.getJSONObject(i).getString("last_name"));
                    staffItems.add(data);
                    staffitemsauto.add(data);
                }
                Log.d("staffItems",staffItems.toString());

            } catch (JSONException e) {
                Toasty.error(MultiAssigningActivity.this, "Parsing Error!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }
    private class MultipleAssign extends AsyncTask<String, Void, String> {
        String ticketid,assignid;

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
            } finally {
                Toasty.success(MultiAssigningActivity.this, getString(R.string.successfullyAssigned), Toast.LENGTH_LONG).show();
                Intent intent=new Intent(MultiAssigningActivity.this,MainActivity.class);
                startActivity(intent);
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
