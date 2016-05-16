package controllers;

import dao.ItemDAO;
import dao.RecieptIssueDAO;
import models.Item;
import models.JSON.ReceiptForm;
import models.RecieptIssue;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security;

import javax.inject.Inject;

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
//        System.out.println("getItem_id:"+recieptIssueForm.getItem_id());
//        System.out.println("getItem_name:"+recieptIssueForm.getItem_name());
//        System.out.println("getPrice:"+recieptIssueForm.getPrice());
//        System.out.println("getQuantity:"+recieptIssueForm.getQuantity());
//        System.out.println("getType:"+recieptIssueForm.getType());
//        System.out.println("getDatePurchase:"+recieptIssueForm.getDatePurchase());

        RecieptIssue recieptIssue=new RecieptIssue();
        recieptIssueForm.fillToRecieptIssue(recieptIssue);

        Item item=itemDAO.getByKey(recieptIssue.getItem_id());
        if(item==null){
            responeForm.setErrorMessage(getMessages().at("Admin.Item.notfound"));
            return ok(Json.toJson(responeForm));
        }

        item.setQuantity(item.getQuantity()+recieptIssue.getQuantity());

        itemDAO.save(item);
        recieptIssueDAO.save(recieptIssue);
        responeForm.setSuccess(true);
        responeForm.setQuantity(item.getQuantity());
        responeForm.setItem_id(item.getId());
        return ok(Json.toJson(responeForm));
    }

    public Result addIssue() {
        return ok();
    }

}
