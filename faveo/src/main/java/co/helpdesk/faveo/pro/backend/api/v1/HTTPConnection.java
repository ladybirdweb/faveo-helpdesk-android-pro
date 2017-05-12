package co.helpdesk.faveo.pro.backend.api.v1;

import android.util.Log;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;

import co.helpdesk.faveo.pro.FaveoApplication;

/**
 * Created by Sumit
 */
class HTTPConnection {

    HTTPConnection() {

        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
    }

    private StringBuilder sb = null;
    private InputStream is = null;
    private URL url;

    String HTTPResponsePost(String stringURL, String parameters) {

        try {
            url = new URL(stringURL);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Offer-type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestMethod("POST");
            connection.setDoInput(true);

            OutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(outputStream, "UTF-8"));
            if (parameters != null)
                writer.write(parameters);

            writer.flush();
            writer.close();
            outputStream.close();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.d("HTTP Error", "http response code is " + connection.getResponseCode());
                return null;
            }

            is = connection.getInputStream();
            Log.e("Response Code", connection.getResponseCode() + "");
        } catch (IOException e) {
            if (e.getMessage().contains("No authentication challenges found")) {
                if (refreshToken() == null)
                    return null;
                new Helpdesk();
                new Authenticate();
                return "tokenRefreshed";
            }
            Log.e("error in faveo", e.getMessage());
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            sb = new StringBuilder();
            sb.append(reader.readLine()).append("\n");
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            is.close();
        } catch (Exception e) {
            Log.e("log_tag", "Error converting result " + e.toString());
        }
        if (sb == null)
            return null;

        String input = sb.toString();
        if (input.contains("token_expired")) {
            if (refreshToken() == null)
                return null;
            new Helpdesk();
            new Authenticate();
            return "tokenRefreshed";
        }
        return sb.toString();
    }

    public String HTTPResponsePut(String stringURL, String parameters) {
        try {
            url = new URL(stringURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Offer-type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestMethod("PUT");
            connection.setDoInput(true);

            OutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(parameters);

            writer.flush();
            writer.close();
            outputStream.close();

            is = connection.getInputStream();
            Log.e("Response Code", connection.getResponseCode() + "");
        } catch (IOException e) {
            if (e.getMessage().contains("No authentication challenges found")) {
                if (refreshToken() == null)
                    return null;
                new Helpdesk();
                new Authenticate();
                return "tokenRefreshed";
            }
            Log.e("error in faveo", e.getMessage());
            e.printStackTrace();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            sb = new StringBuilder();
            sb.append(reader.readLine()).append("\n");
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            is.close();
        } catch (Exception e) {
            Log.e("log_tag", "Error converting result " + e.toString());
        }
        if (sb == null)
            return null;

        String input = sb.toString();
        if (input.contains("token_expired")) {
            if (refreshToken() == null)
                return null;
            new Helpdesk();
            new Authenticate();
            return "tokenRefreshed";
        }
        return sb.toString();
    }

    String HTTPResponseGet(String stringURL) {
        try {
            url = new URL(stringURL);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Offer-type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.getResponseCode();
            Log.e("Response Codee", connection.getResponseCode() + "");
            is = connection.getInputStream();

        } catch (IOException e) {

            if (e.getMessage().contains("No authentication challenges found")) {
                Log.e("IOException", "contains(\"No authentication challenges found\")");
                if (refreshToken() == null)
                    return null;
                new Helpdesk();
                new Authenticate();
                return "tokenRefreshed";
            } else if (e.getMessage().contains("404 Not Found error")) {
                Toast.makeText(FaveoApplication.getInstance(), "Oops! File not found", Toast.LENGTH_LONG).show();
            }

            Log.e("error in faveo", e.getMessage());
            e.printStackTrace();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            sb = new StringBuilder();
            sb.append(reader.readLine()).append("\n");
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            is.close();
        } catch (Exception e) {
            Log.e("log_tag", "Error converting result " + e.toString());
        }
        if (sb == null)
            return null;

        String input = sb.toString();
        Log.e("input", "" + input);
        if (input.contains("token_expired")) {
            if (refreshToken() == null)
                return null;
            new Helpdesk();
            new Authenticate();
            return "tokenRefreshed";
        }
        return sb.toString();
    }

    private String refreshToken() {
        String result = new Authenticate().postAuthenticateUser(Prefs.getString("USERNAME", null), Prefs.getString("PASSWORD", null));
        if (result == null)
            return null;
        try {
            JSONObject jsonObject = new JSONObject(result);
            String token = jsonObject.getString("token");
            Prefs.putString("TOKEN", token);
            // Preference.setToken(token);
            Authenticate.token = token;
            Helpdesk.token = token;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return "success";
    }

}