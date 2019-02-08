package co.helpdesk.faveo.pro.frontend.drawers;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.helpdesk.faveo.pro.CircleTransform;
import co.helpdesk.faveo.pro.Constants;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.UIUtils;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.pro.frontend.activities.CreateTicketActivity;
import co.helpdesk.faveo.pro.frontend.activities.LoginActivity;
import co.helpdesk.faveo.pro.frontend.activities.MainActivity;
import co.helpdesk.faveo.pro.frontend.activities.SettingsActivity;
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
    FragmentDrawerListener drawerListener;
    View layout;
    Context context;
    DataModel[] drawerItem;
    DrawerItemCustomAdapter drawerItemCustomAdapter;
    ConfirmationDialog confirmationDialog;
    //int count=0;
    ProgressDialog progressDialog;
    String title;
    static String token;
    int responseCodeForShow;
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
    TextView textViewuserName;
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
    @BindView(R.id.clientList)
    TextView textViewClientList;
    @BindView(R.id.clientImage)
    ImageView clientImage;
    @BindView(R.id.create_ticket)
    LinearLayout linearLayoutCreate;
    @BindView(R.id.client_list)
    LinearLayout linearClientList;
    @BindView(R.id.settingsoption)
    LinearLayout linearSettings;
    @BindView(R.id.helpSection)
    LinearLayout linearHelp;
    @BindView(R.id.about)
    LinearLayout linearLayoutAbout;
    @BindView(R.id.logout)
    LinearLayout linearLog;
    @BindView(R.id.settingimage)
    ImageView imageViewsettimg;
    @BindView(R.id.settingtext)
    TextView textViewsetting;
    @BindView(R.id.helpimage)
    ImageView imageviewhelp;
    @BindView(R.id.helptext)
    TextView textviewhelp;
    @BindView(R.id.aboutimage)
    ImageView imageviewabout;
    @BindView(R.id.abouttext)
    TextView textviewabout;
    @BindView(R.id.logoutimage)
    ImageView imageViewlogout;
    @BindView(R.id.logouttext)
    TextView textviewlogout;


    int option=5;
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
        layout.findViewById(R.id.client_list).setOnClickListener(this);
        layout.findViewById(R.id.settingsoption).setOnClickListener(this);
        layout.findViewById(R.id.helpSection).setOnClickListener(this);
        layout.findViewById(R.id.about).setOnClickListener(this);
        layout.findViewById(R.id.logout).setOnClickListener(this);
        drawerItem = new DataModel[5];
        ButterKnife.bind(this, layout);
        confirmationDialog=new ConfirmationDialog();
        drawerItem[0] = new DataModel(R.drawable.inbox_tickets,getString(R.string.inbox),Prefs.getString("inboxTickets",null));
        drawerItem[1] = new DataModel(R.drawable.my_ticket,getString(R.string.my_tickets),Prefs.getString("myTickets", null));
        drawerItem[2] = new DataModel(R.drawable.unassigned_ticket,getString(R.string.unassigned_tickets),Prefs.getString("unassignedTickets",null));
        drawerItem[3] = new DataModel(R.drawable.closed_ticket,getString(R.string.closed_tickets),Prefs.getString("closedTickets", null));
        drawerItem[4] = new DataModel(R.drawable.trash_ticket ,getString(R.string.trash),Prefs.getString("trashTickets", null));
        drawerItemCustomAdapter=new DrawerItemCustomAdapter(getActivity(),R.layout.list_view_item_row,drawerItem);
        listView.setAdapter(drawerItemCustomAdapter);
        progressDialog=new ProgressDialog(getActivity());
        drawerItemCustomAdapter.notifyDataSetChanged();
        //UIUtils.setListViewHeightBasedOnItems(listView);
        //UIUtils.setListViewHeightBasedOnItems(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment=null;
                //Toast.makeText(context, "Clicked at :"+position, Toast.LENGTH_SHORT).show();
                if (position==0){
                    option=0;
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
                }
                else if (position==1){
                    option=1;
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
                    option=2;
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
                    option=3;
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
                    option=4;
                    title = getString(R.string.trash);
                    fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
                    if (fragment == null)
                        fragment = new TrashTickets();
                    if (fragment != null) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, fragment);
                        fragmentTransaction.commit();
                        ((MainActivity) getActivity()).setActionBarTitle(title);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                    }
                }

            }
        });
