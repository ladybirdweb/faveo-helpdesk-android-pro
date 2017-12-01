package co.helpdesk.faveo.pro.model;

/**
 * Created by Sumit.
 * Model class for client overview.
 */
public class ClientOverview {

    public int clientID;
    public String clientPicture;
    public String clientName;
    public String clientEmail;
    public String clientPhone;
    public String clientCompany;
    public String clientActive;
    public String placeholder;
    public String username;

    public ClientOverview(int clientID, String clientPicture, String clientName,
                          String clientEmail, String clientPhone, String clientActive, String placeholder) {
        this.clientID = clientID;
        this.clientPicture = clientPicture;
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.clientPhone = clientPhone;
        this.clientActive = clientActive;
        this.placeholder = placeholder;
        this.username=username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public String getClientPicture() {
        return clientPicture;
    }

    public void setClientPicture(String clientPicture) {
        this.clientPicture = clientPicture;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public String getClientActive() {
        return clientActive;
    }

    public void setClientActive(String clientActive) {
        this.clientActive = clientActive;
    }
}
