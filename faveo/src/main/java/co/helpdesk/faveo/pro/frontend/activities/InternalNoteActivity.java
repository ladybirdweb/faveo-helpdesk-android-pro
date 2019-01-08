package co.helpdesk.faveo.pro.frontend.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import es.dmoral.toasty.Toasty;

public class InternalNoteActivity extends AppCompatActivity {
    ImageView imageView;
    EditText editTextInternalNote;
    Button buttonCreate;
    public static String ticketID;
    ProgressDialog progressDialog;
    String option;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internal_note);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Window window = InternalNoteActivity.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(InternalNoteActivity.this,R.color.mainActivityTopBar));
        option=Prefs.getString("cameFromNotification", null);
        switch (option) {
            case "true":
                Prefs.putString("cameFromNotification","true");
                break;
            case "false":
                Prefs.putString("cameFromNotification","false");
                break;
            case "none":
                Prefs.putString("cameFromNotification","none");
                break;
            case "client":
                Prefs.putString("cameFromNotification","client");
                break;
            default:
                Prefs.putString("cameFromNotification","");
                break;
        }
        imageView= (ImageView) findViewById(R.id.imageViewBackTicketInternalNote);
        editTextInternalNote = (EditText) findViewById(R.id.editText_internal_note);
        editTextInternalNote.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.editText_internal_note) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });
        buttonCreate = (Button) findViewById(R.id.button_create);
        final Intent intent = getIntent();
        ticketID=intent.getStringExtra("ticket_id");
        Prefs.putString("TICKETid",ticketID);
        Prefs.putString("ticketId",ticketID);
        progressDialog=new ProgressDialog(this);
            imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
//                Intent intent1=new Intent(InternalNoteActivity.this,TicketDetailActivity.class);
//                intent1.putExtra("ticket_id", ticketID);
//                startActivity(intent1);
            }
        });
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = editTextInternalNote.getText().toString();
                if (note.trim().length() == 0) {
                    Toasty.warning(InternalNoteActivity.this, getString(R.string.msg_must_not_be_empty), Toast.LENGTH_LONG).show();
                    return;
                }
                String userID = Prefs.getString("ID", null);
                if (userID != null && userID.length() != 0) {
                    try {
                        note = URLEncoder.encode(note, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    new CreateInternalNoteForTicket(Integer.parseInt(ticketID), Integer.parseInt(userID), note).execute();
                    progressDialog.setMessage(getString(R.string.creating_note));
                    progressDialog.show();


                } else
                    Toasty.warning(InternalNoteActivity.this, getString(R.string.wrong_user_id), Toast.LENGTH_LONG).show();
            }
        });

    }
    class CreateInternalNoteForTicket extends AsyncTask<String, Void, String> {
        int ticketID;
        int userID;
        String note;

        CreateInternalNoteForTicket(int ticketID, int userID, String note) {
            this.ticketID = ticketID;
            this.userID = userID;
            this.note = note;
        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().postCreateInternalNote(ticketID, userID, note);
        }

        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            if (result == null) {
                Toasty.error(InternalNoteActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }
            new FetchTicketThreads(InternalNoteActivity.this, Prefs.getString("TICKETid", null)).execute();

        }
    }
    class FetchTicketThreads extends AsyncTask<String, Void, String> {
        Context context;
        String ticketID;


        FetchTicketThreads(Context context,String ticketID) {
            this.context = context;
            this.ticketID = ticketID;
        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().getTicketThread(ticketID);
        }

        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            Log.d("calledFromReply","true");
            try {
                progressDialog.dismiss();
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            if (result == null) {
                //Toasty.error(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }
            try {
                JSONObject jsonObject=new JSONObject(result);
                Log.d("ticketThreadReply",jsonObject.toString());
                Prefs.putString("ticketThread",jsonObject.toString());
                Toasty.success(InternalNoteActivity.this, getString(R.string.internal_notes_posted), Toast.LENGTH_LONG).show();
                Intent intent=new Intent(InternalNoteActivity.this,MainActivity.class);
                startActivity(intent);
                //editTextInternalNote.getText().clear();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onBackPressed() {
        finish();
//        Intent intent1=new Intent(InternalNoteActivity.this,TicketDetailActivity.class);
//        intent1.putExtra("ticket_id", ticketID);
//        startActivity(intent1);
    }
}
