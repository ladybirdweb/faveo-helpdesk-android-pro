package co.helpdesk.faveo.pro.frontend.drawers;


import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import butterknife.BindView;
import butterknife.ButterKnife;
import co.helpdesk.faveo.pro.CircleTransform;
import co.helpdesk.faveo.pro.Constants;
import co.helpdesk.faveo.pro.FaveoApplication;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.UIUtils;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.pro.frontend.activities.CreateTicketActivity;
import co.helpdesk.faveo.pro.frontend.activities.HelpSection;
import co.helpdesk.faveo.pro.frontend.activities.LoginActivity;
import co.helpdesk.faveo.pro.frontend.activities.MainActivity;
import co.helpdesk.faveo.pro.frontend.activities.SettingsActivity;
import co.helpdesk.faveo.pro.frontend.adapters.DrawerItemCustomAdapter;
import co.helpdesk.faveo.pro.frontend.fragments.About;
import co.helpdesk.faveo.pro.frontend.fragments.ClientList;
import co.helpdesk.faveo.pro.frontend.fragments.ConfirmationDialog;
import co.helpdesk.faveo.pro.frontend.fragments.Settings;
import co.helpdesk.faveo.pro.frontend.fragments.tickets.ClosedTickets;
import co.helpdesk.faveo.pro.frontend.fragments.tickets.InboxTickets;
import co.helpdesk.faveo.pro.frontend.fragments.tickets.MyTickets;
import co.helpdesk.faveo.pro.frontend.fragments.tickets.TrashTickets;
import co.helpdesk.faveo.pro.frontend.fragments.tickets.UnassignedTickets;
import co.helpdesk.faveo.pro.frontend.services.MyFirebaseInstanceIDService;
import co.helpdesk.faveo.pro.model.DataModel;
import es.dmoral.toasty.Toasty;

/**
 * This is the fragment where we are going to handle the
 * drawer item events,for create ticket ,inbox,client list...
 */
public class FragmentDrawer extends Fragment implements View.OnClickListener {


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    View containerView;
    private static String[] titles = null;
    private FragmentDrawerListener drawerListener;
    View layout;
    Context context;
    DataModel[] drawerItem;
    DrawerItemCustomAdapter drawerItemCustomAdapter;
    ConfirmationDialog confirmationDialog;
    //int count=0;
    ProgressDialog progressDialog;
    String title;

    //    @BindView(R.id.inbox_count)
//    TextView inbox_count;
//    @BindView(R.id.my_tickets_count)
//    TextView my_tickets_count;
//    @BindView(R.id.trash_tickets_count)
//    TextView trash_tickets_count;
//    @BindView(R.id.unassigned_tickets_count)
//    TextView unassigned_tickets_count;
//    @BindView(R.id.closed_tickets_count)
//    TextView closed_tickets_count;
    @BindView(R.id.usernametv)
    TextView userName;
    @BindView(R.id.domaintv)
    TextView domainAddress;
    @BindView(R.id.roleTv)
    TextView userRole;
    @BindView(R.id.imageView_default_profile)
    ImageView profilePic;
    @BindView(R.id.listviewNavigation)
    ListView listView;
    @BindView(R.id.ticket_list)
    LinearLayout ticketList;




