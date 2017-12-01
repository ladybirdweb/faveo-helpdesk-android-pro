package co.helpdesk.faveo.pro.frontend.activities;

import android.app.Activity;
import android.app.ProgressDialog;
//import android.app.SearchManager;
//import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
//import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.widget.CursorAdapter;
//import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
//import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.pixplicity.easyprefs.library.Prefs;
//import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
//import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
//import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.helpdesk.faveo.pro.Helper;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.pro.model.Data;
import co.helpdesk.faveo.pro.model.MessageEvent;
import es.dmoral.toasty.Toasty;

/**
 * This activity is for responsible for creating the ticket.
 * Here we are using create ticket async task which is
 * POST request.We are getting the JSON data here from the dependency API.
 */
public class CreateTicketActivity extends AppCompatActivity {
//    private static int RESULT_LOAD_IMG = 1;
//    private static int RESULT_LOAD_FILE = 42;
    //CountryCodePicker ccp;
    //String imgDecodableString;
//    static final String TAG = "CreateTicketActivity";
    boolean allCorrect;
    String term;
    String collaborators=null;
    ArrayAdapter<Data> spinnerPriArrayAdapter, spinnerHelpArrayAdapter,spinnerStaffArrayAdapter,autocompletetextview,stringArrayAdapterHint,arrayAdapterCC;
    ArrayAdapter<Data> arrayAdapterCollaborator;
//    ArrayAdapter<String> spinnerSlaArrayAdapter, spinnerAssignToArrayAdapter,
//            spinnerDeptArrayAdapter;

    @BindView(R.id.fname_edittext)
    EditText editTextFirstName;
    AutoCompleteTextView editTextEmail;
    @BindView(R.id.lname_edittext)
    EditText editTextLastName;
    @BindView(R.id.phone_edittext)
    EditText editTextPhone;
//    @BindView(R.id.spinner_code)
//    SearchableSpinner phCode;
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
    //    @BindView(R.id.spinner_dept)
//    Spinner spinnerDept;
    Spinner autoCompletePriority;
    Spinner autoCompleteHelpTopic;
//    @BindView(R.id.assignedto)
//    Spinner spinnerAssignto;
    @BindView(R.id.buttonSubmit)
    Button buttonSubmit;
    Button buttonUserCreate;
    ImageView imageViewBack;
    AutoCompleteTextView autoCompleteTextView;
    String collaboratorFinal;
    ArrayList<String> collaboratorArray;
    //String email;
    String[] cc,cc1;
    StringBuilder sb,sb1;
    String emailfromsuggestion;
    String email2;

