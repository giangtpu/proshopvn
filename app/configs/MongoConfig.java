package configs;

import com.mongodb.*;
import org.bson.Document;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import play.Application;
import play.Configuration;
import play.Logger;
import play.Logger.ALogger;
import play.Play;


import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//@Component
//@Qualifier("mongoConfig")
@Singleton
public class MongoConfig extends AbstractMongoConfiguration{
//    private static final ALogger LOG = play.Logger.of(MongoConfig.class);
    Logger.ALogger LOG = Logger.of(MongoConfig.class);



    private final MongoClient mongoClient;
    public boolean isReplicaSet=false;

    @Inject
    public MongoConfig(Configuration configuration) throws Exception{
        this.mongoClient = mongoClient(configuration);
    }


    public MongoClient mongoClient(Configuration configuration) throws Exception {

        String networkMembers =configuration.getString("mongo.network.members");
        int connectionPerHost =  configuration.getInt("mongo.network.connectionPerHost");
        int threadAllowBlock = configuration.getInt("mongo.network.threadAllowBlock");
        isReplicaSet = configuration.getBoolean("mongo.network.replicaSet");
        boolean socketKeepAlive = configuration.getBoolean("mongo.network.socketKeepAlive");
        int socketTimeout = configuration.getInt("mongo.network.socketTimeout");
        int connectTimeout =configuration.getInt("mongo.network.connectTimeout");
        String username =  configuration.getString("mongo.db.username");
        String password = configuration.getString("mongo.db.password");
        String databaseName = configuration.getString("mongo.db.databaseName");

        List<ServerAddress> seeds = new ArrayList<ServerAddress>();
        for(String networkMember: Arrays.asList(networkMembers.split(","))){
            networkMember = networkMember.trim();
            List<String> network = Arrays.asList(networkMember.split(":"));
            String host = network.get(0);
            int port = Integer.parseInt(network.get(1));
            seeds.add(new ServerAddress(host,port));
        }

        MongoCredential credential = MongoCredential.createCredential(username, databaseName, password.toCharArray());
        MongoClientOptions options = MongoClientOptions.builder()
                //.writeConcern(WriteConcern.REPLICAS_SAFE)
                //.readPreference(ReadPreference.secondaryPreferred())
                .connectionsPerHost(connectionPerHost) //recommence 40 < 100
                .threadsAllowedToBlockForConnectionMultiplier(threadAllowBlock) // recommence 1/4 connectionPerHost
                .socketKeepAlive(socketKeepAlive) // true | falsef
                .socketTimeout(socketTimeout) //millisecond
                .connectTimeout(connectTimeout) //millisecond
                .build();

        MongoClient client = new MongoClient(seeds, Arrays.asList(credential), options);
        if(isReplicaSet){
            client.setWriteConcern(WriteConcern.REPLICAS_SAFE);
        }

        LOG.info("----------------------new mongoClient-------------------------------");
        return client;
    }

    @Override
    protected String getDatabaseName() {
        // TODO Auto-generated method stub
        String databaseName = Play.application().configuration().getString("mongo.db.databaseName");
        return databaseName;
    }

    @Override
    public Mongo mongo() throws Exception {
        // TODO Auto-generated method stub
        return mongoClient;
    }

    @PreDestroy
    public void shutdown() {
        try {
            LOG.info("MongoConfig shutdown: mongo().close()");
            mongo().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Document rsStatus(){
        Document result = null;
        if(isReplicaSet) {
            result = mongoClient.getDatabase("admin").runCommand(new Document("replSetGetStatus", 1));
        }
        else {
            result = mongoClient.getDatabase("admin").runCommand(new Document("serverStatus", 1));
        }
        return  result;
    }
}
