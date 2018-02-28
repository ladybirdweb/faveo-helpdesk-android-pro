package co.helpdesk.faveo.pro.frontend.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import co.helpdesk.faveo.pro.R;

public class SelectingMultipleTickets extends AppCompatActivity {
    TextView textView;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecting_multiple_tickets);
        imageView= (ImageView) findViewById(R.id.imageViewBack);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SelectingMultipleTickets.this,OtherFeatures.class);
                startActivity(intent);
            }
        });
    }
}
