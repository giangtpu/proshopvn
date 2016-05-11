package models.JSON;

/**
 * Created by giangbb on 11/05/2016.
 */
public class ItemImageUpload {
    private boolean issuccess;
    private String url;
    private String filename;

    public ItemImageUpload() {
    }

    public boolean issuccess() {
        return issuccess;
    }

    public void setIssuccess(boolean issuccess) {
        this.issuccess = issuccess;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
