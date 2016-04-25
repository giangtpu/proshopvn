package controllers;

import dao.ItemDAO;
import models.Item;
import models.JSON.TestForm;
import models.User;
import play.Configuration;
import play.Play;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.libs.Json;
import play.mvc.*;

import utils.UserHelper;
import views.html.*;

import javax.inject.Inject;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends AbstractController {
    @Inject
    ItemDAO itemDAO;
//    @Inject
//    Messages messages;
    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
//        User user =new User("saivnct","saivnct@gmail.com","Buigiang88",User.Roles.admin.getCode());
//        userDAO.save(user);

        Item item=new Item("456", "testitem", "cover", "testitem test", "leather", "teo.jpg", 100, 125000);
        itemDAO.save(item);

        System.out.println(item.getImageLinkPath());
        String s= getMessages().at("home.title");
        return ok(index.render(s));
    }

    public Result ajaxTest(){
        TestForm testForm=new TestForm();
        testForm.setRespone(getMessages().at("js.testajax"));

        return ok(Json.toJson(testForm));
    }

    public Result setlang(String lang) {

        session(UserHelper.SessionData.language, lang);
        ctx().changeLang(lang);
        return redirect(routes.HomeController.index());
    }

}
