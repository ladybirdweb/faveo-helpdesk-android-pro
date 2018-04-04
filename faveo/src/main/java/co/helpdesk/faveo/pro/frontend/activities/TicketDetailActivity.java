package co.helpdesk.faveo.pro.frontend.activities;

import android.app.ProgressDialog;
//import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
//import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
//import android.text.SpannableString;
//import android.text.style.ForegroundColorSpan;
//import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;
import com.pixplicity.easyprefs.library.Prefs;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.helpdesk.faveo.pro.Constants;
//import co.helpdesk.faveo.pro.Helper;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.backend.api.v1.Authenticate;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.pro.frontend.fragments.ticketDetail.Conversation;
import co.helpdesk.faveo.pro.frontend.fragments.ticketDetail.Detail;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.pro.frontend.views.Fab;
import co.helpdesk.faveo.pro.model.MessageEvent;
//import co.helpdesk.faveo.pro.model.TicketDetail;
import es.dmoral.toasty.Toasty;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

/**
 * This splash activity is responsible for
 * getting the metadata of our faveo application from the dependency API.
 */
public class TicketDetailActivity extends AppCompatActivity implements
        Conversation.OnFragmentInteractionListener,
        Detail.OnFragmentInteractionListener {

    ViewPager viewPager;
    public ViewPagerAdapter adapter;
    Conversation fragmentConversation;
    Detail fragmentDetail;
    Boolean fabExpanded = false;
    //    FloatingActionButton fabAdd;
    int cx, cy;
    Fab fab;
    private MaterialSheetFab materialSheetFab;
    View overlay;
    EditText editTextInternalNote, editTextCC, editTextReplyMessage;
    Button buttonCreate, buttonSend,buttonExit,buttonCancel;
    ProgressDialog progressDialog;
    private int statusBarColor;
    public static String ticketID, ticketNumber;
    //    TextView textView,textViewStatus,textViewStatus1,textViewStatus2;
    TextView textView;
    String status;
    String title;
    LinearLayout linearLayout;
    Toolbar mToolbar;
    TextView addCc;
    View viewpriority;
    ImageView imgaeviewBack;
    String cameFromNotification;
    public static boolean isShowing = false;
    TextView textViewStatus, textviewAgentName, textViewTitle, textViewSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ticket_detail);
        setupFab();

//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//
//        StrictMode.setThreadPolicy(policy);
//        final Handler handler = new Handler();
//        Runnable runnable = new Runnable() {
//            public void run() {
//                //
//                // Do the stuff
//                //
//                String result= new Authenticate().postAuthenticateUser(Prefs.getString("USERNAME", null), Prefs.getString("PASSWORD", null));
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    JSONObject jsonObject1=jsonObject.getJSONObject("data");
//                    JSONObject jsonObject2=jsonObject1.getJSONObject("user");
//                    String role1=jsonObject2.getString("role");
//                    if (role1.equals("user")){
//                        Prefs.clear();
//                        //Prefs.putString("role",role);
//                        Intent intent=new Intent(TicketDetailActivity.this,LoginActivity.class);
//                        Toasty.info(TicketDetailActivity.this,getString(R.string.roleChanged), Toast.LENGTH_LONG).show();
//                        startActivity(intent);
//
//
//                    }
//
//
//                } catch (JSONException | NullPointerException e) {
//                    e.printStackTrace();
//                }
//
//                handler.postDelayed(this, 30000);
//            }
//        };
//        runnable.run();
        Log.d("ticketDetailOnCreate","True");

