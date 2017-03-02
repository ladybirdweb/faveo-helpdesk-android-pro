package co.helpdesk.faveo.pro.frontend.activities;

import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
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

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.Utils;
import co.helpdesk.faveo.pro.frontend.fragments.CustomBottomSheetDialog;

public class CreateTicketActivity extends AppCompatActivity {
    private static int RESULT_LOAD_IMG = 1;
    private static int RESULT_LOAD_FILE = 42;
    String imgDecodableString;

    ArrayAdapter<String> spinnerSlaArrayAdapter, spinnerAssignToArrayAdapter,
            spinnerHelpArrayAdapter, spinnerDeptArrayAdapter, spinnerPriArrayAdapter;

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
    @BindView(R.id.spinner_dept)
    Spinner spinnerDept;
    @BindView(R.id.spinner_pri)
    Spinner spinnerPriority;
    @BindView(R.id.spinner_help)
    Spinner spinnerHelpTopic;
    @BindView(R.id.spinner_assign_to)
    Spinner spinnerSLA;
    //    @BindView(R.id.spinner_dept)
//    Spinner spinnerDept;
    @BindView(R.id.cc_searchview)
    SearchView ccSearchview;
    @BindView(R.id.requester_searchview)
    SearchView requesterSearchview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ticket);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Ticket");
        setUpViews();
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
                Toast.makeText(this, "You haven't picked anything!",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
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

    public void setUpViews() {
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
                                             CustomBottomSheetDialog bottomSheetDialog = CustomBottomSheetDialog.getInstance();
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

        spinnerSlaArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Utils.removeDuplicates(SplashActivity.valueSLA.split(","))); //selected item will look like a spinner set from XML
        spinnerSlaArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSLA.setAdapter(spinnerSlaArrayAdapter);

        spinnerAssignToArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Utils.removeDuplicates(SplashActivity.valueDepartment.split(","))); //selected item will look like a spinner set from XML
        spinnerAssignToArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDept.setAdapter(spinnerAssignToArrayAdapter);

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
        boolean allCorrect = true;
        if (subject.trim().length() == 0) {
            Toast.makeText(this, "Subject must not be empty", Toast.LENGTH_SHORT).show();
            allCorrect = false;
        } else if (message.trim().length() == 0) {
            Toast.makeText(this, "Message must not be empty", Toast.LENGTH_SHORT).show();
            allCorrect = false;
        } else if (subject.trim().length() < 5) {
            Toast.makeText(this, "Subject must be less or equal to 5", Toast.LENGTH_SHORT).show();
            allCorrect = false;
        } else if (message.trim().length() < 10) {
            Toast.makeText(this, "Message must be less or equal to 10", Toast.LENGTH_SHORT).show();
            allCorrect = false;
        }

        int helpTopic = spinnerHelpTopic.getSelectedItemPosition();
        int SLAPlans = spinnerSLA.getSelectedItemPosition();
        int dept = spinnerDept.getSelectedItemPosition();
        int priority = spinnerPriority.getSelectedItemPosition();

        if (allCorrect) {

            Toast.makeText(this, "Created", Toast.LENGTH_SHORT).show();
            Log.d("create ticket", "helptopiv :" + helpTopic + "sla :" + SLAPlans + "dept :" + dept + "pri :" + priority);
        }
    }
}
