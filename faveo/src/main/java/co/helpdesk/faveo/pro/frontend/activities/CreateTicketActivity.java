package co.helpdesk.faveo.pro.frontend.activities;

import android.Manifest;
import android.app.Activity;
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
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.widget.CursorAdapter;
//import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
//import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.hbb20.CountryCodePicker;
import com.kishan.askpermission.AskPermission;
import com.kishan.askpermission.ErrorCallback;
import com.kishan.askpermission.PermissionCallback;
import com.kishan.askpermission.PermissionInterface;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.pixplicity.easyprefs.library.Prefs;
//import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import java.io.File;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
//import java.text.DecimalFormat;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
//import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.helpdesk.faveo.pro.BottomNavigationBehavior;
import co.helpdesk.faveo.pro.Helper;
import co.helpdesk.faveo.pro.MyDeserializer;
import co.helpdesk.faveo.pro.MyResponse;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.UserClient;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.pro.model.Data;
import co.helpdesk.faveo.pro.model.MessageEvent;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;

/**
 * This activity is for responsible for creating the ticket.
 * Here we are using create ticket async task which is
 * POST request.We are getting the JSON data here from the dependency API.
 */
public class CreateTicketActivity extends AppCompatActivity implements PermissionCallback, ErrorCallback {
    boolean allCorrect;
    String term;
    String collaborators=null;
    ArrayAdapter<Data> spinnerPriArrayAdapter, spinnerHelpArrayAdapter,spinnerStaffArrayAdapter,autocompletetextview,stringArrayAdapterHint,arrayAdapterCC;
    ArrayAdapter<Data> arrayAdapterCollaborator;
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
    @BindView(R.id.attachment_size)
    TextView attachmentFileSize;
    @BindView(R.id.attachment_layout)
    RelativeLayout attachment_layout;
    @BindView(R.id.attach_fileimgview)
    ImageView imageView;
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
    Button buttonUserCreate;
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
    ArrayList<Data> helptopicItems, priorityItems,staffItems,staffitemsauto,staffItemsHint,emailHint;
    int id=0;
    int id1=0;
    String email1,collaborator;
    ArrayList<Data> stringArraylist;
    String mobile="";
    String splChrs = "-/@#$%^&_+=()" ;
    String countrycode = "";
    int i=0;
    MultiAutoCompleteTextView multiAutoCompleteTextViewCC;
    CountryCodePicker countryCodePicker;
    String firstname,lastname,email,phone;
    ImageButton imageViewGallery,imageViewCamera,imageViewDocument,imageViewAudio;
    Toolbar toolbarAttachment;
    File file3;
    String result;
    Button button;
    File file;
    Thread t;
    int gallery,document,camera,audio=0;
    BottomNavigationView bottomNavigationView;
    String base64,fileName,fileSize,mimeType;
    private static final int CAMERA_REQUEST = 1888;
    private static final int PICKFILE_REQUEST_CODE = 1234;

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

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ImageButton imageButton= (ImageButton) findViewById( R.id.attachment_close);
        bottomNavigationView= (BottomNavigationView) findViewById(R.id.navigation);
        collaboratorArray=new ArrayList<>();
        //toolbarAttachment= (Toolbar) findViewById(R.id.bottom_navigation);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        toolbarBottom= (Toolbar) findViewById(R.id.bottom_navigation);
//        toolbarBottom.setVisibility(View.GONE);
        //imageViewAudio= (ImageButton) toolbarAttachment.findViewById(R.id.audio_img_btn);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // attaching bottom sheet behaviour - hide / show on scroll
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());
//        imageViewGallery= (ImageButton) toolbarAttachment.findViewById(R.id.gallery_img_btn);
//        imageViewCamera= (ImageButton) toolbarAttachment.findViewById(R.id.photo_img_btn);
//        imageViewDocument= (ImageButton) toolbarAttachment.findViewById(R.id.document);
        button= (Button) findViewById(R.id.attachment);