//        try {
//            cameFromNoti = Prefs.getString("cameFromNotification", null);
//            if (cameFromNoti.equals("true")){
//                Log.d("cameFromNotification",getIntent().getStringExtra("ticket_id"));
//                ticketID = getIntent().getStringExtra("ticket_id");
//            }
//            else{
//                ticketID = Prefs.getString("TICKETid", null);
//            }
//        }catch (NullPointerException e){
//            e.printStackTrace();
//        }
        ticketID=Prefs.getString("TICKETid",null);
        try {
            cameFromNotification = Prefs.getString("cameFromNotification", null);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        //Log.d("cameFromNotification",cameFromNotification);
        ticketNumber = getIntent().getStringExtra("ticket_number");
        linearLayout= (LinearLayout) findViewById(R.id.section_internal_note);
        mToolbar = (Toolbar) findViewById(R.id.toolbarTicketDetail);
        textViewStatus = (TextView) mToolbar.findViewById(R.id.status);
        textviewAgentName = (TextView) mToolbar.findViewById(R.id.textViewagentName);
        textViewTitle = (TextView) mToolbar.findViewById(R.id.title);
        textViewSubject = (TextView) mToolbar.findViewById(R.id.subject);
        imgaeviewBack= (ImageView) findViewById(R.id.imageViewBackTicketDetail);
        viewpriority=mToolbar.findViewById(R.id.viewPriority);
        mToolbar.inflateMenu(R.menu.menu_main_new);
        isShowing=true;
        //Log.d("came into ticket detail","true");
        mToolbar.getMenu().getItem(0).setEnabled(false);
        addCc = (TextView) findViewById(R.id.addcc);
        if (InternetReceiver.isConnected()){
//            progressDialog=new ProgressDialog(this);
//            progressDialog.setMessage(getString(R.string.pleasewait));
//            progressDialog.show();
            new Thread(new Runnable() {
                public void run(){
                    Log.d("threadisrunning","true");
                    new FetchTicketDetail(Prefs.getString("TICKETid",null)).execute();
                }
            }).start();
//            new Thread(new Runnable() {
//                public void run(){
//                    Log.d("threadisrunning","true");
//                    new FetchCollaboratorAssociatedWithTicket(Prefs.getString("TICKETid", null)).execute();
//                }
//            }).start();

        }

//        addCc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(TicketDetailActivity.this, collaboratorAdd.class);
//                startActivity(intent);
//            }
//        });


        imgaeviewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (cameFromNotification.equals("true")){
//                    Intent intent=new Intent(TicketDetailActivity.this,NotificationActivity.class);
//                    startActivity(intent);
                Log.d("cameFromnotification",Prefs.getString("cameFromNotification",null));
//                }
//
                     if (Prefs.getString("cameFromNotification",null).equals("true")){
                        Intent intent=new Intent(TicketDetailActivity.this,NotificationActivity.class);
                        startActivity(intent);
                    }
                    else if (Prefs.getString("cameFromNotification",null).equals("false")){
                         Intent intent=new Intent(TicketDetailActivity.this,MainActivity.class);
                         startActivity(intent);
                     }
                    else{
                        Intent intent=new Intent(TicketDetailActivity.this,MainActivity.class);
                        startActivity(intent);
                    }



            }
        });

        setSupportActionBar(mToolbar);

        Constants.URL = Prefs.getString("COMPANY_URL", "");

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        JSONObject jsonObject;
        String json = Prefs.getString("DEPENDENCY", "");

        try {
            jsonObject = new JSONObject(json);
            JSONArray jsonArrayStaffs = jsonObject.getJSONArray("status");

            for (int i = 0; i < jsonArrayStaffs.length(); i++) {
                if (jsonArrayStaffs.getJSONObject(i).getString("name").equals("Open")) {
                    Prefs.putString("openid", jsonArrayStaffs.getJSONObject(i).getString("id"));
                } else if (jsonArrayStaffs.getJSONObject(i).getString("name").equals("Resolved")) {
                    Prefs.putString("resolvedid", jsonArrayStaffs.getJSONObject(i).getString("id"));
                } else if (jsonArrayStaffs.getJSONObject(i).getString("name").equals("Closed")) {
                    Prefs.putString("closedid", jsonArrayStaffs.getJSONObject(i).getString("id"));
                } else if (jsonArrayStaffs.getJSONObject(i).getString("name").equals("Deleted")) {
                    Prefs.putString("deletedid", jsonArrayStaffs.getJSONObject(i).getString("id"));
                } else if (jsonArrayStaffs.getJSONObject(i).getString("name").equals("Archived")) {
                    Prefs.putString("archivedid", jsonArrayStaffs.getJSONObject(i).getString("id"));
                } else if (jsonArrayStaffs.getJSONObject(i).getString("name").equals("Verified")) {
                    Prefs.putString("verifiedid", jsonArrayStaffs.getJSONObject(i).getString("id"));
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(onTabSelectedListener);

        progressDialog = new ProgressDialog(this);
        editTextInternalNote = (EditText) findViewById(R.id.editText_internal_note);
        //editTextCC = (EditText) findViewById(R.id.editText_cc);
        editTextReplyMessage = (EditText) findViewById(R.id.editText_reply_message);
        buttonCreate = (Button) findViewById(R.id.button_create);
        buttonSend = (Button) findViewById(R.id.button_send);


        /*
          This button is for creating the internal note.
         */
//        buttonCreate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String note = editTextInternalNote.getText().toString();
//                if (note.trim().length() == 0) {
//                    Toasty.warning(TicketDetailActivity.this, getString(R.string.msg_must_not_be_empty), Toast.LENGTH_LONG).show();
//                    return;
//                }
//                String userID = Prefs.getString("ID", null);
//                if (userID != null && userID.length() != 0) {
//                    try {
//                        note = URLEncoder.encode(note, "utf-8");
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//
//                    new CreateInternalNoteActivity(Integer.parseInt(ticketID), Integer.parseInt(userID), note).execute();
//                    progressDialog.setMessage(getString(R.string.creating_note));
//                    progressDialog.show();
//
//
//                } else
//                    Toasty.warning(TicketDetailActivity.this, getString(R.string.wrong_user_id), Toast.LENGTH_LONG).show();
//            }
//        });

        /*
          This button is for getting the cc from the reply option
          here we are handling multiple cc items.
         */
//        buttonSend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                String cc = editTextCC.getText().toString();
//                String replyMessage = editTextReplyMessage.getText().toString();
//                if (replyMessage.trim().length() == 0) {
//                    Toasty.warning(TicketDetailActivity.this, getString(R.string.msg_must_not_be_empty), Toast.LENGTH_LONG).show();
//                    return;
//                }
//
////                cc = cc.replace(", ", ",");
////                if (cc.length() > 0) {
////                    String[] multipleEmails = cc.split(",");
////                    for (String email : multipleEmails) {
////                        if (email.length() > 0 && !Helper.isValidEmail(email)) {
////                            Toasty.warning(TicketDetailActivity.this, getString(R.string.invalid_cc), Toast.LENGTH_LONG).show();
////                            return;
////                        }
////                    }
////                }
//
//                String userID = Prefs.getString("ID", null);
//                if (userID != null && userID.length() != 0) {
//                    try {
//                        replyMessage = URLEncoder.encode(replyMessage, "utf-8");
//                        Log.d("Msg", replyMessage);
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//
//                    new ReplyTicket(Integer.parseInt(ticketID), replyMessage).execute();
//                    progressDialog.setMessage(getString(R.string.sending_msg));
//                    progressDialog.show();
//
//                } else
//                    Toasty.warning(TicketDetailActivity.this, getString(R.string.wrong_user_id), Toast.LENGTH_LONG).show();
//            }
//        });

//        fabAdd = (FloatingActionButton) findViewById(R.id.fab);
//        fabAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getCreateRequest();
//            }
//        });
//
        overlay = findViewById(R.id.overlay);
        overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitReveal();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_new, menu);
        //mToolbar.getMenu();


        return true;
    }

    /**
     * Handle action bar item clicks here. The action bar will
     * automatically handle clicks on the Home/Up button, so long
     * as you specify a parent activity in AndroidManifest.xml.
     *
     * @param item items refer to the menu items.
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        try {
            if (item != null) {
                item.getSubMenu().clearHeader();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        Fragment fragment = null;
        title = getString(R.string.app_name);
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.buttonsave) {
            Intent intent = new Intent(TicketDetailActivity.this, TicketSaveActivity.class);
            startActivity(intent);
            finish();
//            title = getString(R.string.title_activity_ticketsave);
//            fragment = getSupportFragmentManager().findFragmentByTag(title);
//            if (fragment == null)
//                fragment = new TicketSaveFragment();
//            if (fragment != null) {
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.container_body, fragment);
//                // fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//            }

        }


//        String ticketid1= Prefs.getString("ticketid",null);
//        StringBuilder stringBuilder=new StringBuilder(ticketid1);
//        stringBuilder.deleteCharAt(ticketid1.length()-1);
//        String ticketid=stringBuilder.toString();
//
//        if (id == R.id.action_search) {
//            startActivity(new Intent(MainActivity.this, SearchActivity.class));
//            return true;
//        }


        if (id == R.id.action_statusOpen) {
            status = Prefs.getString("ticketstatus", null);
            if (status.equals("Open")) {
                Toasty.warning(TicketDetailActivity.this, getString(R.string.ticket_already_open), Toast.LENGTH_SHORT).show();
                return false;
            } else {
                new StatusChange(Integer.parseInt(ticketID), Integer.parseInt(Prefs.getString("openid", null))).execute();
                progressDialog.show();
                progressDialog.setMessage(getString(R.string.pleasewait));

                //Toasty.success(TicketDetailActivity.this, getString(R.string.status_opened), Toast.LENGTH_SHORT).show();
//                finish();
//                startActivity(new Intent(TicketDetailActivity.this, MainActivity.class));
            }

        } else if (id == R.id.action_statusResolved) {
            status = Prefs.getString("ticketstatus", null);
            if (status.equals("Resolved")) {
                Toasty.warning(TicketDetailActivity.this, getString(R.string.ticket_alreday_resolved), Toast.LENGTH_SHORT).show();
                return false;
            }
            new StatusChange(Integer.parseInt(ticketID), Integer.parseInt(Prefs.getString("resolvedid", null))).execute();
            progressDialog.show();
            progressDialog.setMessage(getString(R.string.pleasewait));

            //Toasty.success(TicketDetailActivity.this,getString(R.string.status_resolved),Toast.LENGTH_SHORT).show();
//            finish();
//            startActivity(new Intent(TicketDetailActivity.this, MainActivity.class));

        } else if (id == R.id.action_statusClosed) {
            status = Prefs.getString("ticketstatus", null);
            if (status.equals("Closed")) {
                Toasty.warning(TicketDetailActivity.this, getString(R.string.ticket_already_closed), Toast.LENGTH_SHORT).show();
                return false;
            } else {
                new StatusChange(Integer.parseInt(ticketID), Integer.parseInt(Prefs.getString("closedid", null))).execute();
                progressDialog.show();
                progressDialog.setMessage(getString(R.string.pleasewait));
                //Prefs.putString("ticketstatus","Closed");
                //Toasty.success(TicketDetailActivity.this, getString(R.string.status_closed), Toast.LENGTH_SHORT).show();
//                finish();
//                startActivity(new Intent(TicketDetailActivity.this, MainActivity.class));
            }

        } else if (id == R.id.action_statusDeleted) {
            status = Prefs.getString("ticketstatus", null);
            if (status.equals("Deleted")) {
                Toasty.warning(TicketDetailActivity.this, getString(R.string.ticket_already_deleted), Toast.LENGTH_SHORT).show();
                return false;
            } else {
                new StatusChange(Integer.parseInt(ticketID), Integer.parseInt(Prefs.getString("deletedid", null))).execute();
                progressDialog.show();
                progressDialog.setMessage(getString(R.string.pleasewait));

                //Toasty.success(TicketDetailActivity.this, getString(R.string.status_deleted), Toast.LENGTH_SHORT).show();

            }

        }
//        else{
//            Toasty.error(TicketDetailActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
//        }
//        else if (id==R.id.action_statusRequestForClose){
//            new StatusChange(Integer.parseInt(ticketID),5).execute();
//            Toasty.success(TicketDetailActivity.this,getString(R.string.status_request_for_close),Toast.LENGTH_SHORT).show();
//            finish();
//            startActivity(new Intent(TicketDetailActivity.this, MainActivity.class));
//
//        }
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // close this activity and return to preview activity (if there is any)
        }
//

        return super.onOptionsItemSelected(item);
    }

    /**
     * Sets up the Floating action button.
     */
    private void setupFab() {

        fab = (Fab) findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.show();
        View sheetView = findViewById(R.id.fab_sheet);
        View overlay1 = findViewById(R.id.overlay1);
        int sheetColor = getResources().getColor(R.color.background_card);
        int fabColor = getResources().getColor(R.color.theme_accent);

        /**
         * Create material sheet FAB.
         */
        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay1, sheetColor, fabColor);

        /**
         * Set material sheet event listener.
         */
        materialSheetFab.setEventListener(new MaterialSheetFabEventListener() {
            @Override
            public void onShowSheet() {
                // Save current status bar color
                statusBarColor = getStatusBarColor();
                // Set darker status bar color to match the dim overlay
                setStatusBarColor(getResources().getColor(R.color.theme_primary_dark2));
            }

            @Override
            public void onHideSheet() {
                // Restore status bar color
                setStatusBarColor(statusBarColor);
            }
        });

        /**
         * Set material sheet item click listeners.
         */
        findViewById(R.id.fab_sheet_item_reply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialSheetFab.hideSheetThenFab();
                Intent intent=new Intent(TicketDetailActivity.this,TicketReplyActivity.class);
                startActivity(intent);
                exitReveal();
                //enterReveal("Reply");
            }
        });
        findViewById(R.id.fab_sheet_item_note).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialSheetFab.hideSheetThenFab();
                Intent intent=new Intent(TicketDetailActivity.this,InternalNoteActivity.class);
                startActivity(intent);
                exitReveal();
                //enterReveal("Internal notes");
            }
        });

        findViewById(R.id.fab_sheet_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialSheetFab.hideSheet();
            }
        });

