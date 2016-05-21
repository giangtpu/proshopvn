package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import dao.ItemDAO;
import dao.RecieptIssueDAO;
import models.Item;
import models.JSON.ReceiptForm;
import models.JSON.SearchRecieptIssueData;
import models.RecieptIssue;
import models.SearchCondition;
import models.SearchFilter;
import models.forms.ItemForm;
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
import views.html.Admin_RecieptIssue_list;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by giangdaika on 16/05/2016.
 */
@Security.Authenticated(Secured.class)
public class RecieptIssueController extends AbstractController {
    Logger.ALogger logger = Logger.of(RecieptIssueController.class);
    @Inject
    public RecieptIssueDAO recieptIssueDAO;
    @Inject
    public ItemDAO itemDAO;

    public Result addReciept() {
        Form<ReceiptForm> recieptIssueFormForm = formFactory.form(ReceiptForm.class);

        ReceiptForm responeForm=new ReceiptForm();
        responeForm.setSuccess(false);
        if (recieptIssueFormForm.hasErrors()) {
            responeForm.setErrorMessage(getMessages().at("form.error"));
            return ok(Json.toJson(responeForm));
        }
        ReceiptForm recieptIssueForm=recieptIssueFormForm.bindFromRequest().get();


        RecieptIssue recieptIssue=new RecieptIssue();
        recieptIssueForm.fillToRecieptIssue(recieptIssue);

        Item item=itemDAO.getByKey(recieptIssue.getItem_id());
        if(item==null){
            responeForm.setErrorMessage(getMessages().at("Admin.Item.notfound"));
            return ok(Json.toJson(responeForm));
        }

        item.setQuantity(item.getQuantity()+recieptIssue.getQuantity());
        recieptIssue.setTotal(recieptIssue.getPrice()*recieptIssue.getQuantity());

        itemDAO.save(item);
        recieptIssueDAO.save(recieptIssue);
        responeForm.setSuccess(true);
        responeForm.setQuantity(item.getQuantity());
        responeForm.setItem_id(item.getId());
        return ok(Json.toJson(responeForm));
    }

    public Result addIssue() {
        Form<ReceiptForm> recieptIssueFormForm = formFactory.form(ReceiptForm.class);

        ReceiptForm responeForm=new ReceiptForm();
        responeForm.setSuccess(false);
        if (recieptIssueFormForm.hasErrors()) {
            responeForm.setErrorMessage(getMessages().at("form.error"));
            return ok(Json.toJson(responeForm));
        }
        ReceiptForm recieptIssueForm=recieptIssueFormForm.bindFromRequest().get();
        RecieptIssue recieptIssue=new RecieptIssue();
        recieptIssueForm.fillToRecieptIssue(recieptIssue);
        Item item=itemDAO.getByKey(recieptIssue.getItem_id());
        if(item==null){
            responeForm.setErrorMessage(getMessages().at("Admin.Item.notfound"));
            return ok(Json.toJson(responeForm));
        }

        if(item.getQuantity()-recieptIssue.getQuantity()<0){
            responeForm.setErrorMessage(getMessages().at("Admin.issue.overquantity"));
            return ok(Json.toJson(responeForm));
        }

        item.setQuantity(item.getQuantity()-recieptIssue.getQuantity());

        recieptIssue.setTotal(recieptIssue.getPrice()*recieptIssue.getQuantity());

        itemDAO.save(item);
        recieptIssueDAO.save(recieptIssue);
        responeForm.setSuccess(true);
        responeForm.setQuantity(item.getQuantity());
        responeForm.setItem_id(item.getId());
        return ok(Json.toJson(responeForm));
    }


