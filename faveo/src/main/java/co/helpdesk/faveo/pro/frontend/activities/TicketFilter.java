package co.helpdesk.faveo.pro.frontend.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.pro.frontend.fragments.tickets.DueByAsc;
import co.helpdesk.faveo.pro.frontend.fragments.tickets.InboxTickets;
import es.dmoral.toasty.Toasty;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_filter);
        show=Prefs.getString("Show",null);
        Log.d("Show",show);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        buttonFilter = (ImageView) findViewById(R.id.buttonfilter);
        setSupportActionBar(toolbar);
        //autoStatus= (MultiAutoCompleteTextView) findViewById(R.id.autocompletestatus);
        autoCompleteAssigned = (AutoCompleteTextView) findViewById(R.id.autocompleteassigned);
        //autoCompleteShow = (Spinner) findViewById(R.id.autocompleteshow);
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

//        autoCompleteShow.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                showArrayAdapter=new ArrayAdapter<>(TicketFilter.this,android.R.layout.simple_dropdown_item_1line,showItems);
//                //staffArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                autoCompleteShow.setAdapter(showArrayAdapter);
//                autoCompleteShow.showDropDown();
//                autoCompleteShow.setThreshold(1);
//                autoCompleteShow.setDropDownWidth(1000);
//                return false;
//            }
//        });
//        autoCompleteShow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                show = showArrayAdapter.getItem(i).toString();
//            }
//        });

