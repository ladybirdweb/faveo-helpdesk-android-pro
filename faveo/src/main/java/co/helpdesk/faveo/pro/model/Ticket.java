package co.helpdesk.faveo.pro.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by narendra on 09/12/16.
 */

public class Ticket extends RealmObject {

    @PrimaryKey
    private long ticket_id;

    private String ticket_number;

    private String ticket_opened_by;

    private String ticket_subject;


    public long getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(long ticket_id) {
        this.ticket_id = ticket_id;
    }

    public String getTicket_number() {
        return ticket_number;
    }

    public void setTicket_number(String ticket_number) {
        this.ticket_number = ticket_number;
    }

    public String getTicket_opened_by() {
        return ticket_opened_by;
    }

    public void setTicket_opened_by(String ticket_opened_by) {
        this.ticket_opened_by = ticket_opened_by;
    }

    public String getTicket_subject() {
        return ticket_subject;
    }

    public void setTicket_subject(String ticket_subject) {
        this.ticket_subject = ticket_subject;
    }


}