//        findViewById(R.id.closeFab).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                materialSheetFab.hideSheet();
//            }
//        });
//        findViewById(R.id.fab_sheet_item_photo).setOnClickListener(this);
//        findViewById(R.id.fab_sheet_item_note).setOnClickListener(this);
    }

    private int getStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getWindow().getStatusBarColor();
        }
        return 0;
    }

    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
    }

//    /**
//     * Handling the back button.
//     *
//     * @param item refers to the menu item .
//     * @return
//     */
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // handle arrow click here
//        if (item.getItemId() == android.R.id.home) {
//            onBackPressed(); // close this activity and return to preview activity (if there is any)
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

//    private void getCreateRequest() {
//        final CharSequence[] items = {"Reply", "Internal notes"};
//        AlertDialog.Builder builder = new AlertDialog.Builder(TicketDetailActivity.this);
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                dialog.dismiss();
//                if (items[item].equals("Reply")) {
//                    cx = (int) fabAdd.getX() + dpToPx(40);
//                    cy = (int) fabAdd.getY();
//                    fabExpanded = true;
//                    fabAdd.hide();
//                    enterReveal("Reply");
//                } else {
//                    cx = (int) fabAdd.getX() + dpToPx(40);
//                    cy = (int) fabAdd.getY();
//                    fabExpanded = true;
//                    fabAdd.hide();
//                    enterReveal("Internal notes");
//                }
//            }
//        });
//        builder.show();
//    }

    /**
     * This API is for creating the internal note.
     */
