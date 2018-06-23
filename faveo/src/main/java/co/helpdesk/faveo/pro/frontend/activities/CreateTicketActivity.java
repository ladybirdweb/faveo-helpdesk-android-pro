package co.helpdesk.faveo.pro.frontend.activities;

import android.Manifest;
import android.app.NotificationManager;
import android.app.ProgressDialog;
//import android.app.SearchManager;
//import android.content.Context;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
//import android.graphics.Bitmap;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.widget.CursorAdapter;
//import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
//import android.telephony.TelephonyManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.MenuSheetView;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hbb20.CountryCodePicker;
import com.kishan.askpermission.AskPermission;
import com.kishan.askpermission.ErrorCallback;
import com.kishan.askpermission.PermissionCallback;
import com.kishan.askpermission.PermissionInterface;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
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
//import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadServiceBroadcastReceiver;
import net.gotev.uploadservice.UploadStatusDelegate;

//import org.apache.commons.io.FileUtils;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.HttpResponseException;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.entity.mime.FormBodyPart;
//import org.apache.http.entity.mime.HttpMultipartMode;
//import org.apache.http.entity.mime.MultipartEntity;
//import org.apache.http.entity.mime.content.ContentBody;
//import org.apache.http.entity.mime.content.FileBody;
//import org.apache.http.entity.mime.content.StringBody;
//import org.apache.http.impl.client.BasicResponseHandler;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.util.EntityUtils;
//import org.apache.james.mime4j.message.Multipart;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
//import java.text.DecimalFormat;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;
//import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import co.helpdesk.faveo.pro.BottomNavigationBehavior;
import co.helpdesk.faveo.pro.CameraUtils;
import co.helpdesk.faveo.pro.Constants;
import co.helpdesk.faveo.pro.FaveoApplication;
import co.helpdesk.faveo.pro.Helper;
import co.helpdesk.faveo.pro.ImagePath_MarshMallow;
import co.helpdesk.faveo.pro.MyDeserializer;
import co.helpdesk.faveo.pro.MyResponse;
import co.helpdesk.faveo.pro.PathUtil;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.UserClient;
import co.helpdesk.faveo.pro.backend.api.v1.Authenticate;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.pro.frontend.adapters.CollaboratorAdapter;
import co.helpdesk.faveo.pro.frontend.adapters.MultiCollaboratorAdapter;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.pro.model.CollaboratorSuggestion;
import co.helpdesk.faveo.pro.model.Data;
import co.helpdesk.faveo.pro.model.MessageEvent;
import co.helpdesk.faveo.pro.model.MultiCollaborator;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    ArrayAdapter<Data> spinnerPriArrayAdapter, spinnerHelpArrayAdapter,spinnerStaffArrayAdapter,autocompletetextview,stringArrayAdapterHint;
    ArrayAdapter<CollaboratorAdapter> arrayAdapterCollaborator;
    ArrayAdapter<CollaboratorSuggestion> arrayAdapterCC;
    CollaboratorAdapter adapter=null;
    MultiCollaboratorAdapter adapter1=null;
    @BindView(R.id.fname_edittext)
    EditText editTextFirstName;
    AutoCompleteTextView editTextEmail;
    @BindView(R.id.lname_edittext)
    EditText editTextLastName;
    @BindView(R.id.phone_edittext)
    EditText editTextPhone;
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
    @BindView(R.id.phone_edittext10)
    EditText editTextMobile;
    Spinner autoCompletePriority;
    Spinner autoCompleteHelpTopic;
    @BindView(R.id.buttonSubmit)
    Button buttonSubmit;
    ImageButton buttonUserCreate;
    ImageView imageViewBack;
    Spinner autoCompleteTextView;
    ArrayList<String> collaboratorArray;
    String[] cc,cc1;
    StringBuilder sb,sb1;
    String emailfromsuggestion;
    String email2;
    //    @BindView(R.id.attachment)
//    Button button;
    @BindView(R.id.attachment_close)
    ImageButton imageButtonAttachmentClose;
    ProgressDialog progressDialog;
    ArrayList<Data> helptopicItems, priorityItems,staffItems,staffitemsauto,staffItemsHint;
    ArrayList<CollaboratorSuggestion> emailHint;
    int id=0;
    int id1=0;
    String email1,collaborator;
    ArrayList<MultiCollaborator> stringArraylist;
    //String mobile="";
    String splChrs = "-/@#$%^&_+=()" ;
    String countrycode = "";
    int i=0;
    int res=0;
    MultiAutoCompleteTextView multiAutoCompleteTextViewCC;
    CountryCodePicker countryCodePicker;
    String firstname,lastname,email;
    ImageButton imageViewGallery,imageViewCamera,imageViewDocument,imageViewAudio;
    Toolbar toolbarAttachment;
    File file3;
    String result;
    Button button;
    ImageButton refresh;
    File file;
    Thread t;
    ProgressBar progressBar;
    int gallery,document,camera,audio=0;
    BottomNavigationView bottomNavigationView;
    private static final int CAMERA_REQUEST = 1888;
    private static final int PICKFILE_REQUEST_CODE = 1234;
    private static final int PICKVIDEO_REQUEST_CODE = 1235;
    String path="";
    String phone,mobile,message;
    BottomSheetLayout bottomSheet;
    String token;
    Animation rotation;
    public static final int FILE_PICKER_REQUEST_CODE = 1;
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
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //FirebaseCrash.report(new Exception("App Name : My first Android non-fatal error"));
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        setContentView(R.layout.activity_create_ticket);
        Window window = CreateTicketActivity.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(CreateTicketActivity.this,R.color.faveo));
        GetCountryZipCode();
        refresh= (ImageButton) findViewById(R.id.imageRefresh);
        bottomSheet= (BottomSheetLayout) findViewById(R.id.bottomsheet);
        if (InternetReceiver.isConnected()){
            new FetchDependency().execute();
        }
        //progressBar= (ProgressBar) findViewById(R.id.createTicketProgressbar);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        final ImageButton imageButton= (ImageButton) findViewById( R.id.attachment_close);
        bottomNavigationView= (BottomNavigationView) findViewById(R.id.navigation);
        collaboratorArray=new ArrayList<>();
        //toolbarAttachment= (Toolbar) findViewById(R.id.bottom_navigation);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        toolbarBottom= (Toolbar) findViewById(R.id.bottom_navigation);
//        toolbarBottom.setVisibility(View.GONE);
        //imageViewAudio= (ImageButton) toolbarAttachment.findViewById(R.id.audio_img_btn);
