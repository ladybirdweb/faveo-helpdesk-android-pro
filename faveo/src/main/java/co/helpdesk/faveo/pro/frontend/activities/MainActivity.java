package co.helpdesk.faveo.pro.frontend.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.pixplicity.easyprefs.library.Prefs;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.ButterKnife;
import co.helpdesk.faveo.pro.LocaleHelper;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.frontend.drawers.FragmentDrawer;
import co.helpdesk.faveo.pro.frontend.fragments.About;
import co.helpdesk.faveo.pro.frontend.fragments.ClientList;
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
        SortByTicketPriorityAsc.OnFragmentInteractionListener,SortByTicketPriorityDesc.OnFragmentInteractionListener,CreatedAtAsc.OnFragmentInteractionListener,CreatedAtDesc.OnFragmentInteractionListener{

    // The BroadcastReceiver that tracks network connectivity changes.
//    public InternetReceiver receiver = new InternetReceiver();

    protected boolean doubleBackToExitPressedOnce = false;
    public static boolean isShowing = false;
    ArrayList<String> strings;
    ArrayList<String> strings1;
    Toolbar toolbar;
//    private ArrayList<String> mList = new ArrayList<>();
//    @BindView(R.id.sort_view)
//    RelativeLayout sortView;
//    @BindView(R.id.sorting_type_textview)
//    TextView sortTextview;
//    @BindView(R.id.arrow_imgView)
//    ImageView arrowDown;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isShowing = true;
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        
        // TODO: Move this to where you establish a user session
        //logUser();
//        if (BuildConfig.DEBUG) {
//            DebugDB.getAddressLog();
//            Log.d("Refreshed token: ", "" + FirebaseInstanceId.getInstance().getToken());
//        }

//        String nextPageURL = getIntent().getStringExtra("nextPageURL");
//        Bundle bundle = new Bundle();
//        bundle.putString("nextPageURL", nextPageURL);
        strings=new ArrayList<>();
        strings.add(0,"Sort by");
        strings.add(1,"Due by time");
        strings.add(2,"Priority");
        strings.add(3,"Created at");
        strings.add(4,"Updated at");
        strings.add(5,"Ticket title");
        strings.add(6,"Status");
//        Prefs.putString("came from filter","false");

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



//Initializing the bottomNavigationView
//        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
//        bottomNavigationView.setBackgroundColor(Color.parseColor("#cee0ef"));
//        bottomNavigationView.setOnNavigationItemSelectedListener(
//                new BottomNavigationView.OnNavigationItemSelectedListener() {
//                    @Override
//                    public boolean onNavigationItemSelected(MenuItem item) {
//                        switch (item.getItemId()) {
//                            case R.id.action_call:
//                                Toast.makeText(MainActivity.this, "call clicked", Toast.LENGTH_SHORT).show();
//                                break;
//                            case R.id.action_chat:
//                                Toast.makeText(MainActivity.this, "chat clicked", Toast.LENGTH_SHORT).show();
//                                break;
//                            case R.id.action_contact:
//                                Toast.makeText(MainActivity.this, "contact clicked", Toast.LENGTH_SHORT).show();
//                                break;
//                        }
//                        return false;
//                    }
//                });


//        getSupportActionBar().setTitle("Inbox");

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



//        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
//        builder1.setTitle("Faveo Pro");
//        builder1.setMessage("Welcome to FAVEO!");
//        builder1.setIcon(R.mipmap.ic_launcher);
//        builder1.setCancelable(true);
//
//        final AlertDialog dlg = builder1.create();
//
//        dlg.show();
//
//        final Timer t = new Timer();
//        t.schedule(new TimerTask() {
//            public void run() {
//                dlg.dismiss(); // when the task active then close the dialog
//                t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
//            }
//        }, 2000);

    }



//    private void logUser() {
//        // TODO: Use the current user's information
//        // You can call any combination of these three methods
//        Crashlytics.setUserIdentifier(Preference.getUserID());
//        Crashlytics.setUserEmail(Constants.URL);
//        Crashlytics.setUserName(Preference.getUsername());
//    }
//@Override
//protected void attachBaseContext(Context base) {
//    super.attachBaseContext(LocaleHelper.onAttach(base));
//}
    @Override
    protected void onDestroy() {
        isShowing = false;
        super.onDestroy();

    }

//    @OnClick(R.id.sort_view)
//    public void onClickSort() {
//        arrowDown.animate().rotation(180).start();
//
//        new BottomSheet.Builder(this).title("Sort by").sheet(R.menu.sort_menu).listener(new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which) {
//                    case R.id.action_date:
//                        sortTextview.setText("Due by date");
//                        break;
//                    case R.id.action_time:
//                        sortTextview.setText("Due by time");
//                        break;
//                    case R.id.action_status:
//                        sortTextview.setText("Status");
//                        break;
//                    case R.id.action_priority:
//                        sortTextview.setText("Priority");
//                        break;
//                }
//            }
//        }).setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                arrowDown.animate().rotation(0).start();
//            }
//        }).show();
//
//    }

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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        MenuItem item = menu.findItem(R.id.spinner);
//        final Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
//
//        ArrayAdapter<String> spinnerPriArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,strings); //selected item will look like a spinner set from XML
//        spinnerPriArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(spinnerPriArrayAdapter);
//
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                int pos = spinner.getSelectedItemPosition();
//
//                if (pos == 0) {
//                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.parseColor("#3da6d7"));
//                    return;
//                } else {
//                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.parseColor("#3da6d7"));
////                    ((TextView) adapterView.getChildAt(0)).setTextSize(5);
//                    Toast.makeText(MainActivity.this, "Position" + pos, Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//
//        return true;
//    }

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
        super.onResume();
        // register connection status listener
        //FaveoApplication.getInstance().setInternetListener(this);
        checkConnection();
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

//    /**
//     * Callback will be triggered when there is change in
//     * network connection
//     */
//    @Override
//    public void onNetworkConnectionChanged(boolean isConnected) {
//        showSnack(isConnected);
//    }

    /**
     * Handling the back button here.
     * As if we clicking twice then it will
     * ask press one more time to exit,we are handling
     * the double back button pressing here.
     */
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Snackbar.make(findViewById(android.R.id.content), R.string.press_again_exit, Snackbar.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2500);
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
