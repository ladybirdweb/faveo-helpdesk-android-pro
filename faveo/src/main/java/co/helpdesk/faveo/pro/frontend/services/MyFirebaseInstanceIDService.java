package co.helpdesk.faveo.pro.frontend.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.IOException;

import co.helpdesk.faveo.pro.Constants;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by narendra on 11/07/16.
 * This is for getting push notification from the server.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    public static String refreshedToken;

    @Override
    public void onTokenRefresh() {

        //Getting registration token
        refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);

//      SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCE, 0);
        Boolean loginComplete = Prefs.getBoolean("LOGIN_COMPLETE", false);
        if (loginComplete) {
            Log.d("LoginComplete", "FCM ID Service");
            try {
                sendRegistrationToServer(refreshedToken);
            } catch (IOException e) {
                Log.d(TAG, "refreshed_token error");
                e.printStackTrace();
            }
        }
    }

    /**
     * Send the refreshed fcm_token to the server along with user_id
     * @param token
     * @throws IOException
     */
    public static void sendRegistrationToServer(String token) throws IOException {

        String userID = Prefs.getString("ID", "");
        OkHttpClient httpClient = new OkHttpClient();

        RequestBody body = new FormBody.Builder()
                .add("fcm_token", token)
                .add("user_id", userID)
                .build();

        Request request = new Request.Builder()
                .url(Constants.URL + "fcmtoken?")
                .post(body)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("Response Of okHttp", response.body().string());
            }
        });
        //

    }
}
