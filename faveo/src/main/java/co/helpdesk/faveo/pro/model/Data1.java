package co.helpdesk.faveo.pro.model;

/**
 * Created by narendra on 26/05/17.
 * Model class for displaying the data in spinner list.
 */

public class Data1 {

    public String ID;
    public String name;

    public Data1(String ID, String name) {
        this.ID=ID;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;            // What to display in the Spinner list.
    }

}