//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // attaching bottom sheet behaviour - hide / show on scroll
//        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
//        layoutParams.setBehavior(new BottomNavigationBehavior());
//        imageViewGallery= (ImageButton) toolbarAttachment.findViewById(R.id.gallery_img_btn);
//        imageViewCamera= (ImageButton) toolbarAttachment.findViewById(R.id.photo_img_btn);
//        imageViewDocument= (ImageButton) toolbarAttachment.findViewById(R.id.document);
        button= (Button) findViewById(R.id.attachment);

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
//                            progressDialog=new ProgressDialog(CreateTicketActivity.this);
//                            progressDialog.setMessage(getString(R.string.refreshing));
//                            progressDialog.show();
                            new FetchDependency().execute();
                            setUpViews();
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
        buttonUserCreate= (ImageButton) findViewById(R.id.usercreate);

        imageViewBack= (ImageView) findViewById(R.id.imageViewBack);
        multiAutoCompleteTextViewCC= (MultiAutoCompleteTextView) findViewById(R.id.collaborator);

        stringArraylist=new ArrayList<MultiCollaborator>();
        adapter1=new MultiCollaboratorAdapter(CreateTicketActivity.this,stringArraylist);
        //multiAutoCompleteTextViewCC.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        //multiAutoCompleteTextViewCC.setAdapter(adapter1);
        //arrayAdapterCollaborator=new ArrayAdapter<>(CreateTicketActivity.this,android.R.layout.simple_dropdown_item_1line,stringArraylist);

        multiAutoCompleteTextViewCC.addTextChangedListener(ccedittextwatcher);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachment_layout.setVisibility(View.GONE);
                attachmentFileName.setText("");
                //attachmentFileSize.setText("");
                path="";
                //toolbarAttachment.setVisibility(View.GONE);
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
                //onBackPressed();
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
                            Intent intent = new Intent(CreateTicketActivity.this, MainActivity.class);
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
                else {
                    Intent intent = new Intent(CreateTicketActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
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

//                if (bottomNavigationView.getVisibility()==View.GONE){
//                    bottomNavigationView.setVisibility(View.VISIBLE);
//                }
//                else if (bottomNavigationView.getVisibility()==View.VISIBLE){
//                    bottomNavigationView.setVisibility(View.GONE);
//                }


            }
        });

        //getSupportActionBar().setTitle(R.string.create_ticket);
        //ccp = (CountryCodePicker) findViewById(R.id.ccp);
        countryCodePicker= (CountryCodePicker) findViewById(R.id.countrycoode);
        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                //Toast.makeText(MainActivity.this, "code :"+countryCodePicker.getSelectedCountryCode(), Toast.LENGTH_SHORT).show();

                countrycode=countryCodePicker.getSelectedCountryCode();
            }
        });
        editTextEmail= (AutoCompleteTextView) findViewById(R.id.email_edittext);
        emailHint=new ArrayList<>();
        arrayAdapterCC=new CollaboratorAdapter(this,emailHint);
        //arrayAdapterCC=new ArrayAdapter<Data>(CreateTicketActivity.this,android.R.layout.simple_dropdown_item_1line,emailHint);

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
                        firstname=data2.getFirst_name();
                    lastname=data2.getLast_name();
                    editTextEmail.setText(email1);
                    editTextFirstName.setText(firstname);
                    editTextLastName.setText(lastname);
                    }
                }
//                CollaboratorSuggestion data = emailHint.get(i);
//                id=data.getId();
//                Log.d("id",id+"");
//                email1=data.getEmail();
//                Log.d("email1",email1);
                //editTextEmail.setText(email1);
