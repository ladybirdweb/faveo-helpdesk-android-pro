package co.helpdesk.faveo.pro.model;

public class collaboratorModel {
    public String email,pic;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public collaboratorModel(String email, String pic) {
        this.email = email;
        this.pic = pic;
    }
}
