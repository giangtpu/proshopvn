package cache;


import configs.ModelData;
import org.springframework.data.annotation.Id;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public abstract class AbstractCache<KeyType, T> {
    protected Class<KeyType> keyTypeClass;
    protected Class<T> tClass;

    protected String collectionName;
    protected String mapCacheName;
    protected int mapCacheTTL;
    protected boolean isUseCache;

    //giangbb -not use cache currently
//    protected abstract HazelcastInstance hazelcastInstance();

    public KeyType getKey(T obj){
//        for (Field f: tClass.getFields()) {                   //->get only public field
        for (Field f: tClass.getDeclaredFields()) {             //->get all fields
            Annotation annotation = f.getAnnotation(Id.class);
//            System.out.println("Class:"+tClass+"-------------------Field:"+f+"----------anotation :"+annotation);
            if (annotation instanceof Id) {
//                System.out.println("have ID!!!");
                try {
                    f.setAccessible(true);
                    return (KeyType) f.get(obj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
//        System.out.println("not found ID!!!");
        return null;
    }


    public AbstractCache(Class<KeyType> keyTypeClass, Class<T> type){
        this.keyTypeClass = keyTypeClass;
        this.tClass = type;
        if (tClass.isAnnotationPresent(ModelData.class)) {
            Annotation annotation = tClass.getAnnotation(ModelData.class);
            ModelData modelData = (ModelData) annotation;
            this.collectionName = modelData.collection();
            this.mapCacheName = modelData.mapCacheName();
            isUseCache=true;
            if (StringUtils.isEmpty(mapCacheName))
            {
                isUseCache=false;
            }
            this.mapCacheTTL = modelData.mapCacheTTL();
        }

    }

    // ---------------------- IMAP ------------------------------
    //giangbb -not use cache currently
//    private IMap<KeyType, T> cacheMap;
//
//    private synchronized IMap<KeyType, T> getCacheMap() {
//        try {
//            if (cacheMap == null) {
//                cacheMap = hazelcastInstance().getMap(mapCacheName);
//            }
//            return cacheMap;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    // ---------------------- IMAP ------------------------------

    // ---------------------- Function Cache---------------------
    //giangbb -not use cache currently
//    public void putCache(T obj){
//        putCache(getKey(obj), obj);
//    }
//    public void putCache(KeyType key, T obj){
//        if (StringUtils.isEmpty(key)) {
//            return;
//        }
//        getCacheMap().put(key, obj, mapCacheTTL, TimeUnit.SECONDS);
//    }
//
//    public void deleteCacheByKey(KeyType key)
//    {
//        if (StringUtils.isEmpty(key)) {
//            return;
//        }
//        getCacheMap().delete(key);
//    }
//
//    public T getCacheByKey(KeyType key){
//        if (StringUtils.isEmpty(key)) {
//            return null;
//        }
//        return getCacheMap().get(key);
//    }
//
//    public List<T> getAllCache(){
//        return new ArrayList<T>(getCacheMap().values());
//    }
//
//    public void putAllCache(Map<KeyType, T> objMap){
//        if (objMap == null || objMap.isEmpty()) {
//            return;
//        }
//        getCacheMap().putAll(objMap);
//    }
//
//    public void clearCache(){
//        getCacheMap().destroy();
//    }
    // ---------------------- Function Cache---------------------
}
