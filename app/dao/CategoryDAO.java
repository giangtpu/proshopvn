package dao;

import configs.MongoConfig;
import models.Category;
import models.Item;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.inject.Inject;

/**
 * Created by giangdaika on 25/04/2016.
 */
public class CategoryDAO extends AbstractDAO<String, Category> {

    @Inject
    public MongoConfig mongoConfig;

    protected MongoTemplate mongoTempl() throws Exception {
        return mongoConfig.mongoTemplate();
    }

    public CategoryDAO(){
        super(String.class, Category.class);

    }
}
