package co.helpdesk.faveo.pro.frontend.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.MenuSheetView;
import com.kishan.askpermission.AskPermission;
import com.kishan.askpermission.ErrorCallback;
import com.kishan.askpermission.PermissionCallback;
import com.kishan.askpermission.PermissionInterface;
import com.pixplicity.easyprefs.library.Prefs;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.AudioPickActivity;
import com.vincent.filepicker.activity.ImagePickActivity;
import com.vincent.filepicker.activity.NormalFilePickActivity;
import com.vincent.filepicker.activity.VideoPickActivity;
import com.vincent.filepicker.filter.entity.AudioFile;
import com.vincent.filepicker.filter.entity.ImageFile;
import com.vincent.filepicker.filter.entity.NormalFile;
import com.vincent.filepicker.filter.entity.VideoFile;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import co.helpdesk.faveo.pro.Constants;
import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.backend.api.v1.Helpdesk;
import es.dmoral.toasty.Toasty;

import static com.vincent.filepicker.activity.AudioPickActivity.IS_NEED_RECORDER;
import static com.vincent.filepicker.activity.ImagePickActivity.IS_NEED_CAMERA;

public class TicketReplyActivity extends AppCompatActivity implements PermissionCallback, ErrorCallback {
    ImageView imageView;
    public static String ticketID;
    Button buttonSend;
    EditText editTextReplyMessage;
    ProgressDialog progressDialog;
    TextView addCc;
    Button button;
    ImageButton imageButton;
    RelativeLayout attachment_layout;
    TextView attachmentFileName;
    String token;
    ProgressBar progressBar;
    private static final int PICKFILE_REQUEST_CODE = 1234;
    int gallery,document,camera,audio=0;
    String path="1",realPath="1";
    String replyMessage;
    int id;
    String email;
    BottomSheetLayout bottomSheet;
    String option;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_reply);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Window window = TicketReplyActivity.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(TicketReplyActivity.this,R.color.faveo));
        button= (Button) findViewById(R.id.attachment);
        attachment_layout= (RelativeLayout) findViewById(R.id.attachment_layout);
        attachmentFileName= (TextView) findViewById(R.id.attachment_name);
        imageButton= (ImageButton) findViewById(R.id.attachment_close);
        bottomSheet= (BottomSheetLayout) findViewById(R.id.bottomsheet);
        final Intent intent = getIntent();
        ticketID=intent.getStringExtra("ticket_id");
        Prefs.putString("TICKETid",ticketID);
        Prefs.putString("ticketId",ticketID);
        //bottomNavigationView= (BottomNavigationView) findViewById(R.id.navigation);
        //bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attachment_layout.setVisibility(View.GONE);
                realPath="1";
                path="1";
                Prefs.putString("filePath","1");
                Log.d("path",realPath);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = TicketReplyActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                MenuSheetView menuSheetView =
                        new MenuSheetView(TicketReplyActivity.this, MenuSheetView.MenuType.LIST, "Choose...", new MenuSheetView.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                if (bottomSheet.isSheetShowing()) {
                                    bottomSheet.dismissSheet();
                                }
                                if (item.getItemId()==R.id.imageGalley){
                                    gallery=2;
                                    reqPermissionCamera();
                                    return true;
                                    }
                                else if (item.getItemId()==R.id.videoGallery){
                                    camera=3;
                                    reqPermissionCamera();
                                    return true;
                                }
                                else if (item.getItemId()==R.id.musicGallery){
                                    audio=4;
                                    reqPermissionCamera();
                                    return true;
                                }
                                else if (item.getItemId()==R.id.documentGallery){
                                    document=1;
                                    reqPermissionCamera();
                                    return true;
                                }

                                return true;
                            }
                        });
                menuSheetView.inflateMenu(R.menu.navigation);
                bottomSheet.showWithSheetView(menuSheetView);

//                if (bottomNavigationView.getVisibility()==View.GONE){
//                    bottomNavigationView.setVisibility(View.VISIBLE);
//                }
//                else if (bottomNavigationView.getVisibility()==View.VISIBLE){
//                    bottomNavigationView.setVisibility(View.GONE);
//                }
               }
        });
        //ticketID=Prefs.getString("ticketId",null);
        buttonSend = (Button) findViewById(R.id.button_send);
        imageView= (ImageView) findViewById(R.id.imageViewBackTicketReply);
        progressDialog = new ProgressDialog(this);
        progressBar= (ProgressBar) findViewById(R.id.progress_bar);
        addCc = (TextView) findViewById(R.id.addcc);
        editTextReplyMessage = (EditText) findViewById(R.id.editText_reply_message);
        editTextReplyMessage.setCursorVisible(true);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent1=new Intent(TicketReplyActivity.this,TicketDetailActivity.class);
