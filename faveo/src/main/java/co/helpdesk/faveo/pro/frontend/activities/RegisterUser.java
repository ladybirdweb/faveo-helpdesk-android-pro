package co.helpdesk.faveo.pro.frontend.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

    ImageView imageViewBackFromRegister;
    Button submit;
    EditText editTextEmail,editTextFirstName,editTextPhone,editTextCompany,editTextLastName;
    boolean allCorect;
    ProgressDialog progressDialog;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register_user);
        Window window = RegisterUser.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(RegisterUser.this,R.color.faveo));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        submit= (Button) findViewById(R.id.buttonCreateUser);
        editTextEmail= (EditText) findViewById(R.id.email_edittextUser);
        editTextFirstName= (EditText) findViewById(R.id.fname_edittextUser);
        editTextLastName= (EditText) findViewById(R.id.lastname_edittext);
        editTextPhone= (EditText) findViewById(R.id.phone_edittextUser);
        editTextCompany= (EditText) findViewById(R.id.company_edittextUser);
        imageViewBackFromRegister= (ImageView) findViewById(R.id.imageViewBack);
        imageViewBackFromRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

                if (email.length()==0&&firstname.length()==0&&lastname.length()==0){
                    allCorect=false;
                    Toasty.warning(co.helpdesk.faveo.pro.frontend.activities.RegisterUser.this,getString(R.string.fill_all_the_details), Toast.LENGTH_SHORT).show();
                }
                else if (email.length()==0||!Helper.isValidEmail(email)){
                    allCorect=false;
                    Toasty.warning(co.helpdesk.faveo.pro.frontend.activities.RegisterUser.this,getString(R.string.invalid_email),Toast.LENGTH_SHORT).show();
                }
                else if (firstname.length()==0){
                    allCorect=false;
                    Toasty.warning(co.helpdesk.faveo.pro.frontend.activities.RegisterUser.this,getString(R.string.fill_Firstname),Toast.LENGTH_SHORT).show();
                }
                else if (firstname.matches("^[0-9]*$")){
                    allCorect=false;
                    Toasty.warning(RegisterUser.this,getString(R.string.firstNameCharacter),Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (lastname.length()==0){
                    allCorect=false;
                    Toasty.warning(co.helpdesk.faveo.pro.frontend.activities.RegisterUser.this,getString(R.string.fill_Lastname),Toast.LENGTH_SHORT).show();
                }
                else if (lastname.matches("^[0-9]*$")){
                    allCorect=false;
                    Toasty.warning(RegisterUser.this,getString(R.string.lastNameCharacter),Toast.LENGTH_SHORT).show();
                    return;
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
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterUser.this);

                        // Setting Dialog Title
                        alertDialog.setTitle(getString(R.string.registerUser));

                        // Setting Dialog Message
                        alertDialog.setMessage(getString(R.string.userConfirmation));

                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.mipmap.ic_launcher);

                        // Setting Positive "Yes" Button
                        final String finalFirstname = firstname;
                        final String finalLastname = lastname;
                        final String finalEmail = email;
                        final String finalPhone = phone;
                        final String finalCompany = company;
                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to invoke YES event
                                //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                                if (InternetReceiver.isConnected()){
                                    progressDialog = new ProgressDialog(RegisterUser.this);
                                    progressDialog.show();
                                    progressDialog.setMessage(getString(R.string.UserCreating ));
                                    new RegisterUserNew(finalFirstname, finalLastname, finalEmail, finalPhone, finalCompany).execute();
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
            try{
                JSONObject jsonObject=new JSONObject(result);
                JSONObject jsonObject1=jsonObject.getJSONObject("result");
                String error=jsonObject1.getString("error");
                if (error.equals("lang.methon_not_allowed")){
                    Toasty.success(RegisterUser.this,getString(R.string.registrationsuccesfull),Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(RegisterUser.this,CreateTicketActivity.class);
                    Prefs.putString("newuseremail",email);
                    startActivity(intent);

                }

            }catch (JSONException e){
                e.printStackTrace();
            }
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
        finish();
    }
}