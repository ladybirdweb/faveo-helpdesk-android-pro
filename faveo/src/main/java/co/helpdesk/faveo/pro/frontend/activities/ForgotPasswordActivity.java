//package co.helpdesk.faveo.pro.frontend.activities;
//
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import co.helpdesk.faveo.pro.FaveoApplication;
//import co.helpdesk.faveo.pro.R;
//import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
//import co.helpdesk.faveo.pro.model.MessageEvent;
//
//public class ForgotPasswordActivity extends AppCompatActivity  {
//
//    TextView textViewErrorEmail;
//    EditText editTextEmail;
//    Button buttonResetPassword;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_forgot_password);
//        setUpViews();
//        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
//    }
//
//    private void setUpViews() {
//        textViewErrorEmail = (TextView) findViewById(R.id.textView_error_email);
//        editTextEmail = (EditText) findViewById(R.id.editText_email);
//        buttonResetPassword = (Button) findViewById(R.id.button_reset_password);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        // register connection status listener
//        //FaveoApplication.getInstance().setInternetListener(this);
//        checkConnection();
//    }
//
//    private void checkConnection() {
//        boolean isConnected = InternetReceiver.isConnected();
//        showSnackIfNoInternet(isConnected);
//    }
//
//    private void showSnackIfNoInternet(boolean isConnected) {
//        if (!isConnected) {
//            final Snackbar snackbar = Snackbar
//                    .make(findViewById(android.R.id.content), R.string.sry_not_connected_to_internet, Snackbar.LENGTH_INDEFINITE);
//
//            View sbView = snackbar.getView();
//            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
//            textView.setTextColor(Color.RED);
//            snackbar.setAction("X", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    snackbar.dismiss();
//                }
//            });
//            snackbar.show();
//        }
//
//    }
//
//    private void showSnack(boolean isConnected) {
//
//        if (isConnected) {
//
//            Snackbar snackbar = Snackbar
//                    .make(findViewById(android.R.id.content), R.string.connected_to_internet, Snackbar.LENGTH_LONG);
//
//            View sbView = snackbar.getView();
//            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
//            textView.setTextColor(Color.WHITE);
//            snackbar.show();
//        } else {
//            showSnackIfNoInternet(false);
//        }
//
//    }
//
//    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(MessageEvent event) {
//
//        showSnack(event.message);
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    public void onStop() {
//        EventBus.getDefault().unregister(this);
//        super.onStop();
//    }
//
////    /**
////     * Callback will be triggered when there is change in
////     * network connection
////     */
////    @Override
////    public void onNetworkConnectionChanged(boolean isConnected) {
////        showSnack(isConnected);
////    }
//}