//                intent1.putExtra("ticket_id", ticketID);
//                startActivity(intent1);
                finish();
            }
        });
        addCc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TicketReplyActivity.this,collaboratorAdd.class);
                intent.putExtra("ticket_id", ticketID);
                startActivity(intent);
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replyMessage = editTextReplyMessage.getText().toString();
                try {
                    realPath = path;
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                if (replyMessage.trim().length() == 0) {
                    Toasty.warning(TicketReplyActivity.this, getString(R.string.msg_must_not_be_empty), Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    Log.d("realPath",path);
                    progressDialog.setMessage(getString(R.string.sending_msg));
                    progressDialog.show();
                    new SendPostRequest().execute();

                }
                }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_clip:
                MenuSheetView menuSheetView =
                        new MenuSheetView(TicketReplyActivity.this, MenuSheetView.MenuType.LIST, "Choose...", new MenuSheetView.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Toast.makeText(TicketReplyActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                                if (bottomSheet.isSheetShowing()) {
                                    bottomSheet.dismissSheet();
                                }
                                return true;
                            }
                        });
                menuSheetView.inflateMenu(R.menu.navigation);
                bottomSheet.showWithSheetView(menuSheetView);

            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
//        Intent intent1=new Intent(TicketReplyActivity.this,TicketDetailActivity.class);
//        intent1.putExtra("ticket_id", ticketID);
//        startActivity(intent1);
        finish();
