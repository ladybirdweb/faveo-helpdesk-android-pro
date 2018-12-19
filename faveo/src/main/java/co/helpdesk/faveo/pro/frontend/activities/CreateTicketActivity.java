package co.helpdesk.faveo.pro.frontend.activities;

import android.Manifest;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.MenuSheetView;
import com.hbb20.CountryCodePicker;
import com.kishan.askpermission.AskPermission;
import com.kishan.askpermission.ErrorCallback;
import com.kishan.askpermission.PermissionCallback;
import com.kishan.askpermission.PermissionInterface;
import com.pixplicity.easyprefs.library.Prefs;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.AudioPickActivity;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.activity.NormalFilePickActivity;
import com.vincent.filepicker.activity.VideoPickActivity;
import com.vincent.filepicker.filter.entity.AudioFile;
import com.vincent.filepicker.filter.entity.ImageFile;
import com.vincent.filepicker.filter.entity.NormalFile;
import com.vincent.filepicker.filter.entity.VideoFile;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadStatusDelegate;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.helpdesk.faveo.pro.Constants;
import co.helpdesk.faveo.pro.FaveoApplication;
import co.helpdesk.faveo.pro.Helper;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.pro.frontend.adapters.CollaboratorAdapter;
import co.helpdesk.faveo.pro.frontend.adapters.MultiCollaboratorAdapter;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.pro.model.CollaboratorSuggestion;
import co.helpdesk.faveo.pro.model.Data;
import co.helpdesk.faveo.pro.model.MessageEvent;
import co.helpdesk.faveo.pro.model.MultiCollaborator;
import es.dmoral.toasty.Toasty;

import static com.vincent.filepicker.activity.AudioPickActivity.IS_NEED_RECORDER;
import static com.vincent.filepicker.activity.ImagePickActivity.IS_NEED_CAMERA;

/**
 * This activity is for responsible for creating the ticket.
 * Here we are using create ticket async task which is
 * POST request.We are getting the JSON data here from the dependency API.
 */
