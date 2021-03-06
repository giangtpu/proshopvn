package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import dao.CategoryDAO;
import dao.ItemDAO;
import models.Category;
import models.Item;
import models.JSON.*;
import models.SearchCondition;
import models.SearchFilter;
import models.forms.ItemForm;
import models.forms.ItemImageUploadForm;
import models.forms.SearchConditionForm;
import models.forms.SearchFilterForm;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.util.StringUtils;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import utils.*;
import views.html.Admin_item_add;
import views.html.Admin_item_info;
import views.html.Admin_item_list;


import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created by giangbb on 10/05/2016.
 */
@Security.Authenticated(Secured.class)
public class ItemController extends AbstractController {
    Logger.ALogger logger = Logger.of(ItemController.class);

    @Inject
    public CategoryDAO categoryDAO;

    @Inject
    public ItemDAO itemDAO;

    public Result addItemView() {
        String description_id = ItemHelper.generateId();
        return ok(Admin_item_add.render(getUserSession(), getMenu(), description_id));
    }

    public Result addItem() {
        Form<ItemForm> itemFormForm = formFactory.form(ItemForm.class);
        if (itemFormForm.hasErrors()) {
            flash("failed", getMessages().at("form.error"));
            return redirect(routes.ItemController.addItemView());
        }
        ItemForm itemForm = itemFormForm.bindFromRequest().get();

        Item item = new Item();
        item.setName(itemForm.getName());
        String idGenerate = ItemHelper.generateItemIDbyName(itemForm.getName());
        if (itemDAO.getByKey(idGenerate) != null) {
            java.util.concurrent.CompletionStage<Boolean> promiseOfDelImg = CompletableFuture.supplyAsync(
                    () -> ImageUtil.delImageWithPrefix(itemForm.getDescription_id(), ItemHelper.itemImageFolderPath)
            );
            flash("failed", getMessages().at("Admin.Item.existed"));
            return redirect(routes.ItemController.addItemView());
        }

        item.setId(idGenerate);


        boolean fillsuccess = itemForm.fillToItem(item);
        if (!fillsuccess) {
            java.util.concurrent.CompletionStage<Boolean> promiseOfDelImg = CompletableFuture.supplyAsync(
                    () -> ImageUtil.delImageWithPrefix(itemForm.getDescription_id(), ItemHelper.itemImageFolderPath)
            );
            flash("failed", getMessages().at("form.error"));
            return redirect(routes.ItemController.addItemView());
        }

        Category category= categoryDAO.getByKey(item.getCategory_id());
        if (category==null) {
            java.util.concurrent.CompletionStage<Boolean> promiseOfDelImg = CompletableFuture.supplyAsync(
                    () -> ImageUtil.delImageWithPrefix(itemForm.getDescription_id(), ItemHelper.itemImageFolderPath)
            );
            flash("failed", getMessages().at("Admin.Category.notfound"));
            return redirect(routes.ItemController.addItemView());
        }
        if (!category.isItemCategory()){
            flash("failed", getMessages().at("Admin.Category.notforItem"));
            return redirect(routes.ItemController.addItemView());
        }
        item.setCategory_name(category.getName());

        if (itemForm.getDescription_img()!=null){
            item.setDescription_img(itemForm.getDescription_img());
        }

        if (itemForm.getRelatedItems()!=null){
            item.setRelatedItems(itemForm.getRelatedItems());

        }


        writeItemImageTodisk(itemForm, item);
        itemDAO.save(item);

        flash("success", getMessages().at("Admin.addsuccess"));
        return redirect(routes.ItemController.addItemView());
    }

