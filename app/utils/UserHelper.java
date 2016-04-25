package utils;

import models.User;
import play.Configuration;
import play.Logger;
import play.Play;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;


public class UserHelper {
    public static Logger.ALogger logger = Logger.of(UserHelper.class);
    public class SessionData{
        public static final String sessionId = "sessionId";
        public static final String email = "email";
        public static final String avatar = "avatar";
        public static final String name = "name"; // substring email 12 character
        public static final String TIMEOUT_KEY = "session.timeout";
        public static final String language = "language";
        public static final String lastModified = "lastModified";
        public static final String isApp = "isApp";
    }

    public static boolean isAdmin(User user){
        if (user.getRole()==User.Roles.admin.getCode()){return true;}
        return false;
    }
    public static boolean isSMod(User user){
        if (user.getRole()==User.Roles.supermod.getCode()){return true;}
        return false;
    }
    public static boolean isMod(User user){
        if (user.getRole()==User.Roles.mod.getCode()){return true;}
        return false;
    }

    public static boolean isSModPermit(User user){
        if (user.getRole()==User.Roles.supermod.getCode()||isAdmin(user)){return true;}
        return false;
    }
    public static boolean isModPermit(User user){
        if (user.getRole()==User.Roles.mod.getCode()||isSModPermit(user)){return true;}
        return false;
    }


    public static String genaralSessionId() {
        return StringUtil.getTimeUUIDString();
    }
    public static String generalUserID() {
        return StringUtil.getTimeUUIDString();
    }
    public static String generateFilename(String contentType) {
        return StringUtil.getTimeUUIDString() + "." + contentType;
    }
    public static String hashPassword(String password){
        String passwordHash = "";
        try {
            passwordHash = PasswordHash.createHash(password);
        }
        catch(NoSuchAlgorithmException |InvalidKeySpecException ex){
            ex.printStackTrace();
        }
        return passwordHash;
    }

    public class StaticContent{
        public static final String WEB_LINK_ROOT_KEY ="web.link.root";
        public static final String WEB_LINK_RESET_PASS ="web.link.reset";
        public static final String WEB_LINK_VIETTEL_ORDER ="web.link.viettelorder";

        public static final String FOLDER_AVATAR_KEY ="content.folder.avatar";


        public static final String LINK_AVATAR_KEY ="content.link.avatar";
        public static final String LINK_KID_AVATAR ="content.link.kidavatar";
        public static final String LINK_AVATAR_DEFAULT ="content.default.linkavatar";



        public static final String MAP_DISPLAY_BTS ="map.displayBTS";

    }

    public static String webLinkRoot = AppHelper.getAppConfigString(StaticContent.WEB_LINK_ROOT_KEY);
    public static String avatarFolderPath = AppHelper.getAppConfigString(StaticContent.FOLDER_AVATAR_KEY);
    public static String avatarLinkPath = webLinkRoot + AppHelper.getAppConfigString(StaticContent.LINK_AVATAR_KEY);
    public static String avatarKidLinkPath = AppHelper.getAppConfigString(StaticContent.LINK_KID_AVATAR);
    public static String avatarDefaultLinkPath = AppHelper.getAppConfigString(StaticContent.LINK_AVATAR_DEFAULT);
    public static String webLinkReset = AppHelper.getAppConfigString(StaticContent.WEB_LINK_RESET_PASS);
    public static String webLinkViettelOrder = AppHelper.getAppConfigString(StaticContent.WEB_LINK_VIETTEL_ORDER);
    public static Boolean isDisplayBTS = AppHelper.getAppConfigBoolean(StaticContent.MAP_DISPLAY_BTS);


    public static boolean isSessionTimeOut(String sessionId){
        long timeSession = DateUtil.convertStringByteArrayToDate(sessionId);
        Date dateSession = DateUtil.convertDate(timeSession);
        Date currentDate = DateUtil.now();
        int timeout = AppHelper.getAppConfigInt(SessionData.TIMEOUT_KEY);
//        logger.debug("dateSession:{}",dateSession);
//        logger.debug("currentDate:{}",currentDate);
//        logger.debug("timeout:{}",timeout);
//        logger.debug("diffMinutes:{}",DateUtil.diffMinutes(currentDate, dateSession));
        if(DateUtil.diffMinutes(currentDate,dateSession)>timeout){
            return true;
        }
        return false;
    }


}
