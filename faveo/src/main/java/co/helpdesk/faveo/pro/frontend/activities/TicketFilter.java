package co.helpdesk.faveo.pro.frontend.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;

import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.pro.frontend.fragments.tickets.InboxTickets;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;

public class TicketFilter extends AppCompatActivity implements InboxTickets.OnFragmentInteractionListener{
    MultiAutoCompleteTextView autoDepartment,autoSource,autoPriority,autoType;
    Toolbar toolbar;
    AutoCompleteTextView autoCompleteAssigned;
    //    Spinner autoCompleteShow;
    MultiAutoCompleteTextView autoAssigned;
    ImageView imageViewback,buttonFilter,clearAll;
    String show,departmment,type,priority,assignedto,source,status;
    int assigned,index;
    String unassigned,department1,show1,source1,priority1,tickettype1,assignto1,assigned1;
    String assignedtofinal,typefinal,priorityfinal,sourcefinal,statusFinal;
    ArrayList<String> staffItems,departmentItems,priorityItems,typeItems,sourceItems,showItems,unassignedItems,statusItems;
    ArrayAdapter<String> staffArrayAdapter,departmentArrayAdapter,priorityAdapter,typeArrayAdapter,sourceArrayAdapter,showArrayAdapter,unassignedArrayAdapter,statusArrayAdapter;
    ProgressDialog progressDialog;
    String result;
    ArrayList<String> getStaffItems;
    ImageView refresh;
    Animation rotation;
    public static String
            keyDepartment = "", valueDepartment = "",
            keySLA = "", valueSLA = "",
            keyStatus = "", valueStatus = "",
            keyStaff = "", valueStaff = "",
            keyName="",
            keyPriority = "", valuePriority = "",
            keyTopic = "", valueTopic = "",
            keySource = "", valueSource = "",
            keyType = "", valueType = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_filter);
        Window window = TicketFilter.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(TicketFilter.this,R.color.mainActivityTopBar));
        rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        show=Prefs.getString("Show",null);
        Log.d("Show",show);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        buttonFilter = (ImageView) findViewById(R.id.buttonfilter);
        setSupportActionBar(toolbar);
        //autoStatus= (MultiAutoCompleteTextView) findViewById(R.id.autocompletestatus);
        autoCompleteAssigned = (AutoCompleteTextView) findViewById(R.id.autocompleteassigned);
        //autoCompleteShow = (Spinner) findViewById(R.id.autocompleteshow);
        refresh= (ImageView) findViewById(R.id.buttonRefresh);
        autoDepartment = (MultiAutoCompleteTextView) findViewById(R.id.autocompletedepartment);
        autoPriority = (MultiAutoCompleteTextView) findViewById(R.id.autocompletetepriority);
        autoSource = (MultiAutoCompleteTextView) findViewById(R.id.autocompletetesource);
        autoType = (MultiAutoCompleteTextView) findViewById(R.id.autocompletetype);
        autoAssigned = (MultiAutoCompleteTextView) findViewById(R.id.autocompleteassign);
        clearAll = (ImageView) findViewById(R.id.buttonclearall);
        imageViewback = (ImageView) findViewById(R.id.imageViewBack);
        progressDialog = new ProgressDialog(TicketFilter.this);
        getStaffItems = new ArrayList<>();
        //autoDepartment.clearFocus();
        InputMethodManager imm = (InputMethodManager) TicketFilter.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = TicketFilter.this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(TicketFilter.this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        if (InternetReceiver.isConnected()){
            new FetchDependency().execute();
        }

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(TicketFilter.this);

                // Setting Dialog Title
                alertDialog.setTitle(getString(R.string.refreshingPage));

                // Setting Dialog Message
                alertDialog.setMessage(getString(R.string.refreshPage));

                // Setting Icon to Dialog
                alertDialog.setIcon(R.mipmap.ic_launcher);

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke YES event
                        //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                        if (InternetReceiver.isConnected()){
                            refresh.startAnimation(rotation);
                            new FetchDependency().execute();
                            setUpViews();
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
        });


        try {
            show1 = Prefs.getString("show", null);
            source1 = Prefs.getString("sourcefinal", null);
            if (!source1.equals("null")) {
                autoSource.setText(source1);
            }
            else{
                autoSource.setText("");
            }
            department1 = Prefs.getString("departmentfinal", null);
            if (!department1.equals("all")) {
                autoDepartment.setText(department1);
            }
            else if (department1.equals("all")){
                autoDepartment.setText("");
            }
            else{
                autoDepartment.setText("");
            }
            priority1 = Prefs.getString("priorityfinal", null);
            if (!priority1.equals("null")) {
                autoPriority.setText(priority1);
            }
            else {
                autoPriority.setText("");
            }
            tickettype1 = Prefs.getString("typefinal", null);
            if (!tickettype1.equals("null")) {
                autoType.setText(tickettype1);
            }
            else{
                autoType.setText("");
            }
            assignto1 = Prefs.getString("assignees", null);
            if (!assignto1.equals("null")) {
                autoAssigned.setText(assignto1);
            }
            else{
                autoAssigned.setText("");
            }
            assigned1 = Prefs.getString("assigned", null);
            if (!assigned1.equals("null")) {
                autoCompleteAssigned.setText(assigned1);
            }
            else if (assigned1.equals("null")){
                autoCompleteAssigned.setText("");
            }


        }catch(NullPointerException e){
            e.printStackTrace();
        }
        autoDepartment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                departmment = departmentArrayAdapter.getItem(i).toString();
            }
        });


        autoSource.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                source = sourceArrayAdapter.getItem(i).toString();
            }
        });
        autoPriority.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                priority = priorityAdapter.getItem(i).toString();
            }
        });
        autoAssigned.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                assignedto = "a-" + staffArrayAdapter.getItem(i).toString();

