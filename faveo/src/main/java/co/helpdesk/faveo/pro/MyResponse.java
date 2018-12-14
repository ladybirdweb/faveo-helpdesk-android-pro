package co.helpdesk.faveo.pro;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sayar Samanta on 3/26/2018.
 */
//public class MyResponse {
//
//    @SerializedName("response")
//    @Expose
//    private Response response;
//
//    public Response getResponse() {
//        return response;
//    }
//
//    public void setResponse(Response response) {
//        this.response = response;
//    }
//
//}
//-----------------------------------com.example.Post.java-----------------------------------


//-----------------------------------com.example.Response.java-----------------------------------


public class MyResponse {

    @SerializedName("ticket_id")
    @Expose
    private Integer ticketId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("redirect")
    @Expose
    private Boolean redirect;

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getRedirect() {
        return redirect;
    }

    public void setRedirect(Boolean redirect) {
        this.redirect = redirect;
    }

    @Override
    public String toString() {
        return "MyResponse{" +
                "ticketId=" + ticketId +
                ", message='" + message + '\'' +
                ", redirect=" + redirect +
                '}';
    }
}