    public FragmentDrawer() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    public static List<NavDrawerItem> getData() {
        List<NavDrawerItem> data = new ArrayList<>();

        for (String title : titles) {
            NavDrawerItem navItem = new NavDrawerItem();
            navItem.setTitle(title);
            data.add(navItem);
        }
        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titles = (new String[]{"Item1", "Item2", "Item3", "Item4"});
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();

        layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        listView= (ListView) layout.findViewById(R.id.listviewNavigation);

        layout.findViewById(R.id.create_ticket).setOnClickListener(this);
//        layout.findViewById(R.id.inbox_tickets).setOnClickListener(this);
//        layout.findViewById(R.id.my_tickets).setOnClickListener(this);
//        layout.findViewById(R.id.unassigned_tickets).setOnClickListener(this);
//        layout.findViewById(R.id.closed_tickets).setOnClickListener(this);
//        layout.findViewById(R.id.trash_tickets).setOnClickListener(this);
        layout.findViewById(R.id.client_list).setOnClickListener(this);
        layout.findViewById(R.id.settings).setOnClickListener(this);
        layout.findViewById(R.id.about).setOnClickListener(this);
        layout.findViewById(R.id.logout).setOnClickListener(this);
        drawerItem = new DataModel[5];


        ButterKnife.bind(this, layout);
        confirmationDialog=new ConfirmationDialog();

        //inbox_count.setText(Prefs.getString("inboxTickets",null));
        //closed_tickets_count.setText(Prefs.getString("closedTickets", null));
        //unassigned_tickets_count.setText(Prefs.getString("unassignedTickets",null));
        //trash_tickets_count.setText(Prefs.getString("trashTickets", null));
        //my_tickets_count.setText(Prefs.getString("myTickets", null));
        drawerItem[0] = new DataModel(R.drawable.inbox_tickets,getString(R.string.inbox),Prefs.getString("inboxTickets",null));
        drawerItem[1] = new DataModel(R.drawable.my_ticket,getString(R.string.my_tickets),Prefs.getString("myTickets", null));
        drawerItem[2] = new DataModel(R.drawable.unassigned_ticket,getString(R.string.unassigned_tickets),Prefs.getString("unassignedTickets",null));
        drawerItem[3] = new DataModel(R.drawable.closed_ticket,getString(R.string.closed_tickets),Prefs.getString("closedTickets", null));
        drawerItem[4] = new DataModel(R.drawable.trash_ticket ,getString(R.string.trash),Prefs.getString("trashTickets", null));
        drawerItemCustomAdapter=new DrawerItemCustomAdapter(getActivity(),R.layout.list_view_item_row,drawerItem);
        listView.setAdapter(drawerItemCustomAdapter);
        progressDialog=new ProgressDialog(getActivity());
        drawerItemCustomAdapter.notifyDataSetChanged();
        UIUtils.setListViewHeightBasedOnItems(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment=null;
                //Toast.makeText(context, "Clicked at :"+position, Toast.LENGTH_SHORT).show();
                if (position==0){
                    title = getString(R.string.inbox);
                    fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
                    if (fragment == null)
                        fragment = new InboxTickets();
                    if (fragment != null) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, fragment);
                        // fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        ((MainActivity) getActivity()).setActionBarTitle(title);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                    }
                }else if (position==1){
                    title = getString(R.string.my_tickets);
                    fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
                    if (fragment == null)
                        fragment = new MyTickets();
                    if (fragment != null) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, fragment);
                        // fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        ((MainActivity) getActivity()).setActionBarTitle(title);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                    }
                }
                else if (position==2){
                    title = getString(R.string.unassigned_tickets);
                    fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
                    if (fragment == null)
                        fragment = new UnassignedTickets();
                    if (fragment != null) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, fragment);
                        // fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        ((MainActivity) getActivity()).setActionBarTitle(title);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                    }
                }else if (position==3){
                    title = getString(R.string.closed_tickets);
                    fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
                    if (fragment == null)
                        fragment = new ClosedTickets();
                    if (fragment != null) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, fragment);
                        // fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        ((MainActivity) getActivity()).setActionBarTitle(title);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                    }
                }else if (position==4){
                    title = getString(R.string.trash);
                    fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
                    if (fragment == null)
                        fragment = new TrashTickets();
                    if (fragment != null) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, fragment);
                        // fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        ((MainActivity) getActivity()).setActionBarTitle(title);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                    }
                }

            }
        });
