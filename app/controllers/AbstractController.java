package controllers;

import dao.UserDAO;
import models.User;
import models.forms.UserForm;
import org.springframework.util.StringUtils;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.ValidationError;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Http;
import services.UserService;
import utils.ImageUtil;
import utils.UserHelper;

import javax.inject.Inject;
import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created by giangdaika on 24/04/2016.
 */
public class AbstractController extends Controller{
    Logger.ALogger logger = Logger.of(AbstractController.class);

    @Inject
    public UserService userService;

    @Inject
    public UserDAO userDAO;

    @Inject
    public MessagesApi messagesApi;

    @Inject
    public FormFactory formFactory;


//    Ex: String s= getMessages().at("home.title");
    public Messages getMessages() {
        Messages messages = messagesApi.preferred(request());
        return messages;
    }

    public String getLangCode(){
        String lang = ctx().lang().code();
        logger.info("lang: {}",lang);
        if (StringUtils.isEmpty(lang))
        {
            return "vi";
        }
        return lang;
    }

    public User getUserSession(){
        String sessionId = session().get(UserHelper.SessionData.sessionId);

        if(StringUtils.isEmpty(sessionId)){
            return null;
        }
        if(UserHelper.isSessionTimeOut(sessionId)){
            // session expired
            logger.debug("session expired - session.clear():");
            userService.cleanUserSessionInCookie(session());
            return null;
        }

        User user = userService.getUserBySession(sessionId);

        return user;
    }

    public boolean isAdmin(){
        return UserHelper.isAdmin(getUserSession());
    }
    public boolean isSmodPermit(){
        return UserHelper.isSModPermit(getUserSession());
    }

    public boolean isModPermit(){
        return UserHelper.isModPermit(getUserSession());
    }

    public <T> void addErrorToFlash(Form<T> formRequest){
        //logger.debug("hasErrors, return badRequest");
        for(String key: formRequest.errors().keySet()){
            List<ValidationError> currentError = formRequest.errors().get(key);
            for(ValidationError error : currentError){
                flash(key, error.message());
                //logger.debug("key: {}, msg: {}", key, error.message());
            }
        }
    }

    public void updateProfile(User user){
        userService.updateProfile(user);
        userService.addUserSessionToCookie(session(), user, false);
    }

    public void updateProfileApp(User user){
        userService.updateProfile(user);
        userService.addUserSessionToCookie(session(), user, true);
    }

    public void addUserSessionToCookie(User user){
//        generalUserSession(user);
        userService.addUserSessionToCookie(session(), user, false);
    }

    public void addUserSessionToCookieApp(User user){
//        generalUserSession(user);
        userService.addUserSessionToCookie(session(),user, true);
    }

    public void writeUserAvatarTodisk(UserForm formuser, User user){
        Http.MultipartFormData.FilePart fileData = formuser.getFileData();
        String oldAvatarFilename = user.getAvatar();
        String fileName = formuser.getFileName();
        String contentType = formuser.getContentType();
        File file = (File) fileData.getFile();
        formuser.setFileClientPath(file.getPath());
        String imageName = UserHelper.generateUniqueFilename(fileName);
        try
        {
            // write image file to disk
            ImageUtil.writeAvatarToDisk(imageName, UserHelper.avatarUserFolderPath, file);
            user.setAvatar(imageName);
            java.util.concurrent.CompletionStage<Boolean> promiseOfDelImg = CompletableFuture.supplyAsync(
                    () -> ImageUtil.delImage(oldAvatarFilename, UserHelper.avatarUserFolderPath)
            );
        }catch (Exception e){
            e.printStackTrace();
        }

    }




}
