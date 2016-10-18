package co.helpdesk.faveo.pro.frontend.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.helpdesk.faveo.pro.FaveoApplication;
import co.helpdesk.faveo.pro.Helper;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.pro.backend.database.DatabaseHandler;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.pro.model.TicketOverview;


public class SplashActivity extends AppCompatActivity implements InternetReceiver.InternetReceiverListener {


    ProgressDialog progressDialog;

    public static String
            keyDepartment = "", valueDepartment = "",
            keySLA = "", valueSLA = "",
            keyStatus = "", valueStatus = "",
            keyStaff = "", valueStaff = "",
            keyTeam = "", valueTeam = "",
            keyPriority = "", valuePriority = "",
            keyTopic = "", valueTopic = "",
            keySource = "", valueSource = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        if (InternetReceiver.isConnected()) {
            progressDialog.show();
            new FetchDependency(this).execute();
        } else Toast.makeText(this, "Oops! No internet", Toast.LENGTH_LONG).show();
//        if (InternetReceiver.isConnected()) {
//            progressDialog.show();
//            if(Preference.isFetchDependency()){
//                new FetchData(this).execute();
//            }else {
//                new FetchDependency(this).execute();
//                Preference.setFetchDependency(true);
//            }
//        } else Toast.makeText(this, "Oops! No internet", Toast.LENGTH_LONG).show();
    }

    public class FetchDependency extends AsyncTask<String, Void, String> {
        Context context;

        public FetchDependency(Context context) {
            this.context = context;
        }

        protected String doInBackground(String... urls) {
            progressDialog.dismiss();
            return new Helpdesk().getDependency();
        }

        protected void onPostExecute(String result) {
            Log.d("Depen Response code : ", result + "");
            if (result == null) {

                Toast.makeText(SplashActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                return;
            }
            new FetchData(context).execute();

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject jsonObject1 = jsonObject.getJSONObject("result");

                JSONArray jsonArrayDepartments = jsonObject1.getJSONArray("departments");
                for (int i = 0; i < jsonArrayDepartments.length(); i++) {
                    keyDepartment += jsonArrayDepartments.getJSONObject(i).getString("id") + ",";
                    valueDepartment += jsonArrayDepartments.getJSONObject(i).getString("name") + ",";
                }


                JSONArray jsonArraySla = jsonObject1.getJSONArray("sla");
                for (int i = 0; i < jsonArraySla.length(); i++) {
                    keySLA += jsonArraySla.getJSONObject(i).getString("id") + ",";
                    valueSLA += jsonArraySla.getJSONObject(i).getString("name") + ",";
                }


                JSONArray jsonArrayStaffs = jsonObject1.getJSONArray("staffs");
                for (int i = 0; i < jsonArrayStaffs.length(); i++) {
                    keyStaff += jsonArrayStaffs.getJSONObject(i).getString("id") + ",";
                    valueStaff += jsonArrayStaffs.getJSONObject(i).getString("email") + ",";
                }


                JSONArray jsonArrayTeams = jsonObject1.getJSONArray("teams");
                for (int i = 0; i < jsonArrayTeams.length(); i++) {
                    keyTeam += jsonArrayTeams.getJSONObject(i).getString("id") + ",";
                    valueTeam += jsonArrayTeams.getJSONObject(i).getString("name") + ",";
                }


                JSONArray jsonArrayPriorities = jsonObject1.getJSONArray("priorities");
                for (int i = 0; i < jsonArrayPriorities.length(); i++) {
                    keyPriority += jsonArrayPriorities.getJSONObject(i).getString("priority_id") + ",";
                    valuePriority += jsonArrayPriorities.getJSONObject(i).getString("priority") + ",";
                }


                JSONArray jsonArrayHelpTopics = jsonObject1.getJSONArray("helptopics");
                for (int i = 0; i < jsonArrayHelpTopics.length(); i++) {
                    keyTopic += jsonArrayHelpTopics.getJSONObject(i).getString("id") + ",";
                    valueTopic += jsonArrayHelpTopics.getJSONObject(i).getString("topic") + ",";
                }


                JSONArray jsonArrayStatus = jsonObject1.getJSONArray("status");
                for (int i = 0; i < jsonArrayStatus.length(); i++) {
                    keyStatus += jsonArrayStatus.getJSONObject(i).getString("id") + ",";
                    valueStatus += jsonArrayStatus.getJSONObject(i).getString("name") + ",";
                }


                JSONArray jsonArraySources = jsonObject1.getJSONArray("sources");
                for (int i = 0; i < jsonArraySources.length(); i++) {
                    keySource += jsonArraySources.getJSONObject(i).getString("id") + ",";
                    valueSource += jsonArraySources.getJSONObject(i).getString("name") + ",";
                }

                new FetchData(context).execute();

            } catch (JSONException e) {
                Toast.makeText(SplashActivity.this, "Error", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    public class FetchData extends AsyncTask<String, Void, String> {
        Context context;
        String nextPageURL;

        public FetchData(Context context) {
            this.context = context;
        }

        protected String doInBackground(String... urls) {
            progressDialog.dismiss();
            String result = new Helpdesk().getInboxTicket();
            if (result == null)
                return null;
            DatabaseHandler databaseHandler = new DatabaseHandler(context);
            databaseHandler.recreateTable();
            try {
                JSONObject jsonObject = new JSONObject(result);
                nextPageURL = jsonObject.getString("next_page_url");
                String data = jsonObject.getString("data");
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    TicketOverview ticketOverview = Helper.parseTicketOverview(jsonArray, i);
                    if (ticketOverview != null)
                        databaseHandler.addTicketOverview(ticketOverview);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            databaseHandler.close();
            return "success";

        }

        protected void onPostExecute(String result) {
            Log.d("Data Response code : ", result + "");
            if (result == null) {
                Toast.makeText(SplashActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                return;
            }
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.putExtra("nextPageURL", nextPageURL);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        FaveoApplication.getInstance().setInternetListener(this);
        checkConnection();
    }

    private void checkConnection() {
        boolean isConnected = InternetReceiver.isConnected();
        showSnackIfNoInternet(isConnected);
    }

    private void showSnackIfNoInternet(boolean isConnected) {
        if (!isConnected) {
            final Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content), "Sorry! Not connected to internet", Snackbar.LENGTH_INDEFINITE);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.RED);
            snackbar.setAction("X", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }

    }

    private void showSnack(boolean isConnected) {

        if (isConnected) {

            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content), "Connected to Internet", Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        } else {
            showSnackIfNoInternet(false);
        }

    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

}
