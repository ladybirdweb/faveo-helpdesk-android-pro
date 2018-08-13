package co.helpdesk.faveo.pro.frontend.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.pixplicity.easyprefs.library.Prefs;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.ButterKnife;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.SharedPreference;
import co.helpdesk.faveo.pro.frontend.drawers.FragmentDrawer;
import co.helpdesk.faveo.pro.frontend.fragments.About;
import co.helpdesk.faveo.pro.frontend.fragments.ClientList;
import co.helpdesk.faveo.pro.frontend.fragments.HelpSection;
import co.helpdesk.faveo.pro.frontend.fragments.Settings;
import co.helpdesk.faveo.pro.frontend.fragments.tickets.ClosedTickets;
import co.helpdesk.faveo.pro.frontend.fragments.tickets.CreatedAtAsc;
import co.helpdesk.faveo.pro.frontend.fragments.tickets.CreatedAtDesc;
import co.helpdesk.faveo.pro.frontend.fragments.tickets.DueByAsc;
import co.helpdesk.faveo.pro.frontend.fragments.tickets.DueByDesc;
import co.helpdesk.faveo.pro.frontend.fragments.tickets.InboxTickets;
import co.helpdesk.faveo.pro.frontend.fragments.tickets.MyTickets;
import co.helpdesk.faveo.pro.frontend.fragments.tickets.SortByTicketNumberAscending;
import co.helpdesk.faveo.pro.frontend.fragments.tickets.SortByTicketNumberDescending;
import co.helpdesk.faveo.pro.frontend.fragments.tickets.SortByTicketPriorityAsc;
import co.helpdesk.faveo.pro.frontend.fragments.tickets.SortByTicketPriorityDesc;
import co.helpdesk.faveo.pro.frontend.fragments.tickets.TrashTickets;
import co.helpdesk.faveo.pro.frontend.fragments.tickets.UnassignedTickets;
import co.helpdesk.faveo.pro.frontend.fragments.tickets.UpdatedAtAsc;
import co.helpdesk.faveo.pro.frontend.fragments.tickets.UpdatedAtDesc;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.pro.model.MessageEvent;
import io.fabric.sdk.android.Fabric;


/**
 * This is the main activity where we are loading the inbox fragment
 * once log in success we will start this activity. Here we are loading the
 * navigation drawer item.
 */
public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener,
        ClosedTickets.OnFragmentInteractionListener,
        InboxTickets.OnFragmentInteractionListener,
        MyTickets.OnFragmentInteractionListener,
        TrashTickets.OnFragmentInteractionListener,
        UnassignedTickets.OnFragmentInteractionListener,
        About.OnFragmentInteractionListener,
        ClientList.OnFragmentInteractionListener,
        Settings.OnFragmentInteractionListener,UpdatedAtDesc.OnFragmentInteractionListener,
        UpdatedAtAsc.OnFragmentInteractionListener,DueByAsc.OnFragmentInteractionListener,DueByDesc.OnFragmentInteractionListener,
        SortByTicketNumberAscending.OnFragmentInteractionListener,SortByTicketNumberDescending.OnFragmentInteractionListener,
        SortByTicketPriorityAsc.OnFragmentInteractionListener,SortByTicketPriorityDesc.OnFragmentInteractionListener,CreatedAtAsc.OnFragmentInteractionListener,CreatedAtDesc.OnFragmentInteractionListener,HelpSection.OnFragmentInteractionListener{

    // The BroadcastReceiver that tracks network connectivity changes.
//    public InternetReceiver receiver = new InternetReceiver();

    protected boolean doubleBackToExitPressedOnce = false;
    //public static boolean isShowing = false;
    ArrayList<String> strings;
    Toolbar toolbar;
    Context context;
    private SharedPreference sharedPreferenceObj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferenceObj = new SharedPreference(MainActivity.this);
        Fabric.with(this, new Crashlytics());
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_main);
        Window window = MainActivity.this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.faveo));
        ButterKnife.bind(this);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Prefs.putString("querry1", "null");
        strings = new ArrayList<>();
        strings.add(0, "Sort by");
        strings.add(1, "Due by time");
        strings.add(2, "Priority");
        strings.add(3, "Created at");
        strings.add(4, "Updated at");
        strings.add(5, "Ticket title");
        strings.add(6, "Status");

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.menu_inbox);
        mToolbar.setNavigationIcon(R.drawable.ic_action_attach_file);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Prefs.putString("querry1", "null");


        FragmentDrawer drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);


        //mToolbar.setVisibility(View.GONE);

        /*
          Loading the inbox fragment here.
         */
        InboxTickets inboxTickets = new InboxTickets();
        //inboxTickets.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_body, inboxTickets);
        fragmentTransaction.commit();
        setActionBarTitle(getResources().getString(R.string.inbox));

        final Display display = getWindowManager().getDefaultDisplay();
        // Load our little droid guy
        final Drawable droid = ContextCompat.getDrawable(this, R.mipmap.ic_launcher);
        // Tell our droid buddy where we want him to appear
        final Rect droidTarget = new Rect(0, 0, droid.getIntrinsicWidth() * 2, droid.getIntrinsicHeight() * 2);
        // Using deprecated methods makes you look way cool
        droidTarget.offset(display.getWidth() / 2, display.getHeight() / 2);

        // We have a sequence of targets, so lets build it!
        final TapTargetSequence sequence = new TapTargetSequence(this)
                .targets(

                        // This tap target will target the back button, we just need to pass its containing toolbar
                        TapTarget.forToolbarNavigationIcon(mToolbar, getString(R.string.fullControl)).id(1)
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.faveo)
                                .targetCircleColor(android.R.color.white)
                                .transparentTarget(true)
                                .textColor(android.R.color.white).cancelable(false),
                        // Likewise, this tap target will target the search button
                        TapTarget.forToolbarMenuItem(mToolbar, R.id.actionsearch, getString(R.string.searchIcon), getString(R.string.searchcriteria))
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.faveo)
                                .targetCircleColor(android.R.color.white)
                                .transparentTarget(true)
                                .textColor(android.R.color.white)
                                .id(2).cancelable(false)
