package dao;

import configs.MongoConfig;
import models.Promotion;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.inject.Inject;

/**
 * Created by giangdaika on 18/05/2016.
 */
public class PromotionDAO  extends AbstractDAO<String, Promotion> {
    @Inject
    public MongoConfig mongoConfig;

    protected MongoTemplate mongoTempl() throws Exception {
        return mongoConfig.mongoTemplate();
    }

    public PromotionDAO(){
        super(String.class, Promotion.class);

    }
}
