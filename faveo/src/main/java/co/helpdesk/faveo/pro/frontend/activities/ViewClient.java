package co.helpdesk.faveo.pro.frontend.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import co.helpdesk.faveo.pro.R;

public class ViewClient extends AppCompatActivity {
TextView textView;
ImageView imageView;
String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_client);
        Window window = ViewClient.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(ViewClient.this,R.color.faveo));
        textView= (TextView) findViewById(R.id.editclient);
        imageView= (ImageView) findViewById(R.id.imageViewBack);
        text="To edit customer you have to click on the edit icon in the client detail page.Once you click you will get " +
                "the above screen where you will be able to change the customer <b>first name</b>,<b>last name</b>,<b>email</b>,<b>mobile number</b>,<b>phone number</b> and <b>company</b>.";
        textView.setText(Html.fromHtml(text));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ViewClient.this,CustomersRelated.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(ViewClient.this,CustomersRelated.class);
        startActivity(intent);
    }
}