//        imageViewGallery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //int currentAPIVersion = Build.VERSION.SDK_INT;
//                    gallery=2;
//                    reqPermissionCamera();
//
//
////                else{
////                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
////                    intent.setType("image/*");
////                    startActivityForResult(intent, PICKFILE_REQUEST_CODE);
////                }
//
//
//
//            }
//        });
//        imageViewDocument.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent();
////                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
////                Uri uri = Uri.fromParts("package", getPackageName(), null);
////                intent.setData(uri);
////                startActivity(intent);
//                    document=1;
//                    reqPermissionCamera();
//
//
//
//
//            }
//        });
//
//        imageViewCamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                if (ContextCompat.checkSelfPermission(CreateTicketActivity.this, Manifest.permission.CAMERA)
////                        == PackageManager.PERMISSION_DENIED){
////                    Toasty.warning(CreateTicketActivity.this,"Permission Denied By The User",Toast.LENGTH_SHORT).show();
////                    return;
////                }
////                ActivityCompat.requestPermissions(CreateTicketActivity.this, new String[] {Manifest.permission.CAMERA}, CAMERA_REQUEST);
//
//                    camera=3;
//                    reqPermissionCamera();
//
//
//                //requestCameraPermission();
//
//            }
//        });
//    imageViewAudio.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            audio=4;
//            reqPermissionCamera();
//        }
//    });


        buttonUserCreate= (Button) findViewById(R.id.usercreate);
//        String emailregister=Prefs.getString("newuseremail",null);
//        try {
//            if (!emailregister.equals("")){
//                editTextEmail.setText(Prefs.getString("newuseremail",null));
//            }
//        }catch (NullPointerException e){
//            e.printStackTrace();
//        }
cc=new String[0];
cc1=new String[0];
        imageViewBack= (ImageView) findViewById(R.id.imageViewBack);
        multiAutoCompleteTextViewCC= (MultiAutoCompleteTextView) findViewById(R.id.collaborator);
        stringArraylist=new ArrayList<>();
        arrayAdapterCollaborator=new ArrayAdapter<>(CreateTicketActivity.this,android.R.layout.simple_dropdown_item_1line,stringArraylist);
        multiAutoCompleteTextViewCC.setDropDownWidth(1000);
        multiAutoCompleteTextViewCC.setThreshold(3);
        multiAutoCompleteTextViewCC.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        multiAutoCompleteTextViewCC.addTextChangedListener(ccedittextwatcher);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachment_layout.setVisibility(View.GONE);
                attachmentFileName.setText("");
                attachmentFileSize.setText("");
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
                Intent intent=new Intent(CreateTicketActivity.this,MainActivity.class);
                //editTextEmail.setText("");
                Prefs.putString("newuseremail","");
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bottomNavigationView.getVisibility()==View.GONE){
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }
                else if (bottomNavigationView.getVisibility()==View.VISIBLE){
                    bottomNavigationView.setVisibility(View.GONE);
                }


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
        arrayAdapterCC=new ArrayAdapter<Data>(CreateTicketActivity.this,android.R.layout.simple_dropdown_item_1line,emailHint);
        editTextEmail.setThreshold(2);
        editTextEmail.setDropDownWidth(1000);
        editTextEmail.addTextChangedListener(passwordWatcheredittextSubject);
        editTextEmail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                arrayAdapterCC = new ArrayAdapter<>(CreateTicketActivity.this, android.R.layout.simple_dropdown_item_1line, emailHint);
                String name1=editTextEmail.getText().toString();
                for (int j=0;i<emailHint.size();i++){
                    Data data = emailHint.get(j);
                    id = data.getID();
                    email1=data.getName();
                }

            }
        });


        //editTextEmail.requestFocus();

//        if (!Prefs.getString("newuseremail",null).equals("")||!Prefs.getString("newuseremail",null).equals(null)){
//            editTextEmail.setText(Prefs.getString("newuseremail",null));
//        }
//        else{
//            editTextEmail.setText("");
//            editTextEmail.requestFocus();
//        }


multiAutoCompleteTextViewCC.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
         emailfromsuggestion=adapterView.getItemAtPosition(i).toString();
        //Toast.makeText(CreateTicketActivity.this, "email is:"+emailfromsuggestion, Toast.LENGTH_SHORT).show();

        //collaboratorArray.add(emailfromsuggestion);
