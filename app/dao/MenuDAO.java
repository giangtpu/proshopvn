package dao;

import configs.MongoConfig;
import models.Menu;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.inject.Inject;

/**
 * Created by giangdaika on 06/05/2016.
 */
public class MenuDAO extends AbstractDAO<String, Menu> {
    @Inject
    public MongoConfig mongoConfig;

    protected MongoTemplate mongoTempl() throws Exception {
        return mongoConfig.mongoTemplate();
    }

    public MenuDAO(){
        super(String.class, Menu.class);

    }
}
