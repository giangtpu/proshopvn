package models;

import configs.ModelData;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import utils.DateUtil;
import utils.ItemHelper;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by giangdaika on 25/04/2016.
 */
@ModelData(collection = "Category", mapCacheName = "", mapCacheTTL = 86400)
@Document(collection = "Category")
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    private boolean isItemCategory=true;

    private String fatherCategoryId;

    private String description;

    private Date lastModified;

    public Category() {
        this.id= ItemHelper.generateId();
        this.lastModified= DateUtil.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
}
