package co.helpdesk.faveo.pro.frontend.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import co.helpdesk.faveo.pro.R;

public class CustomersRelated extends AppCompatActivity {
    ImageView imageView;
    TextView textViewViewClient,textViewEditClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers_related);
        imageView= (ImageView) findViewById(R.id.imageViewBack);
        textViewEditClient= (TextView) findViewById(R.id.clienteditwithfilter);
        textViewViewClient= (TextView) findViewById(R.id.viewclient);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CustomersRelated.this,SettingsActivity.class);
                startActivity(intent);
            }
        });
        textViewViewClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
Intent intent=new Intent(CustomersRelated.this,ViewClient.class);
startActivity(intent);
            }
        });
        textViewEditClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent=new Intent(CustomersRelated.this,CustomerFiltration.class);
            startActivity(intent);
            }
        });

    }
}
