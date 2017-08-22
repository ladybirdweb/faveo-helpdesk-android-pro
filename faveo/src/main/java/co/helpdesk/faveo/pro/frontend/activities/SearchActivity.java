//package co.helpdesk.faveo.pro.frontend.activities;
//
//import android.app.SearchManager;
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.SearchRecentSuggestions;
//import android.support.design.widget.TabLayout;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.SearchView;
//import android.support.v7.widget.Toolbar;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import co.helpdesk.faveo.pro.R;
//import co.helpdesk.faveo.pro.frontend.MySuggestionProvider;
//import co.helpdesk.faveo.pro.frontend.fragments.search.SearchCustomerFragment;
//import co.helpdesk.faveo.pro.frontend.fragments.search.SearchTicketFragmnet;
//
//public class SearchActivity extends AppCompatActivity implements
//        SearchCustomerFragment.OnFragmentInteractionListener,
//        SearchTicketFragmnet.OnFragmentInteractionListener {
//
//    /**
//     * The {@link android.support.v4.view.PagerAdapter} that will provide
//     * fragments for each of the sections. We use a
//     * {@link FragmentPagerAdapter} derivative, which will keep every
//     * loaded fragment in memory. If this becomes too memory intensive, it
//     * may be best to switch to a
//     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
//     */
//    private SectionsPagerAdapter mSectionsPagerAdapter;
//
//    /**
//     * The {@link ViewPager} that will host the section contents.
//     */
//    private ViewPager mViewPager;
//
//    //    @BindView(R.id.searchView)
//    SearchView searchView;
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        setIntent(intent);
//
//        handledearch(intent);
//    }
//
//    public void handledearch(Intent intent) {
//        if (Intent.ACTION_SEARCH.equalsIgnoreCase(intent.getAction())) {
//            String query = intent.getStringExtra(SearchManager.QUERY);
//            SearchRecentSuggestions searchRecentSuggestions = new SearchRecentSuggestions(this,
//                    MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
//            searchRecentSuggestions.saveRecentQuery(query, null);
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search);
//        ButterKnife.bind(this);
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("");
//
//        handledearch(getIntent());
////
////        Intent intent  = getIntent();
////        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
////            String query = intent.getStringExtra(SearchManager.QUERY);
////            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
////                    MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
////            suggestions.saveRecentQuery(query, null);
////        }
//
//        // Do not iconify the widget; expand it by default
//        //searchView.requestFocus();
////        SearchManager searchManager = (SearchManager)
////                getSystemService(Context.SEARCH_SERVICE);
////
////        searchView.setSearchableInfo(searchManager.
////                getSearchableInfo(getComponentName()));
////
////        searchView.requestFocus();
////        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
////            @Override
////            public boolean onQueryTextSubmit(String query) {
////                if (query.length() >= 2) {
////                    //loadData(s);
////                    Toast.makeText(getBaseContext(), query, Toast.LENGTH_SHORT).show();
////                }
////                return true;
////            }
////
////            @Override
////            public boolean onQueryTextChange(String newText) {
////                return false;
////            }
////        });
////        //*** setOnQueryTextFocusChangeListener ***
////        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
////
////            @Override
////            public void onFocusChange(View v, boolean hasFocus) {
////                // TODO Auto-generated method stub
////
////                Toast.makeText(getBaseContext(), String.valueOf(hasFocus),
////                        Toast.LENGTH_SHORT).show();
////            }
////        });
//        // Create the adapter that will return a fragment for each of the three
//        // primary sections of the activity.
//        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
//
//        // Set up the ViewPager with the sections adapter.
//        mViewPager = (ViewPager) findViewById(R.id.container);
//        mViewPager.setAdapter(mSectionsPagerAdapter);
//
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(mViewPager);
//
////        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
////        fab.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
////            }
////        });
//
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the options menu from XML
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.search_menu, menu);
//
//        // Get the SearchView and set the searchable configuration
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//        // Assumes current activity is the searchable activity
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        menu.findItem(R.id.action_search).expandActionView();
//        searchView.setIconified(false);
//        searchView.setFocusable(true);
//
//
//        // Do not iconify the widget; expand it by default
//        searchView.requestFocus();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                if (query.length() >= 2) {
//                    //loadData(s);
//                    Toast.makeText(getBaseContext(), query, Toast.LENGTH_SHORT).show();
//                }
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                //Write your logic here
//                finish();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    @Override
//    public void onFragmentInteraction(Uri uri) {
//
//    }
//
//    /**
//     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
//     * one of the sections/tabs/pages.
//     */
//    public class SectionsPagerAdapter extends FragmentPagerAdapter {
//
//        SectionsPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//
//            if (position == 0) {
//                return SearchTicketFragmnet.newInstance();
//            } else {
//                return SearchCustomerFragment.newInstance();
//            }
//            // getItem is called to instantiate the fragment for the given page.
//            // Return a PlaceholderFragment (defined as a static inner class below).
//            //return PlaceholderFragment.newInstance(position + 1);
//        }
//
//        @Override
//        public int getCount() {
//            // Show 2 total pages.
//            return 2;
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            switch (position) {
//                case 0:
//                    return getString(R.string.tickets);
//                case 1:
//                    return getString(R.string.customers);
//            }
//            return null;
//        }
//    }
//
//    //    @Override
////    public boolean onKeyDown(int keyCode, KeyEvent event) {
////        //replaces the default 'Back' button action
////        if (keyCode == KeyEvent.KEYCODE_BACK) {
////            finish();
////        }
////        return true;
////    }
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        this.finish();
//    }
//
//    /**
//     * A placeholder fragment containing a simple view.
//     */
//    public static class PlaceholderFragment extends Fragment {
//        /**
//         * The fragment argument representing the section number for this
//         * fragment.
//         */
//        private static final String ARG_SECTION_NUMBER = "section_number";
//
//        @BindView(R.id.cardList)
//        RecyclerView recyclerView;
//        @BindView(R.id.swipeRefresh)
//        SwipeRefreshLayout swipeRefresh;
//
//        public PlaceholderFragment() {
//        }
//
//        /**
//         * Returns a new instance of this fragment for the given section
//         * number.
//         */
//        public static PlaceholderFragment newInstance(int sectionNumber) {
//            PlaceholderFragment fragment = new PlaceholderFragment();
//            Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_recycler, container, false);
//            ButterKnife.bind(this, rootView);
//            swipeRefresh.setColorSchemeResources(R.color.faveo_blue);
//            swipeRefresh.setRefreshing(false);
//
//            recyclerView.setHasFixedSize(false);
//            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//            recyclerView.setLayoutManager(linearLayoutManager);
////            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
////            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
//            return rootView;
//        }
//    }
//}