    public SearchRecieptIssueData initSearchRecieptIssueData(int type){

        SearchRecieptIssueData searchRecieptIssueData = new SearchRecieptIssueData();
        List<RecieptIssue> recieptIssueList = new ArrayList<RecieptIssue>();
        SearchFilterForm searchFilterForm = new SearchFilterForm();


        SearchConditionForm searchConditionForm = new SearchConditionForm();
        searchConditionForm.setFieldType("Int");
        searchConditionForm.setFieldName("type");
        searchConditionForm.setCompQueryOp("=");
        searchConditionForm.setFieldValue(Integer.toString(type));
        List<SearchConditionForm> searchConditionFormList = new  ArrayList<SearchConditionForm>();
        searchConditionFormList.add(searchConditionForm);
        searchFilterForm.setConditionList(searchConditionFormList);
        //searchFilterForm.setPageSize(50);
        searchFilterForm.setSortFieldName("datePurchase");
        searchFilterForm.setIsDesc(true);
        List<String> filterTemp = new ArrayList<String>();
        filterTemp.add("type");
        searchFilterForm.setFilter(filterTemp);

        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setFieldName("type");
        searchCondition.setCompQueryOp(SearchCondition.CompQueryOp.eq);
        searchCondition.setFieldValue(type);

        SearchFilter searchFilter = new SearchFilter();
        List<SearchCondition> searchConditionList = new ArrayList<SearchCondition>();
        searchConditionList.add(searchCondition);
        searchFilter.setConditionList(searchConditionList);
        //searchFilter.setPageSize(50);
        searchFilter.setSortFieldName("datePurchase");
        searchFilter.setIsDesc(true);

        Integer pageSize = searchFilter.getPageSize();
        recieptIssueList = recieptIssueDAO.searchAndQuery(searchFilter);
        long countTotal =recieptIssueDAO.countOnQuery(searchFilter);
        long pageTotal =0;
        if(countTotal%pageSize !=0) {
            pageTotal = countTotal/pageSize +1;
        }else {
            pageTotal = countTotal/pageSize;
        }

        searchFilterForm.setTotalPage(pageTotal);
        searchFilterForm.setTotalResult(countTotal);
        searchFilterForm.setIsResetCondition(false);

        searchRecieptIssueData.setSearchFilterForm(searchFilterForm);
        searchRecieptIssueData.setRecieptIssueList(recieptIssueList);
        return searchRecieptIssueData;
    }

    public List<String> initSearchArray(){
        List<String> searchArray = new ArrayList<String>();
        searchArray.add("String");
        searchArray.add("id");
        searchArray.add("Int");
        searchArray.add("type");
        searchArray.add("String");
        searchArray.add("item_id");
        searchArray.add("String");
        searchArray.add("item_name");
        searchArray.add("Date");
        searchArray.add("datePurchase");
        searchArray.add("Date");
        searchArray.add("lastmodified");
        return searchArray;
    }


    public SearchRecieptIssueData filterSearchRecieptIssueData(SearchFilterForm searchFilterForm){
        List<SearchConditionForm> searchConditionFormList = searchFilterForm.getConditionList();
        List<String> filter = searchFilterForm.getFilter();
        String sortFieldName = searchFilterForm.getSortFieldName();
        boolean isDesc =searchFilterForm.isDesc();
        Integer page = searchFilterForm.getPage();
        Integer pageSize =searchFilterForm.getPageSize();
        boolean isResetPage =searchFilterForm.isResetPage();
        boolean isID =false;
        boolean isType =false;
        boolean isItem_id =false;
        boolean isItem_name =false;
        boolean isDatePurchase =false;
        boolean isLastmodified =false;

        if(filter !=null) {
            if (filter.size() != 0) {
                for (String s : filter) {

                    if (s.equals("id")) {
                        isID = true;
                    }
                    if (s.equals("type")) {
                        isType = true;
                    }
                    if (s.equals("item_id")) {
                        isItem_id = true;
                    }
                    if (s.equals("item_name")) {
                        isItem_name = true;
                    }
                    if (s.equals("datePurchase")) {
                        isDatePurchase = true;
                    }

                    if (s.equals("lastmodified")) {
                        isLastmodified = true;
                    }
                }
            }
        }

        List<RecieptIssue> recieptIssueList = new ArrayList<RecieptIssue>();
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
            if(!isType) {
                if(searchConditionForm.getFieldName().equals("type")) {
                    conditionItems++;
                    continue;
                }
            }
            if(!isItem_id) {
                if(searchConditionForm.getFieldName().equals("item_id")) {
                    conditionItems++;
                    continue;
                }
            }
            if(!isItem_name) {
                if(searchConditionForm.getFieldName().equals("item_name")) {
                    conditionItems++;
                    continue;
                }
            }
            if(!isDatePurchase) {
                if(searchConditionForm.getFieldName().equals("datePurchase")) {
                    conditionItems++;
                    continue;
                }
            }
            if(!isLastmodified) {
                if(searchConditionForm.getFieldName().equals("lastmodified")) {
                    conditionItems++;
                    continue;
                }
            }

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

//                for (SearchCondition searchCondition:searchConditionList){
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

        recieptIssueList = recieptIssueDAO.searchAndQuery(searchFilter);
        SearchRecieptIssueData searchRecieptIssueData = new SearchRecieptIssueData();
        long countTotal =recieptIssueDAO.countOnQuery(searchFilter);

        long pageTotal =0;
        if(countTotal%pageSize !=0) {
            pageTotal = countTotal/pageSize +1;
        }else {
            pageTotal = countTotal/pageSize;
        }
        searchFilterForm.setTotalPage(pageTotal);
        searchFilterForm.setTotalResult(countTotal);
        searchFilterForm.setIsResetCondition(false);

        searchRecieptIssueData.setSearchFilterForm(searchFilterForm);
        searchRecieptIssueData.setRecieptIssueList(recieptIssueList);


//        for (RecieptIssue recieptIssue:recieptIssueList){
//            System.out.println("recieptIssue:"+Json.toJson(recieptIssue));
//        }
//
//        for (SearchConditionForm searchConditionForm:searchRecieptIssueData.getSearchFilterForm().getConditionList()){
//            System.out.println("searchConditionForm:"+Json.toJson(searchConditionForm));
//        }
        return searchRecieptIssueData;
    }

