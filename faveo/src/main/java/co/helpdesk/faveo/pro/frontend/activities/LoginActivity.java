package co.helpdesk.faveo.pro.frontend.activities;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.firebase.iid.FirebaseInstanceId;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.pixplicity.easyprefs.library.Prefs;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.helpdesk.faveo.pro.BuildConfig;
import co.helpdesk.faveo.pro.Constants;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.backend.api.v1.Authenticate;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.pro.model.MessageEvent;
import es.dmoral.toasty.Toasty;


public class LoginActivity extends AppCompatActivity {

    TextView textViewFieldError, textViewForgotPassword;
    EditText editTextUsername, editTextPassword, editTextAPIkey;
    int paddingTop, paddingBottom;
    ProgressDialog progressDialogVerifyURL;
    ProgressDialog progressDialogSignIn;
    ProgressDialog progressDialogBilling;
    List<String> urlSuggestions;

    @BindView(R.id.button_signin)
    Button buttonSignIn;
    @BindView(R.id.fab_verify_url)
    FloatingActionButton buttonVerifyURL;
    @BindView(R.id.input_password)
    AppCompatEditText passwordEdittext;
    @BindView(R.id.input_username)
    AppCompatEditText usernameEdittext;
    @BindView(R.id.editText_company_url)
    AutoCompleteTextView editTextCompanyURL;
    @BindView(R.id.viewFlipper)
    ViewFlipper viewflipper;
    @BindView(R.id.input_layout_username)
    TextInputLayout textInputLayoutUsername;
    @BindView(R.id.input_layout_password)
    TextInputLayout textInputLayoutPass;
    @BindView(R.id.url)
    TextView url;
    @BindView(R.id.flipcolor)
    View flipColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        //new Preference(getApplicationContext());

        //Preference.setInstance(getApplicationContext());
        //SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCE, MODE_PRIVATE);

        Boolean loginComplete = Prefs.getBoolean("LOGIN_COMPLETE", false);
        if (loginComplete) {
            Constants.URL = Prefs.getString("COMPANY_URL", "");
            Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }
        url.setVisibility(View.GONE);
        flipColor.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_200));
        buttonSignIn.setEnabled(false);
        usernameEdittext.addTextChangedListener(mTextWatcher);
        passwordEdittext.addTextChangedListener(mTextWatcher);

        //View init
        setUpViews();

        //ONly for Xiaomi devices
        String manufacturer = "xiaomi";
        if (manufacturer.equalsIgnoreCase(android.os.Build.MANUFACTURER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("Enable Permission")
                    .setMessage("To get notifications, we requesting you to enable the permission in auto start for FAVEO. ")
                    .setIcon(R.drawable.ic_warning_black_36dp)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent();
                            intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                            startActivity(intent);
                        }
                    });
            builder.create();
            builder.show();
            //this will open auto start screen where user can enable permission for your app

        }

        //Verifying the URL adress
        buttonVerifyURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String companyURL = editTextCompanyURL.getText().toString();
                if (companyURL.trim().length() == 0 || !Patterns.WEB_URL.matcher(companyURL).matches()) {
                    Toasty.warning(v.getContext(), getString(R.string.please_enter_valid_url), Toast.LENGTH_LONG).show();
                    return;
                }
                if (InternetReceiver.isConnected()) {
                    progressDialogVerifyURL.show();
                    new VerifyURL(LoginActivity.this, companyURL).execute();
                } else
                    Toasty.warning(v.getContext(), getString(R.string.oops_no_internet), Toast.LENGTH_LONG).show();
            }
        });

