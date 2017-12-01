package co.helpdesk.faveo.pro.frontend.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.BindView;
import co.helpdesk.faveo.pro.Helper;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import es.dmoral.toasty.Toasty;

public class RegisterUser extends AppCompatActivity {

    ImageView imageViewBack;
    Button submit;
    EditText editTextEmail,editTextFirstName,editTextPhone,editTextCompany,editTextLastName;
//    CountryCodePicker countryCodePicker;
    //String countrycode;
    boolean allCorect;
    ProgressDialog progressDialog;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        submit= (Button) findViewById(R.id.buttonCreateUser);
        editTextEmail= (EditText) findViewById(R.id.email_edittextUser);
        editTextFirstName= (EditText) findViewById(R.id.fname_edittextUser);
        editTextLastName= (EditText) findViewById(R.id.lastname_edittext);
        editTextPhone= (EditText) findViewById(R.id.phone_edittextUser);
        editTextCompany= (EditText) findViewById(R.id.company_edittextUser);
        //countryCodePicker= (CountryCodePicker) findViewById(R.id.countrycoodeUser);
//        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
//            @Override
//            public void onCountrySelected() {
//                //Toast.makeText(MainActivity.this, "code :"+countryCodePicker.getSelectedCountryCode(), Toast.LENGTH_SHORT).show();
//
//                countrycode=countryCodePicker.getSelectedCountryCode();
//            }
//        });
        imageViewBack= (ImageView) findViewById(R.id.imageViewBack);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent  intent=new Intent(co.helpdesk.faveo.pro.frontend.activities.RegisterUser.this,CreateTicketActivity.class);
                startActivity(intent);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allCorect=true;
                String email=editTextEmail.getText().toString();
                String firstname=editTextFirstName.getText().toString();
                String lastname=editTextLastName.getText().toString();
                String phone=editTextPhone.getText().toString();
                String company=editTextCompany.getText().toString();
                //countrycode=countryCodePicker.getSelectedCountryCode();

                if (email.length()==0&&firstname.length()==0){
                    allCorect=false;
                    Toasty.warning(co.helpdesk.faveo.pro.frontend.activities.RegisterUser.this,getString(R.string.fill_all_the_details), Toast.LENGTH_SHORT).show();
                }
                else if (email.length()==0||!Helper.isValidEmail(email)){
                    allCorect=false;
                    Toasty.warning(co.helpdesk.faveo.pro.frontend.activities.RegisterUser.this,getString(R.string.invalid_email),Toast.LENGTH_SHORT).show();
                }
                else if (firstname.length()==0||!Helper.isValidEmail(email)){
                    allCorect=false;
                    Toasty.warning(co.helpdesk.faveo.pro.frontend.activities.RegisterUser.this,getString(R.string.fill_Firstname),Toast.LENGTH_SHORT).show();
                }
                else if (lastname.length()==0||!Helper.isValidEmail(email)){
                    allCorect=false;
                    Toasty.warning(co.helpdesk.faveo.pro.frontend.activities.RegisterUser.this,getString(R.string.fill_Lastname),Toast.LENGTH_SHORT).show();
                }

                if (allCorect) {
                    Prefs.putString("firstusername",firstname);
                    Prefs.putString("lastusername",lastname);
                    Prefs.putString("firstuseremail",email);
                    Prefs.putString("firstusermobile",phone);

                    if (InternetReceiver.isConnected()) {

                        try {
                            firstname = URLEncoder.encode(firstname, "utf-8");
                            lastname=URLEncoder.encode(lastname,"utf-8");
                            email = URLEncoder.encode(email.trim(), "utf-8");
                            phone = URLEncoder.encode(phone.trim(), "utf-8");
                           company=URLEncoder.encode(company,"utf-8");




                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        progressDialog = new ProgressDialog(co.helpdesk.faveo.pro.frontend.activities.RegisterUser.this);
                        progressDialog.show();
                        progressDialog.setMessage(getString(R.string.UserCreating ));

                        new RegisterUserNew(firstname,lastname,email,phone,company).execute();
                        // new CreateNewTicket(Integer.parseInt(Preference.getUserID()), subject, message, helpTopic, SLAPlans, priority, dept, phone, fname, lname, email, countrycode).execute();
                    } else
                        Toasty.info(RegisterUser.this, getString(R.string.oops_no_internet), Toast.LENGTH_SHORT, true).show();
                }

            }
        });

    }

    private class RegisterUserNew extends AsyncTask<String, Void, String> {
     String firstname,email,mobile,company,lastname;

        RegisterUserNew(String firstname,String lastname,String email,String mobile,String company) {

            this.firstname=firstname;
            this.email=email;
            this.mobile=mobile;
            this.company=company;
            this.lastname=lastname;

        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().postRegisterUser(email,firstname,lastname,mobile,company);
        }

        protected void onPostExecute(String result) {

            progressDialog.dismiss();
            if (result == null) {
                Toasty.error(RegisterUser.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }
//            String state=Prefs.getString("403",null);
//                if (message1.contains("The ticket id field is required.")){
//                    Toasty.warning(TicketDetailActivity.this, getString(R.string.please_select_ticket), Toast.LENGTH_LONG).show();
//                }
//                else if (message1.contains("The status id field is required.")){
//                    Toasty.warning(TicketDetailActivity.this, getString(R.string.please_select_status), Toast.LENGTH_LONG).show();
//                }
//               else
//            try {
//                if (state.equals("403") && !state.equals(null)) {
//                    Toasty.warning(RegisterUser.this, getString(R.string.permission), Toast.LENGTH_LONG).show();
//                    Prefs.putString("403", "null");
//                    return;
//                }
//            }catch (NullPointerException e){
//                e.printStackTrace();
//            }
            try {

                //JSONObject jsonObject=new JSONObject(result);
                JSONArray jsonArray=new JSONArray(result);
                //JSONObject jsonObject1=jsonObject.getJSONObject("result");
                for (int i=0;i<jsonArray.length();i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String message=jsonObject.getString("message");
                    JSONObject jsonObject2=jsonObject.getJSONObject("user");
                    email=jsonObject2.getString("email");

                    if (message.contains("Activate your account! Click on the link that we've sent to your mail")){
                        Toasty.success(RegisterUser.this,getString(R.string.registrationsuccesfull),Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(RegisterUser.this,CreateTicketActivity.class);
                        Prefs.putString("newuseremail",email);
                        startActivity(intent);
                    }
                    else{
                        Toasty.success(RegisterUser.this,getString(R.string.registrationsuccesfull),Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(RegisterUser.this,CreateTicketActivity.class);
                        Prefs.putString("newuseremail",email);
                        startActivity(intent);
                    }

                }




            } catch (JSONException e) {
                e.printStackTrace();
            }




        }


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent  intent=new Intent(RegisterUser.this,CreateTicketActivity.class);
        startActivity(intent);
    }
}
