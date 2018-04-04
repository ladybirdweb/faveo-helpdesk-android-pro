package co.helpdesk.faveo.pro.frontend.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.backend.api.v1.Authenticate;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.pro.frontend.fragments.ticketDetail.Conversation;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import es.dmoral.toasty.Toasty;

public class TicketReplyActivity extends AppCompatActivity {
ImageView imageView;public static String ticketID;
Button buttonSend;
EditText editTextReplyMessage;
ProgressDialog progressDialog;
TextView addCc;
ProgressBar progressBar;
    boolean isImageFitToScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_reply);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                //
                // Do the stuff
                //
                String result= new Authenticate().postAuthenticateUser(Prefs.getString("USERNAME", null), Prefs.getString("PASSWORD", null));
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject jsonObject1=jsonObject.getJSONObject("data");
                    JSONObject jsonObject2=jsonObject1.getJSONObject("user");
                    String role1=jsonObject2.getString("role");
                    if (role1.equals("user")){
                        Prefs.clear();
                        //Prefs.putString("role",role);
                        Intent intent=new Intent(TicketReplyActivity.this,LoginActivity.class);
                        Toasty.warning(TicketReplyActivity.this,getString(R.string.permission), Toast.LENGTH_LONG).show();
                        startActivity(intent);


                    }


                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
                }

                handler.postDelayed(this, 30000);
            }
        };
        runnable.run();
        ticketID=Prefs.getString("TICKETid",null);
        buttonSend = (Button) findViewById(R.id.button_send);
        imageView= (ImageView) findViewById(R.id.imageViewBackTicketReply);
        progressDialog = new ProgressDialog(this);
        progressBar= (ProgressBar) findViewById(R.id.progress_bar);
        addCc = (TextView) findViewById(R.id.addcc);
        editTextReplyMessage = (EditText) findViewById(R.id.editText_reply_message);
//        byte[] decodedString = Base64.decode(Prefs.getString("imageBase64",null), Base64.DEFAULT);
//        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//        imageViewAttachment.setImageBitmap(decodedByte);
        //Glide.with(TicketReplyActivity.this).load(getString(R.string.attachment)).crossFade().fitCenter().into(imageViewAttachment);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onBackPressed();
                Intent intent=new Intent(TicketReplyActivity.this,TicketDetailActivity.class);
                startActivity(intent);
            }
        });
//        imageViewAttachment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(isImageFitToScreen) {
//                    isImageFitToScreen=false;
//                    imageView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
//                    imageView.setAdjustViewBounds(true);
//                }else{
//                    isImageFitToScreen=true;
//                    imageView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
//                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                }
//            }
//        });
        addCc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TicketReplyActivity.this,collaboratorAdd.class);
                startActivity(intent);
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                String cc = editTextCC.getText().toString();
                        String replyMessage = editTextReplyMessage.getText().toString();
                        if (replyMessage.trim().length() == 0) {
                            Toasty.warning(TicketReplyActivity.this, getString(R.string.msg_must_not_be_empty), Toast.LENGTH_LONG).show();
                            return;
                        }

