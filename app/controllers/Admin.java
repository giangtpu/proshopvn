package controllers;

import play.mvc.Result;
import play.mvc.Security;

/**
 * Created by giangdaika on 26/04/2016.
 */
@Security.Authenticated(Secured.class)
public class Admin extends AbstractController {
    public Result index() {
        return ok(views.html.Admin.render(getUserSession()));
    }
}
