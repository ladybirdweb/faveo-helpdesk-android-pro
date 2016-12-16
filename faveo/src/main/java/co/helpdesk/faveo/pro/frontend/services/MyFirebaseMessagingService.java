package co.helpdesk.faveo.pro.frontend.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.frontend.activities.TicketDetailActivity;
import co.helpdesk.faveo.pro.model.Ticket;
import io.realm.Realm;

/**
 * Created by narendra on 11/07/16.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        Log.d(TAG, "remoter " + remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        Log.d(TAG, "Notification Message Data: " + remoteMessage.getData().get("ticket_id"));
        Log.d(TAG, "Notification Message Data: " + remoteMessage.getData().get("ticket_number"));
        Log.d(TAG, "Notification Message Data: " + remoteMessage.getData().get("ticket_opened_by"));
        Log.d(TAG, "Notification Message Data: " + remoteMessage.getData().get("ticket_subject"));


        //Calling method to generate notification
        sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getData().get("ticket_id"), remoteMessage.getData().get("ticket_number"), remoteMessage.getData().get("ticket_opened_by"), remoteMessage.getData().get("ticket_subject"), remoteMessage.getData().get("notification_title"));
        //Log.d("Data",remoteMessage.getNotification().);

        Ticket ticket = new Ticket();
        ticket.setTicket_id(Integer.parseInt(remoteMessage.getData().get("ticket_id")));
        ticket.setTicket_number(remoteMessage.getData().get("ticket_number"));
        ticket.setTicket_opened_by(remoteMessage.getData().get("ticket_opened_by"));
        ticket.setTicket_subject(remoteMessage.getData().get("ticket_subject"));

        Realm realm = null;
        try {
            Log.d(TAG, "Realm");
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.copyToRealm(ticket);
            realm.commitTransaction();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String messageBody, String ID, String number, String opened_by, String subject, String noti_tittle) {

        Intent intent = new Intent(this, TicketDetailActivity.class);

        Log.d("stackadded", "sargewg");
        intent.putExtra("ticket_id", ID);
        intent.putExtra("ticket_number", number);
        intent.putExtra("ticket_opened_by", opened_by);
        intent.putExtra("ticket_subject", subject);

        Log.d("intents from FCM", "ID :" + ID + " Num : " + number + " openedBy : " + opened_by + " sub : " + subject);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack
        stackBuilder.addParentStack(TicketDetailActivity.class);
        // Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(intent);

        int id = (int) System.currentTimeMillis();

        // Gets a PendingIntent containing the entire back stack
        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.mipmap.ic_stat_f1);
        notificationBuilder.setContentTitle(noti_tittle);
        notificationBuilder.setContentText(messageBody);
        notificationBuilder.setDefaults(Notification.DEFAULT_LIGHTS);
        notificationBuilder.setSound(defaultSoundUri);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        // notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
        notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        notificationBuilder.setDefaults(Notification.FLAG_ONGOING_EVENT);
//        notificationBuilder.setDefaults(Notification.PRIORITY_HIGH);
        notificationBuilder.setWhen(System.currentTimeMillis());
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody));
        notificationBuilder.setTicker(messageBody);
        notificationBuilder.setContentIntent(pendingIntent);

        if (defaultSoundUri == null) {
            notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
            Log.e("ringtone", "setDefault");
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(id, notificationBuilder.build());
        Log.d("stackadded", "notification arrived");

    }
}
