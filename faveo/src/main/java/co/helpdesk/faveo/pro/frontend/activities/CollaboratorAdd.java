package co.helpdesk.faveo.pro.frontend.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import co.helpdesk.faveo.pro.CircleTransform;
import co.helpdesk.faveo.pro.Helper;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.pro.frontend.adapters.CollaboratorAdapter;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.pro.model.AttachedCollaborator;
import co.helpdesk.faveo.pro.model.CollaboratorSuggestion;
import es.dmoral.toasty.Toasty;

public class CollaboratorAdd extends AppCompatActivity {

    AutoCompleteTextView autoCompleteTextViewUser;
    Button searchUer, deleteUser;
    ArrayList<CollaboratorSuggestion> stringArrayList;
    CollaboratorAdapter arrayAdapterCC;
    ArrayAdapter<String> spinnerPriArrayAdapter;
    int id = 0;
    int id1 = 0;
    String email;
    ImageView imageView;
    Toolbar toolbar;
    String email1;
    String term;
    ArrayList<String> strings;
    //Spinner recipients;
    String email2;
    ProgressDialog progressDialog;
    public static boolean isShowing = false;
    ProgressBar progressBar;
    ImageButton buttonAdd;
    List<AttachedCollaborator> movieList = new ArrayList<>();
    RecyclerView recyclerView;
    Collaboratoradapter mAdapter;
    RecyclerView list;
    private String ticketID;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collaborator_add);
        Window window = CollaboratorAdd.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(CollaboratorAdd.this,R.color.faveo));
        strings = new ArrayList<>();
        recyclerView= (RecyclerView) findViewById(R.id.list);
        strings.add("Show");

        isShowing=true;
        progressBar= (ProgressBar) findViewById(R.id.collaboratorProgressBarReply);
        progressDialog=new ProgressDialog(CollaboratorAdd.this);
        final Intent intent = getIntent();
        ticketID=intent.getStringExtra("ticket_id");
        Prefs.putString("TICKETid",ticketID);
        Prefs.putString("ticketId",ticketID);
        if (InternetReceiver.isConnected()){
            new FetchCollaboratorAssociatedWithTicket(Prefs.getString("ticketId", null)).execute();
            progressBar.setVisibility(View.VISIBLE);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCollaborator);
        ImageView imageView = (ImageView) toolbar.findViewById(R.id.imageViewBack);
        buttonAdd= (ImageButton) findViewById(R.id.collaboratorAdd);
        autoCompleteTextViewUser = (AutoCompleteTextView) findViewById(R.id.appCompatAutoCompleteTextView);
        autoCompleteTextViewUser.setHorizontallyScrolling(true);
        autoCompleteTextViewUser.setMovementMethod(new ScrollingMovementMethod());
        searchUer = (Button) findViewById(R.id.buttonSearchUser);

        deleteUser = (Button) findViewById(R.id.buttonDeleteUser);
        stringArrayList = new ArrayList<CollaboratorSuggestion>();
        arrayAdapterCC=new CollaboratorAdapter(CollaboratorAdd.this,stringArrayList);
        //arrayAdapterCC = new ArrayAdapter<Data>(CollaboratorAdd.this, android.R.layout.simple_dropdown_item_1line, stringArrayList);

        autoCompleteTextViewUser.addTextChangedListener(passwordWatcheredittextSubject);
        email1 = autoCompleteTextViewUser.getText().toString();
        autoCompleteTextViewUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name1=autoCompleteTextViewUser.getText().toString();
                for (int i1=0;i1<stringArrayList.size();i1++){
                    CollaboratorSuggestion data1=stringArrayList.get(i1);
                    if (data1.getEmail().equals(name1)){
                        CollaboratorSuggestion data2=stringArrayList.get(i1);
                        id1=data2.getId();
                        Log.d("id",id+"");
                        email1=data2.getEmail();
                        Log.d("email1",email1);
                        autoCompleteTextViewUser.setText(email1);
                        searchUer.setVisibility(View.VISIBLE);
                    }
                }

            }


        });
        spinnerPriArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, strings); //selected item will look like a spinner set from XML
        spinnerPriArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollaboratorAdd.super.onBackPressed();
