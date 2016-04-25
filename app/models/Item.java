package models;


import configs.ModelData;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import play.Configuration;
import utils.DateUtil;
import utils.ItemHelper;
import utils.UserHelper;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by giangdaika on 25/04/2016.
 */
@ModelData(collection = "Item", mapCacheName = "", mapCacheTTL = 86400)
@Document(collection = "Item")
public class Item implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private String name;
    private String category_id;
    private String description;
    private String material;
    private String image;

    private int quantity;
    private double price;
    private Date lastModified;

    public Item() {
        this.id= ItemHelper.generateId();
        this.lastModified= DateUtil.now();
    }

    public Item(String id, String name, String category_id, String description, String material, String image, int quantity, double price) {
        this.id = id;
        this.name = name;
        this.category_id = category_id;
        this.description = description;
        this.material = material;
        this.image = image;
        this.quantity = quantity;
        this.price = price;
        this.lastModified= DateUtil.now();
    }
    public String getImageLinkPath(){
        return ItemHelper.itemImageLinkPath + "/" + image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
}
