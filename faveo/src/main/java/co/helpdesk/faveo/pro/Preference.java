package co.helpdesk.faveo.pro;

import android.content.Context;
import android.content.SharedPreferences;

public class Preference {

    //    private Context applicationContext;
    private static SharedPreferences pref;
    //private SharedPreferences.Editor editor;

    private static boolean crashReport;

//    private static Preference instance;

    public Preference() {
    }

    public static void setInstance(Context context) {
        if (pref == null)
            pref = context.getSharedPreferences(Constants.PREFERENCE, Context.MODE_PRIVATE);
    }

//    private static boolean fetchDependency;

//    public static boolean isFetchDependency() {
//        SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
//        return prefs.getBoolean("FETCHDEPENDENCY", false);
//    }
//
//    public static void setFetchDependency(boolean fetchDependency) {
//        SharedPreferences.Editor authenticationEditor = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0).edit();
//        authenticationEditor.putBoolean("FETCHDEPENDENCY", fetchDependency);
//        authenticationEditor.apply();
//    }


    public static String getFCMtoken() {
        //SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
        return pref.getString("FCMtoken", null);
    }

    public static void setFCMtoken(String FCMtoken) {
        SharedPreferences.Editor authenticationEditor = pref.edit();
        authenticationEditor.putString("FCMtoken", FCMtoken);
        authenticationEditor.apply();
    }


//    public static Preference getInstance() {
//        if (instance == null) {
//            instance = new Preference();
//            return instance;
//        }
//        return instance;
//    }

//    public Preference(Context applicationContext) {
//        this.applicationContext = applicationContext;
//        pref = applicationContext.getSharedPreferences(Constants.PREFERENCE, Context.MODE_PRIVATE);
//        editor = pref.edit();
//    }

    public static String getToken() {
        //SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
        return pref.getString("TOKEN", null);
    }

    public static String getUsername() {
        //SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
        return pref.getString("USERNAME", null);
    }

    public static String getPassword() {
        //SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
        return pref.getString("PASSWORD", null);
    }

    public static String getUserID() {
        //SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
        return pref.getString("ID", null);
    }

    public static void setToken(String token) {
        SharedPreferences.Editor authenticationEditor = pref.edit();
        authenticationEditor.putString("TOKEN", token);
        authenticationEditor.apply();
    }

    public static void setCompanyURL(String companyURL) {
        SharedPreferences.Editor authenticationEditor = pref.edit();
        authenticationEditor.putString("COMPANY_URL", companyURL);
        authenticationEditor.apply();
    }

    public static String getCompanyURL() {
        //SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
        return pref.getString("COMPANY_URL", null);
    }

    public static boolean isCrashReport() {
        //SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
        return pref.getBoolean("CRASH_REPORT", true);
    }

