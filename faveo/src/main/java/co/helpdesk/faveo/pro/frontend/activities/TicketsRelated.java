package co.helpdesk.faveo.pro.frontend.activities;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import co.helpdesk.faveo.pro.R;

public class TicketsRelated extends AppCompatActivity {
TextView textViewDashboard,textViewGuide,textViewEdit;
ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets_related);
        Window window = TicketsRelated.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(TicketsRelated.this,R.color.faveo));
        textViewDashboard= (TextView) findViewById(R.id.dashboard);
        textViewGuide= (TextView) findViewById(R.id.ticketguide);
        textViewEdit= (TextView) findViewById(R.id.ticketview);
        imageView= (ImageView) findViewById(R.id.imageViewBack);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TicketsRelated.this,SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        textViewDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TicketsRelated.this,Dashboard.class);
                startActivity(intent);
                finish();
            }
        });
        textViewGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent=new Intent(TicketsRelated.this,TicketList.class);
            startActivity(intent);
            finish();
            }
        });
        textViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent=new Intent(TicketsRelated.this,TicketViewAndEdit.class);
            startActivity(intent);
            finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(TicketsRelated.this,SettingsActivity.class);
        startActivity(intent);
        finish();
    }
}
