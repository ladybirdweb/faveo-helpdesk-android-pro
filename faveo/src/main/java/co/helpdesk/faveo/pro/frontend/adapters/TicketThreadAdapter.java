package co.helpdesk.faveo.pro.frontend.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import co.helpdesk.faveo.pro.CircleTransform;
import co.helpdesk.faveo.pro.Helper;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.frontend.activities.MainActivity;
import co.helpdesk.faveo.pro.model.TicketThread;

/**
 * This adapter is for the conversation page in the ticket detail page.
 */
public class TicketThreadAdapter extends RecyclerView.Adapter<TicketThreadAdapter.TicketViewHolder> {
    private List<TicketThread> ticketThreadList;
    Context context;

    public TicketThreadAdapter(Context context,List<TicketThread> ticketThreadList) {
        this.ticketThreadList = ticketThreadList;
        this.context=context;
    }

    @Override
    public int getItemCount() {
        return ticketThreadList.size();
    }

    @Override
    public void onBindViewHolder(final TicketViewHolder ticketViewHolder, int i) {
        TicketThread ticketThread = ticketThreadList.get(i);
        String letter = String.valueOf(ticketThread.clientName.charAt(0)).toUpperCase();
        ticketViewHolder.textViewClientName.setText(ticketThread.clientName);
        ticketViewHolder.textViewMessageTime.setReferenceTime(Helper.relativeTime(ticketThread.messageTime));
        String message=ticketThread.message.replaceAll("\n","");
        String message1=message.replaceAll("\t","");
        Log.d("without",message1);
        ticketViewHolder.webView.loadDataWithBaseURL(null,message1.replaceAll("\\n", "<br/>"), "text/html", "UTF-8", null);
        if (ticketThread.getClientPicture().contains("jpg")||ticketThread.getClientPicture().contains("png")){
            Picasso.with(context).load(ticketThread.getClientPicture()).transform(new CircleTransform()).into(ticketViewHolder.roundedImageViewProfilePic);
        }
        else{
            ColorGenerator generator = ColorGenerator.MATERIAL;
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(letter, generator.getRandomColor());
            ticketViewHolder.roundedImageViewProfilePic.setImageDrawable(drawable);
        }

//        IImageLoader imageLoader = new PicassoLoader();
//        imageLoader.loadImage(ticketViewHolder.roundedImageViewProfilePic, ticketThread.clientPicture, ticketThread.placeholder);

//        for (int j=0;j<1;j++){
//            ticketViewHolder.webView.setVisibility(View.VISIBLE);
//        }
        if (i==0){
            ticketViewHolder.webView.setVisibility(View.VISIBLE);
        }
        else if (i==1){
            ticketViewHolder.webView.setVisibility(View.VISIBLE);
        }
        else if (i==ticketThreadList.size()-1){
            ticketViewHolder.webView.setVisibility(View.VISIBLE);
        }
        else{
            ticketViewHolder.webView.setVisibility(View.GONE);
        }

//        while (i<1){
//            ticketViewHolder.webView.setVisibility(View.VISIBLE);
//        }




        ticketViewHolder.thread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ticketViewHolder.webView.getVisibility() == View.GONE) {
                    //ticketViewHolder.textViewMessageTitle.setVisibility(View.VISIBLE);
                    ticketViewHolder.webView.setVisibility(View.VISIBLE);
                } else {
                    ticketViewHolder.textViewMessageTitle.setVisibility(View.GONE);
                    //ticketViewHolder.webView.setVisibility(View.VISIBLE);
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

        RelativeLayout thread;
        ImageView roundedImageViewProfilePic;
        TextView textViewClientName;
        RelativeTimeTextView textViewMessageTime;
        TextView textViewMessageTitle;
        TextView textViewType;
        WebView webView;

        TicketViewHolder(View v) {
            super(v);
            thread = (RelativeLayout) v.findViewById(R.id.thread);
            roundedImageViewProfilePic = (ImageView) v.findViewById(R.id.imageView_default_profile);
            textViewClientName = (TextView) v.findViewById(R.id.textView_client_name);
            textViewMessageTime = (RelativeTimeTextView) v.findViewById(R.id.textView_ticket_time);
            //textViewMessageTitle = (TextView) v.findViewById(R.id.textView_client_message_title);
            textViewType = (TextView) v.findViewById(R.id.textView_type);
            webView = (WebView) v.findViewById(R.id.webView);
        }

    }

}
