package co.helpdesk.faveo.pro.frontend.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import co.helpdesk.faveo.pro.CircleTransform;
import co.helpdesk.faveo.pro.Constants;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.backend.api.v1.Authenticate;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.pro.model.Data;
import es.dmoral.toasty.Toasty;

public class TicketSaveActivity extends AppCompatActivity {
    Spinner  spinnerType,spinnerSource,
            spinnerPriority, spinnerHelpTopics;
    ProgressDialog progressDialog;
    AsyncTask<String, Void, String> task;
    EditText edittextsubject;
    Button buttonsave;
    ImageView imageView;
    Spinner autoCompleteTextViewstaff;
    ImageView refresh;
    String subject="",type="--",source="--",priority="--",helptopic="--",staff="--";
    ArrayList<Data> helptopicItems, priorityItems, typeItems, sourceItems, staffItems;
    ArrayAdapter<Data> spinnerPriArrayAdapter, spinnerHelpArrayAdapter, spinnerTypeArrayAdapter, spinnerSourceArrayAdapter, staffArrayAdapter;
    int id,id1;
    Animation rotation;
    String option;
    int responseCodeForShow;
    static String token;
    private URL url;
    InputStream errorstream;
    private StringBuilder sb = null;
    private InputStream is = null;
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
    private String ticketID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_save);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        Window window = TicketSaveActivity.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(TicketSaveActivity.this,R.color.mainActivityTopBar));
        StrictMode.setThreadPolicy(policy);
        refresh= (ImageView) findViewById(R.id.imageViewRefresh);
        rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        if (InternetReceiver.isConnected()) {
//            progressDialog=new ProgressDialog(this);
//            progressDialog.setMessage(getString(R.string.pleasewait));
//            progressDialog.show();
            refresh.startAnimation(rotation);
            Log.d("FromTicketSave","true");
            final Intent intent = getIntent();
            ticketID=intent.getStringExtra("ticket_id");
            Prefs.putString("TICKETid",ticketID);
            Prefs.putString("ticketId",ticketID);
            task = new FetchTicketDetail(Prefs.getString("TICKETid",null));
            task.execute();
        }

