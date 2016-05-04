package models.Template;

import models.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giangbb on 04/05/2016.
 */
public class CategoryTemplate {
    private Category category;
    private List<Category> childCategories;

    public CategoryTemplate(Category category) {
        this.category = category;
        this.childCategories=new ArrayList<Category>();
    }



    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Category> getChildCategories() {
        return childCategories;
    }

    public void setChildCategories(List<Category> childCategories) {
        this.childCategories = childCategories;
    }
}
