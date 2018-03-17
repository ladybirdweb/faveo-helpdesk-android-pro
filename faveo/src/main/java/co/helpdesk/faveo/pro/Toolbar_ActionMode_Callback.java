//package co.helpdesk.faveo.pro;
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.Build;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.view.MenuItemCompat;
//import android.support.v7.view.ActionMode;
//import android.util.Log;
//import android.util.SparseBooleanArray;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.Toast;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import co.helpdesk.faveo.pro.frontend.activities.MainActivity;
//import co.helpdesk.faveo.pro.frontend.adapters.TicketOverviewAdapter;
//import co.helpdesk.faveo.pro.frontend.fragments.tickets.InboxTickets;
//import co.helpdesk.faveo.pro.model.TicketOverview;
//
///**
// * Created by SONU on 22/03/16.
// */
//public class Toolbar_ActionMode_Callback implements ActionMode.Callback {
//
//    private Context context;
//    private TicketOverviewAdapter recyclerView_adapter;
//    private ArrayList<TicketOverview> message_models;
//    private boolean isListViewFragment;
//
//
//    public Toolbar_ActionMode_Callback(Context context, TicketOverviewAdapter ticketOverviewAdapter, TicketOverviewAdapter recyclerView_adapter, List<TicketOverview> message_models, boolean b) {
//        this.context = context;
//        this.recyclerView_adapter = recyclerView_adapter;
//        this.message_models = (ArrayList<TicketOverview>) message_models;
//        this.isListViewFragment = isListViewFragment;
//    }
//
//    @Override
//    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//        mode.getMenuInflater().inflate(R.menu.multiplemenuInbox, menu);//Inflate the menu over action mode
//        return true;
//    }
//
//    @Override
//    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//
//        //Sometimes the meu will not be visible so for that we need to set their visibility manually in this method
//        //So here show action menu according to SDK Levels
//        if (Build.VERSION.SDK_INT < 11) {
//            MenuItemCompat.setShowAsAction(menu.findItem(R.id.actionclosed), MenuItemCompat.SHOW_AS_ACTION_NEVER);
////            MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_copy), MenuItemCompat.SHOW_AS_ACTION_NEVER);
////            MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_forward), MenuItemCompat.SHOW_AS_ACTION_NEVER);
//        } else {
//            menu.findItem(R.id.actionclosed).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
////            menu.findItem(R.id.action_copy).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
////            menu.findItem(R.id.action_forward).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//        }
////
//        return true;
//    }
//
//    @Override
//    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.actionclosed:
////                Toast.makeText(, "You selected close menu.", Toast.LENGTH_SHORT).show();//Show toast
//                Log.d("clicked on closed","closed");
//                //mode.finish();
//                break;
////            case R.id.action_copy:
////
////                //Get selected ids on basis of current fragment action mode
////
////                Toast.makeText(context, "You selected Copy menu.", Toast.LENGTH_SHORT).show();//Show toast
////                mode.finish();//Finish action mode
////                break;
////            case R.id.action_forward:
////                Toast.makeText(context, "You selected Forward menu.", Toast.LENGTH_SHORT).show();//Show toast
////                mode.finish();//Finish action mode
////                break;
//
//
//        }
//        return false;
//    }
//
//
//    @Override
//    public void onDestroyActionMode(ActionMode mode) {
//
//        //When action mode destroyed remove selected selections and set action mode to null
//        //First check current fragment action mode
//        Log.d("onDestroyActionMode","CAME HERE");
//        InboxTickets inboxTickets=new InboxTickets();
//        //recyclerView_adapter.removeSelection();
//        inboxTickets.setNullToActionMode();
//
////        ((InboxTickets) inboxTickets).setNullToActionMode();
//        mode.finish();
//
//          // remove selection
////            Fragment recyclerFragment = new MainActivity().getFragment(1);//Get recycler fragment
////            if (recyclerFragment != null)
////                ((RecyclerView_Fragment) recyclerFragment).setNullToActionMode();//Set action mode null
//
//    }
//}
