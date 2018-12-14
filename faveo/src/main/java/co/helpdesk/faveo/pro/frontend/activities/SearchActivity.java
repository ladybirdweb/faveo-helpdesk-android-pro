package co.helpdesk.faveo.pro.frontend.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pixplicity.easyprefs.library.Prefs;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.frontend.fragments.TicketFragment;
import co.helpdesk.faveo.pro.frontend.fragments.UsersFragment;
import co.helpdesk.faveo.pro.frontend.fragments.ticketdetail.Conversation;
import co.helpdesk.faveo.pro.frontend.fragments.ticketdetail.Detail;


public class SearchActivity extends AppCompatActivity implements
        Conversation.OnFragmentInteractionListener,
        Detail.OnFragmentInteractionListener {
    AutoCompleteTextView searchView;
    ImageView imageViewback;
    ImageView imageViewClearText;
    private ViewPager vpPager;
    ArrayList<String> colorList;
    ArrayAdapter<String> suggestionAdapter;
    Toolbar toolbar;
    String term;
    Context context;
    TabLayout tabLayout;
    String querry;
    RelativeLayout relativeLayout;
    public static boolean isShowing = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_search);
//        ButterKnife.bind(this);
        Window window = SearchActivity.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(SearchActivity.this,R.color.faveo));
        View view = SearchActivity.this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        vpPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout= (TabLayout) findViewById(R.id.tabs);
        relativeLayout= (RelativeLayout) findViewById(R.id.relativeSearch);
        tabLayout.setupWithViewPager(vpPager);
        setupViewPager(vpPager);
        try {
            if (Prefs.getString("cameFromClientList", null).equals("true")) {
                vpPager.setCurrentItem(1);
            } else if (Prefs.getString("cameFromClientList", null).equals("false")){
                vpPager.setCurrentItem(0);
            }
        }catch (NullPointerException e){

        }

        isShowing = true;
        tabLayout.setTabTextColors(
                ContextCompat.getColor(this, R.color.grey_500),
                ContextCompat.getColor(this, R.color.faveo)
        );

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()) {
                    case 0:
                        //Log.d("onSearchPage","true");
                        Prefs.putString("cameFromClientList","false");
                        //vpPager.getAdapter().notifyDataSetChanged();
                        break;
                    case 1:
                        //Log.d("onSearchPage","false");
                        Prefs.putString("cameFromClientList","true");
                        //vpPager.getAdapter().notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.d("onSearchPage","false");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        Prefs.putString("cameFromNotification","none");
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        imageViewback= (ImageView) toolbar.findViewById(R.id.image_search_back);
        searchView= (AutoCompleteTextView) toolbar.findViewById(R.id.edit_text_search);
        imageViewClearText= (ImageView) toolbar.findViewById(R.id.cleartext);
        imageViewClearText.setVisibility(View.GONE);
        colorList=new ArrayList<>();
        colorList.clear();

        suggestionAdapter=new ArrayAdapter<String>(SearchActivity.this,R.layout.row,R.id.textView,colorList);
        String querry=Prefs.getString("querry",null);
        Log.d("querry",querry);
        if (querry.equals("null")){
            searchView.setText("");
        }
        else{
            searchView.setText(querry);
        }
        String recentSuggestion=Prefs.getString("RecentSearh",null);
        try {
        if (!recentSuggestion.equals("")) {

                int pos = recentSuggestion.indexOf("[");
                int pos2 = recentSuggestion.lastIndexOf("]");
                String strin1 = recentSuggestion.substring(pos + 1, pos2);
                String[] namesList = strin1.split(",");
                for (String name : namesList) {
                    if (!colorList.contains(name)) {
                        colorList.add(name.trim());
                        Set set = new HashSet(colorList);
                        colorList.clear();
                        colorList.addAll(set);
                        }


                }
            }
            }catch (NullPointerException e) {
            e.printStackTrace();
        }
        searchView.addTextChangedListener(passwordWatcheredittextSubject);

        imageViewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String option=Prefs.getString("cameFromNotification",null);
                Log.d("cameFromNotification",option);
                switch (option) {
                    case "false": {
//                        Intent intent1=new Intent(SearchActivity.this,MainActivity.class);
//                        startActivity(intent1);
                        finish();
                        break;
                    }
                    case "none": {
                        finish();
                        break;
                    }
                    default: {
//                        Intent intent1=new Intent(SearchActivity.this,MainActivity.class);
//                        startActivity(intent1);
                        finish();
                        break;
                    }
                }
            }
        });
        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String querry=searchView.getText().toString();
                    Prefs.putString("querry",querry);
                    try {
                        String querry1 = URLEncoder.encode(querry, "utf-8");
                        Prefs.putString("querry1",querry1);
                        //Log.d("Msg", replyMessage);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    if (!colorList.contains(querry)){
                        colorList.add(searchView.getText().toString());
                    }
                    View view = SearchActivity.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    Prefs.putString("RecentSearh",colorList.toString());
                    Log.d("suggestionss",colorList.toString());


                    try {
                        if (Prefs.getString("cameFromClientList", null).equals("true")) {

                            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
                            adapter.addFrag(new TicketFragment(), getString(R.string.tickets));
                            adapter.addFrag(new UsersFragment(), getString(R.string.users));
                            adapter.notifyDataSetChanged();
                            vpPager.setAdapter(adapter);
                            vpPager.setCurrentItem(1);

                        } else if (Prefs.getString("cameFromClientList", null).equals("false")){
                            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
                            adapter.addFrag(new TicketFragment(), getString(R.string.tickets));
                            adapter.addFrag(new UsersFragment(), getString(R.string.users));
                            adapter.notifyDataSetChanged();
                            vpPager.setAdapter(adapter);
                            vpPager.setCurrentItem(0);
                        }
                    }catch (NullPointerException e){
                            e.printStackTrace();
                    }

                    //colorList.add(searchView.getText().toString());

                    //performSearch();
                    return true;
                }
                return false;
            }
        });

        searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    Display display = getWindowManager().getDefaultDisplay();

                    int width = display.getWidth();  // deprecated
                    Log.d("searchviewinfocus","true");
                    suggestionAdapter=new ArrayAdapter<String>(SearchActivity.this,R.layout.row,R.id.textView,colorList);
                    searchView.setAdapter(suggestionAdapter);
                    searchView.setDropDownWidth(width);
                    searchView.setThreshold(1);
                    //searchView.showDropDown();
                }
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Display display = getWindowManager().getDefaultDisplay();
                int width = display.getWidth();
                Log.d("searchviewinfocus","true");
                suggestionAdapter=new ArrayAdapter<String>(SearchActivity.this,R.layout.row,R.id.textView,colorList);
                searchView.setAdapter(suggestionAdapter);
                searchView.setDropDownWidth(width);
                searchView.setThreshold(1);
                searchView.showDropDown();
            }
        });

        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view1, int i, long l) {
                String querry=searchView.getText().toString();
                Prefs.putString("querry",querry);
                try {
                    String querry1 = URLEncoder.encode(querry, "utf-8");
                    Prefs.putString("querry1",querry1);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (!colorList.contains(querry)){
                    colorList.add(searchView.getText().toString());
                }
                View view = SearchActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                Prefs.putString("RecentSearh",colorList.toString());
                Log.d("suggestionss",colorList.toString());


                try {
                    if (Prefs.getString("cameFromClientList", null).equals("true")) {

                        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
                        adapter.addFrag(new TicketFragment(), getString(R.string.tickets));
                        adapter.addFrag(new UsersFragment(), getString(R.string.users));
                        adapter.notifyDataSetChanged();
                        vpPager.setAdapter(adapter);
                        vpPager.setCurrentItem(1);

                    } else if (Prefs.getString("cameFromClientList", null).equals("false")){
                        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
                        adapter.addFrag(new TicketFragment(), getString(R.string.tickets));
                        adapter.addFrag(new UsersFragment(), getString(R.string.users));
                        adapter.notifyDataSetChanged();
                        vpPager.setAdapter(adapter);
                        vpPager.setCurrentItem(0);
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        });

        imageViewClearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setText("");
                suggestionAdapter=new ArrayAdapter<String>(SearchActivity.this,R.layout.row,R.id.textView,colorList);
                searchView.setAdapter(suggestionAdapter);
                searchView.setDropDownWidth(1500);
                searchView.setThreshold(1);
                searchView.showDropDown();
                }
        });
        }
        @Override
    protected void onResume() {
        Prefs.putString("searchResult","");
        Log.d("calledOnResume","true");
        try {
            querry = Prefs.getString("querry", null);
            Log.d("QUERRYonResume",querry);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        if (querry.equals("")||querry.equals(null)||querry.equals("null")){
            searchView.setText("");
        }
        else{
            searchView.setText(querry);
        }
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        isShowing = false;
        super.onDestroy();
        }




    @Override
    public void onBackPressed() {
        Prefs.putString("querry","null");
        Prefs.putString("querry1","null");
        String option=Prefs.getString("cameFromNotification",null);
        Log.d("cameFromNotification",option);
        switch (option) {
            case "false": {
                Intent intent1=new Intent(SearchActivity.this,MainActivity.class);
                startActivity(intent1);
                finish();
                break;
            }
            case "none": {
                finish();
                break;
            }
            default: {
                Intent intent1=new Intent(SearchActivity.this,MainActivity.class);
                startActivity(intent1);
                finish();
                break;
            }
        }
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new TicketFragment(), getString(R.string.tickets));
        adapter.addFrag(new UsersFragment(), getString(R.string.users));
        adapter.notifyDataSetChanged();
        viewPager.setAdapter(adapter);

    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
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

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }




    final TextWatcher passwordWatcheredittextSubject = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            term = searchView.getText().toString();
            if (term.equals("")){
//                searchView.showDropDown();
                imageViewClearText.setVisibility(View.GONE);
                imageViewClearText.setClickable(false);
            }
            else{
                imageViewClearText.setVisibility(View.VISIBLE);
            }
        }

        public void afterTextChanged(Editable s) {
            if (term.equals("")){
                imageViewClearText.setVisibility(View.GONE);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                //searchView.showDropDown();
                imageViewClearText.setClickable(false);
            }
            else{
                imageViewClearText.setVisibility(View.VISIBLE);
                //searchView.showDropDown();
                //searchView.showDropDown();
            }
        }
    };

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


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}