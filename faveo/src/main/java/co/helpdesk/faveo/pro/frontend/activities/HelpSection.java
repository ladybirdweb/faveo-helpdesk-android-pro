package co.helpdesk.faveo.pro.frontend.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import co.helpdesk.faveo.pro.R;

public class HelpSection extends AppCompatActivity {
    ImageView imageView;
    RelativeLayout login,tickets,users,others;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_section);
        imageView= (ImageView) findViewById(R.id.imageViewBack);
        login= (RelativeLayout) findViewById(R.id.login);
        tickets= (RelativeLayout) findViewById(R.id.ticket);
        users= (RelativeLayout) findViewById(R.id.userandagent);
        others= (RelativeLayout) findViewById(R.id.other);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HelpSection.this, MainActivity.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent=new Intent(HelpSection.this,LogIn.class);
            startActivity(intent);
            }
        });
        tickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}