//    private class CreateInternalNoteActivity extends AsyncTask<String, Void, String> {
//        int ticketID;
//        int userID;
//        String note;
//
//        CreateInternalNoteActivity(int ticketID, int userID, String note) {
//            this.ticketID = ticketID;
//            this.userID = userID;
//            this.note = note;
//        }
//
//        protected String doInBackground(String... urls) {
//            return new Helpdesk().postCreateInternalNote(ticketID, userID, note);
//        }
//
//        protected void onPostExecute(String result) {
//            progressDialog.dismiss();
//            if (result == null) {
//                Toasty.error(TicketDetailActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
//                return;
//            }
//            Toasty.success(TicketDetailActivity.this, getString(R.string.internal_notes_posted), Toast.LENGTH_LONG).show();
//            editTextInternalNote.getText().clear();
//            exitReveal();
//
//            Conversation conversation = (Conversation) adapter.getItem(0);
//            if (conversation != null)
//                conversation.refresh();
//        }
//    }

    /**
     * Async task for changing the status of the ticket.
     */
    private class StatusChange extends AsyncTask<String, Void, String> {
        int ticketId, statusId;

        StatusChange(int ticketId, int statusId) {

            this.ticketId = ticketId;
            this.statusId = statusId;

        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().postStatusChanged(ticketId, statusId);
            //return new Helpdesk().postStatusChanged(ticketId,statusId);
        }

        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            //progressDialog.dismiss();
//            if (result == null) {
//                Toasty.error(TicketDetailActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
//                return;
            try {
                String state = Prefs.getString("403", null);
                if (state.equals("403") && !state.equals("null")) {
                    Toasty.warning(TicketDetailActivity.this, getString(R.string.permission), Toast.LENGTH_LONG).show();
                    Prefs.putString("403", "null");
                    return;
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }
//                if (message1.contains("The ticket id field is required.")){
//                    Toasty.warning(TicketDetailActivity.this, getString(R.string.please_select_ticket), Toast.LENGTH_LONG).show();
//                }
//                else if (message1.contains("The status id field is required.")){
//                    Toasty.warning(TicketDetailActivity.this, getString(R.string.please_select_status), Toast.LENGTH_LONG).show();
//                }
//               else
//            try {
//                JSONObject jsonObject = new JSONObject(result);
//                JSONObject jsonObject1 = jsonObject.getJSONObject("response");
//                JSONArray jsonArray = jsonObject1.getJSONArray("message");
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    String message = jsonArray.getString(i);
//                    if (message.equals("Permission denied, you do not have permission to access the requested page.")) {
//                        Toasty.warning(TicketDetailActivity.this, getString(R.string.permission), Toast.LENGTH_LONG).show();
//                        Prefs.putString("403", "null");
//                        return;
//                    }
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

//            }
            try {

                JSONObject jsonObject = new JSONObject(result);
                //JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                //JSONObject jsonObject2=jsonObject.getJSONObject("error");
                //String message1=jsonObject2.getString("ticket_id");
                String message2 = jsonObject.getString("message");


//                if (message2.contains("permission denied")&&Prefs.getString("403",null).equals("403")){
//
//                }
                if (message2.contains("Status changed to Deleted")) {
                    Toasty.success(TicketDetailActivity.this, getString(R.string.status_deleted), Toast.LENGTH_LONG).show();
                    Prefs.putString("ticketstatus", "Deleted");
                    finish();
                    startActivity(new Intent(TicketDetailActivity.this, MainActivity.class));
                } else if (message2.contains("Status changed to Open")) {
                    Toasty.success(TicketDetailActivity.this, getString(R.string.status_opened), Toast.LENGTH_LONG).show();
                    Prefs.putString("ticketstatus", "Open");
                    finish();
                    startActivity(new Intent(TicketDetailActivity.this, MainActivity.class));
                } else if (message2.contains("Status changed to Closed")) {
                    Toasty.success(TicketDetailActivity.this, getString(R.string.status_closed), Toast.LENGTH_LONG).show();
                    Prefs.putString("ticketstatus", "Closed");
                    finish();
                    startActivity(new Intent(TicketDetailActivity.this, MainActivity.class));
                } else if (message2.contains("Status changed to Resolved")) {
                    Toasty.success(TicketDetailActivity.this, getString(R.string.status_resolved), Toast.LENGTH_LONG).show();
                    Prefs.putString("ticketstatus", "Resolved");
                    finish();
                    startActivity(new Intent(TicketDetailActivity.this, MainActivity.class));
                }
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();

            }


        }


    }

