package co.helpdesk.faveo.pro.frontend.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.URLConnection;
import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import co.helpdesk.faveo.pro.CircleTransform;
import co.helpdesk.faveo.pro.Helper;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.frontend.activities.MainActivity;
import co.helpdesk.faveo.pro.frontend.activities.ShowingAttachment;
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
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public void onBindViewHolder(final TicketViewHolder ticketViewHolder, final int i) {
        final TicketThread ticketThread = ticketThreadList.get(i);
        final StringBuilder builder = new StringBuilder();
        String letter="U";
        Log.d("customerUname",ticketThread.clientName);
        try {
            if (!ticketThread.clientName.equals("")) {
                if (Character.isUpperCase(ticketThread.clientName.charAt(0))){
                    letter = String.valueOf(ticketThread.clientName.charAt(0));
                }
                else{
                    letter = String.valueOf(ticketThread.clientName.charAt(0)).toUpperCase();
                }

            }
//                else if (ticketOverview.clientName.equals("null")){
//                    letter="U";
//                }
            else{
                ticketViewHolder.textViewClientName.setVisibility(View.GONE);
            }
        }catch (StringIndexOutOfBoundsException e){
            e.printStackTrace();
        }
        //String letter = String.valueOf(ticketThread.clientName.charAt(0)).toUpperCase();
        ticketViewHolder.textViewClientName.setText(ticketThread.clientName);
        ticketViewHolder.textViewMessageTime.setReferenceTime(Helper.relativeTime(ticketThread.messageTime));
        ticketViewHolder.textViewTicketCreatedTime.setReferenceTime(Helper.relativeTime(ticketThread.messageTime));
        String message=ticketThread.message.replaceAll("\n","");
        String message1=message.replaceAll("\t","");
        String message2=message1.replaceAll("&nbsp;"," ");
        Log.d("without",message1);
//        Document doc = Jsoup.parse(message1);
//        Elements elements = doc.select("body").first().children();
////Elements elements = doc.select("p");//or only `<p>` elements
//        for (Element link : elements) {
//            builder.append(link.text()).append("");
//        }
        ticketViewHolder.textViewShowingSome.setText(Jsoup.parse(message1).text());
        //ticketViewHolder.textViewShowingSome.setText(message1);
        ticketViewHolder.webView.loadDataWithBaseURL(null,message2.replaceAll("\\n", "<br/>"), "text/html", "UTF-8", null);
        if (ticketThread.getClientPicture().contains("jpg")||ticketThread.getClientPicture().contains("png")||ticketThread.getClientPicture().contains("jpeg")){
            Picasso.with(context).load(ticketThread.getClientPicture()).transform(new CircleTransform()).into(ticketViewHolder.roundedImageViewProfilePic);
        }

        else if (!ticketThread.getClientPicture().contains("jpg")||!ticketThread.getClientPicture().contains("png")||!ticketThread.getClientPicture().contains("jpeg")){
            int color= Color.parseColor("#cdc5bf");
            ColorGenerator generator = ColorGenerator.MATERIAL;
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(letter,generator.getRandomColor());
            ticketViewHolder.roundedImageViewProfilePic.setAlpha(0.6f);
            ticketViewHolder.roundedImageViewProfilePic.setImageDrawable(drawable);
        }
        else{
            ticketViewHolder.roundedImageViewProfilePic.setVisibility(View.GONE);
        }

        if (ticketThread.getName().equals("")){
            ticketViewHolder.relativeLayout.setVisibility(View.GONE);
            ticketViewHolder.view.setVisibility(View.GONE);
        }
        else{
            ticketViewHolder.relativeLayout.setVisibility(View.VISIBLE);
            ticketViewHolder.view.setVisibility(View.VISIBLE);
            ticketViewHolder.textView.setText("Attachment "+"("+ticketThread.getNoOfAttachments()+")");

        }
        ticketViewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), ShowingAttachment.class);
                Prefs.putString("fileName",ticketThread.getName());
                Prefs.putString("file",ticketThread.getFile());
                Prefs.putString("multipleName",ticketThread.getName());
                Prefs.putString("multipleFile",ticketThread.getFile());
                Log.d("MultipleFileNameFrom",ticketThread.getName());
                view.getContext().startActivity(intent);

            }
        });

        if (i==ticketThreadList.size()-1){
            ticketViewHolder.reportAndReply.setText("reported ");
        }
        else{
            ticketViewHolder.reportAndReply.setText("updated ");
        }

//        IImageLoader imageLoader = new PicassoLoader();
//        imageLoader.loadImage(ticketViewHolder.roundedImageViewProfilePic, ticketThread.clientPicture, ticketThread.placeholder);

