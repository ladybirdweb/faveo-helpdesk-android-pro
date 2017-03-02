package co.helpdesk.faveo.pro.backend.api.v1;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import co.helpdesk.faveo.pro.Constants;
import co.helpdesk.faveo.pro.Preference;

/**
 * Created by Sumit
 */
public class Authenticate {

    static String apiKey;
    static String token;
    static String IP;

    public Authenticate() {
        apiKey = Constants.API_KEY;
        token = Preference.getToken();
        IP = null;
    }

    public String postAuthenticateUser(String username, String password) {
        String parameters = null;
        JSONObject obj = new JSONObject();
        try {
            obj.put("username", username);
            obj.put("password", password);
            obj.put("api_key", apiKey);
            obj.put("ip", IP);
            parameters = obj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("Authenticate-URL :", Constants.URL + "authenticate" + parameters);
        return new HTTPConnection().HTTPResponsePost(Constants.URL + "authenticate", parameters);
    }

}
