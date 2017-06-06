package co.helpdesk.faveo.pro.frontend.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.curioustechizen.ago.RelativeTimeTextView;

import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import co.helpdesk.faveo.pro.Helper;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.frontend.activities.TicketDetailActivity;
import co.helpdesk.faveo.pro.model.TicketOverview;

/**
 * This adapter is for the tickets(inbox,mytickets,trash...) page in the main page.
 */
public class TicketOverviewAdapter extends RecyclerView.Adapter<TicketOverviewAdapter.TicketViewHolder> {
    private List<TicketOverview> ticketOverviewList;

    public TicketOverviewAdapter(List<TicketOverview> ticketOverviewList) {
        this.ticketOverviewList = ticketOverviewList;
    }

    @Override
    public int getItemCount() {
        return ticketOverviewList.size();
    }

    @Override
    public void onBindViewHolder(TicketViewHolder ticketViewHolder, int i) {
        final TicketOverview ticketOverview = ticketOverviewList.get(i);
        if (ticketOverview.ticketAttachments.equals("0")) {
            ticketViewHolder.attachementView.setVisibility(View.GONE);
        } else {
            ticketViewHolder.attachementView.setVisibility(View.VISIBLE);
        }
        if (ticketOverview.dueDate != null && !ticketOverview.dueDate.equals("null"))
            if (Helper.compareDates(ticketOverview.dueDate) == 1) {
                ticketViewHolder.textViewOverdue.setVisibility(View.VISIBLE);
            } else ticketViewHolder.textViewOverdue.setVisibility(View.GONE);

        ticketViewHolder.textViewTicketID.setText(ticketOverview.ticketID + "");
        ticketViewHolder.textViewTicketNumber.setText(ticketOverview.ticketNumber);
        ticketViewHolder.textViewClientName.setText(ticketOverview.clientName);
        ticketViewHolder.textViewSubject.setText(ticketOverview.ticketSubject);

//        GradientDrawable shape = new GradientDrawable(new );
//        shape.mutate();
//        shape.setCornerRadii(new float[]{10f,10f});
//        ticketViewHolder.ticketPriority.setBackground(shape);
        if (ticketOverview.ticketPriorityColor != null) {
            ticketViewHolder.ticketPriority.setBackgroundColor(Color.parseColor(ticketOverview.ticketPriorityColor));
        }
        ticketViewHolder.textViewTime.setReferenceTime(Helper.relativeTime(ticketOverview.ticketTime));


        IImageLoader imageLoader = new PicassoLoader();
        imageLoader.loadImage(ticketViewHolder.roundedImageViewProfilePic, ticketOverview.clientPicture, ticketOverview.placeholder);

//        if (ticketOverview.clientPicture != null && ticketOverview.clientPicture.trim().length() != 0)
//            Picasso.with(ticketViewHolder.roundedImageViewProfilePic.getContext())
//                    .load(ticketOverview.clientPicture)
//                    .placeholder(R.drawable.default_pic)
//                    .error(R.drawable.default_pic)
//                    .into(ticketViewHolder.roundedImageViewProfilePic);

        ticketViewHolder.ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TicketDetailActivity.class);
                intent.putExtra("ticket_id", ticketOverview.ticketID + "");
                intent.putExtra("ticket_number", ticketOverview.ticketNumber);
                intent.putExtra("ticket_opened_by", ticketOverview.clientName);
                intent.putExtra("ticket_subject", ticketOverview.ticketSubject);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public TicketViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_ticket, viewGroup, false);
        return new TicketViewHolder(itemView);
    }

    static class TicketViewHolder extends RecyclerView.ViewHolder {

        protected View ticket;
        AvatarView roundedImageViewProfilePic;
        TextView textViewTicketID;
        TextView textViewTicketNumber;
        TextView textViewClientName;
        TextView textViewSubject;
        RelativeTimeTextView textViewTime;
        TextView textViewOverdue;
        View ticketPriority;
        // TextView ticketStatus;
        ImageView attachementView;


        TicketViewHolder(View v) {
            super(v);
            ticket = v.findViewById(R.id.ticket);
            attachementView = (ImageView) v.findViewById(R.id.attachment_icon);
            ticketPriority = v.findViewById(R.id.priority_view);
            roundedImageViewProfilePic = (AvatarView) v.findViewById(R.id.imageView_default_profile);
            textViewTicketID = (TextView) v.findViewById(R.id.textView_ticket_id);
            textViewTicketNumber = (TextView) v.findViewById(R.id.textView_ticket_number);
            textViewClientName = (TextView) v.findViewById(R.id.textView_client_name);
            textViewSubject = (TextView) v.findViewById(R.id.textView_ticket_subject);
            textViewTime = (RelativeTimeTextView) v.findViewById(R.id.textView_ticket_time);
            textViewOverdue = (TextView) v.findViewById(R.id.overdue_view);
        }

    }

}
