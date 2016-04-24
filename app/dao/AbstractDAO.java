package dao;

import cache.AbstractCache;
import com.google.common.collect.Lists;
import com.mongodb.WriteResult;
import models.SearchCondition;
import models.SearchFilter;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;
import play.Logger;
import utils.ParseUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by tannb on 11/02/2016.
 */
public abstract class AbstractDAO<KeyType, T> extends AbstractCache<KeyType, T> {
    Logger.ALogger logger = Logger.of(AbstractDAO.class);

    protected abstract MongoTemplate mongoTempl() throws Exception;

    public AbstractDAO(Class<KeyType> keyTypeClass, Class<T> type){
        super(keyTypeClass, type);

    }

    // ---------------------- Function DAO---------------------

    public void save(T obj) {
        save(getKey(obj), obj);
    }

    public void update(T obj) {
        save(getKey(obj), obj);
    }

    public void save(KeyType key, T obj) {
        if (obj == null) {
            return;
        }
        try {
            mongoTempl().save(obj, collectionName);
            //giangbb -not use cache currently
//            if (isUseCache)
//            {
//                try {
//                    putCache(key, obj);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }




    public T getByKey(KeyType key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        T obj = null;
        //giangbb -not use cache currently
//        if (isUseCache)
//        {
//            try {
//                obj = getCacheByKey(key);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

        if (obj == null) {
            try {
                Query query = new Query(Criteria.where("_id").is(key));
                obj = mongoTempl().findOne(query, tClass, collectionName);
                //giangbb -not use cache currently
//                if (isUseCache)
//                {
//                    try {
//                        if(obj != null) {
//                            putCache(key, obj);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return obj;
    }

    public int deleteByKey(KeyType key) {
        Query query = new Query(Criteria.where("_id").is(key));
        WriteResult result = null;
        try {
            result = mongoTempl().remove(query, tClass, collectionName);
            //giangbb -not use cache currently
//            if (isUseCache)
//            {
//                try {
//                    deleteCacheByKey(key);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.getN();
    }

    public List<T> getAll() {
        List<T> objList=new ArrayList<T>();
        //giangbb -not use cache currently
//        if (isUseCache)
//        {
//            try {
//                objList = getAllCache();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

        if (objList == null || objList.isEmpty()) {
            try {
                objList = mongoTempl().findAll(tClass, collectionName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return objList;
    }

    public List<T> getAll_reverse() {
        List<T> objList=new ArrayList<T>();
        //giangbb -not use cache currently
//        if (isUseCache)
//        {
//            try {
//                objList = getAllCache();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

        if (objList == null || objList.isEmpty()) {
            try {
                objList = mongoTempl().findAll(tClass, collectionName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Lists.reverse(objList);
    }

    public void saveAll(Map<KeyType, T> objMap) {
        if (objMap == null || objMap.isEmpty()) {
            return;
        }
        try {
            for(KeyType key: objMap.keySet()){
                save(key, objMap.get(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    public long CountQuerry()
    {
        long num=0;
        try {
            num=mongoTempl().getCollection(collectionName).count();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }

    public List<T> getByPage(int page,int pagesize){
        List<T> objList=new ArrayList<T>();
        try {
            Query query = new Query();
            query.skip(pagesize*(page-1));
            query.limit(pagesize);
            objList = mongoTempl().find(query, tClass, collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objList;
    }

    public List<T> getByDate(String dateFieldName,Date stardate,Date enddate){
        List<T> objList=new ArrayList<T>();
        try {
            Query query = new Query(Criteria.where(dateFieldName).gte(stardate).lte(enddate));
            objList = mongoTempl().find(query, tClass, collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objList;
    }

    public List<T> searchInField(String fieldName,String searchKey){
        List<T> objList=new ArrayList<T>();
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where(fieldName).regex(searchKey,"i"));
            objList = mongoTempl().find(query, tClass, collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objList;
    }

    public List<T> getbyfield(String fieldName,String searchKey){
        List<T> deviceKeyIdList=new ArrayList<T>();
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where(fieldName).regex(searchKey));
            deviceKeyIdList = mongoTempl().find(query, tClass, collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceKeyIdList;
    }

    public  long countOnQuery(SearchFilter searchFilter){
        long TotalResult =0;
        List<SearchCondition> searchConditionList =searchFilter.getConditionList();
        try {
            Query query = new Query();
            Integer conditionItems =0;
            while (conditionItems < searchConditionList.size()){
                SearchCondition condition =new SearchCondition();
                condition = searchConditionList.get(conditionItems);
                if(condition.getMulCondition() !=1) {
                    List<SearchCondition> conditionMulList = new ArrayList<SearchCondition>();
                    Integer multiConditionIndex =0;
                    while (multiConditionIndex <condition.getMulCondition() ){
                        SearchCondition searchConditionMulti = new SearchCondition();
                        searchConditionMulti = searchConditionList.get(conditionItems);
                        conditionMulList.add(searchConditionMulti);
                        conditionItems++;
                        multiConditionIndex++;
                    }
                    query.addCriteria(createMultiCriteria(conditionMulList));
                    conditionItems++;
                }else {
                    query.addCriteria(createCriteria(condition));
                    conditionItems++;
                }
            }
            // TotalResult = mongoTempl().count(query, DeviceGPS.class, "DeviceGPS");
            TotalResult = mongoTempl().count(query, tClass, collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TotalResult;
    }

    public List<T> searchAndQuery(SearchFilter searchFilter){
        int page = searchFilter.getPage();
        int pagesize =searchFilter.getPageSize();
        List<SearchCondition> searchConditionList =searchFilter.getConditionList();
        List<T> objList=new ArrayList<T>();
        try {
            Query query = new Query();
            Integer conditionItems =0;
            while (conditionItems < searchConditionList.size()){
                SearchCondition condition =new SearchCondition();
                condition = searchConditionList.get(conditionItems);
                if(condition.getMulCondition() !=1) {
                    List<SearchCondition> conditionMulList = new ArrayList<SearchCondition>();
                    Integer multiConditionIndex =0;
                    while (multiConditionIndex <condition.getMulCondition() ){
                        SearchCondition searchConditionMulti = new SearchCondition();
                        searchConditionMulti = searchConditionList.get(conditionItems);
                        conditionMulList.add(searchConditionMulti);
                        conditionItems++;
                        multiConditionIndex++;
                    }
                    query.addCriteria(createMultiCriteria(conditionMulList));
                    conditionItems++;
                }else {
                    query.addCriteria(createCriteria(condition));
                    conditionItems++;
                }
            }
            /*Criteria criteria = new Criteria();
            Criteria criteria1 = Criteria.where("dateTracking");
            Criteria criteria2 = Criteria.where("dateTracking");
            //criteria.ne(searchConditionList.get(0).getFieldValue());
            criteria1.gte(searchConditionList.get(0).getFieldValue());
            criteria2.lte(searchConditionList.get(1).getFieldValue());
            criteria.andOperator(criteria1,criteria2);
            query.addCriteria(criteria);*/
           /* for(SearchCondition condition: searchFilter.getConditionList()) {
                query.addCriteria(createCriteria(condition));
            }*/
            query.skip(pagesize * (page - 1));
            query.limit(pagesize);
            if(!StringUtils.isEmpty(searchFilter.getSortFieldName())) {
                if(searchFilter.isDesc()) {
                    query.with(new Sort(Sort.Direction.DESC,  searchFilter.getSortFieldName()));
                }
                else {
                    query.with(new Sort(Sort.Direction.ASC,  searchFilter.getSortFieldName()));
                }
            }
            objList = mongoTempl().find(query, tClass, collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objList;
    }

    public List<T> searchAndQueryWithoutPaging(SearchFilter searchFilter){
        List<SearchCondition> searchConditionList =searchFilter.getConditionList();
        List<T> objList=new ArrayList<T>();
        try {
            Query query = new Query();
            Integer conditionItems =0;
            while (conditionItems < searchConditionList.size()){
                SearchCondition condition =new SearchCondition();
                condition = searchConditionList.get(conditionItems);
                if(condition.getMulCondition() !=1) {
                    List<SearchCondition> conditionMulList = new ArrayList<SearchCondition>();
                    Integer multiConditionIndex =0;
                    while (multiConditionIndex <condition.getMulCondition() ){
                        SearchCondition searchConditionMulti = new SearchCondition();
                        searchConditionMulti = searchConditionList.get(conditionItems);
                        conditionMulList.add(searchConditionMulti);
                        conditionItems++;
                        multiConditionIndex++;
                    }
                    query.addCriteria(createMultiCriteria(conditionMulList));
                    conditionItems++;
                }else {
                    query.addCriteria(createCriteria(condition));
                    conditionItems++;
                }
            }
            /*Criteria criteria = new Criteria();
            Criteria criteria1 = Criteria.where("dateTracking");
            Criteria criteria2 = Criteria.where("dateTracking");
            //criteria.ne(searchConditionList.get(0).getFieldValue());
            criteria1.gte(searchConditionList.get(0).getFieldValue());
            criteria2.lte(searchConditionList.get(1).getFieldValue());
            criteria.andOperator(criteria1,criteria2);
            query.addCriteria(criteria);*/
           /* for(SearchCondition condition: searchFilter.getConditionList()) {
                query.addCriteria(createCriteria(condition));
            }*/
            if(!StringUtils.isEmpty(searchFilter.getSortFieldName())) {
                if(searchFilter.isDesc()) {
                    query.with(new Sort(Sort.Direction.DESC,  searchFilter.getSortFieldName()));
                }
                else {
                    query.with(new Sort(Sort.Direction.ASC,  searchFilter.getSortFieldName()));
                }
            }
            objList = mongoTempl().find(query, tClass, collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objList;
    }
    public Criteria createMultiCriteria (List<SearchCondition> searchConditionList){
        Criteria criteria = new Criteria();
        Integer mulConditionTotal = searchConditionList.size();
        if (0== mulConditionTotal) {
            return  null;
        }
        List<Criteria> mulCriteriaList = new ArrayList<Criteria>();
        for(SearchCondition searchMulCondition : searchConditionList){
            Criteria criteriaTemp = Criteria.where(searchConditionList.get(0).getFieldName());
            String valueStr = "";
            Object value = searchMulCondition.getFieldValue();
            switch (searchMulCondition.getCompQueryOp()) {
                case eq:
                    criteriaTemp.is(value);
                    break;
                case ne:
                    criteriaTemp.ne(value);
                    break;
                case gt:
                    criteriaTemp.gt(value);
                    break;
                case lt:
                    criteriaTemp.lt(value);
                    break;
                case gte:
                    criteriaTemp.gte(value);
                    break;
                case lte:
                    criteriaTemp.lte(value);
                    break;
                case like:
                    valueStr = ParseUtil.parseString(value);
                    criteriaTemp.regex(valueStr,"i");
                    break;
                case nlike:
                    valueStr = ParseUtil.parseString(value);
                    criteriaTemp.not().regex(valueStr,"i");
                    break;
                default:
            }
            mulCriteriaList.add(criteriaTemp);
        }
        switch (mulCriteriaList.size()) {
            case 2:
                criteria.andOperator(mulCriteriaList.get(0), mulCriteriaList.get(1));
                break;
            case 3:
                criteria.andOperator(mulCriteriaList.get(0), mulCriteriaList.get(1), mulCriteriaList.get(2));
                break;
            case 4:
                criteria.andOperator(mulCriteriaList.get(0), mulCriteriaList.get(1), mulCriteriaList.get(2),mulCriteriaList.get(3));
                break;
            case 5:
                criteria.andOperator(mulCriteriaList.get(0), mulCriteriaList.get(1), mulCriteriaList.get(2), mulCriteriaList.get(3),mulCriteriaList.get(4));
                break;
            case 6:
                criteria.andOperator(mulCriteriaList.get(0), mulCriteriaList.get(1), mulCriteriaList.get(2), mulCriteriaList.get(3), mulCriteriaList.get(4),mulCriteriaList.get(5));
                break;
            default:
        }
        return criteria;
    }

    public Criteria createCriteria(SearchCondition condition){

        Object value = condition.getFieldValue();

        String valueStr = "";
        Criteria criteria = Criteria.where(condition.getFieldName());
        // eq, ne, gt, lt, gte , lte, like, nlike
        switch (condition.getCompQueryOp()) {
            case eq:
                criteria.is(value);
                break;
            case ne:
                criteria.ne(value);
                break;
            case gt:
                criteria.gt(value);
                break;
            case lt:
                criteria.lt(value);
                break;
            case gte:
                criteria.gte(value);
                break;
            case lte:
                criteria.lte(value);
                break;
            case like:
                valueStr = ParseUtil.parseString(value);
                valueStr=valueStr.trim();
                criteria.regex(valueStr,"i");
                break;
            case nlike:
                valueStr = ParseUtil.parseString(value);
                valueStr=valueStr.trim();
                criteria.not().regex(valueStr,"i");
                break;
            default:

        }
        return criteria;
    }
    // ---------------------- Function DAO---------------------
}
