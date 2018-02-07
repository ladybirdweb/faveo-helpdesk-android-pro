package co.helpdesk.faveo.pro.frontend.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.helpdesk.faveo.pro.Helper;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.pro.model.Data;
import es.dmoral.toasty.Toasty;

public class collaboratorAdd extends AppCompatActivity {

    AutoCompleteTextView autoCompleteTextViewUser;
    Button searchUer, deleteUser;
    ArrayList<Data> stringArrayList;
    ArrayAdapter<Data> arrayAdapterCC;
    RelativeLayout relativeLayout;
    ArrayAdapter<String> spinnerPriArrayAdapter;
    int id = 0;
    String email, finalEmail, finalEmail2;
    ImageView imageView;
    Toolbar toolbar;
    String email1;
    String term;
    ArrayList<String> strings;
    Spinner recipients;
    String email2;
    ProgressDialog progressDialog;
//    ArrayList<String> strings;
//    @BindView(R.id.toolbar)
//    android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_collaborator_add);
        recipients= (Spinner) findViewById(R.id.spinnerRecipients);
        relativeLayout= (RelativeLayout) findViewById(R.id.recipients);
        strings = new ArrayList<>();
        strings.add("Show Recipients");
        progressDialog=new ProgressDialog(collaboratorAdd.this);
        //recipients.setVisibility(View.VISIBLE);
//        strings.add(getString(R.string.searchuser));

        new FetchCollaboratorAssociatedWithTicket(Prefs.getString("TICKETid", null)).execute();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImageView imageView = (ImageView) toolbar.findViewById(R.id.imageViewBack);
        autoCompleteTextViewUser = (AutoCompleteTextView) findViewById(R.id.appCompatAutoCompleteTextView);
        autoCompleteTextViewUser.setHorizontallyScrolling(true);
        autoCompleteTextViewUser.setMovementMethod(new ScrollingMovementMethod());
        searchUer = (Button) findViewById(R.id.buttonSearchUser);
        deleteUser = (Button) findViewById(R.id.buttonDeleteUser);
        stringArrayList = new ArrayList<Data>();


        arrayAdapterCC = new ArrayAdapter<Data>(collaboratorAdd.this, android.R.layout.simple_dropdown_item_1line, stringArrayList);
        autoCompleteTextViewUser.setThreshold(1);
        autoCompleteTextViewUser.addTextChangedListener(passwordWatcheredittextSubject);
        //new FetchCollaborator("s").execute();
        email1 = autoCompleteTextViewUser.getText().toString();

        autoCompleteTextViewUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                arrayAdapterCC = new ArrayAdapter<>(collaboratorAdd.this, android.R.layout.simple_dropdown_item_1line, stringArrayList);
                Data data = stringArrayList.get(i);
                id = data.getID();
                email = data.getName();
//                        int pos=email.indexOf("<");
//                        int pos1=email.lastIndexOf(">");
//                        finalEmail=email.substring(pos+1,pos1);
                //Toast.makeText(collaboratorAdd.this, "email:" + email, Toast.LENGTH_SHORT).show();

//                for (int j=0;j<stringArrayList.size();j++){
//                    if (stringArrayList.get(i).getName().equalsIgnoreCase(email)){
//                       // strings.add(finalEmail);
//                        Toast.makeText(collaboratorAdd.this, "match found", Toast.LENGTH_SHORT).show();
//                        //autoCompleteTextViewUser.setText("");
//                        //Log.d("strings",strings+"");
//                    }
//                    else{
//                        strings.remove(finalEmail);
//                        Toast.makeText(collaboratorAdd.this, "match not found", Toast.LENGTH_SHORT).show();
//
//                    }
            }


        });



        spinnerPriArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, strings); //selected item will look like a spinner set from XML
        spinnerPriArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recipients.setAdapter(spinnerPriArrayAdapter);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(collaboratorAdd.this, TicketDetailActivity.class);
                startActivity(intent);
            }
        });

      recipients.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (i==0){
                email2=null;
            }
            else{
               email2=recipients.getSelectedItem().toString();
                //Toast.makeText(collaboratorAdd.this, "email is:"+email2, Toast.LENGTH_SHORT).show();
            }
          }

          @Override
          public void onNothingSelected(AdapterView<?> adapterView) {
                email2=null;
          }
      });
        searchUer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email=autoCompleteTextViewUser.getText().toString();

//                String email=autoCompleteTextViewUser.getText().toString();
//                int pos=email.lastIndexOf(",");
//                String newemail=email.substring(0,pos);
                //List<String> list = new ArrayList<String>(Arrays.asList(string.split(" , ")));
