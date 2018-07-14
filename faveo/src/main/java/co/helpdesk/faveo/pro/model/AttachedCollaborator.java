package co.helpdesk.faveo.pro.model;

public class AttachedCollaborator {
    private String email,picture,name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AttachedCollaborator(String email, String picture, String name) {
        this.email = email;
        this.picture = picture;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

}
