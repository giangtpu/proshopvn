package dao;

import configs.MongoConfig;
import models.Item;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by giangdaika on 25/04/2016.
 */
@Singleton
public class ItemDAO extends AbstractDAO<String, Item> {
    @Inject
    public MongoConfig mongoConfig;

    protected MongoTemplate mongoTempl() throws Exception {
        return mongoConfig.mongoTemplate();
    }

    public ItemDAO(){
        super(String.class, Item.class);

    }
}
