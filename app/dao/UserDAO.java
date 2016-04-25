package dao;

import configs.MongoConfig;
import models.User;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;
import utils.StringUtil;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by giangdaika on 24/04/2016.
 */
@Singleton
public class UserDAO extends AbstractDAO<String, User>{

    public static final String UserCollection = "User";

    @Inject
    public MongoConfig mongoConfig;

    private static String idField = "_id";
    private static String sessionIdField = "sessionId";
    private static String emailField = "email";
    private static String createdDateField ="createdDate";
    private static String lastModifiedField ="lastModified";
    private static String usernameField = "username";
    private static String phoneField = "phone";

    protected MongoTemplate mongoTempl() throws Exception {
        return mongoConfig.mongoTemplate();
    }



    public UserDAO(){
        super(String.class, User.class);

    }

    public User updateSessionId(User user){
        if(user == null){
            return null;
        }
        try {
            Query query = new Query(Criteria.where(idField).is(user.getId()));
            Update update = Update.update(sessionIdField, user.getSessionId());
            mongoTempl().updateFirst(query, update, User.class, UserCollection);
            //giangbb -not use cache currently
//            try {
//                userCache.putUserMap(user);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public User updateCreatedAndLastModifiedDate(User user){
        if(user == null){
            return null;
        }
        try {
            Query query = new Query(Criteria.where(idField).is(user.getId()));
            Update update = new Update();
            update.set(createdDateField, user.getCreatedDate());
            update.set(lastModifiedField,user.getLastModified());
            mongoTempl().updateFirst(query, update, User.class, UserCollection);
            //giangbb -not use cache currently
//            try {
//                userCache.putUserMap(user);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public void deleteSessionId(User user){
        try {
            Query query = new Query(Criteria.where(idField).is(user.getId()));
            Update update = Update.update(sessionIdField, StringUtil.blank);
            mongoTempl().updateFirst(query, update, User.class, UserCollection);
            //giangbb -not use cache currently
//            try {
//                userCache.deleteSessionId(user.getSessionId());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User getByEmail(String email){
        if(StringUtils.isEmpty(email)){
            return null;
        }
        User user=null;
//        try {
//            user = userCache.getUserByEmail(email);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        if(user==null){
            try {
                Query query = new Query(Criteria.where(emailField).is(email));
                user = mongoTempl().findOne(query, User.class, UserCollection);

//                try {
//                    if(user!=null) {
//                        userCache.putUserMap(user);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return user;

    }

    public User getBySessionId(String sessionId){
        if(StringUtils.isEmpty(sessionId)){
            return null;
        }
        User user=null;
//        try {
//            user = userCache.getUserBySessionId(sessionId);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        if(user==null){
            try {
                Query query = new Query(Criteria.where(sessionIdField).is(sessionId));
                user = mongoTempl().findOne(query, User.class, UserCollection);

//                try {
//                    if(user!=null) {
//                        userCache.putUserMap(user);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return user;

    }

    public List<User> getUsersByCreateDate(Date stardate, Date enddate){
        List<User> userList=new ArrayList<User>();
        try {
            Query query = new Query(Criteria.where(createdDateField).gte(stardate).lte(enddate));
            userList = mongoTempl().find(query, User.class, UserCollection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    public User getByPhone(String phone){
        if(StringUtils.isEmpty(phone)){
            return null;
        }
        User user=null;
//        try {
//            user = userCache.getUserByPhone(phone);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        if(user==null){
            try {
                Query query = new Query(Criteria.where(phoneField).is(phone));
                user = mongoTempl().findOne(query, User.class, UserCollection);

//                try {
//                    if(user!=null) {
//                        userCache.putUserMap(user);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return user;

    }





}