//              for (int j=0;j<emailHint.size();j++){
//                    CollaboratorSuggestion data = emailHint.get(j);
//                    id = data.getId();
//                    Log.d("id",id+"");
//                    email1=data.getEmail();
//                    Log.d("email1",email1);
//                    String profilePic=data.getProfile_pic();
//                    firstname=data.getFirst_name();
//                    lastname=data.getLast_name();
//                    editTextEmail.setText(email1);
//                    editTextFirstName.setText(firstname);
//                    editTextLastName.setText(lastname);
//                    Log.d("profilePic",profilePic);
//
//                }

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
        try {
            firstname = Prefs.getString("firstusername", null);
            lastname = Prefs.getString("lastusername", null);
            email = Prefs.getString("firstuseremail", null);
            phone = Prefs.getString("firstusermobile", null);
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        if (firstname.equals("null")){
            editTextFirstName.setText("");
        }
        else{
            editTextFirstName.setText(firstname);
        }


        if (lastname.equals("null")){
            editTextLastName.setText("");
        }
        else{
            editTextLastName.setText(lastname);
        }


        if (email.equals("null")){
            editTextEmail.setText("");
        }
        else{
            editTextEmail.setText(email);
        }


        if (phone.equals("null")){
            editTextPhone.setText("");
        }
        else{
            editTextPhone.setText(phone);
        }


        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        // AndroidNetworking.enableLogging();
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
//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.imageGalley:
//                    gallery=2;
//                    reqPermissionCamera();
//                    return true;
//                case R.id.videoGallery:
//                    camera=3;
//                    reqPermissionCamera();
//                    return true;
//                case R.id.musicGallery:
//                    document=1;
//                    reqPermissionCamera();
//                    return true;
//                case R.id.documentGallery:
//                    audio=4;
//                    reqPermissionCamera();
//                    return true;
//
//            }
//
//            return false;
//        }
//    };


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//
//        if (requestCode == CAMERA_REQUEST) {
//            // BEGIN_INCLUDE(permission_result)
//            // Received permission result for camera permission.
//            //Log.i(TAG, "Received response for Camera permission request.");
//
//            // Check if the only required permission has been granted
//            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Camera permission has been granted, preview can be displayed
//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, CAMERA_REQUEST);
//
//
//            } else {
//                //Log.i(TAG, "CAMERA permission was NOT granted.");
//                Snackbar.make(findViewById(android.R.id.content), R.string.permissions_not_granted,
//                        Snackbar.LENGTH_SHORT).show();
//
//            }
//            // END_INCLUDE(permission_result)
//
//        } else {
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//    }


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




//        if (requestCode == 1 && resultCode == RESULT_OK) {
//            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
//            //getMimeType(data.getData());
//            File file = new File(filePath);
//            long size = file.length() / 1024;
//            if (size > 6000) {
//                Toasty.info(CreateTicketActivity.this, getString(R.string.fileSize), Toast.LENGTH_SHORT).show();
//                return;
//            } else {
//                path = filePath;
//                int pos=path.lastIndexOf("/");
//                fileName=path.substring(pos+1,path.length());
//                attachmentFileName.setText(fileName+"("+size+"KB)");
//                Log.d("fileName",fileName);
//            }
//            Log.d("fileSize", "" + size);
//            Log.d("Path", filePath);
//            // Do anything with file
//        } else if (requestCode == PICKVIDEO_REQUEST_CODE && resultCode == RESULT_OK) {
//            //Uri uri1 = data.getData();
//            Log.d("BuildVersionSDK",""+Build.VERSION.SDK_INT);
//            uri = data.getData();
//            String filePath=getRealPathFromUri(CreateTicketActivity.this,uri);
//            Log.d("PATH",filePath);
//            File file = new File(filePath);
//            long size = file.length()/1024;
//            if (size > 6000) {
//                Toasty.info(CreateTicketActivity.this, getString(R.string.fileSize), Toast.LENGTH_SHORT).show();
//                return;
//            } else {
//                path = filePath;
//                int pos=path.lastIndexOf("/");
//                fileName=path.substring(pos+1,path.length());
//                attachmentFileName.setText(fileName);
//                Log.d("fileName",fileName);
//            }
//        }

//        else if (requestCode==PICKFILE_REQUEST_CODE&&resultCode==RESULT_OK){
//            uri = data.getData();
//            String filePath=getRealPathFromUri(CreateTicketActivity.this,uri);
//            Log.d("PATH",filePath);
//            File file = new File(filePath);
//            long size = file.length()/1024;
//            if (size > 6000) {
//                Toasty.info(CreateTicketActivity.this, getString(R.string.fileSize), Toast.LENGTH_SHORT).show();
//                return;
//            } else {
//                path = filePath;
//                int pos=path.lastIndexOf("/");
//                fileName=path.substring(pos+1,path.length());
//                attachmentFileName.setText(fileName);
//                Log.d("fileName",fileName);
//            }
////            Log.d("fileSize", "" + size);
////            Log.d("Path", filePath);
//        }
    }

    public Uri getImageUriForCamera(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURIForImage(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
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

//    @Nullable
//    private static String multipost(String urlString, MultipartEntity reqEntity) {
//
//        try {
//            URL url = new URL(urlString);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setReadTimeout(60*1000);
//            conn.setConnectTimeout(60*1000);
//            conn.setRequestMethod("POST");
//            conn.setUseCaches(false);
//            conn.setDoInput(true);
//            conn.setDoOutput(true);
//
//            conn.setRequestProperty("Connection", "Keep-Alive");
//            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
//            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
//            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + "*****");
//
//            conn.addRequestProperty("Content-length", reqEntity.getContentLength()+"");
//            conn.addRequestProperty(reqEntity.getContentType().getName(), reqEntity.getContentType().getValue());
//
//            OutputStream os = conn.getOutputStream();
//            reqEntity.writeTo(conn.getOutputStream());
//            os.close();
//            conn.connect();
//
//            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                return readStream(conn.getInputStream());
//            }
//
//        } catch (Exception e) {
//            Log.e("MainActivity", "multipart post error " + e + "(" + urlString + ")");
//        }
//        return null;
//    }
//
//    private static String readStream(InputStream in) {
//        BufferedReader reader = null;
//        StringBuilder builder = new StringBuilder();
//        try {
//            reader = new BufferedReader(new InputStreamReader(in));
//            String line = "";
//            while ((line = reader.readLine()) != null) {
//                builder.append(line);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return builder.toString();
//    }

    private void uploadMultipartData() throws UnsupportedEncodingException {

//        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
//
//        //FileBody fileBody = new FileBody();
//        // FormBodyPart fileBodyPart = new FormBodyPart(fileName, new FileBody(file));
//        reqEntity.addPart("media_attachment[]", new FileBody(file));
//        //api_key=9p41T2XFZ34YRZJUNQAdmM7iV0Rr1CjN&token="+token1+"&ip=null&user_id=1&subject=gggggggggg&body=hhzbbbsbbbsbbdhjanbsbdb&help_topic=1&priority=1&first_name=Adnan&last_name=Prasad&email=adnan@gmail.com&assigned=1&phone=null&code=91&mobile=5884889466"
//        reqEntity.addPart("username", new StringBody("demoadmin"));
//        reqEntity.addPart("password",new StringBody("demopass"));

//        try {
//
//            fname = URLEncoder.encode(fname.trim(), "utf-8");
//            lname = URLEncoder.encode(lname.trim(), "utf-8");
//            subject = URLEncoder.encode(subject.trim(), "utf-8");
//            message = URLEncoder.encode(message.trim(), "utf-8");
//            email1 = URLEncoder.encode(email1.trim(), "utf-8");
//            phone = URLEncoder.encode(phone.trim(), "utf-8");
//            mobile = URLEncoder.encode(mobile.trim(), "utf-8");


    }
//        catch (Exception exc) {
//            //Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
//            Log.d("exception",exc.getMessage());
//        }

//        reqEntity.addPart("token",new StringBody(token1));
//        reqEntity.addPart("user_id",new StringBody(""+1));
//        reqEntity.addPart("subject",new StringBody("ggggggggggggg"));
//        reqEntity.addPart("body",new StringBody("hhhhhhhhhhhhhh"));
//        reqEntity.addPart("help_topic",new StringBody(""+1));
//        reqEntity.addPart("priority",new StringBody(""+1));
//        reqEntity.addPart("first_name",new StringBody("sayar"));
//        reqEntity.addPart("last_name",new StringBody("samanta"));

    //reqEntity.addPart("password", new StringBody("demopass"));
    //String response = multipost("http://www.jamboreebliss.com/sayar/public/api/v1/authenticate?", reqEntity);
    //Log.d("MainActivity", "Response :"+response);





//        try {
//             buf=getBytesFromFile(file);
//             base64 = Base64.encodeToString(buf,Base64.DEFAULT);
//             Log.d("base64",base64);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    //encodeImage(path);




//        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
//            i++;
//            attachment_layout.setVisibility(View.VISIBLE);
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            Uri tempUri = getImageUri(getApplicationContext(), photo);
//            File finalFile = new File(getRealPathFromURI(tempUri));
//            long fileSizeInBytes = finalFile.length();
//            long fileSizeInKB = fileSizeInBytes / 1024;
//            String path=finalFile.getPath();
//            Log.d("fileSize", String.valueOf(finalFile.length()/1024));
//            Log.d("FinalPath",path);
//            Log.d("DATA",data.toString());
////            int bitmapHeight=photo.getHeight();
////            int bitmapWidth=photo.getWidth();
////            Log.d("bitmapheight",""+bitmapHeight);
////            Log.d("bitmapwidth",""+bitmapWidth);
////            int size=bitmapHeight*bitmapWidth;
////            Log.d("photoSize",""+size);
//            //Uri uri=data.getData();
//            Picasso.with(this).load(String.valueOf(photo)).into(imageView);
//            //getMimeType(uri);
//           // Log.d("URI",uri.toString());
//            String picName=tempUri.toString();
//            if (!picName.equals("")){
//                int pos=picName.lastIndexOf("/");
//                String newName=picName.substring(pos+1,picName.length());
//                StringBuilder stringBuilder=new StringBuilder();
//                stringBuilder.append("Faveo"+i+".jpeg");
//                Log.d("newName",stringBuilder.toString());
//                attachmentFileName.setText(newName+".jpg");
//            }
//
//            //int bitmapByteCount= BitmapCompat.getAllocationByteCount(photo)/ (1024 * 1024);
////            int size= 0;
////            //int finalSize=0;
////            if (photo != null) {
////                size = photo.getRowBytes()*photo.getHeight();
////                //finalSize=size/ (1024 * 1024);
////            }
//
//
//            //int size=BitmapCompat.getAllocationByteCount(photo)/(1024*1024);
//
//            //Log.d("bitmapsize",""+size);
////            long l = 9999999990L;
////            long MEGABYTE = 1024L * 1024L;
////            long b = l / MEGABYTE;
//
//            imageView.setImageBitmap(photo);
//            //Log.d("BitMapSize",finalSize+"");
//            attachmentFileSize.setText(""+fileSizeInKB+"KB");
//        }
//        else if (requestCode==PICKFILE_REQUEST_CODE&&resultCode == RESULT_OK&&null != data){
//            i++;
//            attachment_layout.setVisibility(View.VISIBLE);
//
////            //Bitmap photo = (Bitmap) data.getExtras().get("data");
//            Uri selectedImage = data.getData();
//            File file = new File(selectedImage.toString());
//            File file1=new File(selectedImage.getPath());
//            String strFileName = file.getName();
//            Log.d("FilePath", String.valueOf(file.length()));
//            Log.d("FileSize", String.valueOf(file.length()));
//            Log.d("ImageName",strFileName);
//            getMimeType(selectedImage);
//            if (mimeType.contains("image/jpeg")||mimeType.contains("image/png")||mimeType.contains("jpg")){
//                Bitmap bmp = null;
//                try {
//                    bmp = getBitmapFromUri(selectedImage);
//                    //bmp=ShrinkBitmap(String.valueOf(file),100,100);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                Uri tempUri = getImageUri(getApplicationContext(), bmp);
//                File finalFile = new File(getRealPathFromURI(tempUri));
//                long fileSizeInBytes = finalFile.length();
//                String path = tempUri.getPath();
//                File file3 = new File(path);  //file Path
//                byte[] b = new byte[(int) file.length()];
//                try {
//                    FileInputStream fileInputStream = new FileInputStream(file);
//                    fileInputStream.read(b);
//                    for (int j = 0; j < b.length; j++) {
//                        System.out.print((char) b[j]);
//                    }
//                } catch (FileNotFoundException e) {
//                    System.out.println("File Not Found.");
//                    e.printStackTrace();
//                } catch (IOException e1) {
//                    System.out.println("Error Reading The File.");
//                    e1.printStackTrace();
//                }
//
//                byte[] byteFileArray = new byte[0];
//                try {
//                    byteFileArray = FileUtils.readFileToByteArray(file);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                String base64String = "";
//                if (byteFileArray.length > 0) {
//                    base64String = android.util.Base64.encodeToString(byteFileArray, android.util.Base64.NO_WRAP);
//                    Log.i("File Base64 string", "IMAGE PARSE ==>" + base64String);
//                }
//                long fileSizeInKB = fileSizeInBytes / 1024;
//// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
//                long fileSizeInMB = fileSizeInKB / 1024;
//                Log.d("fileSizeInMB",""+fileSizeInKB);
//                //Bitmap converetdImage = getResizedBitmap(bmp, 100);
//                //Log.d("convertedImage",converetdImage.toString());
//               //int size=BitmapCompat.getAllocationByteCount(bmp)/(1024*1024);
//                //Log.d("File Size",""+size);
//                String picName=selectedImage.toString();
//            if (!picName.equals("")){
//                int pos=strFileName.indexOf("A");
//                String finalName=strFileName.substring(pos+1,strFileName.length()).replaceAll("%20"," ");
//                //String newName=picName.substring(pos+1,picName.length());
//                //StringBuilder stringBuilder=new StringBuilder(finalName);
//                //stringBuilder.append("Faveo"+ finalName+".jpeg");
//                Log.d("newName",finalName);
//                if (finalName.contains(".jpg")){
//                    attachmentFileName.setText(finalName);
//                }
//                else{
//                    attachmentFileName.setText(finalName+".jpg");
//                }
//
//            }
//
////                int height=imageView.getDrawable().getIntrinsicWidth();;
////                int width=imageView.getDrawable().getIntrinsicHeight();
////                int bitmapHeight=bmp.getHeight();
////                int bitmapWidth=bmp.getWidth();
////                Log.d("imageviewheight",""+height);
////                Log.d("imageviewweight",""+width);
////                Log.d("bitmapheight",""+bitmapHeight);
////                Log.d("bitmapwidth",""+bitmapWidth);
//
//
////            int bitmapByteCount= BitmapCompat.getAllocationByteCount(bmp)/ (1024*1024);
////            Log.d("ImageSize",""+bitmapByteCount);
//////                float aspectRatio = bmp.getWidth() /
//////                        (float) bmp.getHeight();
//////                int width = 480;
//////                int height = Math.round(width / aspectRatio);
//////
//////                bmp = Bitmap.createScaledBitmap(
//////                        bmp, width, height, false);
//            attachmentFileSize.setText(""+fileSizeInKB+"KB");
//            Picasso.with(this).load(selectedImage).into(imageView);
//            }
//            else if (mimeType.contains("application/pdf")){
//                Uri uri=data.getData();
//                Log.d("URI for PDF",uri.toString());
//                getMimeType(uri);
//                File file3 = new File(uri.toString());
//                long length = file3.length();
//                //length = length/1024;
//                //attachmentFileSize.setText(""+length+"kb");
//                //Log.d("fileLength", String.valueOf(file3.length()));
//                int pos=strFileName.indexOf("A");
//                String finalName=strFileName.substring(pos+1,strFileName.length()).replaceAll("%20"," ");
//                attachmentFileName.setText(finalName);
//                Log.d("Pdf file name",file3.getAbsolutePath().substring(file3.getAbsolutePath().lastIndexOf("\\")+1));
//                String data1=file3.getAbsolutePath().substring(file3.getAbsolutePath().lastIndexOf("\\")+1);
//                //attachmentFileSize.setText(""+length+"mb");
//                imageView.setImageResource(R.drawable.ic_picture_as_pdf_black_24dp);
//
//            }
//            else if (mimeType.contains("text/plain")){
//                int pos=strFileName.indexOf("A");
//                Uri uriData=data.getData();
//                getMimeType(uriData);
//                String finalName=strFileName.substring(pos+1,strFileName.length()).replaceAll("%20"," ");
//                String path = uriData.getPath();
//                File file3 = new File(path);  //file Path
//                byte[] b = new byte[(int) file.length()];
//                try {
//                    FileInputStream fileInputStream = new FileInputStream(file);
//                    fileInputStream.read(b);
//                    for (int j = 0; j < b.length; j++) {
//                        System.out.print((char) b[j]);
//                    }
//                } catch (FileNotFoundException e) {
//                    System.out.println("File Not Found.");
//                    e.printStackTrace();
//                } catch (IOException e1) {
//                    System.out.println("Error Reading The File.");
//                    e1.printStackTrace();
//                }
//
//                byte[] byteFileArray = new byte[0];
//                try {
//                    byteFileArray = FileUtils.readFileToByteArray(file);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                String base64String = "";
//                if (byteFileArray.length > 0) {
//                    base64String = android.util.Base64.encodeToString(byteFileArray, android.util.Base64.NO_WRAP);
//                    Log.i("File Base64 string", "IMAGE PARSE ==>" + base64String);
//                }
//                long length = file1.length();
//                length = length/1024;
//                //int file_size = Integer.parseInt(String.valueOf(file1.length()/1024));
//                //attachmentFileName.setText("Faveo"+i+".pdf");
//                attachmentFileName.setText(finalName);
//                Log.d("Pdf file name",file1.getAbsolutePath().substring(file1.getAbsolutePath().lastIndexOf("\\")+1));
//                String data1=file1.getAbsolutePath().substring(file1.getAbsolutePath().lastIndexOf("\\")+1);
//
//                String base64 = Base64.encodeToString(data1.getBytes(), Base64.DEFAULT);
//                Log.d("Base64",base64);
//                attachmentFileSize.setText(""+length+"kb");
//                Log.d("MimeType","Plain Text");
//                //file3 = new File(uri.toString());
//                //openFile(file3);
//            }
////            else if (mimeType.contains("audio/mpeg")){
////                Uri audioFileUri = data.getData();
////                getRealPathFromURI(audioFileUri);
////                Log.d("URI",audioFileUri.toString());
////
////            }
//
//            else{
//                Toasty.warning(this, getString(R.string.unsupportedFileType),
//                        Toast.LENGTH_LONG).show();
//            }
//        }



    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider

        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            System.out.println("getPath() uri: " + uri.toString());
            System.out.println("getPath() uri authority: " + uri.getAuthority());
            System.out.println("getPath() uri path: " + uri.getPath());


            // ExternalStorageProvider
            if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                System.out.println("getPath() docId: " + docId + ", split: " + split.length + ", type: " + type);

                // This is for checking Main Memory
                if ("primary".equalsIgnoreCase(type)) {
                    if (split.length > 1) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1] + "/";
                    } else {
                        return Environment.getExternalStorageDirectory() + "/";
                    }
                    // This is for checking SD Card
                } else {
                    return "storage" + "/" + docId.replace(":", "/");
                }

            }
        }
        return null;
    }
