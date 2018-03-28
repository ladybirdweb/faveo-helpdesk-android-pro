package co.helpdesk.faveo.pro;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import co.helpdesk.faveo.pro.model.ClientOverview;
import co.helpdesk.faveo.pro.model.NotificationThread;
import co.helpdesk.faveo.pro.model.TicketOverview;


/**
 * This helper class is responsible for parsing ticket,client and for the notifications.
 * Here we are doing the JSON parsing for the particular model class.After
 * getting the json data we are creating object for that model class.
 */
public class Helper {
    /**
     * Tickets Page.
     * @param jsonArray refers to the array of JSON elements.
     * @param i position of the element in array.
     * @return object for ticket overview.
     */
    public static TicketOverview parseTicketOverview(JSONArray jsonArray, int i) {
        String agentName;
        try {
            //Date updated_at = null;

            String firstName = jsonArray.getJSONObject(i).getJSONObject("from").getString("first_name");
            String lastName = jsonArray.getJSONObject(i).getJSONObject("from").getString("last_name");
            String username = jsonArray.getJSONObject(i).getJSONObject("from").getString("user_name");
            // String email = jsonArray.getJSONObject(i).getString("email");
            String profilePic = jsonArray.getJSONObject(i).getJSONObject("from").getString("profile_pic");
            //String profilePic= String.valueOf(android.R.drawable.ic_delete);
            String ticketNumber = jsonArray.getJSONObject(i).getString("ticket_number");
            String ID = jsonArray.getJSONObject(i).getString("id");
            String countcollaborator=jsonArray.getJSONObject(i).getString("countcollaborator");
            String countthread=jsonArray.getJSONObject(i).getString("thread_count");
            String source=jsonArray.getJSONObject(i).getString("source");
            //String title = jsonArray.getJSONObject(i).getString("ticket_title");
            String title=jsonArray.getJSONObject(i).getString("title");

//            String createdAt = jsonArray.getJSONObject(i).getString("created_at");
//            String departmentName = jsonArray.getJSONObject(i).getString("department_name");
//            String priorityName = jsonArray.getJSONObject(i).getString("priotity_name");
//            String slaPlanName = jsonArray.getJSONObject(i).getString("sla_plan_name");
//            String helpTopicName = jsonArray.getJSONObject(i).getString("help_topic_name");
            String ticketStatusName = jsonArray.getJSONObject(i).getString("status");
            //String ticketStatusName="open";
            String updatedAt = jsonArray.getJSONObject(i).getString("updated_at");
            String dueDate = jsonArray.getJSONObject(i).getString("duedate");
            String priorityColor = jsonArray.getJSONObject(i).getJSONObject("priority").getString("color");
            String attachment = jsonArray.getJSONObject(i).getString("attachment_count");

            String last_replier=jsonArray.getJSONObject(i).getString("last_replier");
            if (jsonArray.getJSONObject(i).getJSONObject("assignee").getString("first_name").equals("")||jsonArray.getJSONObject(i).getJSONObject("assignee").getString("last_name").equals("")){
             agentName= jsonArray.getJSONObject(i).getJSONObject("assignee").getString("user_name");
            }
            else if (jsonArray.getJSONObject(i).getJSONObject("assignee").getString("first_name").equals("null")||jsonArray.getJSONObject(i).getJSONObject("assignee").getString("last_name").equals("null")){
                agentName=jsonArray.getJSONObject(i).getJSONObject("assignee").getString("user_name");
            }
            else {
                agentName = jsonArray.getJSONObject(i).getJSONObject("assignee").getString("first_name") + " " + jsonArray.getJSONObject(i).getJSONObject("assignee").getString("last_name");
            }
            String clientname;
            //String agentname;

            String agentname;
            if (agentName.equals("null null")){
                agentname="Unassigned";
            }
            else if (agentName.equals("nullnull")){
                agentname="Unassigned";
            }
            else if (agentName.equals("null")){
                agentname="Unassigned";
            }
            else{
                agentname=agentName;
            }

            if (firstName == null || firstName.equals("")) {
                clientname = username;
            }
            else
                clientname = firstName + " " + lastName;
            return new TicketOverview(Integer.parseInt(ID), profilePic,
                    ticketNumber, clientname, title, updatedAt, priorityColor, ticketStatusName, i + "", attachment, dueDate, clientname,countcollaborator,countthread,source,last_replier,agentname);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static TicketOverview parseTicketSearchOverview(JSONArray jsonArray, int i) {
        String agentName;
        try {
            JSONObject jsonObject2=jsonArray.getJSONObject(i);
            String ticketNumber = jsonObject2.getString("ticket_number");
            String assignedto=jsonObject2.getString("assigned_to");
            String ID = jsonObject2.getString("id");
            JSONObject user=jsonObject2.getJSONObject("user");
            String firstName = user.getString("first_name");
            String lastName = user.getString("last_name");
            String username = user.getString("user_name");
            String profilePic = user.getString("profile_pic");
            if (assignedto.equals("null")){
                        agentName="Unassigned";
                    }
                    else{
                        JSONObject assigned=jsonObject2.getJSONObject("assigned");
                        agentName=assigned.getString("first_name")+" "+assigned.getString("last_name");
                    }
//            JSONObject assigned=jsonObject2.getJSONObject("assigned");
//            agentName=assigned.getString("first_name")+" "+assigned.getString("last_name");
            JSONArray thread=jsonObject2.getJSONArray("thread");
            JSONObject jsonObject5=thread.getJSONObject(0);
            String title=jsonObject5.getString("title");
            JSONObject statuses=jsonObject2.getJSONObject("statuses");
            String ticketStatusName = statuses.getString("name");
            JSONObject priority=jsonObject2.getJSONObject("priority");
            String priorityColor = priority.getString("priority_color");
            JSONObject jsonObjectSources=jsonObject2.getJSONObject("sources");
            String source=jsonObjectSources.getString("name");
//            String firstName = jsonArray.getJSONObject(i).getString("c_fname");
//            String lastName = jsonArray.getJSONObject(i).getString("c_lname");
//            String username = jsonArray.getJSONObject(i).getString("c_uname");
//            String profilePic = jsonArray.getJSONObject(i).getString("profile_pic");
//            String ticketNumber = jsonArray.getJSONObject(i).getString("ticket_number");
//            String ID = jsonArray.getJSONObject(i).getString("id");
            String countcollaborator=jsonArray.getJSONObject(i).getString("collaborator_count");
            String countthread=jsonArray.getJSONObject(i).getString("thread_count");

//            String title = jsonArray.getJSONObject(i).getString("ticket_title");
//            String ticketStatusName = jsonArray.getJSONObject(i).getString("ticket_status_name");
            String updatedAt = jsonArray.getJSONObject(i).getString("updated_at");
            String dueDate = jsonArray.getJSONObject(i).getString("duedate");
//            String priorityColor = jsonArray.getJSONObject(i).getString("color");
           String attachment = jsonArray.getJSONObject(i).getString("attachment_count");
            String last_replier=jsonArray.getJSONObject(i).getString("last_replier");
//            String agentName=jsonArray.getJSONObject(i).getString("a_fname")+" "+jsonArray.getJSONObject(i).getString("a_lname");
            String clientname;
            String agentname;

            if (agentName.equals("null null")){
                agentname="Unassigned";
            }
            else if (agentName.equals("nullnull")){
                agentname="Unassigned";
            }
            else{
                agentname=agentName;
            }

            if (firstName == null || firstName.equals(""))
                clientname = username;
            else
                clientname = firstName + " " + lastName;
            return new TicketOverview(Integer.parseInt(ID), profilePic,
                    ticketNumber, clientname, title, updatedAt, priorityColor, ticketStatusName, i + "", attachment, dueDate, clientname,countcollaborator,countthread,source,last_replier,agentname);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static TicketOverview parseTicketOverviewSort(JSONArray jsonArray, int i) {
        String agentName;
        try {
            //Date updated_at = null;
            String firstName = jsonArray.getJSONObject(i).getJSONObject("from").getString("first_name");
            String lastName = jsonArray.getJSONObject(i).getJSONObject("from").getString("last_name");
            String username = jsonArray.getJSONObject(i).getJSONObject("from").getString("user_name");
            // String email = jsonArray.getJSONObject(i).getString("email");
            String profilePic = jsonArray.getJSONObject(i).getJSONObject("from").getString("profile_pic");
            //String profilePic= String.valueOf(android.R.drawable.ic_delete);
            String ticketNumber = jsonArray.getJSONObject(i).getString("ticket_number");
            String ID = jsonArray.getJSONObject(i).getString("id");
            String countcollaborator=jsonArray.getJSONObject(i).getString("countcollaborator");
            String countthread=jsonArray.getJSONObject(i).getString("thread_count");
            String source=jsonArray.getJSONObject(i).getString("source");
            //String title = jsonArray.getJSONObject(i).getString("ticket_title");
            String title=jsonArray.getJSONObject(i).getString("title");

//            String createdAt = jsonArray.getJSONObject(i).getString("created_at");
//            String departmentName = jsonArray.getJSONObject(i).getString("department_name");
//            String priorityName = jsonArray.getJSONObject(i).getString("priotity_name");
//            String slaPlanName = jsonArray.getJSONObject(i).getString("sla_plan_name");
//            String helpTopicName = jsonArray.getJSONObject(i).getString("help_topic_name");
            String ticketStatusName = jsonArray.getJSONObject(i).getString("status");
            //String ticketStatusName="open";
            String updatedAt = jsonArray.getJSONObject(i).getString("updated_at");
            String dueDate = jsonArray.getJSONObject(i).getString("duedate");
            String priorityColor = jsonArray.getJSONObject(i).getJSONObject("priority").getString("color");
            String attachment = jsonArray.getJSONObject(i).getString("attachment_count");

            String last_replier=jsonArray.getJSONObject(i).getString("last_replier");
            if (jsonArray.getJSONObject(i).getJSONObject("assignee").getString("first_name").equals("")||jsonArray.getJSONObject(i).getJSONObject("assignee").getString("last_name").equals("")){
                agentName= jsonArray.getJSONObject(i).getJSONObject("assignee").getString("user_name");
            }
            else if (jsonArray.getJSONObject(i).getJSONObject("assignee").getString("first_name").equals("null")||jsonArray.getJSONObject(i).getJSONObject("assignee").getString("last_name").equals("null")){
                agentName=jsonArray.getJSONObject(i).getJSONObject("assignee").getString("user_name");
            }
            else {
                agentName = jsonArray.getJSONObject(i).getJSONObject("assignee").getString("first_name") + " " + jsonArray.getJSONObject(i).getJSONObject("assignee").getString("last_name");
            }
            String clientname;
            //String agentname;

            String agentname;
            if (agentName.equals("null null")){
                agentname="Unassigned";
            }
            else if (agentName.equals("nullnull")){
                agentname="Unassigned";
            }
            else if (agentName.equals("null")){
                agentname="Unassigned";
            }
            else{
                agentname=agentName;
            }

            if (firstName == null || firstName.equals("")) {
                clientname = username;
            }
            else
                clientname = firstName + " " + lastName;
            return new TicketOverview(Integer.parseInt(ID), profilePic,
                    ticketNumber, clientname, title, updatedAt, priorityColor, ticketStatusName, i + "", attachment, dueDate, clientname,countcollaborator,countthread,source,last_replier,agentname);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * Client Page.
     * @param jsonArray jsonArray refers to the array of JSON elements.
     * @param i position of the element in the array.
     * @return object for client overview.
     */
    public static ClientOverview parseClientOverview(JSONArray jsonArray, int i) {
        try {
            String clientID = jsonArray.getJSONObject(i).getString("id");
            String clientPicture = jsonArray.getJSONObject(i).getString("profile_pic");
            String userName = jsonArray.getJSONObject(i).getString("user_name");
            String firstName = jsonArray.getJSONObject(i).getString("first_name");
            String lastName = jsonArray.getJSONObject(i).getString("last_name");
            String clientEmail = jsonArray.getJSONObject(i).getString("email");
            String clientPhone = jsonArray.getJSONObject(i).getString("phone_number");
            String clientActive = jsonArray.getJSONObject(i).getString("active");
            String clientname;
            if (firstName == null || firstName.equals(""))
                clientname = userName;
            else
                clientname = firstName + " " + lastName;
            return new ClientOverview(Integer.parseInt(clientID), clientPicture,userName,clientEmail, clientPhone, clientActive, clientname);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ClientOverview parseSearchClientOverview(JSONArray jsonArray, int i) {
        try {

            JSONObject jsonObject2=jsonArray.getJSONObject(i);
            //JSONObject user=jsonObject2.getJSONObject("user_name");
            String clientID = jsonObject2.getString("id");
            String firstName = jsonObject2.getString("first_name");
            String lastName = jsonObject2.getString("last_name");
            //String username = user.getString("user_name");
            String clientEmail=jsonObject2.getString("email");
            //String profilePic = user.getString("profile_pic");
            String clientPicture = jsonObject2.getString("profile_pic");
            String userName = jsonObject2.getString("user_name");
            //String firstName = jsonArray.getJSONObject(i).getString("first_name");
            //String lastName = jsonArray.getJSONObject(i).getString("last_name");
            //String clientEmail = jsonArray.getJSONObject(i).getString("email");
            String clientPhone = jsonArray.getJSONObject(i).getString("mobile");
            String clientActive = jsonArray.getJSONObject(i).getString("active");

            String clientname;
            if (firstName == null || firstName.equals(""))
                clientname = userName;
            else
                clientname = firstName + " " + lastName;
            return new ClientOverview(Integer.parseInt(clientID), clientPicture,userName,clientEmail, clientPhone, clientActive, clientname);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static ClientOverview parseClientTickets(JSONArray jsonArray, int i) {
//        try {
//            String clientID = jsonArray.getJSONObject(i).getString("id");
//            String clientPicture = jsonArray.getJSONObject(i).getString("profile_pic");
//            String firstName = jsonArray.getJSONObject(i).getString("first_name");
//            String lastName = jsonArray.getJSONObject(i).getString("last_name");
//            String clientEmail = jsonArray.getJSONObject(i).getString("email");
//            String clientPhone = jsonArray.getJSONObject(i).getString("phone_number");
//            String clientCompany = jsonArray.getJSONObject(i).getString("company");
//            String clientActive = jsonArray.getJSONObject(i).getString("active");
//            String f = "", l = "";
//            if (firstName.trim().length() != 0) {
//                f = firstName.substring(0, 1);
//            }
//            if (lastName.trim().length() != 0) {
//                l = lastName.substring(0, 1);
//            }
//            return new ClientOverview(Integer.parseInt(clientID), clientPicture,
//                    firstName + " " + lastName, clientEmail, clientPhone, clientCompany, clientActive, f + l);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    /**
     * Notification Page.
     * @param jsonArray jsonArray jsonArray refers to the array of JSON elements.
     * @param i position of the element in the array.
     * @return object for notification thread.
     */
    public static NotificationThread parseNotifications(JSONArray jsonArray, int i) {
        try {
            String message = jsonArray.getJSONObject(i).getString("message");
            int ticketID = jsonArray.getJSONObject(i).getInt("row_id");
            int notiID = jsonArray.getJSONObject(i).getInt("id");
            String created_at = jsonArray.getJSONObject(i).getString("created_utc");
            String senario = jsonArray.getJSONObject(i).getString("senario");
            String seen = jsonArray.getJSONObject(i).getString("seen");
            JSONObject requester = jsonArray.getJSONObject(i).getJSONObject("requester");
            int clientID = requester.getInt("id");
            String firstName = requester.getString("changed_by_first_name");
            String clientPicture = requester.getString("profile_pic");
            String lastName = requester.getString("changed_by_last_name");
            String userName = requester.getString("changed_by_user_name");
            String clientname;
            if (firstName == null || firstName.equals(""))
                clientname = userName;
            else
                clientname = firstName + " " + lastName;
            return new NotificationThread(clientPicture, created_at, ticketID, message, clientname, senario, clientID, notiID, seen, clientname);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static Long notiRelativeTime(String dateToParse) {
//
//        SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy HH:mm:ss");
//        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
//        Date d = null;
//        try {
//            d = sdf.parse(dateToParse);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        SimpleDateFormat output = new SimpleDateFormat("d MMM yyyy  HH:mm");
//        output.setTimeZone(TimeZone.getDefault());
//
//        String formattedTime = output.format(d);
//        Date gg = null;
//        try {
//            gg = output.parse(formattedTime);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        // SimpleDateFormat day = new SimpleDateFormat("dd");
////            String formattedDay = day.format(d) + Helper.getDayOfMonthSuffix(Integer.parseInt(day.format(d)));
////            formattedTime = formattedTime.replaceFirst(formattedTime.substring(0, formattedTime.indexOf(" ")), formattedDay);
////            sdf.parse(dateToParse);
//        return gg != null ? gg.getTime() : 0;
//    }

    /**
     * Converting UTC to local returns long timeinmillseconds.
     * @param dateToParse is the date that we have to parse.
     * @return
     */
    public static Long relativeTime(String dateToParse) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date d = null;

        try {
            d = sdf.parse(dateToParse);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat output = new SimpleDateFormat("d MMM yyyy  HH:mm");

        output.setTimeZone(TimeZone.getDefault());

        String formattedTime = output.format(d);
        //Date gg = null;
        Date gg = new Date();
        //long diff = gg.getTime();

        try {
            gg = output.parse(formattedTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // SimpleDateFormat day = new SimpleDateFormat("dd");
//            String formattedDay = day.format(d) + Helper.getDayOfMonthSuffix(Integer.parseInt(day.format(d)));
//            formattedTime = formattedTime.replaceFirst(formattedTime.substring(0, formattedTime.indexOf(" ")), formattedDay);
//            sdf.parse(dateToParse);
        return gg != null ? gg.getTime() : 0;
    }

    /**
     * UTC time conversion to local time returns String Date.
     * @param dateToParse
     * @return
     */
    public static String parseDate(String dateToParse) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date dAte = sdf.parse(dateToParse);

            SimpleDateFormat output = new SimpleDateFormat("d MMM yyyy  HH:mm");
            output.setTimeZone(TimeZone.getDefault());

            String formattedTime = output.format(dAte);
            SimpleDateFormat day = new SimpleDateFormat("dd");
            String formattedDay = day.format(dAte) + Helper.getDayOfMonthSuffix(Integer.parseInt(day.format(dAte)));
            formattedTime = formattedTime.replaceFirst(formattedTime.substring(0, formattedTime.indexOf(" ")), formattedDay);
            sdf.parse(dateToParse);
            return formattedTime;
        } catch (ParseException e) {
            e.printStackTrace();
            return dateToParse;
        }
    }

    /**
     * Comparing two dates for DUEDATE.
     * @param duedate1
     * @return
     */
    public static int compareDates(String duedate1) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        sdf1.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        Date date2=null;
        try {
            date = sdf.parse(duedate1);
            date2=sdf1.parse(duedate1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat output = new SimpleDateFormat("d MMM yyyy  HH:mm");
        SimpleDateFormat output1 = new SimpleDateFormat("d MMM yyyy");
        output.setTimeZone(TimeZone.getDefault());
        output1.setTimeZone(TimeZone.getDefault());
        String formattedTime = output.format(date);
        String formattedTime1=output1.format(date2);
        Date dueDate = null;
        Date curntDate = null;
        Date dueDate1=null;
        Date currDate1=null;

        String currentStringDate = new SimpleDateFormat("d MMM yyyy  HH:mm", Locale.getDefault()).format(new Date());
        String currentStringDate1 = new SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(new Date());
        try {
            dueDate = output.parse(formattedTime);
            curntDate = output.parse(currentStringDate);
            dueDate1=output1.parse(formattedTime1);
            currDate1=output1.parse(currentStringDate1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int i = 0;

        if (dueDate1.equals(currDate1)) {
            i = 2;
        }

        else if (dueDate.after(curntDate)) {
            i = 0;
        } else if (dueDate.before(curntDate)) {
            i = 1;
        }


        return i;

    }

    private static String getDayOfMonthSuffix(final int n) {
        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    /**
     * Email validation.
     * @param target
     * @return
     */
    public static boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}
