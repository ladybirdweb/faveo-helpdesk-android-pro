package co.helpdesk.faveo.pro.frontend.activities;

import android.app.ProgressDialog;
//import android.content.DialogInterface;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
//import android.support.v7.app.AlertDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.elyeproj.loaderviewlibrary.LoaderImageView;
import com.elyeproj.loaderviewlibrary.LoaderTextView;
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
import co.helpdesk.faveo.pro.model.Data;
import co.helpdesk.faveo.pro.model.MessageEvent;
//import co.helpdesk.faveo.pro.model.TicketDetail;
import co.helpdesk.faveo.pro.model.TicketDetail;
import es.dmoral.toasty.Toasty;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

/**
 * This splash activity is responsible for
 * getting the metadata of our faveo application from the dependency API.
 */
public class TicketDetailActivity extends AppCompatActivity implements
        Conversation.OnFragmentInteractionListener,
        Detail.OnFragmentInteractionListener,View.OnClickListener {

    ViewPager viewPager;
    public ViewPagerAdapter adapter;
    Conversation fragmentConversation;
    Detail fragmentDetail;
    Boolean fabExpanded = false;
    int cx, cy;
    Fab fab;
    private MaterialSheetFab materialSheetFab;
    View overlay;
    EditText editTextInternalNote, editTextCC, editTextReplyMessage;
    Button buttonCreate, buttonSend,buttonExit,buttonCancel;
    ProgressDialog progressDialog;
    private int statusBarColor;
    public static String ticketID, ticketNumber;
    TextView textView;
    String status;
    String title;
    LinearLayout linearLayout;
    TextView addCc;
    View viewpriority,viewCollapsePriority;
    ImageView imgaeviewBack;
    String cameFromNotification;
    public static boolean isShowing = false;
    LoaderTextView textViewStatus, textviewAgentName,textViewTitle,textViewDepartment;
    LoaderTextView textViewSubject;
    ArrayList<Data> statusItems;
    int id = 0;
    ProgressBar progressBar;
    FabSpeedDial fabSpeedDial;
    View view;
    CoordinatorLayout coordinatorLayout;
    private boolean isFabOpen;
    private Menu menu;
    ImageView loaderImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ticket_detail);
        Window window = TicketDetailActivity.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(TicketDetailActivity.this,R.color.faveo));
        view=findViewById(R.id.overlay);
        fabSpeedDial = (FabSpeedDial) findViewById(R.id.fab_speed_dial);
        loaderImageView= (ImageView) findViewById(R.id.collaboratorview);
