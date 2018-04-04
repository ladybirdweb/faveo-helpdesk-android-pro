package co.helpdesk.faveo.pro.model;

/**
 * Created by Sumit
 * Model class for ticket thread.
 */
public class TicketThread {

    public String clientPicture;
    public String clientName;
    public String messageTime;
    public String messageTitle;
    public String message;
    public String isReply;
    public String placeholder;
    public String name;
    public String file;
    public String type;
    public String noOfAttachments;


//    public TicketThread(String clientName, String messageTime, String message, String isReply) {
//        this.messageTime = messageTime;
//        this.message = message;
//        this.isReply = isReply;
//        this.clientName = clientName;
//    }

    public TicketThread(String clientPicture, String clientName, String messageTime, String messageTitle, String message, String isReply, String placeholder,String name,String file,String type,String noOfAttachments) {
        this.clientPicture = clientPicture;
        this.clientName = clientName;
        this.messageTime = messageTime;
        this.messageTitle = messageTitle;
        this.message = message;
        this.isReply = isReply;
        this.placeholder = placeholder;
        this.name=name;
        this.file=file;
        this.type=type;
        this.noOfAttachments=noOfAttachments;
    }

    public String getNoOfAttachments() {
        return noOfAttachments;
    }

    public void setNoOfAttachments(String noOfAttachments) {
        this.noOfAttachments = noOfAttachments;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
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

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIsReply() {
        return isReply;
    }

    public void setIsReply(String isReply) {
        this.isReply = isReply;
    }
}
