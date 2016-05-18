package controllers;

import dao.ItemDAO;
import dao.PromotionDAO;
import models.Item;
import models.JSON.PromotionForm;
import models.JSON.ReceiptForm;
import models.JSON.ResultForm;
import models.JSON.SearchPromotionData;
import models.Promotion;
import models.SearchCondition;
import models.SearchFilter;
import models.forms.SearchConditionForm;
import models.forms.SearchFilterForm;
import org.springframework.util.StringUtils;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security;
import utils.DateUtil;
import utils.ParseUtil;
import views.html.Admin_promotion_list;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by giangdaika on 18/05/2016.
 */
@Security.Authenticated(Secured.class)
public class PromotionController extends AbstractController  {
    Logger.ALogger logger = Logger.of(PromotionController.class);
    @Inject
    public ItemDAO itemDAO;
    @Inject
    public PromotionDAO promotionDAO;

    public Result addPromotion() {
        Form<PromotionForm> promotionFormForm = formFactory.form(PromotionForm.class);
        ResultForm responeForm=new ResultForm();
        responeForm.setSuccess(false);
        if (promotionFormForm.hasErrors()) {
            responeForm.setErrorMessage(getMessages().at("form.error"));
            return ok(Json.toJson(responeForm));
        }

        PromotionForm promotionForm=promotionFormForm.bindFromRequest().get();

        Promotion promotion=new Promotion();

        boolean fill=promotionForm.fillToPromotion(promotion);
        if(!fill){
            responeForm.setErrorMessage(getMessages().at("form.error"));
            return ok(Json.toJson(responeForm));
        }

        Item item=itemDAO.getByKey(promotion.getItem_id());
        if(item==null){
            responeForm.setErrorMessage(getMessages().at("Admin.Item.notfound"));
            return ok(Json.toJson(responeForm));
        }

//        double discountPrice=item.getPrice_sell()-(item.getPrice_sell()*promotion.getDiscountRate());


        promotionDAO.save(promotion);

        responeForm.setSuccess(true);

        return ok(Json.toJson(responeForm));
    }

    public Result promotionList(){
        SearchPromotionData searchPromotionData = new SearchPromotionData();
        List<Promotion> promotionList = new ArrayList<Promotion>();
        SearchFilterForm searchFilterForm = new SearchFilterForm();

        SearchConditionForm searchConditionForm = new SearchConditionForm();
        searchConditionForm.setFieldName("id");
        searchConditionForm.setCompQueryOp("=");
        searchConditionForm.setFieldValue("");
        List<SearchConditionForm> searchConditionFormList = new  ArrayList<SearchConditionForm>();
        searchConditionFormList.add(searchConditionForm);
        searchFilterForm.setConditionList(searchConditionFormList);
        //searchFilterForm.setPageSize(50);
        searchFilterForm.setSortFieldName("lastModified");
        searchFilterForm.setIsDesc(true);
        List<String> filterTemp = new ArrayList<String>();
        filterTemp.add("id");
        searchFilterForm.setFilter(filterTemp);

        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setFieldName("id");
        searchCondition.setCompQueryOp(SearchCondition.CompQueryOp.like);
        searchCondition.setFieldValue("");

        SearchFilter searchFilter = new SearchFilter();
        List<SearchCondition> searchConditionList = new ArrayList<SearchCondition>();
        searchConditionList.add(searchCondition);
        searchFilter.setConditionList(searchConditionList);
        //searchFilter.setPageSize(50);
        searchFilter.setSortFieldName("lastModified");
        searchFilter.setIsDesc(true);

        Integer pageSize = searchFilter.getPageSize();
        promotionList = promotionDAO.searchAndQuery(searchFilter);
        long countTotal =promotionDAO.countOnQuery(searchFilter);
        long pageTotal =0;
        if(countTotal%pageSize !=0) {
            pageTotal = countTotal/pageSize +1;
        }else {
            pageTotal = countTotal/pageSize;
        }

        searchFilterForm.setTotalPage(pageTotal);
        searchFilterForm.setTotalResult(countTotal);
        searchFilterForm.setIsResetCondition(false);

        searchPromotionData.setSearchFilterForm(searchFilterForm);
        searchPromotionData.setPromotionList(promotionList);
        List<String> searchArray = new ArrayList<String>();
        searchArray.add("String");
        searchArray.add("id");
        searchArray.add("String");
        searchArray.add("item_id");
        searchArray.add("String");
        searchArray.add("item_name");
        searchArray.add("String");
        searchArray.add("category_id");
        searchArray.add("Double");
        searchArray.add("discountRate");
        searchArray.add("Date");
        searchArray.add("lastModified");
        searchArray.add("Date");
        searchArray.add("datePromotionStart");
        searchArray.add("Date");
        searchArray.add("datePromotionEnd");

        return ok(Admin_promotion_list.render(getUserSession(),searchPromotionData,searchArray));
    }


