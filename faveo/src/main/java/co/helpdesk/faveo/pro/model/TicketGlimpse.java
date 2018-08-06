package co.helpdesk.faveo.pro.model;

/**
 * Created by Sumit
 * Model class for ticket glimpse.
 */
public class TicketGlimpse {

    public int ticketID;
    public String ticketNumber;
    public String ticketSubject;
    public boolean isTicketOpen;
    public String status;
    public String clientId;
    public String clinetName;

    public TicketGlimpse(int ticketID, String ticketNumber, String ticketSubject, boolean isTicketOpen,String status,String clientId,String clinetName) {
        this.ticketID = ticketID;
        this.ticketNumber = ticketNumber;
        this.ticketSubject = ticketSubject;
        this.isTicketOpen = isTicketOpen;
        this.status=status;
        this.clinetName=clinetName;
        this.clientId=clientId;
    }

    public String getClinetName() {
        return clinetName;
    }

    public void setClinetName(String clinetName) {
        this.clinetName = clinetName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTicketID() {
        return ticketID;
    }

    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getTicketSubject() {
        return ticketSubject;
    }

    public void setTicketSubject(String ticketSubject) {
        this.ticketSubject = ticketSubject;
    }

    public boolean isTicketOpen() {
        return isTicketOpen;
    }

    public void setIsTicketOpen(boolean isTicketOpen) {
        this.isTicketOpen = isTicketOpen;
    }

}
