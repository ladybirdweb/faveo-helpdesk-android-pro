package co.helpdesk.faveo.pro.frontend.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
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
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
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

import java.util.ArrayList;
import java.util.List;

import co.helpdesk.faveo.pro.CircleTransform;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.pro.frontend.adapters.CollaboratorAdapter;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.pro.model.AttachedCollaborator;
import co.helpdesk.faveo.pro.model.CollaboratorSuggestion;
import es.dmoral.toasty.Toasty;

public class collaboratorAdd extends AppCompatActivity {

    AutoCompleteTextView autoCompleteTextViewUser;
    Button searchUer, deleteUser;
    ArrayList<CollaboratorSuggestion> stringArrayList;
    CollaboratorAdapter arrayAdapterCC;
    RelativeLayout relativeLayout;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_collaborator_add);
        Window window = collaboratorAdd.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(collaboratorAdd.this,R.color.faveo));
        //recipients= (Spinner) findViewById(R.id.spinnerRecipients);
        //relativeLayout= (RelativeLayout) findViewById(R.id.recipients);
        strings = new ArrayList<>();
        recyclerView= (RecyclerView) findViewById(R.id.list);
        strings.add("Show");

        isShowing=true;
        progressBar= (ProgressBar) findViewById(R.id.collaboratorProgressBarReply);
        progressDialog=new ProgressDialog(collaboratorAdd.this);
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
        arrayAdapterCC=new CollaboratorAdapter(collaboratorAdd.this,stringArrayList);
        //arrayAdapterCC = new ArrayAdapter<Data>(collaboratorAdd.this, android.R.layout.simple_dropdown_item_1line, stringArrayList);

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
//                        editTextEmail.setText(email1);
//                        firstname=data2.getFirst_name();
//                        lastname=data2.getLast_name();
//                        editTextEmail.setText(email1);
//                        editTextFirstName.setText(firstname);
//                        editTextLastName.setText(lastname);
                    }
                }
//                try{
//                    Data data = stringArrayList.get(i);
//                    id1 = data.getID();
//                    email = data.getName();
//                    Log.d("idoftheuser",id1+"");
//                    Log.d("ccobject",stringArrayList.get(i).toString());
//                }catch (IndexOutOfBoundsException e){
//                    e.printStackTrace();
//                }
            }


        });
        spinnerPriArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, strings); //selected item will look like a spinner set from XML
        spinnerPriArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //recipients.setAdapter(spinnerPriArrayAdapter);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (Prefs.getString("cameFromTicket", null).equals("true")) {
                        Intent intent = new Intent(collaboratorAdd.this, TicketDetailActivity.class);
                        Prefs.putString("cameFromTicket","false");
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(collaboratorAdd.this, TicketReplyActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        });

//        recipients.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                if (i==0){
//                    deleteUser.setVisibility(View.GONE);
//                    email2=null;
//                }
//                else{
//                    deleteUser.setVisibility(View.VISIBLE);
//                    email2=recipients.getSelectedItem().toString();
//                    //Toast.makeText(collaboratorAdd.this, "email is:"+email2, Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//                email2=null;
//            }
//        });
        searchUer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email=autoCompleteTextViewUser.getText().toString();

                if (!email1.equals(email)&&autoCompleteTextViewUser.getText().toString().equals("")) {
                    Toasty.info(collaboratorAdd.this, getString(R.string.collaboratorEmpty), Toast.LENGTH_SHORT).show();
                    id1=0;
                }
                else if (id1==0){
                    Toasty.info(collaboratorAdd.this, getString(R.string.collaboratorNotFound), Toast.LENGTH_SHORT).show();
                }
                else{
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(collaboratorAdd.this);
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
                                Toasty.error(collaboratorAdd.this, getString(R.string.collaboratorNotFound), Toast.LENGTH_SHORT).show();
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

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent=new Intent(collaboratorAdd.this,collaboratorcreate.class);
                    startActivity(intent);
                    finish();
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
                //int pos=recipients.getSelectedItemPosition();
                try {

                    if (email2.equals("")){
                        Toasty.info(collaboratorAdd.this,getString(R.string.userEmpty),Toast.LENGTH_SHORT).show();

                    }
                    else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(collaboratorAdd.this);
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
                                progressDialog=new ProgressDialog(collaboratorAdd.this);
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


                //Toast.makeText(collaboratorAdd.this,getString(R.string.userEmpty), Toast.LENGTH_SHORT).show();
            }
        });
