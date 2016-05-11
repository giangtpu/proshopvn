package controllers;

import dao.CategoryDAO;
import dao.ItemDAO;
import models.forms.ItemForm;
import play.Logger;
import play.data.Form;
import play.mvc.Result;
import play.mvc.Security;
import views.html.Admin_item_add;

import javax.inject.Inject;

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

        return ok(Admin_item_add.render(getUserSession(),getMenu()));
    }
    public Result addItem() {
        Form<ItemForm> itemFormForm = formFactory.form(ItemForm.class);
        if(itemFormForm.hasErrors())
        {
            flash("failed",getMessages().at("form.error"));
            return redirect(routes.ItemController.addItemView());
        }
        ItemForm itemForm=itemFormForm.bindFromRequest().get();

        if(itemForm.isPromotion()){
            String startdatepromotion=itemForm.getDatePromotionStart();
            String enddatepromotion=itemForm.getDatePromotionEnd();

            System.out.println("startdatepromotion:"+startdatepromotion);
            System.out.println("enddatepromotion:"+enddatepromotion);
        }



        String[] techkeys=itemForm.getTechkey();
        String[] techvalues=itemForm.getTechvalue();

        if (techkeys!=null&&techvalues!=null){
            for (int i=0;i<techkeys.length;i++)
            {
                System.out.println("key:"+techkeys[i]+"   -   value:"+techvalues[i]);
            }
        }




        return redirect(routes.ItemController.addItemView());
    }

}
