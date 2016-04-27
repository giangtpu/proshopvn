package controllers;

import models.User;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Result;
import play.mvc.Security;
import views.html.Admin_user_profile;

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
}
