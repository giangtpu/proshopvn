package dao;


import configs.MongoConfig;
import models.RecieptIssue;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.inject.Inject;

/**
 * Created by giangbb on 26/04/2016.
 */
public class RecieptIssueDAO extends AbstractDAO<String, RecieptIssue> {
    @Inject
    public MongoConfig mongoConfig;

    protected MongoTemplate mongoTempl() throws Exception {
        return mongoConfig.mongoTemplate();
    }

    public RecieptIssueDAO(){
        super(String.class, RecieptIssue.class);

    }
}