//        Log.d("EMAIL",email);
//        stringArraylist.remove(email);


    }
});



        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createButtonClick();
            }
        });
        //Toast.makeText(this, "For mobile no code is mandatory", Toast.LENGTH_LONG).show();

        JSONObject jsonObject;
        Data data;
        String json = Prefs.getString("DEPENDENCY", "");
        try {
            staffItems=new ArrayList<>();
            staffitemsauto=new ArrayList<>();
            staffitemsauto.add(new Data(0,"select assignee"));
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
            }

            helptopicItems = new ArrayList<>();
            helptopicItems.add(new Data(0, "Please select help topic"));
            jsonObject = new JSONObject(json);
            JSONArray jsonArrayHelpTopics = jsonObject.getJSONArray("helptopics");
            for (int i = 0; i < jsonArrayHelpTopics.length(); i++) {
                Data data1 = new Data(Integer.parseInt(jsonArrayHelpTopics.getJSONObject(i).getString("id")), jsonArrayHelpTopics.getJSONObject(i).getString("topic"));
                helptopicItems.add(data1);
            }

            JSONArray jsonArrayPriorities = jsonObject.getJSONArray("priorities");
            priorityItems = new ArrayList<>();
            priorityItems.add(new Data(0, "Please select the priority"));
            for (int i = 0; i < jsonArrayPriorities.length(); i++) {
                Data data2 = new Data(Integer.parseInt(jsonArrayPriorities.getJSONObject(i).getString("priority_id")), jsonArrayPriorities.getJSONObject(i).getString("priority"));
                priorityItems.add(data2);
            }
        } catch (JSONException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        autoCompleteTextView= (Spinner) findViewById(R.id.autocompletetext);

        autoCompleteHelpTopic= (Spinner) findViewById(R.id.spinner_help);
        autoCompletePriority= (Spinner) findViewById(R.id.spinner_pri);


//        autoCompleteTextView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                autocompletetextview = new ArrayAdapter<>(CreateTicketActivity.this, android.R.layout.simple_dropdown_item_1line, staffitemsauto);
////                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
////                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
//              //autoCompleteTextView.setThreshold(1);
//                autoCompleteTextView.showDropDown();
//                autoCompleteTextView.setDropDownWidth(1000);
////                String name=staffitemsauto.get(pos).toString();
////                Data data=staffItems.get(pos);
////                id=data.getID();
////                //Toast.makeText(CreateTicketActivity.this, ""+id, Toast.LENGTH_SHORT).show();
//////                if (name.equals(autocompletetextview.getItem(pos).getName())){
//////                    Toast.makeText(CreateTicketActivity.this, ""+autocompletetextview.getItem(pos).getID(), Toast.LENGTH_SHORT).show();
//////                }
//////                id=autocompletetextview.getItem(pos).getID();
//////                String name=autocompletetextview.getItem(i).getName();
//////                Log.d("ID",""+id);
////                Log.d("name",""+name);
////                Log.d("id",""+id);
//                return false;
//            }
//        });
//autoCompleteTextView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//    @Override
//    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//        autocompletetextview = new ArrayAdapter<>(CreateTicketActivity.this, android.R.layout.simple_dropdown_item_1line, staffitemsauto);
//        Data data=staffitemsauto.get(i);
//        id=data.getID();
//        Toast.makeText(CreateTicketActivity.this, ""+id, Toast.LENGTH_SHORT).show();
//        Log.d("assigneeid",""+id);
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> adapterView) {
//
//    }
//});

//        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                autocompletetextview = new ArrayAdapter<>(CreateTicketActivity.this, android.R.layout.simple_dropdown_item_1line, staffitemsauto);
//                String name1=autoCompleteTextView.getText().toString();
//                for (int j = 0; j < staffitemsauto.size(); j++) {
//                    if (staffitemsauto.get(j).getName().equalsIgnoreCase(name1)) {
//                        Data data = staffitemsauto.get(j);
//                        id = data.getID();
//                        //Toast.makeText(CreateTicketActivity.this, "id:" + id, Toast.LENGTH_SHORT).show();
//                    }
//                }
//
////                Data data=staffitemsauto.get(i);
////                String name=autoCompleteTextView.getText().toString();
////                if (staffitemsauto.contains(name)){
////
////                }
//               //Toast.makeText(CreateTicketActivity.this, "Data:"+name1, Toast.LENGTH_SHORT).show();
//
////                String name =autoCompleteTextView.getText().toString();
////                if (name.equals(autocompletetextview.getItem(i).getName())){
////                    id=autocompletetextview.getItem(i).getID();
////                    Toast.makeText(CreateTicketActivity.this, ""+autocompletetextview.getItem(i).getID(), Toast.LENGTH_SHORT).show();
////                }
////                id=autocompletetextview.getItem(i).getID();
////                String name=autocompletetextview.getItem(i).getName();
////                Log.d("ID",""+id);
//                //Log.d("name",""+name);
//
//            }
//
//
//        });


//

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
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_shop:
                    gallery=2;
                    reqPermissionCamera();
                    return true;
                case R.id.navigation_gifts:
                    camera=3;
                    reqPermissionCamera();
                    return true;
                case R.id.navigation_cart:
                    document=1;
                    reqPermissionCamera();
                    return true;

            }

            return false;
        }
    };


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

        Uri uri=data.getData();

        Uri uri1 = null;
        String path=getPath(CreateTicketActivity.this,uri);
