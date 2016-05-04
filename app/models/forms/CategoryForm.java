package models.forms;

/**
 * Created by giangdaika on 05/05/2016.
 */
public class CategoryForm {
    private String name;
    private boolean isItemCategory;
    private String fatherCategoryId;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isItemCategory() {
        return isItemCategory;
    }

    public void setItemCategory(boolean itemCategory) {
        isItemCategory = itemCategory;
    }

    public String getFatherCategoryId() {
        return fatherCategoryId;
    }

    public void setFatherCategoryId(String fatherCategoryId) {
        this.fatherCategoryId = fatherCategoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
