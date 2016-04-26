package controllers;

import models.User;
import models.forms.LoginForm;
import play.data.Form;
import play.data.validation.ValidationError;
import play.i18n.Messages;
import play.mvc.Result;
import utils.DateUtil;
import utils.StaticData;
import utils.UserHelper;
import views.html.Admin_login;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giangdaika on 26/04/2016.
 */
public class Application extends AbstractController {
    public Result setlang(String lang) {
        session(UserHelper.SessionData.language, lang);
        ctx().changeLang(lang);
        return redirect(routes.Admin.index());
    }

    public Result login() {
//        User admin=new User("giangdaika","saivnct@gmail.com","Buigiang88",User.Roles.admin.getCode());
//        userDAO.save(admin);

        User user = getUserSession();
        if (user == null) {
            return ok(Admin_login.render());
        } else {
            return redirect(
                    routes.Admin.index()
            );
        }
    }

    public Result authenticate() {
        String referer = request().getHeader("referer");
        Form<LoginForm> formRequest = formFactory.form(LoginForm.class);
        if (formRequest.hasErrors()) {

            flash("failed",getMessages().at("form.error"));
            return redirect(routes.Application.login());
        }
        LoginForm loginForm = formRequest.bindFromRequest().get();
        String email = loginForm.getEmail();
        String password = loginForm.getPassword();
        User user = userService.authenticate(email, password);
        if (user == null) {
            flash("failed",getMessages().at("loginForm.InvalidEmailPass"));
            return redirect(routes.Application.login());
        }

        addUserSessionToCookie(user);

        flash("success", getMessages().at("login.success",user.getUsername()));
        return redirect(
                routes.Admin.index()
        );

    }

    public Result logout() {
        String sessionId = session().get(UserHelper.SessionData.sessionId);
        session().clear();
        userService.deleteSessionId(sessionId);
        flash("success", getMessages().at("loginForm.LoggedOut"));
        return redirect(
                routes.Application.login()
        );
    }

}
