package co.helpdesk.faveo.pro.frontend.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import co.helpdesk.faveo.pro.R;

public class ChangingStatus extends AppCompatActivity {
ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changing_status);
        imageView= (ImageView) findViewById(R.id.imageViewBack);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChangingStatus.this,OtherFeatures.class);
                startActivity(intent);
            }
        });
    }
}
