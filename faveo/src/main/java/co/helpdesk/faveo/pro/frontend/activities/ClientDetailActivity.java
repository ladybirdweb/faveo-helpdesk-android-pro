package co.helpdesk.faveo.pro.frontend.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import butterknife.BindView;
import butterknife.ButterKnife;
import co.helpdesk.faveo.pro.Constants;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.pro.frontend.fragments.client.ClosedTickets;
import co.helpdesk.faveo.pro.frontend.fragments.client.OpenTickets;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.pro.model.MessageEvent;
import co.helpdesk.faveo.pro.model.TicketGlimpse;
import es.dmoral.toasty.Toasty;

/**
 * In this activity we are showing the client details to the user.
 * We have used view pager for showing the open and closed tickets
 * so we are loading the view pager here.
 */
public class ClientDetailActivity extends AppCompatActivity implements
        OpenTickets.OnFragmentInteractionListener,
        ClosedTickets.OnFragmentInteractionListener {

    AsyncTask<String, Void, String> task;

    @BindView(R.id.imageView_default_profile)
    AvatarView imageViewClientPicture;

    @BindView(R.id.textView_client_name)
    TextView textViewClientName;

    @BindView(R.id.textView_client_email)
    TextView textViewClientEmail;

    @BindView(R.id.textView_client_phone)
    TextView textViewClientPhone;

    @BindView(R.id.textView_client_status)
    TextView textViewClientStatus;

    @BindView(R.id.textView_client_company)
    TextView textViewClientCompany;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    ViewPagerAdapter adapter;
    OpenTickets fragmentOpenTickets;
    ClosedTickets fragmentClosedTickets;
    String clientID, clientName;
    List<TicketGlimpse> listTicketGlimpse;
    ProgressDialog progressDialog;

    @Override
    public void onPause() {
        if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
            task.cancel(true);
            Log.d("Async Detail", "Cancelled");
        }
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile);
        ButterKnife.bind(this);
        Constants.URL = Prefs.getString("COMPANY_URL", "");
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        TextView mTitle = (TextView) mToolbar.findViewById(R.id.title);
        mTitle.setText(R.string.profile);

        setUpViews();
        Intent intent = getIntent();
        clientID = intent.getStringExtra("CLIENT_ID");
        // clientName = intent.getStringExtra("CLIENT_NAME");
