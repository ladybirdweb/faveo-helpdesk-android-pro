package co.helpdesk.faveo.pro.frontend.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.kishan.askpermission.AskPermission;
import com.kishan.askpermission.ErrorCallback;
import com.kishan.askpermission.PermissionCallback;
import com.kishan.askpermission.PermissionInterface;
import com.pixplicity.easyprefs.library.Prefs;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import co.helpdesk.faveo.pro.Helper;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.pro.model.MessageEvent;
import es.dmoral.toasty.Toasty;

public class EditCustomer extends AppCompatActivity implements PermissionCallback, ErrorCallback {

    EditText username, firstname, lastname, email, phoneEditText, mobile;
    ImageView imageviewback, imageViewCallMobile, imageViewCallPhone;
    Button submit;
    String clientID;
    int status;
    int i = 0;
    ProgressDialog progressDialog;
    String countrycode = "";
    CountryCodePicker countryCodePicker;
    String userName, firstName, lastName, emailtext, phoneText, mobileText;
    String usernameGet="",firstnameGet="",lastnameGet="",emailGet="",phoneGet="",mobileGet="";
    String phone1,mobile1;
    private int PICKFILE_REQUEST_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Window window = EditCustomer.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(EditCustomer.this,R.color.mainActivityTopBar));
        getCountryZipCode();
        countryCodePicker = (CountryCodePicker) findViewById(R.id.countrycoode);
        imageViewCallMobile = (ImageView) findViewById(R.id.forCalling);
        imageViewCallPhone = (ImageView) findViewById(R.id.forCallingPhone);

        imageViewCallPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reqPermissionCamera();


            }
        });

        imageViewCallMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reqPermissionCamera();

            }
        });

        //textViewUserStatus= (TextView) findViewById(R.id.userStatus);
        //userStatusChangeButton= (Button) findViewById(R.id.userStatusChange);
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
        phoneEditText= (EditText) findViewById(R.id.phone);
        //aSwitch= (Switch) findViewById(R.id.default_switch);
        //textViewActiveOrDeactivated= (TextView) findViewById(R.id.activateordeactivate);
        progressDialog=new ProgressDialog(EditCustomer.this);
        username.addTextChangedListener(textWatcherUser);
        firstname.addTextChangedListener(textWatcherFirst);
        lastname.addTextChangedListener(textWatcherLast);
        email.addTextChangedListener(textWatcherEmail);
        phoneEditText.addTextChangedListener(textWatcherPhone);
        mobile.addTextChangedListener(textWatcherMobile);
        submit= (Button) findViewById(R.id.buttonSubmit);
        imageviewback= (ImageView) findViewById(R.id.imageViewBack);

        if (InternetReceiver.isConnected()){
            progressDialog.setMessage(getString(R.string.pleasewait));
            progressDialog.show();
            new FetchClientTickets(EditCustomer.this).execute();
        }

        imageviewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("username",usernameGet);
                Log.d("firstnameGet",firstnameGet);
                Log.d("lastnameGet",lastnameGet);
                Log.d("emailGet",emailGet);
                Log.d("phoneGet",phoneGet);
                Log.d("mobileGet",mobileGet);

                if (!usernameGet.equalsIgnoreCase(username.getText().toString())||!firstnameGet.equalsIgnoreCase(firstname.getText().toString())||
                        !lastnameGet.equalsIgnoreCase(lastname.getText().toString())||!emailGet.equalsIgnoreCase(email.getText().toString())||!phoneGet.equalsIgnoreCase(phoneEditText.getText().toString())
                        ||!mobileGet.equalsIgnoreCase(mobile.getText().toString())){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditCustomer.this);

                    // Setting Dialog Title
                    alertDialog.setTitle(R.string.discard);

                    // Setting Dialog Message
                    //alertDialog.setMessage(getString(R.string.createConfirmation));

                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.mipmap.ic_launcher);

                    // Setting Positive "Yes" Button

                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke YES event
                            //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(EditCustomer.this,ClientDetailActivity.class);
                            startActivity(intent);
                            finish();
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
                }
                else{
                    Intent intent=new Intent(EditCustomer.this,ClientDetailActivity.class);
                    startActivity(intent);
                }


            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName = firstname.getText().toString();
                lastName = lastname.getText().toString();
                emailtext = email.getText().toString();
                phoneText = phoneEditText.getText().toString();
                mobileText = mobile.getText().toString();
                userName = username.getText().toString();
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
                } else if (firstName.matches("^[0-9]*$")) {
                    Toasty.warning(EditCustomer.this, getString(R.string.firstNameCharacter), Toast.LENGTH_SHORT).show();
                    return;
                } else if (lastname.getText().toString().equals("")) {
                    Toasty.warning(EditCustomer.this, getString(R.string.fill_Lastname), Toast.LENGTH_SHORT).show();
                    return;
                } else if (lastName.matches("^[0-9]*$") && lastName.length() > 2) {
                    Toasty.warning(EditCustomer.this, getString(R.string.lastNameCharacter), Toast.LENGTH_SHORT).show();
                    return;
                } else if (email.getText().toString().equals("") || !Helper.isValidEmail(email.getText().toString())) {
                    Toasty.warning(EditCustomer.this, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (phoneText.equals("")) {
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
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditCustomer.this);
                                // Setting Dialog Title
                                alertDialog.setTitle(getString(R.string.editUser));
                                // Setting Dialog Message
                                alertDialog.setMessage(getString(R.string.editConfirmation));
                                // Setting Icon to Dialog
                                alertDialog.setIcon(R.mipmap.ic_launcher);
                                // Setting Positive "Yes" Button
                                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to invoke YES event
                                        progressDialog.show();
                                        progressDialog.setMessage(getString(R.string.pleasewait));
                                        new EditClient(EditCustomer.this, clientID, firstName, lastName, emailtext, userName + "&mobile=" + mobileText + "&code=" + countrycode).execute();
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
                            }

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    } else {

                        try {
                            firstName = URLEncoder.encode(firstName.trim(), "utf-8");
                            lastName = URLEncoder.encode(lastName.trim(), "utf-8");
                            emailtext = URLEncoder.encode(emailtext.trim(), "utf-8");
                            userName = URLEncoder.encode(userName.trim(), "utf-8");

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        if (InternetReceiver.isConnected()) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditCustomer.this);
                            // Setting Dialog Title
                            alertDialog.setTitle(getString(R.string.editUser));
                            // Setting Dialog Message
                            alertDialog.setMessage(getString(R.string.editConfirmation));
                            // Setting Icon to Dialog
                            alertDialog.setIcon(R.mipmap.ic_launcher);
                            // Setting Positive "Yes" Button
                            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to invoke YES event
                                    progressDialog.show();
                                    progressDialog.setMessage(getString(R.string.pleasewait));
                                    new EditClient(EditCustomer.this, clientID, firstName, lastName, emailtext, userName).execute();
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
                        }
                    }

                }else{
                    if (!mobileText.equals("")) {
                        try {
                            //userName = URLEncoder.encode(userName.trim(), "utf-8");
                            firstName = URLEncoder.encode(firstName.trim(), "utf-8");
                            lastName = URLEncoder.encode(lastName.trim(), "utf-8");
                            emailtext = URLEncoder.encode(emailtext.trim(), "utf-8");
                            phoneText = URLEncoder.encode(phoneText.trim(), "utf-8");
                            mobileText = URLEncoder.encode(mobileText.trim(), "utf-8");
                            userName = URLEncoder.encode(userName.trim(), "utf-8");
                            if (InternetReceiver.isConnected()) {
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditCustomer.this);
                                // Setting Dialog Title
                                alertDialog.setTitle(getString(R.string.editUser));
                                // Setting Dialog Message
                                alertDialog.setMessage(getString(R.string.editConfirmation));
                                // Setting Icon to Dialog
                                alertDialog.setIcon(R.mipmap.ic_launcher);
                                // Setting Positive "Yes" Button
                                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to invoke YES event
                                        progressDialog.show();
                                        progressDialog.setMessage(getString(R.string.pleasewait));
                                        new EditClient(EditCustomer.this, clientID, firstName, lastName, emailtext, userName + "&mobile=" + mobileText + "&code=" + countrycode+"&phone_number="+phoneText).execute();
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
                            }

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    } else {

                        try {
                            firstName = URLEncoder.encode(firstName.trim(), "utf-8");
                            lastName = URLEncoder.encode(lastName.trim(), "utf-8");
                            emailtext = URLEncoder.encode(emailtext.trim(), "utf-8");
                            userName = URLEncoder.encode(userName.trim(), "utf-8");
                            phoneText = URLEncoder.encode(phoneText.trim(), "utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        if (InternetReceiver.isConnected()) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditCustomer.this);
                            // Setting Dialog Title
                            alertDialog.setTitle(getString(R.string.editUser));
                            // Setting Dialog Message
                            alertDialog.setMessage(getString(R.string.editConfirmation));
                            // Setting Icon to Dialog
                            alertDialog.setIcon(R.mipmap.ic_launcher);
                            // Setting Positive "Yes" Button
                            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to invoke YES event
                                    progressDialog.show();
                                    progressDialog.setMessage(getString(R.string.pleasewait));
                                    new EditClient(EditCustomer.this, clientID, firstName, lastName, emailtext, userName+"&phone_number="+phoneText).execute();
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
                        }
                    }

                }
            }
        });
    }
    private void reqPermissionCamera() {
        new AskPermission.Builder(this).setPermissions(Manifest.permission.CALL_PHONE)
                .setCallback(this)
                .setErrorCallback(this)
                .request(PICKFILE_REQUEST_CODE);
    }


    public String getCountryZipCode(){
        String CountryID="";
        String CountryZipCode="";

        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        assert manager != null;
        CountryID= manager.getSimCountryIso().toUpperCase();
        String[] rl=this.getResources().getStringArray(R.array.CountryCodes);
        for (String aRl : rl) {
            String[] g = aRl.split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                CountryZipCode = g[0];
                break;
            }
        }

        Log.d("ZIPcode",CountryZipCode);
        countrycode=CountryZipCode;
        return CountryZipCode;
    }


    @Override
    public void onBackPressed() {
        if (!usernameGet.equalsIgnoreCase(username.getText().toString())||!firstnameGet.equalsIgnoreCase(firstname.getText().toString())||
                !lastnameGet.equalsIgnoreCase(lastname.getText().toString())||!emailGet.equalsIgnoreCase(email.getText().toString())||!phoneGet.equalsIgnoreCase(phoneEditText.getText().toString())
                ||!mobileGet.equalsIgnoreCase(mobile.getText().toString())){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditCustomer.this);

            // Setting Dialog Title
            alertDialog.setTitle(R.string.discard);

            // Setting Dialog Message
            //alertDialog.setMessage(getString(R.string.createConfirmation));

            // Setting Icon to Dialog
            alertDialog.setIcon(R.mipmap.ic_launcher);

            // Setting Positive "Yes" Button

            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to invoke YES event
                    //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(EditCustomer.this,ClientDetailActivity.class);
                    startActivity(intent);
                    finish();
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
        }
        else{
            Intent intent=new Intent(EditCustomer.this,ClientDetailActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onShowRationalDialog(final PermissionInterface permissionInterface, int requestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("We need permissions for this app.");
        builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permissionInterface.onDialogShown();
            }
        });
        builder.setNegativeButton(R.string.btn_cancel, null);
        builder.show();
    }

    @Override
    public void onShowSettings(final PermissionInterface permissionInterface, int requestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("We need permissions for this app. Open setting screen?");
        builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permissionInterface.onSettingsShown();
            }
        });
        builder.setNegativeButton(R.string.btn_cancel, null);
        builder.show();
    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        if (PICKFILE_REQUEST_CODE==1){

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditCustomer.this);
            // Setting Dialog Title
            alertDialog.setTitle(getString(R.string.makingCall));
            // Setting Dialog Message
            alertDialog.setMessage(getString(R.string.makingCallConfirmation));
            // Setting Icon to Dialog
            alertDialog.setIcon(R.mipmap.ic_launcher);
            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @SuppressLint("MissingPermission")
                public void onClick(DialogInterface dialog, int which) {

                    // Write your code here to invoke YES event
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:"+mobile1));
                    startActivity(intent);
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
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode) {
        Toasty.warning(EditCustomer.this,getString(R.string.needPermissionToMakeCall),Toast.LENGTH_SHORT).show();
        return;
    }
    private class FetchClientTickets extends AsyncTask<String, Void, String> {
        Context context;
        FetchClientTickets(Context context) {
            this.context = context;
        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().getTicketsByUser(clientID);
        }

        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            if (isCancelled()) return;

            if (result == null) return;
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject requester = jsonObject.getJSONObject("requester");
                String firstname1 = requester.getString("first_name");
                String lastName1 = requester.getString("last_name");
                String username1 = requester.getString("user_name");
                phone1 = requester.getString("phone_number");
                mobile1=requester.getString("mobile");
                String clientname1;

                if (firstname1 == null || firstname1.equals(""))
                    clientname1 = username1;
                else
                    clientname1 = firstname1 + " " + lastName1;

                username.setText(clientname1);
                usernameGet=clientname1;
                email.setText(requester.getString("email"));
                emailGet=requester.getString("email");
                if (firstname1.equals("null")||firstname1.equals("")){
                    firstname.setText("");
                }else {
                    firstname.setText(firstname1);
                    firstnameGet=firstname1;
                }
                if (lastName1.equals("null")||lastName1.equals("")){
                    lastname.setText("");

                }else {
                    lastname.setText(lastName1);
                    lastnameGet=lastName1;
                }
                if (phone1.equals("null")||phone1.equals("")||phone1.equals("Not available")){
                    phoneEditText.setText("");
                    imageViewCallPhone.setVisibility(View.GONE);
                }else {
                    phoneEditText.setText(phone1);
                    phoneGet=phone1;
                }
                if (mobile1.equals("null")||mobile1.equals("")||mobile1.equals("Not available")){
                    mobile.setText("");
                    imageViewCallMobile.setVisibility(View.GONE);

                }
                else {
                    mobile.setText(mobile1);
                    mobileGet=mobile1;
                }

                status= Integer.parseInt(requester.getString("is_delete"));
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
            String state=Prefs.getString("403",null);
            try {
                if (state.equals("403") && !state.equals("null")) {
                    Toasty.warning(EditCustomer.this, getString(R.string.permission), Toast.LENGTH_LONG).show();
                    Prefs.putString("403", "null");
                    return;
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                String message=jsonObject1.getString("message");
                if (message.equals("Updated successfully")){
                    i=0;
                    Toasty.success(EditCustomer.this,getString(R.string.editedcustomerdetails),Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(EditCustomer.this,MainActivity.class);
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

    private TextWatcher textWatcherUser = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if(usernameGet.equals(String.valueOf(s))) {
                //do something
                submit.setVisibility(View.GONE);
            }else{
                submit.setVisibility(View.VISIBLE);
                //do something
            }

        }
    };
    private TextWatcher textWatcherFirst = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if(firstnameGet.equals(String.valueOf(s))) {
                //do something
                submit.setVisibility(View.GONE);
            }else{
                submit.setVisibility(View.VISIBLE);
                //do something
            }

        }
    };
    private TextWatcher textWatcherLast = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if(lastnameGet.equals(String.valueOf(s))) {
                //do something
                submit.setVisibility(View.GONE);
            }else{
                submit.setVisibility(View.VISIBLE);
                //do something
            }

        }
    };
    private TextWatcher textWatcherEmail = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if(emailGet.equals(String.valueOf(s))) {
                //do something
                submit.setVisibility(View.GONE);
            }else{
                submit.setVisibility(View.VISIBLE);
                //do something
            }

        }
    };
    private TextWatcher textWatcherMobile = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if(mobileGet.equals(String.valueOf(s))) {
                //do something
                submit.setVisibility(View.GONE);
            }else{
                submit.setVisibility(View.VISIBLE);
                //do something
            }

        }
    };
    private TextWatcher textWatcherPhone = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if(phoneGet.equals(String.valueOf(s))) {
                //do something
                submit.setVisibility(View.GONE);
            }else{
                submit.setVisibility(View.VISIBLE);
                //do something
            }

        }
    };

}