//        if (!TicketDetailActivity.isShowing) {
//            Log.d("isShowing", "false");
//            Intent intent = new Intent(this, TicketDetailActivity.class);
//            intent.putExtra("ticket_id", ticketID);
//            startActivity(intent);
//        } else {
//            Intent intent = new Intent(this, TicketDetailActivity.class);
//            intent.putExtra("ticket_id", ticketID);
//            startActivity(intent);
//            Log.d("isShowing", "true");
//        }
//
//        super.onBackPressed();
    }
    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {
            try {

                URL url = new URL(Constants.URL + "authenticate"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("username", Prefs.getString("USERNAME", null));
                postDataParams.put("password", Prefs.getString("PASSWORD", null));
                Log.e("params",postDataParams.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("resultFromNewCall",result);
            try {
                JSONObject jsonObject=new JSONObject(result);
                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                token = jsonObject1.getString("token");
                Prefs.putString("TOKEN", token);
                Log.d("TOKEN",token);
                String userID = Prefs.getString("ID", null);
                try {
                    Log.d("realPath",realPath);
                    if (userID != null && userID.length() != 0) {
                        if (realPath.equals("1")) {
                            try {
                                replyMessage = URLEncoder.encode(replyMessage, "utf-8");
                            } catch (UnsupportedEncodingException e1) {
                                e1.printStackTrace();
                            }
                            new ReplyTicket(Integer.parseInt(ticketID),replyMessage).execute();
                        } else {
                            try {
                                try {
                                    String  replyMessag1 = URLEncoder.encode(replyMessage, "utf-8");
                                    Log.d("replyMessage",replyMessag1);
//                        progressDialog.setMessage(getString(R.string.sending_msg));
//                        progressDialog.show();
                                    String token = Prefs.getString("TOKEN", null);
                                    String uploadId = UUID.randomUUID().toString();
                                    new MultipartUploadRequest(TicketReplyActivity.this, uploadId, Constants.URL + "helpdesk/reply?token=" + token)
                                            .addFileToUpload(realPath, "media_attachment[]")
                                            //Adding file
                                            //.addParameter("token", token1)
                                            .addParameter("ticket_id", ticketID)
                                            .addParameter("reply_content", replyMessage)
                                            //Adding text parameter to the request
                                            //.setNotificationConfig(new UploadNotificationConfig())
                                            .setMaxRetries(1)
                                            .setMethod("POST").setDelegate(new UploadStatusDelegate() {
                                        @Override
                                        public void onProgress(UploadInfo uploadInfo) {

                                        }

                                        @Override
                                        public void onError(UploadInfo uploadInfo, Exception exception) {

                                        }

                                        @Override
                                        public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {
                                            //progressDialog.dismiss();
                                            Log.d("newStyle", serverResponse.getBodyAsString());
                                            Log.i("newStyle", String.format(Locale.getDefault(),
                                                    "ID %1$s: completed in %2$ds at %3$.2f Kbit/s. Response code: %4$d, body:[%5$s]",
                                                    uploadInfo.getUploadId(), uploadInfo.getElapsedTime() / 1000,
                                                    uploadInfo.getUploadRate(), serverResponse.getHttpCode(),
                                                    serverResponse.getBodyAsString()));
                                            //new FetchTicketThreads(TicketReplyActivity.this, Prefs.getString("TICKETid", null)).execute();
                                editTextReplyMessage.getText().clear();
                                Toasty.success(TicketReplyActivity.this, getString(R.string.posted_reply), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(TicketReplyActivity.this, MainActivity.class);
                                startActivity(intent);

                                        }

                                        @Override
                                        public void onCancelled(UploadInfo uploadInfo) {

                                        }
                                    })
                                            .startUpload();
                                    //Starting the upload
                                } catch (MalformedURLException | NullPointerException | IllegalArgumentException | FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    } else
                        Toasty.warning(TicketReplyActivity.this, getString(R.string.wrong_user_id), Toast.LENGTH_LONG).show();
                }catch (NullPointerException e){
                    Log.d("replyContent",replyMessage);
//                    progressDialog.setMessage(getString(R.string.sending_msg));
//                    progressDialog.show();
                    e.printStackTrace();
                }

            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }


    @Override
    public void onPermissionsGranted(int requestCode) {
        Log.d("requestCode",""+requestCode);
        if (document==1){
            Intent intent4 = new Intent(this, NormalFilePickActivity.class);
            intent4.putExtra(Constant.MAX_NUMBER, 1);
            intent4.putExtra(NormalFilePickActivity.SUFFIX,
                    new String[] {"xlsx", "xls", "doc", "dOcX", "ppt", ".pptx", "pdf"});
            startActivityForResult(intent4, Constant.REQUEST_CODE_PICK_FILE);
            document=0;
        }
        if (gallery==2){
            Intent intent1 = new Intent(this, ImagePickActivity.class);
            intent1.putExtra(IS_NEED_CAMERA, true);
            intent1.putExtra(Constant.MAX_NUMBER, 1);
            startActivityForResult(intent1, Constant.REQUEST_CODE_PICK_IMAGE);
            gallery=0;
        }

        if (camera==3){
            Intent intent2 = new Intent(this, VideoPickActivity.class);
            intent2.putExtra(IS_NEED_CAMERA, true);
            intent2.putExtra(Constant.MAX_NUMBER, 1);
            startActivityForResult(intent2, Constant.REQUEST_CODE_PICK_VIDEO);
            camera=0;
        }
        if (audio==4){
            Intent intent3 = new Intent(this, AudioPickActivity.class);
            intent3.putExtra(IS_NEED_RECORDER, true);
            intent3.putExtra(Constant.MAX_NUMBER, 1);
            startActivityForResult(intent3, Constant.REQUEST_CODE_PICK_AUDIO);
            audio=0;

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        String filePath = null;
        String fileName;
        try {
            if (data.toString().equals("")) {
                attachment_layout.setVisibility(View.GONE);
            } else {
                switch (requestCode){
                    case Constant.REQUEST_CODE_PICK_IMAGE:
                        if (resultCode==RESULT_OK){
                            ArrayList<ImageFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_IMAGE);
                            StringBuilder builder = new StringBuilder();
                            for (ImageFile file : list) {
                                filePath = file.getPath();
                                Log.d("filePath",filePath);
                                builder.append(filePath + "\n");
                            }
                            File file = new File(filePath);
                            long size = file.length()/1024;
                            if (size > 6000) {
                                Toasty.info(TicketReplyActivity.this, getString(R.string.fileSize), Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                attachment_layout.setVisibility(View.VISIBLE);
                                path = filePath;
                                Prefs.putString("filePath",path);
                                int pos=path.lastIndexOf("/");
                                fileName=path.substring(pos+1,path.length());
                                attachmentFileName.setText(fileName);
                                Log.d("fileName",fileName);
                            }
                        }

                        break;
                    case Constant.REQUEST_CODE_PICK_VIDEO:
                        if (resultCode == RESULT_OK) {
                            ArrayList<VideoFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_VIDEO);
                            StringBuilder builder = new StringBuilder();
                            for (VideoFile file : list) {
                                filePath = file.getPath();
                                builder.append(filePath + "\n");
                            }
                            File file = new File(filePath);
                            long size = file.length()/1024;
                            if (size > 6000) {
                                Toasty.info(TicketReplyActivity.this, getString(R.string.fileSize), Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                attachment_layout.setVisibility(View.VISIBLE);
                                path = filePath;
                                Prefs.putString("filePath",path);
                                int pos=path.lastIndexOf("/");
                                fileName=path.substring(pos+1,path.length());
                                attachmentFileName.setText(fileName);
                                Log.d("fileName",fileName);
                            }
                        }
                        break;
                    case Constant.REQUEST_CODE_PICK_AUDIO:
                        if (resultCode == RESULT_OK) {
                            ArrayList<AudioFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_AUDIO);
                            StringBuilder builder = new StringBuilder();
                            for (AudioFile file : list) {
                                filePath = file.getPath();
                                builder.append(filePath + "\n");
                            }
                            File file = new File(filePath);
                            long size = file.length()/1024;
                            if (size > 6000) {
                                Toasty.info(TicketReplyActivity.this, getString(R.string.fileSize), Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                attachment_layout.setVisibility(View.VISIBLE);
                                path = filePath;
                                Prefs.putString("filePath",path);
                                int pos=path.lastIndexOf("/");
                                fileName=path.substring(pos+1,path.length());
                                attachmentFileName.setText(fileName);
                                Log.d("fileName",fileName);
                            }
                        }
                        break;
                    case Constant.REQUEST_CODE_PICK_FILE:
                        if (resultCode == RESULT_OK) {
                            ArrayList<NormalFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_FILE);
                            StringBuilder builder = new StringBuilder();
                            for (NormalFile file : list) {
                                filePath = file.getPath();
                                builder.append(filePath + "\n");
                            }
                            File file = new File(filePath);
                            long size = file.length()/1024;
                            if (size > 6000) {
                                Toasty.info(TicketReplyActivity.this, getString(R.string.fileSize), Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                path=filePath;
                                Prefs.putString("filePath",path);
                                attachment_layout.setVisibility(View.VISIBLE);                                path = filePath;
                                int pos=path.lastIndexOf("/");
                                fileName=path.substring(pos+1,path.length());
                                attachmentFileName.setText(fileName);
                                Log.d("fileName",fileName);
                            }
                        }
                        break;

                }
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode) {
        Toasty.warning(TicketReplyActivity.this,getString(R.string.permission_camera_denied),Toast.LENGTH_SHORT).show();
        return;
    }

    @Override
    public void onShowRationalDialog(final PermissionInterface permissionInterface, int requestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("We need permissions for this app.");
        builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permissionInterface.onDialogShown();
            }
        });
        builder.setNegativeButton(R.string.btn_cancel, null);
        builder.show();
    }
    @Override
    public void onShowSettings(final PermissionInterface permissionInterface, int requestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("We need permissions for this app. Open setting screen?");
        builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permissionInterface.onSettingsShown();
            }
        });
        builder.setNegativeButton(R.string.btn_cancel, null);
        builder.show();
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
            //progressDialog.dismiss();
            if (result == null) {
                Toasty.error(TicketReplyActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }
            try{
                JSONObject jsonObject=new JSONObject(result);
                String message=jsonObject.getString("message");

                if (message.contains("Successfully replied")){
                    Toasty.success(TicketReplyActivity.this, getString(R.string.posted_reply), Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(TicketReplyActivity.this,MainActivity.class);
                    startActivity(intent);
                    //new FetchTicketThreads(TicketReplyActivity.this, Prefs.getString("TICKETid", null)).execute();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    @SuppressLint("StaticFieldLeak")
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
            //Prefs.putString("cameFromNotification","false");
            int noOfCollaborator = 0;
            if (isCancelled()) return;
            if (result == null) {
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
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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

    private void reqPermissionCamera() {
        new AskPermission.Builder(this).setPermissions(Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setCallback(TicketReplyActivity.this)
                .setErrorCallback(TicketReplyActivity.this)
                .request(PICKFILE_REQUEST_CODE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        addCc.setVisibility(View.GONE);
        new FetchCollaboratorAssociatedWithTicket(Prefs.getString("ticketId", null)).execute();
    }

}