//        IImageLoader imageLoader = new PicassoLoader();
//        imageLoader.loadImage(profilePic, Prefs.getString("PROFILE_PIC", null), Prefs.getString("USERNAME", " ").charAt(0) + "");
        String letter = String.valueOf(Prefs.getString("PROFILE_NAME", "").toUpperCase().charAt(0));
        if (Prefs.getString("PROFILE_NAME", "").contains("jpg")||Prefs.getString("PROFILE_NAME", "").contains("png")){
            Picasso.with(context).load(Prefs.getString("PROFILE_PIC", "")).transform(new CircleTransform()).into(profilePic);
        }
        else{
            ColorGenerator generator = ColorGenerator.MATERIAL;
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(letter, generator.getRandomColor());
            profilePic.setImageDrawable(drawable);
        }
        userRole.setText(Prefs.getString("ROLE", ""));
        domainAddress.setText(Prefs.getString("BASE_URL", ""));
        userName.setText(Prefs.getString("PROFILE_NAME", ""));

        ticketList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //count++;
                listView.setVisibility(View.VISIBLE);
            }
        });
        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * Whenever the this method is going to be called then the
     * async task will be cancelled .
     */
    @Override
    public void onStop() {
        // notice here that I keep a reference to the task being executed as a class member:
        if (this.new FetchDependency() != null && this.new FetchDependency().getStatus() == AsyncTask.Status.RUNNING)
            this.new FetchDependency().cancel(true);
        super.onStop();
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //Toast.makeText(getActivity(), "Drawer opened", Toast.LENGTH_SHORT).show();
                //listView.setAdapter(null);
                //progressDialog=new ProgressDialog(getActivity());

                new FetchDependency().execute();
                getActivity().invalidateOptionsMenu();



            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //listView.setAdapter(null);
                //progressDialog=new ProgressDialog(getActivity());

//                new FetchDependency().execute();
                //Toasty.normal(getActivity(), "Getting Information", Toast.LENGTH_LONG).show();
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                //listView.setAdapter(null);
                //progressDialog=new ProgressDialog(getActivity());

//                new FetchDependency().execute();
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }
    private class FetchDependency extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressDialog.setMessage("Loading your information");
        }

        protected String doInBackground(String... urls) {

            return new Helpdesk().getDependency();

        }

        protected void onPostExecute(String result) {
            Log.d("Depen Response : ", result + "");
            progressDialog.dismiss();

            if (result == null) {

                return;
            }

            try {

                JSONObject jsonObject = new JSONObject(result);
                JSONObject jsonObject1 = jsonObject.getJSONObject("result");

                int open = 0, closed = 0, trash = 0, unasigned = 0, my_tickets = 0;
                JSONArray jsonArrayTicketsCount = jsonObject1.getJSONArray("tickets_count");
                for (int i = 0; i < jsonArrayTicketsCount.length(); i++) {
                    String name = jsonArrayTicketsCount.getJSONObject(i).getString("name");
                    String count = jsonArrayTicketsCount.getJSONObject(i).getString("count");

                    switch (name) {
                        case "Open":
                            open = Integer.parseInt(count);
                            break;
                        case "Closed":
                            closed = Integer.parseInt(count);
                            break;
                        case "Deleted":
                            trash = Integer.parseInt(count);
                            break;
                        case "unassigned":
                            unasigned = Integer.parseInt(count);
                            break;
                        case "mytickets":
                            my_tickets = Integer.parseInt(count);
                            break;
                        default:
                            break;

                    }
                }


                if (open > 999)
                    Prefs.putString("inboxTickets", "999+");
                else
                    Prefs.putString("inboxTickets", open + "");

                if (closed > 999)
                    Prefs.putString("closedTickets", "999+");
                else
                    Prefs.putString("closedTickets", closed + "");

                if (my_tickets > 999)
                    Prefs.putString("myTickets", "999+");
                else
                    Prefs.putString("myTickets", my_tickets + "");

                if (trash > 999)
                    Prefs.putString("trashTickets", "999+");
                else
                    Prefs.putString("trashTickets", trash + "");

                if (unasigned > 999)
                    Prefs.putString("unassignedTickets", "999+");
                else
                    Prefs.putString("unassignedTickets", unasigned + "");
                if (isAdded()) {
                    drawerItem[0] = new DataModel(R.drawable.inbox_tickets, getString(R.string.inbox), Prefs.getString("inboxTickets", null));
                    drawerItem[1] = new DataModel(R.drawable.my_ticket, getString(R.string.my_tickets), Prefs.getString("myTickets", null));
                    drawerItem[2] = new DataModel(R.drawable.unassigned_ticket, getString(R.string.unassigned_tickets), Prefs.getString("unassignedTickets", null));
                    drawerItem[3] = new DataModel(R.drawable.closed_ticket, getString(R.string.closed_tickets), Prefs.getString("closedTickets", null));
                    drawerItem[4] = new DataModel(R.drawable.trash_ticket, getString(R.string.trash), Prefs.getString("trashTickets", null));
                    drawerItemCustomAdapter = new DrawerItemCustomAdapter(getActivity(), R.layout.list_view_item_row, drawerItem);
                    listView.setAdapter(drawerItemCustomAdapter);
                    drawerItemCustomAdapter.notifyDataSetChanged();
                    UIUtils.setListViewHeightBasedOnItems(listView);
//                drawerItemCustomAdapter.notifyDataSetChanged();
//

                }
//                else{
//                    Toasty.warning(getActivity(),"Something went wrong",Toast.LENGTH_LONG).show();
//                }
            }

            catch (JSONException e) {
                Toasty.error(getActivity(), "Parsing Error!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
//            AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
//            builder.setTitle("Welcome to FAVEO");
//            //builder.setMessage("After 2 second, this dialog will be closed automatically!");
//            builder.setCancelable(true);
//
//            final AlertDialog dlg = builder.create();
//
//            dlg.show();
//
//            final Timer t = new Timer();
//            t.schedule(new TimerTask() {
//                public void run() {
//                    dlg.dismiss(); // when the task active then close the dialog
//                    t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
//                }
//            }, 3000);

        }
    }


//    @Override
//    public void onResume() {
//        super.onResume();
//        new FetchDependency().execute();
//        drawerItemCustomAdapter.notifyDataSetChanged();
//    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        title = getString(R.string.app_name);
        switch (v.getId()) {

            case R.id.create_ticket:
//                title = getString(R.string.create_ticket);
//                fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
//                if (fragment == null)
//                    fragment = new CreateTicket();
                Prefs.putString("firstusername","null");
                Prefs.putString("lastusername","null");
                Prefs.putString("firstuseremail","null");
                Prefs.putString("firstusermobile","null");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                Intent inte = new Intent(getContext(), CreateTicketActivity.class);
                startActivity(inte);
                break;
            case R.id.settings:
                Intent intent=new Intent(getContext(),SettingsActivity.class);
                startActivity(intent);
                break;
//            case R.id.inbox_tickets:
//
//                title = getString(R.string.inbox);
//                fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
//                if (fragment == null)
//                    fragment = new InboxTickets();
//                break;
//            case R.id.my_tickets:
//                title = getString(R.string.my_tickets);
//                fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
//                if (fragment == null)
//                    fragment = new MyTickets();
//                break;
//            case R.id.unassigned_tickets:
//                title = getString(R.string.unassigned_tickets);
//                fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
//                if (fragment == null)
//                    fragment = new UnassignedTickets();
//                break;
//            case R.id.closed_tickets:
//                title = getString(R.string.closed_tickets);
//                fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
//                if (fragment == null)
//                    fragment = new ClosedTickets();
//                break;
//            case R.id.trash_tickets:
//                title = getString(R.string.trash);
//                fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
//                if (fragment == null)
//                    fragment = new TrashTickets();
//                break;
            case R.id.client_list:
                Prefs.putString("normalclientlist","true");
                Prefs.putString("filtercustomer","true");
                title = getString(R.string.client_list);
                fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
                if (fragment == null)
                    fragment = new ClientList();
                break;
//            case R.id.settings:
//                Intent intentSettings=new Intent(getContext(), SettingsActivity.class);
//                startActivity(intentSettings);
////                title = getString(R.string.settings);
////                fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
////                if (fragment == null)
////                    fragment = new Settings();
//                break;
            case R.id.about:
                title = getString(R.string.about);
                fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
                if (fragment == null)
                    fragment = new About();
                break;
            case R.id.logout:
                try {
                    MyFirebaseInstanceIDService.sendRegistrationToServer("0");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                confirmationDialog.show(getFragmentManager(),null);
//                if (RealmController.with(this).hasTickets()) {
//                    RealmController.with(this).clearAll();
//                }
//                NotificationManager notificationManager =
//                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//                notificationManager.cancelAll();
//                FaveoApplication.getInstance().clearApplicationData();
//                Prefs.clear();
//                getActivity().getSharedPreferences(Constants.PREFERENCE, Context.MODE_PRIVATE).edit().clear().apply();
//                Intent intent = new Intent(getActivity(), LoginActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//                Toasty.success(getActivity(), "Logged out successfully!", Toast.LENGTH_SHORT).show();

                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            // fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            ((MainActivity) getActivity()).setActionBarTitle(title);
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }



    public interface FragmentDrawerListener {
        void onDrawerItemSelected(View view, int position);
    }


}

