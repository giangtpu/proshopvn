package controllers;

import dao.CategoryDAO;
import models.Category;
import models.forms.CategoryForm;
import org.springframework.util.StringUtils;
import play.Logger;
import play.data.Form;
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

//    private static List<Category> menu;

    public void getCategoryChild(Category category,List<Category> categories, List<Category> menu){

        for (Category subcategory:categories){
            if (subcategory.getFatherCategoryId().equals(category.getId())){
                menu.add(subcategory);
                getCategoryChild(subcategory,categories,menu);
            }
        }

    }

    public List<Category> getListCategoryMenu(){

        List<Category> categories=categoryDAO.getAll();
        List<Category> menu=new ArrayList<Category>();

        for (Category category:categories){
           if (StringUtils.isEmpty(category.getFatherCategoryId())){
               menu.add(category);
               getCategoryChild(category,categories,menu);
           }
        }


//        for (Category category:menu){
//            System.out.println("name:"+category.getName());
//        }

        return menu;
    }

    public Result addCategoryView() {

        return ok(Admin_add_category.render(getUserSession(),getListCategoryMenu()));
    }

    public Result addCategory() {
        Form<CategoryForm> CategoryForm = formFactory.form(CategoryForm.class);
        if(CategoryForm.hasErrors())
        {
            flash("failed",getMessages().at("form.error"));
            return redirect(routes.CategoryController.addCategoryView());
        }
        CategoryForm formcategory=CategoryForm.bindFromRequest().get();

//        System.out.println("name:"+formcategory.getName());
//        System.out.println("isItemCategory:"+formcategory.isItemCategory());
//        System.out.println("fatherCategoryId:"+formcategory.getFatherCategoryId());
//        System.out.println("description:"+formcategory.getDescription());

        Category category=new Category();
        category.setName(formcategory.getName());
        if(!formcategory.getFatherCategoryId().equals("0"))
        {
            category.setFatherCategoryId(formcategory.getFatherCategoryId());
            Category categoryFather=categoryDAO.getByKey(formcategory.getFatherCategoryId());
            if(categoryFather!=null)
            {
                category.setLevel(categoryFather.getLevel()+1);
            }

        }
        category.setDescription(formcategory.getDescription());

        categoryDAO.save(category);

        return redirect(routes.CategoryController.addCategoryView());
    }
}
