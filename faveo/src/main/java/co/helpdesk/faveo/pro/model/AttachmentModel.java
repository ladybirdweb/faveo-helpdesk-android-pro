package co.helpdesk.faveo.pro.model;

/**
 * Created by Sayar Samanta on 3/19/2018.
 */

public class AttachmentModel {
    public String name;
    public String file;

    public AttachmentModel(String name, String file) {
        this.name = name;
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
