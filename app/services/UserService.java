package services;

import models.User;
import org.apache.commons.lang.StringUtils;
import play.mvc.Http;
import utils.ParseUtil;
import utils.PasswordHash;
import utils.UserHelper;

import javax.inject.Singleton;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by giangdaika on 24/04/2016.
 */
@Singleton
public class UserService extends AbstractService{
    public User authenticate(String email, String password) {
        User user = userDAO.getByEmail(email);
        if(user !=null) {
            String encryptPassword = user.getPassword();
            boolean isValidatePassword = false;
            try {
                isValidatePassword = PasswordHash.validatePassword(password, encryptPassword);
            }
            catch (NoSuchAlgorithmException |InvalidKeySpecException ex){
                ex.printStackTrace();
            }

            //logger.debug("username:{} - userId:{}",user.getUsername(), user.getId());
            //logger.debug("isActive:{} - isValidatePassword:{}",user.isActive(), isValidatePassword);
            if (user.isActive() && isValidatePassword) {
                //logger.debug("isActive & login pass");
                addSessionIdToUser(user);
                //logger.debug("login success");
                return user;
            }
            //logger.debug("login false");
        }
        //logger.debug("not have user:{}", email);
        return null;
    }

    public void cleanUserSessionInCookie(Http.Session session){
        String sessionId = session.get(UserHelper.SessionData.sessionId);
        session.clear();
        deleteSessionId(sessionId);
    }
    public void addUserSessionToCookie(Http.Session session, User user, boolean isApp){
        String email = user.getEmail();
        int maxlengthName = (email.length()>=8)? 8: email.length();
        String name = email.substring(0, maxlengthName);
        String sessionId = session.get(UserHelper.SessionData.sessionId);
        if(org.springframework.util.StringUtils.isEmpty(sessionId)){
            sessionId = UserHelper.genaralSessionId();
            session.put(UserHelper.SessionData.sessionId, sessionId);
            user.setSessionId(sessionId);
            userDAO.updateSessionId(user);
        }

        session.put(UserHelper.SessionData.email, email);
        session.put(UserHelper.SessionData.name, name);
        session.put(UserHelper.SessionData.avatar, user.getAvatarLinkPath());
        session.put(UserHelper.SessionData.lastModified, "" + ParseUtil.parseDate(user.getLastModified()));
        if(isApp==true) {
            session.put(UserHelper.SessionData.isApp, "1");
        }
    }

    public User addSessionIdToUser(User user){
        String sessionId = UserHelper.genaralSessionId();
        user.setSessionId(sessionId);
        userDAO.updateSessionId(user);
        return user;
    }

    public boolean isEmailExisted(String email){
        User user = userDAO.getByEmail(email);
        if (user != null){
            return true;
        }
        else{
            return false;
        }
    }

    public User register(String email, String password,int role){
        User user = new User(email, password,role);
        userDAO.save(user);
        return user;
    }

    public void deleteSessionId(String sessionId){
        if(StringUtils.isEmpty(sessionId)){
            return;
        }
        User user = userDAO.getBySessionId(sessionId);
        if(user != null){
            userDAO.deleteSessionId(user);
        }
    }

    public User getUserBySession(String sessionId){
        return userDAO.getBySessionId(sessionId);
    }

    public void updateProfile(User user){
        userDAO.save(user);
    }


}