//        for (int j=0;j<1;j++){
//            ticketViewHolder.linearLayout.setVisibility(View.VISIBLE);
//            ticketViewHolder.reportAndReply.setVisibility(View.VISIBLE);
//            ticketViewHolder.textViewMessageTime.setVisibility(View.GONE);
//            ticketViewHolder.textViewTicketCreatedTime.setVisibility(View.VISIBLE);
//            ticketViewHolder.textViewShowingSome.setVisibility(View.GONE);
//            //ticketViewHolder.webView.setVisibility(View.VISIBLE);
//        }
//        if (i==0){
//            ticketViewHolder.linearLayout.setVisibility(View.VISIBLE);
//            ticketViewHolder.reportAndReply.setVisibility(View.VISIBLE);
//            ticketViewHolder.textViewMessageTime.setVisibility(View.GONE);
//            ticketViewHolder.textViewTicketCreatedTime.setVisibility(View.VISIBLE);
//            ticketViewHolder.textViewShowingSome.setVisibility(View.GONE);
//        }
////         if (i==1){
////            ticketViewHolder.linearLayout.setVisibility(View.VISIBLE);
////        }

        if (i==0){
            ticketViewHolder.linearLayout.setVisibility(View.GONE);
            ticketViewHolder.reportAndReply.setVisibility(View.GONE);
            ticketViewHolder.textViewMessageTime.setVisibility(View.VISIBLE);
            ticketViewHolder.textViewTicketCreatedTime.setVisibility(View.GONE);
            ticketViewHolder.textViewShowingSome.setVisibility(View.VISIBLE);
        }
        else if (i==ticketThreadList.size()-1){
            ticketViewHolder.textViewShowingSome.setVisibility(View.GONE);
            ticketViewHolder.reportAndReply.setVisibility(View.VISIBLE);
            ticketViewHolder.textViewTicketCreatedTime.setVisibility(View.VISIBLE);
            ticketViewHolder.textViewMessageTime.setVisibility(View.GONE);
            ticketViewHolder.linearLayout.setVisibility(View.VISIBLE);
        }
        else {
            ticketViewHolder.linearLayout.setVisibility(View.GONE);
            ticketViewHolder.reportAndReply.setVisibility(View.GONE);
            ticketViewHolder.textViewMessageTime.setVisibility(View.VISIBLE);
            ticketViewHolder.textViewTicketCreatedTime.setVisibility(View.GONE);
            ticketViewHolder.textViewShowingSome.setVisibility(View.VISIBLE);
        }
        ticketViewHolder.thread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ticketViewHolder.linearLayout.getVisibility() == View.VISIBLE) {
                    //ticketViewHolder.textViewMessageTitle.setVisibility(View.VISIBLE);
                    ticketViewHolder.linearLayout.setVisibility(View.GONE);
                    ticketViewHolder.reportAndReply.setVisibility(View.GONE);
                    ticketViewHolder.textViewMessageTime.setVisibility(View.VISIBLE);
                    ticketViewHolder.textViewTicketCreatedTime.setVisibility(View.GONE);
                    ticketViewHolder.textViewShowingSome.setVisibility(View.VISIBLE);
                } else {
                    ticketViewHolder.textViewShowingSome.setVisibility(View.GONE);
                    ticketViewHolder.reportAndReply.setVisibility(View.VISIBLE);
                    ticketViewHolder.textViewTicketCreatedTime.setVisibility(View.VISIBLE);
                    ticketViewHolder.textViewMessageTime.setVisibility(View.GONE);
                    ticketViewHolder.linearLayout.setVisibility(View.VISIBLE);
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
        TextView textViewShowingSome;
        ImageView roundedImageViewProfilePic;
        TextView textViewClientName;
        RelativeTimeTextView textViewMessageTime;
        RelativeTimeTextView textViewTicketCreatedTime;
        TextView textViewMessageTitle;
        TextView textViewType;
        WebView webView;
        RelativeLayout relativeLayout;
        TextView textView;
        TextView reportAndReply;
        LinearLayout linearLayout;
        View view;
        TicketViewHolder(View v) {
            super(v);
            thread = (RelativeLayout) v.findViewById(R.id.thread);
            view=v.findViewById(R.id.attachmentSeparation);
            roundedImageViewProfilePic = (ImageView) v.findViewById(R.id.imageView_default_profile);
            textViewClientName = (TextView) v.findViewById(R.id.textView_client_name);
            textViewMessageTime = (RelativeTimeTextView) v.findViewById(R.id.textView_ticket_time);
            textViewTicketCreatedTime= (RelativeTimeTextView) v.findViewById(R.id.textView_ticket_related);
            //textViewMessageTitle = (TextView) v.findViewById(R.id.textView_client_message_title);
            textViewType = (TextView) v.findViewById(R.id.textView_type);
            webView = (WebView) v.findViewById(R.id.webView);
            textViewShowingSome= (TextView) v.findViewById(R.id.showingSome);
            relativeLayout= (RelativeLayout) v.findViewById(R.id.attachmentlayout);
            textView= (TextView) v.findViewById(R.id.textView7);
            reportAndReply= (TextView) v.findViewById(R.id.reported);
            linearLayout= (LinearLayout) v.findViewById(R.id.linearWebView);

        }

    }

}
