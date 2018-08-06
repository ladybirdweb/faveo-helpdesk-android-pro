package co.helpdesk.faveo.pro.backend.api.v1;

import android.util.Log;

import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import co.helpdesk.faveo.pro.Constants;

/**
 * In this class we are basically setting the API
 * which we need to call ,so when ever we need to call that API we wil create the object for this class.
 * This class contains all of the API's which we have used in our application.
 */
public class Helpdesk {

    static String apiKey;
    public static String token;
    static String IP;
    String domain=Prefs.getString("domain",null);
    String newurl=Prefs.getString("companyUrl",null);
        //Log.d("newurl",newurl);
    public Helpdesk() {
        apiKey = Constants.API_KEY;
        token = Prefs.getString("TOKEN", "");
        //token = Preference.getToken();
        IP = null;
    }



    public String getBaseURL(String companyURL) {

        Log.d("checkingURL", companyURL + "api/v1/helpdesk/url?url=" + companyURL.substring(0, companyURL.length() - 1) + "&api_key=" + apiKey);
        Prefs.putString("companyurl",companyURL+"api/v2/helpdesk/");
        Log.d("companyUrl",Prefs.getString("companyurl",null));
        //Constants.URL1=Prefs.getString("companyurl",null);
        //Log.d("Constants.URL1",Constants.URL1);


        return new HTTPConnection().HTTPResponseGet(companyURL + "api/v1/helpdesk/url?url=" + companyURL.substring(0, companyURL.length() - 1) + "&api_key=" + apiKey);
    }

    public String postCreateTicket(int userID, String subject, String body, int helpTopic,
                                   int priority, String fname, String lname, String phone, String email, String code, int staff, String mobile) {
        Log.d("postCreateTicketAPI", Constants.URL + "helpdesk/create?" +
                "api_key=" + apiKey +
                "&token=" + token+
                "&ip=" + IP +
                "&user_id=" + userID +
                "&subject=" + subject +
                "&body=" + body +
                "&help_topic=" + helpTopic +
                // "&sla=" + sla +
                "&priority=" + priority +
                //"&dept=" + dept +
                "&first_name=" + fname +
                "&last_name=" + lname +
                "&email=" + email +
                "&assigned=" + staff+ "&phone=" + mobile +
                "&code=" + code +
                "&mobile=" + phone);
        Prefs.putString("createTicketApi",Constants.URL + "helpdesk/create?" +
                "api_key=" + apiKey +
                "&token=" + token+
                "&ip=" + IP +
                "&user_id=" + userID +
                "&subject=" + subject +
                "&body=" + body +
                "&help_topic=" + helpTopic +
                // "&sla=" + sla +
                "&priority=" + priority +
                //"&dept=" + dept +
                "&first_name=" + fname +
                "&last_name=" + lname +
                "&email=" + email +
                "&assigned=" + staff+ "&phone=" + mobile +
                "&code=" + code +
                "&mobile=" + phone);

        String result = new HTTPConnection().HTTPResponsePost(Constants.URL + "helpdesk/create?" +
                "api_key=" + apiKey +
                "&token=" + token+
                "&ip=" + IP +
                "&user_id=" + userID +
                "&subject=" + subject +
                "&body=" + body +
                "&help_topic=" + helpTopic +
                // "&sla=" + sla +
                "&priority=" + priority +
                // "&dept=" + dept +
                "&first_name=" + fname +
                "&last_name=" + lname +
                "&email=" + email +
                "&assigned=" + staff + "&phone=" + mobile +
                "&code=" + code +
                "&mobile=" + phone, null);

        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponsePost(Constants.URL + "helpdesk/create?" +
                    "api_key=" + apiKey +
                    "&token=" + token+
                    "&ip=" + IP +
                    "&user_id=" + userID +
                    "&subject=" + subject +
                    "&body=" + body +
                    "&help_topic=" + helpTopic +
                    // "&sla=" + sla +
                    "&priority=" + priority +
                    //  "&dept=" + dept +
                    "&first_name=" + fname +
                    "&last_name=" + lname+
                    "&email=" + email +
                    "&assigned=" + staff+ "&phone=" + mobile +
                    "&code=" + code +
                    "&mobile=" + phone, null);
        return result;
    }

//    public String postCreateTicket(int userID, String subject, String body, int helpTopic,
//                                   int priority,) {
//        Log.d("postCreateTicketAPI", Constants.URL + "helpdesk/create?" +
//                "&ip=" + IP +
//                "&subject=" + subject +
//                "&body=" + body +
//                "&user_id=" + userID +
//                "&helptopic=" + helpTopic +
//                "&sla=" + sla +
//                "&priority=" + priority +
//                "&dept=" + dept +
//                "&token=" + token);
//
//        String result = new HTTPConnection().HTTPResponsePost(Constants.URL + "helpdesk/create?" +
//                "&ip=" + IP +
//                "&subject=" + subject +
//                "&user_id=" + userID +
//                "&body=" + body +
//                "&helptopic=" + helpTopic +
//                "&sla=" + sla +
//                "&priority=" + priority +
//                "&dept=" + dept +
//                "&token=" + token, null);
//
//        if (result != null && result.equals("tokenRefreshed"))
//            return new HTTPConnection().HTTPResponsePost(Constants.URL + "helpdesk/create?" +
//                    "&ip=" + IP +
//                    "&subject=" + subject +
//                    "&user_id=" + userID +
//                    "&body=" + body +
//                    "&helptopic=" + helpTopic +
//                    "&sla=" + sla +
//                    "&priority=" + priority +
//                    "&dept=" + dept +
//                    "&token=" + token, null);
//        return result;
//    }