public class CreateTicketActivity extends AppCompatActivity implements PermissionCallback, ErrorCallback {
    boolean allCorrect;
    String term;
    String collaborators=null;
    ArrayAdapter<Data> spinnerPriArrayAdapter, spinnerHelpArrayAdapter,typeArrayAdapter,autocompletetextview,stringArrayAdapterHint;
    ArrayAdapter<CollaboratorSuggestion> arrayAdapterCC;
    MultiCollaboratorAdapter adapter1=null;
    AutoCompleteTextView editTextEmail;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.attachment_name)
    TextView attachmentFileName;
    @BindView(R.id.attachment_layout)
    RelativeLayout attachment_layout;
    @BindView(R.id.sub_edittext)
    EditText subEdittext;
    @BindView(R.id.msg_edittext)
    EditText msgEdittext;
    Spinner autoCompletePriority;
    Spinner autoCompleteHelpTopic;
    Spinner spinnerType;
    @BindView(R.id.buttonSubmit)
    Button buttonSubmit;
    ImageButton buttonUserCreate;
    ImageView imageViewBack;
    Spinner autoCompleteTextView;
    ArrayList<String> collaboratorArray;
    String[] cc,cc1;
    StringBuilder sb,sb1;
    String emailfromsuggestion;
    String email2="";
    @BindView(R.id.attachment_close)
    ImageButton imageButtonAttachmentClose;
    ProgressDialog progressDialog;
    ArrayList<Data> helptopicItems, priorityItems,staffItems,staffitemsauto,typeItems;
    ArrayList<CollaboratorSuggestion> emailHint;
    int id=0;
    int id1=0;
    String email1="",collaborator;
    ArrayList<MultiCollaborator> stringArraylist;
    String splChrs = "-/@#$%^&_+=()" ;
    String countrycode = "";
    int i=0;
    int res=0;
    MultiAutoCompleteTextView multiAutoCompleteTextViewCC;
    String firstname,lastname,email;
    String result;
    Button button;
    ImageButton refresh;
    int gallery,document,camera,audio=0;
    BottomNavigationView bottomNavigationView;
    private static final int PICKFILE_REQUEST_CODE = 1234;
    String path="";
    String phone,mobile,message;
    BottomSheetLayout bottomSheet;
    String token;
    Animation rotation;
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
    String CountryID="";
    String CountryZipCode="";
    String fname="",lname="";
    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            String blockCharacterSet = "~!@#$%^&*()_-;:<>,.[]{}|/+";
            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        setContentView(R.layout.activity_create_ticket);
        Window window = CreateTicketActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(CreateTicketActivity.this,R.color.faveo));
        GetCountryZipCode();
        refresh= (ImageButton) findViewById(R.id.imageRefresh);
        bottomSheet= (BottomSheetLayout) findViewById(R.id.bottomsheet);
        if (InternetReceiver.isConnected()){
            new FetchDependency().execute();
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        final ImageButton imageButton= (ImageButton) findViewById( R.id.attachment_close);
        bottomNavigationView= (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        collaboratorArray=new ArrayList<>();
        button= (Button) findViewById(R.id.attachment);
        msgEdittext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_SCROLL:
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                        return true;
                    case MotionEvent.ACTION_BUTTON_PRESS:
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(msgEdittext, InputMethodManager.SHOW_IMPLICIT);
                }
                return false;
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateTicketActivity.this);

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
                            }
                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        dialog.cancel();
                    }
                });

                alertDialog.show();
            }
        });
        buttonUserCreate= (ImageButton) findViewById(R.id.usercreate);

        imageViewBack= (ImageView) findViewById(R.id.imageViewBack);
        multiAutoCompleteTextViewCC= (MultiAutoCompleteTextView) findViewById(R.id.collaborator);

        stringArraylist=new ArrayList<MultiCollaborator>();
        adapter1=new MultiCollaboratorAdapter(CreateTicketActivity.this,stringArraylist);

        multiAutoCompleteTextViewCC.addTextChangedListener(ccedittextwatcher);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachment_layout.setVisibility(View.GONE);
                attachmentFileName.setText("");
                path="";
            }
        });

        buttonUserCreate.setVisibility(View.VISIBLE);
        buttonUserCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(CreateTicketActivity.this,RegisterUser.class);
                startActivity(intent);
            }
        });

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prefs.putString("newuseremail","");
                if (!editTextEmail.getText().toString().equals("")
                        ||!subEdittext.getText().toString().equals("")||
                !msgEdittext.getText().toString().equals("")){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateTicketActivity.this);

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
                            Intent newIntent = new Intent(CreateTicketActivity.this,MainActivity.class);
                            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(newIntent);
                        }
                    });

                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
                            dialog.cancel();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();

                }
                else {
//
                    Intent newIntent = new Intent(CreateTicketActivity.this,MainActivity.class);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(newIntent);
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = CreateTicketActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                MenuSheetView menuSheetView =
                        new MenuSheetView(CreateTicketActivity.this, MenuSheetView.MenuType.LIST, "Choose...", new MenuSheetView.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                if (bottomSheet.isSheetShowing()) {
                                    bottomSheet.dismissSheet();
                                }
                                if (item.getItemId()==R.id.imageGalley){
                                    gallery=2;
                                    reqPermissionCamera();
                                    return true;
                                }
                                else if (item.getItemId()==R.id.videoGallery){
                                    camera=3;
                                    reqPermissionCamera();
                                    return true;
                                }
                                else if (item.getItemId()==R.id.musicGallery){
                                    audio=4;
                                    reqPermissionCamera();
                                    return true;
                                }
                                else if (item.getItemId()==R.id.documentGallery){
                                    document=1;
                                    reqPermissionCamera();
                                    return true;
                                }

                                return true;
                            }
                        });
                menuSheetView.inflateMenu(R.menu.navigation);
                bottomSheet.showWithSheetView(menuSheetView);


            }
        });
        editTextEmail= (AutoCompleteTextView) findViewById(R.id.email_edittext);
        emailHint=new ArrayList<>();
        arrayAdapterCC=new CollaboratorAdapter(this,emailHint);

        editTextEmail.addTextChangedListener(passwordWatcheredittextSubject);
        editTextEmail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name1=editTextEmail.getText().toString();
                for (int i1=0;i1<emailHint.size();i1++){
                    CollaboratorSuggestion data1=emailHint.get(i1);
                    if (data1.getEmail().equals(name1)){
                        CollaboratorSuggestion data2=emailHint.get(i1);
                        id=data2.getId();
                        Log.d("id",id+"");
                        email1=data2.getEmail();
                        Log.d("email1",email1);
                        editTextEmail.setText(email1);
                        firstname=data2.getFirstname();
                    lastname=data2.getLastname();
                    editTextEmail.setText(email1);
                    }
                }

            }
        });



        multiAutoCompleteTextViewCC.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    emailfromsuggestion = adapterView.getItemAtPosition(i).toString();
                }catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                }



            }
        });
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createButtonClick();
            }
        });
        autoCompleteTextView= (Spinner) findViewById(R.id.autocompletetext);
        autoCompleteHelpTopic= (Spinner) findViewById(R.id.spinner_help);
        autoCompletePriority= (Spinner) findViewById(R.id.spinner_pri);
        setUpViews();

        autoCompleteHelpTopic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(subEdittext.getWindowToken(), 0);
                return false;
            }
        });
        autoCompletePriority.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(subEdittext.getWindowToken(), 0);
                return false;
            }
        });
        autoCompleteTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(subEdittext.getWindowToken(), 0);
                return false;
            }
        });
        try {
            firstname = Prefs.getString("firstusername", null);
            lastname = Prefs.getString("lastusername", null);
            email = Prefs.getString("firstuseremail", null);
            phone = Prefs.getString("firstusermobile", null);
        }catch (NullPointerException e){
            e.printStackTrace(); }


        if (email.equals("null")){
            editTextEmail.setText("");
        }
        else{
            editTextEmail.setText(email);
        }
    }

    public String GetCountryZipCode(){
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED){
            switch (requestCode) {
                case 3:
                    break;
                //Read External Storage
                case 4:
                    Intent imageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(imageIntent, 11);
                    break;
                //Camera
                case 5:
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, 12);
                    }
                    break;

            }

            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        String fileName;
        String filePath = null;
        try {
            if (data.toString().equals("")) {
                attachment_layout.setVisibility(View.GONE);
            } else {
                attachment_layout.setVisibility(View.VISIBLE);
                switch (requestCode){
                    case Constant.REQUEST_CODE_PICK_IMAGE:
                        if (resultCode==RESULT_OK){
                            ArrayList<ImageFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_IMAGE);
                            StringBuilder builder = new StringBuilder();
                            for (ImageFile file : list) {
                                filePath = file.getPath();
                                Log.d("filePath",path);
                                builder.append(path + "\n");
                            }
                            File file = new File(filePath);
                            long size = file.length()/1024;
                            if (size > 6000) {
                                Toasty.info(CreateTicketActivity.this, getString(R.string.fileSize), Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                path = filePath;
                                int pos=path.lastIndexOf("/");
                                fileName=path.substring(pos+1,path.length());
                                attachmentFileName.setText(fileName);
                                Log.d("fileName",fileName);
                            }
                        }
                        break;
                    case Constant.REQUEST_CODE_PICK_VIDEO:
                        if (resultCode == RESULT_OK) {
                            ArrayList<VideoFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_VIDEO);
                            StringBuilder builder = new StringBuilder();
                            for (VideoFile file : list) {
                                filePath = file.getPath();
                                builder.append(path + "\n");
                            }
                            File file = new File(filePath);
                            long size = file.length()/1024;
                            if (size > 6000) {
                                Toasty.info(CreateTicketActivity.this, getString(R.string.fileSize), Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                path = filePath;
                                int pos=path.lastIndexOf("/");
                                fileName=path.substring(pos+1,path.length());
                                attachmentFileName.setText(fileName);
                                Log.d("fileName",fileName);
                            }
                        }
                        break;
                    case Constant.REQUEST_CODE_PICK_AUDIO:
                        if (resultCode == RESULT_OK) {
                            ArrayList<AudioFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_AUDIO);
                            StringBuilder builder = new StringBuilder();
                            for (AudioFile file : list) {
                                filePath = file.getPath();
                                builder.append(path + "\n");
                            }
                            File file = new File(filePath);
                            long size = file.length()/1024;
                            if (size > 6000) {
                                Toasty.info(CreateTicketActivity.this, getString(R.string.fileSize), Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                path = filePath;
                                int pos=path.lastIndexOf("/");
                                fileName=path.substring(pos+1,path.length());
                                attachmentFileName.setText(fileName);
                                Log.d("fileName",fileName);
                            }
                        }
                        break;
                    case Constant.REQUEST_CODE_PICK_FILE:
                        if (resultCode == RESULT_OK) {
                            ArrayList<NormalFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_FILE);
                            StringBuilder builder = new StringBuilder();
                            for (NormalFile file : list) {
                                filePath = file.getPath();
                                builder.append(path + "\n");
                            }
                            File file = new File(filePath);
                            long size = file.length()/1024;
                            if (size > 6000) {
                                Toasty.info(CreateTicketActivity.this, getString(R.string.fileSize), Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                path = filePath;
                                int pos=path.lastIndexOf("/");
                                fileName=path.substring(pos+1,path.length());
                                attachmentFileName.setText(fileName);
                                Log.d("fileName",fileName);
                            }
                        }
                        break;

                }
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
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
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            //progressDialog.dismiss();
            //Toast.makeText(getApplicationContext(), result,
            //Toast.LENGTH_LONG).show();
            Log.d("resultFromNewCall",result);
            try {
                JSONObject jsonObject=new JSONObject(result);
                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                token = jsonObject1.getString("token");
                Prefs.putString("TOKEN", token);
                Log.d("TOKEN",token);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            try {
//                uploadMultipartData();
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }

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

    private void reqPermissionCamera() {
        new AskPermission.Builder(this).setPermissions(Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setCallback(this)
                .setErrorCallback(this)
                .request(PICKFILE_REQUEST_CODE);
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_ticket_menu, menu);

        return true;
    }

    /**
     * Handlig the menu items here.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

//            case R.id.action_create:
//                createButtonClick();
//                return true;

            case android.R.id.home:
                //Write your logic here
                this.finish();
                return true;
//            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Setting up the views here.
     */
    public void setUpViews() {
        JSONObject jsonObject;
        Data data;
        String json = Prefs.getString("DEPENDENCY", "");
        try {
            staffItems=new ArrayList<>();
            staffitemsauto=new ArrayList<>();
            staffitemsauto.add(new Data(0,"--"));

            jsonObject=new JSONObject(json);
            JSONArray jsonArrayStaffs=jsonObject.getJSONArray("staffs");
            for (int i=0;i<jsonArrayStaffs.length();i++){
                if (jsonArrayStaffs.getJSONObject(i).getString("first_name").equals("")&&jsonArrayStaffs.getJSONObject(i).getString("last_name").equals("")){
                    Log.d("cameHere","TRUE");
                    data = new Data(Integer.parseInt(jsonArrayStaffs.getJSONObject(i).getString("id")), jsonArrayStaffs.getJSONObject(i).getString("email"));
                }
                else {
                    data = new Data(Integer.parseInt(jsonArrayStaffs.getJSONObject(i).getString("id")), jsonArrayStaffs.getJSONObject(i).getString("first_name")+" "+jsonArrayStaffs.getJSONObject(i).getString("last_name"));
                }
                staffItems.add(data);
                staffitemsauto.add(data);
                Collections.sort(staffitemsauto, new Comparator<Data>() {
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
        } catch (JSONException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }



        spinnerHelpArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, helptopicItems); //selected item will look like a spinner set from XML
        spinnerHelpArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoCompleteHelpTopic.setAdapter(spinnerHelpArrayAdapter);


        autocompletetextview = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,staffitemsauto);
        autoCompleteTextView.setAdapter(autocompletetextview);
        //autoCompleteTextView.setThreshold(0);


        spinnerPriArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, priorityItems); //selected item will look like a spinner set from XML
        spinnerPriArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoCompletePriority.setAdapter(spinnerPriArrayAdapter);


        autoCompletePriority.setDropDownWidth(1000);

//
        // editTextFirstName.addTextChangedListener(mTextWatcher);
        subEdittext.setFilters(new InputFilter[]{filter});

    }

    @Override
    public void onBackPressed() {

        if (!editTextEmail.getText().toString().equals("")
                ||!subEdittext.getText().toString().equals("")||
                !msgEdittext.getText().toString().equals("")){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateTicketActivity.this);

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
                    finish();
//                    Intent newIntent = new Intent(CreateTicketActivity.this,MainActivity.class);
//                    newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(newIntent);
//                    if (!MainActivity.isShowing) {
//                        Log.d("isShowing", "false");
//                        Intent intent = new Intent(CreateTicketActivity.this, MainActivity.class);
//                        startActivity(intent);
//                    } else {
//                        Log.d("isShowing", "true");
//                        Intent intent = new Intent(CreateTicketActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
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
        else {
            Intent newIntent = new Intent(CreateTicketActivity.this,MainActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newIntent);

//            if (!MainActivity.isShowing) {
//                Log.d("isShowing", "false");
////                Intent intent = new Intent(this, MainActivity.class);
////                startActivity(intent);
//                finish();
//            } else {
//                Log.d("isShowing", "true");
////                Intent intent = new Intent(this, MainActivity.class);
////                startActivity(intent);
//                finish();
//            }
        }



    }

    /**
     * Handling the create button here.
     */
    public void createButtonClick() {
        new SendPostRequest().execute();
        res=0;
        cc=new String[2];
        cc1=new String[2];
        String first_user = null,second_user = null,third_user=null;
        String subject = subEdittext.getText().toString();
        String message = msgEdittext.getText().toString();
        email2 = editTextEmail.getText().toString();
        String email3=editTextEmail.getText().toString();
        if (!email3.equals(email1)){
            email2="";
        }
        if (email3.equals(email)){
            email2=email;
        }
        if (email3.equals(email1)){
            email2=email1;
        }

        Log.d("email2",email2);
        fname=firstname;
        lname=lastname;


        collaborator = multiAutoCompleteTextViewCC.getText().toString().replaceAll(" ","");
        String newCollaList;

                if (!collaborator.equals("")) {
                    if (!(collaborator.charAt(collaborator.length() - 1) == ',')) {
                        StringBuilder stringBuilder = new StringBuilder(collaborator);
                        newCollaList = stringBuilder.append(",").toString();
                        for (int i = 0; i < stringBuilder.toString().length(); i++) {
                            // checking character in string
                            if (stringBuilder.toString().charAt(i) == ',')
                                res++;
                        }

                    } else {
                        newCollaList = collaborator;
                        for (int i = 0; i < collaborator.length(); i++) {
                            // checking character in string
                            if (collaborator.charAt(i) == ',')
                                res++;
                        }

                    }

                    Log.d("newCollaList", newCollaList);

                    if (res > 3) {
                        Toasty.info(this, "you can add up to 3 collaborators", Toast.LENGTH_LONG).show();
                        return;
                    }
//
                    if (!newCollaList.equals("")) {
                        Log.d("colla", newCollaList);
                        Log.d("lastTerm", "commaExcluded");
                        //collaborator.replace("" + collaborator.charAt(collaborator.length() - 1), ",");
//                StringBuilder stringBuilder=new StringBuilder(newColla);
//                stringBuilder.append(",");
                        cc = newCollaList.split(",");
                        sb = new StringBuilder();
                        for (String aCc : cc) {
                            String one = aCc.trim();
                            Log.d("one", one);
                            if (!Helper.isValidEmail(one)) {
                                Toasty.error(CreateTicketActivity.this, getString(R.string.enter_valid_email), Toast.LENGTH_LONG).show();
                                allCorrect = false;
                                return;
                            } else {
                                sb.append(one).append(",");
                            }
                        }
                        Log.d("sb", sb.toString());
                        cc1 = sb.toString().split(",");
                        sb1 = new StringBuilder();
                        if (res == 1) {
                            for (String n : cc1) {
                                sb1.append("&cc[]=");
                                sb1.append(n);
                                first_user = cc1[0];
                            }
                            Log.d("first_user", first_user);
                            collaborators = sb1.toString();
                            Log.d("sb1", sb1.toString());
                        } else if (res == 2) {
                            for (String n : cc1) {
                                sb1.append("&cc[]=").append(n);
                                //sb1.append(n);
                                first_user = cc1[0];
                                second_user = cc1[1];

                            }
                            Log.d("first_user", first_user);
                            Log.d("second_user", second_user);
                            collaborators = sb1.toString();
                            Log.d("sb1", sb1.toString());
                        } else if (res == 3) {
                            for (String n : cc1) {
                                sb1.append("&cc[]=");
                                sb1.append(n);
                                first_user = cc1[0];
                                second_user = cc1[1];
                                third_user = cc1[2];
                            }
                            Log.d("first_user", first_user);
                            Log.d("second_user", second_user);
                            Log.d("third_user", third_user);
                            collaborators = sb1.toString();
                            Log.d("sb1", sb1.toString());
                        }
//                        for (String n : cc1) {
//                            sb1.append("&cc[]=");
//                            sb1.append(n);
//                        }
//                        collaborators = sb1.toString();
//                        Log.d("collaborators",collaborators);
                    } else {
                        Toasty.info(this, getString(R.string.collaboratorExisting), Toast.LENGTH_SHORT).show();
                        allCorrect = false;
                        return;
                    }
                }


        final Data helpTopic = (Data) autoCompleteHelpTopic.getSelectedItem();
        final Data priority = (Data) autoCompletePriority.getSelectedItem();
        final Data staff = (Data) autoCompleteTextView.getSelectedItem();



        allCorrect = true;

        if (email2.trim().length() == 0 || !Helper.isValidEmail(email2)||email2.equals("")) {
            Toasty.warning(this, getString(R.string.requestornotfound), Toast.LENGTH_SHORT).show();
            allCorrect = false;
        }
        else if (subject.trim().length() == 0) {
            Toasty.warning(this, getString(R.string.sub_must_not_be_empty), Toast.LENGTH_SHORT).show();
            allCorrect = false;
        } else if (subject.trim().length() < 5) {
            Toasty.warning(this, getString(R.string.sub_minimum_char), Toast.LENGTH_SHORT).show();
            allCorrect = false;
        } else if (subject.matches("[" + splChrs + "]+")) {
            Toasty.warning(this, getString(R.string.only_special_characters_not_allowed_here), Toast.LENGTH_SHORT).show();
            allCorrect = false;
        } else if (subject.trim().length() > 200) {
            Toasty.warning(this, getString(R.string.subjectExceed)
                    , Toast.LENGTH_SHORT).show();
            allCorrect = false;
        } else if (priority.ID == 0) {
            allCorrect = false;
            Toasty.warning(CreateTicketActivity.this, getString(R.string.please_select_some_priority), Toast.LENGTH_SHORT).show();
        } else if (helpTopic.ID == 0) {
            allCorrect = false;
            Toasty.warning(CreateTicketActivity.this, getString(R.string.select_some_helptopic), Toast.LENGTH_SHORT).show();
        } else if (message.trim().length() == 0) {
            Toasty.warning(this, getString(R.string.msg_must_not_be_empty), Toast.LENGTH_SHORT).show();
            allCorrect = false;
        } else if (message.trim().length() < 10) {
            Toasty.warning(this, getString(R.string.msg_minimum_char), Toast.LENGTH_SHORT).show();
            allCorrect = false;
        }
        if (autoCompleteTextView.getSelectedItem().toString().equals("")) {
            id = 0;
        }
        if (allCorrect) {

            if (InternetReceiver.isConnected()) {

                progressDialog = new ProgressDialog(CreateTicketActivity.this);
                if (path.equals("")) {
                    //Starting the upload
                    progressDialog = new ProgressDialog(CreateTicketActivity.this);
                    progressDialog.setMessage(getString(R.string.creating_ticket));


                    try {
                        fname = URLEncoder.encode(fname.trim(), "utf-8");
                        lname = URLEncoder.encode(lname.trim(), "utf-8");
                        subject = URLEncoder.encode(subject.trim(), "utf-8");
                        message = URLEncoder.encode(message.trim(), "utf-8");
                        email1 = URLEncoder.encode(email1.trim(), "utf-8");

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateTicketActivity.this);

                    // Setting Dialog Title
                    alertDialog.setTitle(getString(R.string.creatingTicket));

                    // Setting Dialog Message
                    alertDialog.setMessage(getString(R.string.createConfirmation));

                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.mipmap.ic_launcher);

                    // Setting Positive "Yes" Button
                    final String finalSubject = subject;
                    final String finalMessage = message;
                    final String finalFname = fname;
                    final String finalLname = lname;
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke YES event
                            //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                            if (InternetReceiver.isConnected()){
                                progressDialog = new ProgressDialog(CreateTicketActivity.this);
                                progressDialog.setMessage("Please wait");
                                progressDialog.show();
                                new CreateNewTicket(Integer.parseInt(Prefs.getString("ID", null)), finalSubject, finalMessage, helpTopic.ID, priority.ID,finalFname, finalLname, staff.ID,email2).execute();
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
                    if (res == 1) {
//                    progressDialog.setMessage(getString(R.string.creating_ticket));
//                    progressDialog.show();
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateTicketActivity.this);

                        // Setting Dialog Title
                        alertDialog.setTitle(getString(R.string.creatingTicket));

                        // Setting Dialog Message
                        alertDialog.setMessage(getString(R.string.createConfirmation));


                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.mipmap.ic_launcher);

                        // Setting Positive "Yes" Button
                        final String finalSubject1 = subject;
                        final String finalMessage1 = message;
                        final String finalPhone1 = phone;
                        final String finalFirst_user = first_user;
                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to invoke YES event
                                //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                                if (InternetReceiver.isConnected()) {
                                    progressDialog = new ProgressDialog(CreateTicketActivity.this);
                                    progressDialog.setMessage(getString(R.string.creating_ticket));
                                    progressDialog.show();
                                    try {
                                        token = Prefs.getString("TOKEN", null);
                                        String uploadId = UUID.randomUUID().toString();
                                        new MultipartUploadRequest(CreateTicketActivity.this, uploadId, Constants.URL + "helpdesk/create?token=" + token)
                                                .addFileToUpload(path, "media_attachment[]")
                                                .addParameter("user_id", Prefs.getString("ID", null))
                                                .addParameter("subject", finalSubject1)
                                                .addParameter("body", finalMessage1)
                                                .addParameter("help_topic", "" + helpTopic.ID)
                                                .addParameter("priority", "" + priority.ID)
                                                .addParameter("assigned", "" + staff.ID)
                                                .addParameter("first_name", firstname)
                                                .addParameter("last_name", lastname)
                                                .addArrayParameter("cc[]", finalFirst_user)
                                                .addParameter("email", email2)
                                                .setMaxRetries(1)
                                                .setMethod("POST").setDelegate(new UploadStatusDelegate() {
                                            @Override
                                            public void onProgress(UploadInfo uploadInfo) {

                                            }

                                            @Override
                                            public void onError(UploadInfo uploadInfo, Exception exception) {

                                            }

                                            @Override
                                            public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {
                                                progressDialog.dismiss();
                                                Log.d("newStyle", serverResponse.getBodyAsString());
                                                Log.i("newStyle", String.format(Locale.getDefault(),
                                                        "ID %1$s: completed in %2$ds at %3$.2f Kbit/s. Response code: %4$d, body:[%5$s]",
                                                        uploadInfo.getUploadId(), uploadInfo.getElapsedTime() / 1000,
                                                        uploadInfo.getUploadRate(), serverResponse.getHttpCode(),
                                                        serverResponse.getBodyAsString()));

                                                if (serverResponse.getBodyAsString().contains("Permission denied")){
                                                    Toasty.warning(CreateTicketActivity.this, getString(R.string.permission), Toast.LENGTH_LONG).show();
                                                    return;
                                                }

                                                try {
                                                    JSONObject jsonObject = new JSONObject(serverResponse.getBodyAsString());
                                                    JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                                                    String message = jsonObject1.getString("message");
                                                    if (message.equals("Ticket created successfully!")) {
                                                        Intent intent = new Intent(CreateTicketActivity.this, MainActivity.class);
                                                        editTextEmail.setText("");
                                                        startActivity(intent);
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                                String state = Prefs.getString("403", null);
                                                try {
                                                    if (state.equals("403") && !state.equals("null")) {
                                                        Toasty.warning(CreateTicketActivity.this, getString(R.string.permission), Toast.LENGTH_LONG).show();
                                                        Prefs.putString("403", "null");
                                                        return;
                                                    }
                                                } catch (NullPointerException e) {
                                                    e.printStackTrace();
                                                }


                                                try {

                                                    JSONObject jsonObject = new JSONObject(serverResponse.getBodyAsString());
                                                    JSONObject jsonObject1 = jsonObject.getJSONObject("error");
                                                    // JSONArray jsonArray=jsonObject1.getJSONArray("code");
                                                    String message = jsonObject1.getString("code");
                                                    if (message.contains("The code feild is required.")) {
                                                        Toasty.warning(CreateTicketActivity.this, getString(R.string.select_code), Toast.LENGTH_SHORT).show();
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }


//                            Intent intent=new Intent(CreateTicketActivity.this,MainActivity.class);
//                            startActivity(intent);

                                            }

                                            @Override
                                            public void onCancelled(UploadInfo uploadInfo) {

                                            }
                                        })
                                                .startUpload(); //Starting the upload
                                    } catch (MalformedURLException | NullPointerException | IllegalArgumentException | FileNotFoundException e) {
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
                    } else if (res == 2) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateTicketActivity.this);

                        // Setting Dialog Title
                        alertDialog.setTitle(getString(R.string.creatingTicket));

                        // Setting Dialog Message
                        alertDialog.setMessage(getString(R.string.createConfirmation));


                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.mipmap.ic_launcher);

                        // Setting Positive "Yes" Button
                        final String finalSubject1 = subject;
                        final String finalMessage1 = message;
                        final String finalPhone1 = phone;
                        final String finalFirst_user = first_user;
                        final String finalSecond_user = second_user;
                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to invoke YES event
                                //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                                if (InternetReceiver.isConnected()) {
                                    progressDialog = new ProgressDialog(CreateTicketActivity.this);
                                    progressDialog.setMessage(getString(R.string.creating_ticket));
                                    progressDialog.show();
                                    try {
                                        token = Prefs.getString("TOKEN", null);
                                        String uploadId = UUID.randomUUID().toString();
                                        new MultipartUploadRequest(CreateTicketActivity.this, uploadId, Constants.URL + "helpdesk/create?token=" + token)
                                                .addFileToUpload(path, "media_attachment[]")
                                                .addParameter("user_id", Prefs.getString("ID", null))
                                                .addParameter("subject", finalSubject1)
                                                .addParameter("body", finalMessage1)
                                                .addParameter("help_topic", "" + helpTopic.ID)
                                                .addParameter("priority", "" + priority.ID)
                                                .addParameter("assigned", "" + staff.ID)
                                                .addParameter("first_name", firstname)
                                                .addParameter("last_name", lastname)
                                                .addArrayParameter("cc[]", finalFirst_user)
                                                .addArrayParameter("cc[]", finalSecond_user)
                                                .addParameter("email", email2)
                                                .setMaxRetries(1)
                                                .setMethod("POST").setDelegate(new UploadStatusDelegate() {
                                            @Override
                                            public void onProgress(UploadInfo uploadInfo) {

                                            }

                                            @Override
                                            public void onError(UploadInfo uploadInfo, Exception exception) {

                                            }

                                            @Override
                                            public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {
                                                progressDialog.dismiss();
                                                Log.d("newStyle", serverResponse.getBodyAsString());
                                                Log.i("newStyle", String.format(Locale.getDefault(),
                                                        "ID %1$s: completed in %2$ds at %3$.2f Kbit/s. Response code: %4$d, body:[%5$s]",
                                                        uploadInfo.getUploadId(), uploadInfo.getElapsedTime() / 1000,
                                                        uploadInfo.getUploadRate(), serverResponse.getHttpCode(),
                                                        serverResponse.getBodyAsString()));
                                                if (serverResponse.getBodyAsString().contains("Permission denied")){
                                                    Toasty.warning(CreateTicketActivity.this, getString(R.string.permission), Toast.LENGTH_LONG).show();
                                                    return;
                                                }

                                                try {
                                                    JSONObject jsonObject = new JSONObject(serverResponse.getBodyAsString());
                                                    JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                                                    String message = jsonObject1.getString("message");
                                                    if (message.equals("Ticket created successfully!")) {
                                                        Intent intent = new Intent(CreateTicketActivity.this, MainActivity.class);
                                                        editTextEmail.setText("");
                                                        startActivity(intent);
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                                String state = Prefs.getString("403", null);
                                                try {
                                                    if (state.equals("403") && !state.equals("null")) {
                                                        Toasty.warning(CreateTicketActivity.this, getString(R.string.permission), Toast.LENGTH_LONG).show();
                                                        Prefs.putString("403", "null");
                                                        return;
                                                    }
                                                } catch (NullPointerException e) {
                                                    e.printStackTrace();
                                                }


                                                try {

                                                    JSONObject jsonObject = new JSONObject(serverResponse.getBodyAsString());
                                                    JSONObject jsonObject1 = jsonObject.getJSONObject("error");
                                                    // JSONArray jsonArray=jsonObject1.getJSONArray("code");
                                                    String message = jsonObject1.getString("code");
                                                    if (message.contains("The code feild is required.")) {
                                                        Toasty.warning(CreateTicketActivity.this, getString(R.string.select_code), Toast.LENGTH_SHORT).show();
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }


//                            Intent intent=new Intent(CreateTicketActivity.this,MainActivity.class);
//                            startActivity(intent);

                                            }

                                            @Override
                                            public void onCancelled(UploadInfo uploadInfo) {

                                            }
                                        })
                                                .startUpload(); //Starting the upload
                                    } catch (MalformedURLException | NullPointerException | IllegalArgumentException | FileNotFoundException e) {
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
                    } else if (res == 3) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateTicketActivity.this);

                        // Setting Dialog Title
                        alertDialog.setTitle(getString(R.string.creatingTicket));

                        // Setting Dialog Message
                        alertDialog.setMessage(getString(R.string.createConfirmation));


                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.mipmap.ic_launcher);

                        // Setting Positive "Yes" Button
                        final String finalSubject1 = subject;
                        final String finalMessage1 = message;
                        final String finalPhone1 = phone;
                        final String finalFirst_user = first_user;
                        final String finalSecond_user = second_user;
                        final String finalThird_user = third_user;
                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to invoke YES event
                                //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                                if (InternetReceiver.isConnected()) {
                                    progressDialog = new ProgressDialog(CreateTicketActivity.this);
                                    progressDialog.setMessage(getString(R.string.creating_ticket));
                                    progressDialog.show();
                                    try {
                                        token = Prefs.getString("TOKEN", null);
                                        String uploadId = UUID.randomUUID().toString();
                                        new MultipartUploadRequest(CreateTicketActivity.this, uploadId, Constants.URL + "helpdesk/create?token=" + token)
                                                .addFileToUpload(path, "media_attachment[]")
                                                .addParameter("user_id", Prefs.getString("ID", null))
                                                .addParameter("subject", finalSubject1)
                                                .addParameter("body", finalMessage1)
                                                .addParameter("help_topic", "" + helpTopic.ID)
                                                .addParameter("priority", "" + priority.ID)
                                                .addParameter("assigned", "" + staff.ID)
                                                .addParameter("first_name", firstname)
                                                .addParameter("last_name", lastname)
                                                .addArrayParameter("cc[]", finalFirst_user)
                                                .addArrayParameter("cc[]", finalSecond_user)
                                                .addArrayParameter("cc[]", finalThird_user)
                                                .addParameter("email", email2)
                                                .setMaxRetries(1)
                                                .setMethod("POST").setDelegate(new UploadStatusDelegate() {
                                            @Override
                                            public void onProgress(UploadInfo uploadInfo) {

                                            }

                                            @Override
                                            public void onError(UploadInfo uploadInfo, Exception exception) {

                                            }

                                            @Override
                                            public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {
                                                progressDialog.dismiss();
                                                Log.d("newStyle", serverResponse.getBodyAsString());
                                                Log.i("newStyle", String.format(Locale.getDefault(),
                                                        "ID %1$s: completed in %2$ds at %3$.2f Kbit/s. Response code: %4$d, body:[%5$s]",
                                                        uploadInfo.getUploadId(), uploadInfo.getElapsedTime() / 1000,
                                                        uploadInfo.getUploadRate(), serverResponse.getHttpCode(),
                                                        serverResponse.getBodyAsString()));
                                                if (serverResponse.getBodyAsString().contains("Permission denied")){
                                                    Toasty.warning(CreateTicketActivity.this, getString(R.string.permission), Toast.LENGTH_LONG).show();
                                                    return;
                                                }

                                                try {
                                                    JSONObject jsonObject = new JSONObject(serverResponse.getBodyAsString());
                                                    JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                                                    String message = jsonObject1.getString("message");
                                                    if (message.equals("Ticket created successfully!")) {
                                                        Intent intent = new Intent(CreateTicketActivity.this, MainActivity.class);
                                                        editTextEmail.setText("");
                                                        startActivity(intent);
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                                String state = Prefs.getString("403", null);
                                                try {
                                                    if (state.equals("403") && !state.equals("null")) {
                                                        Toasty.warning(CreateTicketActivity.this, getString(R.string.permission), Toast.LENGTH_LONG).show();
                                                        Prefs.putString("403", "null");
                                                        return;
                                                    }
                                                } catch (NullPointerException e) {
                                                    e.printStackTrace();
                                                }


                                                try {

                                                    JSONObject jsonObject = new JSONObject(serverResponse.getBodyAsString());
                                                    JSONObject jsonObject1 = jsonObject.getJSONObject("error");
                                                    String message = jsonObject1.getString("code");
                                                    if (message.contains("The code feild is required.")) {
                                                        Toasty.warning(CreateTicketActivity.this, getString(R.string.select_code), Toast.LENGTH_SHORT).show();
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }

                                            @Override
                                            public void onCancelled(UploadInfo uploadInfo) {

                                            }
                                        })
                                                .startUpload(); //Starting the upload
                                    } catch (MalformedURLException | NullPointerException | IllegalArgumentException | FileNotFoundException e) {
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
                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateTicketActivity.this);

                        alertDialog.setTitle(getString(R.string.creatingTicket));

                        // Setting Dialog Message
                        alertDialog.setMessage(getString(R.string.createConfirmation));


                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.mipmap.ic_launcher);

                        // Setting Positive "Yes" Button
                        final String finalSubject1 = subject;
                        final String finalMessage1 = message;
                        final String finalPhone1 = phone;
                        final String finalFirst_user = first_user;
                        final String finalSecond_user = second_user;
                        final String finalThird_user = third_user;
                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to invoke YES event
                                //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                                if (InternetReceiver.isConnected()) {
                                    progressDialog = new ProgressDialog(CreateTicketActivity.this);
                                    progressDialog.setMessage(getString(R.string.creating_ticket));
                                    progressDialog.show();
                                    try {
                                        token = Prefs.getString("TOKEN", null);
                                        String uploadId = UUID.randomUUID().toString();
                                        new MultipartUploadRequest(CreateTicketActivity.this, uploadId, Constants.URL + "helpdesk/create?token=" + token)
                                                .addFileToUpload(path, "media_attachment[]")
                                                //Adding file
                                                //.addParameter("token", token1)
                                                //.addParameter("token",token)
                                                .addParameter("user_id", Prefs.getString("ID", null))
                                                .addParameter("subject", finalSubject1)
                                                .addParameter("body", finalMessage1)
                                                .addParameter("help_topic", "" + helpTopic.ID)
                                                .addParameter("priority", "" + priority.ID)
                                                .addParameter("assigned", "" + staff.ID)
                                                .addParameter("first_name", firstname)
                                                .addParameter("last_name", lastname)
                                                .addParameter("email", email2)
                                                .setMaxRetries(1)
                                                .setMethod("POST").setDelegate(new UploadStatusDelegate() {
                                            @Override
                                            public void onProgress(UploadInfo uploadInfo) {

                                            }

                                            @Override
                                            public void onError(UploadInfo uploadInfo, Exception exception) {

                                            }

                                            @Override
                                            public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {
                                                progressDialog.dismiss();
                                                Log.d("newStyle", serverResponse.getBodyAsString());
                                                Log.i("newStyle", String.format(Locale.getDefault(),
                                                        "ID %1$s: completed in %2$ds at %3$.2f Kbit/s. Response code: %4$d, body:[%5$s]",
                                                        uploadInfo.getUploadId(), uploadInfo.getElapsedTime() / 1000,
                                                        uploadInfo.getUploadRate(), serverResponse.getHttpCode(),
                                                        serverResponse.getBodyAsString()));

                                                if (serverResponse.getBodyAsString().contains("Permission denied")){
                                                    Toasty.warning(CreateTicketActivity.this, getString(R.string.permission), Toast.LENGTH_LONG).show();
                                                    return;
                                                }
                                                try {
                                                    JSONObject jsonObject = new JSONObject(serverResponse.getBodyAsString());
                                                    JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                                                    String message = jsonObject1.getString("message");
                                                    if (message.equals("Ticket created successfully!")) {
                                                        Intent intent = new Intent(CreateTicketActivity.this, MainActivity.class);
                                                        editTextEmail.setText("");
                                                        startActivity(intent);
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                String state = Prefs.getString("403", null);
                                                try {
                                                    if (state.equals("403") && !state.equals("null")) {
                                                        Toasty.warning(CreateTicketActivity.this, getString(R.string.permission), Toast.LENGTH_LONG).show();
                                                        Prefs.putString("403", "null");
                                                        return;
                                                    }
                                                } catch (NullPointerException e) {
                                                    e.printStackTrace();
                                                }


                                                try {

                                                    JSONObject jsonObject = new JSONObject(serverResponse.getBodyAsString());
                                                    JSONObject jsonObject1 = jsonObject.getJSONObject("error");
                                                    String message = jsonObject1.getString("code");
                                                    if (message.contains("The code feild is required.")) {
                                                        Toasty.warning(CreateTicketActivity.this, getString(R.string.select_code), Toast.LENGTH_SHORT).show();
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }

                                            @Override
                                            public void onCancelled(UploadInfo uploadInfo) {

                                            }
                                        })
                                                .startUpload(); //Starting the upload
                                    } catch (MalformedURLException | NullPointerException | IllegalArgumentException | FileNotFoundException e) {
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
            } else {
                Toasty.info(this, getString(R.string.oops_no_internet), Toast.LENGTH_SHORT, true).show();
            }
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
        Log.d("requestCode",""+requestCode);

        if (document==1){
            Intent intent4 = new Intent(this, NormalFilePickActivity.class);
            intent4.putExtra(Constant.MAX_NUMBER, 1);
            intent4.putExtra(NormalFilePickActivity.SUFFIX,
                    new String[] {"xlsx", "xls", "doc", "dOcX", "ppt", ".pptx", "pdf"});
            startActivityForResult(intent4, Constant.REQUEST_CODE_PICK_FILE);
            document=0;
        }
        if (gallery==2){
            Intent intent1 = new Intent(this, ImagePickActivity.class);
            intent1.putExtra(IS_NEED_CAMERA, true);
            intent1.putExtra(Constant.MAX_NUMBER, 1);
            startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);
            gallery=0;
        }

        if (camera==3){
            Intent intent2 = new Intent(this, VideoPickActivity.class);
            intent2.putExtra(IS_NEED_CAMERA, true);
            intent2.putExtra(Constant.MAX_NUMBER, 1);
            startActivityForResult(intent2, Constant.REQUEST_CODE_PICK_VIDEO);
            camera=0;
        }
        if (audio==4){
            Intent intent3 = new Intent(this, AudioPickActivity.class);
            intent3.putExtra(IS_NEED_RECORDER, true);
            intent3.putExtra(Constant.MAX_NUMBER, 1);
            startActivityForResult(intent3, Constant.REQUEST_CODE_PICK_AUDIO);
            audio=0;

        }
    }

    @Override
    public void onPermissionsDenied(int requestCode) {
        Toasty.warning(CreateTicketActivity.this,getString(R.string.permission_camera_denied),Toast.LENGTH_SHORT).show();
        return;
    }
    /**
     * Async task for creating the ticket.
     */
    private class CreateNewTicket extends AsyncTask<File, Void, String> {
        String fname, lname, email;
        String subject;
        public String body;
        int helpTopic;
        // int SLA;
        int priority;
        //int dept;
        int userID;
        int staff;
        String string;

        CreateNewTicket(int userID, String subject, String body,
                        int helpTopic, int priority,String fname, String lname,int staff, String email) {

            this.subject = subject;
            this.body = body;
            this.helpTopic = helpTopic;
            //this.SLA = SLA;
            this.priority = priority;
            //this.dept = dept;
            this.userID = userID;
            this.lname = lname;
            this.fname = fname;
            this.email = email;
            this.staff=staff;

        }

        @Override
        protected String doInBackground(File... files) {


            return new Helpdesk().postCreateTicket(userID, subject, body, helpTopic, priority, fname, lname, staff,email + collaborators);
        }

        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            id=0;
            collaborators=null;
            if (result == null) {
                Toasty.error(CreateTicketActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }
            String state=Prefs.getString("403",null);
            try {
                if (state.equals("403") && !state.equals("null")) {
                    Toasty.warning(CreateTicketActivity.this, getString(R.string.permission), Toast.LENGTH_LONG).show();
                    Prefs.putString("403", "null");
                    return;
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }


            try {

                JSONObject jsonObject=new JSONObject(result);
                JSONObject jsonObject1=jsonObject.getJSONObject("error");
                String message=jsonObject1.getString("code");
                if (message.contains("The code feild is required.")){
                    Toasty.warning(CreateTicketActivity.this,getString(R.string.select_code),Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (result.contains("Ticket created successfully!")) {
                Toasty.success(CreateTicketActivity.this, getString(R.string.ticket_created_success), Toast.LENGTH_LONG).show();
                finish();
                editTextEmail.setText("");
                id=0;
                Prefs.putString("newuseremail",null);
                startActivity(new Intent(CreateTicketActivity.this, MainActivity.class));

            }


        }


    }



    /**
     * This method will be called when a MessageEvent is posted (in the UI thread for Toast).
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {

        showSnack(event.message);
    }

    @Override
    protected void onResume() {

        super.onResume();

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Display the snackbar if network connection is not there.
     *
     * @param isConnected is a boolean value of network connection.
     */
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

    /**
     * Display the snackbar if network connection is there.
     *
     * @param isConnected is a boolean value of network connection.
     */
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
    TextWatcher passwordWatcheredittextSubject = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String term = editTextEmail.getText().toString();
            if (InternetReceiver.isConnected()) {
                if (!term.equals("")&&term.length()==3){
                    refresh.startAnimation(rotation);
                    String newTerm=term;
                    arrayAdapterCC=new CollaboratorAdapter(CreateTicketActivity.this,emailHint);
                    new FetchCollaborator(newTerm.trim()).execute();

                }
            }
        }

        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher ccedittextwatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            term = multiAutoCompleteTextViewCC.getText().toString();
            if (InternetReceiver.isConnected()) {
                    if (term.contains(",")) {

                        int pos = term.lastIndexOf(",");
                        term = term.substring(pos + 1, term.length());
                        if (term.length()==3){
                            Log.d("newTerm", term);
                            adapter1 = new MultiCollaboratorAdapter(CreateTicketActivity.this, stringArraylist);
                            refresh.startAnimation(rotation);
                            new FetchCollaboratorCC(term.trim()).execute();
                        }

                    }
                    else if (term.equals("")) {
                        adapter1 = new MultiCollaboratorAdapter(CreateTicketActivity.this, stringArraylist);

                    } else if (term.length()==3){
                        adapter1 = new MultiCollaboratorAdapter(CreateTicketActivity.this, stringArraylist);
                        refresh.startAnimation(rotation);

                        new FetchCollaboratorCC(term).execute();


                    }
                }

        }
        public void afterTextChanged(Editable s) {
        }
    };
    private class FetchCollaborator extends AsyncTask<String, Void, String> {
        String term;

        FetchCollaborator(String term) {

            this.term = term;
        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().getUser(term);
        }

        protected void onPostExecute(String result) {
            if (isCancelled()) return;
            refresh.clearAnimation();
            emailHint.clear();

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("users");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String email = jsonObject1.getString("email");
                    id1 = Integer.parseInt(jsonObject1.getString("id"));
                    String first_name=jsonObject1.getString("first_name");
                    String last_name=jsonObject1.getString("last_name");
                    String profilePic=jsonObject1.getString("profile_pic");
                    CollaboratorSuggestion collaboratorSuggestion=new CollaboratorSuggestion(id1,first_name,last_name,email,profilePic);
                    emailHint.add(collaboratorSuggestion);

                }
                editTextEmail.setThreshold(3);
                editTextEmail.setDropDownWidth(1500);
                editTextEmail.setAdapter(arrayAdapterCC);
                editTextEmail.showDropDown();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }


        }
    }

    private class FetchCollaboratorCC extends AsyncTask<String, Void, String> {
        String term;
        int count=0;
        FetchCollaboratorCC(String term) {

            this.term = term;
        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().getUser(term);
        }

        protected void onPostExecute(String result) {
            //progressBar.setVisibility(View.GONE);
            refresh.clearAnimation();
            if (isCancelled()) return;
            stringArraylist.clear();

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("users");
                if (jsonArray.length()==0){
                    Prefs.putString("noUser","null");

                }
                else{
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String email = jsonObject1.getString("email");
                        id1 = Integer.parseInt(jsonObject1.getString("id"));
                        String first_name = jsonObject1.getString("first_name");
                        String last_name = jsonObject1.getString("last_name");
                        String profilePic=jsonObject1.getString("profile_pic");
                        //Toast.makeText(TicketSaveActivity.this, "email:"+email, Toast.LENGTH_SHORT).show();
                        MultiCollaborator collaboratorSuggestion=new MultiCollaborator(first_name,last_name,email,profilePic,id1);
                        //Data data = new Data(id, first_name + " " + last_name + " <" + email + ">");
                        stringArraylist.add(collaboratorSuggestion);
                        Log.d("array",stringArraylist.toString());
                    }
                    multiAutoCompleteTextViewCC.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
                    multiAutoCompleteTextViewCC.setThreshold(3);
                    multiAutoCompleteTextViewCC.setDropDownWidth(1500);
                    multiAutoCompleteTextViewCC.setAdapter(adapter1);
                    multiAutoCompleteTextViewCC.showDropDown();

                }

            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
            }


        }
    }
    private class FetchDependency extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {

            return new Helpdesk().getDependency();

        }

        protected void onPostExecute(String result) {
                refresh.clearAnimation();
            try {
                progressDialog.dismiss();
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            Log.d("Depen Response : ", result + "");

            if (result==null) {

            }
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                Prefs.putString("DEPENDENCY", jsonObject1.toString());
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
                    keyPriority += jsonArrayPriorities.getJSONObject(i).getString("priority_id") + ",";
                    valuePriority += jsonArrayPriorities.getJSONObject(i).getString("priority") + ",";
                }
                Prefs.putString("keyPri", keyPriority);
                Prefs.putString("valuePri", valuePriority);


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
                button.setVisibility(View.VISIBLE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //new FaveoApplication().clearApplicationData();
                        NotificationManager notificationManager =
                                (NotificationManager) CreateTicketActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.cancelAll();
                        FaveoApplication.getInstance().clearApplicationData();
                        String url=Prefs.getString("URLneedtoshow",null);
                        Prefs.clear();
                        Prefs.putString("URLneedtoshow",url);
                        CreateTicketActivity.this.getSharedPreferences(Constants.PREFERENCE, Context.MODE_PRIVATE).edit().clear().apply();
                        Intent intent = new Intent(CreateTicketActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        Toasty.success(CreateTicketActivity.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
                    }
                });
                Prefs.putString("unauthorized", "false");
                Prefs.putString("401","false");
                e.printStackTrace();
            } finally {

            }
        }
    }



}