//    public static byte[] getBytesFromFile(File file) throws IOException {
//        // Get the size of the file
//        long length = file.length();
//
//        // You cannot create an array using a long type.
//        // It needs to be an int type.
//        // Before converting to an int type, check
//        // to ensure that file is not larger than Integer.MAX_VALUE.
//        if (length > Integer.MAX_VALUE) {
//            // File is too large
//            throw new IOException("File is too large!");
//        }
//
//        // Create the byte array to hold the data
//        byte[] bytes = new byte[(int)length];
//
//        // Read in the bytes
//        int offset = 0;
//        int numRead = 0;
//
//        InputStream is = new FileInputStream(file);
//        try {
//            while (offset < bytes.length
//                    && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
//                offset += numRead;
//            }
//        } finally {
//            is.close();
//        }
//
//        // Ensure all the bytes have been read in
//        if (offset < bytes.length) {
//            throw new IOException("Could not completely read file "+file.getName());
//        }
//        return bytes;
//    }
//    private void uploadFile(Uri fileUri) {
//        File file = null;
//
//        RequestBody requestBody=RequestBody.create(MultipartBody.FORM, "subject");
//
//        File originalFile=FileUtils.getFile(fileName);
//       try {
//           file = new File(getPathFromUri(this,fileUri));
//       }catch (NullPointerException e){
//           e.printStackTrace();
//       }
//        RequestBody filePart=RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)),file);
////        Gson gson = new GsonBuilder()
////                .setLenient()
////                .create();
//        final Gson gson =
//                new GsonBuilder()
//                        .registerTypeAdapter(MyResponse.class, new MyDeserializer())
//                        .create();
//        MultipartBody.Part part=MultipartBody.Part.createFormData("media_attachment",originalFile.getName(),filePart);
//        Retrofit.Builder builder=new Retrofit.Builder().baseUrl("http://jamboreebliss.com/")
//                .addConverterFactory(GsonConverterFactory.create(gson));
//
//        Retrofit retrofit=builder.build();
//        UserClient client=retrofit.create(UserClient.class);
//
//
//        Call<ResponseBody> responseBodyCall=client.createTicket(requestBody,part);
//        responseBodyCall.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
//                Log.d("status",""+response.code());
//                Log.d("URL",""+call.request().url());
//                Log.d("Call request header", call.request().headers().toString());
//                Log.d("Response raw header", response.headers().toString());
//                Log.d("Response raw", String.valueOf(response.raw().body()));
//                Log.e("TAG", "response 33: "+new Gson().toJson(response.body()) );
//                //MyResponse c = gson.fromJson(myJson, MyResponse.class);
////                if(response.isSuccessful()) {
////                    //showResponse(response.body().toString());
////                    Log.i("ddddd", "post submitted to API." +new Gson().toJson(response.body()));
////                }
////                else{
////                    try {
////                        Log.i("ddddd", "post submitted to API." +new Gson().toJson(response.body()));
////                    }catch (NullPointerException e){
////                        e.printStackTrace();
////                    }
////                    Log.i("responseFailure","true");
////                }
//                //Log.d("jsonObject",response.body().getMessage());
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.d("URL",""+call.request().url());
//                Log.d("Error",t.toString());
//
//            }
//        });
//        responseBodyCall.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
//
//
//                Toast.makeText(CreateTicketActivity.this, ""+response.body(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
//                Log.d("callingUrl", "call failed against the url: " + call.request().url());
//
//            }
//        });

    //}



