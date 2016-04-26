package models;

import configs.ModelData;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.StringUtils;
import utils.DateUtil;
import utils.UserHelper;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by giangbb on 26/04/2016.
 */
@ModelData(collection = "Customer", mapCacheName = "", mapCacheTTL = 86400)
@Document(collection = "Customer")
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    private String password;
    @Indexed(unique = true)
    String email;
    String name;
    @Indexed(unique = true)
    String phone;
    String address;
    private String avatar;
    private String sessionId;
    private int numberOrder=0;      //so lan dat hang
    private int numberCancelOrder=0;    //so lan huy dat hang

    private Date lastModified;

    public Customer() {
        Date now = DateUtil.now();
        this.id = UserHelper.generalUserID();
        this.lastModified = now;
    }

    public String getAvatarLinkPath(){
        if (StringUtils.isEmpty(avatar))
            return UserHelper.avatarDefaultLinkPath;
        else
            return UserHelper.avatarCustomerLinkPath + "/" + avatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public int getNumberOrder() {
        return numberOrder;
    }

    public void setNumberOrder(int numberOrder) {
        this.numberOrder = numberOrder;
    }

    public int getNumberCancelOrder() {
        return numberCancelOrder;
    }

    public void setNumberCancelOrder(int numberCancelOrder) {
        this.numberCancelOrder = numberCancelOrder;
    }
}