//        Log.d("PATH",path);
        byte[] buf;


//        try {
//            assert path != null;
//
//            uri1=Uri.fromFile(new File(path));
//            file = new File(String.valueOf(uri1));
//            InputStream file1=getContentResolver().openInputStream(uri1);
//            //FileInputStream fileInputStream = new FileInputStream(String.valueOf(file1));
//            //result = IOUtils.toString(file1, StandardCharsets.UTF_8);
//            Log.d("file",result);
//            Log.d("File",file+"");
//            Log.d("URI",""+uri1);
//            buf=getBytesFromFile(file);
//            //base64 = Base64.encodeToString(buf,Base64.DEFAULT);
//            //Log.d("base64",base64);
//        }catch (NullPointerException |IOException e){
//            e.printStackTrace();
//        }
        mimeType = getContentResolver().getType(uri);

        fileName=getFileName(uri);
        uploadFile(uri);
//        final Uri returnUri = data.getData();
//        Cursor returnCursor =
//                getContentResolver().query(returnUri, null, null, null, null);
//    /*
//     * Get the column indexes of the data in the Cursor,
//     * move to the first row in the Cursor, get the data,
//     * and display it.
//     */
//        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
//        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
//        returnCursor.moveToFirst();
//        //Log.d("NAME",returnCursor.getString(nameIndex));
//        //Log.d("SiZE",Long.toString(returnCursor.getLong(sizeIndex)));
//        Long bytes= returnCursor.getLong(sizeIndex);
//        long kibibytes = bytes / 1024;
//        Log.d("MIMETYPE",mimeType);
//        fileSize= String.valueOf(kibibytes);
//        Log.d("sizeInKB",""+kibibytes);
//        File file = new File(path);
//        Log.d("FileName", "Filename " + file.getName());
//        t = new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                File f  = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
//                String content_type  = getMimeType(returnUri);
//
//                String file_path = f.getAbsolutePath();
//                OkHttpClient client = new OkHttpClient();
//                RequestBody file_body = RequestBody.create(MediaType.parse(content_type),f);
//
//                RequestBody request_body = new MultipartBody.Builder()
//                        .setType(MultipartBody.FORM)
//                        .addFormDataPart("type",content_type)
//                        .addFormDataPart("media_attachment[]",file_path.substring(file_path.lastIndexOf("/")+1), file_body)
//                        .build();
//
//                Request request = new Request.Builder()
//                        .url("http://jamboreebliss.com/sayarnew/public/api/v1/helpdesk/create")
//                        .post(request_body)
//                        .build();
//
//                try {
//                    Response response = client.newCall(request).execute();
//
//                    if(!response.isSuccessful()){
//                        throw new IOException("Error : "+response);
//                    }
//
//                    //progress.dismiss();
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        });
        //t.start();
        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