//        buttonSignIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setNormalStates();
//
//                String username = editTextUsername.getText().toString();
//                String password = editTextPassword.getText().toString();
//                if (username.trim().length() == 0 || password.trim().length() == 0) {
//                    if (username.trim().length() == 0)
//                        setUsernameErrorStates();
//                    else
//                        setPasswordErrorStates();
//                    return;
//                }
//                if (InternetReceiver.isConnected()) {
//                    progressDialogSignIn.show();
//                    new SignIn(LoginActivity.this, username, password).execute();
//                } else
//                    Toast.makeText(v.getContext(), "Oops! No internet", Toast.LENGTH_LONG).show();
//            }
//        });

        //Forgot password
        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    private class VerifyURL extends AsyncTask<String, Void, String> {
        Context context;
        String companyURL;
        String baseURL;

        VerifyURL(Context context, String companyURL) {
            this.context = context;
            this.companyURL = companyURL;
            baseURL = companyURL;
        }

        protected String doInBackground(String... urls) {
            if (!companyURL.endsWith("/"))
                companyURL = companyURL.concat("/");
            return new Helpdesk().getBaseURL(companyURL);
        }

        protected void onPostExecute(String result) {
            progressDialogVerifyURL.dismiss();
            if (result == null) {
                Toasty.warning(context, getString(R.string.invalid_url), Toast.LENGTH_LONG).show();
                return;
            }

            if (result.contains("success")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                    dynamicShortcut();
                }
                urlSuggestions.add(baseURL);
                Set<String> set = new HashSet<>(urlSuggestions);
                Prefs.putStringSet("URL_SUG", set);
                Prefs.putString("BASE_URL", baseURL);
                Prefs.putString("COMPANY_URL", companyURL + "api/v1/");
                Constants.URL = Prefs.getString("COMPANY_URL", "");
                if (BuildConfig.DEBUG) {
                    viewflipper.showNext();
                    url.setText(baseURL);
                    url.setVisibility(View.VISIBLE);
                    flipColor.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.faveo));
                } else {
                    progressDialogBilling.show();
                    new VerifyBilling(LoginActivity.this, baseURL).execute();
                }

            } else {
                Toasty.error(context, getString(R.string.error_verifying_url), Toast.LENGTH_LONG).show();
            }
        }
    }

    //N 7.0-> shortcuts
    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    void dynamicShortcut() {
        ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);

        ShortcutInfo webShortcut = new ShortcutInfo.Builder(this, "Web app")
                .setShortLabel("Web App")
                .setLongLabel("Open the web app")
                .setIcon(Icon.createWithResource(this, R.drawable.add))
                .setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(editTextCompanyURL.getText().toString())))
                .build();

        shortcutManager.setDynamicShortcuts(Collections.singletonList(webShortcut));
    }

    private class VerifyBilling extends AsyncTask<String, Void, String> {
        Context context;
        String baseURL;

        VerifyBilling(Context context, String baseURL) {
            this.context = context;
            this.baseURL = baseURL;
        }

        @Override
        protected String doInBackground(String... params) {

            return new Helpdesk().getCheckBillingURL(baseURL);
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialogBilling.dismiss();
            Log.d("Response BillingVerfy", result + "");
            if (result == null) {
                Toasty.error(LoginActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                return;
            }
            if (result.contains("success")) {
                viewflipper.showNext();
                url.setText(baseURL);
                url.setVisibility(View.VISIBLE);
                flipColor.setBackgroundColor(ContextCompat.getColor(context, R.color.faveo));
            } else if (result.contains("fails")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                        .setTitle(R.string.access_denied)
                        .setMessage(R.string.please_purchase_faveo)
                        .setIcon(R.drawable.ic_warning_black_36dp)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                builder.create();
                builder.show();

            } else
                Toasty.error(context, getString(R.string.error_checking_pro), Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.button_signin)
    public void signIn() {
        String username = usernameEdittext.getText().toString();
        String password = passwordEdittext.getText().toString();
        if (InternetReceiver.isConnected()) {
            //progressDialogSignIn.show();
            textInputLayoutUsername.setEnabled(false);
            textInputLayoutPass.setEnabled(false);
            buttonSignIn.setText(R.string.signing_in);
            new SignIn(LoginActivity.this, username, password).execute();
        } else
            Toasty.warning(this, getString(R.string.oops_no_internet), Toast.LENGTH_LONG).show();
    }

    private class SignIn extends AsyncTask<String, Void, String> {
        Context context;
        String username;
        String password;

        SignIn(Context context, String username, String password) {
            this.context = context;
            this.username = username;
            this.password = password;
        }

        protected String doInBackground(String... urls) {
            return new Authenticate().postAuthenticateUser(username, password);
        }

        protected void onPostExecute(String result) {

            Log.d("Response login", result + "");
            //progressDialogSignIn.dismiss();
            if (result == null) {
                textInputLayoutUsername.setEnabled(true);
                textInputLayoutPass.setEnabled(true);
                buttonSignIn.setText(getString(R.string.sign_in));
                Toasty.error(LoginActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                return;
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String error = jsonObject.getString("status_code");
                    if (error.equals("401")) {
                        textInputLayoutUsername.setEnabled(true);
                        textInputLayoutPass.setEnabled(true);
                        buttonSignIn.setText(getString(R.string.sign_in));
                        //Toast.makeText(LoginActivity.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
                        StyleableToast st = new StyleableToast(LoginActivity.this, getString(R.string.wrong_credentials), Toast.LENGTH_LONG);
                        st.setBackgroundColor(Color.parseColor("#3da6d7"));
                        st.setBoldText();
                        st.setTextColor(Color.WHITE);
                        st.setCornerRadius(7);
                        st.show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            try {
                JSONObject jsonObject = new JSONObject(result);
                String token = jsonObject.getString("token");
                JSONObject jsonObject1 = jsonObject.getJSONObject("user_id");
                String userID = jsonObject1.getString("id");
                String profile_pic = jsonObject1.getString("profile_pic");
                String role = jsonObject1.getString("role");
                String firstName = jsonObject1.getString("first_name");
                String lastName = jsonObject1.getString("last_name");
                String userName = jsonObject1.getString("user_name");
                String clientname;
                if (firstName == null || firstName.equals(""))
                    clientname = userName;
                else
                    clientname = firstName + " " + lastName;
                SharedPreferences.Editor authenticationEditor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                authenticationEditor.putString("ID", userID);
                authenticationEditor.putString("TOKEN", token);
                authenticationEditor.putString("USERNAME", username);
                authenticationEditor.putString("PASSWORD", password);
                authenticationEditor.putBoolean("LOGIN_COMPLETE", true);
                authenticationEditor.putString("PROFILE_PIC", profile_pic);
                authenticationEditor.putString("ROLE", role);
                authenticationEditor.putString("PROFILE_NAME", clientname);
                authenticationEditor.putString("AGENT_SIGN", jsonObject1.getString("agent_sign"));
                authenticationEditor.apply();

                Prefs.putString("FCMtoken", FirebaseInstanceId.getInstance().getToken());

                new SendingFCM(LoginActivity.this, FirebaseInstanceId.getInstance().getToken()).execute();

                Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } catch (JSONException e) {
                Toasty.error(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }
    }

//    private void setNormalStates() {
//        textViewFieldError.setVisibility(View.INVISIBLE);
//        editTextUsername.setBackgroundResource(R.drawable.edittext_modified_states);
//        editTextPassword.setBackgroundResource(R.drawable.edittext_modified_states);
//        editTextAPIkey.setBackgroundResource(R.drawable.edittext_modified_states);
//        editTextAPIkey.setPadding(0, paddingTop, 0, paddingBottom);
//        editTextUsername.setPadding(0, paddingTop, 0, paddingBottom);
//        editTextPassword.setPadding(0, paddingTop, 0, paddingBottom);
//    }
//
//    private void setUsernameErrorStates() {
//        textViewFieldError.setText("Please insert username");
//        textViewFieldError.setVisibility(View.VISIBLE);
//        editTextUsername.setBackgroundResource(R.drawable.edittext_error_state);
//        editTextUsername.setPadding(0, paddingTop, 0, paddingBottom);
//    }
//
//    private void setPasswordErrorStates() {
//        textViewFieldError.setText("Please insert password");
//        textViewFieldError.setVisibility(View.VISIBLE);
//        editTextPassword.setBackgroundResource(R.drawable.edittext_error_state);
//        editTextPassword.setPadding(0, paddingTop, 0, paddingBottom);
//    }

    private void setUpViews() {

        progressDialogVerifyURL = new ProgressDialog(this);
        progressDialogVerifyURL.setMessage(getString(R.string.verifying_url));
        progressDialogVerifyURL.setCancelable(false);

        progressDialogSignIn = new ProgressDialog(this);
        progressDialogSignIn.setMessage(getString(R.string.signing_in));
        progressDialogSignIn.setCancelable(false);

        progressDialogBilling = new ProgressDialog(this);
        progressDialogBilling.setMessage(getString(R.string.access_checking));
        progressDialogBilling.setCancelable(false);

//        InputFilter filter = new InputFilter() {
//            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
//                String filtered = "";
//                for (int i = start; i < end; i++) {
//                    char character = source.charAt(i);
//                    if (!Character.isWhitespace(character)) {
//                        filtered += character;
//                    }
//                }
//
//                return filtered;
//            }
//
//        };

        // editTextCompanyURL = (EditText) findViewById(R.id.editText_company_url);
        // editTextCompanyURL.setFilters(new InputFilter[]{filter});

        if (editTextCompanyURL != null) {
            editTextCompanyURL.setText("");
            editTextCompanyURL.append("http://");
        }
        Set<String> set = Prefs.getStringSet("URL_SUG", new HashSet<String>());
        urlSuggestions = new ArrayList<>(set);

        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (this, android.R.layout.simple_dropdown_item_1line, urlSuggestions);
        editTextCompanyURL.setThreshold(1);
        editTextCompanyURL.setAdapter(adapter);


        //viewflipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        // buttonVerifyURL = (FloatingActionButton) findViewById(R.id.fab_verify_url);
        textViewFieldError = (TextView) findViewById(R.id.textView_field_error);
        textViewForgotPassword = (TextView) findViewById(R.id.forgot_password);
        editTextUsername = (EditText) findViewById(R.id.editText_username);
        editTextPassword = (EditText) findViewById(R.id.editText_password);
        editTextAPIkey = (EditText) findViewById(R.id.editText_company_api_key);
        //buttonSignIn = (Button) findViewById(R.id.button_1);
        paddingTop = editTextUsername.getPaddingTop();
        paddingBottom = editTextUsername.getPaddingBottom();

    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        //FaveoApplication.getInstance().setInternetListener(this);
        checkConnection();
    }

    private void checkConnection() {
        boolean isConnected = InternetReceiver.isConnected();
        showSnackIfNoInternet(isConnected);
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

//    /**
//     * Callback will be triggered when there is change in
//     * network connection
//     */
//    @Override
//    public void onNetworkConnectionChanged(boolean isConnected) {
//        showSnack(isConnected);
//    }

    private class SendingFCM extends AsyncTask<String, Void, String> {
        Context context;
        String token;

        SendingFCM(Context context, String token) {
            this.context = context;
            this.token = token;
        }


        @Override
        protected String doInBackground(String... params) {

            return new Helpdesk().postFCMToken(token, Prefs.getString("ID", null));
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("Response FCM", result + "");
            if (result == null) {
                return;
            }

            if (result.contains("success")) {
                Log.d("RESPONSE_FCM_Success", "" + result);
            } else if (result.contains("fails")) {
                Log.d("RESPONSE_FCM_FAILS", "" + result);
            }
        }
    }

    void checkFieldsForEmptyValues() {

        String username = usernameEdittext.getText().toString();
        String password = passwordEdittext.getText().toString();
        if (username.trim().length() == 0 || password.trim().length() == 0) {
            buttonSignIn.setEnabled(false);
        } else {
            buttonSignIn.setEnabled(true);
        }

//        if (username.trim().length() == 0 || password.trim().length() == 0) {
//            buttonSignIn.setEnabled(false);
//        } else if (username.trim().length() <= 2) {
//            textInputLayoutUsername.setError("Username must be at least 3 characters");
//        } else {
//            textInputLayoutUsername.setError(null);
//            buttonSignIn.setEnabled(true);
//        }
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // check Fields For Empty Values
            checkFieldsForEmptyValues();
        }
    };

    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {

        showSnack(event.message);
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


}
