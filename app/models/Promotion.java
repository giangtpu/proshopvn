package models;

import configs.ModelData;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import utils.DateUtil;
import utils.ItemHelper;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by giangdaika on 18/05/2016.
 */
@ModelData(collection = "Promotion", mapCacheName = "", mapCacheTTL = 86400)
@Document(collection = "Promotion")
public class Promotion implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private String item_id;
    private String item_name;
    private String category_id;
    private double discountRate=0;        // tinh theo %
//    private double discountPrice=0;        // tinh theo %
    private Date datePromotionStart;        //ngay bat dau khuyen mai
    private Date datePromotionEnd;        //ngay ket thuc khuyen mai

    private Date lastModified;


    public Promotion() {
        this.id= ItemHelper.generateId();
        this.lastModified= DateUtil.now();

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
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

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }
}
