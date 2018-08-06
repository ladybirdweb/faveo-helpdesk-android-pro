package co.helpdesk.faveo.pro.frontend.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.pixplicity.easyprefs.library.Prefs;
import com.vorlonsoft.android.rate.AppRate;
import com.vorlonsoft.android.rate.OnClickButtonListener;
import com.vorlonsoft.android.rate.StoreType;

import co.helpdesk.faveo.pro.R;

public class SettingsActivity extends AppCompatActivity {
    //SwitchCompat switchCompatCrashReports;
    //public static boolean isCheckedCrashReports = true;
    ImageView imageView;
    RelativeLayout login, ticket, users, others;
    Button buttonFeedback,buttonRating;


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
        buttonRating= (Button) findViewById(R.id.rateus);
        ticket = (RelativeLayout) findViewById(R.id.tickets);
        users = (RelativeLayout) findViewById(R.id.userandagent);
        others = (RelativeLayout) findViewById(R.id.other);
        buttonFeedback = (Button) findViewById(R.id.feedback);
        Prefs.putString("cameFromSetting", "true");

        buttonRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppRate.with(SettingsActivity.this)
                        .setStoreType(StoreType.GOOGLEPLAY) //default is GOOGLEPLAY (Google Play), other options are
                        //           AMAZON (Amazon Appstore) and
                        //           SAMSUNG (Samsung Galaxy Apps)
                        //.setInstallDays((byte) 0) // default 10, 0 means install day
                        .setLaunchTimes((byte) 3) // default 10
                        .setRemindInterval((byte) 2) // default 1
                        .setRemindLaunchTimes((byte) 2) // default 1 (each launch)
                        .setShowLaterButton(true) // default true
                        .setDebug(false) // default false
                        //Java 8+: .setOnClickButtonListener(which -> Log.d(MainActivity.class.getName(), Byte.toString(which)))
                        .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
                            @Override
                            public void onClickButton(byte which) {
                                Log.d(MainActivity.class.getName(), Byte.toString(which));
                            }
                        })
                        .monitor();

                if (AppRate.with(SettingsActivity.this).getStoreType() == StoreType.GOOGLEPLAY) {
                    //Check that Google Play is available
                    if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(SettingsActivity.this) != ConnectionResult.SERVICE_MISSING) {
                        // Show a dialog if meets conditions
                        AppRate.showRateDialogIfMeetsConditions(SettingsActivity.this);
                    }
                } else {
                    // Show a dialog if meets conditions
                    AppRate.showRateDialogIfMeetsConditions(SettingsActivity.this);
                }

                AppRate.with(SettingsActivity.this).showRateDialog(SettingsActivity.this);
            }
        });
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
                finish();

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
        //super.onBackPressed();
        finish();
    }
}
