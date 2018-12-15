package co.helpdesk.faveo.pro.model;

/**
 * Created by Sayar Samanta on 4/5/2018.
 */

public class CollaboratorSuggestion {
    public String first_name,last_name,email,profile_pic;
    private int id;

    public CollaboratorSuggestion(int id,String first_name, String last_name, String email, String profile_pic) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.profile_pic = profile_pic;
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return first_name;
    }

    public void setFirstname(String first_name) {
        this.first_name = first_name;
    }

    public String getLastname() {
        return last_name;
    }

    public void setLastname(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilepic() {
        return profile_pic;
    }

    public void setProfilepic(String profile_pic) {
        this.profile_pic = profile_pic;
    }
    @Override
    public String toString() {
        return this.email;
    }
}
