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
import java.util.Map;

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
    private String category_name;

    private String description;
    private String description_id;          //su dung de xoa cac data image... da up len
    private String[]  description_img;

    private String material;
    private List<String> images=new ArrayList<String>();
    private String producer;    //nha san xuat
    private String origin; //xuat xu
    private int warrantyTime;       //thoi gian bao hanh - tinh theo thang

    private int quantity;
    private double price_receipt;   //gia nhap hang
    private double price_sell;      //gia ban
    private Date lastModified;

    private boolean promotion=false;    //khuyen mai
    private double discountRate=0;        // tinh theo %
    private Date datePromotionStart;        //ngay bat dau khuyen mai
    private Date datePromotionEnd;        //ngay ket thuc khuyen mai

    private String[] techkey;           //thong so ky thuat
    private String[] techvalue;         //thong so ky thuat



    private List<String> relatedItems=new ArrayList<String>(); //cac san pham co lien quan


    private double rating=5;     //diem 1-5
    private int numbervote=1;   //so luong danh gia
    private List<String> comments=new ArrayList<String>();      //phan hoi

    public Item() {
//        this.id= ItemHelper.generateId();
        this.lastModified= DateUtil.now();
        images.add(0,"");
        images.add(1,"");
        images.add(2,"");
    }

    public Item(String id, String name, String category_id, String description, String material, String producer, String origin, int quantity, double price_sell) {
        this.id = id;
        this.name = name;
        this.category_id = category_id;
        this.description = description;
        this.material = material;
        this.producer=producer;
        this.origin=origin;
        this.quantity = quantity;
        this.price_sell = price_sell;
        this.lastModified= DateUtil.now();
    }
    public String getImageLinkPath(int position){
        return ItemHelper.itemImageLinkPath + "/" + getImages().get(position);
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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
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

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public int getWarrantyTime() {
        return warrantyTime;
    }

    public void setWarrantyTime(int warrantyTime) {
        this.warrantyTime = warrantyTime;
    }

    public List<String> getRelatedItems() {
        return relatedItems;
    }

    public void setRelatedItems(List<String> relatedItems) {
        this.relatedItems = relatedItems;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getNumbervote() {
        return numbervote;
    }

    public void setNumbervote(int numbervote) {
        this.numbervote = numbervote;
    }

    public String getDescription_id() {
        return description_id;
    }

    public void setDescription_id(String description_id) {
        this.description_id = description_id;
    }

    public String[] getTechkey() {
        return techkey;
    }

    public void setTechkey(String[] techkey) {
        this.techkey = techkey;
    }

    public String[] getTechvalue() {
        return techvalue;
    }

    public void setTechvalue(String[] techvalue) {
        this.techvalue = techvalue;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String[] getDescription_img() {
        return description_img;
    }

    public void setDescription_img(String[] description_img) {
        this.description_img = description_img;
    }
}
