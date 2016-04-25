package controllers;

import jsmessages.JsMessages;
import jsmessages.JsMessagesFactory;
import jsmessages.japi.Helper;
import play.Routes;
import play.libs.Scala;
import play.mvc.Controller;
import play.mvc.Result;
import play.routing.JavaScriptReverseRouter;


import javax.inject.Inject;

/**
 * Created by giangbb on 25/04/2016.
 */
public class JsController extends Controller {
    private final JsMessages jsMessages;
    @Inject
    public JsController(JsMessagesFactory jsMessagesFactory) {
        jsMessages = jsMessagesFactory.all();
    }

    public Result jsMessages() {
        return ok(jsMessages.apply(Scala.Option("window.Messages"), Helper.messagesFromCurrentHttpContext()));
    }

    public  Result javascriptRoutes() {
//        response().setContentType("text/javascript");     //2.5 not supported any more , use as instead
        return ok(
                JavaScriptReverseRouter.create("jsRoutes",
                        controllers.routes.javascript.HomeController.ajaxTest())

        ).as("text/javascript");

    }
}
