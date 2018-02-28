package co.helpdesk.faveo.pro.frontend.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
        textViewDashboard= (TextView) findViewById(R.id.dashboard);
        textViewGuide= (TextView) findViewById(R.id.ticketguide);
        textViewEdit= (TextView) findViewById(R.id.ticketview);
        imageView= (ImageView) findViewById(R.id.imageViewBack);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TicketsRelated.this,SettingsActivity.class);
                startActivity(intent);
            }
        });
        textViewDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TicketsRelated.this,Dashboard.class);
                startActivity(intent);
            }
        });
        textViewGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent=new Intent(TicketsRelated.this,TicketList.class);
            startActivity(intent);
            }
        });
        textViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent=new Intent(TicketsRelated.this,TicketViewAndEdit.class);
            startActivity(intent);
            }
        });
    }
}