//                String newresult= Arrays.toString(result);
                //Log.d("newemail",newresult);


                if (autoCompleteTextViewUser.getText().toString().equals("")) {
                    Toasty.info(collaboratorAdd.this, getString(R.string.collaboratorEmpty), Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!autoCompleteTextViewUser.getText().toString().contains("<")){
                    Toasty.info(collaboratorAdd.this,getString(R.string.collaboratorExisting),Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    progressDialog.setMessage(getString(R.string.pleasewait));
                    progressDialog.show();
                    new collaboratorAdduser(Prefs.getString("TICKETid", null), String.valueOf(id)).execute();
                }
//                if (email.contains("<")) {
////                    int pos = email.indexOf("<");
////                    int pos1 = email.lastIndexOf(">");
////                    finalEmail2=email.substring(pos+1,pos1);
//
//
//                }else{
//                    Toasty.info(collaboratorAdd.this,getString(R.string.userEmpty),Toast.LENGTH_SHORT).show();
//                    //Toast.makeText(collaboratorAdd.this,getString(R.string.userEmpty), Toast.LENGTH_SHORT).show();
//                    return;
//                }


//                else if (!finalEmail2.equalsIgnoreCase(finalEmail)){
//
//                }


//                else if (id==0){
//                    Toast.makeText(collaboratorAdd.this, "user not found", Toast.LENGTH_SHORT).show();
//                    return;
//                }


                //Toast.makeText(collaboratorAdd.this, "added", Toast.LENGTH_SHORT).show();

            }
        });
        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String email = autoCompleteTextViewUser.getText().toString();

