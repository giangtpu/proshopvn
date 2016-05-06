package models;

import configs.ModelData;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

/**
 * Created by giangdaika on 06/05/2016.
 */
@ModelData(collection = "Menu", mapCacheName = "", mapCacheTTL = 86400)
@Document(collection = "Menu")
public class Menu implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    private List<Category> categoryList;

    public Menu() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }
}
