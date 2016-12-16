package co.helpdesk.faveo.pro.frontend.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import co.helpdesk.faveo.pro.Controller.RealmController;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.frontend.adapters.NotificationAdapter;
import co.helpdesk.faveo.pro.frontend.adapters.RealmTicketAdapter;
import co.helpdesk.faveo.pro.model.Ticket;
import io.realm.Realm;
import io.realm.RealmResults;

public class NotificationActivity extends AppCompatActivity {

    private NotificationAdapter adapter;
    private Realm realm;
    private RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recycler = (RecyclerView) findViewById(R.id.recycler_view);

        //get realm instance
        this.realm = RealmController.with(this).getRealm();

        setupRecycler();

        Log.d("hasTickets: ", "" + RealmController.with(this).hasTickets());
//        if (!Prefs.with(this).getPreLoad()) {
//            setRealmData();
//        }
        // refresh the realm instance
        RealmController.with(this).refresh();
        // get all persisted objects
        // create the helper adapter and notify data set changes
        // changes will be reflected automatically
        setRealmAdapter(RealmController.with(this).getTickets());
    }

    private void setupRecycler() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recycler.setHasFixedSize(true);

        // use a linear layout manager since the cards are vertically scrollable
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(layoutManager);

        // create an empty adapter and add it to the recycler view
        adapter = new NotificationAdapter(this);
        recycler.setAdapter(adapter);
    }

    public void setRealmAdapter(RealmResults<Ticket> tickets) {

        RealmTicketAdapter realmAdapter = new RealmTicketAdapter(this.getApplicationContext(), tickets, true);
        // Set the data and tell the RecyclerView to draw
        adapter.setRealmAdapter(realmAdapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
