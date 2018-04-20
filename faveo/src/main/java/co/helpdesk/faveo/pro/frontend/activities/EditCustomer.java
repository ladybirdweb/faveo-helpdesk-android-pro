package co.helpdesk.faveo.pro.frontend.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.hbb20.CountryCodePicker;
import com.pixplicity.easyprefs.library.Prefs;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import co.helpdesk.faveo.pro.Helper;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.backend.api.v1.Authenticate;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.pro.model.MessageEvent;
import co.helpdesk.faveo.pro.model.TicketGlimpse;
import es.dmoral.toasty.Toasty;

public class EditCustomer extends AppCompatActivity {

    EditText username,firstname,lastname,email,phone,mobile;
    ImageView imageviewback;
    Button submit;
    String clientID;
    //Switch aSwitch;
    int status;
    int i=0;
    TextView textViewActiveOrDeactivated;
    ProgressDialog progressDialog;
    String countrycode = "";
    CountryCodePicker countryCodePicker;
    String userName,firstName,lastName,emailtext,phoneText,mobileText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_customer);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
//        final Handler handler = new Handler();
//        Runnable runnable = new Runnable() {
//            public void run() {
//                //
//                // Do the stuff
//                //
//                String result= new Authenticate().postAuthenticateUser(Prefs.getString("USERNAME", null), Prefs.getString("PASSWORD", null));
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    JSONObject jsonObject1=jsonObject.getJSONObject("data");
//                    JSONObject jsonObject2=jsonObject1.getJSONObject("user");
//                    String role1=jsonObject2.getString("role");
//                    if (role1.equals("user")){
//                        Prefs.clear();
//                        //Prefs.putString("role",role);
//                        Intent intent=new Intent(EditCustomer.this,LoginActivity.class);
//                        Toasty.info(EditCustomer.this,getString(R.string.roleChanged), Toast.LENGTH_LONG).show();
//                        startActivity(intent);
//
//
//                    }
//
//
//                } catch (JSONException | NullPointerException e) {
//                    e.printStackTrace();
//                }
//
//                handler.postDelayed(this, 30000);
//            }
//        };
//        runnable.run();
        countryCodePicker= (CountryCodePicker) findViewById(R.id.countrycoode);
        Log.d("country code",countrycode);
        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                //Toast.makeText(MainActivity.this, "code :"+countryCodePicker.getSelectedCountryCode(), Toast.LENGTH_SHORT).show();

                countrycode=countryCodePicker.getSelectedCountryCode();
                Log.d("country code",countrycode);
            }
        });
        clientID= Prefs.getString("clientId",null);
        Log.d("clientId",clientID);
        username= (EditText) findViewById(R.id.username);
        firstname= (EditText) findViewById(R.id.firstname);
        lastname= (EditText) findViewById(R.id.lastname);
        email= (EditText) findViewById(R.id.email);
        //phone= (EditText) findViewById(R.id.phone);
        mobile= (EditText) findViewById(R.id.mobile);
        //aSwitch= (Switch) findViewById(R.id.default_switch);
        //textViewActiveOrDeactivated= (TextView) findViewById(R.id.activateordeactivate);
        progressDialog=new ProgressDialog(EditCustomer.this);


        submit= (Button) findViewById(R.id.buttonSubmit);
        imageviewback= (ImageView) findViewById(R.id.imageViewBack);

        //


        if (InternetReceiver.isConnected()){
            new FetchClientTickets(EditCustomer.this).execute();
        }

        imageviewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EditCustomer.this,ClientDetailActivity.class);
                startActivity(intent);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().equals("") || firstname.getText().toString().equals("") && lastname.getText().toString().equals("")
                        && email.getText().toString().equals("")) {
                    Toasty.warning(EditCustomer.this, getString(R.string.fill_all_the_details), Toast.LENGTH_SHORT).show();
                    return;
                } else if (username.getText().toString().equals("")) {
                    Toasty.warning(EditCustomer.this, getString(R.string.fill_username), Toast.LENGTH_SHORT).show();
                    return;
                } else if (firstname.getText().toString().equals("")) {
                    Toasty.warning(EditCustomer.this, getString(R.string.fill_Firstname), Toast.LENGTH_SHORT).show();
                    return;
                } else if (lastname.getText().toString().equals("")) {
                    Toasty.warning(EditCustomer.this, getString(R.string.fill_Lastname), Toast.LENGTH_SHORT).show();
                    return;
                } else if (email.getText().toString().equals("") || !Helper.isValidEmail(email.getText().toString())) {
                    Toasty.warning(EditCustomer.this, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!mobile.getText().toString().equals("")&&countrycode.equals("")){
                    Toasty.warning(EditCustomer.this, getString(R.string.mobileCountryCode), Toast.LENGTH_SHORT).show();
                    return;
                }

                //userName=username.getText().toString();
                firstName = firstname.getText().toString();
                lastName = lastname.getText().toString();
                emailtext = email.getText().toString();
                //phoneText=phone.getText().toString();
                mobileText = mobile.getText().toString();
                userName = username.getText().toString();
                if (!mobileText.equals("")) {
                    try {

                        //userName = URLEncoder.encode(userName.trim(), "utf-8");
                        firstName = URLEncoder.encode(firstName.trim(), "utf-8");
                        lastName = URLEncoder.encode(lastName.trim(), "utf-8");
                        emailtext = URLEncoder.encode(emailtext.trim(), "utf-8");
                        //phoneText = URLEncoder.encode(phoneText.trim(), "utf-8");
                        mobileText = URLEncoder.encode(mobileText.trim(), "utf-8");
                        userName = URLEncoder.encode(userName.trim(), "utf-8");


                        if (InternetReceiver.isConnected()) {
                            progressDialog.show();
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            new EditClient(EditCustomer.this, clientID, firstName, lastName, emailtext, userName + "&mobile=" + mobileText + "&code=" + countrycode).execute();
                        }

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                } else {

                    try {

                        //userName = URLEncoder.encode(userName.trim(), "utf-8");
                        firstName = URLEncoder.encode(firstName.trim(), "utf-8");
                        lastName = URLEncoder.encode(lastName.trim(), "utf-8");
                        emailtext = URLEncoder.encode(emailtext.trim(), "utf-8");
                        //phoneText = URLEncoder.encode(phoneText.trim(), "utf-8");
                        mobileText = URLEncoder.encode(mobileText.trim(), "utf-8");
                        userName = URLEncoder.encode(userName.trim(), "utf-8");

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                    if (InternetReceiver.isConnected()) {
                        progressDialog.show();
                        progressDialog.setMessage(getString(R.string.pleasewait));
                        new EditClient(EditCustomer.this, clientID, firstName, lastName, emailtext, userName).execute();
                    }
                }
//                else{
//                    phoneText.equals("")
//                }
            }
        });





    }
    private class FetchClientTickets extends AsyncTask<String, Void, String> {
        Context context;
        FetchClientTickets(Context context) {
            this.context = context;
        }

        protected String doInBackground(String... urls) {
//            listTicketGlimpse = new ArrayList<>();
//            String result = new Helpdesk().getTicketsByUser(clientID);
//            if (result == null)
//                return null;
//            try {
//                JSONObject jsonObject = new JSONObject(result);
//
//                JSONArray jsonArray = jsonObject.getJSONArray("tickets");
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    int ticketID = Integer.parseInt(jsonArray.getJSONObject(i).getString("id"));
//                    boolean isOpen = true;
//                    String ticketNumber = jsonArray.getJSONObject(i).getString("ticket_number");
//                    String ticketSubject = jsonArray.getJSONObject(i).getString("title");
//                    try {
//                        isOpen = jsonArray.getJSONObject(i).getString("ticket_status_name").equals("Open");
//                        if (isOpen)
//                            listOpenTicketGlimpse.add(new TicketGlimpse(ticketID, ticketNumber, ticketSubject, true));
//                        else
//                            listClosedTicketGlimpse.add(new TicketGlimpse(ticketID, ticketNumber, ticketSubject, false));
//                    } catch (Exception e) {
//                        listOpenTicketGlimpse.add(new TicketGlimpse(ticketID, ticketNumber, ticketSubject, true));
//                    }
//                    listTicketGlimpse.add(new TicketGlimpse(ticketID, ticketNumber, ticketSubject, isOpen));
//                }
//            } catch (JSONException e) {
//                Toast.makeText(ClientDetailActivity.this, R.string.unexpected_error, Toast.LENGTH_LONG).show();
//                e.printStackTrace();
//            }
            // return "success";
            return new Helpdesk().getTicketsByUser(clientID);
        }

        protected void onPostExecute(String result) {
            if (isCancelled()) return;

            if (result == null) return;

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject requester = jsonObject.getJSONObject("requester");
                String firstname1 = requester.getString("first_name");
                String lastName1 = requester.getString("last_name");
                String username1 = requester.getString("user_name");
                String  phone1 = requester.getString("phone_number");
                String  mobile1=requester.getString("mobile");
                String clientname1;

                if (firstname1 == null || firstname1.equals(""))
                    clientname1 = username1;
                else
                    clientname1 = firstname1 + " " + lastName1;

                username.setText(clientname1);
                email.setText(requester.getString("email"));
                if (firstname1.equals("null")||firstname1.equals("")){
                    firstname.setText("");
                }else {
                    firstname.setText(firstname1);
                }
                if (lastName1.equals("null")||lastName1.equals("")){
                    lastname.setText("");
                }else {
                    lastname.setText(lastName1);
                }
//                if (phone1.equals("null")||phone1.equals("")||phone1.equals("Not available")){
//                    phone.setText("");
//                }else {
//                    phone.setText(phone1);
//                }
                if (mobile1.equals("null")||mobile1.equals("")||mobile1.equals("Not available")){
                    mobile.setText("");
                }
                else {
                    mobile.setText(mobile1);
                }

                status= Integer.parseInt(requester.getString("is_delete"));
//                if (status==1){
//
//                    aSwitch.setTextOff(getString(R.string.activateUser));
//                    //textViewActiveOrDeactivated.setText(getString(R.string.activateUser));
//
//                }
//                else if (status==0){
//
//                    aSwitch.setTextOn(getString(R.string.deactivateuser));
//                    //textViewActiveOrDeactivated.setText(getString(R.string.deactivateuser));
//                }
//                else{
//                    aSwitch.setVisibility(View.GONE);
//                }




            } catch (JSONException e) {
                Toasty.error(EditCustomer.this, getString(R.string.unexpected_error), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        }
    }

    private class EditClient extends AsyncTask<String, Void, String> {
        Context context;
        String clientid;
        String firstname;
        String lastname;
        String email;
        String username1;
        String badRequest;
        EditClient(Context context,String clientid,
                String firstname,
                String lastname,
                String email,String username1) {
            this.context = context;
            this.clientid=clientid;
            this.firstname=firstname;
            this.lastname=lastname;
            this.email=email;
            this.username1=username1;

        }

        protected String doInBackground(String... urls) {

            return new Helpdesk().saveCustomerDetails(clientID,firstname,lastname,email,username1);
        }

        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            if (isCancelled()) return;


//            if (result == null) return;
            try {
                Log.d("depenResponse", result);
            }catch (NullPointerException e){
                e.printStackTrace();
            }
//            try {
//                badRequest = Prefs.getString("400", null);
//                Log.d("badREquest",badRequest);
//            }catch (NullPointerException e){
//                e.printStackTrace();
//            }
//            if (badRequest.equals("badRequest")){
//                try{
//                JSONObject jsonObject=new JSONObject(result);
//                String message=jsonObject.getString("message");
//                if (message.equals("The code field is required")){
//                    Toast.makeText(EditCustomer.this, getString(R.string.select_code), Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//            }
//            catch (JSONException e){
//                e.printStackTrace();
//            }
//            }
//            try{
//                JSONObject jsonObject=new JSONObject(result);
//                String message=jsonObject.getString("message");
//                if (message.equals("The code feild is required")){
//                    Toast.makeText(EditCustomer.this, getString(R.string.select_code), Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//            }
//            catch (JSONException e){
//                e.printStackTrace();
//            }


            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                String message=jsonObject1.getString("message");
                if (message.equals("Updated successfully")){
                    i=0;
                    Toasty.success(EditCustomer.this,getString(R.string.editedcustomerdetails),Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(EditCustomer.this,ClientDetailActivity.class);
                    startActivity(intent);
                }



            } catch (JSONException e) {
                Toasty.error(EditCustomer.this, getString(R.string.unexpected_error), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        }
    }
    private void showSnack(boolean isConnected) {

        if (isConnected) {
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content), R.string.connected_to_internet, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        } else {
            showSnackIfNoInternet(false);
        }

    }
    private void showSnackIfNoInternet(boolean isConnected) {
        if (!isConnected) {
            final Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content), R.string.sry_not_connected_to_internet, Snackbar.LENGTH_INDEFINITE);

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
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {

        showSnack(event.message);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

}
