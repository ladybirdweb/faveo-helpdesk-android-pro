package co.helpdesk.faveo.pro.frontend.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import co.helpdesk.faveo.pro.R;
import es.dmoral.toasty.Toasty;

import static co.helpdesk.faveo.pro.FaveoApplication.getContext;

public class SettingsActivity extends AppCompatActivity {
    //SwitchCompat switchCompatCrashReports;
    //public static boolean isCheckedCrashReports = true;
    ImageView imageView;
    RelativeLayout login, ticket, users, others;
    Button buttonFeedback, buttonFeedBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Window window = SettingsActivity.this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(SettingsActivity.this, R.color.faveo));
        imageView = (ImageView) findViewById(R.id.imageViewBack);
        //switchCompatCrashReports = (SwitchCompat) findViewById(R.id.switch_crash_reports);
        buttonFeedback = (Button) findViewById(R.id.feedback);
        login = (RelativeLayout) findViewById(R.id.login);
        ticket = (RelativeLayout) findViewById(R.id.tickets);
        users = (RelativeLayout) findViewById(R.id.userandagent);
        others = (RelativeLayout) findViewById(R.id.other);
        buttonFeedback = (Button) findViewById(R.id.feedback);
        Prefs.putString("cameFromSetting", "true");
        buttonFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, FeedBackActivity.class);
                startActivity(intent);
            }
        });
        //switchCompatCrashReports.setChecked(Prefs.getBoolean("CRASH_REPORT", false));
        // switchCompatCrashReports.setOnCheckedChangeListener(this);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, LogIn.class);
                startActivity(intent);
            }
        });
        ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, TicketsRelated.class);
                startActivity(intent);
            }
        });
        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, CustomersRelated.class);
                startActivity(intent);
            }
        });
        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, OtherFeatures.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