//        if (InternetReceiver.isConnected()){
//            new FetchDependency().execute();
//        }
        option=Prefs.getString("cameFromNotification", null);
        switch (option) {
            case "true":
                Prefs.putString("cameFromNotification","true");
                break;
            case "false":
                Prefs.putString("cameFromNotification","false");
                break;
            case "none":
                Prefs.putString("cameFromNotification","none");
                break;
            case "client":
                Prefs.putString("cameFromNotification","client");
                break;
            default:
                Prefs.putString("cameFromNotification","");
                break;
        }
        setUpViews();


        spinnerHelpTopics.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edittextsubject.getWindowToken(), 0);
                return false;
            }
        });

        spinnerPriority.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edittextsubject.getWindowToken(), 0);
                return false;
            }
        });
        spinnerSource.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edittextsubject.getWindowToken(), 0);
                return false;
            }
        });

        autoCompleteTextViewstaff.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edittextsubject.getWindowToken(), 0);
                return false;
            }
        });

        spinnerType.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edittextsubject.getWindowToken(), 0);
                return false;
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarsave);
        TextView textView = (TextView) toolbar.findViewById(R.id.titlesave);
        imageView= (ImageView) toolbar.findViewById(R.id.imageViewBackTicketSave);
        textView.setText(getString(R.string.ticketProperties));

        spinnerHelpTopics.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (helptopic.equals(spinnerHelpTopics.getSelectedItem().toString())){
                    buttonsave.setVisibility(View.GONE);
                }
                else{
                    buttonsave.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (priority.equals(spinnerPriority.getSelectedItem().toString())){
                    buttonsave.setVisibility(View.GONE);
                }
                else{
                    buttonsave.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerSource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (source.equals(spinnerSource.getSelectedItem().toString())){
                    buttonsave.setVisibility(View.GONE);
                }
                else{
                    buttonsave.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        autoCompleteTextViewstaff.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                buttonsave.setVisibility(View.VISIBLE);
//                if (staff.equals(autoCompleteTextViewstaff.getSelectedItem().toString())){
//                    buttonsave.setVisibility(View.GONE);
//                }
//                else{
//                    buttonsave.setVisibility(View.VISIBLE);
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (type.equals(spinnerType.getSelectedItem().toString())){
                    buttonsave.setVisibility(View.GONE);
                }
                else{
                    buttonsave.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        edittextsubject.addTextChangedListener(textWatcher);


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(TicketSaveActivity.this);

                // Setting Dialog Title
                alertDialog.setTitle(getString(R.string.refreshingPage));

                // Setting Dialog Message
                alertDialog.setMessage(getString(R.string.refreshPage));

                // Setting Icon to Dialog
                alertDialog.setIcon(R.mipmap.ic_launcher);

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke YES event
                        //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                        if (InternetReceiver.isConnected()){
                            refresh.startAnimation(rotation);

                            new FetchDependency().execute();
                            setUpViews();
                            task = new FetchTicketDetail(Prefs.getString("TICKETid",null));
                            task.execute();
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
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    if (!subject.equalsIgnoreCase(edittextsubject.getText().toString())||!type.equalsIgnoreCase(spinnerType.getSelectedItem().toString())||
                        !source.equalsIgnoreCase(spinnerSource.getSelectedItem().toString())||
                        !priority.equalsIgnoreCase(spinnerPriority.getSelectedItem().toString())
                        ||!helptopic.equalsIgnoreCase(spinnerHelpTopics.getSelectedItem().toString())||
                        !staff.equalsIgnoreCase(autoCompleteTextViewstaff.getSelectedItem().toString())){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(TicketSaveActivity.this);

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

//                            Intent intent1=new Intent(TicketSaveActivity.this,TicketDetailActivity.class);
//                            intent1.putExtra("ticket_id", ticketID);
//                            startActivity(intent1);
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

//                        Intent intent1=new Intent(TicketSaveActivity.this,TicketDetailActivity.class);
//                        intent1.putExtra("ticket_id", ticketID);
//                        startActivity(intent1);
                        finish();
                }
            }
        });
        setSupportActionBar(toolbar);

        buttonsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SendPostRequest().execute();
                boolean allCorrect = true;
                final String subject = edittextsubject.getText().toString();
                final Data helpTopic = (Data) spinnerHelpTopics.getSelectedItem();
                final Data source = (Data) spinnerSource.getSelectedItem();
                final Data priority = (Data) spinnerPriority.getSelectedItem();
                final Data type = (Data) spinnerType.getSelectedItem();
                final Data staffId= (Data) autoCompleteTextViewstaff.getSelectedItem();

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
                        Log.d("priorityId",priority.ID+"");

                        if (type.ID==0){
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(TicketSaveActivity.this);

                            // Setting Dialog Title
                            alertDialog.setTitle(getString(R.string.editingticket));

                            // Setting Dialog Message
                            alertDialog.setMessage(getString(R.string.editingConfirmation));

                            // Setting Icon to Dialog
                            alertDialog.setIcon(R.mipmap.ic_launcher);

                            // Setting Positive "Yes" Button
                            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to invoke YES event
                                    //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                                    if (InternetReceiver.isConnected()){
                                        progressDialog=new ProgressDialog(TicketSaveActivity.this);
                                        progressDialog.setMessage(getString(R.string.updating_ticket));
                                        progressDialog.show();
                                        try {
                                            new SaveTicketWithoutType(
                                                    Integer.parseInt(Prefs.getString("TICKETid",null)),
                                                    URLEncoder.encode(subject.trim(), "utf-8"),
                                                    helpTopic.ID,
                                                    source.ID,
                                                    priority.ID,staffId.ID)
                                                    .execute();
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }

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
                        }
                        else{
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(TicketSaveActivity.this);

                            // Setting Dialog Title
                            alertDialog.setTitle(getString(R.string.editingticket));

                            // Setting Dialog Message
                            alertDialog.setMessage(getString(R.string.editingConfirmation));

                            // Setting Icon to Dialog
                            alertDialog.setIcon(R.mipmap.ic_launcher);

                            // Setting Positive "Yes" Button
                            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to invoke YES event
                                    //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                                    if (InternetReceiver.isConnected()){
                                        progressDialog=new ProgressDialog(TicketSaveActivity.this);
                                        progressDialog.setMessage(getString(R.string.updating_ticket));
                                        progressDialog.show();
                                        try {
                                            new SaveTicket(
                                                    Integer.parseInt(Prefs.getString("TICKETid",null)),
                                                    URLEncoder.encode(subject.trim(), "utf-8"),
                                                    helpTopic.ID,
                                                    source.ID,
                                                    priority.ID,type.ID,staffId.ID)
                                                    .execute();
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }

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
                        }


                    }
                }
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
//        if (InternetReceiver.isConnected()){
//            new FetchTicketDetail(Prefs.getString("TICKETid",null)).execute();
//        }
    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {
            try {

                URL url = new URL(Constants.URL + "authenticate"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("username", Prefs.getString("USERNAME", null));
                postDataParams.put("password", Prefs.getString("PASSWORD", null));
                Log.e("params",postDataParams.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                //MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    Log.d("ifresponseCode",""+responseCode);
                    BufferedReader in=new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    in.close();
                    return sb.toString();
                }
                else {
                    if (responseCode==400){
                        Log.d("cameInThisBlock","true");
                        responseCodeForShow=400;
                    }
                    else if (responseCode==405){
                        responseCodeForShow=405;
                    }
                    else if (responseCode==302){
                        responseCodeForShow=302;
                    }
                    Log.d("elseresponseCode",""+responseCode);
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }

        }


        @Override
        protected void onPostExecute(String result) {
            Log.d("resultFromNewCall",result);

                if (responseCodeForShow == 400) {
                    final Toast toast = Toasty.info(TicketSaveActivity.this, getString(R.string.urlchange), Toast.LENGTH_SHORT);
                    toast.show();
                    new CountDownTimer(10000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            toast.show();
                        }

                        public void onFinish() {
                            toast.cancel();
                        }
                    }.start();
                    Prefs.clear();
                    Intent intent = new Intent(TicketSaveActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }


                if (responseCodeForShow == 405) {
                    final Toast toast = Toasty.info(TicketSaveActivity.this, getString(R.string.urlchange), Toast.LENGTH_SHORT);
                    toast.show();
                    new CountDownTimer(10000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            toast.show();
                        }

                        public void onFinish() {
                            toast.cancel();
                        }
                    }.start();
                    Prefs.clear();
                    Intent intent = new Intent(TicketSaveActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }


                if (responseCodeForShow == 302) {
                    final Toast toast = Toasty.info(TicketSaveActivity.this, getString(R.string.urlchange), Toast.LENGTH_SHORT);
                    toast.show();
                    new CountDownTimer(10000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            toast.show();
                        }

                        public void onFinish() {
                            toast.cancel();
                        }
                    }.start();
                    Prefs.clear();
                    Intent intent = new Intent(TicketSaveActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }


            try {
                JSONObject jsonObject=new JSONObject(result);
                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                token = jsonObject1.getString("token");
                Prefs.putString("TOKEN", token);
                JSONObject jsonObject2=jsonObject1.getJSONObject("user");
                String role=jsonObject2.getString("role");
                String profile_pic = jsonObject2.getString("profile_pic");
                Prefs.putString("profilePicture",profile_pic);
                if (role.equals("user")){
                    final Toast toast = Toasty.info(TicketSaveActivity.this, getString(R.string.roleChanged),Toast.LENGTH_SHORT);
                    toast.show();
                    new CountDownTimer(10000, 1000)
                    {
                        public void onTick(long millisUntilFinished) {toast.show();}
                        public void onFinish() {toast.cancel();}
                    }.start();
                    Prefs.clear();
                    //Prefs.putString("role",role);
                    Intent intent = new Intent(TicketSaveActivity.this, LoginActivity.class);
                    //Toasty.info(getActivity(), getString(R.string.roleChanged), Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }
                String firstName = jsonObject2.getString("first_name");
                String lastName = jsonObject2.getString("last_name");
                String userName = jsonObject2.getString("user_name");
                String email=jsonObject2.getString("email");
                String clientname;
                if (firstName == null || firstName.equals(""))
                    clientname = userName;
                else
                    clientname = firstName + " " + lastName;
                Prefs.putString("clientNameForFeedback",clientname);
                Prefs.putString("emailForFeedback",email);
                Prefs.putString("PROFILE_NAME",clientname);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

//    String hTTPResponsePost(String stringURL, String parameters) {
//
//        try {
//            url = new URL(stringURL);
//            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestProperty("Offer-type", "application/json");
//            connection.setRequestProperty("Accept", "application/json");
//            connection.setRequestProperty("token",token);
//            connection.setDoInput(true);
//            connection.setUseCaches(false);
//            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=xxxxxxxxxx");
//            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
//            connection.setRequestProperty( "application-type", "REST" );
//            connection.setRequestMethod("POST");
//            connection.connect();
//            OutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
//            BufferedWriter writer = new BufferedWriter(
//                    new OutputStreamWriter(outputStream, "UTF-8"));
//            if (parameters != null)
//                writer.write(parameters);
//
//            writer.flush();
//            writer.close();
//            outputStream.close();
//
//            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
//                String ret = null;
//                switch (connection.getResponseCode()) {
//                    case HttpURLConnection.HTTP_UNAUTHORIZED:
//                        Log.e("Response code: ", "401-UNAUTHORIZED!");
////                        Prefs.putString("unauthorized","true");
////                        ret="Unauthorized Access";
////                        break;
//                        //ret="HTTP_UNAUTHORIZED";
//                        if (refreshToken() == null)
//                            return null;
//                        new Helpdesk();
//                        new Authenticate();
//                        return "tokenRefreshed";
//                    case HttpURLConnection.HTTP_NOT_FOUND:
//                        Log.e("Response code: ", "NotFound-404!");
//                        //ret = "notFound";
//                        break;
//                    case HttpURLConnection.HTTP_BAD_METHOD:
//                        Log.e("Response code: ", "405 MethodNotAllowed!");
//                        ret="MethodNotAllowed";
//                        Log.d("MethodNotAllowed","CAMEHERE");
//                        Prefs.putString("405","True");
//                    case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
//                        Log.e("Response code: ", "Timeout!");
//                        // ret = "timeout";
//                        break;
//                    case HttpURLConnection.HTTP_UNAVAILABLE:
//                        Log.e("Response code: ", "Unavailable!");
//                        // ret = "unavailable";
//                        break;// retry, server is unstable
//                    case HttpURLConnection.HTTP_BAD_REQUEST:
//                        Log.e("Response code: ", "BadRequest!");
//                        ret="badRequest";
//                        Prefs.putString("400",ret);
////                        if (refreshToken() == null)
////                            return null;
////                        new Helpdesk();
////                        new Authenticate();
////                        ret = "tokenRefreshed";
//                        break;
//                    case HttpURLConnection.HTTP_FORBIDDEN:
//                        Log.e("Response code","Forbidden");
//                        ret="Forbidden";
//                        Prefs.putString("403","403");
//
//                        break;
//                    case HttpURLConnection.HTTP_CONFLICT:
//                        Log.e("Response code","Conflict");
//                        ret="Conflict";
//                        Prefs.putString("409","409");
//                        break;
//                    default:
//
//                        break; // abort
//                }
//
//                return ret;
//            }
//            Prefs.putString("405","False");
//            Prefs.putString("unauthorized","false");
//            Prefs.putString("400","false");
//            Prefs.putString("403","false");
//            Prefs.putString("409","true");
//            is = connection.getInputStream();
//            Log.e("Response Code", connection.getResponseCode() + "");
//        } catch (IOException e) {
//            if (e.getMessage().contains("No authentication challenges found")) {
//                if (refreshToken() == null)
//                    return null;
//                new Helpdesk();
//                new Authenticate();
//                return "tokenRefreshed";
//            }
//            Log.e("error in faveo", e.getMessage());
//            e.printStackTrace();
//        }
//
//        try {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
//            sb = new StringBuilder();
//            sb.append(reader.readLine()).append("\n");
//            String line;
//            while ((line = reader.readLine()) != null) {
//                sb.append(line).append("\n");
//            }
//            is.close();
//        } catch (Exception e) {
//            Log.e("log_tag", "Error converting result " + e.toString());
//        }
//        if (sb == null)
//            return null;
//        String input = sb.toString();
//        if (input.contains("token_expired") || input.contains("token_invalid")||input.contains("tokenRefreshed")) {
//            if (refreshToken() == null)
//                return null;
//            new Helpdesk();
//            new Authenticate();
//            return "tokenRefreshed";
//        }
//        return sb.toString();
//    }
//    public String refreshToken() {
//        String result = new Authenticate().postAuthenticateUser(Prefs.getString("USERNAME", null), Prefs.getString("PASSWORD", null));
//        if (result == null) {
//            return null;
//        }
//        try {
//            JSONObject jsonObject = new JSONObject(result);
//            Log.d("tokenExpired","called");
//            JSONObject jsonObject1=jsonObject.getJSONObject("data");
//            String token = jsonObject1.getString("token");
//            JSONObject jsonObject2=jsonObject1.getJSONObject("user");
////            if (role.equals("user")){
////                //Prefs.clear();
////                //Prefs.putString("role",role);
////                Intent intent=new Intent(FaveoApplication.getContext(),LoginActivity.class);
////                Toast.makeText(FaveoApplication.getContext(),FaveoApplication.getContext().getString(R.string.permission), Toast.LENGTH_SHORT).show();
////                FaveoApplication.getContext().startActivity(intent);
////
////
////            }
//
//            String profilePic = jsonObject2.getString("profile_pic");
//            //Prefs.putString("role",role);
//            Log.d("result", result);
//            Log.d("profilePicture", profilePic);
//            //String token = jsonObject.getString("token");
//            Prefs.putString("TOKEN", token);
//            Prefs.putString("profilePicture", profilePic);
//            Authenticate.token = token;
//            Helpdesk.token = token;
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Log.d("cameInException","true");
//            Prefs.clear();
//            Prefs.putString("NoToken","True");
//            return null;
//        }
//        return "success";
//    }
    private class FetchTicketDetail1 extends AsyncTask<String, Void, String> {
        String ticketID;
        String agentName;
        String title;
        FetchTicketDetail1(String ticketID) {

            this.ticketID = ticketID;
        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().getTicketDetail(ticketID);
        }

        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            if (isCancelled()) return;
//            if (progressDialog.isShowing())
//                progressDialog.dismiss();

            if (result == null) {
                //Toasty.error(TicketDetailActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);
                Prefs.putString("TicketRelated",jsonObject.toString());
                Toasty.success(TicketSaveActivity.this, getString(R.string.update_success), Toast.LENGTH_LONG).show();
                Intent intent=new Intent(TicketSaveActivity.this, MainActivity.class);
                startActivity(intent);
                } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private class SaveTicket extends AsyncTask<String, Void, String> {
        private int ticketNumber;
        private  String subject;
        private int helpTopic;
        private int ticketSource;
        private int ticketPriority;
        private int ticketType;

        int staff;

        SaveTicket(int ticketNumber, String subject, int helpTopic, int ticketSource, int ticketPriority,int ticketType,int staff) {
            this.ticketNumber = ticketNumber;
            this.subject = subject;
            this.helpTopic = helpTopic;
            this.ticketSource = ticketSource;
            this.ticketPriority = ticketPriority;
            this.ticketType = ticketType;
            this.staff=staff;
        }

        protected String doInBackground(String... urls) {
            if (subject.equals("Not available"))
                subject = "";
            return new Helpdesk().postEditTicket(ticketNumber, subject,
                    helpTopic, ticketSource, ticketPriority,ticketType,staff);
        }

        protected void onPostExecute(String result) {
//            if (progressDialog.isShowing())
               progressDialog.dismiss();
            Prefs.putString("ticketThread","");
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
                new FetchTicketDetail1(Prefs.getString("TICKETid",null)).execute();

            } else
                Toasty.error(TicketSaveActivity.this, getString(R.string.failed_to_update_ticket), Toast.LENGTH_LONG).show();
        }
    }

    private class SaveTicketWithoutType extends AsyncTask<String, Void, String> {
        int ticketNumber;
        String subject;
        //int slaPlan;
        int helpTopic;
        int ticketSource;
        int ticketPriority;
        int ticketStatus;
        String token;
        int staff;

        SaveTicketWithoutType(int ticketNumber, String subject, int helpTopic, int ticketSource, int ticketPriority,int staff) {
            this.ticketNumber = ticketNumber;
            this.subject = subject;
            // this.slaPlan = slaPlan;
            this.helpTopic = helpTopic;
            this.ticketSource = ticketSource;
            this.ticketPriority = ticketPriority;
            // this.ticketStatus = ticketStatus;
            this.token=token;

            this.staff=staff;
        }

        protected String doInBackground(String... urls) {
            if (subject.equals("Not available"))
                subject = "";
            return new Helpdesk().postEditTicketWithoutType(ticketNumber, subject,
                    helpTopic, ticketSource, ticketPriority,staff);
        }

        protected void onPostExecute(String result) {
//            if (progressDialog.isShowing())
            progressDialog.dismiss();
            Prefs.putString("ticketThread","");
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
                new FetchTicketDetail1(Prefs.getString("TICKETid",null)).execute();

            } else
                Toasty.error(TicketSaveActivity.this, getString(R.string.failed_to_update_ticket), Toast.LENGTH_LONG).show();
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
            refresh.clearAnimation();
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
                if (title.startsWith("=?UTF-8?Q?") && title.endsWith("?=")) {
                    String first = title.replace("=?UTF-8?Q?", "");
                    String second = first.replace("_", " ");
                    String second1=second.replace("=C3=BA","");
                    String third = second1.replace("=C2=A0", "");
                    String fourth = third.replace("?=", "");
                    String fifth = fourth.replace("=E2=80=99", "'");
                    String sixth=fifth.replace("=3F","?");
                    edittextsubject.setText(sixth);
                    subject=sixth;
                } else {
                    edittextsubject.setText(title);
                    subject=title;
                }
                //edittextsubject.setText(title);
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
                                    staff=autoCompleteTextViewstaff.getSelectedItem().toString();
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

                try {
                    if (jsonObject2.getString("priority_name") != null) {

                        spinnerPriority.setSelection(getIndex(spinnerPriority, jsonObject2.getString("priority_name")));
                        priority=spinnerPriority.getSelectedItem().toString();

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
                                type=spinnerType.getSelectedItem().toString();
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
                        spinnerHelpTopics.setSelection(getIndex(spinnerHelpTopics,jsonObject2.getString("helptopic_name")));
                            helptopic=spinnerHelpTopics.getSelectedItem().toString();

                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                } catch (Exception e) {

                    e.printStackTrace();
                }



                try {
                    if (jsonObject2.getString("source_name") != null)


                        spinnerSource.setSelection(getIndex(spinnerSource, jsonObject2.getString("source_name")));
                    source=spinnerSource.getSelectedItem().toString();

                } catch (JSONException | NumberFormatException e) {
                    e.printStackTrace();
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }


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
                Collections.sort(staffItems, new Comparator<Data>() {
                    @Override
                    public int compare(Data lhs, Data rhs) {
                        return lhs.getName().compareTo(rhs.getName());
                    }
                });
            }
            helptopicItems = new ArrayList<>();
            helptopicItems.add(new Data(0, "--"));
            jsonObject = new JSONObject(json);
            JSONArray jsonArrayHelpTopics = jsonObject.getJSONArray("helptopics");
            for (int i = 0; i < jsonArrayHelpTopics.length(); i++) {
                Data data1 = new Data(Integer.parseInt(jsonArrayHelpTopics.getJSONObject(i).getString("id")), jsonArrayHelpTopics.getJSONObject(i).getString("topic"));
                helptopicItems.add(data1);
                Collections.sort(helptopicItems, new Comparator<Data>() {
                    @Override
                    public int compare(Data lhs, Data rhs) {
                        return lhs.getName().compareTo(rhs.getName());
                    }
                });
            }

            JSONArray jsonArrayPriorities = jsonObject.getJSONArray("priorities");
            priorityItems = new ArrayList<>();
            priorityItems.add(new Data(0, "--"));
            for (int i = 0; i < jsonArrayPriorities.length(); i++) {
                Data data2 = new Data(Integer.parseInt(jsonArrayPriorities.getJSONObject(i).getString("priority_id")), jsonArrayPriorities.getJSONObject(i).getString("priority"));
                priorityItems.add(data2);
                Collections.sort(priorityItems, new Comparator<Data>() {
                    @Override
                    public int compare(Data lhs, Data rhs) {
                        return lhs.getName().compareTo(rhs.getName());
                    }
                });
            }

            JSONArray jsonArrayType = jsonObject.getJSONArray("type");
            typeItems = new ArrayList<>();
            typeItems.add(new Data(0, "--"));
            for (int i = 0; i < jsonArrayType.length(); i++) {
                Data data3 = new Data(Integer.parseInt(jsonArrayType.getJSONObject(i).getString("id")), jsonArrayType.getJSONObject(i).getString("name"));
                typeItems.add(data3);
                Collections.sort(typeItems, new Comparator<Data>() {
                    @Override
                    public int compare(Data lhs, Data rhs) {
                        return lhs.getName().compareTo(rhs.getName());
                    }
                });

            }

            JSONArray jsonArraySources = jsonObject.getJSONArray("sources");
            sourceItems = new ArrayList<>();
            sourceItems.add(new Data(0, "--"));
            for (int i = 0; i < jsonArraySources.length(); i++) {
                Data data4 = new Data(Integer.parseInt(jsonArraySources.getJSONObject(i).getString("id")), jsonArraySources.getJSONObject(i).getString("name"));
                sourceItems.add(data4);
                Collections.sort(sourceItems, new Comparator<Data>() {
                    @Override
                    public int compare(Data lhs, Data rhs) {
                        return lhs.getName().compareTo(rhs.getName());
                    }
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        edittextsubject= (EditText) findViewById(R.id.editTextsubject);
        buttonsave= (Button) findViewById(R.id.buttonsave);
        buttonsave.setVisibility(View.GONE);
        spinnerPriority= (Spinner) findViewById(R.id.spinner_priority);
        spinnerPriArrayAdapter = new ArrayAdapter<>(TicketSaveActivity.this, android.R.layout.simple_spinner_dropdown_item, priorityItems);
        //selected item will look like a spinner set from XML
        spinnerPriArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(spinnerPriArrayAdapter);

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

        if (!subject.equalsIgnoreCase(edittextsubject.getText().toString())||!type.equalsIgnoreCase(spinnerType.getSelectedItem().toString())||
                !source.equalsIgnoreCase(spinnerSource.getSelectedItem().toString())||
                !priority.equalsIgnoreCase(spinnerPriority.getSelectedItem().toString())
                ||!helptopic.equalsIgnoreCase(spinnerHelpTopics.getSelectedItem().toString())||
                !staff.equalsIgnoreCase(autoCompleteTextViewstaff.getSelectedItem().toString())){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(TicketSaveActivity.this);

            // Setting Dialog Title
            alertDialog.setTitle("Discard changes?");

            // Setting Dialog Message
            //alertDialog.setMessage(getString(R.string.createConfirmation));

            // Setting Icon to Dialog
            alertDialog.setIcon(R.mipmap.ic_launcher);

            // Setting Positive "Yes" Button

            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to invoke YES event
                    //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                    //finishAffinity();
//                    Intent intent1=new Intent(TicketSaveActivity.this,TicketDetailActivity.class);
//                    intent1.putExtra("ticket_id", ticketID);
//                    startActivity(intent1);
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
            //finishAffinity();
//            Intent intent1=new Intent(TicketSaveActivity.this,TicketDetailActivity.class);
//            intent1.putExtra("ticket_id", ticketID);
//            startActivity(intent1);
            finish();
        }

    }
    private class FetchDependency extends AsyncTask<String, Void, String> {
        String unauthorized;

        protected String doInBackground(String... urls) {

            return new Helpdesk().getDependency();

        }

        protected void onPostExecute(String result) {
            refresh.clearAnimation();
            Log.d("Depen Response : ", result + "");
            Log.d("cameHere","True");

            if (result==null) {
                Toast.makeText(TicketSaveActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                return;
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
    private TextWatcher textWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if(subject.equals(String.valueOf(s))) {
                //do something
                buttonsave.setVisibility(View.GONE);
            }else{
                buttonsave.setVisibility(View.VISIBLE);
                //do something
            }

        }
    };
}