    public Result recieptList(){
        return ok(Admin_RecieptIssue_list.render(getUserSession(),initSearchRecieptIssueData(RecieptIssue.Types.reciept.getCode()),initSearchArray(),"recieptList"));
    }


    public Result filterrecieptList() {
        Form<SearchFilterForm> searchGenericFormForm = formFactory.form(SearchFilterForm.class);
        SearchFilterForm searchFilterForm = searchGenericFormForm.bindFromRequest().get();
        return ok(Admin_RecieptIssue_list.render(getUserSession(),filterSearchRecieptIssueData(searchFilterForm),initSearchArray(),"recieptList"));
    }



    ////////////////////////////ISSUE////////////////////////////////////////////////////////

    public Result issueList(){
        return ok(Admin_RecieptIssue_list.render(getUserSession(),initSearchRecieptIssueData(RecieptIssue.Types.issue.getCode()),initSearchArray(),"issueList"));
    }

    public Result filterissueList() {
        Form<SearchFilterForm> searchGenericFormForm = formFactory.form(SearchFilterForm.class);
        SearchFilterForm searchFilterForm = searchGenericFormForm.bindFromRequest().get();
        return ok(Admin_RecieptIssue_list.render(getUserSession(),filterSearchRecieptIssueData(searchFilterForm),initSearchArray(),"issueList"));
    }

    //////////////////////DELETE///////////////////
    public Result deleteRecieptIssue(){
        JsonNode json = request().body().asJson();
        String id = json.findPath("id").textValue();

        ReceiptForm receiptForm=new ReceiptForm();
        receiptForm.setSuccess(false);

        RecieptIssue recieptIssue= recieptIssueDAO.getByKey(id);
        if (recieptIssue==null){
            receiptForm.setErrorMessage(getMessages().at("Admin.recieptissue.notfound"));
            return ok(Json.toJson(receiptForm));
        }
        Item item=itemDAO.getByKey(recieptIssue.getItem_id());
        if (item==null){
            receiptForm.setErrorMessage(getMessages().at("Admin.Item.notfound"));
            return ok(Json.toJson(receiptForm));
        }
        if (recieptIssue.getType()==RecieptIssue.Types.issue.getCode()){
            item.setQuantity(item.getQuantity()+recieptIssue.getQuantity());
        }else if(recieptIssue.getType()==RecieptIssue.Types.reciept.getCode()){
            item.setQuantity(item.getQuantity()-recieptIssue.getQuantity());
        }
        itemDAO.save(item);
        recieptIssueDAO.deleteByKey(id);
        receiptForm.setSuccess(true);
        receiptForm.setId(id);
        return ok(Json.toJson(receiptForm));
    }

