package co.helpdesk.faveo.pro.frontend.adapters;

import android.content.Context;

import co.helpdesk.faveo.pro.model.Ticket;
import io.realm.RealmResults;

/**
 * Created by narendra on 09/12/16.
 */

public class RealmTicketAdapter extends RealmModelAdapter<Ticket> {

    public RealmTicketAdapter(Context context, RealmResults<Ticket> realmResults, boolean automaticUpdate) {

        super(context, realmResults, automaticUpdate);
    }
}
