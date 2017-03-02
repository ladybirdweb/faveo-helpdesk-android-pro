package co.helpdesk.faveo.pro;

/**
 * Created by sumit on 3/13/2016.
 */


import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.amitshekhar.DebugDB;
import com.crashlytics.android.Crashlytics;
import com.squareup.leakcanary.LeakCanary;

import java.io.File;

import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class FaveoApplication extends MultiDexApplication {
    private static FaveoApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...

        Log.d("Debug address :",DebugDB.getAddressLog());

        //Fast Android Networking init..
        //AndroidNetworking.initialize(getApplicationContext());
        //AndroidNetworking.setParserFactory(new JacksonParserFactory());

        //Realm.io
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        //Fabric.io
        Fabric.with(this, new Crashlytics());
        instance = this;
    }

    public static synchronized FaveoApplication getInstance() {
        return instance;
    }

    public void setInternetListener(InternetReceiver.InternetReceiverListener listener) {
        InternetReceiver.internetReceiverListener = listener;
    }

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

//    public static boolean deleteDir(File dir) {
//        if (dir != null && dir.isDirectory()) {
//            String[] children = dir.list();
//            for (String aChildren : children)
//                return deleteDir(new File(dir, aChildren));
//        }
//        return dir.delete();
//    }

    private void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory != null && fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                child.delete();
                deleteRecursive(child);
            }
        }
        fileOrDirectory.delete();
    }
}