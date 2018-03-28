package co.helpdesk.faveo.pro.frontend.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.pro.model.Data;
import co.helpdesk.faveo.pro.model.MessageEvent;
import es.dmoral.toasty.Toasty;

public class TicketMergeActtivity extends AppCompatActivity {

ImageView imageViewBack,imageViewMerge;
Spinner spinnerParentTicket;
EditText titleEditText,ReasonForMerging;
int position;
String ticketSubject;
String ticketID;
    int id;
    String ids=null;
ArrayList<Data> dataArrayList,childDataArrayList;
ArrayList<Integer> idarray;
ArrayList<String> subjectarray;
ArrayAdapter<Data> spinnerParentAdapter;
ProgressDialog progressDialog;
    String[] cc,cc1;
    String two;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ticket_merge_acttivity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageViewBack= (ImageView) findViewById(R.id.imageViewBack);
        imageViewMerge= (ImageView) findViewById(R.id.buttonMerge);
        spinnerParentTicket= (Spinner) findViewById(R.id.spinnerParentTicket);
        titleEditText= (EditText) findViewById(R.id.titleEditText);
        ReasonForMerging= (EditText) findViewById(R.id.reasonForMerging);
        ticketSubject= Prefs.getString("TicketSubject",null);
        ticketID=Prefs.getString("tickets",null);
        idarray=new ArrayList<>();
        subjectarray=new ArrayList<>();
        dataArrayList=new ArrayList<>();
        childDataArrayList=new ArrayList<>();
        cc=new String[0];
        cc1=new String[0];
        //Toast.makeText(this, "ticketids: "+ticketID, Toast.LENGTH_LONG).show();
        //Toast.makeText(this, "ticketsubject: "+ticketSubject, Toast.LENGTH_SHORT).show();
        //Log.d("ticketids",ticketID);
        //Log.d("ticketsubject",ticketSubject);
        int pos = ticketID.indexOf("[");
        int pos2=ticketSubject.indexOf("[");
        int pos1 = ticketID.lastIndexOf("]");
        int pos3=ticketSubject.lastIndexOf("]");
        String text1 = ticketID.substring(pos + 1, pos1);
        String text2=ticketSubject.substring(pos2 + 1, pos3);
        //Log.d("subject",text2);
        //Log.d("ticketid",ticketID);
        String[] namesList = text1.split(",");
        String[] subjectList=text2.split(",");
        for (String name : namesList) {
            //Toast.makeText(this, "id :"+name, Toast.LENGTH_SHORT).show();
            idarray.add(Integer.valueOf(name.trim()));
        }

        String[] trimmedArray = new String[namesList.length];
        for (int i = 0; i < namesList.length; i++) {
            trimmedArray[i] = namesList[i];
            //Toast.makeText(this, "id :"+trimmedArray[i], Toast.LENGTH_SHORT).show();
        }
        //Log.d("trimmedarray",trimmedArray.toString());

        //Log.d("idarray",idarray.toString());
        for (String name1 : subjectList){
            //Toast.makeText(this, "subject :"+name1, Toast.LENGTH_SHORT).show();
            subjectarray.add(name1);
        }
//
//
        dataArrayList.add(new Data(0,"Select Parent Ticket"));
        for (int i=0;i<idarray.size();i++){
            int id= idarray.get(i);
            //Toast.makeText(this, "id :"+id, Toast.LENGTH_SHORT).show();
            String subject=subjectarray.get(i);
            //Toast.makeText(this, "subject :"+subject, Toast.LENGTH_SHORT).show();
            Data data=new Data(id,subject);
            dataArrayList.add(data);
            childDataArrayList.add(data);
            Log.d("DATA",childDataArrayList.toString());
        }


        spinnerParentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dataArrayList); //selected item will look like a spinner set from XML
        spinnerParentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerParentTicket.setAdapter(spinnerParentAdapter);



        spinnerParentTicket.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){
                    return;
                }
                else{
                    Data data=dataArrayList.get(i);
                    id=data.getID();
                    //Toast.makeText(TicketMergeActtivity.this, "childarray:"+childDataArrayList.toString(), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(TicketMergeActtivity.this, "id is :"+id, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });








//        int pos2 = stringBuffer.toString().lastIndexOf(",");
//        ticket = stringBuffer.toString().substring(0, pos2);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(TicketMergeActtivity.this,MainActivity.class);
//                Prefs.putString("tickets", null);
//                dataArrayList.clear();
//                startActivity(intent);
                onBackPressed();
            }
        });


        imageViewMerge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerParentTicket.getSelectedItemPosition()==0){
                    Toasty.warning(TicketMergeActtivity.this, getString(R.string.PleaseSelectParentTicket), Toast.LENGTH_LONG).show();
                    return;
                }
                StringBuilder sb=new StringBuilder();
                StringBuilder sb1=new StringBuilder();
                String title=titleEditText.getText().toString();
                String reason=ReasonForMerging.getText().toString();

                //Toast.makeText(TicketMergeActtivity.this, "id's are :"+idarray.toString(), Toast.LENGTH_SHORT).show();
                //Log.d("ids are :",idarray.toString());
                //cc = idarray.toString().split(",");

