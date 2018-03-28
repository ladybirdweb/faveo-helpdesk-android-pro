package co.helpdesk.faveo.pro.frontend.activities;

import android.app.SearchManager;
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
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.frontend.adapters.MyPagerAdapter;
import co.helpdesk.faveo.pro.frontend.adapters.TabsPagerAdapter;
import co.helpdesk.faveo.pro.frontend.fragments.TicketFragment;
import co.helpdesk.faveo.pro.frontend.fragments.UsersFragment;
import co.helpdesk.faveo.pro.frontend.fragments.ticketDetail.Conversation;
import co.helpdesk.faveo.pro.frontend.fragments.ticketDetail.Detail;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.pro.model.Data;


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
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
       setContentView(R.layout.activity_search);
//        ButterKnife.bind(this);
        vpPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout= (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(vpPager);
        setupViewPager(vpPager);

        //adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        //vpPager.setAdapter(adapterViewPager);
        //handleIntent(getIntent());
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        //ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        //myAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        //vpPager.setAdapter(myAdapter);
        imageViewback= (ImageView) toolbar.findViewById(R.id.image_search_back);
        searchView= (AutoCompleteTextView) toolbar.findViewById(R.id.edit_text_search);
        imageViewClearText= (ImageView) toolbar.findViewById(R.id.cleartext);
        imageViewClearText.setVisibility(View.GONE);
        //imageViewSearchIcon= (ImageView) toolbar.findViewById(R.id.searchIcon);
        colorList=new ArrayList<>();
        colorList.clear();
        String querry=Prefs.getString("querry",null);
        Log.d("querry",querry);
        if (querry.equals("null")){
            searchView.setText("");
        }
        else{
            searchView.setText(querry);
            //searchView.setCursorVisible(false);
        }
        String recentSuggestion=Prefs.getString("RecentSearh",null);
        try {
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
        }catch (NullPointerException e){
            e.printStackTrace();
        }
//        suggestionAdapter=new ArrayAdapter<String>(SearchActivity.this,R.layout.row,R.id.textView,colorList);
//        searchView.setAdapter(suggestionAdapter);
//        searchView.setDropDownWidth(1500);
//        searchView.setThreshold(1);
//        searchView.showDropDown();

//        colorList.add("Cat");
//        colorList.add("Dog");
//        colorList.add("Owl");
//        colorList.add("Rhyno");
//        colorList.add("Crow");
//        colorList.add("Cow");
//        try {
//            if (recentSuggestion.startsWith("[")) {
//                suggestionAdapter = new ArrayAdapter<String>(this, R.layout.row, R.id.textView, colorList);
//                //suggestionAdapter = new ArrayAdapter<>(SearchActivity.this, android.R.layout.simple_dropdown_item_1line, colorList);
//                searchView.setAdapter(suggestionAdapter);
//
//                //searchView.showDropDown();
//                searchView.setDropDownWidth(1100);
//                searchView.setThreshold(1);
//
//            }
//        }catch (NullPointerException e){
//            e.printStackTrace();
//        }

        searchView.addTextChangedListener(passwordWatcheredittextSubject);
//        else {
//            suggestionAdapter = new ArrayAdapter<String>(this, R.layout.row, R.id.textView, colorList);
//            searchView.showDropDown();
//            searchView.setDropDownWidth(1100);
//            searchView.setAdapter(suggestionAdapter);
//            searchView.setThreshold(1);
//        }

        //searchView.onActionViewExpanded();


//        imageViewSearchIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String query=searchView.getText().toString();
//                Log.d("CLICKED",query);
////                Toast.makeText(SearchActivity.this, "came here", Toast.LENGTH_SHORT).show();
////                Intent intent=new Intent(SearchActivity.this,MainActivity.class);
////                    startActivity(intent);
////                    colorList.add(query);
//
//                if (query.equals("")){
//                    Toast.makeText(SearchActivity.this, "field is empty", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                else{
//                    colorList.add(query);
//                    suggestionAdapter=new ArrayAdapter<String>(SearchActivity.this,R.layout.row,R.id.textView,colorList);
//                    searchView.setAdapter(suggestionAdapter);
//                    searchView.setDropDownWidth(1100);
//                    searchView.setThreshold(1);
//                    searchView.showDropDown();
//                    //Prefs.putString("RecentSearh",colorList.toString());
////                    Intent intent=new Intent(SearchActivity.this,SearchActivity.class);
////                    finish();
////                    startActivity(intent);
//                    Log.d("suggestion",colorList.toString());
//
//                }
//            }
//        });
        imageViewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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

                Prefs.putString("RecentSearh",colorList.toString());
                Log.d("suggestionss",colorList.toString());
                //Toast.makeText(SearchActivity.this, "Text is :"+searchView.getText().toString(), Toast.LENGTH_SHORT).show();
//                    Log.d("IME SEARCH",searchView.getText().toString());
                Intent intent=new Intent(SearchActivity.this,SearchActivity.class);
                finish();
                startActivity(intent);

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

                    Prefs.putString("RecentSearh",colorList.toString());
                    Log.d("suggestionss",colorList.toString());
                    //Toast.makeText(SearchActivity.this, "Text is :"+searchView.getText().toString(), Toast.LENGTH_SHORT).show();
//                    Log.d("IME SEARCH",searchView.getText().toString());
                    Intent intent=new Intent(SearchActivity.this,SearchActivity.class);
                    finish();
                    startActivity(intent);
                    //colorList.add(searchView.getText().toString());

                    //performSearch();
                    return true;
                }
                return false;
            }
        });