//                try {
//                    if (Prefs.getString("cameFromTicket", null).equals("true")) {
//                        Intent intent = new Intent(CollaboratorAdd.this, TicketDetailActivity.class);
//                        intent.putExtra("ticket_id", ticketID);
//                        Prefs.putString("cameFromTicket","false");
//                        startActivity(intent);
//                        finish();
//                    } else {
//                        Intent intent = new Intent(CollaboratorAdd.this, TicketReplyActivity.class);
//                        intent.putExtra("ticket_id", ticketID);
//                        startActivity(intent);
//                        finish();
//                    }
//                }catch (NullPointerException e){
//                    e.printStackTrace();
//                }
            }
        });


        searchUer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email=autoCompleteTextViewUser.getText().toString();

                if (!email1.equals(email)&&autoCompleteTextViewUser.getText().toString().equals("")) {
                    Toasty.info(CollaboratorAdd.this, getString(R.string.collaboratorEmpty), Toast.LENGTH_SHORT).show();
                    id1=0;
                }
                else if (id1==0){
                    Toasty.info(CollaboratorAdd.this, getString(R.string.collaboratorNotFound), Toast.LENGTH_SHORT).show();
                }
                else{
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(CollaboratorAdd.this);
                    // Setting Dialog Title
                    alertDialog.setTitle("Adding collaborator...");
                    // Setting Dialog Message
                    alertDialog.setMessage("Are you sure you want to " +
                            "add this user as collaborator?");
                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.mipmap.ic_launcher);
                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke YES event
                            Log.d("id of the user", String.valueOf(id));
                            if (id1==0){
                                Toasty.error(CollaboratorAdd.this, getString(R.string.collaboratorNotFound), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            progressDialog.setMessage(getString(R.string.pleasewait));
                            progressDialog.show();
                            new collaboratorAdduser(Prefs.getString("ticketId", null), String.valueOf(id1)).execute();

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

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialogClassSolution cdd = new CustomDialogClassSolution(CollaboratorAdd.this);
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.show();
                    //finish();
            }
        });
        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    if (email2.equals("")){
                        Toasty.info(CollaboratorAdd.this,getString(R.string.userEmpty),Toast.LENGTH_SHORT).show();

                    }
                    else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CollaboratorAdd.this);
                        alertDialog.setMessage(R.string.user_collaborator);
                        alertDialog.setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        alertDialog.setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog=new ProgressDialog(CollaboratorAdd.this);
                                progressDialog.setMessage(getString(R.string.pleasewait));
                                progressDialog.show();
                                Log.d("email3",email2);
                                new collaboratorRemoveUser(Prefs.getString("ticketId", null), email2).execute();
                                // DO SOMETHING HERE

                            }
                        });

                        AlertDialog dialog = alertDialog.create();
                        dialog.show();


                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }


                //Toast.makeText(CollaboratorAdd.this,getString(R.string.userEmpty), Toast.LENGTH_SHORT).show();
            }
        });
