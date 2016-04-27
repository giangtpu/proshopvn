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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private String producer;    //nha san xuat
    private String origin; //xuat xu

    private int quantity;
    private double price_receipt;   //gia nhap hang
    private double price_sell;      //gia ban
    private Date lastModified;

    private boolean promotion=false;    //khuyen mai
    private double discountRate=0;        // tinh theo %
    private Date datePromotionStart;        //ngay bat dau khuyen mai
    private Date datePromotionEnd;        //ngay ket thuc khuyen mai

    private List<String> relatedItems=new ArrayList<String>(); //cac san pham co lien quan


    private int rating=5;     //diem 1-5
    private List<String> comments=new ArrayList<String>();      //phan hoi

    public Item() {
        this.id= ItemHelper.generateId();
        this.lastModified= DateUtil.now();
    }

    public Item(String id, String name, String category_id, String description, String material, String image,String producer, String origin, int quantity, double price_sell) {
        this.id = id;
        this.name = name;
        this.category_id = category_id;
        this.description = description;
        this.material = material;
        this.image = image;
        this.producer=producer;
        this.origin=origin;
        this.quantity = quantity;
        this.price_sell = price_sell;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice_receipt() {
        return price_receipt;
    }

    public void setPrice_receipt(double price_receipt) {
        this.price_receipt = price_receipt;
    }

    public double getPrice_sell() {
        return price_sell;
    }

    public void setPrice_sell(double price_sell) {
        this.price_sell = price_sell;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public boolean isPromotion() {
        return promotion;
    }

    public void setPromotion(boolean promotion) {
        this.promotion = promotion;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public double getPromotionPrice() {
        return (price_sell-(price_sell*(discountRate/100)));
    }

    public Date getDatePromotionStart() {
        return datePromotionStart;
    }

    public void setDatePromotionStart(Date datePromotionStart) {
        this.datePromotionStart = datePromotionStart;
    }

    public Date getDatePromotionEnd() {
        return datePromotionEnd;
    }

    public void setDatePromotionEnd(Date datePromotionEnd) {
        this.datePromotionEnd = datePromotionEnd;
    }
}
