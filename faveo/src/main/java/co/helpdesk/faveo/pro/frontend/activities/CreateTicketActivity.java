package co.helpdesk.faveo.pro.frontend.activities;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cocosw.bottomsheet.BottomSheet;
import com.pixplicity.easyprefs.library.Prefs;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.Utils;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.pro.frontend.fragments.CustomBottomSheetDialog;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.pro.model.MessageEvent;
import es.dmoral.toasty.Toasty;

public class CreateTicketActivity extends AppCompatActivity {
    private static int RESULT_LOAD_IMG = 1;
    private static int RESULT_LOAD_FILE = 42;
    //String imgDecodableString;
    static final String TAG = "CreateTicketActivity";

    ArrayAdapter<String> spinnerSlaArrayAdapter, spinnerAssignToArrayAdapter,
            spinnerHelpArrayAdapter, spinnerDeptArrayAdapter, spinnerPriArrayAdapter;

    //    @BindView(R.id.fname_edittext)
//    EditText editTextFirstName;
//    @BindView(R.id.email_edittext)
//    EditText editTextEmail;
//    @BindView(R.id.lname_edittext)
//    EditText editTextLastName;
//    @BindView(R.id.phone_edittext)
//    EditText editTextPhone;
//    @BindView(R.id.spinner_code)
//    Spinner phCode;
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
    //    @BindView(R.id.spinner_dept)
//    Spinner spinnerDept;
    @BindView(R.id.spinner_pri)
    Spinner spinnerPriority;
    @BindView(R.id.spinner_help)
    Spinner spinnerHelpTopic;
    //    @BindView(R.id.spinner_assign_to)
//    Spinner spinnerSLA;
    //    @BindView(R.id.spinner_dept)
//    Spinner spinnerDept;
    @BindView(R.id.cc_searchview)
    SearchView ccSearchview;
    @BindView(R.id.requester_searchview)
    SearchView requesterSearchview;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ticket);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.create_ticket);
        setUpViews();
        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        // AndroidNetworking.enableLogging();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_ticket_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.action_create:
                createButtonClick();
                return true;

            case android.R.id.home:
                //Write your logic here
                this.finish();
                return true;

            case R.id.action_attach: {
                new BottomSheet.Builder(this).title("Attach files from").sheet(R.menu.bottom_menu).listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case R.id.action_gallery:

                                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
                                break;
//                            case R.id.action_docx:
//
////                                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
////                                //intent.setType("text/*");
////                                intent.setType("*/*");
////                                intent.addCategory(Intent.CATEGORY_OPENABLE);
////                                startActivityForResult(Intent.createChooser(intent, "Select a doc"), RESULT_LOAD_FILE);
//
//                                break;
                            default:
                                break;
                        }
                    }
                }).show();
                return true;
            }
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == RESULT_LOAD_FILE && resultCode == RESULT_OK
                    && null != data) {
                Uri selectedFile = data.getData();
                String uriString = getPath(selectedFile);
                File myFile = new File(uriString);
                imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_txt));
                attachmentFileSize.setText(getFileSize(myFile.length()));
                attachmentFileName.setText(myFile.getName());
            } else if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data
                Uri selectedImage = data.getData();
                Log.d("selectedIMG  ", selectedImage + "");
                Log.d("getPath()  ", getPath(selectedImage) + "");
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                String uriString = getPath(selectedImage);
                File myFile = new File(uriString);
