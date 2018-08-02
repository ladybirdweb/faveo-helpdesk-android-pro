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

public class OtherFeatures extends AppCompatActivity {
ImageView imageView;
TextView textViewMultipleTicket,textViewChangeStatus,textViewAssign,textViewMerging,textViewGivingFeedback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_features);
        Window window = OtherFeatures.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(OtherFeatures.this,R.color.faveo));
        imageView= (ImageView) findViewById(R.id.imageViewBack);
        textViewMultipleTicket= (TextView) findViewById(R.id.multipleticket);
        textViewChangeStatus= (TextView) findViewById(R.id.changestatus);
        textViewAssign= (TextView) findViewById(R.id.assignmultipleticket);
        textViewMerging= (TextView) findViewById(R.id.mergingticket);
        textViewGivingFeedback= (TextView) findViewById(R.id.givingFeedback);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(OtherFeatures.this,SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        textViewMultipleTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(OtherFeatures.this,SelectingMultipleTickets.class);
                startActivity(intent);
                finish();
            }
        });
        textViewChangeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent=new Intent(OtherFeatures.this,ChangingStatus.class);;
            startActivity(intent);
                finish();
            }
        });
        textViewAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        Intent intent=new Intent(OtherFeatures.this,MultipleTicketAssign.class);
        startActivity(intent);
                finish();
            }
        });
        textViewMerging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        Intent intent=new Intent(OtherFeatures.this,MultipleTicketMerge.class);
        startActivity(intent);
                finish();
            }
        });
        textViewGivingFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(OtherFeatures.this,GivingFeedback.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(OtherFeatures.this,SettingsActivity.class);
        startActivity(intent);
        finish();
    }
}