////

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
                        //Toast.makeText(TicketSaveActivity.this, "email:"+email, Toast.LENGTH_SHORT).show();
                        CollaboratorSuggestion collaboratorSuggestion=new CollaboratorSuggestion(id,first_name,last_name,email,profilePic);
                        //Data data = new Data(id, first_name + " " + last_name + " <" + email + ">");
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

    private class collaboratorAdduser extends AsyncTask<String, Void, String> {
        String ticketId, userId;

        public collaboratorAdduser(String ticketId, String userId) {
            this.ticketId = ticketId;
            this.userId = userId;
        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().createCollaborator(Prefs.getString("ticketId", null), String.valueOf(userId));
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
                    id1=0;
                    Toasty.success(collaboratorAdd.this, getString(R.string.collaboratoraddedsuccesfully), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(collaboratorAdd.this,TicketReplyActivity.class);
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
            //progressDialog.dismiss();
            if (isCancelled()) return;
            try {
                JSONObject jsonObject = new JSONObject(result);
                String collaborator=jsonObject.getString("collaborator");
                if (collaborator.equals("deleted successfully")){
                    email2="";
                    Toasty.success(collaboratorAdd.this, getString(R.string.collaboratorRemove), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(collaboratorAdd.this,TicketReplyActivity.class);
                    startActivity(intent);
                    //finish();
                }

            } catch (JSONException |NullPointerException e) {
                e.printStackTrace();
            }

            if (result == null) {
                Toasty.error(collaboratorAdd.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
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
                    progressBar.setVisibility(View.GONE);
                    //recipients.setVisibility(View.GONE);
                    return;
                }else {
                    progressBar.setVisibility(View.GONE);
                    //relativeLayout.setVisibility(View.VISIBLE);
                    //recipients.setVisibility(View.VISIBLE);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        noOfCollaborator++;
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

                        //int id= Integer.parseInt(jsonObject1.getString("id"));
                        //Toast.makeText(TicketSaveActivity.this, "email:"+email, Toast.LENGTH_SHORT).show();

                        //stringArrayList.add(data);

                        //strings.add(email);

                    }

                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    mAdapter = new Collaboratoradapter(collaboratorAdd.this,movieList);
                    runLayoutAnimation(recyclerView);
                    recyclerView.setAdapter(mAdapter);
                    //recyclerView.getAdapter().notifyDataSetChanged();
                    mAdapter.notifyDataSetChanged();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


//                if (jsonObject1.getString("last_message").equals("null")) {
//                    editTextLastMessage.setText("Not available");
//                } else
//                    editTextLastMessage.setText(jsonObject1.getString("last_message"));


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

            //Toast.makeText(TicketSaveActivity.this, "API called", Toast.LENGTH_SHORT).show();
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            term = autoCompleteTextViewUser.getText().toString();
            if (InternetReceiver.isConnected()) {
                if (!term.equals("")&&term.length()==3) {
                    //searchUer.setVisibility(View.GONE);
                    //int pos = term.lastIndexOf(",");
                    //term = term.substring(pos + 1, term.length());
                    String newTerm=term;
                    Log.d("newTerm", newTerm);
                    arrayAdapterCC=new CollaboratorAdapter(collaboratorAdd.this,stringArrayList);
                    progressBar.setVisibility(View.VISIBLE);
                    //arrayAdapterCC = new ArrayAdapter<>(collaboratorAdd.this, android.R.layout.simple_dropdown_item_1line, stringArrayList);
                    new FetchCollaborator(newTerm.trim()).execute();

                }
//            Toast.makeText(collaboratorAdd.this, "term:"+term, Toast.LENGTH_SHORT).show();
                else {
                    //arrayAdapterCC=new CollaboratorAdapter(collaboratorAdd.this,stringArrayList);
                    //arrayAdapterCC = new ArrayAdapter<>(collaboratorAdd.this, android.R.layout.simple_dropdown_item_1line, stringArrayList);
                    //new FetchCollaborator("s").execute();
                    //Data data = new Data(0, "No result found");
                    //stringArrayList.add(data);
//                autoCompleteTextViewCC.setAdapter(stringArrayAdapterCC);
//                stringArrayAdapterCC.notifyDataSetChanged();
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
        try {
            if (Prefs.getString("cameFromTicket", null).equals("true")) {
                Intent intent = new Intent(collaboratorAdd.this, TicketDetailActivity.class);
                Prefs.putString("cameFromTicket","false");
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(collaboratorAdd.this, TicketReplyActivity.class);
                startActivity(intent);
                finish();
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
//        if (!TicketDetailActivity.isShowing) {
//            Log.d("isShowing", "false");
//            Intent intent = new Intent(this, TicketDetailActivity.class);
//            startActivity(intent);
//        } else Log.d("isShowing", "true");
//        super.onBackPressed();

//        if (fabExpanded)
//            exitReveal();
//        else super.onBackPressed();
    }
    public class Collaboratoradapter extends RecyclerView.Adapter<Collaboratoradapter.MyViewHolder> {

        private List<co.helpdesk.faveo.pro.model.AttachedCollaborator> moviesList;
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
        public Collaboratoradapter(Context context,List<co.helpdesk.faveo.pro.model.AttachedCollaborator> moviesList) {
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
            final co.helpdesk.faveo.pro.model.AttachedCollaborator movie = moviesList.get(position);
            holder.relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        email2=movie.getEmail();
                        return false;
                    }
                });

            holder.deletecolla.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    email2=movie.getEmail();
                    try {

                        if (email2.equals("")){
                            Toasty.info(collaboratorAdd.this,getString(R.string.userEmpty),Toast.LENGTH_SHORT).show();

                        }
                        else {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(collaboratorAdd.this);
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
                                    progressDialog=new ProgressDialog(collaboratorAdd.this);
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

//                holder.deletecolla.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                        Log.d("checked",""+b);
//                        if (b){
//                            holder.deletecolla.setVisibility(View.VISIBLE);
//                            holder.relativeLayout.setBackgroundColor(getResources().getColor(R.color.grey_200));
//                            holder.deletecolla.setChecked(true);
//                            deleteUser.setVisibility(View.VISIBLE);
//                        }
//                        else{
//                            holder.deletecolla.setChecked(false);
//                            holder.deletecolla.setVisibility(View.GONE);
//                            holder.relativeLayout.setBackgroundColor(getResources().getColor(R.color.collaborator));
//                            deleteUser.setVisibility(View.GONE);
//                        }
//                    }
//                });



//                holder.deletecolla.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                        if (b){
//                           holder.deletecolla.setChecked(false);
//                           holder.deletecolla.setVisibility(View.GONE);
//                           deleteUser.setVisibility(View.GONE);
//                        }
//                        else{
//                            holder.deletecolla.setChecked(true);
//                            deleteUser.setVisibility(View.VISIBLE);
//                        }
//                    }
//                });

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
                    Picasso.with(collaboratorAdd.this).load(movie.getPicture()).placeholder(R.drawable.default_pic).transform(new CircleTransform()).into(holder.imageViewCollaborator);
                }
                else{
                    Log.d("cameInThisBlock","true");
                    Picasso.with(collaboratorAdd.this).load(movie.getPicture()).placeholder(R.drawable.default_pic).transform(new CircleTransform()).into(holder.imageViewCollaborator);
                }

            }

        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }
    }



}