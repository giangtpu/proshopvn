package controllers;

import models.User;

import models.forms.UserForm;
import org.springframework.util.StringUtils;
import play.data.Form;
import play.i18n.Messages;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import utils.DateUtil;
import utils.ImageUtil;
import utils.StringUtil;
import utils.UserHelper;
import views.html.Admin_user_profile;

import java.io.File;
import java.util.concurrent.CompletableFuture;

/**
 * Created by giangdaika on 26/04/2016.
 */
@Security.Authenticated(Secured.class)
public class Admin extends AbstractController {
    public Result index() {
        return ok(views.html.Admin.render(getUserSession()));
    }

    public Result userAdminprofile(String id)
    {
        if (!isAdmin()&&!id.equals(getUserSession().getId())&&!id.equals("me")){
            flash("failed", getMessages().at("Admin.donthavepermission"));
            return redirect(routes.Admin.index());
        }
        User user=userDAO.getByKey(id);
        if (user==null||id.equals("me"))
        {
            user=getUserSession();          //neu khong tim thay tra ve user hien tai

            return ok(Admin_user_profile.render(getUserSession(),user));
        }

        return ok(Admin_user_profile.render(getUserSession(), user));
    }

    public Result updateUserAdmin(String id)
    {
        Form<UserForm> Userform = formFactory.form(UserForm.class);



        if(Userform.hasErrors())
        {
            flash("failed",getMessages().at("form.error"));
            return redirect(routes.Admin.userAdminprofile(id));
        }
        UserForm formuser=Userform.bindFromRequest().get();
        Http.MultipartFormData.FilePart fileData = formuser.getFileData();

        User userupdate = userDAO.getByKey(formuser.getId());


        if (userupdate!=null)
        {
            User currentUser=getUserSession();
            if(isAdmin() && (formuser.getRole() !=1) && currentUser.getId().equals(userupdate.getId())){

                flash("failed",getMessages().at("Admin.cannotdowngrade"));
                return redirect(routes.Admin.userAdminprofile(id));
            }
            if (isAdmin()) {
                userupdate.setRole(formuser.getRole());
            }
            if (isAdmin()||currentUser.getId().equals(userupdate.getId()))
            {

                if(!StringUtils.isEmpty(formuser.getUsername()))
                {
                    userupdate.setUsername(formuser.getUsername());
                }
                if(!StringUtils.isEmpty(formuser.getPhone()))
                {
                    userupdate.setPhone(formuser.getPhone());
                }
                if(!StringUtils.isEmpty(formuser.getPassword()))
                {
                    if (!StringUtil.isComplicatedPassword(formuser.getPassword())){
                        flash("failed",getMessages().at("Admin.PasswordNotComplicated"));
                        return redirect(routes.Admin.userAdminprofile(id));
                    }
                    if (!formuser.getPassword().equals(formuser.getRepeatPassword()))
                    {
                        flash("failed",getMessages().at("Admin.PasswordNotMatch"));
                        return redirect(routes.Admin.userAdminprofile(id));
                    }
                    userupdate.setPassword(UserHelper.hashPassword(formuser.getPassword()));
                }
                if(!StringUtils.isEmpty(formuser.getEmail()))
                {
                    if (!userupdate.getEmail().equals(formuser.getEmail())){
                        if (userService.isEmailExisted(formuser.getEmail())) {
                            flash("failed",getMessages().at("loginForm.ExistedEmail"));
                            return redirect(routes.Admin.userAdminprofile(id));
                        }
                        userDAO.deleteByKey(userupdate.getId());
                        userupdate.setEmail(formuser.getEmail());
                    }
                }


                if(fileData!=null) {
                    String oldAvatarFilename = userupdate.getAvatar();

                    String fileName = formuser.getFileName();
                    String contentType = formuser.getContentType();
                    File file = (File) fileData.getFile();
                    formuser.setFileClientPath(file.getPath());
                    String imageName = UserHelper.generateUniqueFilename(fileName);

//                    System.out.println("imageName:"+imageName);
                    // write image file to disk
                    java.util.concurrent.CompletionStage<Boolean> promiseOfSaveImg = CompletableFuture.supplyAsync(
                            () -> ImageUtil.writeAvatarToDisk(imageName, UserHelper.avatarUserLinkPath, file)
                    );
//                    ImageUtil.writeAvatarToDisk(imageName, UserHelper.avatarUserLinkPath, file);
                    userupdate.setAvatar(imageName);
                    java.util.concurrent.CompletionStage<Boolean> promiseOfDelImg = CompletableFuture.supplyAsync(
                            () -> ImageUtil.delImage(oldAvatarFilename, UserHelper.avatarUserLinkPath)
                    );
                    logger.debug("User.getAvatarPath:{}", userupdate.getAvatarLinkPath());
                }




                updateProfile(userupdate);
            } else {
                flash("failed",getMessages().at("Admin.donthavepermission"));
                return redirect(routes.Admin.userAdminprofile(id));

            }
            flash("success",getMessages().at("Admin.updatesuccess"));
            return redirect(routes.Admin.userAdminprofile(id));
        }

        flash("success",getMessages().at("Admin.usernotfound"));
        return redirect(routes.Admin.userAdminprofile(id));
    }
}