//       fabSpeedDial.setOnClickListener(this);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
               int id=menuItem.getItemId();

               if (id==R.id.fab_reply){
                   Intent intent=new Intent(TicketDetailActivity.this,TicketReplyActivity.class);
                   startActivity(intent);
               }
               else if (id==R.id.fab_internalnote){
                   Intent intent=new Intent(TicketDetailActivity.this,InternalNoteActivity.class);
                   startActivity(intent);
               }
                //TODO: Start some activity
                return false;
            }
        });

        loaderImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prefs.putString("cameFromTicket","true");
                Intent intent=new Intent(TicketDetailActivity.this,collaboratorAdd.class);
                startActivity(intent);
                finish();
            }
        });


        //setupFab();
        //Prefs.putString("querry","null");
        statusItems=new ArrayList<>();
        JSONObject jsonObject1;
        //progressBar= (ProgressBar) findViewById(R.id.TicketDetailProgressbar);
        Data data;
        String json1 = Prefs.getString("DEPENDENCY", "");
        //statusItems.add(new Data(0, "Please select help topic"));
        try {
            jsonObject1 = new JSONObject(json1);
            JSONArray jsonArrayHelpTopics = jsonObject1.getJSONArray("status");
            for (int i = 0; i < jsonArrayHelpTopics.length(); i++) {
                Data data1 = new Data(Integer.parseInt(jsonArrayHelpTopics.getJSONObject(i).getString("id")), jsonArrayHelpTopics.getJSONObject(i).getString("name"));
                statusItems.add(data1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("ticketDetailOnCreate","True");
        Prefs.putString("TicketRelated","");
        ticketID=Prefs.getString("TICKETid",null);
        AppBarLayout mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbarTicketDetail);
        setSupportActionBar(mToolbar);
        //Log.d("cameFromNotification",cameFromNotification);
        ticketNumber = getIntent().getStringExtra("ticket_number");
        //linearLayout= (LinearLayout) findViewById(R.id.section_internal_note);
        //mToolbar = (Toolbar) findViewById(R.id.toolbarTicketDetail);
        textViewStatus = (LoaderTextView) mAppBarLayout.findViewById(R.id.status);
        textviewAgentName = (LoaderTextView) mAppBarLayout.findViewById(R.id.textViewagentName);
        textViewTitle = (LoaderTextView) mAppBarLayout.findViewById(R.id.title);
        textViewDepartment= (LoaderTextView) mAppBarLayout.findViewById(R.id.department);
        textViewSubject = (LoaderTextView) mToolbar.findViewById(R.id.subject);
        imgaeviewBack= (ImageView) mToolbar.findViewById(R.id.imageViewBackTicketDetail);
        viewpriority=mToolbar.findViewById(R.id.viewPriority);
        viewCollapsePriority=mAppBarLayout.findViewById(R.id.viewPriority1);
        //viewCollapsePriority.setBackgroundColor(Color.parseColor("#FF0000"));
        mToolbar.inflateMenu(R.menu.menu_main_new);
        isShowing=true;
        //Log.d("came into ticket detail","true");
        mToolbar.getMenu().getItem(0).setEnabled(false);

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
                    //showOption(R.id.action_info);
                } else if (isShow) {
                    isShow = false;
                    //hideOption(R.id.action_info);
                }
            }
        });
        addCc = (TextView) findViewById(R.id.addcc);
        if (InternetReceiver.isConnected()){
//            progressDialog=new ProgressDialog(this);
//            progressDialog.setMessage(getString(R.string.pleasewait));
//            progressDialog.show();
            new FetchCollaboratorAssociatedWithTicket(Prefs.getString("ticketId", null)).execute();
            new Thread(new Runnable() {
                public void run(){
                    Log.d("threadisrunning","true");
                    new FetchTicketDetail(Prefs.getString("TICKETid",null)).execute();
                }
            }).start();
            }
        imgaeviewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("cameFromnotification",Prefs.getString("cameFromNotification",null));
                String option=Prefs.getString("cameFromNotification",null);
                switch (option) {
                    case "true": {
                        Intent intent = new Intent(TicketDetailActivity.this, NotificationActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                    case "none": {
                        Intent intent1=new Intent(TicketDetailActivity.this,SearchActivity.class);
                        startActivity(intent1);
                        finish();
                        break;
                    }
                    case "false": {
//                        Intent intent1=new Intent(TicketDetailActivity.this,MainActivity.class);
//                        startActivity(intent1);
                        finish();
                        break;
                    }
                    case "client": {
                        Intent intent1=new Intent(TicketDetailActivity.this,ClientDetailActivity.class);
                        startActivity(intent1);
                        finish();
                        break;
                    }
                    default: {
//                        Intent intent1 = new Intent(TicketDetailActivity.this, MainActivity.class);
//                        startActivity(intent1);
                        finish();
                        break;
                    }
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
                switch (jsonArrayStaffs.getJSONObject(i).getString("name")) {
                    case "Open":
                        Prefs.putString("openid", jsonArrayStaffs.getJSONObject(i).getString("id"));
                        break;
                    case "Resolved":
                        Prefs.putString("resolvedid", jsonArrayStaffs.getJSONObject(i).getString("id"));
                        break;
                    case "Closed":
                        Prefs.putString("closedid", jsonArrayStaffs.getJSONObject(i).getString("id"));
                        break;
                    case "Deleted":
                        Prefs.putString("deletedid", jsonArrayStaffs.getJSONObject(i).getString("id"));
                        break;
                    case "Archived":
                        Prefs.putString("archivedid", jsonArrayStaffs.getJSONObject(i).getString("id"));
                        break;
                    case "Verified":
                        Prefs.putString("verifiedid", jsonArrayStaffs.getJSONObject(i).getString("id"));
                        break;
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
        editTextReplyMessage = (EditText) findViewById(R.id.editText_reply_message);
        buttonCreate = (Button) findViewById(R.id.button_create);
        buttonSend = (Button) findViewById(R.id.button_send);

        //overlay = findViewById(R.id.overlay);
//        overlay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //exitReveal();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_main_new, menu);
        for (int i=0;i<statusItems.size();i++){
            Data data=statusItems.get(i);
            menu.add(data.getName());
        }


//        mToolbar.getMenu();


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
        int id1 = item.getItemId();
        if (id1 == R.id.buttonsave) {
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
        else if (item.getItemId() == android.R.id.home) {
            Log.d("camehere","true");
            onBackPressed(); // close this activity and return to preview activity (if there is any)
        }
//
//        try {
//            if (item != null) {
//                item.getSubMenu().clearHeader();
//            }
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }

        else{

            for (int i=0;i<statusItems.size();i++){
                Data data=statusItems.get(i);
                if (data.getName().equals(item.toString())){
                    id=data.getID();
                    Log.d("ID",""+id);
                }
            }
            status = Prefs.getString("ticketstatus", null);

            if (status.equalsIgnoreCase(item.toString())){
                Toasty.warning(TicketDetailActivity.this, "Ticket is already in "+item.toString()+" state", Toast.LENGTH_SHORT).show();
            }
            else {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(TicketDetailActivity.this);

                // Setting Dialog Title
                alertDialog.setTitle(getString(R.string.changingStatus));

                // Setting Dialog Message
                alertDialog.setMessage(getString(R.string.statusConfirmation));

                // Setting Icon to Dialog
                alertDialog.setIcon(R.mipmap.ic_launcher);

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke YES event
                        //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                        new StatusChange(Integer.parseInt(ticketID), id).execute();
                        progressDialog.show();
                        progressDialog.setMessage(getString(R.string.pleasewait));

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
        Log.d("item", String.valueOf(item));

//        Fragment fragment = null;
//        title = getString(R.string.app_name);
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.


        return super.onOptionsItemSelected(item);
    }
    private void hideOption(int id) {
        MenuItem item = menu.findItem(id);
        item.setVisible(false);
    }

    private void showOption(int id) {
        MenuItem item = menu.findItem(id);
        item.setVisible(true);
    }

    /**
     * Sets up the Floating action button.
     */
//    private void setupFab() {
//
//        fab = (Fab) findViewById(R.id.fab);
//        fab.setVisibility(View.VISIBLE);
//        fab.show();
//        View sheetView = findViewById(R.id.fab_sheet);
//        View overlay1 = findViewById(R.id.overlay1);
//        int sheetColor = getResources().getColor(R.color.background_card);
//        int fabColor = getResources().getColor(R.color.theme_accent);
//
//        /**
//         * Create material sheet FAB.
//         */
//        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay1, sheetColor, fabColor);
//
//        /**
//         * Set material sheet event listener.
//         */
//        materialSheetFab.setEventListener(new MaterialSheetFabEventListener() {
//            @Override
//            public void onShowSheet() {
//                // Save current status bar color
//                statusBarColor = getStatusBarColor();
//                // Set darker status bar color to match the dim overlay
//                setStatusBarColor(getResources().getColor(R.color.theme_primary_dark2));
//            }
//
//            @Override
//            public void onHideSheet() {
//                // Restore status bar color
//                setStatusBarColor(statusBarColor);
//            }
//        });

        /**
         * Set material sheet item click listeners.
         */
//        findViewById(R.id.fab_sheet_item_reply).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                materialSheetFab.hideSheetThenFab();
//                Intent intent=new Intent(TicketDetailActivity.this,TicketReplyActivity.class);
//                startActivity(intent);
//                exitReveal();
//                //enterReveal("Reply");
//            }
//        });
//        findViewById(R.id.fab_sheet_item_note).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                materialSheetFab.hideSheetThenFab();
//                Intent intent=new Intent(TicketDetailActivity.this,InternalNoteActivity.class);
//                startActivity(intent);
//                exitReveal();
//                //enterReveal("Internal notes");
//            }
//        });
//
//        findViewById(R.id.fab_sheet_exit).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                materialSheetFab.hideSheet();
//            }
//        });

//        findViewById(R.id.closeFab).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                materialSheetFab.hideSheet();
//            }
//        });
//        findViewById(R.id.fab_sheet_item_photo).setOnClickListener(this);
//        findViewById(R.id.fab_sheet_item_note).setOnClickListener(this);
    //}

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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.fab_reply:

                animateFAB();
                Intent intent=new Intent(TicketDetailActivity.this,TicketReplyActivity.class);
                startActivity(intent);
                break;
            case R.id.fab_internalnote:
                Intent intent1=new Intent(TicketDetailActivity.this,InternalNoteActivity.class);
                startActivity(intent1);
                Log.d("Raj", "Fab 1");
                break;

        }
    }
    public void animateFAB(){

        if(fabSpeedDial.isMenuOpen()){

            view.setVisibility(View.VISIBLE);
            isFabOpen = false;
            Log.d("Raj", "close");

        } else {
            view.setVisibility(View.GONE);
            isFabOpen = true;
            Log.d("Raj","open");

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

            try{
                JSONObject jsonObject=new JSONObject(result);
                JSONArray jsonArray=jsonObject.getJSONArray("message");
                for (int i=0;i<jsonArray.length();i++){
                    String message=jsonArray.getString(i);
                    if (message.contains("Permission denied")){
                        Toasty.warning(TicketDetailActivity.this, getString(R.string.permission), Toast.LENGTH_LONG).show();
                        Prefs.putString("403", "null");
                        return;
                    }
                }
            } catch (JSONException e) {
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
                if (!message2.equals("null")){
                    Toasty.success(TicketDetailActivity.this, getString(R.string.successfullyChanged), Toast.LENGTH_LONG).show();
                    Prefs.putString("ticketstatus", "Deleted");
                    finish();
                    startActivity(new Intent(TicketDetailActivity.this, MainActivity.class));
                }

//                if (message2.contains("Status changed to Deleted")) {
//                    Toasty.success(TicketDetailActivity.this, getString(R.string.status_deleted), Toast.LENGTH_LONG).show();
//                    Prefs.putString("ticketstatus", "Deleted");
//                    finish();
//                    startActivity(new Intent(TicketDetailActivity.this, MainActivity.class));
//                } else if (message2.contains("Status changed to Open")) {
//                    Toasty.success(TicketDetailActivity.this, getString(R.string.status_opened), Toast.LENGTH_LONG).show();
//                    Prefs.putString("ticketstatus", "Open");
//                    finish();
//                    startActivity(new Intent(TicketDetailActivity.this, MainActivity.class));
//                } else if (message2.contains("Status changed to Closed")) {
//                    Toasty.success(TicketDetailActivity.this, getString(R.string.status_closed), Toast.LENGTH_LONG).show();
//                    Prefs.putString("ticketstatus", "Closed");
//                    finish();
//                    startActivity(new Intent(TicketDetailActivity.this, MainActivity.class));
//                } else if (message2.contains("Status changed to Resolved")) {
//                    Toasty.success(TicketDetailActivity.this, getString(R.string.status_resolved), Toast.LENGTH_LONG).show();
//                    Prefs.putString("ticketstatus", "Resolved");
//                    finish();
//                    startActivity(new Intent(TicketDetailActivity.this, MainActivity.class));
//                }
            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();

            }


        }


    }

public void fabOpen(){
        if (fabSpeedDial.isMenuOpen()){
            viewPager.setVisibility(View.GONE);
        }
        else{
            viewPager.setVisibility(View.VISIBLE);
        }
}
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
            fabSpeedDial.setRotation(positionOffset * 180.0f);

        }
        /**
         * This method is for controlling the FAB button.
         * @param position of the FAB button.
         */
        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    fabSpeedDial.show();
                    break;

                case 1:
                    fabSpeedDial.hide();
                    break;
                default:
                    fabSpeedDial.show();
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
    private class FetchCollaboratorAssociatedWithTicket extends AsyncTask<String, Void, String> {
        String ticketid;

        FetchCollaboratorAssociatedWithTicket(String ticketid) {

            this.ticketid = ticketid;
        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().postCollaboratorAssociatedWithTicket(ticketid);
        }

        protected void onPostExecute(String result) {

            int noOfCollaborator=0;
            if (isCancelled()) return;
            //strings.clear();

//            if (progressDialog.isShowing())
//                progressDialog.dismiss();

            if (result == null) {
                Toasty.error(TicketDetailActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
//                Data data=new Data(0,"No recipients");
//                stringArrayList.add(data);
                return;
            }

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("collaborator");
                if (jsonArray.length()==0){
                    loaderImageView.setVisibility(View.GONE);
                    return;
                }
                else{
                    loaderImageView.setVisibility(View.VISIBLE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            }

    }
    @Override
    public void onBackPressed() {
        Log.d("cameFromnotification",Prefs.getString("cameFromNotification",null));
        String option=Prefs.getString("cameFromNotification",null);
        switch (option) {
            case "true": {
                Intent intent = new Intent(TicketDetailActivity.this, NotificationActivity.class);
                startActivity(intent);
                finish();
                break;
            }
            case "none": {
                Intent intent1=new Intent(TicketDetailActivity.this,SearchActivity.class);
                startActivity(intent1);
                finish();
                break;
            }
            case "false": {
//                        Intent intent1=new Intent(TicketDetailActivity.this,MainActivity.class);
//                        startActivity(intent1);
                finish();
                break;
            }
            case "client": {
                Intent intent1=new Intent(TicketDetailActivity.this,ClientDetailActivity.class);
                startActivity(intent1);
                finish();
                break;
            }
            default: {
//                        Intent intent1 = new Intent(TicketDetailActivity.this, MainActivity.class);
//                        startActivity(intent1);
                finish();
                break;
            }
        }
//        if (!MainActivity.isShowing) {
//            Log.d("isShowing", "false");
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//        }
//        else {
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//            Log.d("isShowing", "true");
//        }
        super.onBackPressed();

//        if (materialSheetFab.isSheetVisible()) {
//            materialSheetFab.hideSheet();
//        } else {
//
//        }
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
        JSONObject jsonObject = null;
//        progressDialog.setMessage(getString(R.string.pleasewait));
//        progressDialog.show();
        //new FetchTicketDetail(Prefs.getString("TICKETid",null)).execute();
        Log.d("onResume","CALLED");
        Prefs.putString("TicketRelated","");
        if (Prefs.getString("TicketRelated",null).equals("")){
//                    progressDialog.setMessage(getString(R.string.pleasewait));
//                    progressDialog.show();
//            progressBar.setVisibility(View.VISIBLE);
            //new FetchCollaboratorAssociatedWithTicket(Prefs.getString("ticketId", null)).execute();
            new FetchTicketDetail(Prefs.getString("TICKETid",null)).execute();
        }
//        else{
//
//            String ticketInformation=Prefs.getString("TicketRelated",null);
//            Log.d("TicketRelated",ticketInformation);
//            try {
//                jsonObject = new JSONObject(ticketInformation);
//                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
//                JSONObject jsonObject2=jsonObject1.getJSONObject("ticket");
//                String ticketNumber=jsonObject2.getString("ticket_number");
//                String statusName=jsonObject2.getString("status_name");
//                String subject=jsonObject2.getString("title");
//                String department=jsonObject2.getString("dept_name");
//                String priorityColor=jsonObject2.getString("priority_color");
//                if (!priorityColor.equals("")||!priorityColor.equals("null")){
//                    viewpriority.setBackgroundColor(Color.parseColor(priorityColor));
//                    viewCollapsePriority.setBackgroundColor(Color.parseColor(priorityColor));
//                }
//                else{
//                    viewpriority.setVisibility(View.GONE);
//                    viewCollapsePriority.setVisibility(View.GONE);
//                }
//                JSONObject jsonObject3=jsonObject2.getJSONObject("from");
//                String userName = jsonObject3.getString("first_name")+" "+jsonObject3.getString("last_name");
//                if (userName.equals("")||userName.equals("null null")||userName.equals(" ")){
//                    userName=jsonObject3.getString("user_name");
//                    textviewAgentName.setText(userName);
//                }
//                else{
//                    userName=jsonObject3.getString("first_name")+" "+jsonObject3.getString("last_name");
//                    textviewAgentName.setText(userName);
//                }
//                if (!statusName.equals("null")||!statusName.equals("")){
//                    textViewStatus.setText(statusName);
//                }
//                else{
//                    textViewStatus.setVisibility(View.GONE);
//                }
//                textViewTitle.setText(ticketNumber);
//                if (subject.startsWith("=?")){
//                    title=subject.replaceAll("=?UTF-8?Q?","");
//                    String newTitle=title.replaceAll("=E2=80=99","");
//                    String second1=newTitle.replace("=C3=BA","");
//                    String third = second1.replace("=C2=A0", "");
//                    String finalTitle=third.replace("=??Q?","");
//                    String newTitle1=finalTitle.replace("?=","");
//                    String newTitle2=newTitle1.replace("_"," ");
//                    Log.d("new name",newTitle2);
//                    textViewSubject.setText(newTitle2);
//                }
//                else if (!subject.equals("null")){
//                    textViewSubject.setText(subject);
//                }
//                else if (subject.equals("null")){
//                    textViewSubject.setText("");
//                }
//                if (!department.equals("")||!department.equals("null")){
//                    textViewDepartment.setText(department);
//                }
//                else{
//                    textViewDepartment.setVisibility(View.GONE);
//                }
//
//                Log.d("TITLE",subject);
//                Log.d("TICKETNUMBER",ticketNumber);
//                //String priority=jsonObject1.getString("priority_id");
//
//
//
//
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        //}
        Prefs.putString("filePath","");
        checkConnection();
        //setupFab();
//        fab.bringToFront();
        super.onResume();
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
            //progressBar.setVisibility(View.GONE);
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
                String department=jsonObject2.getString("dept_name");
                String priorityColor=jsonObject2.getString("priority_color");
                if (!priorityColor.equals("")||!priorityColor.equals("null")){
                    viewpriority.setBackgroundColor(Color.parseColor(priorityColor));
                    viewCollapsePriority.setBackgroundColor(Color.parseColor(priorityColor));
                }
                else{
                    viewpriority.setVisibility(View.GONE);
                    viewCollapsePriority.setVisibility(View.GONE);
                }
                JSONObject jsonObject3=jsonObject2.getJSONObject("from");
                String userName = jsonObject3.getString("first_name")+" "+jsonObject3.getString("last_name");
                if (userName.equals("")||userName.equals("null null")||userName.equals(" ")){
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
                    String second1=newTitle.replace("=C3=BA","");
                    String third = second1.replace("=C2=A0", "");
                    String finalTitle=third.replace("=??Q?","");
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
                if (!department.equals("")||!department.equals("null")){
                    textViewDepartment.setText(department);
                }
                else{
                    textViewDepartment.setVisibility(View.GONE);
                }

                Log.d("TITLE",subject);
                Log.d("TICKETNUMBER",ticketNumber);
                //String priority=jsonObject1.getString("priority_id");
                new FetchCollaboratorAssociatedWithTicket(Prefs.getString("ticketId", null)).execute();




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