//                String[] filePathColumn = {MediaStore.Images.Media.DATA};
//
//                // Get the cursor
//                Cursor cursor = getContentResolver().query(selectedImage,
//                        filePathColumn, null, null, null);
//                // Move to first row
//                cursor.moveToFirst();
//
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                imgDecodableString = cursor.getString(columnIndex);
//                cursor.close();
                attachment_layout.setVisibility(View.VISIBLE);
                // Set the Image in ImageView after decoding the String
                imageView.setImageBitmap(bitmap);
                Log.d("size", myFile.length() + "");
                //attachmentFileSize.setText("(" + myFile.length() / 1024 + "kb)");
                attachmentFileSize.setText(getFileSize(myFile.length()));
                attachmentFileName.setText(myFile.getName());

            } else {
                Toasty.info(this, getString(R.string.you_hvent_picked_anything),
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toasty.error(this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG)
                    .show();
        }
    }

    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public int getCountryZipCode() {
        String CountryID = "";
        //String CountryZipCode = "";
        int code = 0;

        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID = manager.getSimCountryIso().toUpperCase();
        String[] rl = this.getResources().getStringArray(R.array.spinnerCountryCodes);
        for (int i = 0; i < rl.length; i++) {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                //CountryZipCode = g[0];
                code = i;
                break;
            }
        }
        return code;
    }

    public void setUpViews() {
        // phCode.setSelection(getCountryZipCode());
        final CursorAdapter suggestionAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                null,
                new String[]{SearchManager.SUGGEST_COLUMN_TEXT_1},
                new int[]{android.R.id.text1},
                0);
        final List<String> suggestions = new ArrayList<>();

        requesterSearchview.setSuggestionsAdapter(suggestionAdapter);
        requesterSearchview.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                requesterSearchview.setQuery(suggestions.get(position), false);
                requesterSearchview.clearFocus();
                //doSearch(suggestions.get(position));
                return true;
            }
        });

        requesterSearchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 2) {
                    //loadData(s);
                    Toast.makeText(getBaseContext(), newText, Toast.LENGTH_SHORT).show();
                }

                //                MyApp.autocompleteService.search(newText, new Callback<Autocomplete>() {
//                    @Override
//                    public void success(Autocomplete autocomplete, Response response) {
//                        suggestions.clear();
//                        suggestions.addAll(autocomplete.suggestions);
//
//                        String[] columns = {
//                                BaseColumns._ID,
//                                SearchManager.SUGGEST_COLUMN_TEXT_1,
//                                SearchManager.SUGGEST_COLUMN_INTENT_DATA
//                        };
//
//                        MatrixCursor cursor = new MatrixCursor(columns);
//
//                        for (int i = 0; i < autocomplete.suggestions.size(); i++) {
//                            String[] tmp = {Integer.toString(i), autocomplete.suggestions.get(i), autocomplete.suggestions.get(i)};
//                            cursor.addRow(tmp);
//                        }
//                        suggestionAdapter.swapCursor(cursor);
//                    }
//
//                    @Override
//                    public void failure(RetrofitError error) {
//                        Toast.makeText(SearchFoodActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                        Log.w("autocompleteService", error.getMessage());
//                    }
//                });
                return true;
            }
        });
        ccSearchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(getBaseContext(), query, Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }
        });

        ImageButton attchmentClose = (ImageButton) findViewById(R.id.attachment_close);
        ImageButton addButton = (ImageButton) findViewById(R.id.addrequester_button);
        addButton.setOnClickListener(new View.OnClickListener()
                                     {
                                         @Override
                                         public void onClick(View v) {

                                             CustomBottomSheetDialog bottomSheetDialog = new CustomBottomSheetDialog();
                                             bottomSheetDialog.show(getSupportFragmentManager(), "Custom Bottom Sheet");

                                         }
                                     }

        );
        attchmentClose.setOnClickListener(new View.OnClickListener()

                                          {
                                              @Override
                                              public void onClick(View v) {
                                                  attachment_layout.setVisibility(View.GONE);
                                              }
                                          }

        );

        spinnerHelpArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Utils.removeDuplicates(SplashActivity.valueTopic.split(","))); //selected item will look like a spinner set from XML
        spinnerHelpArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHelpTopic.setAdapter(spinnerHelpArrayAdapter);