////

    }


    class CustomDialogClassSolution extends Dialog implements
            android.view.View.OnClickListener {
        String firstName,lastName;
        public Activity c;
        public Dialog d;
        public Button buttoncreate, buttonclose;
        EditText editTextEmail,editTextFirstName,editTextLastName;
        public CustomDialogClassSolution(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.activity_collaboratorcreate);
            buttoncreate = (Button) findViewById(R.id.buttonAddUser);
            buttonclose = (Button) findViewById(R.id.buttonClose);
            editTextEmail=findViewById(R.id.email_edittextUser);
            editTextFirstName=findViewById(R.id.fname_edittextUser);
            editTextLastName=findViewById(R.id.lastname_edittext);
            buttoncreate.setOnClickListener(this);
            buttonclose.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonAddUser:
                    if (editTextEmail.getText().toString().equals("") || editTextFirstName.getText().toString().equals("") || editTextLastName.getText().toString().equals("")) {
                        Toasty.info(CollaboratorAdd.this, getString(R.string.fill_all_the_details), Toast.LENGTH_LONG).show();
                        return;
                    } else if (!editTextEmail.getText().toString().equals("") && !Helper.isValidEmail(editTextEmail.getText().toString())) {
                        Toasty.info(CollaboratorAdd.this, getString(R.string.invalid_email), Toast.LENGTH_LONG).show();
                        return;
                    } else if (editTextFirstName.getText().toString().trim().length() < 2) {
                        Toasty.warning(CollaboratorAdd.this, getString(R.string.firstname_minimum_char), Toast.LENGTH_SHORT).show();
                    } else if (editTextFirstName.getText().toString().trim().length() > 30) {
                        Toasty.warning(CollaboratorAdd.this, getString(R.string.firstname_maximum_char), Toast.LENGTH_SHORT).show();
                    } else if (editTextLastName.getText().toString().trim().length() < 2) {
                        Toasty.warning(CollaboratorAdd.this, getString(R.string.lastname_minimum_char), Toast.LENGTH_SHORT).show();
                    } else if (editTextLastName.getText().toString().trim().length() > 30) {
                        Toasty.warning(CollaboratorAdd.this, getString(R.string.lastname_maximum_char), Toast.LENGTH_SHORT).show();
                    } else {
                        email = editTextEmail.getText().toString();
                        firstName = editTextFirstName.getText().toString();
                        lastName = editTextLastName.getText().toString();

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CollaboratorAdd.this);

                        // Setting Dialog Title
                        alertDialog.setTitle(getString(R.string.creatinguser));

                        // Setting Dialog Message
                        alertDialog.setMessage(getString(R.string.createConfirmation));

                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.mipmap.ic_launcher);
                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to invoke YES event
                                //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                                if (InternetReceiver.isConnected()) {
                                    progressDialog.setMessage(getString(R.string.creatinguser));
                                    progressDialog.show();
                                    new RegisterUserNew(email, firstName, lastName).execute();
                                }
                            }
                        });

                        // Setting Negative "NO" Button
                        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        alertDialog.show();


                    }
                    break;
                case R.id.buttonClose:
                    dismiss();
                    break;
                default:
                    break;
            }
            dismiss();
        }
    }

    private class RegisterUserNew extends AsyncTask<String, Void, String> {
        String firstname, email, lastname;

        RegisterUserNew(String email, String firstname, String lastname) {

            this.firstname = firstname;
            this.email = email;
            this.lastname = lastname;

        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().postCreateUser(email, firstname, lastname);
        }

        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            progressDialog.setMessage(getString(R.string.adding_collaborator));
            progressDialog.show();
            if (result == null) {
                Toasty.error(CollaboratorAdd.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }
            try {
                String state = Prefs.getString("400", null);

                if (state.equals("badRequest")) {
                    progressDialog.dismiss();
                    Toasty.info(CollaboratorAdd.this, "The user is already registered", Toast.LENGTH_LONG).show();
                    return;
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String message = jsonObject.getString("message");
                    JSONObject jsonObject2 = jsonObject.getJSONObject("user");
                    email = jsonObject2.getString("email");
                    int id = jsonObject2.getInt("id");
                    Log.d("idNew", id + "");

                    if (message.contains("Activate your account! Click on the link that we've sent to your mail")) {
                        new collaboratorAdduser(ticketID, String.valueOf(id)).execute();
                    }

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
                String role = jsonObject1.getString("role");
                if (role.contains("ccc")) {
                    Toasty.success(CollaboratorAdd.this, getString(R.string.collaboratoraddedsuccesfully), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CollaboratorAdd.this, TicketReplyActivity.class);
                    intent.putExtra("ticket_id", ticketID);
                    startActivity(intent);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
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
            progressBar.setVisibility(View.GONE);

            if (isCancelled()) return;
            stringArrayList.clear();

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("users");
                if (jsonArray.length() == 0) {
                    //searchUer.setVisibility(View.GONE);
                    Prefs.putString("noUser", "null");
                } else {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String email = jsonObject1.getString("email");
                        id = Integer.parseInt(jsonObject1.getString("id"));
                        String first_name = jsonObject1.getString("first_name");
                        String last_name = jsonObject1.getString("last_name");
                        String profilePic=jsonObject1.getString("profile_pic");
                        CollaboratorSuggestion collaboratorSuggestion=new CollaboratorSuggestion(id,first_name,last_name,email,profilePic);
                        stringArrayList.add(collaboratorSuggestion);
                        Prefs.putString("noUser", "1");
                    }

                    autoCompleteTextViewUser.setThreshold(3);
                    autoCompleteTextViewUser.setDropDownWidth(1500);
                    autoCompleteTextViewUser.setAdapter(arrayAdapterCC);
                    autoCompleteTextViewUser.showDropDown();


                }
                } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }


        }
    }

