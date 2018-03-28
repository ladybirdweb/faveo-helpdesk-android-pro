package co.helpdesk.faveo.pro.backend.api.v1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONArray;
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
import co.helpdesk.faveo.pro.frontend.activities.LoginActivity;
import es.dmoral.toasty.Toasty;

/**
 * Created by Sumit
 */
class HTTPConnection{
    private StringBuilder sb = null;
    private InputStream is = null;
    private URL url;
    Context context;

   public HTTPConnection(Context context) {
        super();
        this.context = context;

    }
    HTTPConnection() {

        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
    }
    String HTTPResponsePost(String stringURL, String parameters) {

        try {
            url = new URL(stringURL);

            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Offer-type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=xxxxxxxxxx");
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
                String ret = null;
                switch (connection.getResponseCode()) {
                    case HttpURLConnection.HTTP_UNAUTHORIZED:
                        Log.e("Response code: ", "401-UNAUTHORIZED!");
                        //ret="HTTP_UNAUTHORIZED";
                        if (refreshToken() == null)
                            return null;
                        new Helpdesk();
                        new Authenticate();
                        return "tokenRefreshed";
                    case HttpURLConnection.HTTP_NOT_FOUND:
                        Log.e("Response code: ", "NotFound-404!");
                        //ret = "notFound";
                        break;
                    case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
                        Log.e("Response code: ", "Timeout!");
                       // ret = "timeout";
                        break;
                    case HttpURLConnection.HTTP_UNAVAILABLE:
                        Log.e("Response code: ", "Unavailable!");
                       // ret = "unavailable";
                        break;// retry, server is unstable
                    case HttpURLConnection.HTTP_BAD_REQUEST:
                        Log.e("Response code: ", "BadRequest!");
                        if (refreshToken() == null)
                            return null;
                        new Helpdesk();
                        new Authenticate();
                        ret = "tokenRefreshed";
                        break;
                    case HttpURLConnection.HTTP_FORBIDDEN:
                        Log.e("Response code","Forbidden");
                        ret="Forbidden";
                        Prefs.putString("403","403");

                        break;
                    default:

                        break; // abort
                }

                return ret;
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
        if (input.contains("token_expired") || input.contains("token_invalid")||input.contains("tokenRefreshed")) {
            if (refreshToken() == null)
                return null;
            new Helpdesk();
            new Authenticate();
            return "tokenRefreshed";
        }
        return sb.toString();
    }
    

    public String HTTPResponsePatch(String stringURL, String parameters) {
        try {
            url = new URL(stringURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("X-HTTP-Method-Override", "PATCH");
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Offer-type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setDoInput(true);

            OutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(outputStream, "UTF-8"));
            if (parameters!=null)
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
        if (input.contains("token_expired") || input.contains("token_invalid")) {
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
            Log.e("Response Codee", connection.getResponseCode() + "");

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                String ret = null;
                switch (connection.getResponseCode()) {
                    case HttpURLConnection.HTTP_UNAUTHORIZED:
                        Log.e("Response code: ", "401-UNAUTHORIZED!");
                        //ret="HTTP_UNAUTHORIZED";
                        if (refreshToken() == null)
                            return null;
                        new Helpdesk();
                        new Authenticate();
                        return "tokenRefreshed";
                    case HttpURLConnection.HTTP_NOT_FOUND:
                        Log.e("Response code: ", "404-NOT_FOUND!");
                        ret="HTTP_NOT_FOUND";
                        Log.d("404","came here");
                        Prefs.putString("404","True");
                        break;
                    case HttpURLConnection.HTTP_INTERNAL_ERROR:
                        Log.e("Response code: ", "500-INTERNAL_ERROR!");
                        ret="HTTP_INTERNAL_ERROR";
                        break;
                    case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
                        Log.e("Response code: ", "504-Timeout!");
                        ret="HTTP_GATEWAY_TIMEOUT";
                        break;// retry
                    case HttpURLConnection.HTTP_UNAVAILABLE:
                        Log.e("Response code: ", "503-Unavailable!");
                        ret="HTTP_UNAVAILABLE";
                        break;// retry, server is unstable
                    case HttpURLConnection.HTTP_BAD_REQUEST:
                        Log.e("Response code: ", "400-BadRequest!");
                        if (refreshToken() == null)
                            return null;
                        new Helpdesk();
                        new Authenticate();
                        ret = "tokenRefreshed";
                        break;
                    case HttpURLConnection.HTTP_FORBIDDEN:
                        Log.e("Response code","Forbidden");
                        if (refreshToken() == null)
                            return null;
                        new Helpdesk();
                        new Authenticate();
//                        ret = "tokenRefreshed";
//                        ret="Forbidden";
//                        Prefs.putString("403","403");

                        break;
                    default:

                        break; // abort
                }
                return ret;
            }
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
        if (input.contains("token_expired") || input.contains("token_invalid")|| input.contains("token_not_provided")) {
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
        if (result == null) {
            return null;
        }
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject jsonObject1=jsonObject.getJSONObject("data");
            String token = jsonObject1.getString("token");
            Log.d("result",result);
            //String token = jsonObject.getString("token");
            Prefs.putString("TOKEN", token);
            Authenticate.token = token;
            Helpdesk.token = token;

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("cameInException","true");
            Prefs.clear();
            Prefs.putString("NoToken","True");
            return null;
        }
        return "success";
    }

}