//        textViewClientName.setText(clientName);
//        textViewClientEmail.setText(intent.getStringExtra("CLIENT_EMAIL"));
//        if (intent.getStringExtra("CLIENT_PHONE") == null || intent.getStringExtra("CLIENT_PHONE").equals(""))
//            textViewClientPhone.setVisibility(View.INVISIBLE);
//        else
//            textViewClientPhone.setText(intent.getStringExtra("CLIENT_PHONE"));
//        String clientPictureUrl = intent.getStringExtra("CLIENT_PICTURE");
//        if (intent.getStringExtra("CLIENT_COMPANY").equals("null") || intent.getStringExtra("CLIENT_COMPANY").equals(""))
//            textViewClientCompany.setText("");
//        else
//            textViewClientCompany.setText(intent.getStringExtra("CLIENT_COMPANY"));
//        textViewClientStatus.setText(intent.getStringExtra("CLIENT_ACTIVE").equals("1") ? getString(R.string.active) : getString(R.string.inactive));

        //IImageLoader imageLoader = new PicassoLoader();
        //imageLoader.loadImage(imageViewClientPicture, clientPictureUrl, clientName);

        if (InternetReceiver.isConnected()) {
            progressDialog.show();
            task = new FetchClientTickets(ClientDetailActivity.this);
            task.execute();


        } else Toasty.warning(this, getString(R.string.oops_no_internet), Toast.LENGTH_LONG).show();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(onTabSelectedListener);

    }

    /**
     * Handling the back button here.
     */
    @Override
    public void onBackPressed() {
        if (!MainActivity.isShowing) {
            Log.d("isShowing", "false");
            Intent intent = new Intent(ClientDetailActivity.this, SplashActivity.class);
            startActivity(intent);
        } else Log.d("isShowing", "true");
        super.onBackPressed();
    }

    /**
     * Handling the menu items here.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This async task is for getting the client details.We have
     * used two list for open and close ticket.
     */
    private class FetchClientTickets extends AsyncTask<String, Void, String> {
        Context context;
        List<TicketGlimpse> listOpenTicketGlimpse = new ArrayList<>();
        List<TicketGlimpse> listClosedTicketGlimpse = new ArrayList<>();

        FetchClientTickets(Context context) {
            this.context = context;
        }

        protected String doInBackground(String... urls) {
//            listTicketGlimpse = new ArrayList<>();
//            String result = new Helpdesk().getTicketsByUser(clientID);
//            if (result == null)
//                return null;
//            try {
//                JSONObject jsonObject = new JSONObject(result);
//
//                JSONArray jsonArray = jsonObject.getJSONArray("tickets");
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    int ticketID = Integer.parseInt(jsonArray.getJSONObject(i).getString("id"));
//                    boolean isOpen = true;
//                    String ticketNumber = jsonArray.getJSONObject(i).getString("ticket_number");
//                    String ticketSubject = jsonArray.getJSONObject(i).getString("title");
//                    try {
//                        isOpen = jsonArray.getJSONObject(i).getString("ticket_status_name").equals("Open");
//                        if (isOpen)
//                            listOpenTicketGlimpse.add(new TicketGlimpse(ticketID, ticketNumber, ticketSubject, true));
//                        else
//                            listClosedTicketGlimpse.add(new TicketGlimpse(ticketID, ticketNumber, ticketSubject, false));
//                    } catch (Exception e) {
//                        listOpenTicketGlimpse.add(new TicketGlimpse(ticketID, ticketNumber, ticketSubject, true));
//                    }
//                    listTicketGlimpse.add(new TicketGlimpse(ticketID, ticketNumber, ticketSubject, isOpen));
//                }
//            } catch (JSONException e) {
//                Toast.makeText(ClientDetailActivity.this, R.string.unexpected_error, Toast.LENGTH_LONG).show();
//                e.printStackTrace();
//            }
            // return "success";
            return new Helpdesk().getTicketsByUser(clientID);
        }

        protected void onPostExecute(String result) {
            if (isCancelled()) return;
            progressDialog.dismiss();
            if (result == null) return;
            listTicketGlimpse = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject requester = jsonObject.getJSONObject("requester");
                String firstname = requester.getString("first_name");
                String lastName = requester.getString("last_name");
                String username = requester.getString("user_name");
                String clientname;

                if (firstname == null || firstname.equals(""))
                    clientname = username;
                else
                    clientname = firstname + " " + lastName;

                textViewClientName.setText(clientname);
                textViewClientEmail.setText(requester.getString("email"));

                String phone = "";
                if (requester.getString("mobile") == null || requester.getString("mobile").equals(""))
                    textViewClientPhone.setVisibility(View.INVISIBLE);
                else
                    phone = requester.getString("mobile");

                textViewClientPhone.setText(phone);


                if (requester.getString("company").equals("null") || requester.getString("company").equals(""))
                    textViewClientCompany.setText("");
                else
                    textViewClientCompany.setText(requester.getString("company"));
                textViewClientStatus.setText(requester.getString("active" +
                        "").equals("1") ? getString(R.string.active) : getString(R.string.inactive));
                String clientPictureUrl = requester.getString("profile_pic");
                IImageLoader imageLoader = new PicassoLoader();
                imageLoader.loadImage(imageViewClientPicture, clientPictureUrl, clientname);
                JSONArray jsonArray = jsonObject.getJSONArray("tickets");
                for (int i = 0; i < jsonArray.length(); i++) {
                    int ticketID = Integer.parseInt(jsonArray.getJSONObject(i).getString("id"));
                    boolean isOpen = true;
                    String ticketNumber = jsonArray.getJSONObject(i).getString("ticket_number");
                    String ticketSubject = jsonArray.getJSONObject(i).getString("title");
                    try {
                        isOpen = jsonArray.getJSONObject(i).getString("ticket_status_name").equals("Open");
                        if (isOpen)
                            listOpenTicketGlimpse.add(new TicketGlimpse(ticketID, ticketNumber, ticketSubject, true));
                        else
                            listClosedTicketGlimpse.add(new TicketGlimpse(ticketID, ticketNumber, ticketSubject, false));
                    } catch (Exception e) {
                        listOpenTicketGlimpse.add(new TicketGlimpse(ticketID, ticketNumber, ticketSubject, true));
                    }
                    listTicketGlimpse.add(new TicketGlimpse(ticketID, ticketNumber, ticketSubject, isOpen));
                }
            } catch (JSONException e) {
                Toasty.error(ClientDetailActivity.this, getString(R.string.unexpected_error), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            fragmentOpenTickets.populateData(listOpenTicketGlimpse, clientName);
            fragmentClosedTickets.populateData(listClosedTicketGlimpse, clientName);
        }
    }

    /**
     * Here we are initializing the view pager and the
     * adapter for the view pager.
     */
    private void setupViewPager() {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragmentOpenTickets = new OpenTickets();
        fragmentClosedTickets = new ClosedTickets();
        adapter.addFragment(fragmentOpenTickets, getString(R.string.open_ticket));
        adapter.addFragment(fragmentClosedTickets, getString(R.string.closed_ticket));
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

        @Override
        public void onPageSelected(int position) {

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

    private void setUpViews() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.fetching_tickets));
        //imageViewClientPicture = (ImageView) findViewById(R.id.imageView_default_profile);
        //textViewClientName = (TextView) findViewById(R.id.textView_client_name);
        // textViewClientEmail = (TextView) findViewById(R.id.textView_client_email);
        // textViewClientPhone = (TextView) findViewById(R.id.textView_client_phone);
        //textViewClientCompany = (TextView) findViewById(R.id.textView_client_company);
        //textViewClientStatus = (TextView) findViewById(R.id.textView_client_status);
        // viewPager = (ViewPager) findViewById(R.id.viewpager);

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
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
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
