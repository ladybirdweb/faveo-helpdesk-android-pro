package co.helpdesk.faveo.pro.frontend.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import co.helpdesk.faveo.pro.frontend.fragments.TicketFragment;
import co.helpdesk.faveo.pro.frontend.fragments.UsersFragment;

public  class MyPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 2;

    public MyPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return TicketFragment.newInstance(0, "TICKETS");
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return UsersFragment.newInstance(1, "USERS");
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        String title="";
        if (position==0){
            title="Tickets " ;
        }
        else if (position==1){
            title="USERS";

        }
        return title;

    }

}