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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by giangdaika on 24/04/2016.
 */
@ModelData(collection = "User", mapCacheName = "", mapCacheTTL = 86400)
//@Document(collection = "User")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Roles {
        admin(1),
        supermod(2),
        mod(3);

        private final int code;

        private static final Map<Integer, Roles> roleByCode = new HashMap<Integer, Roles>();

        static {
            for(Roles role: Roles.values()){
                roleByCode.put(role.getCode(),role);
            }
        }

        private Roles(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name();
        }

        public static Roles fromCode(int code){
            return roleByCode.get(code);
        }

        public static String getNameByCode(int code){
            Roles role = roleByCode.get(code);
            if(role!=null){
                return role.getName();
            }
            return "";
        }

    }

    @Id
    private String id;
    private String username;
    @Indexed(unique = true)
    private String email;
    private String password;
    private String phone;
    private String avatar;
    private int role;
    private boolean active=true;
    private String sessionId;
    private Date createdDate;
    private Date lastModified;


    public User() {
    }

    public User(String username, String email, String password, int role) {
        Date now = DateUtil.now();
        this.id = UserHelper.generalUserID();
        this.username=username;
        this.email = email;
        this.password = UserHelper.hashPassword(password);
        this.role=role;
        this.createdDate = now;
        this.lastModified = now;

    }

    public User(String email, String password,int role) {
        Date now = DateUtil.now();
        this.id = UserHelper.generalUserID();
        this.email = email;
        this.password = UserHelper.hashPassword(password);
        this.role=role;
        this.createdDate = now;
        this.lastModified = now;
    }

    public String getAvatarLinkPath(){
        if (StringUtils.isEmpty(avatar))
            return UserHelper.avatarDefaultLinkPath;
        else
            return UserHelper.avatarUserLinkPath + "/" + avatar;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
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

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
