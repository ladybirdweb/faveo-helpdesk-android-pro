package co.helpdesk.faveo.pro;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import co.helpdesk.faveo.pro.frontend.drawers.FragmentDrawer;

//import co.helpdesk.faveo.pro.frontend.adapters.DrawerItemCustomAdapter;

/**
 * Created by Lenovo on 6/29/2017.
 */

public class UIUtils {
    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        FragmentDrawer.DrawerItemCustomAdapter listAdapter = (FragmentDrawer.DrawerItemCustomAdapter) listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }
}
