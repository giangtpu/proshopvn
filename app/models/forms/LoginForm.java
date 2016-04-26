package models.forms;
import org.apache.commons.lang3.StringUtils;
import play.data.validation.ValidationError;
import play.i18n.Messages;
import utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giangbb on 26/08/2015.
 */
public class LoginForm {
    private String email;
    private String password;
    private String passwordRepeat;
    private boolean isRegisterForm=false;

    public LoginForm(){

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }

    public boolean isRegisterForm() {
        return isRegisterForm;
    }

    public void setIsRegisterForm(boolean isRegisterForm) {
        this.isRegisterForm = isRegisterForm;
    }
}
