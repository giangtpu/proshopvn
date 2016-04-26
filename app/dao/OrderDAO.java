package dao;

import configs.MongoConfig;
import models.Item;
import models.Order;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.inject.Inject;

/**
 * Created by giangbb on 26/04/2016.
 */
public class OrderDAO  extends AbstractDAO<String, Order>  {

    @Inject
    public MongoConfig mongoConfig;

    protected MongoTemplate mongoTempl() throws Exception {
        return mongoConfig.mongoTemplate();
    }

    public OrderDAO(){
        super(String.class, Order.class);

    }
}