//        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
//        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(UserClient.BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        UserClient uploadImage = retrofit.create(UserClient.class);
//        Call<UploadObject> fileUpload = uploadImage.uploadImage(fileToUpload, filename);
//        fileUpload.enqueue(new Callback<UploadObject>() {
//            @Override
//            public void onResponse(Call<UploadObject> call, Response<UploadObject> response) {
//                Toast.makeText(CreateTicketActivity.this, "Response " + response.raw().message(), Toast.LENGTH_LONG).show();
//                Toast.makeText(CreateTicketActivity.this, "Success " + response.body().getSuccess(), Toast.LENGTH_LONG).show();
//            }
//            @Override
//            public void onFailure(Call<UploadObject> call, Throwable t) {
//                Log.d("", "Error " + t.getMessage());
//            }
//        });
    }

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
    public static byte[] getBytesFromFile(File file) throws IOException {
        // Get the size of the file
        long length = file.length();

        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            // File is too large
            throw new IOException("File is too large!");
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;

        InputStream is = new FileInputStream(file);
        try {
            while (offset < bytes.length
                    && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
                offset += numRead;
            }
        } finally {
            is.close();
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }
        return bytes;
    }
    private void uploadFile(Uri fileUri) {
        File file = null;

        RequestBody requestBody=RequestBody.create(MultipartBody.FORM, "subject");

        File originalFile=FileUtils.getFile(fileName);
       try {
           file = new File(getPathFromUri(this,fileUri));
       }catch (NullPointerException e){
           e.printStackTrace();
       }
        RequestBody filePart=RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)),file);
//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();
        final Gson gson =
                new GsonBuilder()
                        .registerTypeAdapter(MyResponse.class, new MyDeserializer())
                        .create();
        MultipartBody.Part part=MultipartBody.Part.createFormData("media_attachment",originalFile.getName(),filePart);
        Retrofit.Builder builder=new Retrofit.Builder().baseUrl("http://jamboreebliss.com/")
                .addConverterFactory(GsonConverterFactory.create(gson));

        Retrofit retrofit=builder.build();
        UserClient client=retrofit.create(UserClient.class);


        Call<ResponseBody> responseBodyCall=client.createTicket(requestBody,part);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                Log.d("status",""+response.code());
                Log.d("URL",""+call.request().url());
                Log.d("Call request header", call.request().headers().toString());
                Log.d("Response raw header", response.headers().toString());
                Log.d("Response raw", String.valueOf(response.raw().body()));
                Log.e("TAG", "response 33: "+new Gson().toJson(response.body()) );
                //MyResponse c = gson.fromJson(myJson, MyResponse.class);
//                if(response.isSuccessful()) {
//                    //showResponse(response.body().toString());
//                    Log.i("ddddd", "post submitted to API." +new Gson().toJson(response.body()));
//                }
//                else{
//                    try {
//                        Log.i("ddddd", "post submitted to API." +new Gson().toJson(response.body()));
//                    }catch (NullPointerException e){
//                        e.printStackTrace();
//                    }
//                    Log.i("responseFailure","true");
//                }
                //Log.d("jsonObject",response.body().getMessage());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("URL",""+call.request().url());
                Log.d("Error",t.toString());

            }
        });
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

    }



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


//    public String getRealPathFromURI (Uri contentUri) {
//        String path = null;
//        String[] proj = { MediaStore.MediaColumns.DATA };
//        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
//        if (cursor.moveToFirst()) {
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//            path = cursor.getString(column_index);
//        }
//        cursor.close();
//        return path;
//    }
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