//                sb = new StringBuilder();
//                for (int i = 0; i < cc.length; i++) {
//                    String one = cc[i].toString();
//                    int pos = one.indexOf("[");
//                    int pos2 = one.lastIndexOf("]");
//                    try {
//                        two = one.substring(pos + 1, pos2);
//                    sb.append(two+",");
//                        //String three=sb.toString();
//
//                        //cc=three.split(",");
//
//                    } catch (StringIndexOutOfBoundsException e) {
//                        e.printStackTrace();
//                    }
//
//
//                }
                int pos=idarray.toString().indexOf("[");
                int pos1=idarray.toString().lastIndexOf("]");
                String id4=idarray.toString().substring(pos+1,pos1);
                cc1 = id4.split(",");
                sb1 = new StringBuilder();
                for (int i=0;i<cc1.length;i++){
                    sb1.append("&t_id[]="+cc1[i].toString());
                }
                String childId=sb1.toString();
                Log.d("ids are :",sb1.toString());
//                cc = sb.toString().split(",");
//                for (String n : cc) {
//                    sb.append("&t_id[]=");
//                    sb.append(n);
//                }
//                ids=sb.toString();
//                Toast.makeText(TicketMergeActtivity.this, "id :"+ids, Toast.LENGTH_SHORT).show();

                progressDialog = new ProgressDialog(TicketMergeActtivity.this);
                progressDialog.setMessage(getString(R.string.mergingTicket));

                try {
                    title = URLEncoder.encode(title.trim(), "utf-8");
                    reason = URLEncoder.encode(reason.trim(), "utf-8");
                    childId=URLEncoder.encode(childId.trim(), "utf-8");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if (InternetReceiver.isConnected()) {
                    progressDialog.show();
                    new MergeTicket(id, title, reason+sb1.toString()).execute();

                }









            }
        });

    }
    @Override
    public void onBackPressed() {
        if (!MainActivity.isShowing) {
            Log.d("isShowing", "false");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else Log.d("isShowing", "true");


        super.onBackPressed();

//        if (fabExpanded)
//            exitReveal();
//        else super.onBackPressed();
    }
    private class MergeTicket extends AsyncTask<String, Void, String> {
        int parentId;
        String title;
        String reason;


        public MergeTicket(int parentId, String title, String reason) {
            this.parentId = parentId;
            this.title = title;
            this.reason = reason;
        }

        protected String doInBackground(String... urls) {

            return new Helpdesk().mergeTicket(parentId, title, reason);
        }

        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            id=0;
            if (result == null) {
                Toasty.error(TicketMergeActtivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }
            String state=Prefs.getString("403",null);
            try {
                if (state.equals("403") && !state.equals("null")) {
                    Toasty.warning(TicketMergeActtivity.this, getString(R.string.permission), Toast.LENGTH_LONG).show();
                    Prefs.putString("403", "null");
                    return;
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }

            try{
                JSONObject jsonObject=new JSONObject(result);
                JSONObject jsonObject1=jsonObject.getJSONObject("response");
                String message=jsonObject1.getString("message");
                if (message.equals("Permission denied, you do not have permission to access the requested page.")){
                    Toasty.warning(TicketMergeActtivity.this, getString(R.string.permission), Toast.LENGTH_LONG).show();
                    Prefs.putString("403", "null");
                    return;
                }

            }catch (JSONException e){
                e.printStackTrace();
            }


            try {

                JSONObject jsonObject=new JSONObject(result);
                JSONObject jsonObject1=jsonObject.getJSONObject("response");
                String message=jsonObject1.getString("message");
                if (message.equals("merged successfully")){
                    Toasty.success(TicketMergeActtivity.this, getString(R.string.successfullyMerged), Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(TicketMergeActtivity.this,MainActivity.class);
                    Prefs.putString("tickets", null);
                    startActivity(intent);
                }
                else if (message.equals("tickets from different users")){
                    Toasty.warning(TicketMergeActtivity.this, getString(R.string.ticketsFromDifferentUsers), Toast.LENGTH_LONG).show();
                    return;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {

        showSnack(event.message);
    }

    @Override
    protected void onResume() {

        super.onResume();

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
//        progressDialog.dismiss();
//        progressDialog=null;
        super.onDestroy();
    }

    /**
     * Display the snackbar if network connection is not there.
     *
     * @param isConnected is a boolean value of network connection.
     */
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


}
