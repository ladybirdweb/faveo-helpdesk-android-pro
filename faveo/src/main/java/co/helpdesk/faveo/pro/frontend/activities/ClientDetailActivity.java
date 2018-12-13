package co.helpdesk.faveo.pro.frontend.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.elyeproj.loaderviewlibrary.LoaderTextView;
import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.helpdesk.faveo.pro.CircleTransform;
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
    ImageView imageViewClientPicture;

    @BindView(R.id.textView_client_name)
    LoaderTextView textViewClientName;

    @BindView(R.id.textView_client_email)
    LoaderTextView textViewClientEmail;

    @BindView(R.id.textView_client_phone)
    LoaderTextView textViewClientPhone;

    @BindView(R.id.textView_client_mobile)
    LoaderTextView textViewClientMobile;

    @BindView(R.id.textView_client_status)
    TextView textViewClientStatus;



    @BindView(R.id.viewpager)
    ViewPager viewPager;

    ViewPagerAdapter adapter;
    OpenTickets fragmentOpenTickets;
    ClosedTickets fragmentClosedTickets;
    public String clientID, clientName;
    List<TicketGlimpse> listTicketGlimpse;
    ProgressDialog progressDialog;
    ImageView imageViewClientEdit;
    ImageView imageViewBack;
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
        Window window = ClientDetailActivity.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(ClientDetailActivity.this,R.color.faveo));
        ButterKnife.bind(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        imageViewBack= (ImageView) findViewById(R.id.imageViewBackClient);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String option=Prefs.getString("cameFromNotification",null);
                switch (option) {
                    case "true": {
                        Intent intent = new Intent(ClientDetailActivity.this, NotificationActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                    case "none": {
                        Prefs.putString("cameFromClientList","false");
                        Intent intent1=new Intent(ClientDetailActivity.this,SearchActivity.class);
                        startActivity(intent1);
                        finish();
                        break;
                    }
                    case "false": {
                       finish();
                        break;
                    }

                    default: {
                       finish();
                        break;
                    }
                }



            }
        });
        imageViewClientEdit= (ImageView) findViewById(R.id.clientedit);
        Constants.URL = Prefs.getString("COMPANY_URL", "");
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        TextView mTitle = (TextView) mToolbar.findViewById(R.id.title);
        mTitle.setText(R.string.profile);
        setUpViews();
        textViewClientStatus= (TextView) findViewById(R.id.textView_client_status);
        final Intent intent = getIntent();
        clientID = intent.getStringExtra("CLIENT_ID");
        Prefs.putString("clientId",clientID);
        imageViewClientEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent1=new Intent(ClientDetailActivity.this,EditCustomer.class);
            startActivity(intent1);
            }
        });

        if (InternetReceiver.isConnected()) {

            progressDialog.show();
            task = new FetchClientTickets(ClientDetailActivity.this);
            task.execute();


        } else Toasty.warning(this, getString(R.string.oops_no_internet), Toast.LENGTH_LONG).show();



        //aSwitch.setEnabled(false);


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
        String option=Prefs.getString("cameFromNotification",null);
        switch (option) {
            case "true": {
                Intent intent = new Intent(ClientDetailActivity.this, NotificationActivity.class);
                startActivity(intent);
                finish();
                break;
            }
            case "none": {
                Prefs.putString("cameFromClientList","false");
                Intent intent1=new Intent(ClientDetailActivity.this,SearchActivity.class);
                startActivity(intent1);
                finish();
                break;
            }
            case "false": {
                finish();
                break;
            }

            default: {
                finish();
                break;
            }
        }
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
            return new Helpdesk().getTicketsByUser(clientID);
        }
        protected void onPostExecute(String result) {
            if (isCancelled()) return;
            progressDialog.dismiss();
            if (result == null) return;
            listTicketGlimpse = new ArrayList<>();
            try{
                JSONObject jsonObject = new JSONObject(result);
                String error=jsonObject.getString("error");
                if (error.equals("This is not a client")){
                    imageViewClientEdit.setVisibility(View.GONE);
                    Toasty.info(ClientDetailActivity.this, "This is not a client", Toast.LENGTH_LONG).show();
                    return;
                }
            }catch(JSONException e){
                e.printStackTrace();
            }
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject requester = jsonObject.getJSONObject("requester");
                String firstname = requester.getString("first_name");
                String lastName = requester.getString("last_name");
                String username = requester.getString("user_name");
                String clientname;
                String letter="A";
                if (firstname.equals("")&&lastName.equals("")){
                    letter= String.valueOf(username.toUpperCase().charAt(0));
                }
                else{
                    letter= String.valueOf(firstname.toUpperCase().charAt(0));
                }

                if (firstname == null || firstname.equals(""))
                    clientname = username;
                else
                    clientname = firstname + " " + lastName;
                    if (!clientname.equals("")){
                        textViewClientEmail.setVisibility(View.VISIBLE);
                        textViewClientName.setText(clientname);
                        textViewClientEmail.setText(requester.getString("email"));
                    }


                String phone = "";
                String mobile="";
                    phone = requester.getString("phone_number");
                mobile=requester.getString("mobile");
                String clientId=requester.getString("id");

                if (requester.getString("first_name").equals("")&& requester.getString("last_name").equals("")){
                    clientName= requester.getString("user_name");
                }
                else if ((requester.getString("first_name").equals("null")||
                        requester.getString("first_name").equals(""))&&
                        (requester.getString("last_name").equals("null")||requester.
                                getString("last_name").equals(""))){
                    clientName=requester.getString("user_name");
                }



                else if (!requester.getString("first_name").equals("")&&
                        requester.getString("last_name").equals("")){
                    clientName=requester.getString("first_name");
                }



                else if (requester.getString("first_name").equals("")&&
                        !requester.getString("last_name").equals("")){
                    clientName=requester.getString("last_name");
                }



                else {
                    clientName = requester.getString("first_name") + " " + requester.getString("last_name");
                }



                if (phone.equals("null")||phone.equals(" ")||phone.equals("Not available")||phone.equals("")){
                    textViewClientPhone.setVisibility(View.GONE);
                }else {
                    textViewClientPhone.setVisibility(View.VISIBLE);
                    textViewClientPhone.setText(phone);
                }
                if (mobile.equals("null")||mobile.equals(" ")||mobile.equals("Not available")){
                    textViewClientMobile.setVisibility(View.GONE);
                }
            else {
                    textViewClientMobile.setVisibility(View.VISIBLE);
                    textViewClientMobile.setText(mobile);
                }


                textViewClientStatus.setText(requester.getString("active" +
                        "").equals("1") ? getString(R.string.active) : getString(R.string.inactive));
                String clientPictureUrl = requester.getString("profile_pic");
                if (clientPictureUrl.contains(".jpg")||clientPictureUrl.contains(".png")||clientPictureUrl.contains(".jpeg")){
                    Picasso.with(context).load(clientPictureUrl).transform(new CircleTransform()).into(imageViewClientPicture);
                }
               else if (clientPictureUrl.equals("")){
                    imageViewClientPicture.setVisibility(View.GONE);

                }
                else{
                    int color=Color.parseColor("#ffffff");
<<<<<<< HEAD
=======
                    ColorGenerator generator = ColorGenerator.MATERIAL;
>>>>>>> master
                    TextDrawable drawable = TextDrawable.builder()
                            .buildRound(letter,color);
                    imageViewClientPicture.setColorFilter(context.getResources().getColor(R.color.faveo), PorterDuff.Mode.SRC_IN);
                    imageViewClientPicture.setImageDrawable(drawable);
                    //imageViewClientPicture.setImageResource(R.drawable.default_pic);
                }

                JSONArray jsonArray = jsonObject.getJSONArray("tickets");
                for (int i = 0; i < jsonArray.length(); i++) {
                    int ticketID = Integer.parseInt(jsonArray.getJSONObject(i).getString("id"));

                    boolean isOpen = true;
                    String ticketNumber = jsonArray.getJSONObject(i).getString("ticket_number");
                    String ticketSubject = jsonArray.getJSONObject(i).getString("title");
                    String status=jsonArray.getJSONObject(i).getString("ticket_status_name");

                    try {
                        isOpen = jsonArray.getJSONObject(i).getString("ticket_status_name").equals("Open");
                        if (isOpen)
                            listOpenTicketGlimpse.add(new TicketGlimpse(ticketID, ticketNumber, ticketSubject, true,status,clientId,clientName));
                        else
                            listClosedTicketGlimpse.add(new TicketGlimpse(ticketID, ticketNumber, ticketSubject, false,status,clientId,clientName));
                    } catch (Exception e) {
                        listOpenTicketGlimpse.add(new TicketGlimpse(ticketID, ticketNumber, ticketSubject, true,status,clientId,clientName));
                    }
                    listTicketGlimpse.add(new TicketGlimpse(ticketID, ticketNumber, ticketSubject, isOpen,status,clientId,clientName));
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

    }

    /**
     * While resuming it will check if the internet
     * is available or not.
     */
    @Override
    protected void onResume() {
        super.onResume();
        checkConnection();
//        if (InternetReceiver.isConnected()){
//            new FetchClientTickets(ClientDetailActivity.this).execute();
//        }
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
//        if (InternetReceiver.isConnected()){
//            new FetchClientTickets(ClientDetailActivity.this).execute();
//        }
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

}