//    public String getRealPathFromURI(Uri contentUri, Context activity) {
//        String path = null;
//        try {
//            final String[] proj = {MediaStore.MediaColumns.DATA};
//            final Cursor cursor = ((Activity) activity).managedQuery(contentUri, proj, null, null, null);
//            final int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//            cursor.moveToFirst();
//            path = cursor.getString(column_index);
//        } catch (Exception e) {
//        }
//        if (path != null && path.length() > 0) {
//            return path;
//        } else return contentUri.getPath();
//    }
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

    public String getMimeType(Uri uri) {

        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = getApplicationContext().getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        Log.d("MimeType",mimeType);
        return mimeType;
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
//                String uriString = getPath(selectedFile);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //replaces the default 'Back' button action
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return true;
    }

    /**
     * Handling the create button here.
     */
    public void createButtonClick() {
        String subject = subEdittext.getText().toString();
        String message = msgEdittext.getText().toString();
        email1 = editTextEmail.getText().toString();
        Log.d("emialwithname",email1);

        if (!email1.equals("")&&email1.contains("<")){
            int pos=email1.indexOf("<");
            int pos2=email1.lastIndexOf(">");
            email2=email1.substring(pos+1,pos2);
        }
        else if (!email1.contains("<")){
            email2=editTextEmail.getText().toString();
        }
        else{
            allCorrect=false;
            Toasty.info(this,getString(R.string.requestornotfound),Toast.LENGTH_SHORT).show();
            return;
        }

        collaborator=multiAutoCompleteTextViewCC.getText().toString();
        if (!collaborator.equals("")) {
        if (collaborator.contains("<")) {

                //collaboratorFinal = collaborator.replaceAll("\\s+,$", "");
                //collaboratorFinal = collaborator.replaceAll(" ", "");
                //Toast.makeText(this, "emails are :" + collaboratorFinal, Toast.LENGTH_LONG).show();
                //Log.d("emails", collaboratorFinal);

                cc = collaborator.split(",");
                sb = new StringBuilder();

                for (int i = 0; i < cc.length; i++) {
                    String one = cc[i].toString();
                    int pos = one.indexOf("<");
                    int pos2 = one.lastIndexOf(">");
                    try {
                        String two = one.substring(pos + 1, pos2);
                        sb.append(two + ",");
                        //String three=sb.toString();

                        //cc=three.split(",");

                    } catch (StringIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }
                cc1 = sb.toString().split(",");
                sb1 = new StringBuilder();
                for (String n : cc1) {
                    sb1.append("&cc[]=");
                    sb1.append(n);
                }
            collaborators=sb1.toString();
            }
        else{
            Toasty.info(this,getString(R.string.collaboratorExisting),Toast.LENGTH_SHORT).show();
            allCorrect=false;
            return;
        }
        }


//        Toast.makeText(this, "Sending emails :"+sb1.toString(), Toast.LENGTH_LONG).show();
        //Toast.makeText(this, "final email:"+sb.toString(), Toast.LENGTH_SHORT).show();
        //collaboratorArray.clear();
        //Toast.makeText(this, "collaborators are:"+sb.toString(), Toast.LENGTH_LONG).show();
        //ArrayList aList = new ArrayList(Arrays.asList(collaboratorFinal.split(",")));
//        for (int i = 0; i < aList.size(); i++) {
//            //Toast.makeText(TicketFilter.this, "staffs:"+"a-"+aList.get(i), Toast.LENGTH_SHORT).show();
//            collaboratorArray.add("&cc[]=" + aList.get(i));
//        }


        //Toast.makeText(this, "collaborators finals are:"+sb.toString(), Toast.LENGTH_LONG).show();



//        collaboratorFinal=collaborator.replaceAll("\\s+,$", "");
//        collaboratorFinal = collaborator.replaceAll(" ", "");
//         collaboratorArray = Arrays.asList(collaboratorFinal.split("\\s*,\\s*"));
        //Toast.makeText(this, "collaborators are :"+collaboratorArray.toString(), Toast.LENGTH_SHORT).show();

//        if (finalEmail.equals("")){
//            email=editTextEmail.getText().toString();
//        }
        String fname = editTextFirstName.getText().toString();
        String lname = editTextLastName.getText().toString();
        String phone = editTextPhone.getText().toString();
        mobile = editTextMobile.getText().toString();


//        if (!phCode.getSelectedItem().toString().equals("Code")) {
//            countrycode = phCode.getSelectedItem().toString();
//            String[] cc = countrycode.split(",");
//            countrycode = cc[1];
//        }

        countrycode=countryCodePicker.getSelectedCountryCode();


        allCorrect = true;


        Data helpTopic = (Data) autoCompleteHelpTopic.getSelectedItem();
//        Log.d("ID of objt", "" + helpTopic.ID);
        //  int SLAPlans = spinnerSLA.getSelectedItemPosition();
        //int dept = spinnerDept.getSelectedItemPosition();
        Data priority = (Data) autoCompletePriority.getSelectedItem();
        Data staff= (Data) autoCompleteTextView.getSelectedItem();

//    if (phCode.equals("")){
//        Toast.makeText(this, "Select the code", Toast.LENGTH_SHORT).show();
//    }

        if (fname.length()==0&&email2.length()==0&&subject.length()==0&&message.length()==0&& helpTopic.ID == 0&&priority.ID == 0){
            Toasty.warning(this,getString(R.string.fill_all_the_details),Toast.LENGTH_SHORT).show();
            allCorrect=false;
        }

        else if (fname.trim().length() == 0) {
            Toasty.warning(this, getString(R.string.fill_firstname), Toast.LENGTH_SHORT).show();
            allCorrect = false;
        } else if (fname.trim().length() < 3) {
            Toasty.warning(this, getString(R.string.firstname_minimum_char), Toast.LENGTH_SHORT).show();
            allCorrect = false;
        }
        else if (fname.length()>20){
            Toasty.warning(this, getString(R.string.firstname_maximum_char), Toast.LENGTH_SHORT).show();
            allCorrect=false;
        }   else if (email2.trim().length() == 0 || !Helper.isValidEmail(email2)) {
            Toasty.warning(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();
            allCorrect = false;
        }
        else if (subject.trim().length() == 0) {
            Toasty.warning(this, getString(R.string.sub_must_not_be_empty), Toast.LENGTH_SHORT).show();
            allCorrect = false;
        }
        else if (subject.trim().length() < 5) {
            Toasty.warning(this, getString(R.string.sub_minimum_char), Toast.LENGTH_SHORT).show();
            allCorrect = false;
        }
        else if (subject.matches("[" + splChrs + "]+")){
            Toasty.warning(this, getString(R.string.only_special_characters_not_allowed_here), Toast.LENGTH_SHORT).show();
            allCorrect=false;
        }
        else if (subject.trim().length()>100){
            Toasty.warning(this,"Subject must not exceed 150 characters"
                    , Toast.LENGTH_SHORT).show();
            allCorrect=false;
        }
        else if (priority.ID == 0) {
            allCorrect = false;
            Toasty.warning(CreateTicketActivity.this, getString(R.string.please_select_some_priority), Toast.LENGTH_SHORT).show();
        }
        else if (helpTopic.ID == 0) {
            allCorrect = false;
            Toasty.warning(CreateTicketActivity.this, getString(R.string.select_some_helptopic), Toast.LENGTH_SHORT).show();
        }
        else if (message.trim().length() == 0) {
            Toasty.warning(this, getString(R.string.msg_must_not_be_empty), Toast.LENGTH_SHORT).show();
            allCorrect = false;
        }  else if (message.trim().length() < 10) {
            Toasty.warning(this, getString(R.string.msg_minimum_char), Toast.LENGTH_SHORT).show();
            allCorrect = false;
        }
       if (autoCompleteTextView.getSelectedItem().toString().equals("")){
            id=0;
        }

//        if (lname.trim().length() == 0) {
//            Toasty.warning(this, getString(R.string.fill_lastname), Toast.LENGTH_SHORT).show();
//            allCorrect = false;
//        } else
//        if (dept == 0) {
//            allCorrect = false;
//            Toasty.warning(CreateTicketActivity.this, "Please select some Department", Toast.LENGTH_SHORT).show();
//        } else if (SLAPlans == 0) {
//            allCorrect = false;
//            Toasty.warning(CreateTicketActivity.this, "Please select some SLA plan", Toast.LENGTH_SHORT).show();
//        } else

        if (allCorrect) {

            if (InternetReceiver.isConnected()) {
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
                    progressDialog.show();
                new CreateNewTicket(Integer.parseInt(Prefs.getString("ID", null)), subject, message, helpTopic.ID, priority.ID, phone, fname, lname, email2, countrycode, staff.ID, mobile ).execute();
            } else
                Toasty.info(this, getString(R.string.oops_no_internet), Toast.LENGTH_SHORT, true).show();
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
        //Toast.makeText(CreateTicketActivity.this, "Permission Received", Toast.LENGTH_SHORT).show();
        Log.d("requestCode",""+requestCode);

        if (document==1){
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("*/*");
            startActivityForResult(intent, PICKFILE_REQUEST_CODE);
            document=0;
        }
         if (gallery==2){
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, PICKFILE_REQUEST_CODE);
            gallery=0;
        }
         if (camera==3){
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_REQUEST);
            camera=0;
        }
        if (audio==4){
            Intent intent;
            intent = new Intent();
            intent.setType("audio/mp3");
            startActivityForResult(intent,PICKFILE_REQUEST_CODE);
            audio=0;

        }
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
//            return new Helpdesk().postCreateTicket(userID, subject, body, helpTopic, priority, fname, lname, phone, email, code, staff, mobile+ collaborators, new File[]{new File(result)});
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
            if (term.equals("")) {
                arrayAdapterCC = new ArrayAdapter<Data>(CreateTicketActivity.this, android.R.layout.simple_dropdown_item_1line, emailHint);
                //new FetchCollaborator("s").execute();
                Data data = new Data(0, "No result found");
                emailHint.add(data);
//                autoCompleteTextViewCC.setAdapter(stringArrayAdapterCC);
//                stringArrayAdapterCC.notifyDataSetChanged();
//                autoCompleteTextViewCC.setThreshold(0);
//                autoCompleteTextViewCC.setDropDownWidth(1000);

            } else {
                arrayAdapterCC = new ArrayAdapter<Data>(CreateTicketActivity.this, android.R.layout.simple_dropdown_item_1line, emailHint);
                new FetchCollaborator(term).execute();
                editTextEmail.setAdapter(arrayAdapterCC);
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
                    Log.d("newTerm", term);
                    arrayAdapterCollaborator = new ArrayAdapter<>(CreateTicketActivity.this, android.R.layout.simple_dropdown_item_1line, stringArraylist);
                    new FetchCollaboratorCC(term.trim()).execute();
                    //arrayAdapterCollaborator = new ArrayAdapter<>(CreateTicketActivity.this, android.R.layout.simple_dropdown_item_1line, stringArraylist);
                    multiAutoCompleteTextViewCC.setAdapter(arrayAdapterCollaborator);
                }
//            Toast.makeText(collaboratorAdd.this, "term:"+term, Toast.LENGTH_SHORT).show();
                else if (term.equals("")) {
                    arrayAdapterCollaborator = new ArrayAdapter<>(CreateTicketActivity.this, android.R.layout.simple_dropdown_item_1line, stringArraylist);
                    //new FetchCollaborator("s").execute();
                    //Data data=new Data(0,"No result found");

//                autoCompleteTextViewCC.setAdapter(stringArrayAdapterCC);
//                stringArrayAdapterCC.notifyDataSetChanged();
//                autoCompleteTextViewCC.setThreshold(0);
//                autoCompleteTextViewCC.setDropDownWidth(1000);

                } else {
                    arrayAdapterCollaborator = new ArrayAdapter<>(CreateTicketActivity.this, android.R.layout.simple_dropdown_item_1line, stringArraylist);
                    new FetchCollaboratorCC(term).execute();
                    multiAutoCompleteTextViewCC.setAdapter(arrayAdapterCollaborator);


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
            emailHint.clear();
//            if (progressDialog.isShowing())
//                progressDialog.dismiss();

//            if (result == null) {
//                Toasty.error(collaboratorAdd.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
//                return;
//            }

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("users");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String email = jsonObject1.getString("email");
                    id1 = Integer.parseInt(jsonObject1.getString("id"));
                    String first_name=jsonObject1.getString("first_name");
                    String last_name=jsonObject1.getString("last_name");
                    //Toast.makeText(TicketSaveActivity.this, "email:"+email, Toast.LENGTH_SHORT).show();
                    Data data = new Data(id1,first_name + " " + last_name + " <" + email + ">");
                    emailHint.add(data);

                }
                multiAutoCompleteTextViewCC.setAdapter(arrayAdapterCollaborator);
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
                        //Toast.makeText(TicketSaveActivity.this, "email:"+email, Toast.LENGTH_SHORT).show();
                        Data data = new Data(id, first_name + " " + last_name + " <" + email + ">");
                        stringArraylist.add(data);

//                        Set<String> stringSet=new HashSet<>(stringArraylist);
//                        stringArraylist.clear();
//                        stringArraylist.addAll(stringSet);


                       // Prefs.putString("noUser","1");
                    }



//                    for (int i=0;i<stringArraylist.size();i++){
//                        if (stringArraylist.contains(emailfromsuggestion)){
//                            stringArraylist.remove(emailfromsuggestion);
//                        }
//                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }


        }
    }


}