    //////////////////////EDIT///////////////////
    public Result editRecieptIssue(){
        Form<ReceiptForm> recieptIssueFormForm = formFactory.form(ReceiptForm.class);

        ReceiptForm responeForm=new ReceiptForm();
        responeForm.setSuccess(false);
        if (recieptIssueFormForm.hasErrors()) {
            responeForm.setErrorMessage(getMessages().at("form.error"));
            return ok(Json.toJson(responeForm));
        }
        ReceiptForm recieptIssueForm=recieptIssueFormForm.bindFromRequest().get();
        RecieptIssue recieptIssue=recieptIssueDAO.getByKey(recieptIssueForm.getId());
        if(recieptIssue==null){
            responeForm.setErrorMessage(getMessages().at("Admin.recieptissue.notfound"));
            return ok(Json.toJson(responeForm));
        }

        Item item=itemDAO.getByKey(recieptIssue.getItem_id());
        if(item==null){
            responeForm.setErrorMessage(getMessages().at("Admin.Item.notfound"));
            return ok(Json.toJson(responeForm));
        }

        if(!StringUtils.isEmpty(recieptIssueForm.getDescription())){
            recieptIssue.setDescription(recieptIssueForm.getDescription());
        }
        if(!StringUtils.isEmpty(recieptIssueForm.getDatePurchase())){
            Date datePurc= (DateUtil.convertStringtoDate(
                    recieptIssueForm.getDatePurchase(),
                    DateUtil.TIME_ITEM));
            recieptIssue.setDatePurchase(datePurc);
        }
        if(!StringUtils.isEmpty(recieptIssueForm.getPrice())){
            recieptIssue.setPrice(recieptIssueForm.getPrice());
        }
        if(!StringUtils.isEmpty(recieptIssueForm.getQuantity())){
            if(recieptIssueForm.getQuantity()!=recieptIssue.getQuantity()){
                if(recieptIssue.getType()==recieptIssueForm.getType()){
                    if (recieptIssue.getType()==RecieptIssue.Types.issue.getCode()){
                        if(recieptIssueForm.getQuantity()>recieptIssue.getQuantity()){
                            int delta=recieptIssueForm.getQuantity()-recieptIssue.getQuantity();
                            item.setQuantity(item.getQuantity()-delta);
                        }else{
                            int delta=recieptIssue.getQuantity()-recieptIssueForm.getQuantity();
                            item.setQuantity(item.getQuantity()+delta);
                        }

                    }else if(recieptIssue.getType()==RecieptIssue.Types.reciept.getCode()){
                        if(recieptIssueForm.getQuantity()>recieptIssue.getQuantity()){
                            int delta=recieptIssueForm.getQuantity()-recieptIssue.getQuantity();
                            item.setQuantity(item.getQuantity()+delta);
                        }else{
                            int delta=recieptIssue.getQuantity()-recieptIssueForm.getQuantity();
                            item.setQuantity(item.getQuantity()-delta);
                        }
                    }
                    recieptIssue.setQuantity(recieptIssueForm.getQuantity());
                }else{
                    responeForm.setErrorMessage(getMessages().at("Admin.recieptissue.typeerror"));
                    return ok(Json.toJson(responeForm));
                }

            }
        }


        if(item.getQuantity()<0){
            responeForm.setErrorMessage(getMessages().at("Admin.issue.negativeItem"));
            return ok(Json.toJson(responeForm));
        }


        recieptIssue.setTotal(recieptIssue.getPrice()*recieptIssue.getQuantity());

        itemDAO.save(item);
        recieptIssueDAO.save(recieptIssue);
        responeForm.setSuccess(true);
        responeForm.setId(recieptIssue.getId());
        responeForm.setQuantity(recieptIssue.getQuantity());
        responeForm.setPrice(recieptIssue.getPrice());
        responeForm.setDatePurchase(DateUtil.getDateByFormat(recieptIssue.getDatePurchase(),DateUtil.TIME_ITEM));
        responeForm.setDescription(recieptIssue.getDescription());
        return ok(Json.toJson(responeForm));
    }
}