    public String postCreateInternalNote(int ticketID, int userID, String note) {
        Log.d("CreateInternalNoteAPI", Constants.URL + "helpdesk/internal-note?" +
                "api_key=" + apiKey +
                "&ip=" + IP +
                "&token=" + token +
                "&ticket_id=" + ticketID +
                "&user_id=" + userID +
                "&body=" + note);
        String result = new HTTPConnection().HTTPResponsePost(Constants.URL + "helpdesk/internal-note?" +
                "api_key=" + apiKey +
                "&ip=" + IP +
                "&token=" + token +
                "&ticket_id=" + ticketID +
                "&user_id=" + userID +
                "&body=" + note, null);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponsePost(Constants.URL + "helpdesk/internal-note?" +
                    "api_key=" + apiKey +
                    "&ip=" + IP +
                    "&token=" + token +
                    "&ticket_id=" + ticketID +
                    "&user_id=" + userID +
                    "&body=" + note, null);
        return result;
    }

    public String postReplyTicket(int ticketID, String replyContent) {
        Log.d("ReplyTicketAPI", Constants.URL + "helpdesk/reply?" +
                "api_key=" + apiKey +
                "&ip=" + IP +
                "&token=" + token +
                "&ticket_id=" + ticketID +
                "&reply_content=" + replyContent);
        String result = new HTTPConnection().HTTPResponsePost(Constants.URL + "helpdesk/reply?" +
                        "api_key=" + apiKey +
                        "&ip=" + IP +
                        "&token=" + token +
                        "&ticket_id=" + ticketID +
                        "&reply_content=" + replyContent,
                null);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponsePost(Constants.URL + "helpdesk/reply?" +
                    "api_key=" + apiKey +
                    "&ip=" + IP +
                    "&token=" + token +
                    "&ticket_id=" + ticketID +
                    "&reply_content=" + replyContent, null);
        return result;
    }


    public String postEditTicket(int ticketID, String subject, int helpTopic,
                                 int ticketSource, int ticketPriority, int ticketType,int staff) {
        Log.d("EditTicketAPI", Constants.URL + "helpdesk/edit?" +
                "api_key=" + apiKey +
                "&ip=" + IP +
                "&token=" + token +
                "&ticket_id=" + ticketID +
                "&subject=" + subject +
                "&help_topic=" + helpTopic +
                "&ticket_source=" + ticketSource +
                "&ticket_priority=" + ticketPriority +
                "&ticket_type=" + ticketType + "&assigned="
                + staff
        );
        String result = new HTTPConnection().HTTPResponsePost(Constants.URL + "helpdesk/edit?" +
                "api_key=" + apiKey +
                "&ip=" + IP +
                "&token=" + token +
                "&ticket_id=" + ticketID +
                "&subject=" + subject +
                "&help_topic=" + helpTopic +
                "&ticket_source=" + ticketSource +
                "&ticket_priority=" + ticketPriority +
                "&ticket_type=" + ticketType + "&assigned="
                + staff, null);

        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponsePost(Constants.URL + "helpdesk/edit?" +
                    "api_key=" + apiKey +
                    "&ip=" + IP +
                    "&token=" + token +
                    "&ticket_id=" + ticketID +
                    "&subject=" + subject +
                    "&help_topic=" + helpTopic +
                    "&ticket_source=" + ticketSource +
                    "&ticket_priority=" + ticketPriority +
                    "&ticket_type=" + ticketType + "&assigned="
                    + staff, null);
        return result;
    }

