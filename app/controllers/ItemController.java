package controllers;

import dao.CategoryDAO;
import dao.ItemDAO;
import models.Category;
import models.Item;
import models.JSON.ItemImageUpload;
import models.forms.ItemForm;
import models.forms.ItemImageUploadForm;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.util.StringUtils;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import utils.ImageUtil;
import utils.ItemHelper;
import utils.UserHelper;
import views.html.Admin_item_add;
import views.html.Admin_item_info;


import javax.inject.Inject;
import java.io.File;
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
        item.setCategory_name(category.getName());

        if (itemForm.getDescription_img()!=null){
            item.setDescription_img(itemForm.getDescription_img());
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

        Http.MultipartFormData.FilePart fileData = itemForm.getFileData();
        String fileName = itemForm.getFileName();
        String contentType = itemForm.getContentType();
        File file = (File) fileData.getFile();
        itemForm.setFileClientPath(file.getPath());
        String imageName = itemForm.getDescription_id() + "-" + fileName;
        try {
            // write image file to disk
            ImageUtil.writeAvatarToDisk(imageName, ItemHelper.itemImageFolderPath, file);
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
        item.setCategory_name(category.getName());

        if (itemForm.getDescription_img()!=null){
            item.setDescription_img(itemForm.getDescription_img());
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
        itemDAO.save(item);

        flash("success", getMessages().at("Admin.addsuccess"));
        return redirect(routes.ItemController.infoitem(item.getId()));
    }
}