//        searchView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                suggestionAdapter=new ArrayAdapter<String>(SearchActivity.this,R.layout.row,R.id.textView,colorList);
//                searchView.setAdapter(suggestionAdapter);
//                searchView.setDropDownWidth(1090);
//                searchView.setThreshold(1);
//                searchView.showDropDown();
//                return false;
//            }
//        });
        searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    suggestionAdapter=new ArrayAdapter<String>(SearchActivity.this,R.layout.row,R.id.textView,colorList);
                    searchView.setAdapter(suggestionAdapter);
                    searchView.setDropDownWidth(1090);
                    searchView.setThreshold(1);
//                    searchView.showDropDown();
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

//        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//            // This method will be invoked when a new page becomes selected.
//            @Override
//            public void onPageSelected(int position) {
//                //Toast.makeText(SearchActivity.this,
//                       // "Selected page position: " + position, Toast.LENGTH_SHORT).show();
//            }
//
//            // This method will be invoked when the current page is scrolled
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                // Code goes here
//                if (position==1){
//
//                }
//            }
//
//            // Called when the scroll state changes:
//            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                // Code goes here
//            }
//        });



    }
    @Override
    public void onBackPressed() {
        if (!MainActivity.isShowing) {
            Log.d("isShowing", "false");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else Log.d("isShowing", "true");


        super.onBackPressed();

//        if (fabExpanded)
//            exitReveal();
//        else super.onBackPressed();
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new TicketFragment(), getString(R.string.tickets));
        adapter.addFrag(new UsersFragment(), getString(R.string.users));
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
            searchView.showDropDown();

            //Toast.makeText(TicketSaveActivity.this, "API called", Toast.LENGTH_SHORT).show();
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            term = searchView.getText().toString();
            if (term.equals("")){
                searchView.showDropDown();
                imageViewClearText.setVisibility(View.GONE);
                imageViewClearText.setClickable(false);
            }
            else{
                imageViewClearText.setVisibility(View.VISIBLE);
            }
//            if (colorList.contains(term)){
//                searchView.showDropDown();
//            }
//            else {
//                searchView.dismissDropDown();
//            }
//            if (InternetReceiver.isConnected()) {
//                if (term.contains(",")) {
//                    int pos = term.lastIndexOf(",");
//                    term = term.substring(pos + 1, term.length());
//                    Log.d("newTerm", term);
//                    arrayAdapterCC = new ArrayAdapter<>(collaboratorAdd.this, android.R.layout.simple_dropdown_item_1line, stringArrayList);
//                    new collaboratorAdd.FetchCollaborator(term.trim()).execute();
//                    autoCompleteTextViewUser.setAdapter(arrayAdapterCC);
//                }
////            Toast.makeText(collaboratorAdd.this, "term:"+term, Toast.LENGTH_SHORT).show();
//                else if (term.equals("")) {
//                    arrayAdapterCC = new ArrayAdapter<>(collaboratorAdd.this, android.R.layout.simple_dropdown_item_1line, stringArrayList);
//                    //new FetchCollaborator("s").execute();
//                    Data data = new Data(0, "No result found");
//                    stringArrayList.add(data);
////                autoCompleteTextViewCC.setAdapter(stringArrayAdapterCC);
////                stringArrayAdapterCC.notifyDataSetChanged();
////                autoCompleteTextViewCC.setThreshold(0);
////                autoCompleteTextViewCC.setDropDownWidth(1000);
//
//                } else {
//                    arrayAdapterCC = new ArrayAdapter<>(collaboratorAdd.this, android.R.layout.simple_dropdown_item_1line, stringArrayList);
//                    new collaboratorAdd.FetchCollaborator(term).execute();
//                    autoCompleteTextViewUser.setAdapter(arrayAdapterCC);
//
//
//                    //stringArrayAdapterCC.notifyDataSetChanged();
////                autoCompleteTextViewCC.setThreshold(0);
////                autoCompleteTextViewCC.setDropDownWidth(1000);
//
//                }
//
//
//                //buttonsave.setEnabled(true);
//            }
        }

        public void afterTextChanged(Editable s) {
            if (term.equals("")){
                imageViewClearText.setVisibility(View.GONE);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                searchView.showDropDown();
                imageViewClearText.setClickable(false);
            }
            else{
                imageViewClearText.setVisibility(View.VISIBLE);
                searchView.showDropDown();
                //searchView.showDropDown();
            }
        }
    };

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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}