//        spinnerSlaArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Utils.removeDuplicates(SplashActivity.valueSLA.split(","))); //selected item will look like a spinner set from XML
//        spinnerSlaArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerSLA.setAdapter(spinnerSlaArrayAdapter);
//
//        spinnerAssignToArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Utils.removeDuplicates(SplashActivity.valueDepartment.split(","))); //selected item will look like a spinner set from XML
//        spinnerAssignToArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerDept.setAdapter(spinnerAssignToArrayAdapter);

        spinnerPriArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Utils.removeDuplicates(SplashActivity.valuePriority.split(","))); //selected item will look like a spinner set from XML
        spinnerPriArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(spinnerPriArrayAdapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //replaces the default 'Back' button action
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return true;
    }

    public void createButtonClick() {
        String subject = subEdittext.getText().toString();
        String message = msgEdittext.getText().toString();
//        String email = editTextEmail.getText().toString();
//        String fname = editTextFirstName.getText().toString();
//        String lname = editTextLastName.getText().toString();
//        String phone = editTextPhone.getText().toString();
//        String countrycode = phCode.getSelectedItem().toString();
//        String[] cc = countrycode.split(",");
//        countrycode = cc[0];
        boolean allCorrect = true;

        int helpTopic = spinnerHelpTopic.getSelectedItemPosition();
        //  int SLAPlans = spinnerSLA.getSelectedItemPosition();
        //int dept = spinnerDept.getSelectedItemPosition();
        int priority = spinnerPriority.getSelectedItemPosition();

//        if (fname.trim().length() == 0) {
//            Toasty.warning(this, getString(R.string.fill_firstname), Toast.LENGTH_SHORT).show();
//            allCorrect = false;
//        } else if (fname.trim().length() < 3) {
//            Toasty.warning(this, getString(R.string.firstname_minimum_char), Toast.LENGTH_SHORT).show();
//            allCorrect = false;
//        } else if (lname.trim().length() == 0) {
//            Toasty.warning(this, getString(R.string.fill_lastname), Toast.LENGTH_SHORT).show();
//            allCorrect = false;
//        } else if (email.trim().length() == 0 || !Helper.isValidEmail(email)) {
//            Toasty.warning(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();
//            allCorrect = false;
//        } else
        if (subject.trim().length() == 0) {
            Toasty.warning(this, getString(R.string.sub_must_not_be_empty), Toast.LENGTH_SHORT).show();
            allCorrect = false;
        } else if (message.trim().length() == 0) {
            Toasty.warning(this, getString(R.string.msg_must_not_be_empty), Toast.LENGTH_SHORT).show();
            allCorrect = false;
        } else if (subject.trim().length() < 5) {
            Toasty.warning(this, getString(R.string.sub_minimum_char), Toast.LENGTH_SHORT).show();
            allCorrect = false;
        } else if (message.trim().length() < 10) {
            Toasty.warning(this, getString(R.string.msg_minimum_char), Toast.LENGTH_SHORT).show();
            allCorrect = false;
        } else if (helpTopic == 0) {
            allCorrect = false;
            Toasty.warning(CreateTicketActivity.this, "Please select some helptopic", Toast.LENGTH_SHORT).show();
        } else if (priority == 0) {
            allCorrect = false;
            Toasty.warning(CreateTicketActivity.this, "Please select some Priority", Toast.LENGTH_SHORT).show();
        }
//        if (dept == 0) {
//            allCorrect = false;
//            Toasty.warning(CreateTicketActivity.this, "Please select some Department", Toast.LENGTH_SHORT).show();
//        } else if (SLAPlans == 0) {
//            allCorrect = false;
//            Toasty.warning(CreateTicketActivity.this, "Please select some SLA plan", Toast.LENGTH_SHORT).show();
//        } else
        if (allCorrect) {

            if (InternetReceiver.isConnected()) {
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage(getString(R.string.creating_ticket));

                try {
                    // fname = URLEncoder.encode(fname, "utf-8");
                    // lname = URLEncoder.encode(lname, "utf-8");
                    subject = URLEncoder.encode(subject, "utf-8");
                    message = URLEncoder.encode(message, "utf-8");
                    // email = URLEncoder.encode(email, "utf-8");
                    // phone = URLEncoder.encode(phone, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                progressDialog.show();
                new CreateNewTicket(Integer.parseInt(Prefs.getString("ID", null)), subject, message, helpTopic, priority).execute();
//                JSONObject jsonObject = new JSONObject();
//                try {
//                    //jsonObject.put("api_key", Constants.API_KEY);
//                    jsonObject.put("ip", null);
//                    jsonObject.put("token", Preference.getToken());
//                    // jsonObject.put("user_id", Integer.parseInt(Preference.getUserID()));
//                    jsonObject.put("subject", subject);
//                    jsonObject.put("body", message);
//                    jsonObject.put("helptopic", helpTopic);
//                    jsonObject.put("sla", SLAPlans);
//                    jsonObject.put("priority", priority);
//                    jsonObject.put("dept", dept);
//                    // jsonObject.put("first_name", fname);
//                    //jsonObject.put("last_name", lname);
//                    // jsonObject.put("code", countrycode);
//                    // jsonObject.put("phone", phone);
//                    // jsonObject.put("email", email);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                // Log.d(TAG, jsonObject + "");

//                ANRequest anRequest = AndroidNetworking.post(Constants.URL + "helpdesk/create?")
//                        .addJSONObjectBody(jsonObject) // posting json
//                        .setTag("create_ticket")
//                        .setPriority(Priority.HIGH)
//                        .build();
//                Log.d("ANRequested URL", anRequest.getUrl() + "");
//                anRequest.getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.d("FAN", response + "");
//                        progressDialog.dismiss();
//                        if (response.has("token_expired")) {
//                            new Helpdesk();
//                            new Authenticate();
//                            createButtonClick();
//                        } else if (response.has("NotificationThread created successfully!")) {
//                            Toasty.success(CreateTicketActivity.this, getString(R.string.ticket_created_success), Toast.LENGTH_LONG).show();
//                        }
//                        // do anything with response
//                    }
//
//                    @Override
//                    public void onError(ANError error) {
//                        progressDialog.dismiss();
//                        if (error.getErrorCode() != 0) {
//                            // received error from server
//                            // error.getErrorCode() - the error code from server
//                            // error.getErrorBody() - the error body from server
//                            // error.getErrorDetail() - just an error detail
//                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
//                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
//                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
//                            // get parsed error object (If ApiError is your class)
//                            //ApiError apiError = error.getErrorAsObject(ApiError.class);
//                        } else {
//                            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
//                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
//                        }
//                        // handle error
//                        Toasty.error(CreateTicketActivity.this, getString(R.string.error_creating_ticket), Toast.LENGTH_SHORT, true).show();
//                    }
//                });

                // new CreateNewTicket(Integer.parseInt(Preference.getUserID()), subject, message, helpTopic, SLAPlans, priority, dept, phone, fname, lname, email, countrycode).execute();
            } else
                Toasty.info(this, getString(R.string.oops_no_internet), Toast.LENGTH_SHORT, true).show();
        }
    }

    private class CreateNewTicket extends AsyncTask<String, Void, String> {
        String fname, lname, email, code;
        String subject;
        String body;
        String phone;
        int helpTopic;
        // int SLA;
        int priority;
        //int dept;
        int userID;

        CreateNewTicket(int userID, String subject, String body,
                        int helpTopic, int priority) {

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

        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().postCreateTicket(userID, subject, body, helpTopic, priority, fname, lname, phone, email, code);
        }

        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            if (result == null) {
                Toasty.error(CreateTicketActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }
            if (result.contains("NotificationThread created successfully!")) {
                Toasty.success(CreateTicketActivity.this, getString(R.string.ticket_created_success), Toast.LENGTH_LONG).show();
            }
        }
    }

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
}