//    private String encodeImage(String path)
//    {
//        File imagefile = new File(path);
//        FileInputStream fis = null;
//        try{
//            fis = new FileInputStream(imagefile);
//        }catch(FileNotFoundException e){
//            e.printStackTrace();
//        }
//        Bitmap bm = BitmapFactory.decodeStream(fis);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
//        byte[] b = baos.toByteArray();
//        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
//        Log.d("base64",encImage);
//        return encImage;
//
//    }




//    private void requestCameraPermission() {
//        Log.i("RequestingPermission", "CAMERA permission has NOT been granted. Requesting permission.");
//
//        // BEGIN_INCLUDE(camera_permission_request)
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                Manifest.permission.CAMERA)) {
//            // Provide an additional rationale to the user if the permission was not granted
//            // and the user would benefit from additional context for the use of the permission.
//            // For example if the user has previously denied the permission.
//            Log.i("DisplayingPermission",
//                    "Displaying camera permission rationale to provide additional context.");
//            Snackbar.make(findViewById(android.R.id.content), R.string.permission_camera_rationale,
//                    Snackbar.LENGTH_INDEFINITE)
//                    .setAction(R.string.ok, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            ActivityCompat.requestPermissions(CreateTicketActivity.this,
//                                    new String[]{Manifest.permission.CAMERA},
//                                    CAMERA_REQUEST);
//                        }
//                    })
//                    .show();
//        } else {
//
//            // Camera permission has not been granted yet. Request it directly.
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
//                    CAMERA_REQUEST);
//        }
//        // END_INCLUDE(camera_permission_request)
//    }
//    public void showCamera(View view) {
//        Log.i("PressingCameraButton", "Show camera button pressed. Checking permission.");
//        // BEGIN_INCLUDE(camera_permission)
//        // Check if the Camera permission is already available.
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//                != PackageManager.PERMISSION_GRANTED) {
//            // Camera permission has not been granted.
//
//            requestCameraPermission();
//
//        } else {
//
//            // Camera permissions is already available, show the camera preview.
//            Log.i("AlreadyPermission",
//                    "CAMERA permission has already been granted. Displaying camera preview.");
//            //showCameraPreview();
//
//        }
//        // END_INCLUDE(camera_permission)
//
//    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    Log.d("result",result);
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public String getRealPathFromURIMethod (Uri contentUri) {
        String path = null;
        String[] proj = { MediaStore.MediaColumns.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }
    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
    private String getRealPathFromURI(Uri contentURI)
    {
        String result = null;

        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);

        if (cursor == null)
        { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        }
        else
        {
            if(cursor.moveToFirst())
            {
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx);
            }
            cursor.close();
        }
        return result;
    }

    //this method compresses the image and saves into a location in sdcard
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    //    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
//        ParcelFileDescriptor parcelFileDescriptor =
//                getContentResolver().openFileDescriptor(uri, "r");
//        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
//        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
//        parcelFileDescriptor.close();
//        return image;
//    }
    private void reqPermissionCamera() {
        new AskPermission.Builder(this).setPermissions(Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setCallback(this)
                .setErrorCallback(this)
                .request(PICKFILE_REQUEST_CODE);
    }

//    private void reqPermissionStorage(){
//        new AskPermission.Builder(this).setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
//                .setCallback(this)
//                .setErrorCallback(this)
//                .request(PICKFILE_REQUEST_CODE);
//    }





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

//            case R.id.action_attach: {
//                new BottomSheet.Builder(this).title("Attach files from").sheet(R.menu.bottom_menu).listener(new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch (which) {
//                            case R.id.action_gallery:
//
//                                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
//                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//                                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
//                                break;
////                            case R.id.action_docx:
////
//////                                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//////                                //intent.setType("text/*");
//////                                intent.setType("*/*");
//////                                intent.addCategory(Intent.CATEGORY_OPENABLE);
//////                                startActivityForResult(Intent.createChooser(intent, "Select a doc"), RESULT_LOAD_FILE);
////
////                                break;
//                            default:
//                                break;
//                        }
//                    }
//                }).show();
//                return true;
//            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    public String getPath(Uri uri) {
//        String[] projection = {MediaStore.Images.Media.DATA};
//        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        String ss = cursor.getString(column_index);
//        cursor.close();
//        return ss;
//    }

//    /**
//     * Here we are handling the activity result.
//     *
//     * @param requestCode
//     * @param resultCode
//     * @param data
//     */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        try {
//            if (requestCode == RESULT_LOAD_FILE && resultCode == RESULT_OK
//                    && null != data) {
//                Uri selectedFile = data.getData();
    //String uriString = getPath(selectedFile);
//                File myFile = new File(uriString);
//                imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_txt));
//                attachmentFileSize.setText(getFileSize(myFile.length()));
//                attachmentFileName.setText(myFile.getName());
//            } else if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
//                    && null != data) {
//                // Get the Image from data
//                Uri selectedImage = data.getData();
//                Log.d("selectedIMG  ", selectedImage + "");
//                Log.d("getPath()  ", getPath(selectedImage) + "");
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
//                String uriString = getPath(selectedImage);
//                File myFile = new File(uriString);
////                String[] filePathColumn = {MediaStore.Images.Media.DATA};
////
////                // Get the cursor
////                Cursor cursor = getContentResolver().query(selectedImage,
////                        filePathColumn, null, null, null);
////                // Move to first row
////                cursor.moveToFirst();
////
////                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
////                imgDecodableString = cursor.getString(columnIndex);
////                cursor.close();
//                attachment_layout.setVisibility(View.VISIBLE);
//                // Set the Image in ImageView after decoding the String
//                imageView.setImageBitmap(bitmap);
//                Log.d("size", myFile.length() + "");
//                //attachmentFileSize.setText("(" + myFile.length() / 1024 + "kb)");
//                attachmentFileSize.setText(getFileSize(myFile.length()));
//                attachmentFileName.setText(myFile.getName());
//
//            } else {
//                Toasty.info(this, getString(R.string.you_hvent_picked_anything),
//                        Toast.LENGTH_LONG).show();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toasty.error(this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG)
//                    .show();
//        }
//    }

//    public static String getFileSize(long size) {
//        if (size <= 0)
//            return "0";
//        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
//        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
//        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
//    }
//
//    public int getCountryZipCode() {
//        String CountryID = "";
//        String CountryZipCode = "";
//        int code = 0;
//
//        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
//        //getNetworkCountryIso
//        CountryID = manager.getSimCountryIso().toUpperCase();
//        String[] rl = this.getResources().getStringArray(R.array.spinnerCountryCodes);
//        for (String aRl : rl) {
//            String[] g = aRl.split(",");
//            if (g[1].trim().equals(CountryID.trim())) {
//                CountryZipCode = g[0];
//                //code = i;
//                break;
//            }
//        }
//        return Integer.parseInt(CountryZipCode);
//    }
//
//    private void selectValue(Spinner spinner, Object value) {
//        for (int i = 0; i < spinner.getCount(); i++) {
//            spinner.getItemAtPosition(i);
//            String[] split = spinner.getItemAtPosition(i).toString().split(",");
//            String s = split[1];
//            if (s.equals(value)) {
//                Log.d("dsegffg", i + "");
//                spinner.setSelection(i);
//                break;
//            }
//        }
//    }

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
        // selectValue(phCode, getCountryZipCode());
        // phCode.setSelection(getCountryZipCode());
//        final CursorAdapter suggestionAdapter = new SimpleCursorAdapter(this,
//                android.R.layout.simple_list_item_1,
//                null,
//                new String[]{SearchManager.SUGGEST_COLUMN_TEXT_1},
//                new int[]{android.R.id.text1},
//                0);
//        final List<String> suggestions = new ArrayList<>();

//        requesterSearchview.setSuggestionsAdapter(suggestionAdapter);
//        requesterSearchview.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
//            @Override
//            public boolean onSuggestionSelect(int position) {
//                return false;
//            }
//
//            @Override
//            public boolean onSuggestionClick(int position) {
//                requesterSearchview.setQuery(suggestions.get(position), false);
//                requesterSearchview.clearFocus();
//                //doSearch(suggestions.get(position));
//                return true;
//            }
//        });
//
//        requesterSearchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                if (newText.length() > 2) {
//                    //loadData(s);
//                    Toast.makeText(getBaseContext(), newText, Toast.LENGTH_SHORT).show();
//                }
//
//                //                MyApp.autocompleteService.search(newText, new Callback<Autocomplete>() {
////                    @Override
////                    public void success(Autocomplete autocomplete, Response response) {
////                        suggestions.clear();
////                        suggestions.addAll(autocomplete.suggestions);
////
////                        String[] columns = {
////                                BaseColumns._ID,
////                                SearchManager.SUGGEST_COLUMN_TEXT_1,
////                                SearchManager.SUGGEST_COLUMN_INTENT_DATA
////                        };
////
////                        MatrixCursor cursor = new MatrixCursor(columns);
////
////                        for (int i = 0; i < autocomplete.suggestions.size(); i++) {
////                            String[] tmp = {Integer.toString(i), autocomplete.suggestions.get(i), autocomplete.suggestions.get(i)};
////                            cursor.addRow(tmp);
////                        }
////                        suggestionAdapter.swapCursor(cursor);
////                    }
////
////                    @Override
////                    public void failure(RetrofitError error) {
////                        Toast.makeText(SearchFoodActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
////                        Log.w("autocompleteService", error.getMessage());
////                    }
////                });
//                return true;
//            }
//        });
//        ccSearchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                //Toast.makeText(getBaseContext(), query, Toast.LENGTH_LONG).show();
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//
//                return true;
//            }
//        });

//        ImageButton attchmentClose = (ImageButton) findViewById(R.id.attachment_close);
//        ImageButton addButton = (ImageButton) findViewById(R.id.addrequester_button);
//        addButton.setOnClickListener(new View.OnClickListener()
//                                     {
//                                         @Override
//                                         public void onClick(View v) {
//
//                                             CustomBottomSheetDialog bottomSheetDialog = new CustomBottomSheetDialog();
//                                             bottomSheetDialog.show(getSupportFragmentManager(), "Custom Bottom Sheet");
//
//                                         }
//                                     }
//
//        );
//        attchmentClose.setOnClickListener(new View.OnClickListener()
//
//                                          {
//                                              @Override
//                                              public void onClick(View v) {
//                                                  attachment_layout.setVisibility(View.GONE);
//                                              }
//                                          }
//
//        );


        spinnerHelpArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, helptopicItems); //selected item will look like a spinner set from XML
        spinnerHelpArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoCompleteHelpTopic.setAdapter(spinnerHelpArrayAdapter);


