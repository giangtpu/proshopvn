package models.JSON;

/**
 * Created by giangdaika on 15/05/2016.
 */
public class ItemDelForm {
    boolean success=false;
    String id;

    public ItemDelForm() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
