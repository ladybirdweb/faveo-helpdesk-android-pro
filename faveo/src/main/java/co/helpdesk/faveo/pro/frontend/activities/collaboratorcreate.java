package co.helpdesk.faveo.pro.frontend.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.helpdesk.faveo.pro.Helper;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import es.dmoral.toasty.Toasty;

public class collaboratorcreate extends AppCompatActivity {

    ImageView imageView;
    Button button;
    EditText editTextMail,editTextFirstName,editTextLastName;
    ProgressDialog progressDialog;
    String ticketId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collaboratorcreate);
        Window window = collaboratorcreate.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(collaboratorcreate.this,R.color.faveo));

        imageView= (ImageView) findViewById(R.id.imageViewBackAddUser);
        editTextMail= (EditText) findViewById(R.id.email_edittextUser);
        editTextFirstName= (EditText) findViewById(R.id.fname_edittextUser);
        editTextLastName= (EditText) findViewById(R.id.lastname_edittext);
        button= (Button) findViewById(R.id.buttonAddUser);
        progressDialog=new ProgressDialog(collaboratorcreate.this);
        progressDialog.setMessage(getString(R.string.pleasewait));
        ticketId=Prefs.getString("ticketId", null);
        Log.d("ticketId",ticketId);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(collaboratorcreate.this,collaboratorAdd.class);
                startActivity(intent);
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editTextMail.getText().toString().equals("")||editTextFirstName.getText().toString().equals("")||editTextLastName.getText().toString().equals("")){
                    Toasty.info(collaboratorcreate.this,getString(R.string.fill_all_the_details), Toast.LENGTH_LONG).show();
                    return;
                }

                else if (!editTextMail.getText().toString().equals("")&& !Helper.isValidEmail(editTextMail.getText().toString())){
                    Toasty.info(collaboratorcreate.this,getString(R.string.invalid_email),Toast.LENGTH_LONG).show();
                    return;
                }
                else if (editTextFirstName.getText().toString().trim().length() < 2) {
                    Toasty.warning(collaboratorcreate.this, getString(R.string.firstname_minimum_char), Toast.LENGTH_SHORT).show();
                }
                else if (editTextFirstName.getText().toString().trim().length() > 30) {
                    Toasty.warning(collaboratorcreate.this, getString(R.string.firstname_maximum_char), Toast.LENGTH_SHORT).show();
                }
                else if (editTextLastName.getText().toString().trim().length() < 2) {
                    Toasty.warning(collaboratorcreate.this, getString(R.string.lastname_minimum_char), Toast.LENGTH_SHORT).show();
                }
                else if (editTextLastName.getText().toString().trim().length() > 30) {
                    Toasty.warning(collaboratorcreate.this, getString(R.string.lastname_maximum_char), Toast.LENGTH_SHORT).show();
                }
                else{
                    final String email=editTextMail.getText().toString();
                    final String firstName=editTextFirstName.getText().toString();
                    final String lastName=editTextLastName.getText().toString();

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(collaboratorcreate.this);

                    // Setting Dialog Title
                    alertDialog.setTitle(getString(R.string.creatingTicket));

                    // Setting Dialog Message
                    alertDialog.setMessage(getString(R.string.createConfirmation));

                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.mipmap.ic_launcher);
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke YES event
                            //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                            if (InternetReceiver.isConnected()){
                                progressDialog.show();
                                new RegisterUserNew(email,firstName,lastName).execute();
                            }
                        }
                    });

                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
                            //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();




                }

            }
        });




    }
    private class RegisterUserNew extends AsyncTask<String, Void, String> {
        String firstname,email,lastname;

        RegisterUserNew(String email,String firstname,String lastname) {

            this.firstname=firstname;
            this.email=email;
            this.lastname=lastname;

        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().postCreateUser(email,firstname,lastname);
        }

        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            progressDialog.setMessage(getString(R.string.adding_collaborator));
            progressDialog.show();
            if (result == null) {
                Toasty.error(collaboratorcreate.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }
            try {
                String state = Prefs.getString("400", null);

                if (state.equals("badRequest")) {
                    progressDialog.dismiss();
                    Toasty.info(collaboratorcreate.this,"The user is already registered",Toast.LENGTH_LONG).show();
                    return;
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }

            try{
                JSONObject jsonObject=new JSONObject(result);
                JSONObject jsonObject1=jsonObject.getJSONObject("result");
                String error=jsonObject1.getString("error");
                if (error.equals("lang.methon_not_allowed")){
                    Toasty.success(collaboratorcreate.this,getString(R.string.registrationsuccesfull),Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(collaboratorcreate.this,collaboratorcreate.class);
                    startActivity(intent);

                }

            }catch (JSONException e){
                e.printStackTrace();
            }

            try {

                //JSONObject jsonObject=new JSONObject(result);
                JSONArray jsonArray=new JSONArray(result);
                //JSONObject jsonObject1=jsonObject.getJSONObject("result");
                for (int i=0;i<jsonArray.length();i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String message=jsonObject.getString("message");
                    JSONObject jsonObject2=jsonObject.getJSONObject("user");
                    email=jsonObject2.getString("email");
                    int id=jsonObject2.getInt("id");
                    Log.d("idNew",id+"");

                    if (message.contains("Activate your account! Click on the link that we've sent to your mail")){

                        new collaboratorAdduser(ticketId, String.valueOf(id)).execute();
                    }
//                    else{
//                        Toasty.success(collaboratorcreate.this,getString(R.string.registrationsuccesfull),Toast.LENGTH_SHORT).show();
//                        Intent intent=new Intent(collaboratorcreate.this,collaboratorcreate.class);
//                        startActivity(intent);
//                    }

                }




            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
    private class collaboratorAdduser extends AsyncTask<String, Void, String> {
        String ticketId, userId;

        public collaboratorAdduser(String ticketId, String userId) {
            this.ticketId = ticketId;
            this.userId = userId;
        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().createCollaborator(ticketId, String.valueOf(userId));
        }

        protected void onPostExecute(String result) {
            if (isCancelled()) return;
            progressDialog.dismiss();

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject jsonObject1 = jsonObject.getJSONObject("collaborator");
//                JSONArray jsonArray=jsonObject.getJSONArray("users");
//                if (jsonArray.length()==0){
//                    Toast.makeText(collaboratorAdd.this, "user not found", Toast.LENGTH_SHORT).show();
//                }
                String role = jsonObject1.getString("role");
                if (role.contains("ccc")) {
                    Toasty.success(collaboratorcreate.this, getString(R.string.collaboratoraddedsuccesfully), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(collaboratorcreate.this,collaboratorAdd.class);
                    startActivity(intent);
                    finish();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


//            if (progressDialog.isShowing())
//                progressDialog.dismiss();

//            if (result == null) {
//                Toasty.error(collaboratorAdd.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
//                return;
//            }


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(collaboratorcreate.this,collaboratorAdd.class);
        startActivity(intent);
        finish();
    }
}
