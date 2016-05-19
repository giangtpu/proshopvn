package models.JSON;

/**
 * Created by giangdaika on 18/05/2016.
 */
public class ResultForm {
    public boolean success=false;
    public String errorMessage;

    public ResultForm() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