    public static void setCrashReport(boolean crashReport) {
        Preference.crashReport = crashReport;
    }

//    public static String getKeyDept() {
//        SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
//        return prefs.getString("KEYDEPT", null);
//    }
//
//    public static void setKeyDept(String keyDept) {
//        SharedPreferences.Editor authenticationEditor = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0).edit();
//        authenticationEditor.putString("KEYDEPT", keyDept);
//        authenticationEditor.apply();
//
//    }
//
//    public static String getValueDept() {
//        SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
//        return prefs.getString("VALUEDEPT", null);
//    }
//
//    public static void setValueDept(String valueDept) {
//        SharedPreferences.Editor authenticationEditor = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0).edit();
//        authenticationEditor.putString("VALUEDEPT", valueDept);
//        authenticationEditor.apply();
//
//    }
//
//    public static String getKeySLA() {
//        SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
//        return prefs.getString("KEYSLA", null);
//    }
//
//    public static void setKeySLA(String keySLA) {
//        SharedPreferences.Editor authenticationEditor = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0).edit();
//        authenticationEditor.putString("KEYSLA", keySLA);
//        authenticationEditor.apply();
//
//    }
//
//    public static String getKeyStaff() {
//        SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
//        return prefs.getString("KEYSTAFF", null);
//    }
//
//    public static void setKeyStaff(String keyStaff) {
//        SharedPreferences.Editor authenticationEditor = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0).edit();
//        authenticationEditor.putString("KEYSTAFF", keyStaff);
//        authenticationEditor.apply();
//
//    }
//
//    public static String getValueSLA() {
//        SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
//        return prefs.getString("VALUESLA", null);
//    }
//
//    public static void setValueSLA(String valueSLA) {
//        SharedPreferences.Editor authenticationEditor = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0).edit();
//        authenticationEditor.putString("VALUESLA", valueSLA);
//        authenticationEditor.apply();
//
//    }
//
//    public static String getValueStaff() {
//        SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
//        return prefs.getString("VALUESTAFF", null);
//    }
//
//    public static void setValueStaff(String valueStaff) {
//        SharedPreferences.Editor authenticationEditor = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0).edit();
//        authenticationEditor.putString("VALUESTAFF", valueStaff);
//        authenticationEditor.apply();
//
//    }
//
//    public static String getKeyTeam() {
//        SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
//        return prefs.getString("KEYTEAM", null);
//    }
//
//    public static void setKeyTeam(String keyTeam) {
//        SharedPreferences.Editor authenticationEditor = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0).edit();
//        authenticationEditor.putString("KEYTEAM", keyTeam);
//        authenticationEditor.apply();
//
//    }
//
//    public static String getValueTeam() {
//        SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
//        return prefs.getString("VALUETEAM", null);
//    }
//
//    public static void setValueTeam(String valueTeam) {
//        SharedPreferences.Editor authenticationEditor = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0).edit();
//        authenticationEditor.putString("VALUETEAM", valueTeam);
//        authenticationEditor.apply();
//
//    }
//
//    public static String getKeyPriority() {
//        SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
//        return prefs.getString("KEYPRIORITY", null);
//    }
//
//    public static void setKeyPriority(String keyPriority) {
//        SharedPreferences.Editor authenticationEditor = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0).edit();
//        authenticationEditor.putString("KEYPRIORITY", keyPriority);
//        authenticationEditor.apply();
//
//    }
//
//    public static String getValuePriority() {
//        SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
//        return prefs.getString("VALUEPRIORITY", null);
//    }
//
//    public static void setValuePriority(String valuePriority) {
//        SharedPreferences.Editor authenticationEditor = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0).edit();
//        authenticationEditor.putString("VALUEPRIORITY", valuePriority);
//        authenticationEditor.apply();
//
//    }


//    public static String getValueSource() {
//        SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
//        return prefs.getString("VALUESOURCE", null);
//    }
//
//    public static void setValueSource(String valueSource) {
//        SharedPreferences.Editor authenticationEditor = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0).edit();
//        authenticationEditor.putString("VALUESOURCE", valueSource);
//        authenticationEditor.apply();
//
//    }
//
//    public static String getKeySource() {
//        SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
//        return prefs.getString("KEYSOURCE", null);
//    }
//
//    public static void setKeySource(String keySource) {
//        SharedPreferences.Editor authenticationEditor = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0).edit();
//        authenticationEditor.putString("KEYSOURCE", keySource);
//        authenticationEditor.apply();
//
//    }
//
//    public static String getValueStatus() {
//        SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
//        return prefs.getString("VALUESTATUS", null);
//    }
//
//    public static void setValueStatus(String valueStatus) {
//        SharedPreferences.Editor authenticationEditor = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0).edit();
//        authenticationEditor.putString("VALUESTATUS", valueStatus);
//        authenticationEditor.apply();
//
//    }
//
//    public static String getKeyStatus() {
//        SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
//        return prefs.getString("KEYSTATUS", null);
//    }
//
//    public static void setKeyStatus(String keyStatus) {
//        SharedPreferences.Editor authenticationEditor = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0).edit();
//        authenticationEditor.putString("KEYSTATUS", keyStatus);
//        authenticationEditor.apply();
//
//    }
//
//    public static String getValueTopic() {
//        SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
//        return prefs.getString("VALUETOPIC", null);
//    }
//
//    public static void setValueTopic(String valueTopic) {
//        SharedPreferences.Editor authenticationEditor = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0).edit();
//        authenticationEditor.putString("VALUETOPIC", valueTopic);
//        authenticationEditor.apply();
//
//    }
//
//    public static String getKeyTopic() {
//        SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
//        return prefs.getString("KEYTOPIC", null);
//    }
//
//    public static void setKeyTopic(String keyTopic) {
//        SharedPreferences.Editor authenticationEditor = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0).edit();
//        authenticationEditor.putString("KEYTOPIC", keyTopic);
//        authenticationEditor.apply();
//
//    }
}
