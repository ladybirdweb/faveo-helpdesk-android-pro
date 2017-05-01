package co.helpdesk.faveo.pro;

import android.content.Context;
import android.content.SharedPreferences;

import org.jetbrains.annotations.Contract;

public class Preference {

//    private static Context applicationContext;
//    // private static SharedPreferences pref;
//    //private SharedPreferences.Editor editor;
//
//    private static boolean crashReport;
//
////    private static Preference instance;
//
////    private Preference() {
////    }
//
////    public static void setDependencyObject(JSONObject modal, String key) {
////
////        SharedPreferences.Editor prefsEditor = pref.edit();
//////        Gson gson = new Gson();
//////        String jsonObject = gson.toJson(modal);
////        prefsEditor.putString(key, modal.toString());
////        prefsEditor.apply();
////
////    }
////
////    public static JSONObject getDependencyObject(String key) throws JSONException {
////
////        String json = pref.getString(key, null);
////        //        Gson gson = new Gson();
//////        Dependency selectedUser = gson.fromJson(json, Dependency.class);
////        return new JSONObject(json);
////    }
//
////    public static synchronized void setInstance(Context context) {
////        if (pref == null)
////            pref = context.getSharedPreferences("faveo", Context.MODE_PRIVATE);
////    }
//
//    public static String getFCMtoken() {
//        SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
//        return prefs.getString("FCMtoken", null);
//    }
//
//    public static void setFCMtoken(String FCMtoken) {
//        SharedPreferences.Editor authenticationEditor = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0).edit();
//        authenticationEditor.putString("FCMtoken", FCMtoken);
//        authenticationEditor.apply();
//    }
//
////    public static Preference getInstance() {
////        if (instance == null) {
////            instance = new Preference();
////            return instance;
////        }
////        return instance;
////    }
//
//    public Preference(Context applicationContext) {
//        Preference.applicationContext = applicationContext;
////        pref = applicationContext.getSharedPreferences(Constants.PREFERENCE, Context.MODE_PRIVATE);
////        editor = pref.edit();
//    }
//
////    public static String getToken() {
////        SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
////        return prefs.getString("TOKEN", null);
////    }
//
//    public static String getUsername() {
//        SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
//        return prefs.getString("USERNAME", null);
//    }
//
//    public static String getPassword() {
//        SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
//        return prefs.getString("PASSWORD", null);
//    }
//
//    public static String getUserID() {
//        SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
//        return prefs.getString("ID", null);
//    }
//
////    public static void setToken(String token) {
////        SharedPreferences.Editor authenticationEditor = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0).edit();
////        authenticationEditor.putString("TOKEN", token);
////        authenticationEditor.apply();
////    }
//
//    public static void setCompanyURL(String companyURL) {
//        SharedPreferences.Editor authenticationEditor = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0).edit();
//        authenticationEditor.putString("COMPANY_URL", companyURL);
//        authenticationEditor.apply();
//    }
//
//    public static String getCompanyURL() {
//        SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
//        return prefs.getString("COMPANY_URL", null);
//    }
//
//    public static boolean isCrashReport() {
//        SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
//        return prefs.getBoolean("CRASH_REPORT", true);
//    }
//
//    public static void setCrashReport(boolean crashReport) {
//        Preference.crashReport = crashReport;
//    }
//
//    @Contract(" -> !null")
//    public static String getInboxTickets() {
//        SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
//        return prefs.getString("inboxTickets", "-");
//    }
//
//    public static void setInboxTickets(String inboxTickets) {
//        SharedPreferences.Editor authenticationEditor = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0).edit();
//        authenticationEditor.putString("inboxTickets", inboxTickets);
//        authenticationEditor.apply();
//    }
//
//    @Contract(" -> !null")
//    public static String getMyTickets() {
//        SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
//        return prefs.getString("myTickets", "-");
//    }
//
//    public static void setMyTickets(String myTickets) {
//        SharedPreferences.Editor authenticationEditor = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0).edit();
//        authenticationEditor.putString("myTickets", myTickets);
//        authenticationEditor.apply();
//    }
//
//    @Contract(" -> !null")
//    public static String getClosedTickets() {
//        SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
//        return prefs.getString("closedTickets", "-");
//    }
//
//    public static void setClosedTickets(String closedTickets) {
//        SharedPreferences.Editor authenticationEditor = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0).edit();
//        authenticationEditor.putString("closedTickets", closedTickets);
//        authenticationEditor.apply();
//    }
//
//    @Contract(" -> !null")
//    public static String getUnassignedTickets() {
//        SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
//        return prefs.getString("unassignedTickets", "-");
//    }
//
//    public static void setUnassignedTickets(String unassignedTickets) {
//        SharedPreferences.Editor authenticationEditor = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0).edit();
//        authenticationEditor.putString("unassignedTickets", unassignedTickets);
//        authenticationEditor.apply();
//    }
//
//    @Contract(" -> !null")
//    public static String getTrashTickets() {
//        SharedPreferences prefs = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0);
//        return prefs.getString("trashTickets", "-");
//    }
//
//    public static void setTrashTickets(String trashTickets) {
//        SharedPreferences.Editor authenticationEditor = applicationContext.getSharedPreferences(Constants.PREFERENCE, 0).edit();
//        authenticationEditor.putString("trashTickets", trashTickets);
//        authenticationEditor.apply();
//    }
//
////    public static void clearAll() {
////
////    }
}
