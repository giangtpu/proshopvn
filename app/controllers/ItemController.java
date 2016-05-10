package controllers;

import dao.CategoryDAO;
import dao.ItemDAO;
import play.Logger;
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

}
