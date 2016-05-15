package controllers;

import dao.CategoryDAO;
import models.Category;
import models.Menu;
import models.forms.CategoryForm;
import org.springframework.util.StringUtils;
import play.Logger;
import play.data.Form;
import play.mvc.Result;
import play.mvc.Security;
import utils.DateUtil;
import utils.ItemHelper;
import views.html.Admin_categories;
import views.html.Admin_category_add;
import views.html.Admin_category_info;

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



    public Result categoryList(){
        return ok(Admin_categories.render(getUserSession(),getMenu()));
    }

    public Result categoryInfo(String id){

        Category category=categoryDAO.getByKey(id);
        if (category==null){
            flash("failed",getMessages().at("Admin.Category.notfound"));
            return redirect(routes.CategoryController.categoryList());
        }

        return ok(Admin_category_info.render(getUserSession(),getMenu(),category));
    }

    public Result deleteCategory(){


        Form<CategoryForm> CategoryForm = formFactory.form(CategoryForm.class);
        if(CategoryForm.hasErrors())
        {
            flash("failed",getMessages().at("form.error"));
            return redirect(routes.CategoryController.categoryList());
        }
        if(!isAdmin())
        {
            flash("failed",getMessages().at("Admin.donthavepermission"));
            return redirect(routes.CategoryController.categoryList());
        }

        CategoryForm formcategory=CategoryForm.bindFromRequest().get();


        Menu menu=getMenu();
        for (Category categorymenu:menu.getCategoryList()){
            if (categorymenu.getFatherCategoryId().equals(formcategory.getId())){
                flash("failed",getMessages().at("Admin.Category.childconsist"));
                return redirect(routes.CategoryController.categoryList());
            }
        }

//        System.out.println("formcategory.getId():"+formcategory.getId());

        categoryDAO.deleteByKey(formcategory.getId());
        updateMenu();


        flash("success",getMessages().at("Admin.deletesuccess"));
        return redirect(routes.CategoryController.categoryList());
    }



    public Result addCategoryView() {

        return ok(Admin_category_add.render(getUserSession(),getMenu()));
    }

    public Result updateCategory() {
        Form<CategoryForm> CategoryForm = formFactory.form(CategoryForm.class);
        if(CategoryForm.hasErrors())
        {
            flash("failed",getMessages().at("form.error"));
            return redirect(routes.CategoryController.categoryList());
        }
        CategoryForm formcategory=CategoryForm.bindFromRequest().get();
        Category category=categoryDAO.getByKey(formcategory.getId());
        if (category==null){
            flash("failed",getMessages().at("Admin.Category.notfound"));
            return redirect(routes.CategoryController.categoryInfo(formcategory.getId()));
        }

        if (!category.getName().equals(formcategory.getName())){
//            System.out.println("khac ten");
            category.setName(formcategory.getName());

            String idGenerate=ItemHelper.generateCategoryIDbyName(formcategory.getName());
            if(categoryDAO.getByKey(idGenerate)!=null){
                flash("failed",getMessages().at("Admin.Category.existed"));
                return redirect(routes.CategoryController.categoryInfo(formcategory.getId()));
            }

            List<Category> categories=categoryDAO.getAll();
            for (Category cate:categories){
                if (cate.getFatherCategoryId().equals(category.getId())){
                    cate.setFatherCategoryId(idGenerate);
                    categoryDAO.save(cate);
                }
            }

            categoryDAO.deleteByKey(category.getId());
            category.setId(idGenerate);

        }


        if(formcategory.getIsItemCategory()==1){
            category.setItemCategory(true);
        }else
        {
            category.setItemCategory(false);
        }




//        System.out.println("formcategory.isItemCategory():"+formcategory.getIsItemCategory());

        if(!formcategory.getFatherCategoryId().equals("0"))
        {
            category.setFatherCategoryId(formcategory.getFatherCategoryId());
            Category categoryFather=categoryDAO.getByKey(formcategory.getFatherCategoryId());
            if(categoryFather!=null)
            {
                category.setLevel(categoryFather.getLevel()+1);
            }

        }else{
            category.setFatherCategoryId("");
            category.setLevel(0);
        }

        category.setDescription(formcategory.getDescription());
        writeCatagoryImageTodisk(formcategory,category);
        category.setLastModified(DateUtil.now());
        categoryDAO.save(category);
        updateMenu();


        flash("success",getMessages().at("Admin.Updatesuccess"));
        return redirect(routes.CategoryController.categoryInfo(category.getId()));
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
        String idGenerate=ItemHelper.generateCategoryIDbyName(formcategory.getName());
        if(categoryDAO.getByKey(idGenerate)!=null){
            flash("failed",getMessages().at("Admin.Category.existed"));
            return redirect(routes.CategoryController.addCategoryView());
        }
        category.setId(idGenerate);
//        System.out.println(ItemHelper.generateCategoryIDbyName(formcategory.getName()));
        if(formcategory.getIsItemCategory()==1){
            category.setItemCategory(true);
        }else
        {
            category.setItemCategory(false);
        }
//        category.setItemCategory(formcategory.isItemCategory());
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

        writeCatagoryImageTodisk(formcategory,category);
        categoryDAO.save(category);
        updateMenu();
        flash("success",getMessages().at("Admin.addsuccess"));
        return redirect(routes.CategoryController.addCategoryView());
    }
}