//        IImageLoader imageLoader = new PicassoLoader();
//        imageLoader.loadImage(profilePic, Prefs.getString("PROFILE_PIC", null), Prefs.getString("USERNAME", " ").charAt(0) + "");
        try {
            String letter = Prefs.getString("profilePicture", null);
            Log.d("profilePicture", letter);
            if (letter.contains("jpg") || letter.contains("png") || letter.contains("jpeg")) {
                //profilePic.setColorFilter(getContext().getResources().getColor(R.color.white));
                //profilePic.setColorFilter(context.getResources().getColor(R.color.faveo), PorterDuff.Mode.SRC_IN);
                Picasso.with(context).load(letter).transform(new CircleTransform()).into(profilePic);
            }
            else {
                int color= Color.parseColor("#ffffff");
                String letter1 = String.valueOf(Prefs.getString("PROFILE_NAME", "").charAt(0));
                TextDrawable drawable = TextDrawable.builder()
                        .buildRound(letter1,color);
                //profilePic.setAlpha(0.2f);
                profilePic.setColorFilter(context.getResources().getColor(R.color.faveo), PorterDuff.Mode.SRC_IN);
                profilePic.setImageDrawable(drawable);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        userRole.setText(Prefs.getString("ROLE", ""));
        domainAddress.setText(Prefs.getString("BASE_URL", ""));
        textViewuserName.setText(Prefs.getString("PROFILE_NAME", ""));
        try {
            String cameFromSetting = Prefs.getString("cameFromSetting", null);
            if (cameFromSetting.equals("true")) {
                option = 5;
                textviewhelp.setTextColor(getResources().getColor(R.color.faveo));
                imageviewhelp.setColorFilter(getResources().getColor(R.color.faveo));
                linearHelp.setBackgroundColor(getResources().getColor(R.color.grey_200));

                textviewhelp.setTextColor(getResources().getColor(R.color.black));
                imageviewhelp.setColorFilter(getResources().getColor(R.color.grey_500));
                linearHelp.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                linearSettings.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                textViewsetting.setTextColor(getResources().getColor(R.color.black));
                imageViewsettimg.setColorFilter(getResources().getColor(R.color.grey_500));

                textviewabout.setTextColor(getResources().getColor(R.color.black));
                imageviewabout.setColorFilter(getResources().getColor(R.color.grey_500));
                linearLayoutAbout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                linearClientList.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                textViewClientList.setTextColor(getResources().getColor(R.color.black));
                clientImage.setColorFilter(getResources().getColor(R.color.grey_500));

                textviewlogout.setTextColor(getResources().getColor(R.color.black));
                imageViewlogout.setColorFilter(getResources().getColor(R.color.grey_500));
                linearLog.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                Prefs.putString("cameFromSetting", "false");
                drawerItemCustomAdapter.notifyDataSetChanged();
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        ticketList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listView.getVisibility()==View.VISIBLE){
                    listView.setVisibility(View.GONE);
                }
                else{
                    listView.setVisibility(View.VISIBLE);
                }
                //count++;

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

                new SendPostRequest().execute();
                new FetchDependency().execute();
                drawerItemCustomAdapter.notifyDataSetChanged();
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                new SendPostRequest().execute();
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }


        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
//        mDrawerToggle.setDrawerIndicatorEnabled(false);
//        mDrawerToggle.setHomeAsUpIndicator(getActivity().getDrawable(R.drawable.ic_menu_black_24dp));

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }
    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {
            try {

                URL url = new URL(Constants.URL + "authenticate"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("username", Prefs.getString("USERNAME", null));
                postDataParams.put("password", Prefs.getString("PASSWORD", null));
                Log.e("params",postDataParams.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                //MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    Log.d("ifresponseCode",""+responseCode);
                    BufferedReader in=new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    in.close();
                    return sb.toString();
                }
                else {
                    if (responseCode==400){
                        Log.d("cameInThisBlock","true");
                        responseCodeForShow=400;
                    }
                    else if (responseCode==405){
                        responseCodeForShow=405;
                    }
                    else if (responseCode==302){
                        responseCodeForShow=302;
                    }
                    Log.d("elseresponseCode",""+responseCode);
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }

        }


        @Override
        protected void onPostExecute(String result) {
            Log.d("resultFromNewCall",result);
                if (isAdded()) {
                    if (responseCodeForShow == 400) {
                        final Toast toast = Toasty.info(getActivity(), getString(R.string.urlchange), Toast.LENGTH_SHORT);
                        toast.show();
                        new CountDownTimer(10000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                toast.show();
                            }

                            public void onFinish() {
                                toast.cancel();
                            }
                        }.start();
                        Prefs.clear();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        return;
                    }


                    if (responseCodeForShow == 405) {
                        final Toast toast = Toasty.info(getActivity(), getString(R.string.urlchange), Toast.LENGTH_SHORT);
                        toast.show();
                        new CountDownTimer(10000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                toast.show();
                            }

                            public void onFinish() {
                                toast.cancel();
                            }
                        }.start();
                        Prefs.clear();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        return;
                    }


                    if (responseCodeForShow == 302) {
                        final Toast toast = Toasty.info(getActivity(), getString(R.string.urlchange), Toast.LENGTH_SHORT);
                        toast.show();
                        new CountDownTimer(10000, 1000) {
                            public void onTick(long millisUntilFinished) {
                                toast.show();
                            }

                            public void onFinish() {
                                toast.cancel();
                            }
                        }.start();
                        Prefs.clear();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        return;
                    }
                }

            try {
                JSONObject jsonObject=new JSONObject(result);
                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                token = jsonObject1.getString("token");
                Prefs.putString("TOKEN", token);
                JSONObject jsonObject2=jsonObject1.getJSONObject("user");
                String role=jsonObject2.getString("role");
                String profile_pic = jsonObject2.getString("profile_pic");
                Prefs.putString("profilePicture",profile_pic);
                if (role.equals("user")){
                    final Toast toast = Toasty.info(getActivity(), getString(R.string.roleChanged),Toast.LENGTH_SHORT);
                    toast.show();
                    new CountDownTimer(10000, 1000)
                    {
                        public void onTick(long millisUntilFinished) {toast.show();}
                        public void onFinish() {toast.cancel();}
                    }.start();
                    Prefs.clear();
                    //Prefs.putString("role",role);
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    //Toasty.info(getActivity(), getString(R.string.roleChanged), Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }
                String firstName = jsonObject2.getString("first_name");
                String lastName = jsonObject2.getString("last_name");
                String userName = jsonObject2.getString("user_name");
                String email=jsonObject2.getString("email");
                String clientname;
                if (firstName == null || firstName.equals(""))
                    clientname = userName;
                else
                    clientname = firstName + " " + lastName;
                Prefs.putString("clientNameForFeedback",clientname);
                Prefs.putString("emailForFeedback",email);
                Prefs.putString("PROFILE_NAME",clientname);

                try {
                    String letter = Prefs.getString("profilePicture", null);
                    Log.d("profilePicture", letter);
                    if (letter.contains("jpg") || letter.contains("png") || letter.contains("jpeg")) {
                        //profilePic.setColorFilter(getContext().getResources().getColor(R.color.white));
                        //profilePic.setColorFilter(context.getResources().getColor(R.color.faveo), PorterDuff.Mode.SRC_IN);
                        Picasso.with(context).load(letter).transform(new CircleTransform()).into(profilePic);
                    }
                    else {
                        int color= Color.parseColor("#ffffff");
                        String letter1 = String.valueOf(Prefs.getString("PROFILE_NAME", "").charAt(0));
                        ColorGenerator generator = ColorGenerator.MATERIAL;
                        TextDrawable drawable = TextDrawable.builder()
                                .buildRound(letter1,color);
                        //profilePic.setAlpha(0.2f);
                        profilePic.setColorFilter(context.getResources().getColor(R.color.faveo), PorterDuff.Mode.SRC_IN);
                        profilePic.setImageDrawable(drawable);
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
                userRole.setText(Prefs.getString("ROLE", ""));
                domainAddress.setText(Prefs.getString("BASE_URL", ""));
                textViewuserName.setText(Prefs.getString("PROFILE_NAME", ""));

                Log.d("TOKEN",token);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
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
            String state=Prefs.getString("403",null);
            try {
                if (state.equals("403") && !state.equals(null)) {
                    Toasty.warning(getActivity(), getString(R.string.permission), Toast.LENGTH_LONG).show();
                    Prefs.clear();
                    Intent intent=new Intent(getActivity(),LoginActivity.class);
                    Prefs.putString("403", "null");
                    startActivity(intent);
                    return;
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
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
                String state1=Prefs.getString("400",null);

                try {
                    if (state1.equals("badRequest")) {
                        Toasty.info(getActivity(), getString(R.string.apiDisabled), Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toasty.error(getActivity(), "Parsing Error!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }catch (NullPointerException e1){
                    e1.printStackTrace();
                }


            }
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
                option=6;
                //linearLayoutCreate.setBackgroundColor(getResources().getColor(R.color.grey_200));
                Prefs.putString("firstusername","null");
                Prefs.putString("lastusername","null");
                Prefs.putString("firstuseremail","null");
                Prefs.putString("firstusermobile","null");
                mDrawerLayout.closeDrawer(GravityCompat.START);
                Intent inte = new Intent(getContext(), CreateTicketActivity.class);
                startActivity(inte);
                break;
//            case R.id.settings:
//
////                Intent intent=new Intent(getContext(),SettingsActivity.class);
////                startActivity(intent);
//                break;
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
//                    fragment = new Profile();
//                break;
//            case R.id.trash_tickets:
//                title = getString(R.string.trash);
//                fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
//                if (fragment == null)
//                    fragment = new TrashTickets();
//                break;
            case R.id.client_list:
                option=6;
                linearClientList.setBackgroundColor(getResources().getColor(R.color.grey_200));
                Prefs.putString("normalclientlist","true");
                Prefs.putString("filtercustomer","true");
                title = getString(R.string.client_list);
                textViewClientList.setTextColor(getResources().getColor(R.color.faveo));
                clientImage.setColorFilter(getResources().getColor(R.color.faveo));

                linearSettings.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                textViewsetting.setTextColor(getResources().getColor(R.color.black));
                imageViewsettimg.setColorFilter(getResources().getColor(R.color.grey_500));

                textviewhelp.setTextColor(getResources().getColor(R.color.black));
                imageviewhelp.setColorFilter(getResources().getColor(R.color.grey_500));
                linearHelp.setBackgroundColor(getResources().getColor(R.color.colorAccent));



                textviewabout.setTextColor(getResources().getColor(R.color.black));
                imageviewabout.setColorFilter(getResources().getColor(R.color.grey_500));
                linearLayoutAbout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);

                textviewlogout.setTextColor(getResources().getColor(R.color.black));
                imageViewlogout.setColorFilter(getResources().getColor(R.color.grey_500));
                linearLog.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                if (fragment == null)
                    fragment = new ClientList();
                break;
            case R.id.settingsoption:
                option=6;
                textviewhelp.setTextColor(getResources().getColor(R.color.black));
                imageviewhelp.setColorFilter(getResources().getColor(R.color.grey_500));
                linearHelp.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                textviewabout.setTextColor(getResources().getColor(R.color.black));
                imageviewabout.setColorFilter(getResources().getColor(R.color.grey_500));
                linearLayoutAbout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                linearClientList.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                textViewClientList.setTextColor(getResources().getColor(R.color.black));
                clientImage.setColorFilter(getResources().getColor(R.color.grey_500));
                textviewlogout.setTextColor(getResources().getColor(R.color.black));
                imageViewlogout.setColorFilter(getResources().getColor(R.color.grey_500));
                linearLog.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                linearSettings.setBackgroundColor(getResources().getColor(R.color.grey_200));
                textViewsetting.setTextColor(getResources().getColor(R.color.faveo));
                imageViewsettimg.setColorFilter(getResources().getColor(R.color.faveo));
                title = getString(R.string.settings);
                fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
                if (fragment == null)
                    fragment = new Settings();
                break;
            case R.id.helpSection:
                option=6;
                textviewhelp.setTextColor(getResources().getColor(R.color.faveo));
                imageviewhelp.setColorFilter(getResources().getColor(R.color.faveo));
                linearHelp.setBackgroundColor(getResources().getColor(R.color.grey_200));

                linearSettings.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                textViewsetting.setTextColor(getResources().getColor(R.color.black));
                imageViewsettimg.setColorFilter(getResources().getColor(R.color.grey_500));

                textviewabout.setTextColor(getResources().getColor(R.color.black));
                imageviewabout.setColorFilter(getResources().getColor(R.color.grey_500));
                linearLayoutAbout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                linearClientList.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                textViewClientList.setTextColor(getResources().getColor(R.color.black));
                clientImage.setColorFilter(getResources().getColor(R.color.grey_500));

                textviewlogout.setTextColor(getResources().getColor(R.color.black));
                imageViewlogout.setColorFilter(getResources().getColor(R.color.grey_500));
                linearLog.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                Intent intent=new Intent(getActivity(),SettingsActivity.class);
                startActivity(intent);
//                title = getString(R.string.helpSection);
//                fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
//                if (fragment == null)
//                    fragment = new HelpSection();
                break;
            case R.id.about:
                option=6;
                textviewabout.setTextColor(getResources().getColor(R.color.faveo));
                imageviewabout.setColorFilter(getResources().getColor(R.color.faveo));
                linearLayoutAbout.setBackgroundColor(getResources().getColor(R.color.grey_200));


                textviewhelp.setTextColor(getResources().getColor(R.color.black));
                imageviewhelp.setColorFilter(getResources().getColor(R.color.grey_500));
                linearHelp.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                linearSettings.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                textViewsetting.setTextColor(getResources().getColor(R.color.black));
                imageViewsettimg.setColorFilter(getResources().getColor(R.color.grey_500));

                linearClientList.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                textViewClientList.setTextColor(getResources().getColor(R.color.black));
                clientImage.setColorFilter(getResources().getColor(R.color.grey_500));

                textviewlogout.setTextColor(getResources().getColor(R.color.black));
                imageViewlogout.setColorFilter(getResources().getColor(R.color.grey_500));
                linearLog.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                title = getString(R.string.about);
                fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
                if (fragment == null)
                    fragment = new About();
                break;
            case R.id.logout:
                option=6;
                textviewlogout.setTextColor(getResources().getColor(R.color.faveo));
                imageViewlogout.setColorFilter(getResources().getColor(R.color.faveo));
                linearLog.setBackgroundColor(getResources().getColor(R.color.grey_200));

                textviewlogout.setTextColor(getResources().getColor(R.color.black));
                imageViewlogout.setColorFilter(getResources().getColor(R.color.grey_500));
                linearLog.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                textviewhelp.setTextColor(getResources().getColor(R.color.black));
                imageviewhelp.setColorFilter(getResources().getColor(R.color.grey_500));
                linearHelp.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                linearSettings.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                textViewsetting.setTextColor(getResources().getColor(R.color.black));
                imageViewsettimg.setColorFilter(getResources().getColor(R.color.grey_500));

                linearClientList.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                textViewClientList.setTextColor(getResources().getColor(R.color.black));
                clientImage.setColorFilter(getResources().getColor(R.color.grey_500));
                try {
                    MyFirebaseInstanceIDService.sendRegistrationToServer("0");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mDrawerLayout.closeDrawers();
                drawerItemCustomAdapter.notifyDataSetChanged();
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

    public class DrawerItemCustomAdapter extends ArrayAdapter<DataModel> {
        Context mContext;
        int layoutResourceId;
        DataModel data[] = null;
        public DrawerItemCustomAdapter(Context mContext, int layoutResourceId, DataModel[] data) {

            super(mContext, layoutResourceId, data);
            this.layoutResourceId = layoutResourceId;
            this.mContext = mContext;
            this.data = data;
        }

        @Override
        public View getView(final int position, final View convertView, ViewGroup parent) {

            View listItem = convertView;

            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            listItem = inflater.inflate(layoutResourceId, parent, false);
            ImageView imageViewIcon = (ImageView) listItem.findViewById(R.id.imageView2);
            final TextView textViewName = (TextView) listItem.findViewById(R.id.inboxtv);
            TextView countNumber= (TextView) listItem.findViewById(R.id.inbox_count);

            DataModel folder = data[position];
            if (option==0){
                if (position==0){
                    listItem.setBackgroundColor(getResources().getColor(R.color.grey_200));
                    textViewName.setTextColor(getResources().getColor(R.color.faveo));
                    imageViewIcon.setColorFilter(getResources().getColor(R.color.faveo));
                    textviewlogout.setTextColor(getResources().getColor(R.color.black));
                    imageViewlogout.setColorFilter(getResources().getColor(R.color.grey_500));
                    linearLog.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                    textviewlogout.setTextColor(getResources().getColor(R.color.black));
                    imageViewlogout.setColorFilter(getResources().getColor(R.color.grey_500));
                    linearLog.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    textviewhelp.setTextColor(getResources().getColor(R.color.black));
                    imageviewhelp.setColorFilter(getResources().getColor(R.color.grey_500));
                    linearHelp.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                    linearSettings.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    textViewsetting.setTextColor(getResources().getColor(R.color.black));
                    imageViewsettimg.setColorFilter(getResources().getColor(R.color.grey_500));

                    linearClientList.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    textViewClientList.setTextColor(getResources().getColor(R.color.black));
                    clientImage.setColorFilter(getResources().getColor(R.color.grey_500));
                    textviewabout.setTextColor(getResources().getColor(R.color.black));
                    imageviewabout.setColorFilter(getResources().getColor(R.color.grey_500));
                    linearLayoutAbout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }

            }
            else if (option==1){
                if (position==1){
                    listItem.setBackgroundColor(getResources().getColor(R.color.grey_200));
                    textViewName.setTextColor(getResources().getColor(R.color.faveo));
                    imageViewIcon.setColorFilter(getResources().getColor(R.color.faveo));
                    textviewlogout.setTextColor(getResources().getColor(R.color.black));
                    imageViewlogout.setColorFilter(getResources().getColor(R.color.grey_500));
                    linearLog.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                    textviewlogout.setTextColor(getResources().getColor(R.color.black));
                    imageViewlogout.setColorFilter(getResources().getColor(R.color.grey_500));
                    linearLog.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    textviewhelp.setTextColor(getResources().getColor(R.color.black));
                    imageviewhelp.setColorFilter(getResources().getColor(R.color.grey_500));
                    linearHelp.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                    linearSettings.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    textViewsetting.setTextColor(getResources().getColor(R.color.black));
                    imageViewsettimg.setColorFilter(getResources().getColor(R.color.grey_500));

                    linearClientList.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    textViewClientList.setTextColor(getResources().getColor(R.color.black));
                    clientImage.setColorFilter(getResources().getColor(R.color.grey_500));
                    textviewabout.setTextColor(getResources().getColor(R.color.black));
                    imageviewabout.setColorFilter(getResources().getColor(R.color.grey_500));
                    linearLayoutAbout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }

            }
            else if (option==2){
                if (position==2){
                    listItem.setBackgroundColor(getResources().getColor(R.color.grey_200));
                    textViewName.setTextColor(getResources().getColor(R.color.faveo));
                    imageViewIcon.setColorFilter(getResources().getColor(R.color.faveo));
                    textviewlogout.setTextColor(getResources().getColor(R.color.black));
                    imageViewlogout.setColorFilter(getResources().getColor(R.color.grey_500));
                    linearLog.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                    textviewlogout.setTextColor(getResources().getColor(R.color.black));
                    imageViewlogout.setColorFilter(getResources().getColor(R.color.grey_500));
                    linearLog.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    textviewhelp.setTextColor(getResources().getColor(R.color.black));
                    imageviewhelp.setColorFilter(getResources().getColor(R.color.grey_500));
                    linearHelp.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                    linearSettings.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    textViewsetting.setTextColor(getResources().getColor(R.color.black));
                    imageViewsettimg.setColorFilter(getResources().getColor(R.color.grey_500));

                    linearClientList.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    textViewClientList.setTextColor(getResources().getColor(R.color.black));
                    clientImage.setColorFilter(getResources().getColor(R.color.grey_500));
                    textviewabout.setTextColor(getResources().getColor(R.color.black));
                    imageviewabout.setColorFilter(getResources().getColor(R.color.grey_500));
                    linearLayoutAbout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }

            }
            else if (option==3){
                if (position==3){
                    listItem.setBackgroundColor(getResources().getColor(R.color.grey_200));
                    textViewName.setTextColor(getResources().getColor(R.color.faveo));
                    imageViewIcon.setColorFilter(getResources().getColor(R.color.faveo));
                    textviewlogout.setTextColor(getResources().getColor(R.color.black));
                    imageViewlogout.setColorFilter(getResources().getColor(R.color.grey_500));
                    linearLog.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                    textviewlogout.setTextColor(getResources().getColor(R.color.black));
                    imageViewlogout.setColorFilter(getResources().getColor(R.color.grey_500));
                    linearLog.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    textviewhelp.setTextColor(getResources().getColor(R.color.black));
                    imageviewhelp.setColorFilter(getResources().getColor(R.color.grey_500));
                    linearHelp.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                    linearSettings.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    textViewsetting.setTextColor(getResources().getColor(R.color.black));
                    imageViewsettimg.setColorFilter(getResources().getColor(R.color.grey_500));

                    linearClientList.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    textViewClientList.setTextColor(getResources().getColor(R.color.black));
                    clientImage.setColorFilter(getResources().getColor(R.color.grey_500));
                    textviewabout.setTextColor(getResources().getColor(R.color.black));
                    imageviewabout.setColorFilter(getResources().getColor(R.color.grey_500));
                    linearLayoutAbout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }

            }
            else if (option==4){
                if (position==4){
                    listItem.setBackgroundColor(getResources().getColor(R.color.grey_200));
                    textViewName.setTextColor(getResources().getColor(R.color.faveo));
                    imageViewIcon.setColorFilter(getResources().getColor(R.color.faveo));
                    textviewlogout.setTextColor(getResources().getColor(R.color.black));
                    imageViewlogout.setColorFilter(getResources().getColor(R.color.grey_500));
                    linearLog.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                    textviewlogout.setTextColor(getResources().getColor(R.color.black));
                    imageViewlogout.setColorFilter(getResources().getColor(R.color.grey_500));
                    linearLog.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    textviewhelp.setTextColor(getResources().getColor(R.color.black));
                    imageviewhelp.setColorFilter(getResources().getColor(R.color.grey_500));
                    linearHelp.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                    linearSettings.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    textViewsetting.setTextColor(getResources().getColor(R.color.black));
                    imageViewsettimg.setColorFilter(getResources().getColor(R.color.grey_500));

                    linearClientList.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    textViewClientList.setTextColor(getResources().getColor(R.color.black));
                    clientImage.setColorFilter(getResources().getColor(R.color.grey_500));
                    textviewabout.setTextColor(getResources().getColor(R.color.black));
                    imageviewabout.setColorFilter(getResources().getColor(R.color.grey_500));
                    linearLayoutAbout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }

            }

            else if (option==5) {
                if (position == 0) {
                    listItem.setBackgroundColor(getResources().getColor(R.color.grey_200));
                    textViewName.setTextColor(getResources().getColor(R.color.faveo));
                    imageViewIcon.setColorFilter(getResources().getColor(R.color.faveo));
                    textviewlogout.setTextColor(getResources().getColor(R.color.black));
                    imageViewlogout.setColorFilter(getResources().getColor(R.color.grey_500));
                    linearLog.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                    textviewlogout.setTextColor(getResources().getColor(R.color.black));
                    imageViewlogout.setColorFilter(getResources().getColor(R.color.grey_500));
                    linearLog.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    textviewhelp.setTextColor(getResources().getColor(R.color.black));
                    imageviewhelp.setColorFilter(getResources().getColor(R.color.grey_500));
                    linearHelp.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                    linearSettings.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    textViewsetting.setTextColor(getResources().getColor(R.color.black));
                    imageViewsettimg.setColorFilter(getResources().getColor(R.color.grey_500));

                    linearClientList.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    textViewClientList.setTextColor(getResources().getColor(R.color.black));
                    clientImage.setColorFilter(getResources().getColor(R.color.grey_500));
                    textviewabout.setTextColor(getResources().getColor(R.color.black));
                    imageviewabout.setColorFilter(getResources().getColor(R.color.grey_500));
                    linearLayoutAbout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }
            }
            else{
                listItem.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                textViewName.setTextColor(getResources().getColor(R.color.black));
                imageViewIcon.setColorFilter(getResources().getColor(R.color.grey_500));
            }
            imageViewIcon.setImageResource(folder.getIcon());
            textViewName.setText(folder.getName());
            countNumber.setText(folder.getCount());
            drawerItemCustomAdapter.notifyDataSetChanged();



            return listItem;
        }

    }



    public interface FragmentDrawerListener {
        void onDrawerItemSelected(View view, int position);
    }
}

