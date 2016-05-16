package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.User;

import models.forms.UserForm;
import org.springframework.util.StringUtils;
import play.Logger;
import play.data.Form;
import play.i18n.Messages;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import utils.*;
import views.html.Admin_user_add;
import views.html.Admin_user_profile;
import views.html.Admin_users;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

//new git
/**
 * Created by giangdaika on 26/04/2016.
 */
@Security.Authenticated(Secured.class)
public class Admin extends AbstractController {
    Logger.ALogger logger = Logger.of(Admin.class);
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

                writeUserAvatarTodisk(formuser,userupdate);

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

    public Result deleteUserAdmin(int page, int numPage)
    {
        Form<UserForm> Userform = formFactory.form(UserForm.class);


        List<User> userList=new ArrayList<User>();

        if(Userform.hasErrors())
        {
            flash("failed",getMessages().at("form.error"));
            return redirect(routes.Admin.adminusersPage( page, numPage));
        }
        if(!isAdmin())
        {
            flash("failed",getMessages().at("Admin.donthavepermission"));
            return redirect(routes.Admin.adminusersPage( page, numPage));
        }
        UserForm formuser=Userform.bindFromRequest().get();

        User user = userDAO.getByKey(formuser.getId());

//        System.out.printf("user id:"+user.getId());

        if (user==null)
        {
            flash("failed",getMessages().at("Admin.usernotfound"));
            return redirect(routes.Admin.adminusersPage( page, numPage));
        }


        userDAO.deleteByKey(user.getId());
        numPage=(int) userDAO.CountQuerry()/StaticData.itemPerPage_UserAdmin;
        if (userDAO.CountQuerry()%StaticData.itemPerPage_UserAdmin!=0)
        {
            numPage++;
        }
        if (page>numPage)
        {
            page--;
        }
        flash("success",getMessages().at("Admin.deletesuccess"));
        return redirect(routes.Admin.adminusersPage( page, numPage));
    }


    public Result adminusers()
    {
        int numPage=(int) userDAO.CountQuerry()/ StaticData.itemPerPage_UserAdmin;
        if (userDAO.CountQuerry()%StaticData.itemPerPage_UserAdmin!=0)
        {
            numPage++;
        }

        return redirect(routes.Admin.adminusersPage( 1, numPage));
    }
    public Result adminusersPage(int page, int numPage)
    {
        List<User> userList=new ArrayList<User>();
        userList=userDAO.getByPage(page, StaticData.itemPerPage_UserAdmin);
        if (userList==null) {
            flash("failed", getMessages().at("Admin.usernotfound"));
            return redirect(routes.Admin.index());
        }
        return ok(Admin_users.render(getUserSession(), userList, page, numPage));
    }

    public Result addAdminuser(){
        if (!isAdmin()){
            flash("failed", getMessages().at("Admin.donthavepermission"));
            return redirect(routes.Admin.index());
        }
        return ok(Admin_user_add.render(getUserSession()));
    }

    public Result adduserAdmin()
    {
        Form<UserForm> Userform = formFactory.form(UserForm.class);

        if(Userform.hasErrors())
        {
            flash("failed",getMessages().at("form.error"));
            return redirect(routes.Admin.addAdminuser());
        }
        UserForm formuser=Userform.bindFromRequest().get();

        if(!isAdmin() ){

            flash("failed", getMessages().at("Admin.donthavepermission"));
            return redirect(routes.Admin.addAdminuser());
        }
        if (!StringUtil.isComplicatedPassword(formuser.getPassword())){
            flash("failed",getMessages().at("Admin.PasswordNotComplicated"));
            return redirect(routes.Admin.addAdminuser());
        }
        if (!formuser.getPassword().equals(formuser.getRepeatPassword()))
        {
            flash("failed",getMessages().at("Admin.PasswordNotMatch"));
            return redirect(routes.Admin.addAdminuser());
        }
        if (userService.isEmailExisted(formuser.getEmail())) {
            flash("failed",getMessages().at("loginForm.ExistedEmail"));
            return redirect(routes.Admin.addAdminuser());
        }

        int role=formuser.getRole();
        String username=formuser.getUsername();
        String email=formuser.getEmail();
        String password=formuser.getPassword();

        User user=new User(username,email,password,role);
        if(!StringUtils.isEmpty(formuser.getPhone()))
        {
            user.setPhone(formuser.getPhone());
        }

        writeUserAvatarTodisk(formuser,user);

        userDAO.save(user);

        flash("success",getMessages().at("Admin.addsuccess"));
        return redirect(routes.Admin.addAdminuser());

    }





}
