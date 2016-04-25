package utils;

import play.Application;
import play.Configuration;

/**
 * Created by giangdaika on 25/04/2016.
 */
public class AppHelper {
    public static Configuration getAppConfig(){
        return
//				Play.application().configuration(); 		//has been deprecated
                play.api.Play.current().injector().instanceOf(Application.class).configuration();
    }

    public static String getAppConfigString(String key){
        return getAppConfig().getString(key);
    }
    public static Integer getAppConfigInt(String key){
        return getAppConfig().getInt(key);
    }
    public static Long getConfigLong(String key){
        return getAppConfig().getLong(key);
    }
    public static Double getAppConfigDouble(String key){
        return getAppConfig().getDouble(key);
    }

    public static Boolean getAppConfigBoolean(String key){
        return getAppConfig().getBoolean(key);
    }
}
