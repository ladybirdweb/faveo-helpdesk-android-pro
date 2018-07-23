package co.helpdesk.faveo.pro.frontend.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import android.widget.VideoView;
import com.kishan.askpermission.AskPermission;
import com.kishan.askpermission.ErrorCallback;
import com.kishan.askpermission.PermissionCallback;
import com.kishan.askpermission.PermissionInterface;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;

import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.TouchImageView;
import co.helpdesk.faveo.pro.frontend.adapters.AttachmnetAdapter;
import co.helpdesk.faveo.pro.model.AttachmentModel;
import co.helpdesk.faveo.pro.model.TicketThread;
import es.dmoral.toasty.Toasty;

public class ShowingAttachment extends AppCompatActivity implements PermissionCallback, ErrorCallback {
    TouchImageView touchImageView;
    ImageView imageView;
    Toolbar toolbar;
    DecimalFormat df;
    TextView textView;
    WebView textViewFileShow;
    String title, base64String;
    Context context;
    MediaPlayer mediaPlayer;
    String type;
    VideoView videoView;
    byte[] decodedString;
    private static final int PICKFILE_REQUEST_CODE = 1234;
  String file;
    static final Integer WRITE_EXST = 0x3;
    static final Integer READ_EXST = 0x4;
  AttachmnetAdapter attachmnetAdapter;
  ArrayList<AttachmentModel> attachmentModels;
  String multipleName,multipleFile;
  String removeComma,removeCommaFile;
  String ticketID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showing_attachment);
        Window window = ShowingAttachment.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(ShowingAttachment.this,R.color.faveo));
        ListView listView = (ListView) findViewById(R.id.attachment_list);
        reqPermissionCamera();
        ticketID=Prefs.getString("ticketId",null);
//        if (shouldAskPermissions()) {
//            askPermissions();
//        }
        try {
            multipleName=Prefs.getString("multipleName",null);
            multipleFile=Prefs.getString("multipleFile",null);
            removeComma=multipleName.substring(0,multipleName.length()-1);
            removeCommaFile=multipleFile.substring(0,multipleFile.length()-1);
            attachmentModels=new ArrayList<>();
            String[] fileArray=removeCommaFile.split(",");
            String[] animalsArray = removeComma.split(",");
            for (int i=0;i<animalsArray.length;i++){
                title=animalsArray[i];
                file=fileArray[i];
                Log.d("title",title);
                attachmentModels.add(new AttachmentModel(title,file));
            }
            Log.d("multipleN",removeComma);
            //title = Prefs.getString("fileName", null);
            //base64String = Prefs.getString("file", null);
            type = Prefs.getString("type", null);
            file=Prefs.getString("file",null);
            Log.d("type", type);
            Log.d("imagefile", Prefs.getString("base64Image", null));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        attachmnetAdapter = new AttachmnetAdapter(this,attachmentModels);
        listView.setAdapter(attachmnetAdapter);





        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                AttachmentModel attachmentModel=attachmentModels.get(i);
                                String title;
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                byte[] decodedString;
                String base64String;
                //Intent intent=new Intent(view.getContext(), ShowingAttachment.class);
                title=attachmentModel.getName();
                base64String=attachmentModel.getFile();
                decodedString = Base64.decode(base64String, Base64.DEFAULT);

                try {
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                    File myFile = new File(path+"/"+title);
                    File file = new File(path+"/"+title);
                    Uri uri1 = Uri.fromFile(file);
                    Log.d("URI",uri1.toString());
                    myFile.createNewFile();
                    FileOutputStream fOut = new FileOutputStream(myFile);
                    fOut.write(decodedString);
                    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                    myOutWriter.append(title + "came from storage");
                    myOutWriter.close();
                    fOut.close();
                    Intent myIntent = new Intent(Intent.ACTION_VIEW);
                    myIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    String mime= URLConnection.guessContentTypeFromStream(new FileInputStream(myFile));
                    if(mime==null) mime=URLConnection.guessContentTypeFromName(myFile.getName());
                    myIntent.setDataAndType(Uri.fromFile(myFile), mime);
                    view.getContext().startActivity(myIntent);
                    //view.getContext().startActivity(Intent.createChooser(myIntent, "Choose an app to open with"));

                    //Toast.makeText(ShowingAttachment.this, "Done writing SD 'mysdfile.txt'", Toast.LENGTH_SHORT).show();
                    //txtData.setText("");
                } catch (Exception e) {
                    //Toast.makeText(ShowingAttachment.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(ShowingAttachment.this, "CLICKED AT:"+attachmentModel.getFile(), Toast.LENGTH_SHORT).show();
            }
        });
//        mediaPlayer = new MediaPlayer();
//        decodedString = Base64.decode(base64String, Base64.DEFAULT);
//        String text = new String(decodedString);

        toolbar = (Toolbar) findViewById(R.id.toolbarAttachment);
        imageView = (ImageView) toolbar.findViewById(R.id.imageViewBackAttachment);
//        touchImageView = (TouchImageView) findViewById(R.id.attachment_view);
        textView = (TextView) toolbar.findViewById(R.id.attachmenttitle);
        textView.setText(getString(R.string.attachment));
        imageView.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                finishAffinity();
                Intent intent1=new Intent(ShowingAttachment.this,TicketDetailActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("ticket_id", ticketID);
                startActivity(intent1);
            }
        });
    }

    private void reqPermissionCamera() {
        new AskPermission.Builder(this).setPermissions(Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setCallback(this)
                .setErrorCallback(this)
                .request(PICKFILE_REQUEST_CODE);
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

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    public void onPermissionsDenied(int requestCode) {
        Toasty.warning(ShowingAttachment.this,getString(R.string.permission_camera_denied),Toast.LENGTH_SHORT).show();
        return;
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        Intent intent1=new Intent(ShowingAttachment.this,TicketDetailActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent1.putExtra("ticket_id", ticketID);
        startActivity(intent1);
    }
}