//                if (Prefs.getString("noUser",null).equals("null")){
//                    Toasty.info(collaboratorAdd.this,getString(R.string.userEmpty),Toast.LENGTH_SHORT).show();
//                }
                //Toast.makeText(collaboratorAdd.this, "clicked on delete", Toast.LENGTH_SHORT).show();
                int pos=recipients.getSelectedItemPosition();
                try {

                    if (pos==0){
                        Toasty.info(collaboratorAdd.this,getString(R.string.userEmpty),Toast.LENGTH_SHORT).show();

                    }
                    else {
                        email2 = recipients.getSelectedItem().toString();
                        progressDialog=new ProgressDialog(collaboratorAdd.this);
                        progressDialog.setMessage(getString(R.string.pleasewait));
                        progressDialog.show();
                        new collaboratorRemoveUser(Prefs.getString("TICKETid", null), email2).execute();
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }


                    //Toast.makeText(collaboratorAdd.this,getString(R.string.userEmpty), Toast.LENGTH_SHORT).show();





            }
        });


    }

    private class FetchCollaborator extends AsyncTask<String, Void, String> {
        String term;

        FetchCollaborator(String term) {

            this.term = term;
        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().getUser(term);
        }

        protected void onPostExecute(String result) {
            if (isCancelled()) return;
            stringArrayList.clear();
//            if (progressDialog.isShowing())
//                progressDialog.dismiss();

//            if (result == null) {
//                Toasty.error(collaboratorAdd.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
//                return;
//            }

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("users");
                if (jsonArray.length() == 0) {
                    Prefs.putString("noUser", "null");
                } else {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String email = jsonObject1.getString("email");
                        id = Integer.parseInt(jsonObject1.getString("id"));
                        String first_name = jsonObject1.getString("first_name");
                        String last_name = jsonObject1.getString("last_name");
                        //Toast.makeText(TicketSaveActivity.this, "email:"+email, Toast.LENGTH_SHORT).show();
                        Data data = new Data(id, first_name + " " + last_name + " <" + email + ">");
                        stringArrayList.add(data);
                        Prefs.putString("noUser", "1");
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
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
            return new Helpdesk().createCollaborator(Prefs.getString("TICKETid", null), String.valueOf(userId));
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
                    autoCompleteTextViewUser.setText("");
                    id = 0;
                    Toasty.success(collaboratorAdd.this, getString(R.string.collaboratoraddedsuccesfully), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(collaboratorAdd.this,TicketDetailActivity.class);
                    startActivity(intent);
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

    private class collaboratorRemoveUser extends AsyncTask<String, Void, String> {
        String ticketId, userId;

        public collaboratorRemoveUser(String ticketId, String userId) {
            this.ticketId = ticketId;
            this.userId = userId;
        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().removeCollaborator(Prefs.getString("TICKETid", null), email2);
        }

        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            if (isCancelled()) return;
            Log.d("result:",result);
            try {
                    JSONObject jsonObject = new JSONObject(result);
                    String collaborator=jsonObject.getString("collaborator");
                    if (collaborator.equals("deleted successfully")){
                        Toasty.success(collaboratorAdd.this, getString(R.string.collaboratorRemove), Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(collaboratorAdd.this,TicketDetailActivity.class);
                        startActivity(intent);
                    }
//                JSONArray jsonArray=jsonObject.getJSONArray("users");
//                if (jsonArray.length()==0){
//                    Toast.makeText(collaboratorAdd.this, "user not found", Toast.LENGTH_SHORT).show();
//                }
//                String role=jsonObject1.getString("role");
//                if (role.contains("ccc")){
//                    autoCompleteTextViewUser.setText("");
//                    id=0;
//                    Toasty.success(collaboratorAdd.this,getString(R.string.collaboratoraddedsuccesfully),Toast.LENGTH_SHORT).show();
//                }

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

    private class FetchCollaboratorAssociatedWithTicket extends AsyncTask<String, Void, String> {
        String ticketid;

        FetchCollaboratorAssociatedWithTicket(String ticketid) {

            this.ticketid = ticketid;
        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().postCollaboratorAssociatedWithTicket(ticketid);
        }

        protected void onPostExecute(String result) {
            int noOfCollaborator=0;
            if (isCancelled()) return;
            //strings.clear();

//            if (progressDialog.isShowing())
//                progressDialog.dismiss();

            if (result == null) {
                Toasty.error(collaboratorAdd.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
//                Data data=new Data(0,"No recipients");
//                stringArrayList.add(data);
                return;
            }

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("collaborator");
                if (jsonArray.length()==0){
                    recipients.setVisibility(View.GONE);
                   return;
                }else {
                    relativeLayout.setVisibility(View.VISIBLE);
                    recipients.setVisibility(View.VISIBLE);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        noOfCollaborator++;
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String email = jsonObject1.getString("email");
                        //String first_name = jsonObject1.getString("first_name");
                        //String last_name = jsonObject1.getString("last_name");
                        //int id= Integer.parseInt(jsonObject1.getString("id"));
                        //Toast.makeText(TicketSaveActivity.this, "email:"+email, Toast.LENGTH_SHORT).show();

                        //stringArrayList.add(data);

                        strings.add(email);

                    }
                    //Toast.makeText(collaboratorAdd.this, "noofcollaborators:"+noOfCollaborator, Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


//                if (jsonObject1.getString("last_message").equals("null")) {
//                    editTextLastMessage.setText("Not available");
//                } else
//                    editTextLastMessage.setText(jsonObject1.getString("last_message"));


        }


//     class collaboratorRemoveuser extends AsyncTask<String, Void, String> {
//        String ticketId,userId;
//
//        public collaboratorRemoveuser(String ticketId, String userId) {
//            this.ticketId = ticketId;
//            this.userId = userId;
//        }
//
//        protected String doInBackground(String... urls) {
//            return new Helpdesk().removeCollaborator(Prefs.getString("ticketID",null),finalEmail);
//        }
//
//        protected void onPostExecute(String result) {
//            if (isCancelled()) return;
//
//            try {
//                JSONObject jsonObject=new JSONObject(result);
//                String collaboratorNotFound=jsonObject.getString("collaborator");
//                if (collaboratorNotFound.equals("not found")) {
//                    Toasty.warning(collaboratorAdd.this, getString(R.string.collaboratorNotFound), Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                else if (collaboratorNotFound.equals("deleted successfully")){
//                    .setText("");
//                    Toasty.success(collaboratorAdd.this,getString(R.string.collaboratorRemove),Toast.LENGTH_SHORT).show();
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
////            if (progressDialog.isShowing())
////                progressDialog.dismiss();
//
////            if (result == null) {
////                Toasty.error(collaboratorAdd.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
////                return;
////            }
//
//
//
//        }




    }
    final TextWatcher passwordWatcheredittextSubject = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            //Toast.makeText(TicketSaveActivity.this, "API called", Toast.LENGTH_SHORT).show();
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            term = autoCompleteTextViewUser.getText().toString();
            searchUer.setVisibility(View.VISIBLE);
            if (InternetReceiver.isConnected()) {
                if (term.contains(",")) {
                    int pos = term.lastIndexOf(",");
                    term = term.substring(pos + 1, term.length());
                    Log.d("newTerm", term);
                    arrayAdapterCC = new ArrayAdapter<>(collaboratorAdd.this, android.R.layout.simple_dropdown_item_1line, stringArrayList);
                    new FetchCollaborator(term.trim()).execute();
                    autoCompleteTextViewUser.setAdapter(arrayAdapterCC);
                }
//            Toast.makeText(collaboratorAdd.this, "term:"+term, Toast.LENGTH_SHORT).show();
                else if (term.equals("")) {
                    arrayAdapterCC = new ArrayAdapter<>(collaboratorAdd.this, android.R.layout.simple_dropdown_item_1line, stringArrayList);
                    //new FetchCollaborator("s").execute();
                    Data data = new Data(0, "No result found");
                    stringArrayList.add(data);
//                autoCompleteTextViewCC.setAdapter(stringArrayAdapterCC);
//                stringArrayAdapterCC.notifyDataSetChanged();
//                autoCompleteTextViewCC.setThreshold(0);
//                autoCompleteTextViewCC.setDropDownWidth(1000);

                } else {
                    arrayAdapterCC = new ArrayAdapter<>(collaboratorAdd.this, android.R.layout.simple_dropdown_item_1line, stringArrayList);
                    new FetchCollaborator(term).execute();
                    autoCompleteTextViewUser.setAdapter(arrayAdapterCC);


                    //stringArrayAdapterCC.notifyDataSetChanged();
//                autoCompleteTextViewCC.setThreshold(0);
//                autoCompleteTextViewCC.setDropDownWidth(1000);

                }


                //buttonsave.setEnabled(true);
            }
        }

        public void afterTextChanged(Editable s) {
        if (term.equals("")){
            searchUer.setVisibility(View.GONE);
        }
        else{
            searchUer.setVisibility(View.VISIBLE);
        }
        }
    };
}
