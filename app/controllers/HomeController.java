package controllers;

import models.User;
import play.Configuration;
import play.Play;
import play.mvc.*;

import views.html.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends AbstractController {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        User user =new User("saivnct","saivnct@gmail.com","Buigiang88",User.Roles.admin.getCode());
        userDAO.save(user);

//        Configuration config = Play.application().configuration();
//        String networkMembers = config.getString("mongo.network.members");
//        int connectionPerHost = config.getInt("mongo.network.connectionPerHost");
//        int threadAllowBlock = config.getInt("mongo.network.threadAllowBlock");
//        boolean isReplicaSet = config.getBoolean("mongo.network.replicaSet");
//        boolean socketKeepAlive = config.getBoolean("mongo.network.socketKeepAlive");
//        int socketTimeout = config.getInt("mongo.network.socketTimeout");
//        int connectTimeout = config.getInt("mongo.network.connectTimeout");
//        String username = config.getString("mongo.db.username");
//        String password = config.getString("mongo.db.password");
//        String databaseName = config.getString("mongo.db.databaseName");
//
//        System.out.println("networkMembers:"+networkMembers);
//        System.out.println("connectionPerHost:"+connectionPerHost);
//        System.out.println("threadAllowBlock:"+threadAllowBlock);
//        System.out.println("isReplicaSet:"+isReplicaSet);
//        System.out.println("socketKeepAlive:"+socketKeepAlive);
//        System.out.println("socketTimeout:"+socketTimeout);

        return ok("Tao admin thanh cong");
    }

}