//        spinnerStaffArrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,staffItems);
//        spinnerStaffArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerAssignto.setAdapter(spinnerStaffArrayAdapter);
        autocompletetextview = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,staffitemsauto);
        autoCompleteTextView.setAdapter(autocompletetextview);
        //autoCompleteTextView.setThreshold(0);
        //autoCompleteTextView.setDropDownWidth(1000);

//if (autoCompleteTextView.getThreshold()==0){
//    stringArrayAdapterHint = new ArrayAdapter<>
//            (this, android.R.layout.simple_dropdown_item_1line,staffItemsHint);
//    autoCompleteTextView.setAdapter(autocompletetextview);
//    autoCompleteTextView.setThreshold(1);
//    autoCompleteTextView.setDropDownWidth(1000);
//}


        //Define the AutoComplete DropDown width


//        spinnerSlaArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Utils.removeDuplicates(SplashActivity.valueSLA.split(","))); //selected item will look like a spinner set from XML
//        spinnerSlaArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerSLA.setAdapter(spinnerSlaArrayAdapter);
//
//        spinnerAssignToArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Utils.removeDuplicates(SplashActivity.valueDepartment.split(","))); //selected item will look like a spinner set from XML
//        spinnerAssignToArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerDept.setAdapter(spinnerAssignToArrayAdapter);

        spinnerPriArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, priorityItems); //selected item will look like a spinner set from XML
        spinnerPriArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoCompletePriority.setAdapter(spinnerPriArrayAdapter);


        autoCompletePriority.setDropDownWidth(1000);

//
        // editTextFirstName.addTextChangedListener(mTextWatcher);
        editTextLastName.setFilters(new InputFilter[]{filter});
        editTextFirstName.setFilters(new InputFilter[]{filter});
        subEdittext.setFilters(new InputFilter[]{filter});
