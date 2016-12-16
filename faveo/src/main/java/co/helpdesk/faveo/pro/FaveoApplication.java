package co.helpdesk.faveo.pro;

/**
 * Created by sumit on 3/13/2016.
 */


import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import java.io.File;

import android.app.Application;

import co.helpdesk.faveo.pro.frontend.receivers.InternetReceiver;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class FaveoApplication extends Application {
    private static FaveoApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
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
                    deleteDir(new File(appDir, s));
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children)
                return deleteDir(new File(dir, aChildren));
        }
        return dir.delete();
    }
}