//        autoCompleteShow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                show=autoCompleteShow.getSelectedItem().toString();
//            }
//        });
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
//autoCompleteAssigned.setOnTouchListener(new View.OnTouchListener() {
//    @Override
//    public boolean onTouch(View view, MotionEvent motionEvent) {
//        unassignedArrayAdapter=new ArrayAdapter<String>(TicketFilter.this,android.R.layout.simple_dropdown_item_1line,unassignedItems);
//        autoCompleteAssigned.setAdapter(unassignedArrayAdapter);
//        autoCompleteAssigned.showDropDown();
//        autoCompleteAssigned.setThreshold(1);
//        autoCompleteAssigned.setDropDownWidth(1000);
//        return false;
//    }
//});


            imageViewback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(TicketFilter.this, MainActivity.class);
                    startActivity(intent);
                }
            });

            buttonFilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                if (autoDepartment.getText().toString().equals("")&&autoPriority.getText().toString().equals("")&&autoAssigned.getText().toString().equals("")&&autoCompleteShow.getSelectedItemPosition()==0&&autoType.getText().toString().equals("")){
//                    Toasty.info(TicketFilter.this,getString(R.string.emptyField), Toast.LENGTH_SHORT).show();
//                    return;
//                }

//                    if (autoCompleteShow.getSelectedItemPosition() == 0) {
//                        Toasty.info(TicketFilter.this, getString(R.string.rquiredfields), Toast.LENGTH_SHORT).show();
//                        return;
//                    }
                    try {
                        if (!autoDepartment.getText().toString().equals("")) {
                            departmment = autoDepartment.getText().toString();
                            //departmment.replaceAll(" ","");
//                        if (departmment.endsWith(",")) {
//                            Toast.makeText(TicketFilter.this, "comma removed by customer", Toast.LENGTH_SHORT).show();
//                            //departmentfinal = departmment;
//                        }else{
//                            departmment = autoDepartment.getText().toString();
//                            int pos = departmment.lastIndexOf(",");
//                            departmentfinal = departmment.substring(0, pos);
//                            Toast.makeText(TicketFilter.this, "comma removed by programer", Toast.LENGTH_SHORT).show();
//                        }
                            //departmentfinal = departmment.replaceAll(",$", "");
                            result = departmment.replaceAll("\\s+,$", "");
                            result = result.replaceAll(" ", "");
                            Prefs.putString("departmentfinal", result);
//                        if (lastChar.equals(",")){
//                            int pos1 = departmentfinal.lastIndexOf(",");
//                            departmentfinal = departmentfinal.substring(0, pos1);
//                            Prefs.putString("departmentfinal",departmentfinal);
//                        }
//                        else{
//                            Prefs.putString("departmentfinal",departmentfinal);
//                        }
                            //Prefs.putString("departmentfinal",departmentfinal);

                        } else if (autoDepartment.getText().toString().equals("")) {
                            Prefs.putString("departmentfinal", "all");
                        }
//                    else if (autoDepartment.getText().toString().equals("")){
//
//                    }
                    } catch (StringIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }


//                    try{
//                        if (!autoStatus.getText().toString().equals("")){
//                            status = autoStatus.getText().toString();
//                            statusFinal = status.replaceAll("\\s+,$", "");
//                            statusFinal = status.replaceAll(" ", "");
//                            Prefs.putString("statusfinal", statusFinal);
//                        }
//                    }catch (StringIndexOutOfBoundsException e){
//                        e.printStackTrace();
//                    }

                    try {
                        Prefs.putString("show", show);
//                        if (autoCompleteShow.getSelectedItemPosition() > 0) {
//                            show = autoCompleteShow.getSelectedItem().toString();
//                            index=autoCompleteShow.getSelectedItemPosition();
//                            String show1=show.replace(show.charAt(0),show.toLowerCase().charAt(0));
//
//
//                        } else if (autoCompleteShow.getSelectedItemPosition() == 0) {
//                            Prefs.putString("show", "null");
//                        }
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
                            if (autoCompleteAssigned.getText().toString().equals("yes")) {
                                Prefs.putString("assigned", "yes");
                            } else if (autoCompleteAssigned.getText().toString().equals("no")) {
                                Prefs.putString("assigned", "no");
                            }
                        } else if (autoCompleteAssigned.getText().toString().equals("")) {
                            Prefs.putString("assigned", "null");
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    //Toast.makeText(TicketFilter.this, "status:"+statusFinal, Toast.LENGTH_SHORT).show();
//                Toast.makeText(TicketFilter.this, "department:"+result, Toast.LENGTH_SHORT).show();
//                Toast.makeText(TicketFilter.this, "show:"+show, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(TicketFilter.this, "assignedto:"+getStaffItems.toString(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(TicketFilter.this, "priority:"+priorityfinal, Toast.LENGTH_SHORT).show();
//                Toast.makeText(TicketFilter.this, "source:"+sourcefinal, Toast.LENGTH_SHORT).show();
//               Toast.makeText(TicketFilter.this, "type:"+typefinal, Toast.LENGTH_SHORT).show();
//                Toast.makeText(TicketFilter.this, "assigned:"+assigned, Toast.LENGTH_SHORT).show();
                    Prefs.putString("source","6");
                    Prefs.putString("came from filter", "true");
                    Intent intent = new Intent(TicketFilter.this, MainActivity.class);
                    startActivity(intent);


                }
            });
            clearAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //autoCompleteShow.requestFocus();
                    autoType.setText("");
                    //autoCompleteShow.setSelection(0);
                    autoDepartment.setText("");
                    autoPriority.setText("");
                    autoSource.setText("");
                    autoAssigned.setText("");
                    autoCompleteAssigned.setText("");
                    //autoStatus.setText("");
                    //Prefs.putString("departmentfinal", "null");
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

            JSONObject jsonObject;
            String json = Prefs.getString("DEPENDENCY", "");
            try {
                staffItems = new ArrayList<>();
                jsonObject = new JSONObject(json);
                //staffItems.add(new Data(0, "--"));
                JSONArray jsonArrayStaffs = jsonObject.getJSONArray("staffs");
                for (int i = 0; i < jsonArrayStaffs.length(); i++) {
                    //Data data = new Data(Integer.parseInt(jsonArrayStaffs.getJSONObject(i).getString("id")), jsonArrayStaffs.getJSONObject(i).getString("first_name")+" "+jsonArrayStaffs.getJSONObject(i).getString("last_name"));
                    staffItems.add(jsonArrayStaffs.getJSONObject(i).getString("user_name"));
                }
                departmentItems = new ArrayList<>();
                //helptopicItems.add(new Data(0, "--"));
                jsonObject = new JSONObject(json);
                JSONArray jsonArrayHelpTopics = jsonObject.getJSONArray("departments");
                for (int i = 0; i < jsonArrayHelpTopics.length(); i++) {
                    //Data data = new Data(Integer.parseInt(jsonArrayHelpTopics.getJSONObject(i).getString("id")), jsonArrayHelpTopics.getJSONObject(i).getString("topic"));
                    departmentItems.add(jsonArrayHelpTopics.getJSONObject(i).getString("name"));

                }

                statusItems=new ArrayList<>();
                jsonObject = new JSONObject(json);
                JSONArray jsonArrayStatus = jsonObject.getJSONArray("status");
                for (int i = 0; i < jsonArrayStatus.length(); i++) {
                    //Data data = new Data(Integer.parseInt(jsonArrayHelpTopics.getJSONObject(i).getString("id")), jsonArrayHelpTopics.getJSONObject(i).getString("topic"));
                    statusItems.add(jsonArrayStatus.getJSONObject(i).getString("name"));

                }

                showItems = new ArrayList<>();
                showItems.add("Please select a show to filter tickets");
                showItems.add("Inbox");
                showItems.add("Trash");
                showItems.add("Mytickets");
                showItems.add("Closed");

                unassignedItems = new ArrayList<>();
                unassignedItems.add("yes");
                unassignedItems.add("no");

                priorityItems = new ArrayList<>();
                jsonObject = new JSONObject(json);
                JSONArray jsonArrayPriorities = jsonObject.getJSONArray("priorities");
                //priorityItems.add(new Data(0, "--"));
                for (int i = 0; i < jsonArrayPriorities.length(); i++) {
                    // Data data = new Data(Integer.parseInt(jsonArrayPriorities.getJSONObject(i).getString("priority_id")), jsonArrayPriorities.getJSONObject(i).getString("priority"));
                    priorityItems.add(jsonArrayPriorities.getJSONObject(i).getString("priority"));
                }


                typeItems = new ArrayList<>();
                jsonObject = new JSONObject(json);
                JSONArray jsonArrayType = jsonObject.getJSONArray("type");
                //typeItems.add(new Data(0, "--"));
                for (int i = 0; i < jsonArrayType.length(); i++) {
                    //Data data = new Data(Integer.parseInt(jsonArrayType.getJSONObject(i).getString("id")), jsonArrayType.getJSONObject(i).getString("name"));
                    typeItems.add(jsonArrayType.getJSONObject(i).getString("name"));

                }


                sourceItems = new ArrayList<>();
                jsonObject = new JSONObject(json);
                JSONArray jsonArraySources = jsonObject.getJSONArray("sources");
                //sourceItems.add(new Data(0, "--"));
                for (int i = 0; i < jsonArraySources.length(); i++) {
                    //Data data = new Data(Integer.parseInt(jsonArraySources.getJSONObject(i).getString("id")), jsonArraySources.getJSONObject(i).getString("name"));
                    sourceItems.add(jsonArraySources.getJSONObject(i).getString("name"));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            setUpViews();

        }

       public void setUpViews() {



//           statusArrayAdapter=new ArrayAdapter<>(TicketFilter.this,android.R.layout.simple_dropdown_item_1line,statusItems);
//           //staffArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//           autoStatus.setAdapter(statusArrayAdapter);
//           autoStatus.setThreshold(1);
//           autoStatus.setDropDownWidth(1000);
//           autoStatus.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

//
        //autoAssigned= (MultiAutoCompleteTextView) findViewById(R.id.spinner_staffs);
           unassignedArrayAdapter=new ArrayAdapter<String>(TicketFilter.this,android.R.layout.simple_dropdown_item_1line,unassignedItems);
           autoCompleteAssigned.setAdapter(unassignedArrayAdapter);
           autoCompleteAssigned.setThreshold(1);
           autoCompleteAssigned.setDropDownWidth(1000);

//           showArrayAdapter=new ArrayAdapter<>(TicketFilter.this,android.R.layout.simple_dropdown_item_1line,showItems);
//           //staffArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//           autoCompleteShow.setAdapter(showArrayAdapter);
//           autoCompleteShow.setThreshold(1);
//           autoCompleteShow.setDropDownWidth(1000);
//           showArrayAdapter = new ArrayAdapter<>(TicketFilter.this, android.R.layout.simple_spinner_dropdown_item, showItems); //selected item will look like a spinner set from XML
//           showArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//           autoCompleteShow.setAdapter(showArrayAdapter);

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
//    public class SpaceTokenizer implements MultiAutoCompleteTextView.Tokenizer {
//
//        public int findTokenStart(CharSequence text, int cursor) {
//            int i = cursor;
//
//            while (i > 0 && text.charAt(i - 1) != ' ') {
//                i--;
//            }
//            while (i < cursor && text.charAt(i) == ' ') {
//                i++;
//            }
//
//            return i;
//        }
//
//        public int findTokenEnd(CharSequence text, int cursor) {
//            int i = cursor;
//            int len = text.length();
//
//            while (i < len) {
//                if (text.charAt(i) == ' ') {
//                    return i;
//                } else {
//                    i++;
//                }
//            }
//
//            return len;
//        }
//
//        public CharSequence terminateToken(CharSequence text) {
//            int i = text.length();
//
//            while (i > 0 && text.charAt(i - 1) == ' ') {
//                i--;
//            }
//
//            if (i > 0 && text.charAt(i - 1) == ' ') {
//                return text;
//            } else {
//                if (text instanceof Spanned) {
//                    SpannableString sp = new SpannableString(text + ",");
//                    TextUtils.copySpansFrom((Spanned) text, 0, text.length(),
//                            Object.class, sp, 0);
//                    return sp;
//                } else {
//                    return text + ",";
//                }
//            }
//        }
//    }
private int getIndex(Spinner spinner, String myString) {

    int index = 0;

    for (int i = 0; i < spinner.getCount(); i++) {
        Log.d("item ", spinner.getItemAtPosition(i).toString());
        if (spinner.getItemAtPosition(i).toString().equals(myString.trim())) {
            index = i;
            break;
        }
    }
    return index;
}
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent  intent=new Intent(TicketFilter.this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
