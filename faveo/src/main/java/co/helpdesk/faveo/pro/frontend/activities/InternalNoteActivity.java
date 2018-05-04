package co.helpdesk.faveo.pro.frontend.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import co.helpdesk.faveo.pro.backend.api.v1.Authenticate;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import es.dmoral.toasty.Toasty;

public class InternalNoteActivity extends AppCompatActivity {
    ImageView imageView;
    EditText editTextInternalNote;
    Button buttonCreate;
    public static String ticketID;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internal_note);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
//        final Handler handler = new Handler();
//        Runnable runnable = new Runnable() {
//            public void run() {
//                //
//                // Do the stuff
//                //
//                String result= new Authenticate().postAuthenticateUser(Prefs.getString("USERNAME", null), Prefs.getString("PASSWORD", null));
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    JSONObject jsonObject1=jsonObject.getJSONObject("data");
//                    JSONObject jsonObject2=jsonObject1.getJSONObject("user");
//                    String role1=jsonObject2.getString("role");
//                    if (role1.equals("user")){
//                        Prefs.clear();
//                        //Prefs.putString("role",role);
//                        Intent intent=new Intent(InternalNoteActivity.this,LoginActivity.class);
//                        Toasty.info(InternalNoteActivity.this,getString(R.string.roleChanged), Toast.LENGTH_LONG).show();
//                        startActivity(intent);
//
//
//                    }
//
//
//                } catch (JSONException | NullPointerException e) {
//                    e.printStackTrace();
//                }
//
//                handler.postDelayed(this, 30000);
//            }
//        };
//        runnable.run();
        imageView= (ImageView) findViewById(R.id.imageViewBackTicketInternalNote);
        editTextInternalNote = (EditText) findViewById(R.id.editText_internal_note);
        buttonCreate = (Button) findViewById(R.id.button_create);
        ticketID= Prefs.getString("TICKETid",null);
        progressDialog=new ProgressDialog(this);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(InternalNoteActivity.this,TicketDetailActivity.class);
                startActivity(intent);
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
            Toasty.success(InternalNoteActivity.this, getString(R.string.internal_notes_posted), Toast.LENGTH_LONG).show();
            Intent intent=new Intent(InternalNoteActivity.this,TicketDetailActivity.class);
            startActivity(intent);
            editTextInternalNote.getText().clear();
        }
    }


}