    //    @BindView(R.id.spinner_assign_to)
//    Spinner spinnerSLA;
    //    @BindView(R.id.spinner_dept)
//    Spinner spinnerDept;
//    @BindView(R.id.cc_searchview)
//    SearchView ccSearchview;
//    @BindView(R.id.requester_searchview)
//    SearchView requesterSearchview;
    ProgressDialog progressDialog;
    ArrayList<Data> helptopicItems, priorityItems,staffItems,staffitemsauto,staffItemsHint,emailHint;
    int id=0;
    int id1=0;
    String email1,collaborator;
    ArrayList<Data> stringArraylist;
    String mobile="";
    String splChrs = "-/@#$%^&_+=()" ;
    String countrycode = "";
    int pos;
    MultiAutoCompleteTextView multiAutoCompleteTextViewCC;
    CountryCodePicker countryCodePicker;
    String firstname,lastname,email,phone;
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
        setContentView(R.layout.activity_create_ticket);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        collaboratorArray=new ArrayList<>();
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        String json = Prefs.getString("DEPENDENCY", "");
        try {
            staffItems=new ArrayList<>();
            staffitemsauto=new ArrayList<>();
            //staffItems.add(new Data(0,"select assignee"));
            jsonObject=new JSONObject(json);
            JSONArray jsonArrayStaffs=jsonObject.getJSONArray("staffs");
            for (int i=0;i<jsonArrayStaffs.length();i++){
                Data data=new Data(Integer.parseInt(jsonArrayStaffs.getJSONObject(i).getString("id")),jsonArrayStaffs.getJSONObject(i).getString("first_name")+" "+jsonArrayStaffs.getJSONObject(i).getString("last_name"));
                staffItems.add(data);
                staffitemsauto.add(data);
            }

            helptopicItems = new ArrayList<>();
            helptopicItems.add(new Data(0, "Please select help topic"));
            jsonObject = new JSONObject(json);
            JSONArray jsonArrayHelpTopics = jsonObject.getJSONArray("helptopics");
            for (int i = 0; i < jsonArrayHelpTopics.length(); i++) {
                Data data = new Data(Integer.parseInt(jsonArrayHelpTopics.getJSONObject(i).getString("id")), jsonArrayHelpTopics.getJSONObject(i).getString("topic"));
                helptopicItems.add(data);
            }

            JSONArray jsonArrayPriorities = jsonObject.getJSONArray("priorities");
            priorityItems = new ArrayList<>();
            priorityItems.add(new Data(0, "Please select the priority"));
            for (int i = 0; i < jsonArrayPriorities.length(); i++) {
                Data data = new Data(Integer.parseInt(jsonArrayPriorities.getJSONObject(i).getString("priority_id")), jsonArrayPriorities.getJSONObject(i).getString("priority"));
                priorityItems.add(data);
            }
        } catch (JSONException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        autoCompleteTextView= (AutoCompleteTextView) findViewById(R.id.autocompletetext);

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

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                autocompletetextview = new ArrayAdapter<>(CreateTicketActivity.this, android.R.layout.simple_dropdown_item_1line, staffitemsauto);
                String name1=autoCompleteTextView.getText().toString();
                for (int j = 0; j < staffitemsauto.size(); j++) {
                    if (staffitemsauto.get(j).getName().equalsIgnoreCase(name1)) {
                        Data data = staffitemsauto.get(j);
                        id = data.getID();
                        //Toast.makeText(CreateTicketActivity.this, "id:" + id, Toast.LENGTH_SHORT).show();
                    }
                }

//                Data data=staffitemsauto.get(i);
//                String name=autoCompleteTextView.getText().toString();
//                if (staffitemsauto.contains(name)){
//
//                }
               //Toast.makeText(CreateTicketActivity.this, "Data:"+name1, Toast.LENGTH_SHORT).show();

//                String name =autoCompleteTextView.getText().toString();
//                if (name.equals(autocompletetextview.getItem(i).getName())){
//                    id=autocompletetextview.getItem(i).getID();
//                    Toast.makeText(CreateTicketActivity.this, ""+autocompletetextview.getItem(i).getID(), Toast.LENGTH_SHORT).show();
//                }
//                id=autocompletetextview.getItem(i).getID();
//                String name=autocompletetextview.getItem(i).getName();
//                Log.d("ID",""+id);
                //Log.d("name",""+name);

            }


        });


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

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String ss = cursor.getString(column_index);
        cursor.close();
        return ss;
    }

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
        autoCompleteTextView.setThreshold(0);
        autoCompleteTextView.setDropDownWidth(1000);

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

        if (!email1.equals("")&&email1.contains("<")){
            int pos=email1.indexOf("<");
            int pos2=email1.lastIndexOf(">");
            email2=email1.substring(pos+1,pos2);
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
//        Data staff= (Data) spinnerAssignto.getSelectedItem();

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
            Toasty.warning(this,"Subject must not exceed 100 characters"
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
       if (autoCompleteTextView.getText().toString().equals("")){
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
                new CreateNewTicket(Integer.parseInt(Prefs.getString("ID", null)), subject, message, helpTopic.ID, priority.ID, phone, fname, lname, email2, countrycode, id, mobile ).execute();
            } else
                Toasty.info(this, getString(R.string.oops_no_internet), Toast.LENGTH_SHORT, true).show();
        }
    }


    /**
     * Async task for creating the ticket.
     */
    private class CreateNewTicket extends AsyncTask<String, Void, String> {
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

        protected String doInBackground(String... urls) {

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


            try{
                JSONObject jsonObject=new JSONObject(result);
                JSONObject jsonObject1=jsonObject.getJSONObject("response");
                String message=jsonObject1.getString("message");
                if (message.equals("Permission denied, you do not have permission to access the requested page.")){
                    Toasty.warning(CreateTicketActivity.this, getString(R.string.permission), Toast.LENGTH_LONG).show();
                    Prefs.putString("403", "null");
                    return;
                }

            }catch (JSONException e){
                e.printStackTrace();
            }
//            String state=Prefs.getString("403",null);
////                if (message1.contains("The ticket id field is required.")){
////                    Toasty.warning(TicketDetailActivity.this, getString(R.string.please_select_ticket), Toast.LENGTH_LONG).show();
////                }
////                else if (message1.contains("The status id field is required.")){
////                    Toasty.warning(TicketDetailActivity.this, getString(R.string.please_select_status), Toast.LENGTH_LONG).show();
////                }
////               else
//            try {
//                if (state.equals("403") && !state.equals(null)) {
//                    Toasty.warning(CreateTicketActivity.this, getString(R.string.permission), Toast.LENGTH_LONG).show();
//                    Prefs.putString("403", "null");
//                    return;
//                }
//            }catch (NullPointerException e){
//                e.printStackTrace();
//            }


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
