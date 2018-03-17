package co.helpdesk.faveo.pro.frontend.activities;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.Context;
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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import android.widget.VideoView;

import com.github.barteksc.pdfviewer.PDFView;
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

import co.helpdesk.faveo.pro.R;
import co.helpdesk.faveo.pro.TouchImageView;

public class ShowingAttachment extends AppCompatActivity {
    TouchImageView touchImageView;
    ImageView imageView;
    Toolbar toolbar;
    DecimalFormat df;
    TextView textView;
    WebView textViewFileShow;
    String title, base64String;
    Context context;
    PDFView pdfView;
    File file;
    MediaPlayer mediaPlayer;
    String type;
    VideoView videoView;
    byte[] decodedString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());
//        if (shouldAskPermissions()) {
//            askPermissions();
//        }
        try {
            title = Prefs.getString("attachmentTitle", null);
            base64String = Prefs.getString("base64Image", null);
            type = Prefs.getString("type", null);
            Log.d("type", type);
            Log.d("imagefile", Prefs.getString("base64Image", null));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        mediaPlayer = new MediaPlayer();
        decodedString = Base64.decode(base64String, Base64.DEFAULT);
        String text = new String(decodedString);
        setContentView(R.layout.activity_showing_attachment);
        toolbar = (Toolbar) findViewById(R.id.toolbarAttachment);
        imageView = (ImageView) toolbar.findViewById(R.id.imageViewBackAttachment);
        touchImageView = (TouchImageView) findViewById(R.id.attachment_view);
        textView = (TextView) toolbar.findViewById(R.id.attachmenttitle);
        pdfView = (PDFView) findViewById(R.id.pdfView);
        textViewFileShow = (WebView) findViewById(R.id.textFile);
        Uri uri = Uri.parse(text);
        videoView = (VideoView) findViewById(R.id.videoView);
        textViewFileShow.loadData(URLEncoder.encode(text).replaceAll("\\+", " "), "text/html", Xml.Encoding.UTF_8.toString());
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
            //startActivity(myIntent);
            startActivity(Intent.createChooser(myIntent, "Choose an app to open with"));

            //Toast.makeText(ShowingAttachment.this, "Done writing SD 'mysdfile.txt'", Toast.LENGTH_SHORT).show();
            //txtData.setText("");
        } catch (Exception e) {
            Toast.makeText(ShowingAttachment.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
//        textViewFileShow.setMovementMethod(new ScrollingMovementMethod());
//        textViewFileShow.setText(text);
//        pdfView.fromBytes(decodedString).load();
////        String byteArray=new String(decodedString);
//        pdfView.fromBytes(decodedString).load();
        df = new DecimalFormat("#.##");


//        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//        touchImageView.setImageBitmap(decodedByte);
        textView.setText(title);




        touchImageView.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
//                try {
//                    String path = Environment.getExternalStorageDirectory().getAbsolutePath();
//                    File myFile = new File(path+"/"+title);
//                    FileInputStream fIn = new FileInputStream(myFile);
//                    BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
//                    String aDataRow = "";
//                    String aBuffer = "";
//                    while ((aDataRow = myReader.readLine()) != null)
//                    {
//                        aBuffer += aDataRow ;
//                    }
//                    textView.setText(aBuffer);
//                    myReader.close();
//                    Toast.makeText(ShowingAttachment.this,"Done reading SD 'mysdfile.txt'",Toast.LENGTH_SHORT).show();
//                }
//                catch (Exception e)
//                {
//                    Toast.makeText(ShowingAttachment.this, e.getMessage(),Toast.LENGTH_SHORT).show();
//                }
            }

            });


        touchImageView.setOnTouchImageViewListener(new TouchImageView.OnTouchImageViewListener()

        {
            @Override
            public void onMove() {

            }
        });

        imageView.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowingAttachment.this, TicketDetailActivity.class);
                startActivity(intent);
            }
        });
    }



//    private void playMp3(byte[] mp3SoundByteArray) {
//        try {
//            // create temp file that will hold byte array
//            File tempMp3 = File.createTempFile("fmtemp", "mp3", getCacheDir());
//            tempMp3.deleteOnExit();
//            FileOutputStream fos = new FileOutputStream(tempMp3);
//            fos.write(mp3SoundByteArray);
//            fos.close();
//            mediaPlayer.reset();
//            FileInputStream fis = new FileInputStream(tempMp3);
//            mediaPlayer.setDataSource(fis.getFD());
//            mediaPlayer.prepare();
//            mediaPlayer.start();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }
//    protected boolean shouldAskPermissions() {
//        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
//    }
//
//    @TargetApi(23)
//    protected void askPermissions() {
//        String[] permissions = {
//                "android.permission.READ_EXTERNAL_STORAGE",
//                "android.permission.WRITE_EXTERNAL_STORAGE"
//        };
//        int requestCode = 200;
//        requestPermissions(permissions, requestCode);
//    }

}