    public Result saveitemImageDescription() {
        Form<ItemImageUploadForm> itemFormForm = formFactory.form(ItemImageUploadForm.class);
        ItemImageUpload jsonRespone = new ItemImageUpload();
        if (itemFormForm.hasErrors()) {
            return ok(Json.toJson(jsonRespone));
        }
        ItemImageUploadForm itemForm = itemFormForm.bindFromRequest().get();

        jsonRespone.setIssuccess(false);
        if (itemForm.getFileData() == null || StringUtils.isEmpty(itemForm.getDescription_id())) {
            return ok(Json.toJson(jsonRespone));
        }

//        Http.MultipartFormData.FilePart fileData = itemForm.getFileData();
        String fileName = itemForm.getFileName();
        String contentType = itemForm.getContentType();
        File file = itemForm.getFile();
        itemForm.setFileClientPath(file.getPath());
        String imageName = itemForm.getDescription_id() + "-" + fileName;
        try {
            // write image file to disk
            ImageUtil.writeAvatarToDiskLargeSize(imageName, ItemHelper.itemImageFolderPath, file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        jsonRespone.setIssuccess(true);
        jsonRespone.setFilename(imageName);
        String url = ItemHelper.weblinkroot + ItemHelper.itemImageLinkPath + "/" + imageName;
        jsonRespone.setUrl(url);

        return ok(Json.toJson(jsonRespone));
    }

    public Result deleteDescripFilePrefix() {             //delete when unload page
        Form<ItemImageUploadForm> itemFormForm = formFactory.form(ItemImageUploadForm.class);
        if (itemFormForm.hasErrors()) {
            return ok();
        }
        ItemImageUploadForm itemForm = itemFormForm.bindFromRequest().get();

        if (StringUtils.isEmpty(itemForm.getDescription_id())) {
            return ok();
        }

        String prefix = itemForm.getDescription_id();


        java.util.concurrent.CompletionStage<Boolean> promiseOfDelImg = CompletableFuture.supplyAsync(
                () -> ImageUtil.delImageWithPrefix(prefix, ItemHelper.itemImageFolderPath)
        );

        return ok();
    }

    public Result deleteDescripFile() {             //delete when unload page
        Form<ItemImageUploadForm> itemFormForm = formFactory.form(ItemImageUploadForm.class);
        if (itemFormForm.hasErrors()) {
            return ok();
        }
        ItemImageUploadForm itemForm = itemFormForm.bindFromRequest().get();

        if (StringUtils.isEmpty(itemForm.getFileNameToDel())) {
            return ok();
        }

        String fileNameToDel = itemForm.getFileNameToDel();


        java.util.concurrent.CompletionStage<Boolean> promiseOfDelImg = CompletableFuture.supplyAsync(
                () -> ImageUtil.delImage(fileNameToDel, ItemHelper.itemImageFolderPath)
        );

        return ok();
    }


    ////////////////////////////////////////////INFO////////////////////////

    public Result infoitem(String id) {
        Item item=itemDAO.getByKey(id);
        if (item==null)
        {
            return ok("item null");
        }
        unescapeHTML4Item(item);

        return ok(Admin_item_info.render(getUserSession(), getMenu(),item));
    }

    public void deleteNotUserDescriptImageWhenUpdateFail(String[] itemimgs, String[] formimgs){
            for (String img:formimgs){
                boolean initem=false;
                for (String itemimg:itemimgs){
                    if (img.equals(itemimg)){
                        initem=true;
                        break;
                    }
                }
                if(!initem){
                    java.util.concurrent.CompletionStage<Boolean> promiseOfDelImg = CompletableFuture.supplyAsync(
                            () -> ImageUtil.delImage(img, ItemHelper.itemImageFolderPath)
                    );
                }
            }
    }

    public Result updateItem() {
        Form<ItemForm> itemFormForm = formFactory.form(ItemForm.class);
        if (itemFormForm.hasErrors()) {
            flash("failed", getMessages().at("form.error"));
            return redirect(routes.ItemController.addItemView());
        }
        ItemForm itemForm = itemFormForm.bindFromRequest().get();

        Item item=itemDAO.getByKey(itemForm.getId());
        if (item==null) {
            flash("failed", getMessages().at("Admin.Item.notfound"));
            return redirect(routes.ItemController.infoitem(itemForm.getId()));
        }

        boolean fillsuccess = itemForm.fillToItem(item);
        if (!fillsuccess) {
            deleteNotUserDescriptImageWhenUpdateFail(item.getDescription_img(),itemForm.getDescription_img());
            flash("failed", getMessages().at("form.error"));
            return redirect(routes.ItemController.infoitem(itemForm.getId()));
        }

        Category category= categoryDAO.getByKey(item.getCategory_id());
        if (category==null) {
            deleteNotUserDescriptImageWhenUpdateFail(item.getDescription_img(),itemForm.getDescription_img());
            flash("failed", getMessages().at("Admin.Category.notfound"));
            return redirect(routes.ItemController.infoitem(itemForm.getId()));
        }
        if (!category.isItemCategory()){
            flash("failed", getMessages().at("Admin.Category.notforItem"));
            return redirect(routes.ItemController.addItemView());
        }
        item.setCategory_name(category.getName());

        if (itemForm.getDescription_img()!=null){
            item.setDescription_img(itemForm.getDescription_img());
        }
        else{
            String [] decs={};
            item.setDescription_img(decs);
        }


        if (itemForm.getRelatedItems()!=null){
            item.setRelatedItems(itemForm.getRelatedItems());
        }else{
            String [] rels={};
            item.setDescription_img(rels);
        }

        if (!item.getName().equals(itemForm.getName())){
            item.setName(itemForm.getName());
            String idGenerate = ItemHelper.generateItemIDbyName(itemForm.getName());
            if (itemDAO.getByKey(idGenerate) != null) {
                deleteNotUserDescriptImageWhenUpdateFail(item.getDescription_img(),itemForm.getDescription_img());
                flash("failed", getMessages().at("Admin.Item.existed"));
                return redirect(routes.ItemController.infoitem(itemForm.getId()));
            }
            item.setId(idGenerate);

            itemDAO.deleteByKey(itemForm.getId());
        }


        writeItemImageTodisk(itemForm, item);
        item.setLastModified(DateUtil.now());
        itemDAO.save(item);

        flash("success", getMessages().at("Admin.Updatesuccess"));
        return redirect(routes.ItemController.infoitem(item.getId()));
    }

    ///////////////////////RELATED ITEM////////////////////
    public Result findRelatedItem() {
        JsonNode json = request().body().asJson();
        String related_category_id = json.findPath("related_category_id").textValue();
        List<Item> items=itemDAO.getbyfield("category_id",related_category_id);
        RelatedItemForm relatedItemForm=new RelatedItemForm();
        relatedItemForm.setSuccess(false);
        if(items!=null && items.size()>0){
            relatedItemForm.setSuccess(true);
            List<ItemNameId> itemNameIds=new ArrayList<ItemNameId>();
            for (Item item:items){
                ItemNameId itemNameId=new ItemNameId();
                itemNameId.setId(item.getId());
                itemNameId.setName(item.getName());
                itemNameIds.add(itemNameId);
            }
            relatedItemForm.setItemNameIds(itemNameIds);
        }

        return ok(Json.toJson(relatedItemForm));
    }


    /////////////////////////////////ITEM LIST///////////////////////////////////////////
    public Result itemList(){
        SearchItemData searchItemData = new SearchItemData();
        List<Item> itemList = new ArrayList<Item>();
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
        itemList = itemDAO.searchAndQuery(searchFilter);
        long countTotal =itemDAO.countOnQuery(searchFilter);
        long pageTotal =0;
        if(countTotal%pageSize !=0) {
            pageTotal = countTotal/pageSize +1;
        }else {
            pageTotal = countTotal/pageSize;
        }

        searchFilterForm.setTotalPage(pageTotal);
        searchFilterForm.setTotalResult(countTotal);
        searchFilterForm.setIsResetCondition(false);

        searchItemData.setSearchFilterForm(searchFilterForm);
        searchItemData.setItemList(itemList);
        List<String> searchArray = new ArrayList<String>();
        searchArray.add("String");
        searchArray.add("id");
        searchArray.add("String");
        searchArray.add("name");
        searchArray.add("String");
        searchArray.add("category_id");
        searchArray.add("String");
        searchArray.add("category_name");
        searchArray.add("Date");
        searchArray.add("lastModified");
        searchArray.add("String");
        searchArray.add("origin");
        searchArray.add("Int");
        searchArray.add("warrantyTime");
        searchArray.add("Double");
        searchArray.add("price_sell");
        searchArray.add("Double");
        searchArray.add("rating");
//        searchArray.add("Boolean");
//        searchArray.add("promotion");
//        searchArray.add("Int");
//        searchArray.add("quantity");
        //        searchArray.add("String");
//        searchArray.add("material");
//        searchArray.add("String");
//        searchArray.add("producer");
        //        searchArray.add("Double");
//        searchArray.add("discountRate");
//        searchArray.add("Date");
//        searchArray.add("datePromotionStart");
//        searchArray.add("Date");
//        searchArray.add("datePromotionEnd");



        return ok(Admin_item_list.render(getUserSession(),searchItemData,searchArray));

    }


    public Result filteritemList() {
//        Form<SearchFilterForm> searchGenericFormForm = Form.form(SearchFilterForm.class).fill(new SearchFilterForm()).bindFromRequest();
        Form<SearchFilterForm> searchGenericFormForm = formFactory.form(SearchFilterForm.class);
        SearchFilterForm searchFilterForm = searchGenericFormForm.bindFromRequest().get();

        List<SearchConditionForm> searchConditionFormList = searchFilterForm.getConditionList();
        List<String> filter = searchFilterForm.getFilter();
        String sortFieldName = searchFilterForm.getSortFieldName();
        boolean isDesc =searchFilterForm.isDesc();
        Integer page = searchFilterForm.getPage();
        Integer pageSize =searchFilterForm.getPageSize();
        boolean isResetPage =searchFilterForm.isResetPage();
//        System.out.println("sortFieldName:"+sortFieldName);System.out.println("isDesc:"+isDesc);System.out.println("page:"+page);
//        System.out.println("pageSize:"+pageSize);System.out.println("isResetPage:"+isResetPage);

        boolean isID =false;
        boolean isName =false;
        boolean isCategory_id =false;
        boolean isCategory_name =false;
        boolean isLastModified =false;
        boolean isOrigin =false;
        boolean isWarrantyTime =false;
        boolean isPrice_sell =false;
//        boolean isPromotion =false;
//        boolean isDiscountRate =false;
//        boolean isDatePromotionStart =false;
//        boolean isDatePromotionEnd =false;
        //        boolean isMaterial =false;
//        boolean isProducer =false;
        //        boolean isQuantity =false;
        boolean isRating =false;

        if(filter !=null) {
            if (filter.size() != 0) {
                for (String s : filter) {
                    if (s.equals("id")) {
                        isID = true;
                    }
                    if (s.equals("name")) {
                        isName = true;
                    }
                    if (s.equals("category_id")) {
                        isCategory_id = true;
                    }
                    if (s.equals("category_name")) {
                        isCategory_name = true;
                    }
                    if (s.equals("lastModified")) {
                        isLastModified = true;
                    }

                    if (s.equals("origin")) {
                        isOrigin = true;
                    }

                    if (s.equals("rating")) {
                        isRating = true;
                    }
//                    if (s.equals("promotion")) {
//                        isPromotion = true;
//                    }
                    if (s.equals("price_sell")) {
                        isPrice_sell = true;
                    }
                    if (s.equals("warrantyTime")) {
                        isWarrantyTime = true;
                    }
                    //                    if (s.equals("material")) {
//                        isMaterial = true;
//                    }
//                    if (s.equals("producer")) {
//                        isProducer = true;
//                    }
                    //                    if (s.equals("datePromotionEnd")) {
//                        isDatePromotionEnd = true;
//                    }
//                    if (s.equals("datePromotionStart")) {
//                        isDatePromotionStart = true;
//                    }
//                    if (s.equals("discountRate")) {
//                        isDiscountRate = true;
//                    }
                    //                    if (s.equals("quantity")) {
//                        isQuantity = true;
//                    }
                }
            }
        }

//        System.out.println("isID:"+isID);System.out.println("isName:"+isName);System.out.println("isCategory_id:"+isCategory_id);
//        System.out.println("isCategory_name:"+isCategory_name);System.out.println("isLastModified:"+isLastModified);System.out.println("isOrigin:"+isOrigin);
//        System.out.println("isRating:"+isRating);System.out.println("isPromotion:"+isPromotion);System.out.println("isPrice_sell:"+isPrice_sell);
//        System.out.println("isWarrantyTime:"+isWarrantyTime);

        List<Item> itemList = new ArrayList<Item>();
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
            if(!isWarrantyTime) {
                if(searchConditionForm.getFieldName().equals("warrantyTime")) {
                    conditionItems++;
                    continue;
                }
            }
            if(!isPrice_sell) {
                if(searchConditionForm.getFieldName().equals("price_sell")) {
                    conditionItems++;
                    continue;
                }
            }
//            if(!isPromotion) {
//                if(searchConditionForm.getFieldName().equals("promotion")) {
//                    conditionItems++;
//                    continue;
//                }
//            }
            if(!isRating) {
                if(searchConditionForm.getFieldName().equals("rating")) {
                    conditionItems++;
                    continue;
                }
            }
            if(!isOrigin) {
                if(searchConditionForm.getFieldName().equals("origin")) {
                    conditionItems++;
                    continue;
                }
            }
            if(!isID) {
                if(searchConditionForm.getFieldName().equals("id")) {
                    conditionItems++;
                    continue;
                }
            }
            if(!isName) {
                if(searchConditionForm.getFieldName().equals("name")) {
                    conditionItems++;
                    continue;
                }
            }
            if(!isCategory_id) {
                if(searchConditionForm.getFieldName().equals("category_id")) {
                    conditionItems++;
                    continue;
                }
            }
            if(!isCategory_name) {
                if(searchConditionForm.getFieldName().equals("category_name")) {
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
           //            if(!isDiscountRate) {
//                if(searchConditionForm.getFieldName().equals("discountRate")) {
//                    conditionItems++;
//                    continue;
//                }
//            }
//
//            if(!isDatePromotionStart) {
//                if(searchConditionForm.getFieldName().equals("datePromotionStart")) {
//                    conditionItems++;
//                    continue;
//                }
//            }
//
//            if(!isDatePromotionEnd) {
//                if(searchConditionForm.getFieldName().equals("datePromotionEnd")) {
//                    conditionItems++;
//                    continue;
//                }
//            }

//            if(!isMaterial) {
//                if(searchConditionForm.getFieldName().equals("material")) {
//                    conditionItems++;
//                    continue;
//                }
//            }
            //            if(!isProducer) {
//                if(searchConditionForm.getFieldName().equals("producer")) {
//                    conditionItems++;
//                    continue;
//                }
//            }
//            if(!isQuantity) {
//                if(searchConditionForm.getFieldName().equals("quantity")) {
//                    conditionItems++;
//                    continue;
//                }
//            }

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

        itemList = itemDAO.searchAndQuery(searchFilter);
        SearchItemData searchItemData = new SearchItemData();
        long countTotal =itemDAO.countOnQuery(searchFilter);

        long pageTotal =0;
        if(countTotal%pageSize !=0) {
            pageTotal = countTotal/pageSize +1;
        }else {
            pageTotal = countTotal/pageSize;
        }
        searchFilterForm.setTotalPage(pageTotal);
        searchFilterForm.setTotalResult(countTotal);
        searchFilterForm.setIsResetCondition(false);

        searchItemData.setSearchFilterForm(searchFilterForm);
        searchItemData.setItemList(itemList);

//        System.out.println("itemList"+itemList.size());

        List<String> searchArray = new ArrayList<String>();
        searchArray.add("String");
        searchArray.add("id");
        searchArray.add("String");
        searchArray.add("name");
        searchArray.add("String");
        searchArray.add("category_id");
        searchArray.add("String");
        searchArray.add("category_name");
        searchArray.add("Date");
        searchArray.add("lastModified");
        searchArray.add("String");
        searchArray.add("origin");
        searchArray.add("Int");
        searchArray.add("warrantyTime");
        searchArray.add("Double");
        searchArray.add("price_sell");
//        searchArray.add("Boolean");
//        searchArray.add("promotion");
        searchArray.add("Double");
        searchArray.add("rating");
        //        searchArray.add("Double");
//        searchArray.add("discountRate");
//        searchArray.add("Date");
//        searchArray.add("datePromotionStart");
//        searchArray.add("Date");
//        searchArray.add("datePromotionEnd");
        //        searchArray.add("String");
//        searchArray.add("material");
//        searchArray.add("String");
//        searchArray.add("producer");
        //        searchArray.add("Int");
//        searchArray.add("quantity");

//        for (SearchConditionForm searchConditionForm:searchItemData.getSearchFilterForm().getConditionList()){
//            System.out.println("searchConditionForm:"+Json.toJson(searchConditionForm));
//        }

        return ok(Admin_item_list.render(getUserSession(), searchItemData, searchArray));
    }
    /////////////////////////////////ITEM LIST///////////////////////////////////////////

    ////////////////////////DEL ITEM///////////////////////////////////////////////////////
    public Result deleteItem(){

        JsonNode json = request().body().asJson();
        String id = json.findPath("id").textValue();

        ItemDelForm itemDelForm=new ItemDelForm();
        itemDelForm.setSuccess(false);

        itemDAO.deleteByKey(id);
        itemDelForm.setSuccess(true);
        itemDelForm.setId(id);

        return ok(Json.toJson(itemDelForm));

    }

    ////////////////////////DEL ITEM///////////////////////////////////////////////////////


}
