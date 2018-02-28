package co.helpdesk.faveo.pro.frontend.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
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
}