//                cc = cc.replace(", ", ",");
//                if (cc.length() > 0) {
//                    String[] multipleEmails = cc.split(",");
//                    for (String email : multipleEmails) {
//                        if (email.length() > 0 && !Helper.isValidEmail(email)) {
//                            Toasty.warning(TicketDetailActivity.this, getString(R.string.invalid_cc), Toast.LENGTH_LONG).show();
//                            return;
//                        }
//                    }
//                }

                        String userID = Prefs.getString("ID", null);
                        if (userID != null && userID.length() != 0) {
                            try {
                                replyMessage = URLEncoder.encode(replyMessage, "utf-8");
                                Log.d("Msg", replyMessage);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                            new ReplyTicket(Integer.parseInt(ticketID), replyMessage).execute();
                            progressDialog.setMessage(getString(R.string.sending_msg));
                            progressDialog.show();

                        } else
                            Toasty.warning(TicketReplyActivity.this, getString(R.string.wrong_user_id), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }
    private class ReplyTicket extends AsyncTask<String, Void, String> {
        int ticketID;
        String cc;
        String replyContent;

        ReplyTicket(int ticketID, String replyContent) {
            this.ticketID = ticketID;
            this.cc = cc;
            this.replyContent = replyContent;
        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().postReplyTicket(ticketID, replyContent);
        }

        protected void onPostExecute(String result) {
            Log.d("reply", result + "");
            progressDialog.dismiss();
            if (result == null) {
                Toasty.error(TicketReplyActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }
//            editTextCC.getText().clear();
            editTextReplyMessage.getText().clear();
            Toasty.success(TicketReplyActivity.this, getString(R.string.posted_reply), Toast.LENGTH_LONG).show();
            Intent intent=new Intent(TicketReplyActivity.this,TicketDetailActivity.class);
            startActivity(intent);

            // try {
//                TicketThread ticketThread;
//                JSONObject jsonObject = new JSONObject(result);
//                JSONObject res = jsonObject.getJSONObject("result");
//                String clientPicture = "";
//                try {
//                    clientPicture = res.getString("profile_pic");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                String messageTitle = "";
//                try {
//                    messageTitle = res.getString("title");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                String clientName = "";
//                try {
//                    clientName = res.getString("first_name") + " " + res.getString("last_name");
//                    if (clientName.equals("") || clientName == null)
//                        clientName = res.getString("user_name");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                String messageTime = res.getString("created_at");
//                String message = res.getString("body");
//                String isReply = "true";
//                try {
//                    isReply = res.getString("is_reply");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            // ticketThread = new TicketThread(clientPicture, clientName, messageTime, messageTitle, message, isReply);
            //ticketThread = new TicketThread(clientName, messageTime, message, isReply);

//            if (fragmentConversation != null) {
//                exitReveal();
//                //fragmentConversation.addThreadAndUpdate(ticketThread);
//            }
//            } catch (JSONException e) {
//                if (fragmentConversation != null) {
//                    exitReveal();
//                }
//                e.printStackTrace();
//                // Toast.makeText(TicketDetailActivity.this, "Unexpected Error ", Toast.LENGTH_LONG).show();
//            }
        }
    }
    class FetchCollaboratorAssociatedWithTicket extends AsyncTask<String, Void, String> {
        String ticketid;

        FetchCollaboratorAssociatedWithTicket(String ticketid) {

            this.ticketid = ticketid;
        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().postCollaboratorAssociatedWithTicket(ticketid);
        }

        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            addCc.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
//            new FetchTicketDetail(Prefs.getString("TICKETid",null)).execute();
            Prefs.putString("cameFromNotification","false");
            //progressDialog.dismiss();
            int noOfCollaborator = 0;
            if (isCancelled()) return;
//            if (progressDialog.isShowing())
//                progressDialog.dismiss();

            if (result == null) {
                //Toasty.error(TicketDetailActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
//                Data data=new Data(0,"No recipients");
//                stringArrayList.add(data);
                return;
            }

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("collaborator");
                if (jsonArray.length() == 0) {
                    addCc.setText("Add CC");
                    return;
                } else {

                    for (int i = 0; i < jsonArray.length(); i++) {
                        noOfCollaborator++;

                    }
                    addCc.setText("Add cc" + "(" + noOfCollaborator + " Recipients)");
                    //Toast.makeText(TicketDetailActivity.this, "noofcollaborators:" + noOfCollaborator, Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void checkConnection() {
        boolean isConnected = InternetReceiver.isConnected();
        showSnackIfNoInternet(isConnected);
    }
    private void showSnackIfNoInternet(boolean isConnected) {
        if (!isConnected) {
            final Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content), R.string.sry_not_connected_to_internet, Snackbar.LENGTH_INDEFINITE);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.RED);
            snackbar.setAction("X", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }

    }

    /**
     * Display the snackbar if network connection is there.
     *
     * @param isConnected is a boolean value of network connection.
     */
    private void showSnack(boolean isConnected) {

        if (isConnected) {

            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content), R.string.connected_to_internet, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        } else {
            showSnackIfNoInternet(false);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        addCc.setVisibility(View.GONE);
        new FetchCollaboratorAssociatedWithTicket(Prefs.getString("TICKETid", null)).execute();
    }

//    @Override
//    public void onBackPressed() {
//        if (!TicketDetailActivity.isShowing) {
//            Log.d("isShowing", "false");
//            Intent intent = new Intent(this, TicketDetailActivity.class);
//            startActivity(intent);
//        } else Log.d("isShowing", "true");
//
//
//            super.onBackPressed();
//        }
//        if (fabExpanded)
//            exitReveal();
//        else super.onBackPressed();
    }