    public Result filterPromotionList() {
        Form<SearchFilterForm> searchGenericFormForm = formFactory.form(SearchFilterForm.class);
        SearchFilterForm searchFilterForm = searchGenericFormForm.bindFromRequest().get();

        List<SearchConditionForm> searchConditionFormList = searchFilterForm.getConditionList();
        List<String> filter = searchFilterForm.getFilter();
        String sortFieldName = searchFilterForm.getSortFieldName();
        boolean isDesc =searchFilterForm.isDesc();
        Integer page = searchFilterForm.getPage();
        Integer pageSize =searchFilterForm.getPageSize();
        boolean isResetPage =searchFilterForm.isResetPage();
        boolean isID =false;
        boolean isItemID =false;
        boolean isItemName =false;
        boolean isCategoryID =false;
        boolean isDiscountRate =false;
        boolean isLastModified =false;
        boolean isDatePromotionStart=false;
        boolean isDatePromotionEnd =false;


        if(filter !=null) {
            if (filter.size() != 0) {
                for (String s : filter) {
                    if (s.equals("id")) {
                        isID = true;
                    }
                    if (s.equals("item_id")) {
                        isItemID = true;
                    }
                    if (s.equals("item_name")) {
                        isItemName = true;
                    }
                    if (s.equals("category_id")) {
                        isCategoryID = true;
                    }
                    if (s.equals("discountRate")) {
                        isDiscountRate = true;
                    }

                    if (s.equals("lastModified")) {
                        isLastModified = true;
                    }

                    if (s.equals("datePromotionStart")) {
                        isDatePromotionStart = true;
                    }
                    if (s.equals("datePromotionEnd")) {
                        isDatePromotionEnd = true;
                    }

                }
            }
        }

//        System.out.println("isID:"+isID);System.out.println("isName:"+isName);System.out.println("isCategory_id:"+isCategory_id);
//        System.out.println("isCategory_name:"+isCategory_name);System.out.println("isLastModified:"+isLastModified);System.out.println("isOrigin:"+isOrigin);
//        System.out.println("isRating:"+isRating);System.out.println("isPromotion:"+isPromotion);System.out.println("isPrice_sell:"+isPrice_sell);
//        System.out.println("isWarrantyTime:"+isWarrantyTime);

        List<Promotion> promotionList = new ArrayList<Promotion>();
        SearchFilter searchFilter = new SearchFilter();
        if(isResetPage) {
            page=1;
            searchFilterForm.setPage(1);
        }
        searchFilter.setPage(page);
        searchFilter.setPageSize(pageSize);
        List<SearchCondition> searchConditionList = new ArrayList<SearchCondition>();
        Integer conditionItems =0;

        while( conditionItems < searchConditionFormList.size()) {
            //System.out.println("conditionItems"+conditionItems);
            SearchConditionForm searchConditionForm = searchConditionFormList.get(conditionItems);
            if(!isID) {
                if(searchConditionForm.getFieldName().equals("id")) {
                    conditionItems++;
                    continue;
                }
            }
            if(!isItemID) {
                if(searchConditionForm.getFieldName().equals("item_id")) {
                    conditionItems++;
                    continue;
                }
            }

            if(!isItemName) {
                if(searchConditionForm.getFieldName().equals("item_name")) {
                    conditionItems++;
                    continue;
                }
            }
            if(!isCategoryID) {
                if(searchConditionForm.getFieldName().equals("category_id")) {
                    conditionItems++;
                    continue;
                }
            }
            if(!isDiscountRate) {
                if(searchConditionForm.getFieldName().equals("discountRate")) {
                    conditionItems++;
                    continue;
                }
            }
            if(!isLastModified) {
                if(searchConditionForm.getFieldName().equals("lastModified")) {
                    conditionItems++;
                    continue;
                }
            }
            if(!isDatePromotionStart) {
                if(searchConditionForm.getFieldName().equals("datePromotionStart")) {
                    conditionItems++;
                    continue;
                }
            }
            if(!isDatePromotionEnd) {
                if(searchConditionForm.getFieldName().equals("datePromotionEnd")) {
                    conditionItems++;
                    continue;
                }
            }


//            System.out.println("searchConditionForm.getFieldName:"+searchConditionForm.getFieldName());
//            System.out.println("searchConditionForm.getFieldType:"+searchConditionForm.getFieldType());
//            System.out.println("searchConditionForm.getFieldValue:"+searchConditionForm.getFieldValue());
//            System.out.println("searchConditionForm.getCompQueryOp:"+searchConditionForm.getCompQueryOp());
//            System.out.println("-----------------------------------");
            if (searchConditionForm.getFieldType().equals("String") ) {
                SearchCondition condition = new SearchCondition();
                condition.setFieldName(searchConditionForm.getFieldName());
                if(StringUtils.isEmpty(searchConditionForm.getFieldValue().trim())){
                    conditionItems++;
                    continue;
                }
                if (searchConditionForm.getCompQueryOp().equals("=")) {
                    condition.setCompQueryOp(SearchCondition.CompQueryOp.like);
                } else if (searchConditionForm.getCompQueryOp().equals("<>")) {
                    condition.setCompQueryOp(SearchCondition.CompQueryOp.nlike);
                }
                condition.setFieldValue(searchConditionForm.getFieldValue());
                searchConditionList.add(condition);
                conditionItems++;

            }else  if (searchConditionForm.getFieldType().equals("Date") ) {
                String datefromString = searchConditionForm.getFieldValue();
                conditionItems++;
                searchConditionForm = searchConditionFormList.get(conditionItems);
                String datetoString= searchConditionForm.getFieldValue();
                conditionItems++;
                if (StringUtils.isEmpty(datefromString) &&StringUtils.isEmpty(datetoString)  ) {
                    continue;
                }else if( !StringUtils.isEmpty(datefromString) && StringUtils.isEmpty(datetoString) ){
                    Date datefrom = (DateUtil.convertStringtoDate(datefromString, DateUtil.TIME_ITEM));
                    SearchCondition condition = new SearchCondition();
                    condition.setFieldName(searchConditionForm.getFieldName());
                    condition.setCompQueryOp(SearchCondition.CompQueryOp.gte); //greater than begin time
                    condition.setFieldValue(datefrom);
                    searchConditionList.add(condition);
                }else if( StringUtils.isEmpty(datefromString) && !StringUtils.isEmpty(datetoString) ) {
                    Date dateto = (DateUtil.convertStringtoDate(datetoString, DateUtil.TIME_ITEM));
                    SearchCondition condition = new SearchCondition();
                    condition.setFieldName(searchConditionForm.getFieldName());
                    condition.setCompQueryOp(SearchCondition.CompQueryOp.lte); //greater than begin time
                    condition.setFieldValue(dateto);
                    searchConditionList.add(condition);
                }else if( !StringUtils.isEmpty(datefromString) && !StringUtils.isEmpty(datetoString) ) {
                    Date datefrom = (DateUtil.convertStringtoDate(datefromString, DateUtil.TIME_ITEM));
                    Date dateto = (DateUtil.convertStringtoDate(datetoString, DateUtil.TIME_ITEM));
                    SearchCondition condition = new SearchCondition();
                    condition.setFieldName(searchConditionForm.getFieldName());
                    condition.setCompQueryOp(SearchCondition.CompQueryOp.gte); //greater than begin time
                    condition.setFieldValue(datefrom);
                    condition.setMulCondition(2);
                    searchConditionList.add(condition);
                    SearchCondition conditionTo = new SearchCondition();
                    conditionTo.setFieldName(searchConditionForm.getFieldName());
                    conditionTo.setCompQueryOp(SearchCondition.CompQueryOp.lte); // less than end time
                    conditionTo.setFieldValue(dateto);
                    searchConditionList.add(conditionTo);
                }
            }else if (searchConditionForm.getFieldType().equals("Int") ) {
                String valueString = searchConditionForm.getFieldValue();

                Integer valueInt = ParseUtil.parseInt(valueString);
                if (StringUtils.isEmpty(valueString)) {
                    conditionItems+=2;
                    continue;
                }
                if (searchConditionForm.getCompQueryOp().equals("=")) {
                    SearchCondition condition = new SearchCondition();
                    condition.setFieldName(searchConditionForm.getFieldName());
                    condition.setCompQueryOp(SearchCondition.CompQueryOp.eq);
                    condition.setFieldValue(valueInt);
                    searchConditionList.add(condition);
                    conditionItems+=2;
                } else if (searchConditionForm.getCompQueryOp().equals("<>")) {
                    SearchCondition condition = new SearchCondition();
                    condition.setFieldName(searchConditionForm.getFieldName());
                    condition.setCompQueryOp(SearchCondition.CompQueryOp.ne);
                    condition.setFieldValue(valueInt);
                    searchConditionList.add(condition);
                    conditionItems+=2;
                } else if (searchConditionForm.getCompQueryOp().equals(">=")) {
                    SearchCondition condition = new SearchCondition();
                    condition.setFieldName(searchConditionForm.getFieldName());
                    condition.setCompQueryOp(SearchCondition.CompQueryOp.gte);
                    condition.setFieldValue(valueInt);
                    searchConditionList.add(condition);
                    conditionItems+=2;
                } else if (searchConditionForm.getCompQueryOp().equals("<=")) {
                    SearchCondition condition = new SearchCondition();
                    condition.setFieldName(searchConditionForm.getFieldName());
                    condition.setCompQueryOp(SearchCondition.CompQueryOp.lte);
                    condition.setFieldValue(valueInt);
                    searchConditionList.add(condition);
                    conditionItems+=2;
                } else if (searchConditionForm.getCompQueryOp().equals("between")) {
                    SearchCondition condition = new SearchCondition();
                    condition.setFieldName(searchConditionForm.getFieldName());
                    condition.setCompQueryOp(SearchCondition.CompQueryOp.gte); //greater than begin time
                    condition.setFieldValue(valueInt);
                    condition.setMulCondition(2);
                    searchConditionList.add(condition);
                    conditionItems++;
                    searchConditionForm = searchConditionFormList.get(conditionItems);
                    SearchCondition conditionTo = new SearchCondition();
                    conditionTo.setFieldName(searchConditionForm.getFieldName());
                    conditionTo.setCompQueryOp(SearchCondition.CompQueryOp.lte); // less than end time

                    String valueStringNext = searchConditionForm.getFieldValue();
                    Integer valueIntNext = ParseUtil.parseInt(valueStringNext);
                    conditionTo.setFieldValue(valueIntNext);
                    searchConditionList.add(conditionTo);
                    conditionItems++;
                }
            }
            else if (searchConditionForm.getFieldType().equals("Double") ) {
                String valueString = searchConditionForm.getFieldValue();
                double valueDouble = ParseUtil.parseDouble(valueString);
                if (StringUtils.isEmpty(valueString)) {
                    conditionItems+=2;
                    continue;
                }
                if (searchConditionForm.getCompQueryOp().equals("=")) {
                    SearchCondition condition = new SearchCondition();
                    condition.setFieldName(searchConditionForm.getFieldName());
                    condition.setCompQueryOp(SearchCondition.CompQueryOp.eq);
                    condition.setFieldValue(valueDouble);
                    searchConditionList.add(condition);
                    conditionItems+=2;
                } else if (searchConditionForm.getCompQueryOp().equals("<>")) {
                    SearchCondition condition = new SearchCondition();
                    condition.setFieldName(searchConditionForm.getFieldName());
                    condition.setCompQueryOp(SearchCondition.CompQueryOp.ne);
                    condition.setFieldValue(valueDouble);
                    searchConditionList.add(condition);
                    conditionItems+=2;
                } else if (searchConditionForm.getCompQueryOp().equals(">=")) {
                    SearchCondition condition = new SearchCondition();
                    condition.setFieldName(searchConditionForm.getFieldName());
                    condition.setCompQueryOp(SearchCondition.CompQueryOp.gte);
                    condition.setFieldValue(valueDouble);
                    searchConditionList.add(condition);
                    conditionItems+=2;
                } else if (searchConditionForm.getCompQueryOp().equals("<=")) {
                    SearchCondition condition = new SearchCondition();
                    condition.setFieldName(searchConditionForm.getFieldName());
                    condition.setCompQueryOp(SearchCondition.CompQueryOp.lte);
                    condition.setFieldValue(valueDouble);
                    searchConditionList.add(condition);
                    conditionItems+=2;
                } else if (searchConditionForm.getCompQueryOp().equals("between")) {
                    SearchCondition condition = new SearchCondition();
                    condition.setFieldName(searchConditionForm.getFieldName());
                    condition.setCompQueryOp(SearchCondition.CompQueryOp.gte); //greater than begin time
                    condition.setFieldValue(valueDouble);
                    condition.setMulCondition(2);
                    searchConditionList.add(condition);
                    conditionItems++;
                    searchConditionForm = searchConditionFormList.get(conditionItems);
                    SearchCondition conditionTo = new SearchCondition();
                    conditionTo.setFieldName(searchConditionForm.getFieldName());
                    conditionTo.setCompQueryOp(SearchCondition.CompQueryOp.lte); // less than end time

                    String valueStringNext = searchConditionForm.getFieldValue();
                    Integer valueIntNext = ParseUtil.parseInt(valueStringNext);
                    conditionTo.setFieldValue(valueIntNext);
                    searchConditionList.add(conditionTo);
                    conditionItems++;
                }
            }else if (searchConditionForm.getFieldType().equals("Boolean") ) {
                if(searchConditionForm.getFieldValue().equals("any")) {
                    conditionItems++;
                    continue;
                }
                SearchCondition condition = new SearchCondition();
                condition.setFieldName(searchConditionForm.getFieldName());
                condition.setCompQueryOp(SearchCondition.CompQueryOp.eq);
                if (searchConditionForm.getFieldValue().equals("true")) {
                    condition.setFieldValue(true);
                } else if (searchConditionForm.getFieldValue().equals("false")) {
                    condition.setFieldValue(false);
                }
                searchConditionList.add(condition);
                conditionItems++;
            }
            else {
                conditionItems++;
            }
        }


//        for (SearchCondition searchCondition:searchConditionList){
//            System.out.println("getFieldName:"+searchCondition.getFieldName());
//            System.out.println("getFieldType:"+searchCondition.getFieldType());
//            System.out.println("getFieldValue:"+searchCondition.getFieldValue());
//            System.out.println("getCompQueryOp:"+searchCondition.getCompQueryOp());
//            System.out.println("getMulCondition:"+searchCondition.getMulCondition());
//            System.out.println("-----------------------------------");
//        }

        searchFilter.setConditionList(searchConditionList);
        searchFilter.setSortFieldName(sortFieldName);
        searchFilter.setIsDesc(isDesc);

        promotionList = promotionDAO.searchAndQuery(searchFilter);
        SearchPromotionData searchPromotionData = new SearchPromotionData();
        long countTotal =promotionDAO.countOnQuery(searchFilter);

        long pageTotal =0;
        if(countTotal%pageSize !=0) {
            pageTotal = countTotal/pageSize +1;
        }else {
            pageTotal = countTotal/pageSize;
        }
        searchFilterForm.setTotalPage(pageTotal);
        searchFilterForm.setTotalResult(countTotal);
        searchFilterForm.setIsResetCondition(false);

        searchPromotionData.setSearchFilterForm(searchFilterForm);
        searchPromotionData.setPromotionList(promotionList);

//        System.out.println("itemList"+itemList.size());

        List<String> searchArray = new ArrayList<String>();
        searchArray.add("String");
        searchArray.add("id");
        searchArray.add("String");
        searchArray.add("item_id");
        searchArray.add("String");
        searchArray.add("item_name");
        searchArray.add("String");
        searchArray.add("category_id");
        searchArray.add("Double");
        searchArray.add("discountRate");
        searchArray.add("Date");
        searchArray.add("lastModified");
        searchArray.add("Date");
        searchArray.add("datePromotionStart");
        searchArray.add("Date");
        searchArray.add("datePromotionEnd");

        return ok(Admin_promotion_list.render(getUserSession(),searchPromotionData,searchArray));
    }

}
