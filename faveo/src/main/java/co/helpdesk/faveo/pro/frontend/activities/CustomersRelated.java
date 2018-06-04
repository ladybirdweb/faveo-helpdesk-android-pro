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

public class CustomersRelated extends AppCompatActivity {
    ImageView imageView;
    TextView textViewViewClient,textViewEditClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers_related);
        Window window = CustomersRelated.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(CustomersRelated.this,R.color.faveo));
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
