package controllers;

import dao.CategoryDAO;
import models.Category;
import models.Template.CategoryTemplate;
import play.Logger;
import play.mvc.Result;
import play.mvc.Security;
import views.html.Admin_add_category;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by giangbb on 04/05/2016.
 */
@Security.Authenticated(Secured.class)
public class CategoryController extends AbstractController {
    Logger.ALogger logger = Logger.of(CategoryController.class);

    @Inject
    public CategoryDAO categoryDAO;

    public CategoryTemplate getCategoryTemplate(Category category,List<Category> categories){
        CategoryTemplate categoryTemplate=new CategoryTemplate(category);
        List<Category> childCategories=new ArrayList<Category>();
        for (Category ca:categories){
            if (ca.getFatherCategoryId().equals(category.getId())){
                childCategories.add(ca);
            }
        }
        categoryTemplate.setChildCategories(childCategories);

        return categoryTemplate;
    }

    public void getListCategoryTemplate(){
        List<CategoryTemplate> categoryTemplateList=new ArrayList<CategoryTemplate>();

        List<Category> categories=categoryDAO.getAll();

        for (Category category:categories){
            CategoryTemplate categoryTemplate=getCategoryTemplate(category,categories);
            categoryTemplateList.add(categoryTemplate);
        }
    }

    public Result addCategoryView() {
        return ok(Admin_add_category.render(getUserSession()));
    }

    public Result addCategory() {
        return ok();
    }
}
