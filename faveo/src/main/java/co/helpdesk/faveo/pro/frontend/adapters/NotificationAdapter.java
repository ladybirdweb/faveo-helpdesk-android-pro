package co.helpdesk.faveo.pro.frontend.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.helpdesk.faveo.pro.Controller.RealmController;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.frontend.activities.TicketDetailActivity;
import co.helpdesk.faveo.pro.model.Ticket;
import io.realm.Realm;

/**
 * Created by narendra on 09/12/16.
 */

public class NotificationAdapter extends RealmRecyclerViewAdapter<Ticket> {

    final Context context;
    private Realm realm;
    private LayoutInflater inflater;

    public NotificationAdapter(Context context) {

        this.context = context;
    }

    // create new views (invoked by the layout manager)
    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate a new card view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        realm = RealmController.getInstance().getRealm();

        // get the article
        final Ticket ticket = getItem(position);
        // cast the generic view holder to our specific one
        final CardViewHolder holder = (CardViewHolder) viewHolder;

        // set the title and the snippet
        holder.textSub.setText(ticket.getTicket_subject());
        holder.textTicketNumber.setText(ticket.getTicket_number());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), TicketDetailActivity.class);
                intent.putExtra("ticket_id", ticket.getTicket_id()+"");
                intent.putExtra("ticket_number", ticket.getTicket_number());
                intent.putExtra("ticket_opened_by", ticket.getTicket_opened_by());
                intent.putExtra("ticket_subject", ticket.getTicket_subject());
                view.getContext().startActivity(intent);
            }
        });

    }

    // return the size of your data set (invoked by the layout manager)
    public int getItemCount() {

        if (getRealmAdapter() != null) {
            return getRealmAdapter().getCount();
        }
        return 0;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {

        public CardView card;
        public TextView textSub;
        public TextView textTicketNumber;

        public CardViewHolder(View itemView) {
            // standard view holder pattern with Butterknife view injection
            super(itemView);
            card = (CardView) itemView.findViewById(R.id.notification_cardview);
            textSub = (TextView) itemView.findViewById(R.id.ticket_subject);
            textTicketNumber = (TextView) itemView.findViewById(R.id.ticket_number);
        }
    }
}
