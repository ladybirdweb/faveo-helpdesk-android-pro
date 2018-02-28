package co.helpdesk.faveo.pro.frontend.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import es.dmoral.toasty.Toasty;

public class FeedBackActivity extends AppCompatActivity {
ImageView imageView;
String supportEmail="support@ladybirdweb.com";
Button buttonSend;
EditText editTextsubjectFeedback,editTextmessageFeedback,editTextemail;
String message,subject="";
ProgressDialog progressDialog;
String billingUrl,clientName,emailForFeedback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        imageView= (ImageView) findViewById(R.id.imageViewBack);
        editTextmessageFeedback= (EditText) findViewById(R.id.messageFeedback);
        editTextsubjectFeedback= (EditText) findViewById(R.id.subjectFeedback);
        buttonSend= (Button) findViewById(R.id.sendFeedback);
        editTextemail= (EditText) findViewById(R.id.emailfeedback);
        editTextemail.setEnabled(false);
        progressDialog=new ProgressDialog(this);
        try {
            billingUrl = Prefs.getString("BASE_URL", null);
            clientName = Prefs.getString("clientNameForFeedback", null);
            emailForFeedback = Prefs.getString("emailForFeedback", null);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        Log.d("billingFromFeedback",billingUrl);
        Log.d("supportEmail",supportEmail);
        Log.d("emailForFeedback",emailForFeedback);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FeedBackActivity.this,SettingsActivity.class);
                startActivity(intent);
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message=editTextmessageFeedback.getText().toString();
                subject=editTextsubjectFeedback.getText().toString();
                StringBuilder stringBuffer=new StringBuilder(message);
                stringBuffer.append("URL: "+billingUrl);
                String newMessage=message+"<br/><br/><br/><B>URL: </B>"+billingUrl+"<br/><B>Name: </B>"+clientName+"<br/><B>Email: </B>"+emailForFeedback;

                if (message.equals("")&&subject.equals("")){
                    Toasty.info(FeedBackActivity.this,getString(R.string.messageandsubject), Toast.LENGTH_LONG).show();
                    return;
                }
                else if (subject.equals("")){
                    Toasty.info(FeedBackActivity.this,getString(R.string.selectSubject), Toast.LENGTH_LONG).show();
                    return;
                }
                else if (message.equals("")){
                    Toasty.info(FeedBackActivity.this,getString(R.string.selectMessage), Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    if (InternetReceiver.isConnected()){
                        try {
                            message = URLEncoder.encode(newMessage.trim(), "utf-8");
                            subject=URLEncoder.encode(subject.trim(), "utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        progressDialog.setMessage(getString(R.string.submitFeedback));
                        progressDialog.show();
                        new CustomerFeedback(subject,message).execute();
                    }
                }



            }
        });
    }
    private class CustomerFeedback extends AsyncTask<String, Void, String> {
        String subject;
        String message;


        public CustomerFeedback(String subject, String message) {
            this.subject = subject;
            this.message = message;
        }

        protected String doInBackground(String... urls) {

            return new Helpdesk().customerFeedback(subject, message);
        }

        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            Log.d("result",result);
            if (result == null) {
                Toasty.error(FeedBackActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }
//
//            if (result.startsWith("Message Sent!")){
//                Toasty.success(FeedBackActivity.this,getString(R.string.feedbackPosted),Toast.LENGTH_LONG).show();
//                Intent intent=new Intent(FeedBackActivity.this,SettingsActivity.class);
//                startActivity(intent);
//            }
//            String state= Prefs.getString("403",null);
//            try {
//                if (state.equals("403") && !state.equals("null")) {
//                    Toasty.warning(FeedBackActivity.this, getString(R.string.permission), Toast.LENGTH_LONG).show();
//                    Prefs.putString("403", "null");
//                    return;
//                }
//            }catch (NullPointerException e){
//                e.printStackTrace();
//            }

            try{
                JSONObject jsonObject=new JSONObject(result);
                //Log.d("feedbackresult",jsonObject.toString());
                String resultFromResponse=jsonObject.getString("result");
                if (resultFromResponse.equals("Message Sent! Thanks for reaching out! Someone from our team will get back to you soon.")){
                    Toasty.success(FeedBackActivity.this,getString(R.string.feedbackPosted),Toast.LENGTH_LONG).show();
                Intent intent=new Intent(FeedBackActivity.this,SettingsActivity.class);
                startActivity(intent);
                }

            }catch (JSONException e){
                e.printStackTrace();
            }


//            try {
//
//                JSONObject jsonObject=new JSONObject(result);
//                JSONObject jsonObject1=jsonObject.getJSONObject("response");
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }


        }


    }
}