//                        TapTarget.forToolbarMenuItem(mToolbar, R.id.action_noti, "This is a notification icon", "You will get all the notification in your helpdesk from here.")
//                                .dimColor(android.R.color.black)
//                                .outerCircleColor(R.color.faveo)
//                                .targetCircleColor(android.R.color.white)
//                                .transparentTarget(true)
//                                .textColor(android.R.color.white)
//                                .id(3).cancelable(false)
                )
                .listener(new TapTargetSequence.Listener() {
                    // This listener will tell us when interesting(tm) events happen in regards
                    // to the sequence
                    @Override
                    public void onSequenceFinish() {
                        //((TextView) findViewById(R.id.educated)).setText("Congratulations! You're educated now!");
                        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);

                        // Setting Dialog Message
                        alertDialog.setMessage(R.string.educated);

                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.mipmap.ic_launcher);

                        // Setting Positive "Yes" Button
                        alertDialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to invoke YES event
                                //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
                        Log.d("TapTargetView", "Clicked on " + lastTarget.id());
                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                    }
                });
        if (sharedPreferenceObj.getApp_runFirst().equals("FIRST")) {
            // That's mean First Time Launch
            // After your Work , SET Status NO
            TapTargetView.showFor(this,                 // `this` is an Activity
                    TapTarget.forToolbarMenuItem(mToolbar,R.id.action_noti, "This is a notification icon", "You will get all the notification in your helpdesk from here.e")
                            // All options below are optional
                            .dimColor(android.R.color.black)
                            .outerCircleColor(R.color.faveo)
                            .targetCircleColor(android.R.color.white)
                            .textColor(android.R.color.white).cancelable(false),                  // Specify the target radius (in dp)
                    new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                        @Override
                        public void onTargetClick(TapTargetView view) {
                            super.onTargetClick(view);      // This call is optional
                            sequence.start();
                        }
                    });

            sharedPreferenceObj.setApp_runFirst("NO");
        } else {

            // App is not First Time Launch


        }
    }


    @Override
    protected void onDestroy() {
        //isShowing = false;
        super.onDestroy();
        }


    /**
     * This will handle the drawer item.
     * @param view
     * @param position
     */
    @Override
    public void onDrawerItemSelected(View view, int position) {
    }

    public void setActionBarTitle(final String title) {
        Toolbar toolbarTop = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbarTop.findViewById(R.id.title);
        mTitle.setText(title.toUpperCase());

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    /**
     * Handle action bar item clicks here. The action bar will
     * automatically handle clicks on the Home/Up button, so long
     * as you specify a parent activity in AndroidManifest.xml.
     * @param item items refer to the menu items.
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        if (id == R.id.action_search) {
//            startActivity(new Intent(MainActivity.this, SearchActivity.class));
//            return true;
//        }

        if (id == R.id.action_noti) {
            Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * While resuming it will check if the internet
     * is available or not.
     */
    @Override
    protected void onResume() {
        Log.d("OnResumeMainActivity","TRUE");
        Prefs.putString("cameFromNotification","false");
        Prefs.putString("ticketThread","");
        Prefs.putString("TicketRelated","");
        Prefs.putString("searchResult", "");
        Prefs.putString("searchUser","");
        Prefs.putString("querry","null");
        Prefs.putString("querry1","null");
        checkConnection();
        super.onResume();
        // register connection status listener
        //FaveoApplication.getInstance().setInternetListener(this);

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

    /**
     * Handling the back button here.
     * As if we clicking twice then it will
     * ask press one more time to exit,we are handling
     * the double back button pressing here.
     */
    @Override
    public void onBackPressed() {
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle(getString(R.string.confirmLogOut));

        // Setting Dialog Message
        alertDialog.setMessage(getString(R.string.confirmMessage));

        // Setting Icon to Dialog
        alertDialog.setIcon(R.mipmap.ic_launcher);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke YES event
                //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
finishAffinity();
                System.exit(0);

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

    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        //Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show();
//        Snackbar.make(findViewById(android.R.id.content), event.message, Snackbar.LENGTH_LONG).show();
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