//    /**
//     * This API is for replying to the particular ticket.
//     */
//    private class ReplyTicket extends AsyncTask<String, Void, String> {
//        int ticketID;
//        String cc;
//        String replyContent;
//
//        ReplyTicket(int ticketID, String replyContent) {
//            this.ticketID = ticketID;
//            this.cc = cc;
//            this.replyContent = replyContent;
//        }
//
//        protected String doInBackground(String... urls) {
//            return new Helpdesk().postReplyTicket(ticketID, replyContent);
//        }
//
//        protected void onPostExecute(String result) {
//            Log.d("reply", result + "");
//            progressDialog.dismiss();
//            if (result == null) {
//                Toasty.error(TicketDetailActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
//                return;
//            }
//            editTextReplyMessage.getText().clear();
//            exitReveal();
//            Toasty.success(TicketDetailActivity.this, getString(R.string.posted_reply), Toast.LENGTH_LONG).show();
//            Conversation conversation = (Conversation) adapter.getItem(0);
//            if (conversation != null)
//                conversation.refresh();
//        }
//    }
//
//     class FetchCollaboratorAssociatedWithTicket extends AsyncTask<String, Void, String> {
//        String ticketid;
//
//        FetchCollaboratorAssociatedWithTicket(String ticketid) {
//
//            this.ticketid = ticketid;
//        }
//
//        protected String doInBackground(String... urls) {
//            return new Helpdesk().postCollaboratorAssociatedWithTicket(ticketid);
//        }
//
//        protected void onPostExecute(String result) {
//            progressDialog.dismiss();
////            new FetchTicketDetail(Prefs.getString("TICKETid",null)).execute();
//            Prefs.putString("cameFromNotification","false");
//            //progressDialog.dismiss();
//            int noOfCollaborator = 0;
//            if (isCancelled()) return;
////            if (progressDialog.isShowing())
////                progressDialog.dismiss();
//
//            if (result == null) {
//                //Toasty.error(TicketDetailActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
////                Data data=new Data(0,"No recipients");
////                stringArrayList.add(data);
//                return;
//            }
//
//            try {
//                JSONObject jsonObject = new JSONObject(result);
//                JSONArray jsonArray = jsonObject.getJSONArray("collaborator");
//                if (jsonArray.length() == 0) {
//                    addCc.setText("Add cc");
//                    return;
//                } else {
//
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        noOfCollaborator++;
//
//                    }
//                    addCc.setText("Add cc" + "(" + noOfCollaborator + " Recipients)");
//                    //Toast.makeText(TicketDetailActivity.this, "noofcollaborators:" + noOfCollaborator, Toast.LENGTH_SHORT).show();
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    /**
     * Here we are initializing the view pager
     * for the conversation and detail fragment.
     */
    private void setupViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragmentConversation = new Conversation();
        fragmentDetail = new Detail();
        adapter.addFragment(fragmentConversation, getString(R.string.conversation));
        adapter.addFragment(fragmentDetail, getString(R.string.detail));
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(onPageChangeListener);
    }

    TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            viewPager.setCurrentItem(tab.getPosition(), true);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * This method is for controlling the FAB button.
         * @param position of the FAB button.
         */
        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    fab.show();
                    break;

                default:
                    fab.show();
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    /**
     * Here we are controlling the FAB reply and internal note option.
     *
     * @param type
     */
    void enterReveal(String type) {
        fab.setVisibility(View.GONE);
        final View myView = findViewById(R.id.reveal);
        int finalRadius = Math.max(myView.getWidth(), myView.getHeight());
        SupportAnimator anim =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
        if (type.equals("Reply")) {
            myView.setVisibility(View.VISIBLE);
            myView.findViewById(R.id.section_reply).setVisibility(View.VISIBLE);
            myView.findViewById(R.id.section_internal_note).setVisibility(View.GONE);
            overlay.setVisibility(View.VISIBLE);
        } else {
            myView.setVisibility(View.VISIBLE);
            myView.findViewById(R.id.section_reply).setVisibility(View.GONE);
            myView.findViewById(R.id.section_internal_note).setVisibility(View.VISIBLE);
            overlay.setVisibility(View.VISIBLE);
        }

        anim.start();
    }

    void exitReveal() {

        View myView = findViewById(R.id.reveal);
        fab.show();
        fabExpanded = false;
        myView.setVisibility(View.GONE);
        overlay.setVisibility(View.GONE);
//        int finalRadius = Math.max(myView.getWidth(), myView.getHeight());
//        SupportAnimator animator = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
//        animator.setInterpolator(new AccelerateDecelerateInterpolator());
//        animator.setDuration(300);
//        animator = animator.reverse();
//        animator.addListener(new SupportAnimator.AnimatorListener() {
//
//            @Override
//            public void onAnimationStart() {
//
//            }
//
//            @Override
//            public void onAnimationEnd() {
//                fab.show();
//                fabExpanded = false;
//                myView.setVisibility(View.GONE);
//               // overlay.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAnimationCancel() {
//
//            }
//
//            @Override
//            public void onAnimationRepeat() {
//
//            }
//
//        });
//        animator.start();

    }