//                assignedtonames.add(assignedto);
//                Toast.makeText(TicketFilter.this, "names:"+assignedtonames.toString(), Toast.LENGTH_SHORT).show();

            }
        });

        autoType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                type = typeArrayAdapter.getItem(i).toString();
            }
        });

        autoCompleteAssigned.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                unassigned = unassignedArrayAdapter.getItem(i).toString();
                if (unassigned.equals("yes")) {
                    assigned = 1;

                } else if (unassigned.equals("no")) {
                    assigned = 0;
                }
            }
        });


        imageViewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!autoDepartment.getText().toString().equals("")) {
                        departmment = autoDepartment.getText().toString();
                        result = departmment.replaceAll("\\s+,$", "");
                        result = result.replaceAll(" ", "");
                        Prefs.putString("departmentfinal", result);

                    } else if (autoDepartment.getText().toString().equals("")) {
                        Prefs.putString("departmentfinal", "all");
                    }
//                    else if (autoDepartment.getText().toString().equals("")){
//
//                    }
                } catch (StringIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

                try {
                    Prefs.putString("show", show);
                } catch (StringIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

                try {
                    if (!autoAssigned.getText().toString().equals("")) {
                        Prefs.putString("assignees",autoAssigned.getText().toString());
                        assignedto = autoAssigned.getText().toString();
                        assignedtofinal = assignedto.replaceAll("\\s+,$", "");
                        assignedtofinal = assignedto.replaceAll(" ", "");
                        //assignees=assignedtofinal;
                        getStaffItems.clear();
                        ArrayList aList = new ArrayList(Arrays.asList(assignedtofinal.split(",")));
                        for (int i = 0; i < aList.size(); i++) {
                            //Toast.makeText(TicketFilter.this, "staffs:"+"a-"+aList.get(i), Toast.LENGTH_SHORT).show();
                            getStaffItems.add("a-" + aList.get(i));
                        }
                        StringBuilder sb = new StringBuilder();
                        for (String n : getStaffItems) {
                            if (sb.length() > 0) sb.append(',');
                            sb.append(n);
                        }
                        //Toast.makeText(TicketFilter.this, sb.toString(), Toast.LENGTH_SHORT).show();
                        Prefs.putString("assignedtofinal", sb.toString());

                    } else if (autoAssigned.getText().toString().equals("")) {
                        Prefs.putString("assignees","null");
                        Prefs.putString("assignedtofinal", "null");
                    }
                } catch (StringIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

                try {
                    if (!autoPriority.getText().toString().equals("")) {
                        priority = autoPriority.getText().toString();
                        priorityfinal = priority.replaceAll("\\s+,$", "");
                        priorityfinal = priority.replaceAll(" ", "");
                        Prefs.putString("priorityfinal", priorityfinal);
                    } else if (autoPriority.getText().toString().equals("")) {
                        Prefs.putString("priorityfinal", "null");
                    }
                } catch (StringIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                try {
                    if (!autoSource.getText().toString().equals("")) {
                        source = autoSource.getText().toString();
                        sourcefinal = source.replaceAll("\\s+,$", "");
                        sourcefinal = source.replaceAll(" ", "");
                        Prefs.putString("sourcefinal", sourcefinal);
                    } else if (autoSource.getText().toString().equals("")) {
                        Prefs.putString("sourcefinal", "null");
                    }
                } catch (StringIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

                try {
                    if (!autoType.getText().toString().equals("")) {
                        type = autoType.getText().toString();
                        typefinal = type.replaceAll("\\s+,$", "");
                        typefinal = typefinal.replaceAll(" ", "");
                        Prefs.putString("typefinal", typefinal);
                    } else if (autoType.getText().toString().equals("")) {
                        Prefs.putString("typefinal", "null");
                    }
                } catch (StringIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                try {
                    if (!autoCompleteAssigned.getText().toString().equals("")) {
                        if (autoCompleteAssigned.getText().toString().equals("Yes")) {
                            Prefs.putString("assigned", "Yes");
                        } else if (autoCompleteAssigned.getText().toString().equals("No")) {
                            Prefs.putString("assigned", "No");
                        }
                    } else if (autoCompleteAssigned.getText().toString().equals("")) {
                        Prefs.putString("assigned", "null");
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                Prefs.putString("source","6");
                Prefs.putString("came from filter", "true");

                Intent intent = new Intent(TicketFilter.this, MainActivity.class);
                startActivity(intent);


            }
        });
        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoType.setText("");
                autoDepartment.setText("");
                autoPriority.setText("");
                autoSource.setText("");
                autoAssigned.setText("");
                autoCompleteAssigned.setText("");
                Prefs.putString("departmentfinal", "all");
                Prefs.putString("show", "null");
                Prefs.putString("assigned", "null");
                Prefs.putString("assignees", "null");
                Prefs.putString("sourcefinal", "null");
                Prefs.putString("typefinal", "null");
                Prefs.putString("priorityfinal", "null");
            }
        });

        Prefs.getString("keyStaff", null);
        setUpViews();
        }
    public void setUpViews() {
        JSONObject jsonObject;
        String json = Prefs.getString("DEPENDENCY", "");
        try {
            staffItems = new ArrayList<>();
            jsonObject = new JSONObject(json);
            JSONArray jsonArrayStaffs = jsonObject.getJSONArray("staffs");
            for (int i = 0; i < jsonArrayStaffs.length(); i++) {
                String agentName=jsonArrayStaffs.getJSONObject(i).getString("user_name");
                String newAgentName=agentName.replace(agentName.charAt(0),agentName.toUpperCase().charAt(0));
                staffItems.add(newAgentName);
            }
            departmentItems = new ArrayList<>();
            jsonObject = new JSONObject(json);
            JSONArray jsonArrayHelpTopics = jsonObject.getJSONArray("departments");
            for (int i = 0; i < jsonArrayHelpTopics.length(); i++) {
                departmentItems.add(jsonArrayHelpTopics.getJSONObject(i).getString("name"));

            }

            statusItems=new ArrayList<>();
            jsonObject = new JSONObject(json);
            JSONArray jsonArrayStatus = jsonObject.getJSONArray("status");
            for (int i = 0; i < jsonArrayStatus.length(); i++) {
                statusItems.add(jsonArrayStatus.getJSONObject(i).getString("name"));

            }

            showItems = new ArrayList<>();
            showItems.add("Please select a show to filter tickets");
            showItems.add("Inbox");
            showItems.add("Trash");
            showItems.add("Mytickets");
            showItems.add("Closed");

            unassignedItems = new ArrayList<>();
            unassignedItems.add("Yes");
            unassignedItems.add("No");

            priorityItems = new ArrayList<>();
            jsonObject = new JSONObject(json);
            JSONArray jsonArrayPriorities = jsonObject.getJSONArray("priorities");

            for (int i = 0; i < jsonArrayPriorities.length(); i++) {
                priorityItems.add(jsonArrayPriorities.getJSONObject(i).getString("priority"));
            }


            typeItems = new ArrayList<>();
            jsonObject = new JSONObject(json);
            JSONArray jsonArrayType = jsonObject.getJSONArray("type");
            for (int i = 0; i < jsonArrayType.length(); i++) {
                typeItems.add(jsonArrayType.getJSONObject(i).getString("name"));

            }
            sourceItems = new ArrayList<>();
            jsonObject = new JSONObject(json);
            JSONArray jsonArraySources = jsonObject.getJSONArray("sources");
            for (int i = 0; i < jsonArraySources.length(); i++) {
                String sourceName=jsonArraySources.getJSONObject(i).getString("name");
                String newSourceName=sourceName.replace(sourceName.charAt(0),sourceName.toUpperCase().charAt(0));
                sourceItems.add(newSourceName);
            }
            } catch (JSONException e) {
            e.printStackTrace();
        }
        unassignedArrayAdapter=new ArrayAdapter<String>(TicketFilter.this,android.R.layout.simple_dropdown_item_1line,unassignedItems);
        autoCompleteAssigned.setAdapter(unassignedArrayAdapter);
        autoCompleteAssigned.setThreshold(1);
        autoCompleteAssigned.setDropDownWidth(1000);

        staffArrayAdapter=new ArrayAdapter<>(TicketFilter.this,android.R.layout.simple_dropdown_item_1line,staffItems);
        //staffArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoAssigned.setAdapter(staffArrayAdapter);
        autoAssigned.setThreshold(1);
        autoAssigned.setDropDownWidth(1000);
        autoAssigned.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        departmentArrayAdapter=new ArrayAdapter<>(TicketFilter.this,android.R.layout.simple_dropdown_item_1line,departmentItems);
        //staffArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoDepartment.setAdapter(departmentArrayAdapter);
        autoDepartment.setThreshold(1);
        autoDepartment.setDropDownWidth(1000);
        autoDepartment.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        sourceArrayAdapter=new ArrayAdapter<>(TicketFilter.this,android.R.layout.simple_dropdown_item_1line,sourceItems);
        //staffArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoSource.setAdapter(sourceArrayAdapter);
        autoSource.setThreshold(1);
        autoSource.setDropDownWidth(1000);
        autoSource.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        typeArrayAdapter=new ArrayAdapter<>(TicketFilter.this,android.R.layout.simple_dropdown_item_1line,typeItems);
        //staffArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoType.setAdapter(typeArrayAdapter);
        autoType.setThreshold(1);
        autoType.setDropDownWidth(1000);
        autoType.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());


        priorityAdapter=new ArrayAdapter<>(TicketFilter.this,android.R.layout.simple_dropdown_item_1line,priorityItems);
        //staffArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoPriority.setAdapter(priorityAdapter);
        autoPriority.setThreshold(1);
        autoPriority.setDropDownWidth(1000);
        autoPriority.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

    }
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent  intent=new Intent(TicketFilter.this,MainActivity.class);
//        startActivity(intent);
//    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    @Override
    public void onBackPressed() {
        finish();
//        if (!MainActivity.isShowing) {
//            Log.d("isShowing", "false");
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//        } else Log.d("isShowing", "true");
//
//
//        super.onBackPressed();
    }

    private class FetchDependency extends AsyncTask<String, Void, String> {
        String unauthorized;

        protected String doInBackground(String... urls) {

            return new Helpdesk().getDependency();

        }

        protected void onPostExecute(String result) {
            refresh.clearAnimation();
            Log.d("Depen Response : ", result + "");
            Log.d("cameHere","True");

            try{
                progressDialog.dismiss();
            }catch (NullPointerException e){
                e.printStackTrace();
            }

            if (result==null) {

            }


            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                Prefs.putString("DEPENDENCY", jsonObject1.toString());
                // Preference.setDependencyObject(jsonObject1, "dependency");
                JSONArray jsonArrayDepartments = jsonObject1.getJSONArray("departments");
                for (int i = 0; i < jsonArrayDepartments.length(); i++) {
                    keyDepartment += jsonArrayDepartments.getJSONObject(i).getString("id") + ",";
                    valueDepartment += jsonArrayDepartments.getJSONObject(i).getString("name") + ",";
                }
                Prefs.putString("keyDept", keyDepartment);
                Prefs.putString("valueDept", valueDepartment);


                JSONArray jsonArraySla = jsonObject1.getJSONArray("sla");
                for (int i = 0; i < jsonArraySla.length(); i++) {
                    keySLA += jsonArraySla.getJSONObject(i).getString("id") + ",";
                    valueSLA += jsonArraySla.getJSONObject(i).getString("name") + ",";
                }
                Prefs.putString("keySLA", keySLA);
                Prefs.putString("valueSLA", valueSLA);

                JSONArray jsonArrayStaffs = jsonObject1.getJSONArray("staffs");
                for (int i = 0; i < jsonArrayStaffs.length(); i++) {
                    keyName +=jsonArrayStaffs.getJSONObject(i).getString("first_name") + jsonArrayStaffs.getJSONObject(i).getString("last_name") +",";
                    keyStaff += jsonArrayStaffs.getJSONObject(i).getString("id") + ",";
                    valueStaff += jsonArrayStaffs.getJSONObject(i).getString("email") + ",";
                }
                Prefs.putString("keyName",keyName);
                Prefs.putString("keyStaff", keyStaff);
                Prefs.putString("valueStaff", valueStaff);

                JSONArray jsonArrayType = jsonObject1.getJSONArray("type");
                for (int i = 0; i < jsonArrayType.length(); i++) {
                    keyType += jsonArrayType.getJSONObject(i).getString("id") + ",";
                    valueType += jsonArrayType.getJSONObject(i).getString("name") + ",";
                }
                Prefs.putString("keyType", keyType);
                Prefs.putString("valueType", valueType);
                JSONArray jsonArrayPriorities = jsonObject1.getJSONArray("priorities");
                for (int i = 0; i < jsonArrayPriorities.length(); i++) {
                    // keyPri.add(jsonArrayPriorities.getJSONObject(i).getString("priority_id"));
                    //valuePri.add(jsonArrayPriorities.getJSONObject(i).getString("priority"));
                    keyPriority += jsonArrayPriorities.getJSONObject(i).getString("priority_id") + ",";
                    valuePriority += jsonArrayPriorities.getJSONObject(i).getString("priority") + ",";
                }
                Prefs.putString("keyPri", keyPriority);
                Prefs.putString("valuePri", valuePriority);
                //Prefs.putOrderedStringSet("keyPri", keyPri);
                // Prefs.putOrderedStringSet("valuePri", valuePri);
                //Log.d("Testtttttt", Prefs.getOrderedStringSet("keyPri", keyPri) + "   " + Prefs.getOrderedStringSet("valuePri", valuePri));


                JSONArray jsonArrayHelpTopics = jsonObject1.getJSONArray("helptopics");
                for (int i = 0; i < jsonArrayHelpTopics.length(); i++) {

                    keyTopic += jsonArrayHelpTopics.getJSONObject(i).getString("id") + ",";
                    valueTopic += jsonArrayHelpTopics.getJSONObject(i).getString("topic") + ",";
                }

                Prefs.putString("keyHelpTopic", keyTopic);
                Prefs.putString("valueHelptopic", valueTopic);

                JSONArray jsonArrayStatus = jsonObject1.getJSONArray("status");
                for (int i = 0; i < jsonArrayStatus.length(); i++) {

                    keyStatus += jsonArrayStatus.getJSONObject(i).getString("id") + ",";
                    valueStatus += jsonArrayStatus.getJSONObject(i).getString("name") + ",";

                }
                Prefs.putString("keyStatus", keyStatus);
                Prefs.putString("valueStatus", valueStatus);

                JSONArray jsonArraySources = jsonObject1.getJSONArray("sources");
                for (int i = 0; i < jsonArraySources.length(); i++) {
                    keySource += jsonArraySources.getJSONObject(i).getString("id") + ",";
                    valueSource += jsonArraySources.getJSONObject(i).getString("name") + ",";
                }

                Prefs.putString("keySource", keySource);
                Prefs.putString("valueSource", valueSource);

                int open = 0, closed = 0, trash = 0, unasigned = 0, my_tickets = 0;
                JSONArray jsonArrayTicketsCount = jsonObject1.getJSONArray("tickets_count");
                for (int i = 0; i < jsonArrayTicketsCount.length(); i++) {
                    String name = jsonArrayTicketsCount.getJSONObject(i).getString("name");
                    String count = jsonArrayTicketsCount.getJSONObject(i).getString("count");

                    switch (name) {
                        case "Open":
                            open = Integer.parseInt(count);
                            break;
                        case "Closed":
                            closed = Integer.parseInt(count);
                            break;
                        case "Deleted":
                            trash = Integer.parseInt(count);
                            break;
                        case "unassigned":
                            unasigned = Integer.parseInt(count);
                            break;
                        case "mytickets":
                            my_tickets = Integer.parseInt(count);
                            break;
                        default:
                            break;

                    }
                }


                if (open > 999)
                    Prefs.putString("inboxTickets", "999+");
                else
                    Prefs.putString("inboxTickets", open + "");

                if (closed > 999)
                    Prefs.putString("closedTickets", "999+");
                else
                    Prefs.putString("closedTickets", closed + "");

                if (my_tickets > 999)
                    Prefs.putString("myTickets", "999+");
                else
                    Prefs.putString("myTickets", my_tickets + "");

                if (trash > 999)
                    Prefs.putString("trashTickets", "999+");
                else
                    Prefs.putString("trashTickets", trash + "");

                if (unasigned > 999)
                    Prefs.putString("unassignedTickets", "999+");
                else
                    Prefs.putString("unassignedTickets", unasigned + "");

            } catch (JSONException | NullPointerException e) {
                //Toasty.error(SplashActivity.this, "Parsing Error!", Toast.LENGTH_LONG).show();
                Prefs.putString("unauthorized", "false");
                Prefs.putString("401","false");
                e.printStackTrace();
            }
        }
    }
}