//        subEdittext.setFilters(new InputFilter[]{
//                new InputFilter() {
//                    public CharSequence filter(CharSequence src, int start,
//                                               int end, Spanned dst, int dstart, int dend) {
//                        if (src.equals("")) { // for backspace
//                            return src;
//                        }
//                        if (src.toString().matches("[\\x00-\\x7F]+")) {
//                            return src;
//                        }
//                        return "";
//                    }
//                }
//        });

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
                    if (!MainActivity.isShowing) {
                        Log.d("isShowing", "false");
                        Intent intent = new Intent(CreateTicketActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Log.d("isShowing", "true");
                        Intent intent = new Intent(CreateTicketActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
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
        else {
            if (!MainActivity.isShowing) {
                Log.d("isShowing", "false");
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Log.d("isShowing", "true");
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }



    }
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        //replaces the default 'Back' button action
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            finish();
//        }
//        return true;
//    }

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
        email1 = editTextEmail.getText().toString();
        firstname=editTextFirstName.getText().toString();
        lastname=editTextLastName.getText().toString();
        Log.d("emialwithname", email1);
        if (!email1.equals("")) {
            email2 = email1;
        } else if (email1.contains("<")){
            int pos=email1.indexOf("<");
            int pos1=email1.lastIndexOf(">");
            email2=email1.substring(pos,pos1+1);
            Log.d("email2",email2);
        }
        else{
            allCorrect = false;
            Toasty.info(this, getString(R.string.requestornotfound), Toast.LENGTH_SHORT).show();
            return;
        }

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


//        String newColla=collaborator.replaceAll(" ","");
//        Log.d("colla",newColla);
//
////        if (collaborator.charAt(newColla.length()-1)==','){
////            Log.d("lastTerm","commaIncluded");
////
////
////
////        }
////        else{
////            Log.d("lastTerm","commaExcluded");
////        }
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
        String fname = editTextFirstName.getText().toString();
        String lname = editTextLastName.getText().toString();
        String phone = editTextPhone.getText().toString();
        mobile = editTextMobile.getText().toString();


//        if (!phCode.getSelectedItem().toString().equals("Code")) {
//            countrycode = phCode.getSelectedItem().toString();
//            String[] cc = countrycode.split(",");
//            countrycode = cc[1];
//        }

        countrycode = countryCodePicker.getSelectedCountryCode();


        allCorrect = true;


//    if (phCode.equals("")){
//        Toast.makeText(this, "Select the code", Toast.LENGTH_SHORT).show();
//    }
        if (fname.length()==0&&firstname.length()==0){
            Toasty.warning(this, getString(R.string.fill_firstname), Toast.LENGTH_SHORT).show();
            allCorrect = false;
        }
        if (fname.length() == 0 && email2.length() == 0 && subject.length() == 0 && message.length() == 0 && helpTopic.ID == 0 && priority.ID == 0) {
            Toasty.warning(this, getString(R.string.fill_all_the_details), Toast.LENGTH_SHORT).show();
            allCorrect = false;
        } else if (fname.trim().length() == 0||fname.equals("null")||fname.equals(null)) {
            Toasty.warning(this, getString(R.string.fill_firstname), Toast.LENGTH_SHORT).show();
            allCorrect = false;
        } else if (fname.trim().length() < 2) {
            Toasty.warning(this, getString(R.string.firstname_minimum_char), Toast.LENGTH_SHORT).show();
            allCorrect = false;
        } else if (fname.length() > 30) {
            Toasty.warning(this, getString(R.string.firstname_maximum_char), Toast.LENGTH_SHORT).show();
            allCorrect = false;
        } else if (email2.trim().length() == 0 || !Helper.isValidEmail(email2)) {
            Toasty.warning(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();
            allCorrect = false;
        } else if (subject.trim().length() == 0) {
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
                        phone = URLEncoder.encode(phone.trim(), "utf-8");
                        mobile = URLEncoder.encode(mobile.trim(), "utf-8");

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
                    final String finalPhone = phone;
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
                                new CreateNewTicket(Integer.parseInt(Prefs.getString("ID", null)), finalSubject, finalMessage, helpTopic.ID, priority.ID, finalPhone, finalFname, finalLname, email2, countrycode, staff.ID, mobile).execute();
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
                                                .addParameter("phone", finalPhone1)
                                                .addParameter("code", countrycode)
                                                .addParameter("mobile", mobile)
                                                .addArrayParameter("cc[]", finalFirst_user)
                                                .addParameter("email", email2)
                                                //.addParameter("cc[]", String.valueOf(Arrays.asList("sayar@gmail.com","demoadmin@gmail.com")))
                                                //Adding text parameter to the request
                                                //.setNotificationConfig(new UploadNotificationConfig())
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
//                                    if (serverResponse.getBodyAsString().contains("Ticket created successfully!")) {
//                                        Toasty.success(CreateTicketActivity.this, getString(R.string.ticket_created_success), Toast.LENGTH_LONG).show();
//                                        finish();
//                                        editTextEmail.setText("");
//                                        id = 0;
//                                        Prefs.putString("newuseremail", null);
//                                        startActivity(new Intent(CreateTicketActivity.this, MainActivity.class));
//
//                                    }

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
                                                .addParameter("phone", finalPhone1)
                                                .addParameter("code", countrycode)
                                                .addParameter("mobile", mobile)
                                                .addArrayParameter("cc[]", finalFirst_user)
                                                .addArrayParameter("cc[]", finalSecond_user)
                                                .addParameter("email", email2)
                                                //.addParameter("cc[]", String.valueOf(Arrays.asList("sayar@gmail.com","demoadmin@gmail.com")))
                                                //Adding text parameter to the request
                                                //.setNotificationConfig(new UploadNotificationConfig())
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
                                                .addParameter("phone", finalPhone1)
                                                .addParameter("code", countrycode)
                                                .addParameter("mobile", mobile)
                                                .addArrayParameter("cc[]", finalFirst_user)
                                                .addArrayParameter("cc[]", finalSecond_user)
                                                .addArrayParameter("cc[]", finalThird_user)
                                                .addParameter("email", email2)
                                                //.addParameter("cc[]", String.valueOf(Arrays.asList("sayar@gmail.com","demoadmin@gmail.com")))
                                                //Adding text parameter to the request
                                                //.setNotificationConfig(new UploadNotificationConfig())
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
                                                .addParameter("phone", finalPhone1)
                                                .addParameter("code", countrycode)
                                                .addParameter("mobile", mobile)
                                                .addParameter("email", email2)
                                                //.addParameter("cc[]", String.valueOf(Arrays.asList("sayar@gmail.com","demoadmin@gmail.com")))
                                                //Adding text parameter to the request
                                                //.setNotificationConfig(new UploadNotificationConfig())
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

                    }
                }


                //Creating a multi part request


//                try {
//                    uploadMultipartData();
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
            } else {
                Toasty.info(this, getString(R.string.oops_no_internet), Toast.LENGTH_SHORT, true).show();
            }
        }
//                progressDialog = new ProgressDialog(CreateTicketActivity.this);
//                progressDialog.setMessage(getString(R.string.creating_ticket));
//
//
//
//                try {
//                    fname = URLEncoder.encode(fname.trim(), "utf-8");
//                    lname = URLEncoder.encode(lname.trim(), "utf-8");
//                    subject = URLEncoder.encode(subject.trim(), "utf-8");
//                    message = URLEncoder.encode(message.trim(), "utf-8");
//                    email1 = URLEncoder.encode(email1.trim(), "utf-8");
//                    phone = URLEncoder.encode(phone.trim(), "utf-8");
//                    mobile = URLEncoder.encode(mobile.trim(), "utf-8");
//
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                    progressDialog.show();
        //new CreateNewTicket(Integer.parseInt(Prefs.getString("ID", null)), subject, message, helpTopic.ID, priority.ID, phone, fname, lname, email2, countrycode, staff.ID, mobile ).execute();
//            }
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
        //Toast.makeText(CreateTicketActivity.this, "Permission Received", Toast.LENGTH_SHORT).show();
        Log.d("requestCode",""+requestCode);

        if (document==1){
            Intent intent4 = new Intent(this, NormalFilePickActivity.class);
            intent4.putExtra(Constant.MAX_NUMBER, 1);
            //intent4.putExtra(IS_NEED_FOLDER_LIST, true);
            intent4.putExtra(NormalFilePickActivity.SUFFIX,
                    new String[] {"xlsx", "xls", "doc", "dOcX", "ppt", ".pptx", "pdf"});
            startActivityForResult(intent4, Constant.REQUEST_CODE_PICK_FILE);
//            new MaterialFilePicker()
//                    .withActivity(TicketReplyActivity.this)
//                    .withRequestCode(FILE_PICKER_REQUEST_CODE)
//                    .withHiddenFiles(true)
//                    .start();
            document=0;
        }
        if (gallery==2){
            Intent intent1 = new Intent(this, ImagePickActivity.class);
            intent1.putExtra(IS_NEED_CAMERA, true);
            intent1.putExtra(Constant.MAX_NUMBER, 1);
            startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);
//            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            intent.setType("image/*");
//            startActivityForResult(intent, PICKFILE_REQUEST_CODE);
            gallery=0;
        }

        if (camera==3){
            Intent intent2 = new Intent(this, VideoPickActivity.class);
            intent2.putExtra(IS_NEED_CAMERA, true);
            intent2.putExtra(Constant.MAX_NUMBER, 1);
            startActivityForResult(intent2, Constant.REQUEST_CODE_PICK_VIDEO);
//            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//
//            startActivityForResult(intent, CAMERA_REQUEST);
            camera=0;
        }
        if (audio==4){
            Intent intent3 = new Intent(this, AudioPickActivity.class);
            intent3.putExtra(IS_NEED_RECORDER, true);
            intent3.putExtra(Constant.MAX_NUMBER, 1);
            startActivityForResult(intent3, Constant.REQUEST_CODE_PICK_AUDIO);
            audio=0;

        }
//        if (audio==4){
//            Intent intent;
//            intent = new Intent();
//            intent.setType("audio/mp3");
//            startActivityForResult(intent,PICKFILE_REQUEST_CODE);
//            audio=0;
//
//        }
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, CAMERA_REQUEST);
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
        String fname, lname, email, code;
        String subject;
        public String body;
        String phone;
        String mobile;
        int helpTopic;
        // int SLA;
        int priority;
        //int dept;
        int userID;
        int staff;
        String string;

        CreateNewTicket(int userID, String subject, String body,
                        int helpTopic, int priority, String phone, String fname, String lname, String email, String code,int staff,String mobile) {

            this.subject = subject;
            this.body = body;
            this.helpTopic = helpTopic;
            //this.SLA = SLA;
            this.priority = priority;
            //this.dept = dept;
            this.userID = userID;
            this.phone = phone;
            this.lname = lname;
            this.fname = fname;
            this.email = email;
            this.code = code;
            this.staff=staff;
            this.mobile = mobile;

        }

