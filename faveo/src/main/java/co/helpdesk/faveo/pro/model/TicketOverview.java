package co.helpdesk.faveo.pro.model;

/**
 * Created by Sumit
 * Model class for ticket overview.
 */
public class TicketOverview {

    public int ticketID;
    public String clientPicture;
    public String ticketNumber;
    public String clientName;
    public String ticketSubject;
    public String ticketTime;
    public String ticketBubble;
    public String ticketStatus;
    public String ticketPriorityColor;
    public String ticketAttachments;
    public String dueDate;
    public String placeholder;
    public String countcollaborator;
    public String countthread;
    public String sourceTicket;
    public String lastReply;
    public String agentName;
    public String priorityName;
    private boolean isChecked;
    public String departmentname;

    public boolean getChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public TicketOverview(int ticketID, String clientPicture, String ticketNumber, String clientName, String ticketSubject, String ticketTime, String ticketPriorityColor, String ticketStatus, String ticketBubble, String ticketAttachments, String dueDate, String placeholder,String countcollaborator,String countthread,String sourceTicket,String lastReply,String agentName,String priorityName,String departmentname) {
        this.ticketID = ticketID;
        this.clientPicture = clientPicture;
        this.ticketNumber = ticketNumber;
        this.clientName = clientName;
        this.ticketSubject = ticketSubject;
        this.ticketTime = ticketTime;
        this.ticketBubble = ticketBubble;
        this.ticketPriorityColor = ticketPriorityColor;
        this.ticketStatus = ticketStatus;
        this.ticketAttachments = ticketAttachments;
        this.dueDate = dueDate;
        this.placeholder = placeholder;
        this.countcollaborator=countcollaborator;
        this.countthread=countthread;
        this.sourceTicket=sourceTicket;
        this.lastReply=lastReply;
        this.agentName=agentName;
        this.priorityName=priorityName;
        this.departmentname=departmentname;
    }

    public String getDepartmentname() {
        return departmentname;
    }

    public void setDepartmentname(String departmentname) {
        this.departmentname = departmentname;
    }

    public String getPriorityName() {
        return priorityName;
    }

    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
    }

    public String getLastReply() {
        return lastReply;
    }

    public void setLastReply(String lastReply) {
        this.lastReply = lastReply;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getCountcollaborator() {
        return countcollaborator;
    }

    public void setCountcollaborator(String countcollaborator) {
        this.countcollaborator = countcollaborator;
    }

    public String getCountthread() {
        return countthread;
    }

    public void setCountthread(String countthread) {
        this.countthread = countthread;
    }

    public String getSourceTicket() {
        return sourceTicket;
    }

    public void setSourceTicket(String sourceTicket) {
        this.sourceTicket = sourceTicket;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getTicketAttachments() {
        return ticketAttachments;
    }

    public void setTicketAttachments(String ticketAttachments) {
        this.ticketAttachments = ticketAttachments;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getTicketPriority() {
        return ticketPriorityColor;
    }

    public void setTicketPriority(String ticketPriority) {
        this.ticketPriorityColor = ticketPriority;
    }

    public int getTicketID() {
        return ticketID;
    }

    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }

    public String getClientPicture() {
        return clientPicture;
    }

    public void setClientPicture(String clientPicture) {
        this.clientPicture = clientPicture;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getTicketSubject() {
        return ticketSubject;
    }

    public void setTicketSubject(String ticketSubject) {
        this.ticketSubject = ticketSubject;
    }

    public String getTicketTime() {
        return ticketTime;
    }

    public void setTicketTime(String ticketTime) {
        this.ticketTime = ticketTime;
    }

    public String getTicketBubble() {
        return ticketBubble;
    }

    public void setTicketBubble(String ticketBubble) {
        this.ticketBubble = ticketBubble;
    }
}
