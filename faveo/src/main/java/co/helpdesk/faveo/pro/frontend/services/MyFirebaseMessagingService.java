package co.helpdesk.faveo.pro.frontend.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.frontend.activities.ClientDetailActivity;
import co.helpdesk.faveo.pro.frontend.activities.TicketDetailActivity;

/**
 * Created by narendra on 11/07/16.
 * When ever anything posted on the server this method will be executed to get the
 * notification.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    String by;
    Context context;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //Displaying data in log
        //It is optional
        Log.d(TAG, "remoter " + remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());
//        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        Log.d(TAG, "Notification Message Data: " + remoteMessage.getData().get("message"));
        Log.d(TAG, "Notification Message ticket_id: " + remoteMessage.getData().get("id"));
        Prefs.putString("TICKETid", remoteMessage.getData().get("id"));
        //Log.d(TAG, "Notification Message noti_id: " + remoteMessage.getData().get("notification_id"));
        Log.d(TAG, "Notification Message Data: " + remoteMessage.getData());
        Log.d(TAG, "Notification Message Scenario: " + remoteMessage.getData().get("scenario"));

        by=remoteMessage.getData().get("by");
        String pic = "";
        int client_id = 0;
        String clientname = "";
        if (by.equals("System")){
              clientname="System";
              pic=getURLForResource(R.mipmap.ic_launcher);
        }
        else{
            String requester = remoteMessage.getData().get("requester");
//        Log.d("Requester", requester);

            try {
                JSONObject jsonObj = new JSONObject(requester);
                pic = jsonObj.getString("profile_pic");
                client_id = jsonObj.getInt("id");
                String firstName = jsonObj.getString("first_name");
                String lastName = jsonObj.getString("last_name");
                String userName = jsonObj.getString("user_name");
                if (firstName == null || firstName.equals(""))
                    clientname = userName;
                else
                    clientname = firstName + " " + lastName;
                Log.d("Profile_Pic", pic);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        //Calling method to generate notification

        if (remoteMessage.getData().get("scenario").equals("tickets"))
            sendNotificationTicket(remoteMessage.getData().get("message"), remoteMessage.getData().get("id"), clientname.trim(), pic);
        else
            sendNotificationClient(remoteMessage.getData().get("message"), client_id + "", clientname.trim(), pic);

        //Log.d("Data",remoteMessage.getNotification().);

    }

    /**
     * This method is only generating push notification
     * It is same as we did in earlier posts.
     * @param messageBody
     * @param ID
     * @param noti_tittle
     * @param profilePic
     */
    private void sendNotificationClient(String messageBody, String ID, String noti_tittle, String profilePic) {

        Intent intent = new Intent(this, ClientDetailActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("CLIENT_ID", ID);
        Log.d("intents from FCM", "ID :" + ID);

        int id = (int) System.currentTimeMillis();

        // Gets a PendingIntent containing the entire back stack
        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        this,
                        id,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.mipmap.ic_stat_f1);
        Bitmap bitmap=getBitmapFromURL(profilePic);
        Bitmap bitmap1=getCircleBitmap(bitmap);
        notificationBuilder.setLargeIcon(bitmap1);
        //notificationBuilder.setLargeIcon(getBitmapFromURL(profilePic));
        notificationBuilder.setContentTitle(noti_tittle);
        notificationBuilder.setContentText(messageBody);
        notificationBuilder.setDefaults(Notification.DEFAULT_LIGHTS);
        notificationBuilder.setSound(defaultSoundUri);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        // notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
        notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        notificationBuilder.setDefaults(Notification.FLAG_ONGOING_EVENT);
        notificationBuilder.setDefaults(Notification.PRIORITY_HIGH);
        notificationBuilder.setWhen(System.currentTimeMillis());
        notificationBuilder.setAutoCancel(true);
        //notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody));
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


    /**
     * This method is only generating push notification
     * it is same as we did in earlier posts.
     * @param messageBody
     * @param ID
     * @param noti_tittle
     * @param profilePic
     */
    private void sendNotificationTicket(String messageBody, String ID, String noti_tittle, String profilePic) {
        //Prefs.putString("TICKETid", ID);
        //Prefs.putString("cameFromNotification","true");
        Intent intent = new Intent(this, TicketDetailActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("ticket_id", ID);

        Log.d("intents from FCM", "ID :" + ID);

        int id = (int) System.currentTimeMillis();

        // Gets a PendingIntent containing the entire back stack
        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        this,
                        id,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.mipmap.ic_stat_f1);
        Bitmap bitmap=getBitmapFromURL(profilePic);
        //Bitmap bitmap1=getCircleBitmap(bitmap);
        notificationBuilder.setLargeIcon(bitmap);
        notificationBuilder.setContentTitle(noti_tittle);
        notificationBuilder.setContentText(messageBody);
        notificationBuilder.setDefaults(Notification.DEFAULT_LIGHTS);
        notificationBuilder.setSound(defaultSoundUri);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        // notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
        notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        notificationBuilder.setDefaults(Notification.FLAG_ONGOING_EVENT);
        notificationBuilder.setDefaults(Notification.PRIORITY_HIGH);
        notificationBuilder.setWhen(System.currentTimeMillis());
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody));
        notificationBuilder.setTicker(messageBody);
        notificationBuilder.setContentIntent(pendingIntent);
        Log.d("messageBody",messageBody);
        if (defaultSoundUri == null) {
            notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
            Log.e("ringtone", "setDefault");
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(id, notificationBuilder.build());
        Log.d("stackadded", "notification arrived");

    }
    public String getURLForResource (int resourceId) {
        return Uri.parse("android.resource://"+R.class.getPackage().getName()+"/" +resourceId).toString();
    }
    /**
     * Get bitmap image from requested url
     * @param strURL
     * @return
     */
    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Bitmap getCircleBitmap(Bitmap bitmap) {
        Bitmap output;
        Rect srcRect, dstRect;
        float r;
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();

        if (width > height){
            output = Bitmap.createBitmap(height, height, Bitmap.Config.ARGB_8888);
            int left = (width - height) / 2;
            int right = left + height;
            srcRect = new Rect(left, 0, right, height);
            dstRect = new Rect(0, 0, height, height);
            r = height / 2;
        }else{
            output = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
            int top = (height - width)/2;
            int bottom = top + width;
            srcRect = new Rect(0, top, width, bottom);
            dstRect = new Rect(0, 0, width, width);
            r = width / 2;
        }

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(r, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, srcRect, dstRect, paint);

        bitmap.recycle();

        return output;
    }
}
