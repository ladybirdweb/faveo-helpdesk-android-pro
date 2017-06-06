package co.helpdesk.faveo.pro.frontend.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.frontend.activities.TicketDetailActivity;
import co.helpdesk.faveo.pro.model.TicketGlimpse;

/**
 * This adapter is for the Open/Close page in the client detail page.
 */
public class TicketGlimpseAdapter extends RecyclerView.Adapter<TicketGlimpseAdapter.TicketViewHolder> {
    private List<TicketGlimpse> ticketGlimpseList;
    private final String clientName;

    public TicketGlimpseAdapter(List<TicketGlimpse> ticketGlimpseList, String clientName) {
        this.ticketGlimpseList = ticketGlimpseList;
        this.clientName = clientName;
    }

    @Override
    public int getItemCount() {
        return ticketGlimpseList.size();
    }

    @Override
    public void onBindViewHolder(TicketViewHolder ticketViewHolder, int i) {
        TicketGlimpse ticketGlimpse = ticketGlimpseList.get(i);
        ticketViewHolder.textViewTicketID.setText(ticketGlimpse.ticketID + "");
        ticketViewHolder.textViewTicketNumber.setText(ticketGlimpse.ticketNumber);
        ticketViewHolder.textViewSubject.setText(ticketGlimpse.ticketSubject);
    }

    @Override
    public TicketViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_ticket_status, viewGroup, false);
        return new TicketViewHolder(itemView, clientName);
    }

    static class TicketViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTicketID;
        TextView textViewTicketNumber;
        TextView textViewSubject;

        TicketViewHolder(View v, final String clientName) {
            super(v);
            textViewTicketID = (TextView) v.findViewById(R.id.textView_ticket_id);
            textViewTicketNumber = (TextView) v.findViewById(R.id.textView_ticket_number);
            textViewSubject = (TextView) v.findViewById(R.id.textView_ticket_subject);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), TicketDetailActivity.class);
                    intent.putExtra("ticket_id", textViewTicketID.getText().toString());
                    intent.putExtra("ticket_number", textViewTicketNumber.getText().toString());
                    intent.putExtra("ticket_opened_by", clientName);
                    intent.putExtra("ticket_subject", textViewSubject.getText().toString());
                    v.getContext().startActivity(intent);
                }
            });

        }

    }

}
