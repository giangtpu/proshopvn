package controllers;

import models.User;
import play.Configuration;
import play.Play;
import play.mvc.*;

import views.html.*;

import javax.inject.Inject;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends AbstractController {
    @Inject
    play.Configuration configuration;
    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        User user =new User("saivnct","saivnct@gmail.com","Buigiang88",User.Roles.admin.getCode());
        userDAO.save(user);

        return ok("Tao admin thanh cong");
    }

}
