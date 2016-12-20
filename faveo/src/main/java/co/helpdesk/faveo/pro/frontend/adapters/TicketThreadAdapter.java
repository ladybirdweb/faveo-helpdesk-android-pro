package co.helpdesk.faveo.pro.frontend.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import co.helpdesk.faveo.pro.Helper;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.model.TicketThread;

public class TicketThreadAdapter extends RecyclerView.Adapter<TicketThreadAdapter.TicketViewHolder> {
    private List<TicketThread> ticketThreadList;

    public TicketThreadAdapter(List<TicketThread> ticketThreadList) {
        this.ticketThreadList = ticketThreadList;
    }

    @Override
    public int getItemCount() {
        return ticketThreadList.size();
    }

    @Override
    public void onBindViewHolder(final TicketViewHolder ticketViewHolder, int i) {
        TicketThread ticketThread = ticketThreadList.get(i);
        ticketViewHolder.textViewClientName.setText(ticketThread.clientName);
        ticketViewHolder.textViewMessageTime.setText(Helper.parseDate(ticketThread.messageTime));
        ticketViewHolder.textViewMessageTitle.setText(ticketThread.messageTitle);
        //ticketViewHolder.textViewMessage.setText(Html.fromHtml(ticketThread.message));
        ticketViewHolder.webView.loadDataWithBaseURL(null, ticketThread.message, "text/html", "utf-8", null);

        if (ticketThread.clientPicture != null && ticketThread.clientPicture.trim().length() != 0)
            Picasso.with(ticketViewHolder.roundedImageViewProfilePic.getContext())
                    .load(ticketThread.clientPicture)
                    .resize(96, 96)
                    .centerCrop()
                    .placeholder(R.drawable.default_pic)
                    .error(R.drawable.default_pic)
                    .into(ticketViewHolder.roundedImageViewProfilePic);

        ticketViewHolder.thread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ticketViewHolder.textViewMessageTitle.getVisibility() == View.GONE) {
                    ticketViewHolder.textViewMessageTitle.setVisibility(View.VISIBLE);
                    ticketViewHolder.webView.setVisibility(View.VISIBLE);
                } else {
                    ticketViewHolder.textViewMessageTitle.setVisibility(View.GONE);
                    ticketViewHolder.webView.setVisibility(View.GONE);
                }
            }
        });

        if (!ticketThread.getIsReply().equals("true"))
            ticketViewHolder.textViewType.setText("");

    }

    @Override
    public TicketViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_conversation, viewGroup, false);
        return new TicketViewHolder(itemView);
    }

    static class TicketViewHolder extends RecyclerView.ViewHolder {

        protected View thread;
        protected RoundedImageView roundedImageViewProfilePic;
        protected TextView textViewClientName;
        protected TextView textViewMessageTime;
        protected TextView textViewMessageTitle;
        //  protected TextView textViewMessage;
        protected TextView textViewType;
        protected WebView webView;

        public TicketViewHolder(View v) {
            super(v);
            thread = v.findViewById(R.id.thread);
            roundedImageViewProfilePic = (RoundedImageView) v.findViewById(R.id.imageView_default_profile);
            textViewClientName = (TextView) v.findViewById(R.id.textView_client_name);
            textViewMessageTime = (TextView) v.findViewById(R.id.textView_ticket_time);
            textViewMessageTitle = (TextView) v.findViewById(R.id.textView_client_message_title);
            //  textViewMessage = (TextView) v.findViewById(R.id.textView_client_message_body);
            textViewType = (TextView) v.findViewById(R.id.textView_type);
            webView = (WebView) v.findViewById(R.id.webView);
        }

    }

}
