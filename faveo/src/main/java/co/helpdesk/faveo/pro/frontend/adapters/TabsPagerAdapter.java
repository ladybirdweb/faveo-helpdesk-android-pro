package co.helpdesk.faveo.pro.frontend.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import co.helpdesk.faveo.pro.frontend.fragments.TicketFragment;
import co.helpdesk.faveo.pro.frontend.fragments.UsersFragment;

/**
 * Created by Amal on 27/03/2017.
 */

public class TabsPagerAdapter extends FragmentPagerAdapter {

    private int NUM_ITEMS = 2;
    private String[] titles= new String[]{"TICKETS", "USERS"};

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return  NUM_ITEMS ;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TicketFragment();
            case 1:
                return new UsersFragment();
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return  titles[position];
    }

}