//    private class collaboratorAdduserExisting extends AsyncTask<String, Void, String> {
//        String ticketId, userId;
//
//        public collaboratorAdduserExisting(String ticketId, String userId) {
//            this.ticketId = ticketId;
//            this.userId = userId;
//        }
//
//        protected String doInBackground(String... urls) {
//            return new Helpdesk().createCollaborator(Prefs.getString("ticketId", null), String.valueOf(userId));
//        }
//
//        protected void onPostExecute(String result) {
//            if (isCancelled()) return;
//            progressDialog.dismiss();
//
//            try {
//                JSONObject jsonObject = new JSONObject(result);
//                JSONObject jsonObject1 = jsonObject.getJSONObject("collaborator");
//                String role = jsonObject1.getString("role");
//                if (role.contains("ccc")) {
//                    autoCompleteTextViewUser.setText("");
//                    id = 0;
//                    id1=0;
//                    Toasty.success(CollaboratorAdd.this, getString(R.string.collaboratoraddedsuccesfully), Toast.LENGTH_SHORT).show();
//                    Intent intent=new Intent(CollaboratorAdd.this,CollaboratorAdd.class);
//                    intent.putExtra("ticket_id", ticketID);
//                    startActivity(intent);
//                    finish();
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//
//        }
//    }

    private class collaboratorRemoveUser extends AsyncTask<String, Void, String> {
        String ticketId, userId;

        public collaboratorRemoveUser(String ticketId, String userId) {
            this.ticketId = ticketId;
            this.userId = userId;
        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().removeCollaborator(Prefs.getString("ticketId", null),email2);
        }

        protected void onPostExecute(String result) {
            if (isCancelled()) return;
            try {
                JSONObject jsonObject = new JSONObject(result);
                String collaborator=jsonObject.getString("collaborator");
                if (collaborator.equals("deleted successfully")){
                    email2="";
                    Toasty.success(CollaboratorAdd.this, getString(R.string.collaboratorRemove), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(CollaboratorAdd.this,CollaboratorAdd.class);
                    intent.putExtra("ticket_id", ticketID);
                    startActivity(intent);
                    finish();

                }

            } catch (JSONException |NullPointerException e) {
                e.printStackTrace();
            }

            if (result == null) {
                Toasty.error(CollaboratorAdd.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }


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
            try{
                progressDialog.dismiss();
            }catch (NullPointerException e){
                e.printStackTrace();
            }
                movieList.clear();
            if (isCancelled()) return;

            if (result == null) {
                Toasty.error(CollaboratorAdd.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("collaborator");
                if (jsonArray.length()==0){
                    progressBar.setVisibility(View.GONE);
                    return;
                }else {
                    progressBar.setVisibility(View.GONE);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String email = jsonObject1.getString("email");
                        String image=jsonObject1.getString("avatar");
                        String name=jsonObject1.getString("first_name");
                        String last=jsonObject1.getString("last_name");
                        String fullName;
                        if (name.equals("")&&last.equals("")){
                            fullName="";
                        }
                        else if (name.equals("")&&!last.equals("")){
                            fullName=last;
                        }
                        else if (last.equals("")&&!name.equals("")){
                            fullName=name;
                        }
                        else{
                            fullName=name+" "+last;
                        }
                            AttachedCollaborator attachedCollaborator=new AttachedCollaborator(email,image,fullName);
                            movieList.add(attachedCollaborator);


                    }

                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    mAdapter = new Collaboratoradapter(CollaboratorAdd.this,movieList);
                    runLayoutAnimation(recyclerView);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_right);

        recyclerView.setLayoutAnimation(controller);
        mAdapter.notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
    final TextWatcher passwordWatcheredittextSubject = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            term = autoCompleteTextViewUser.getText().toString();
            if (InternetReceiver.isConnected()) {
                    String newTerm=term;
                    Log.d("newTerm", newTerm);
                    arrayAdapterCC=new CollaboratorAdapter(CollaboratorAdd.this,stringArrayList);
                    progressBar.setVisibility(View.VISIBLE);
                    //arrayAdapterCC = new ArrayAdapter<>(CollaboratorAdd.this, android.R.layout.simple_dropdown_item_1line, stringArrayList);
                    new FetchCollaborator(newTerm.trim()).execute();

            }
        }

        public void afterTextChanged(Editable s) {
            if (term.equals("")){
                searchUer.setVisibility(View.GONE);
            }
            else {
                if (term.equalsIgnoreCase(email1)) {
                    searchUer.setVisibility(View.VISIBLE);
                } else {
                    searchUer.setVisibility(View.GONE);
                }
            }
        }
    };
    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(CollaboratorAdd.this, TicketDetailActivity.class);
//        startActivity(intent);
        finish();


//        try {
//            if (Prefs.getString("cameFromTicket", null).equals("true")) {
//                Intent intent = new Intent(CollaboratorAdd.this, TicketDetailActivity.class);
//                Prefs.putString("cameFromTicket","false");
//                intent.putExtra("ticket_id", ticketID);
//                startActivity(intent);
//                finish();
//            } else {
//                Intent intent = new Intent(CollaboratorAdd.this, TicketReplyActivity.class);
//                intent.putExtra("ticket_id", ticketID);
//                startActivity(intent);
//                finish();
//            }
//        }catch (NullPointerException e){
//            e.printStackTrace();
//        }
    }
    public class Collaboratoradapter extends RecyclerView.Adapter<Collaboratoradapter.MyViewHolder> {

        private List<AttachedCollaborator> moviesList;
        Context context;
        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView email;
            public TextView textViewName;
            public ImageView imageViewCollaborator;
            public ImageView deletecolla;
            public RelativeLayout relativeLayout;

            public MyViewHolder(View view) {
                super(view);
                email = (TextView) view.findViewById(R.id.textView_client_email);
                imageViewCollaborator= (ImageView) view.findViewById(R.id.imageView_collaborator);
                relativeLayout= (RelativeLayout) view.findViewById(R.id.attachedCollaborator);
                textViewName= (TextView) view.findViewById(R.id.collaboratorname);
                deletecolla= (ImageView) view.findViewById(R.id.deleteCollaborator);

            }
        }
        public Collaboratoradapter(Context context,List<AttachedCollaborator> moviesList) {
            this.moviesList = moviesList;
            this.context=context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.listview_item_row, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final AttachedCollaborator movie = moviesList.get(position);
            holder.relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        email2=movie.getEmail();
                        return false;
                    }
                });

            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            holder.deletecolla.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    email2=movie.getEmail();
                    try {

                        if (email2.equals("")){
                            Toasty.info(CollaboratorAdd.this,getString(R.string.userEmpty),Toast.LENGTH_SHORT).show();

                        }
                        else {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(CollaboratorAdd.this);
                            alertDialog.setMessage(R.string.user_collaborator);
                            alertDialog.setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            alertDialog.setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    progressDialog=new ProgressDialog(CollaboratorAdd.this);
                                    progressDialog.setMessage(getString(R.string.pleasewait));
                                    progressDialog.show();
                                    Log.d("email3",email2);
                                    new collaboratorRemoveUser(Prefs.getString("ticketId", null), email2).execute();
                                    // DO SOMETHING HERE

                                }
                            });

                            AlertDialog dialog = alertDialog.create();
                            dialog.show();


                        }
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                }
            });


            if (!movie.getEmail().equals("")) {
                holder.email.setText(movie.getEmail());
            }

            if (movie.getName().equals("")){
                holder.textViewName.setVisibility(View.GONE);
            }
            else{
                holder.textViewName.setVisibility(View.VISIBLE);
                holder.textViewName.setText(movie.getName());
            }

            if (!movie.getPicture().equals("")){
                if (movie.getPicture().contains(".jpg")||movie.getPicture().contains(".jpeg")||movie.getPicture().contains(".png")) {
                    Log.d("picture",movie.getPicture()) ;
                    Picasso.with(CollaboratorAdd.this).load(movie.getPicture()).placeholder(R.drawable.default_pic).transform(new CircleTransform()).into(holder.imageViewCollaborator);
                }
                else{
                    Log.d("cameInThisBlock","true");
                    Picasso.with(CollaboratorAdd.this).load(movie.getPicture()).placeholder(R.drawable.default_pic).transform(new CircleTransform()).into(holder.imageViewCollaborator);
                }

            }

        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }
    }



}