//package co.helpdesk.faveo.pro;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//
//import co.helpdesk.faveo.pro.frontend.activities.LoginActivity;
//
///**
// * Created by Lenovo on 6/20/2017.
// */
//
//public class SplashScreen extends Activity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.splash);
//        SharedPreferences sharedPreferences=getSharedPreferences("fisrttime",0);
//        SharedPreferences.Editor editor=sharedPreferences.edit();
//        int x=sharedPreferences.getInt("x",0);
//        if (x==0){
//            x++;
//            editor.putInt("x",x);
//            editor.commit();
//            Thread timerThread = new Thread(){
//                public void run(){
//                    try{
//                        sleep(3000);
//                    }catch(InterruptedException e){
//                        e.printStackTrace();
//                    }
//                    finally {
//                        Intent intent = new Intent(SplashScreen.this,LoginActivity.class);
//                        startActivity(intent);
//                    }
//                }
//            };
//            timerThread.start();
//        }else{
//            Intent intent = new Intent(SplashScreen.this,LoginActivity.class);
//            startActivity(intent);
//        }
//
//
//    }
//
//    @Override
//    protected void onPause() {
//        // TODO Auto-generated method stub
//        super.onPause();
//        finish();
//    }
//}
