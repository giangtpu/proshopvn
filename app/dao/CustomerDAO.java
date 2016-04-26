package dao;

import configs.MongoConfig;
import models.Customer;
import models.Item;
import models.User;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by giangbb on 26/04/2016.
 */
@Singleton
public class CustomerDAO extends AbstractDAO<String, Customer> {
    public static final String CustomerCollection = "Customer";

    @Inject
    public MongoConfig mongoConfig;


    private static String idField = "_id";
    private static String sessionIdField = "sessionId";
    private static String usernameField = "name";
    private static String phoneField = "phone";
    private static String emailField = "email";

    protected MongoTemplate mongoTempl() throws Exception {
        return mongoConfig.mongoTemplate();
    }

    public CustomerDAO(){
        super(String.class, Customer.class);

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
                user = mongoTempl().findOne(query, User.class, CustomerCollection);

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
                user = mongoTempl().findOne(query, User.class, CustomerCollection);

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
