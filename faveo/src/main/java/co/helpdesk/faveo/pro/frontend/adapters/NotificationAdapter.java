package co.helpdesk.faveo.pro.frontend.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.picasso.Picasso;

import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import co.helpdesk.faveo.pro.CircleTransform;
import co.helpdesk.faveo.pro.Helper;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.pro.frontend.activities.ClientDetailActivity;
import co.helpdesk.faveo.pro.frontend.activities.TicketDetailActivity;
import co.helpdesk.faveo.pro.model.NotificationThread;

/**
 * This adapter is for the notification page.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.CardViewHolder> {

    private List<NotificationThread> notiThreadList;
    Context context;
    public NotificationAdapter(Context context,List<NotificationThread> notiThreadList) {
        this.notiThreadList = notiThreadList;
        this.context=context;
    }

    // create new views (invoked by the layout manager)
    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate a new card view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_notification, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CardViewHolder viewHolder, final int position) {
        final NotificationThread notiThread = notiThreadList.get(position);
        String letter = String.valueOf(notiThread.getRequesterName().charAt(0)).toUpperCase();
        TextDrawable.IBuilder mDrawableBuilder;
        viewHolder.textNotificationtime.setReferenceTime(Helper.relativeTime(notiThread.noti_time));
        viewHolder.textSub.setText(notiThread.getRequesterName().trim()+", "+notiThread.getTicket_subject());
        if (notiThread.getProfiel_pic().equals("")){
            viewHolder.roundedImageViewProfilePic.setVisibility(View.GONE);

        }
        else if (notiThread.getProfiel_pic().contains(".jpg")){
            mDrawableBuilder = TextDrawable.builder()
                    .round();
//    TextDrawable drawable1 = mDrawableBuilder.build(generator.getRandomColor());
            Picasso.with(context).load(notiThread.getProfiel_pic()).transform(new CircleTransform()).into(viewHolder.roundedImageViewProfilePic);
//        Glide.with(context)
//            .load(ticketOverview.getClientPicture())
//            .into(ticketViewHolder.roundedImageViewProfilePic);

            //ticketViewHolder.roundedImageViewProfilePic.setImageDrawable(drawable);

        }
        else{
            ColorGenerator generator = ColorGenerator.MATERIAL;
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(letter, generator.getRandomColor());
            viewHolder.roundedImageViewProfilePic.setImageDrawable(drawable);
        }
//        IImageLoader imageLoader = new PicassoLoader();
//        imageLoader.loadImage(viewHolder.roundedImageViewProfilePic, notiThread.profiel_pic, notiThread.placeHolder);
        if (notiThread.getNoti_seen().equals("1")) {
            viewHolder.textSub.setTypeface(null, Typeface.NORMAL);
            viewHolder.textSub.setTextColor(Color.parseColor("#7a7a7a"));
        } else {
            viewHolder.textSub.setTypeface(null, Typeface.BOLD);
            viewHolder.textSub.setTextColor(Color.parseColor("#000000"));
        }
        viewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //updateNotification(notiThread.getTicket_id() + "");
                new UpdateNotificationSeen(notiThread.getNoti_id()).execute();


                if (notiThread.getNoti_scenario().equals("tickets")) {

                    Intent intent = new Intent(view.getContext(), TicketDetailActivity.class);
                    Log.d("ticket_id", notiThread.getTicket_id() + "");
                    Prefs.putString("TICKETid",notiThread.getTicket_id()+"");
                    Prefs.putString("cameFromNotification","true");
                    // intent.putExtra("ticket_id", notiThread.getTicket_id() + "");
                    view.getContext().startActivity(intent);
                } else {

                    Intent intent = new Intent(view.getContext(), ClientDetailActivity.class);
                    intent.putExtra("CLIENT_ID", notiThread.getClient_id() + "");
                    view.getContext().startActivity(intent);
                }
                notiThread.setNoti_seen("1");
                notifyDataSetChanged();
            }
        });

    }


//    private void updateNotification(String ticketID) {
//
//        OkHttpClient httpClient = new OkHttpClient();
//
//        RequestBody body = new FormBody.Builder()
//                .add("notification_id", ticketID)
//                .add("token", Preference.getToken())
//                .build();
//
//        Request request = new Request.Builder()
//                .url(Constants.URL + "helpdesk/notifications-seen?")
//                .post(body)
//                .build();
//
//        httpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Log.d("Response Of okHttp", response.body().string());
//            }
//        });
//        //
//
//    }

    @Override
    public int getItemCount() {
        return notiThreadList.size();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {

        CardView card;
        TextView textSub;
        RelativeTimeTextView textNotificationtime;
        ImageView roundedImageViewProfilePic;

        CardViewHolder(View itemView) {
            super(itemView);
            card = (CardView) itemView.findViewById(R.id.notification_cardview);
            textSub = (TextView) itemView.findViewById(R.id.noti_subject);
            textNotificationtime = (RelativeTimeTextView) itemView.findViewById(R.id.noti_time);
            roundedImageViewProfilePic = (ImageView) itemView.findViewById(R.id.dthumbnail);
        }
    }

    private class UpdateNotificationSeen extends AsyncTask<String, Void, String> {

        int noti_id;

        UpdateNotificationSeen(int noti_id) {
            this.noti_id = noti_id;
        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().postSeenNotifications(noti_id);
        }

        protected void onPostExecute(String result) {

            if (result == null) {
                Log.d("Noti-seen", "Error");
                return;
            }
            Log.d("noti-seen", "success");
        }
    }

}
