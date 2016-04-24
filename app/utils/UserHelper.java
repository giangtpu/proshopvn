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
        public static final String FOLDER_IMAGER_KEY ="content.folder.image";
        public static final String FOLDER_ASSET_KEY ="content.folder.asset";

        public static final String LINK_AVATAR_KEY ="content.link.avatar";
        public static final String LINK_IMAGER_KEY ="content.link.image";
        public static final String LINK_ASSET_KEY ="content.link.asset";
        public static final String LINK_KID_AVATAR ="content.link.kidavatar";
        public static final String LINK_AVATAR_DEFAULT ="content.default.linkavatar";

        public static final String DEFAULT_AVATAR_KEY ="content.default.avatar";
        public static final String DEFAULT_IMAGE_KEY ="content.default.image";

        public static final String MAP_DISPLAY_BTS ="map.displayBTS";

        public static final String GOOGLE_API_BROWSER_KEY ="google.api.browserkey";
        public static final String GOOGLE_API_BROWSER_KEY_MAX ="google.api.browserkeymax";

        public static final String ADMIN_API_ENABLE ="admin.api.enable";
        public static final String DATABASE_SYSTEM_NAME ="database.system.name";
        public static final String SMS_RULE_STEP_SEND_IN_MINUTES="rule.sms.step.send.in.minutes";
        public static final String SMS_RULE_MAX_IN_HOUR="rule.sms.max.in.hour";
        public static final String SMS_RULE_MAX_IN_DAY="rule.sms.max.in.day";
    }

    public static String webLinkRoot = getAppConfigString(StaticContent.WEB_LINK_ROOT_KEY);
    public static String avatarFolderPath = getAppConfigString(StaticContent.FOLDER_AVATAR_KEY);
    public static String avatarLinkPath = webLinkRoot + getAppConfigString(StaticContent.LINK_AVATAR_KEY);
    public static String avatarKidLinkPath = getAppConfigString(StaticContent.LINK_KID_AVATAR);
    public static String avatarDefaultLinkPath = getAppConfigString(StaticContent.LINK_AVATAR_DEFAULT);
    public static String webLinkReset = getAppConfigString(StaticContent.WEB_LINK_RESET_PASS);
    public static String webLinkViettelOrder = getAppConfigString(StaticContent.WEB_LINK_VIETTEL_ORDER);
    public static Boolean isDisplayBTS = getAppConfigBoolean(StaticContent.MAP_DISPLAY_BTS);
    public static Integer maxGoogleApiBrowserKey = getAppConfigInt(StaticContent.GOOGLE_API_BROWSER_KEY_MAX);
    public static Boolean isEnableAdminApi = getAppConfigBoolean(StaticContent.ADMIN_API_ENABLE);
    public static String databaseSystemName = getAppConfigString(StaticContent.DATABASE_SYSTEM_NAME);
    public static Integer smsRuleStepSendInMinutes = getAppConfigInt(StaticContent.SMS_RULE_STEP_SEND_IN_MINUTES);
    public static Integer smsRuleMaxInHour = getAppConfigInt(StaticContent.SMS_RULE_MAX_IN_HOUR);
    public static Integer smsRuleMaxInDay = getAppConfigInt(StaticContent.SMS_RULE_MAX_IN_DAY);

    public static Configuration getAppConfig(){
        return Play.application().configuration();
    }
    public static String getAppConfigString(String key){
        String value = getAppConfig().getString(key);
        return value;
    }
    public static Integer getAppConfigInt(String key){
        return getAppConfig().getInt(key);
    }
    public static Long getConfigLong(String key){
        return getAppConfig().getLong(key);
    }
    public static Double getAppConfigDouble(String key){
        return getAppConfig().getDouble(key);
    }

    public static Boolean getAppConfigBoolean(String key){
        return getAppConfig().getBoolean(key);
    }

    public static boolean isSessionTimeOut(String sessionId){
        long timeSession = DateUtil.convertStringByteArrayToDate(sessionId);
        Date dateSession = DateUtil.convertDate(timeSession);
        Date currentDate = DateUtil.now();
        int timeout = getAppConfigInt(SessionData.TIMEOUT_KEY);
//        logger.debug("dateSession:{}",dateSession);
//        logger.debug("currentDate:{}",currentDate);
//        logger.debug("timeout:{}",timeout);
//        logger.debug("diffMinutes:{}",DateUtil.diffMinutes(currentDate, dateSession));
        if(DateUtil.diffMinutes(currentDate,dateSession)>timeout){
            return true;
        }
        return false;
    }

    public static String linkGoogleMapApi(){
        //src="https://maps.googleapis.com/maps/api/js?v=3&key=AIzaSyD94Yo1rr5OQWWkSGNVGW84nA3vZLIdfbA&lang=vi"
        String apiKeyRandom = getGoogleApiKeyRandom();
        String linkapi = "https://maps.googleapis.com/maps/api/js?v=3&key="+apiKeyRandom+"&lang=vi";
        return linkapi;
    }
    public static String getGoogleApiKeyRandom(){
        //random from 1-9

        int rand = StringUtil.randomNumber(1,maxGoogleApiBrowserKey);
        String keyRandom = getAppConfigString(StaticContent.GOOGLE_API_BROWSER_KEY + rand);

//        System.out.println("GoogleApiBrowserKeyMax:"+maxGoogleApiBrowserKey);
//        System.out.println("GoogleApiBrowserKeyRandom:"+keyRandom);

        return keyRandom;
    }
}
