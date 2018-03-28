package co.helpdesk.faveo.pro.frontend.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

        imageViewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(MultiAssigningActivity.this,MainActivity.class);
//                startActivity(intent);
                onBackPressed();
            }
        });
        buttonAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer stringBuffer = new StringBuffer();
                String ticket;
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

                        if (InternetReceiver.isConnected()){
                            progressDialog = new ProgressDialog(MultiAssigningActivity.this);
                            progressDialog.setMessage("Please wait");
                            progressDialog.show();
                            new MultipleAssign(ticket,""+agentId).execute();


                        }

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
        if (InternetReceiver.isConnected()){
            new FetchDependency().execute();
        }


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
            Log.d("Depen Response : ", result + "");

            if (result == null) {
                Toasty.error(MultiAssigningActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }



            try {

                JSONObject jsonObject = new JSONObject(result);
JSONObject jsonObject1=jsonObject.getJSONObject("data");
                //JSONObject jsonObject1 = jsonObject.getJSONObject("result");

                JSONArray jsonArrayStaffs = jsonObject1.getJSONArray("staffs");
                for (int i = 0; i < jsonArrayStaffs.length(); i++) {
                    Data data=new Data(Integer.parseInt(jsonArrayStaffs.getJSONObject(i).getString("id")),jsonArrayStaffs.getJSONObject(i).getString("first_name")+" "+jsonArrayStaffs.getJSONObject(i).getString("last_name"));
                    staffItems.add(data);
                    staffitemsauto.add(data);
//                        jsonArrayStaffs.getJSONObject(i).getString("first_name") + jsonArrayStaffs.getJSONObject(i).getString("last_name") +",";
//                         jsonArrayStaffs.getJSONObject(i).getString("id") + ",";
//                         jsonArrayStaffs.getJSONObject(i).getString("email") + ",";
                }

            } catch (JSONException e) {
                Toasty.error(MultiAssigningActivity.this, "Parsing Error!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } finally {


            }

//            AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
//            builder.setTitle("Welcome to FAVEO");
//            //builder.setMessage("After 2 second, this dialog will be closed automatically!");
//            builder.setCancelable(true);
//
//            final AlertDialog dlg = builder.create();
//
//            dlg.show();
//
//            final Timer t = new Timer();
//            t.schedule(new TimerTask() {
//                public void run() {
//                    dlg.dismiss(); // when the task active then close the dialog
//                    t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
//                }
//            }, 3000);
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
            try {

                JSONObject jsonObject = new JSONObject(result);
                String response=jsonObject.getString("message");
                if (response.equals("Assigned successfully")){
                    Toasty.success(MultiAssigningActivity.this, getString(R.string.successfullyAssigned), Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(MultiAssigningActivity.this,MainActivity.class);
                    startActivity(intent);
                }

//                JSONObject jsonObject1 = jsonObject.getJSONObject("result");
//
//                JSONArray jsonArrayStaffs = jsonObject1.getJSONArray("staffs");
//                for (int i = 0; i < jsonArrayStaffs.length(); i++) {
//                    Data data=new Data(Integer.parseInt(jsonArrayStaffs.getJSONObject(i).getString("id")),jsonArrayStaffs.getJSONObject(i).getString("first_name")+" "+jsonArrayStaffs.getJSONObject(i).getString("last_name"));
//                    staffItems.add(data);
//                    staffitemsauto.add(data);
////                        jsonArrayStaffs.getJSONObject(i).getString("first_name") + jsonArrayStaffs.getJSONObject(i).getString("last_name") +",";
////                         jsonArrayStaffs.getJSONObject(i).getString("id") + ",";
////                         jsonArrayStaffs.getJSONObject(i).getString("email") + ",";
//                }

            } catch (JSONException e) {
                Toasty.error(MultiAssigningActivity.this, "Parsing Error!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } finally {
                Toasty.success(MultiAssigningActivity.this, getString(R.string.successfullyAssigned), Toast.LENGTH_LONG).show();
                Intent intent=new Intent(MultiAssigningActivity.this,MainActivity.class);
                startActivity(intent);
            }

//            AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
//            builder.setTitle("Welcome to FAVEO");
//            //builder.setMessage("After 2 second, this dialog will be closed automatically!");
//            builder.setCancelable(true);
//
//            final AlertDialog dlg = builder.create();
//
//            dlg.show();
//
//            final Timer t = new Timer();
//            t.schedule(new TimerTask() {
//                public void run() {
//                    dlg.dismiss(); // when the task active then close the dialog
//                    t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
//                }
//            }, 3000);
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
