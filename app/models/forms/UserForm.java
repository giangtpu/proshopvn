package models.forms;

import org.apache.commons.lang.StringUtils;
import play.data.validation.ValidationError;
import play.i18n.Messages;
import play.mvc.Http;
import utils.DateUtil;
import utils.ImageUtil;
import utils.UserHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static play.mvc.Controller.request;

/**
 * Created by giangbb on 22/03/2016.
 */
public class UserForm {
    private String id;
    private String username;
    private String email;
    private String password;
    private String repeatPassword;
    private boolean active=true;
    private String phone;
    //Giangbb 25/12/2015
    private String authenCode;
    private Date dateauthen;
    private Date dateLockAuthen;
    private int sendAuthenTime=0;       //Number of SMS authencode have been send to phone      //max=5;
    private boolean mobileValidate=false;
    private boolean emailValidate=false;

    private String avatar;
    private boolean unsubscribe=false;

    // user,admin - 1,operator - 2
    private int role;
    private String sessionId;
    //	@JsonIgnore	private Date createdDate;
    private Date createdDate;
    private Date lastModified;

    private boolean sociallogin;

    private Http.MultipartFormData.FilePart fileData;
    private String contentType;
    private String fileName;
    private String fileClientPath;
    private String fileServerPath;

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        Http.MultipartFormData data = request().body().asMultipartFormData();

        if (data!=null&&data.getFile("avatar") != null){
            if(!StringUtils.isEmpty(data.getFile("avatar").getFilename())){
                fileData = data.getFile("avatar");
                fileName = fileData.getFilename();
//            System.out.println("fileName:" + fileName);
                contentType = ImageUtil.getImageType(fileName);
                File file = (File )fileData.getFile();
                fileClientPath = file.getParent();

                if(!ImageUtil.checkValidImageType(contentType)){
                    errors.add(new ValidationError("avatar", "wrong format image"));
                }
//            System.out.println("fileName:" + fileName);
//            System.out.println("contentType:" + contentType);
//            System.out.println("fileClientPath:" + fileClientPath);
//
//            System.out.println("getPath:" + file.getPath());
//            System.out.println("getAbsolutePath:" + file.getAbsolutePath());
            }
        }


        return errors.isEmpty() ? null : errors;
    }

    public UserForm(){

    }

    public UserForm(String username, String email, String password) {
        this.id = UserHelper.generalUserID();
        this.username=username;
        this.email = email;
        this.password = UserHelper.hashPassword(password);
    }

    public UserForm(String email, String password) {
        this.id = UserHelper.generalUserID();
        this.email = email;
        this.password = UserHelper.hashPassword(password);
        this.createdDate = DateUtil.now();
        this.lastModified = DateUtil.now();
    }




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public boolean isUnsubscribe() {
        return unsubscribe;
    }

    public void setUnsubscribe(boolean unsubscribe) {
        this.unsubscribe = unsubscribe;
    }


    public boolean isSociallogin() {
        return sociallogin;
    }

    public void setSociallogin(boolean sociallogin) {
        this.sociallogin = sociallogin;
    }

    public boolean isMobileValidate() {
        return mobileValidate;
    }

    public void setMobileValidate(boolean mobileValidate) {
        this.mobileValidate = mobileValidate;
    }

    public boolean isEmailValidate() {
        return emailValidate;
    }

    public void setEmailValidate(boolean emailValidate) {
        this.emailValidate = emailValidate;
    }

    public String getAuthenCode() {
        return authenCode;
    }

    public void setAuthenCode(String authenCode) {
        this.authenCode = authenCode;
    }

    public Date getDateauthen() {
        return dateauthen;
    }

    public void setDateauthen(Date dateauthen) {
        this.dateauthen = dateauthen;
    }

    public int getSendAuthenTime() {
        return sendAuthenTime;
    }

    public void setSendAuthenTime(int sendAuthenTime) {
        this.sendAuthenTime = sendAuthenTime;
    }

    public Date getDateLockAuthen() {
        return dateLockAuthen;
    }

    public void setDateLockAuthen(Date dateLockAuthen) {
        this.dateLockAuthen = dateLockAuthen;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public Http.MultipartFormData.FilePart getFileData() {
        return fileData;
    }

    public void setFileData(Http.MultipartFormData.FilePart fileData) {
        this.fileData = fileData;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileClientPath() {
        return fileClientPath;
    }

    public void setFileClientPath(String fileClientPath) {
        this.fileClientPath = fileClientPath;
    }

    public String getFileServerPath() {
        return fileServerPath;
    }

    public void setFileServerPath(String fileServerPath) {
        this.fileServerPath = fileServerPath;
    }
}