//        protected String doInBackground(String... urls) {
//
        //return new Helpdesk().postCreateTicket(userID, subject, body, helpTopic, priority, fname, lname, phone, email, code, staff, mobile+ collaborators, new File[]{new File(result)});
//        }

        @Override
        protected String doInBackground(File... files) {


            return new Helpdesk().postCreateTicket(userID, subject, body, helpTopic, priority, fname, lname, phone, email, code, staff, mobile+ collaborators);
        }

        protected void onPostExecute(String result) {
            //Toast.makeText(CreateTicketActivity.this, "api called", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            id=0;
            collaborators=null;
            if (result == null) {
                Toasty.error(CreateTicketActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }


//            try{
//                JSONObject jsonObject=new JSONObject(result);
//                JSONObject jsonObject1=jsonObject.getJSONObject("response");
//                String message=jsonObject1.getString("message");
//                if (message.equals("Permission denied, you do not have permission to access the requested page.")){
//                    Toasty.warning(CreateTicketActivity.this, getString(R.string.permission), Toast.LENGTH_LONG).show();
//                    Prefs.putString("403", "null");
//                    return;
//                }
//
//            }catch (JSONException e){
//                e.printStackTrace();
//            }
            String state=Prefs.getString("403",null);
////                if (message1.contains("The ticket id field is required.")){
////                    Toasty.warning(TicketDetailActivity.this, getString(R.string.please_select_ticket), Toast.LENGTH_LONG).show();
////                }
////                else if (message1.contains("The status id field is required.")){
////                    Toasty.warning(TicketDetailActivity.this, getString(R.string.please_select_status), Toast.LENGTH_LONG).show();
////                }
////               else
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
                // JSONArray jsonArray=jsonObject1.getJSONArray("code");
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
//        progressDialog.dismiss();
//        progressDialog=null;
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

    //    private TextWatcher mTextWatcher = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
//        }
//
//        @Override
//        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
//
//            if ((subEdittext.getText().toString()).matches("\\[a-zA-Z]+")) {
//
//
//            }
//        }
//
//        @Override
//        public void afterTextChanged(Editable editable) {
//            // check Fields For Empty Values
//            //checkFieldsForEmptyValues();
//        }
//    };
    TextWatcher passwordWatcheredittextSubject = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            //Toast.makeText(TicketSaveActivity.this, "API called", Toast.LENGTH_SHORT).show();
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String term = editTextEmail.getText().toString();
            if (InternetReceiver.isConnected()) {
                if (!term.equals("")&&term.length()==3){

                    //progressBar.setVisibility(View.VISIBLE);
//                    refresh.startAnimation();
                    refresh.startAnimation(rotation);
                    String newTerm=term;
                    arrayAdapterCC=new CollaboratorAdapter(CreateTicketActivity.this,emailHint);
                    //arrayAdapterCC = new ArrayAdapter<Data>(CreateTicketActivity.this, android.R.layout.simple_dropdown_item_1line, emailHint);
                    new FetchCollaborator(newTerm.trim()).execute();

                    //stringArrayAdapterCC.notifyDataSetChanged();
//                autoCompleteTextViewCC.setThreshold(0);
//                autoCompleteTextViewCC.setDropDownWidth(1000);

                }


                //buttonsave.setEnabled(true);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    };

    TextWatcher ccedittextwatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            //Toast.makeText(TicketSaveActivity.this, "API called", Toast.LENGTH_SHORT).show();
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
                            //progressBar.setVisibility(View.VISIBLE);
                            //refresh.startAnimation();
                            refresh.startAnimation(rotation);
                            //arrayAdapterCollaborator = new ArrayAdapter<>(CreateTicketActivity.this, android.R.layout.simple_dropdown_item_1line, stringArraylist);
                            new FetchCollaboratorCC(term.trim()).execute();
                        }

                    }
                    else if (term.equals("")) {
                        adapter1 = new MultiCollaboratorAdapter(CreateTicketActivity.this, stringArraylist);
                        //arrayAdapterCollaborator = new ArrayAdapter<>(CreateTicketActivity.this, android.R.layout.simple_dropdown_item_1line, stringArraylist);
                        //new FetchCollaborator("s").execute();
                        //Data data=new Data(0,"No result found");

//                autoCompleteTextViewCC.setAdapter(stringArrayAdapterCC);
//                stringArrayAdapterCC.notifyDataSetChanged();
//                autoCompleteTextViewCC.setThreshold(0);
//                autoCompleteTextViewCC.setDropDownWidth(1000);

                    } else if (term.length()==3){
                        adapter1 = new MultiCollaboratorAdapter(CreateTicketActivity.this, stringArraylist);
                        //progressBar.setVisibility(View.VISIBLE);
//                        refresh.startAnimation();
                        refresh.startAnimation(rotation);

                        //arrayAdapterCollaborator = new ArrayAdapter<>(CreateTicketActivity.this, android.R.layout.simple_dropdown_item_1line, stringArraylist);
                        new FetchCollaboratorCC(term).execute();
                        //multiAutoCompleteTextViewCC.setAdapter(adapter1);


                        //stringArrayAdapterCC.notifyDataSetChanged();
//                autoCompleteTextViewCC.setThreshold(0);
//                autoCompleteTextViewCC.setDropDownWidth(1000);

                    }


                    //buttonsave.setEnabled(true);
                }

        }
        //String[] cc=[sayarsamanta@gmail.com,demoadmin@gmail.com,demopass@gmail.com]
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
//            refresh.stopAnimation();
//            Bitmap icon = BitmapFactory.decodeResource(getResources(),
//                    R.drawable.ic_refresh_black_24dp);
//            refresh.doneLoadingAnimation(getResources().getColor(R.color.faveo),icon);
            //progressBar.setVisibility(View.GONE);
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
                    //Toast.makeText(TicketSaveActivity.this, "email:"+email, Toast.LENGTH_SHORT).show();
                    CollaboratorSuggestion collaboratorSuggestion=new CollaboratorSuggestion(id1,first_name,last_name,email,profilePic);
                    //Data data = new Data(id1,first_name + " " + last_name + " <" + email + ">");
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

//            if (progressDialog.isShowing())
//                progressDialog.dismiss();

//            if (result == null) {
//                Toasty.error(collaboratorAdd.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
//                return;
//            }

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

//                        Set<String> stringSet=new HashSet<>(stringArraylist);
//                        stringArraylist.clear();
//                        stringArraylist.addAll(stringSet);


                        // Prefs.putString("noUser","1");
                    }
                    multiAutoCompleteTextViewCC.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
                    multiAutoCompleteTextViewCC.setThreshold(3);
                    multiAutoCompleteTextViewCC.setDropDownWidth(1500);
                    multiAutoCompleteTextViewCC.setAdapter(adapter1);
                    multiAutoCompleteTextViewCC.showDropDown();
                    //multiAutoCompleteTextViewCC.setAdapter(adapter1);



//                    for (int i=0;i<stringArraylist.size();i++){
//                        if (stringArraylist.contains(emailfromsuggestion)){
//                            stringArraylist.remove(emailfromsuggestion);
//                        }
//                    }

                }

            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
            }


        }
    }
    private class FetchDependency extends AsyncTask<String, Void, String> {
        String unauthorized;

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

//                JSONArray jsonArrayStaffs = jsonObject1.getJSONArray("staffs");
//                for (int i = 0; i < jsonArrayStaffs.length(); i++) {
//                    keyStaff += jsonArrayStaffs.getJSONObject(i).getString("id") + ",";
//                    valueStaff += jsonArrayStaffs.getJSONObject(i).getString("email") + ",";
//                }


//                JSONArray jsonArrayTeams = jsonObject1.getJSONArray("teams");
//                for (int i = 0; i < jsonArrayTeams.length(); i++) {
//                    keyTeam += jsonArrayTeams.getJSONObject(i).getString("id") + ",";
//                    valueTeam += jsonArrayTeams.getJSONObject(i).getString("name") + ",";
//                }

                //Set<String> keyPri = new LinkedHashSet<>();
                // Set<String> valuePri = new LinkedHashSet<>();
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