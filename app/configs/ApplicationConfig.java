package configs;

import play.Configuration;
import play.mvc.Controller;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by giangbb on 25/04/2016.
 */
@Singleton
public class ApplicationConfig extends Controller {

    private String networkMembers;
    private int connectionPerHost;
    private int threadAllowBlock;
    private boolean isReplicaSet;
    private boolean socketKeepAlive;
    private int socketTimeout;
    private int connectTimeout;
    private String username;
    private String password;
    private String databaseName;


    @Inject
    public ApplicationConfig(Configuration configuration) {
        this.networkMembers = configuration.getString("mongo.network.members");
        this.connectionPerHost = configuration.getInt("mongo.network.connectionPerHost");
        this.threadAllowBlock = configuration.getInt("mongo.network.threadAllowBlock");
        this.isReplicaSet = configuration.getBoolean("mongo.network.replicaSet");
        this.socketKeepAlive = configuration.getBoolean("mongo.network.socketKeepAlive");
        this.socketTimeout = configuration.getInt("mongo.network.socketTimeout");
        this.connectTimeout = configuration.getInt("mongo.network.connectTimeout");
        this.username = configuration.getString("mongo.db.username");
        this.password = configuration.getString("mongo.db.password");
        this.databaseName = configuration.getString("mongo.db.databaseName");
    }




    public String getNetworkMembers() {
        return networkMembers;
    }

    public void setNetworkMembers(String networkMembers) {
        this.networkMembers = networkMembers;
    }

    public int getConnectionPerHost() {
        return connectionPerHost;
    }

    public void setConnectionPerHost(int connectionPerHost) {
        this.connectionPerHost = connectionPerHost;
    }

    public int getThreadAllowBlock() {
        return threadAllowBlock;
    }

    public void setThreadAllowBlock(int threadAllowBlock) {
        this.threadAllowBlock = threadAllowBlock;
    }

    public boolean isReplicaSet() {
        return isReplicaSet;
    }

    public void setReplicaSet(boolean replicaSet) {
        isReplicaSet = replicaSet;
    }

    public boolean isSocketKeepAlive() {
        return socketKeepAlive;
    }

    public void setSocketKeepAlive(boolean socketKeepAlive) {
        this.socketKeepAlive = socketKeepAlive;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
}