    public String postFCMToken(String token, String ID) {
        Log.d("FCM token beforesending", token + "");
        String parameters = null;
        JSONObject obj = new JSONObject();
        try {
            obj.put("fcm_token", token);
            obj.put("user_id", ID);
            parameters = obj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("fcm call", Constants.URL + "fcmtoken?");
        return new HTTPConnection().HTTPResponsePost(Constants.URL + "fcmtoken?", parameters);
    }

    public String getCheckBillingURL(String baseURL) {
        Log.d("getBillingURL", Constants.BILLING_URL + "?url=" + baseURL);
        return new HTTPConnection().HTTPResponseGet(Constants.BILLING_URL + "?url=" + baseURL);
    }

//    public String postDeleteTicket(int ticketID) {
//        String parameters = null;
//        JSONObject obj = new JSONObject();
//        try {
//            obj.put("api_key", apiKey);
//            obj.put("ip", IP);
//            obj.put("ticket_ID", ticketID);
//            obj.put("token", token);
//            parameters = obj.toString();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return new HTTPConnection().HTTPResponsePost(Constants.URL + "helpdesk/delete?", parameters);
//    }

//    public String getOpenTicket() {
//        String parameters = null;
//        JSONObject obj = new JSONObject();
//        try {
//            obj.put("api_key", apiKey);
//            obj.put("ip", IP);
//            obj.put("token", token);
//            parameters = obj.toString();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/open?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
//    }

    public String getUnassignedTicket(int page) {
        Log.d("UnassignedTicketAPI",newurl + "api/v2/helpdesk/get-tickets?token=" + token + "&api=1&show=inbox&departments=all&page="+page+"&assigned=0");
        String result=new HTTPConnection().HTTPResponseGet(newurl + "api/v2/helpdesk/get-tickets?token=" + token + "&api=1&show=inbox&departments=all&page="+page+"&assigned=0");
        if (result!=null&&result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(newurl + "api/v2/helpdesk/get-tickets?token=" + token + "&api=1&show=inbox&departments=all&page="+page+"&assigned=0");
        //Log.d("URL",result);
        return result;
    }

    public String getClosedTicket(int page) {
        Log.d("ClosedTicketAPI", newurl + "api/v2/helpdesk/get-tickets?token=" + token + "&api=1&show=closed&departments=all&page="+page);
        String result = new HTTPConnection().HTTPResponseGet(newurl + "api/v2/helpdesk/get-tickets?token=" + token + "&api=1&show=closed&departments=all&page="+page);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(newurl + "api/v2/helpdesk/get-tickets?token=" + token + "&api=1&show=closed&departments=all&page="+page);
        return result;
    }


//    public String getAgents() {
//        String parameters = null;
//        JSONObject obj = new JSONObject();
//        try {
//            obj.put("api_key", apiKey);
//            obj.put("ip", IP);
//            obj.put("token", token);
//            parameters = obj.toString();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/agents?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
//    }

//    public String getTeams() {
//        String parameters = null;
//        JSONObject obj = new JSONObject();
//        try {
//            obj.put("api_key", apiKey);
//            obj.put("ip", IP);
//            obj.put("token", token);
//            parameters = obj.toString();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/teams?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
//    }

//    public String getCustomers(String search) {
//        String parameters = null;
//        JSONObject obj = new JSONObject();
//        try {
//            obj.put("api_key", apiKey);
//            obj.put("ip", IP);
//            obj.put("token", token);
//            obj.put("search", search);
//            parameters = obj.toString();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        String result = new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/customers?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&search=" + search);
//        if (result != null && result.equals("tokenRefreshed"))
//            return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/customers?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&search=" + search);
//        return result;
//    }

    public String getCustomersOverview() {
        Log.d("CustomersOverviewAPI", Constants.URL + "helpdesk/customers-custom?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        String result = new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/customers-custom?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/customers-custom?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        return result;
    }

//    public String getCustomer(int userID) {
//        String parameters = null;
//        JSONObject obj = new JSONObject();
//        try {
//            obj.put("api_key", apiKey);
//            obj.put("ip", IP);
//            obj.put("token", token);
//            obj.put("user_id", userID);
//            parameters = obj.toString();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/customer?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&user_id=" + userID);
//    }

//    public String getTicket(String search) {
//        String parameters = null;
//        JSONObject obj = new JSONObject();
//        try {
//            obj.put("api_key", apiKey);
//            obj.put("ip", IP);
//            obj.put("token", token);
//            obj.put("search", search);
//            parameters = obj.toString();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/ticket-search?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&search=" + search);
//    }

    public String getTicketDetail(String ticketID) {
        Log.d("TicketDetailAPI", Constants.URL + "helpdesk/ticket?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&id=" + ticketID);
        String result = new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/ticket?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&id=" + ticketID);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/ticket?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&id=" + ticketID);
        return result;
    }

    public String getTicketThread(String ticketID) {
        Log.d("TicketThreadAPI", Constants.URL + "helpdesk/ticket-thread?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&id=" + ticketID);
        String result = new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/ticket-thread?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&id=" + ticketID);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/ticket-thread?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&id=" + ticketID);
        return result;
    }

//    public String postAssignTicket(int id) {
//        String parameters = null;
//        JSONObject obj = new JSONObject();
//        try {
//            obj.put("api_key", apiKey);
//            obj.put("ip", IP);
//            obj.put("token", token);
//            obj.put("id", id);
//            parameters = obj.toString();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return new HTTPConnection().HTTPResponsePost(Constants.URL + "helpdesk/assign", parameters);
//    }

    public String getInboxTicket(int page) {

        Log.d("Inboxapi",newurl + "api/v2/helpdesk/get-tickets?token=" + token + "&api=1&show=inbox&departments=all&page="+page);
        String result=new HTTPConnection().HTTPResponseGet(newurl + "api/v2/helpdesk/get-tickets?token=" + token + "&api=1&show=inbox&departments=all&page="+page);
        if (result!=null&&result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(newurl + "api/v2/helpdesk/get-tickets?token=" + token + "&api=1&show=inbox&departments=all&page="+page);
//        Log.d("URL",result);
        return result;
    }

    // helpdesk/get-tickets?departments=All&show=inbox&api=1

//    public String getInboxTicket2() {
//        Log.d("InboxTicketAPI", Constants.URL + "helpdesk/get-tickets?token=" + token + "&api=1&departments=All&show=inbox");
//        String result = new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/get-tickets?token=" + token + "&api=1&departments=All&show=inbox");
//        if (result != null && result.equals("tokenRefreshed"))
//            return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/get-tickets?token=" + token + "&api=1&departments=All&show=inbox");
//        return result;
//    }

    public String getTrashTickets(int page) {
        Log.d("TrashTicket",newurl + "api/v2/helpdesk/get-tickets?token=" + token + "&api=1&show=trash&departments=all&page="+page);
        String result=new HTTPConnection().HTTPResponseGet(newurl + "api/v2/helpdesk/get-tickets?token=" + token + "&api=1&show=trash&departments=all&page="+page);
        if (result!=null&&result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(newurl + "api/v2/helpdesk/get-tickets?token=" + token + "&api=1&show=trash&departments=all&page="+page);
//        Log.d("URL",result);
        return result;
    }

    public String nextPageURL(String URL) {
        //Log.d("URL",URL);
        // int lastSlash = URL.lastIndexOf("/");
        // URL = URL.substring(0, lastSlash) + URL.substring(lastSlash + 1, URL.length());
        Log.d("nextPageURLAPI", URL + "&api_key=" + apiKey + "&token=" + token);
        String result = new HTTPConnection().HTTPResponseGet(URL + "&api_key=" + apiKey + "&token=" + token);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(URL + "&api_key=" + apiKey + "&token=" + token);
        return result;
    }

    public String nextpageurl(String show,int page)
    {
        Log.d("Inboxapi",newurl + "api/v2/helpdesk/get-tickets?token=" + token + "&api=1&show="+show+"&departments=all&page="+page);
        String result=new HTTPConnection().HTTPResponseGet(newurl + "api/v2/helpdesk/get-tickets?token=" + token + "&api=1&show="+show+"&departments=all&page="+page);
        if (result!=null&&result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(newurl + "api/v2/helpdesk/get-tickets?token=" + token + "&api=1&show="+show+"&departments=all&page="+page);
        //Log.d("URL",result);
        return result;

    }



    public String nextPageURL(String URL, String userID) {

        Log.d("nextPageURLAPI", URL + "&api_key=" + apiKey + "&token=" + token + "&user_id=" + userID);
        String result = new HTTPConnection().HTTPResponseGet(URL + "&api_key=" + apiKey + "&token=" + token + "&user_id=" + userID);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(URL + "&api_key=" + apiKey + "&token=" + token + "&user_id=" + userID);
        return result;
    }
    public String nextPageSorting(String show,String term,String order,int page){
        Log.d("TicketTitleSOrtingAPI", newurl + "api/v2/helpdesk/get-tickets?token=" +token +"&show="+ show+"&departments=All&api=1&sort-by="+term+"&order="+order+"&page="+page);
        String result=new HTTPConnection().HTTPResponseGet(newurl + "api/v2/helpdesk/get-tickets?token=" +token +"&show="+ show+"&departments=All&api=1&sort-by="+term+"&order="+order+"&page="+page);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(newurl + "api/v2/helpdesk/get-tickets?token=" +token +"&show="+ show+"&departments=All&api=1&sort-by="+term+"&order="+order+"&page="+page);
        return result;
    }




//    public String getMyTickets(String userID) {
//        Log.d("MyTicketsAPI", Constants.URL + "helpdesk/my-tickets?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&user_id=" + userID);
//        String result = new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/my-tickets?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&user_id=" + userID);
//        if (result != null && result.equals("tokenRefreshed"))
//            return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/my-tickets?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&user_id=" + userID);
//        return result;
//    }

    public String getTicketsByAgent(int page) {
        Log.d("MYticketAPI",newurl + "api/v2/helpdesk/get-tickets?token=" + token + "&api=1&show=mytickets&departments=all&page="+page);
        String result=new HTTPConnection().HTTPResponseGet(newurl + "api/v2/helpdesk/get-tickets?token=" + token + "&api=1&show=mytickets&departments=all&page="+page);
        if (result!=null&&result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(newurl + "api/v2/helpdesk/get-tickets?token=" + token + "&api=1&show=mytickets&departments=all&page="+page);
//        Log.d("URL",result);
        return result;
    }



    public String getTicketsByUser(String userID) {
        Log.d("TicketsByUserAPI", Constants.URL + "helpdesk/my-tickets-user?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&user_id=" + userID);
        String result = new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/my-tickets-user?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&user_id=" + userID);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/my-tickets-user?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&user_id=" + userID);
        return result;
    }

    public String getDependency() {
        Log.d("DependencyAPI", Constants.URL + "helpdesk/dependency?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        String result = new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/dependency?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        if (result != null && result.equals("tokenRefreshed")) {
            return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/dependency?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        }
        return result;
    }
    public String postSeenNotifications(int ticketID) {
        Log.d("Noti-seenAPI", Constants.URL + "helpdesk/notifications-seen?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&=notification_id" + ticketID);
        String result = new HTTPConnection().HTTPResponsePost(Constants.URL + "helpdesk/notifications-seen?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&notification_id=" + ticketID, null);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponsePost(Constants.URL + "helpdesk/notifications-seen?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&notification_id=" + ticketID, null);
        return result;
    }
    public String postStatusChanged(int ticketID,int statusID){
        Log.d("StatusChangedApi", newurl + "api/v2/helpdesk/status/change?api_key=" + apiKey + "&token=" + token + "&ticket_id=" + ticketID + "&status_id=" + statusID);
        String result = new HTTPConnection().HTTPResponsePost(newurl + "api/v2/helpdesk/status/change?api_key=" + apiKey + "&token=" + token + "&ticket_id=" + ticketID + "&status_id=" + statusID, null);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponsePost(newurl + "api/v2/helpdesk/status/change?api_key=" + apiKey + "&token=" + token + "&ticket_id=" + ticketID + "&status_id=" + statusID, null);
        return result;
    }
    public String postStatusChangedMultiple(String ticketID,int statusID){
        Log.d("StatusChangedApi", newurl + "api/v2/helpdesk/status/change?api_key=" + apiKey + "&token=" + token + "&ticket_id=" + ticketID + "&status_id=" + statusID);
        String result = new HTTPConnection().HTTPResponsePost(newurl + "api/v2/helpdesk/status/change?api_key=" + apiKey + "&token=" + token + "&ticket_id=" + ticketID + "&status_id=" + statusID, null);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponsePost(newurl + "api/v2/helpdesk/status/change?api_key=" + apiKey + "&token=" + token + "&ticket_id=" + ticketID + "&status_id=" + statusID, null);
        return result;
    }
    public String getNotifications() {
        Log.d("NotificationsAPI", Constants.URL + "helpdesk/notifications?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        String result = new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/notifications?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/notifications?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        return result;
    }
    //http://192.168.0.192/api/v2/helpdesk/get-tickets?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjEsImlzcyI6Imh0dHA6Ly8xOTIuMTY4LjAuMTkyL2FwaS92MS9hdXRoZW50aWNhdGUiLCJpYXQiOjE1MDU5OTIxMjAsImV4cCI6MTUwNTk5MjM2MCwibmJmIjoxNTA1OTkyMTIwLCJqdGkiOiJFaWNWS3dvaTlRREVRMmQ5In0.uegtoFmzzZyZI-SlGWHeEqfv1nMqO7uxo4VYJ3CFCRk&show=inbox&departments=All&api=1&sort-by=ticket_title&order=ASC
    public String getSortByTicketWithTitle(String show,String title,String order){
        Log.d("TicketTitleSOrtingAPI", newurl + "api/v2/helpdesk/get-tickets?token=" +token +"&show=" +show +"&departments=All&api=1&sort-by=" + title +"&order=" + order);
        String result=new HTTPConnection().HTTPResponseGet(newurl + "api/v2/helpdesk/get-tickets?token=" +token +"&show=" +show +"&departments=All&api=1&sort-by=" + title +"&order=" + order);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(newurl + "api/v2/helpdesk/get-tickets?token=" +token +"&show=" +show +"&departments=All&api=1&sort-by=" + title +"&order=" + order);
        return result;
    }
//    public String getSortByTicketWithTicketNumber(String show,String ticketnumber,String order){
//        Log.d("TicketTitleSOrtingAPI", "http://" + Constants.URL1 + "/api/v2/helpdesk/get-tickets?token=" +token +"&show=" +show +"&departments=All&api=1&sort-by=" + ticketnumber +"&order=" + order);
//        String result=new HTTPConnection().HTTPResponseGet("http://" + Constants.URL1 + "/api/v2/helpdesk/get-tickets?token=" +token +"&show=" +show +"&departments=All&api=1&sort-by=" + ticketnumber +"&order=" + order);
//        if (result != null && result.equals("tokenRefreshed"))
//            return new HTTPConnection().HTTPResponseGet("http://" + Constants.URL1 + "/api/v2/helpdesk/get-tickets?token=" +token +"&show=" +show +"&departments=All&api=1&sort-by=" + ticketnumber +"&order=" + order);
//        return result;
//    }

    public String postRegisterUser(String email,String firstname,String lastname,String mobile,String company){
        Log.d("RegisterUser", Constants.URL + "helpdesk/register?token=" +token +"&email=" +email +"&first_name=" +firstname +"&last_name="+ lastname+ "&mobile="+ mobile +"&company=" +company);
        String result=new HTTPConnection().HTTPResponsePost(Constants.URL + "helpdesk/register?token=" +token +"&email=" +email +"&first_name=" +firstname + "&last_name="+ lastname+ "&mobile="+ mobile + "&company=" +company,null);
        if (result!=null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponsePost(Constants.URL + "helpdesk/register?token=" +token +"&email=" +email +"&first_name=" +firstname + "&last_name="+ lastname+ "&mobile="+ mobile + "&company=" +company,null);
        return result;
    }
    public String postCreateUser(String email,String firstname,String lastname){
        Log.d("RegisterUser", Constants.URL + "helpdesk/register?token=" +token +"&email=" +email +"&first_name=" +firstname +"&last_name="+ lastname);
        String result=new HTTPConnection().HTTPResponsePost(Constants.URL +"helpdesk/register?token="+ token +"&email="+email+"&first_name=" +firstname + "&last_name="+ lastname,null);
        if (result!=null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponsePost(Constants.URL + "helpdesk/register?token=" +token +"&email=" +email +"&first_name=" +firstname + "&last_name="+ lastname,null);
        return result;
    }
    public String postCollaboratorAssociatedWithTicket(String ticketid){
        Log.d("CollaboratorFetch",Constants.URL + "helpdesk/collaborator/get-ticket?token=" +token +"&ticket_id="+ticketid);
        String result=new HTTPConnection().HTTPResponsePost(Constants.URL + "helpdesk/collaborator/get-ticket?token=" +token +"&ticket_id="+ticketid,null);
        if (result!=null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponsePost(Constants.URL + "helpdesk/collaborator/get-ticket?token=" +token +"&ticket_id="+ticketid,null);
        return result;
    }
    public String getUser(String term){
        Log.d("getUserApi",Constants.URL +"helpdesk/collaborator/search?token=" +token + "&term="+ term);
        String result=new HTTPConnection().HTTPResponseGet(Constants.URL +"helpdesk/collaborator/search?token=" +token + "&term="+ term);
        if (result!=null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(Constants.URL +"helpdesk/collaborator/search?token=" +token + "&term="+ term);
        return result;
    }
    public String createCollaborator(String ticketid,String userid){
        Log.d("CollaboratorCreate",Constants.URL + "helpdesk/collaborator/create?token="+ token +"&ticket_id=" +ticketid +"&user_id=" +userid);
        String result=new HTTPConnection().HTTPResponsePost(Constants.URL+ "helpdesk/collaborator/create?token="+ token +"&ticket_id=" +ticketid +"&user_id=" +userid,null );
        if (result!=null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponsePost(Constants.URL+ "helpdesk/collaborator/create?token="+ token +"&ticket_id=" +ticketid +"&user_id=" +userid,null );
        return result;
    }
    public String removeCollaborator(String ticketid,String email){
        Log.d("CollaboratorRemove",Constants.URL + "helpdesk/collaborator/remove?token="+ token +"&ticket_id=" +ticketid +"&email=" +email);
        String result=new HTTPConnection().HTTPResponsePost(Constants.URL+ "helpdesk/collaborator/remove?token="+ token +"&ticket_id=" +ticketid +"&email=" +email,null);
        if (result!=null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponsePost(Constants.URL+ "helpdesk/collaborator/create?token="+ token +"&ticket_id=" +ticketid +"&email=" +email,null);
        return result;
    }
    //    public String ticketFiltration(String show,String department,String priority,String type,String source,String assignedto,int assign){
//        Log.d("ticketFiltrationApi","http://"+Constants.URL1 + "/api/v2/helpdesk/get-tickets?token=" + token + "&api=1&show=" +show + "&departments=" + department +"&priority=" + priority +"&type=" +type +"&source=" +source +"&assigned-to=a-" +assignedto +"&assigned="+assign);
//        String result=new HTTPConnection().HTTPResponseGet("http://"+Constants.URL1 + "/api/v2/helpdesk/get-tickets?token=" + token + "&api=1&show=" +show + "&departments=" + department +"&priority=" + priority +"&type=" +type +"&source=" +source +"&assigned-to=a-" +assignedto +"&assigned="+assign);
//        if (result!=null&&result.equals("tokenRefreshed"))
//            return new HTTPConnection().HTTPResponseGet("http://"+Constants.URL1 + "/api/v2/helpdesk/get-tickets?token=" + token + "&api=1&show=" +show + "&departments=" + department +"&priority=" + priority +"&type=" +type +"&source=" +source +"&assigned-to=a-" +assignedto +"&assigned="+assign);
//        return result;
//    }
    public String ticketFiltration(String url,int page){
        Log.d("ticketFiltrationApi",newurl + "api/v2/helpdesk/get-tickets?token=" + token + "&api=1&"+url+"&page="+page);
        String result=new HTTPConnection().HTTPResponseGet(newurl + "api/v2/helpdesk/get-tickets?token=" + token + "&api=1&"+url+"&page="+page);
        if (result!=null&&result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(newurl + "api/v2/helpdesk/get-tickets?token=" + token + "&api=1&"+url+"&page="+page);
        //Log.d("URL",result);

        return result;
    }

    public String nextPageUrlFilter(String url,int page){
        Log.d("ticketFiltrationApi",newurl + "api/v2/helpdesk/get-tickets?token=" + token + "&api=1&"+url+"&page="+page);
        String result=new HTTPConnection().HTTPResponseGet(newurl + "api/v2/helpdesk/get-tickets?token=" + token + "&api=1&"+url+"&page="+page);
        if (result!=null&&result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(domain+Constants.URL1 + "api/v2/helpdesk/get-tickets?token=" + token + "&api=1&"+url+"&page="+page);
        return result;
    }

    public String customerFiltration(int page,String url){
        Log.d("customerFiltration",newurl + "api/v2/helpdesk/user/filter?token=" + token + "&api=1&"+url+"&page="+page);
        String result=new HTTPConnection().HTTPResponseGet(newurl + "api/v2/helpdesk/user/filter?token=" + token + "&api=1&"+url+"&page="+page);
        if (result!=null&&result.equals("tokenRefreshed"))
            return  new HTTPConnection().HTTPResponseGet(newurl + "api/v2/helpdesk/user/filter?token=" + token + "&api=1&"+url+"&page="+page);
        return result;
    }
    public String nextPagecustomerFiltration(int page,String url){
        Log.d("customerFiltration",newurl + "api/v2/helpdesk/user/filter?token=" + token + "&api=1&"+url+"&page="+page);
        String result=new HTTPConnection().HTTPResponseGet(newurl + "api/v2/helpdesk/user/filter?token=" + token + "&api=1&"+url+"&page="+page);
        if (result!=null&&result.equals("tokenRefreshed"))
            return  new HTTPConnection().HTTPResponseGet(newurl + "api/v2/helpdesk/user/filter?token=" + token + "&api=1&"+url+"&page="+page);
        return result;
    }

    public String saveCustomerDetails(String userid,String firstname, String lastname,
                                      String email,String username){
        Log.d("editCustomerApi",newurl + "api/v2/helpdesk/user-edit/"+userid+"?api_key=" + apiKey+"&token="+token+"&first_name="+firstname+"&last_name="+lastname+"&email="+email+"&user_name="+username);
        String result=new HTTPConnection().HTTPResponsePatch(newurl + "api/v2/helpdesk/user-edit/"+userid+"?api_key=" + apiKey+"&token="+token+"&first_name="+firstname+"&last_name="+lastname+"&email="+email+"&user_name="+username,null);
        if (result!=null&&result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponsePatch(newurl + "api/v2/helpdesk/user-edit/"+userid+"?api_key=" + apiKey+"&token="+token+"&first_name="+firstname+"&last_name="+lastname+"&email="+email+"&user_name="+username,null);
        return result;

    }
    public String changeStatusUser(String userid,int status){
        Log.d("changeStatusOfUserAPI",newurl + "api/v2/helpdesk/user/status/"+userid+"?api_key=" + apiKey+"&token="+token+"&status="+status);
        String result=new HTTPConnection().HTTPResponsePatch(newurl + "api/v2/helpdesk/user/status/"+userid+"?api_key=" + apiKey+"&token="+token+"&status="+status,null);
        if (result!=null&&result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponsePatch(newurl + "api/v2/helpdesk/user/status/"+userid+"?api_key=" + apiKey+"&token="+token+"&status="+status,null);
        return result;
    }

    public String mergeTicket(int parentId,String title,String reason){
        Log.d("mergeTicket",newurl + "api/v2/helpdesk/merge/?api_key=" + apiKey+"&token="+token+"&p_id="+parentId+"&title="+title+"&reason="+reason);
        String result=new HTTPConnection().HTTPResponsePost(newurl + "api/v2/helpdesk/merge/?api_key=" + apiKey+"&token="+token+"&p_id="+parentId+"&title="+title+"&reason="+reason,null);
        if (result!=null&&result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponsePost(newurl + "api/v2/helpdesk/merge/?api_key=" + apiKey+"&token="+token+"&p_id="+parentId+"&title="+title+"&reason="+reason,null);
        return result;
    }

    public String searchQuerry(String querry){
        Log.d("SearchApi",Constants.URL + "helpdesk/ticket-search?token="+ token +"&search=" +querry);
        String result=new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/ticket-search?token="+ token +"&search=" +querry);
        if (result!=null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/ticket-search?token="+ token +"&search=" +querry);
        return result;

    }
    public String nextPageURLForSearching(String URL,String querry) {
        //Log.d("URL",URL);
        // int lastSlash = URL.lastIndexOf("/");
        // URL = URL.substring(0, lastSlash) + URL.substring(lastSlash + 1, URL.length());
        Log.d("nextPageURLSearching", URL + "&api_key=" + apiKey + "&token=" + token+"&search="+querry);
        String result = new HTTPConnection().HTTPResponseGet(URL + "&api_key=" + apiKey + "&token=" + token+"&search="+querry);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(URL + "&api_key=" + apiKey + "&token=" + token+"&search="+querry);
        return result;
    }

    public String ticketDeleteForever(String ticketId){
        Log.d("ticketDeleteApi",newurl + "api/v2/helpdesk/ticket/delete?api_key="+apiKey+"&token="+token+ticketId);
        String result=new HTTPConnection().HTTPResponsePost(newurl + "api/v2/helpdesk/ticket/delete?api_key="+apiKey+"&token="+token+ticketId,null);
        if (result!=null&&result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponsePost(newurl + "api/v2/helpdesk/ticket/delete?api_key="+apiKey+"&token="+token+ticketId,null);
        return result;

    }

    public String multipleTicketAssign(String ticketid,String assignid){
        Log.d("multiAssignApi",newurl + "api/v2/helpdesk/ticket/assign?api_key="+apiKey+"&token="+token+"&id[]="+ticketid+"&assign_id="+assignid);
        String result=new HTTPConnection().HTTPResponsePost(newurl + "api/v2/helpdesk/ticket/assign?api_key="+apiKey+"&token="+token+"&id[]="+ticketid+"&assign_id="+assignid,null);
        if (result!=null&&result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponsePost(newurl + "api/v2/helpdesk/ticket/assign?api_key="+apiKey+"&token="+token+"&id[]="+ticketid+"&assign_id="+assignid,null);
        return result;

    }
        public String customerFeedback(String subject,String message){
        Log.d("customerFeedback",Constants.URL + "helpdesk/helpsection/mails?token="+token+"&help_email=faveoservicedesk@gmail.com&help_subject="+subject+"&help_massage="+message);
        String result=new HTTPConnection().HTTPResponsePost(Constants.URL + "helpdesk/helpsection/mails?token="+token+"&help_email=faveoservicedesk@gmail.com&help_subject="+subject+"&help_massage="+message,null);
        if (result!=null&&result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponsePost(Constants.URL + "helpdesk/helpsection/mails?token="+token+"&help_email=faveoservicedesk@gmail.com&help_subject="+subject+"&help_massage="+message,null);
            return result;
        }


}
