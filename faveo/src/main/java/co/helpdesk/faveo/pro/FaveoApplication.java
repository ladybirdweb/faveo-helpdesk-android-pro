package co.helpdesk.faveo.pro;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.NotificationCompat;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.File;

import co.helpdesk.faveo.pro.frontend.activities.MainActivity;
import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import io.fabric.sdk.android.Fabric;

//import com.crashlytics.android.ndk.CrashlyticsNdk;

/**
 * In this class we are adding fabric to our application.
 * This is for crash reporting ,whenever we will have any issue in
 * the user system this will give the error message for the issue
 * to us.
 */
public class FaveoApplication extends MultiDexApplication {
    private static FaveoApplication instance;
    InternetReceiver internetReceiver;
    NotificationManager notifManager;
    @Override
    public void onCreate() {
        //createNotificationChannel();
        FirebaseApp.initializeApp(this);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true); FirebaseMessaging.getInstance().subscribeToTopic("subTopic");
        Fabric.with(this, new Crashlytics());
        Thread.setDefaultUncaughtExceptionHandler(new LocalFileUncaughtExceptionHandler(this,
                Thread.getDefaultUncaughtExceptionHandler()));
        internetReceiver = new InternetReceiver();
        registerReceiver(
                internetReceiver,
                new IntentFilter(
                        ConnectivityManager.CONNECTIVITY_ACTION));
        super.onCreate();
        MultiDex.install(this);

//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("ccc");
//        StringBuffer stringBuffer=new StringBuffer();
//        stringBuffer.append("cvcv");


//        if (BuildConfig.DEBUG) {
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
//            DebugDB.getAddressLog();
//
////            Stetho.initialize(
////                    Stetho.newInitializerBuilder(this)
////                            .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
////                            .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
////                            .build());
//
//            Stetho.initializeWithDefaults(this);
//
//            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
//                    .addNetworkInterceptor(new StethoInterceptor())
//                    .build();
//            AndroidNetworking.initialize(getApplicationContext(), okHttpClient);
//        } else
        //Fast Android Networking init..
        //AndroidNetworking.initialize(getApplicationContext());
        //AndroidNetworking.setParserFactory(new JacksonParserFactory());

//        //Realm.io
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
//                .name(Realm.DEFAULT_REALM_NAME)
//                .schemaVersion(0)
//                .deleteRealmIfMigrationNeeded()
//                .build();
//        Realm.setDefaultConfiguration(realmConfiguration);

        /*
          Fabric.io.
          Crash reporting tool.
         */
        //aFabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        instance = this;


        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }
    public static Context getContext(){
        return instance;
        // or return instance.getApplicationContext();
    }

    public static synchronized FaveoApplication getInstance() {
        return instance;
    }


    /**
     * Deleting the user data while logging out from app.
     */
    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib"))
                    deleteRecursive(new File(appDir, s));
            }
        }
    }


    private void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory != null && fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                child.delete();
                deleteRecursive(child);
            }
        }
        fileOrDirectory.delete();
    }

    @Override
    public void onTerminate() {
        if (internetReceiver != null) {
            unregisterReceiver(internetReceiver);
            internetReceiver = null;
        }
        super.onTerminate();
    }

}