//    public int dpToPx(int dp) {
//        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
//    }

    /**
     * Handling the back button here.
     */
    @Override
    public void onBackPressed() {

        if (!MainActivity.isShowing) {
            Log.d("isShowing", "false");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else Log.d("isShowing", "true");

        if (materialSheetFab.isSheetVisible()) {
            materialSheetFab.hideSheet();
        } else {
            super.onBackPressed();
        }
//        if (fabExpanded)
//            exitReveal();
//        else super.onBackPressed();
    }

    /**
     * While resuming it will check if the internet
     * is available or not.
     */
    @Override
    protected void onResume() {

        Log.d("onResume","CALLED");
        checkConnection();
        setupFab();
        fab.bringToFront();
        super.onResume();
        progressDialog.setMessage(getString(R.string.pleasewait));
            progressDialog.show();
//            setupFab();
       new FetchTicketDetail(Prefs.getString("TICKETid",null)).execute();
//        checkConnection();
//        if (InternetReceiver.isConnected()){
//            progressDialog.setMessage(getString(R.string.pleasewait));
//            progressDialog.show();
//            fab = (Fab) findViewById(R.id.fab);
//            fab.show();
//            new Thread(new Runnable() {
//                public void run(){
//                    Log.d("threadisrunning","true");
//                    new FetchTicketDetail(Prefs.getString("TICKETid",null)).execute();
//                }
//            }).start();
//        }


//        new Thread(new Runnable() {
//            public void run(){
//                Log.d("threadisrunning","true");
//                new FetchCollaboratorAssociatedWithTicket(Prefs.getString("TICKETid", null)).execute();
//            }
//        }).start();
    }
    @Override
    protected void onDestroy() {
        isShowing = false;
        super.onDestroy();

    }

    private void checkConnection() {
        boolean isConnected = InternetReceiver.isConnected();
        showSnackIfNoInternet(isConnected);
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

    private class FetchTicketDetail extends AsyncTask<String, Void, String> {
        String ticketID;
        String agentName;
        String title;
        FetchTicketDetail(String ticketID) {

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
                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                JSONObject jsonObject2=jsonObject1.getJSONObject("ticket");
                String ticketNumber=jsonObject2.getString("ticket_number");
                String statusName=jsonObject2.getString("status_name");
                String subject=jsonObject2.getString("title");
                JSONObject jsonObject3=jsonObject2.getJSONObject("from");
                String userName = jsonObject3.getString("first_name")+" "+jsonObject3.getString("last_name");
                if (userName.equals("")||userName.equals("null null")){
                    userName=jsonObject3.getString("user_name");
                    textviewAgentName.setText(userName);
                }
                else{
                    userName=jsonObject3.getString("first_name")+" "+jsonObject3.getString("last_name");
                    textviewAgentName.setText(userName);
                }
                if (!statusName.equals("null")||!statusName.equals("")){
                    textViewStatus.setText(statusName);
                }
                else{
                    textViewStatus.setVisibility(View.GONE);
                }


                textViewTitle.setText(ticketNumber);
                if (subject.startsWith("=?")){
                    title=subject.replaceAll("=?UTF-8?Q?","");
                    String newTitle=title.replaceAll("=E2=80=99","");
                    String finalTitle=newTitle.replace("=??Q?","");
                    String newTitle1=finalTitle.replace("?=","");
                    String newTitle2=newTitle1.replace("_"," ");
                    Log.d("new name",newTitle2);
                    textViewSubject.setText(newTitle2);
                }
                else if (!subject.equals("null")){
                    textViewSubject.setText(subject);
                }
                else if (subject.equals("null")){
                    textViewSubject.setText("");
                }


//                String assignee=jsonObject2.getString("assignee");
//                if (assignee.equals(null)||assignee.equals("null")||assignee.equals("")){
//                 textviewAgentName.setText(getString(R.string.unassigned));
//                }
//                else{
//                    JSONObject jsonObject3=jsonObject2.getJSONObject("assignee");
//                    try {
//                        if (jsonObject3.getString("first_name") != null&&jsonObject3.getString("last_name") != null) {
//                            //spinnerHelpTopics.setSelection(getIndex(spinnerHelpTopics, jsonObject1.getString("helptopic_name")));
//                            agentName=jsonObject3.getString("first_name") + " " + jsonObject3.getString("last_name");
//                            textviewAgentName.setText(agentName);
//
//                            //spinnerStaffs.setSelection(staffItems.indexOf("assignee_email"));
//                        }
//                        else{
//                            agentName=jsonObject3.getString("user_name");
//                            textviewAgentName.setText(agentName);
//                        }
//                        //spinnerHelpTopics.setSelection(Integer.parseInt(jsonObject1.getString("helptopic_id")));
//                    } catch (ArrayIndexOutOfBoundsException e){
//                        e.printStackTrace();
//                    } catch (Exception e) {
////                    spinnerHelpTopics.setVisibility(View.GONE);
////                    tv_helpTopic.setVisibility(View.GONE);
//                        e.printStackTrace();
//                    }
//                }

                //JSONObject jsonObject3=jsonObject2.getJSONObject("assignee");
//                if (jsonObject3.getString("first_name").equals("")&&jsonObject3.getString("last_name").equals("")){
//                    agentName=jsonObject3.getString("user_name");
//                }
//                else {
//                    agentName = jsonObject3.getString("first_name") + " " + jsonObject3.getString("last_name");
//                }

                Log.d("TITLE",subject);
                Log.d("TICKETNUMBER",ticketNumber);
                //String priority=jsonObject1.getString("priority_id");





            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();

    }


    //    /**
//     * Callback will be triggered when there is change in
//     * network connection
//     */
//    @Override
//    public void onNetworkConnectionChanged(boolean isConnected) {
//        showSnack(isConnected);
//    }

}