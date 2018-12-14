package co.helpdesk.faveo.pro.model;

/**
 * Created by narendra on 09/12/16.
 * Model class for notification.
 */
public class NotificationThread {

    public String profiel_pic;
    public String noti_time;
    public String noti_seen;
    public int ticket_id;
    public String ticket_subject;
    public String placeHolder;
    public String noti_scenario;
    public int client_id;
    public int noti_id;
    public String requesterName;
    public String by;

    public NotificationThread(String profiel_pic, String noti_time, int ticket_id, String ticket_subject, String placeHolder, String noti_scenario, int client_id, int noti_id, String noti_seen, String requesterName,String by) {
        this.profiel_pic = profiel_pic;
        this.noti_time = noti_time;
        this.ticket_id = ticket_id;
        this.ticket_subject = ticket_subject;
        this.placeHolder = placeHolder;
        this.noti_scenario = noti_scenario;
        this.client_id = client_id;
        this.noti_id = noti_id;
        this.noti_seen = noti_seen;
        this.requesterName = requesterName;
        this.by=by;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getNoti_seen() {
        return noti_seen;
    }

    public void setNotiseen(String noti_seen) {
        this.noti_seen = noti_seen;
    }

    public int getNotiid() {
        return noti_id;
    }

    public void setNotiid(int noti_id) {
        this.noti_id = noti_id;
    }

    public int getTicketid() {
        return ticket_id;
    }

    public void setTicketid(int ticket_id) {
        this.ticket_id = ticket_id;
    }

    public int getClientid() {
        return client_id;
    }

    public void setClientid(int client_id) {
        this.client_id = client_id;
    }

    public String getNotiscenario() {
        return noti_scenario;
    }

    public void setNotiscenario(String noti_scenario) {
        this.noti_scenario = noti_scenario;
    }

    public String getPlaceHolder() {
        return placeHolder;
    }

    public void setPlaceHolder(String placeHolder) {
        this.placeHolder = placeHolder;
    }

    public String getTicketsubject() {
        return ticket_subject;
    }

    public void setTicketsubject(String ticket_subject) {
        this.ticket_subject = ticket_subject;
    }


    public String getProfielpic() {
        return profiel_pic;
    }

    public void setProfielpic(String profiel_pic) {
        this.profiel_pic = profiel_pic;
    }

    public String getNoti_time() {
        return noti_time;
    }

    public void setNoti_time(String noti_time) {
        this.noti_time = noti_time;
    }

}
