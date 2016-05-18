package models.JSON;

import models.Promotion;

import java.util.Date;
import org.springframework.util.StringUtils;
import utils.DateUtil;
import utils.ParseUtil;

/**
 * Created by giangdaika on 18/05/2016.
 */
public class PromotionForm {
    private String item_id;
    private String item_name;
    private String category_id;
    private String discountRate;        // tinh theo %
    private String discountPrice;        // tinh theo %
    private String datePromotionStart;        //ngay bat dau khuyen mai
    private String datePromotionEnd;        //ngay ket thuc khuyen mai

    public PromotionForm() {
    }

    public boolean fillToPromotion(Promotion promotion){
        if (!StringUtils.isEmpty(item_id)){
            promotion.setItem_id(item_id);
        }
        if (!StringUtils.isEmpty(item_name)){
            promotion.setItem_name(item_name);
        }
        if (!StringUtils.isEmpty(category_id)){
            promotion.setCategory_id(category_id);
        }
        if (!StringUtils.isEmpty(discountRate))
        {
            promotion.setDiscountRate(ParseUtil.parseDouble(discountRate));
        }
        Date datestart= (DateUtil.convertStringtoDate(
                datePromotionStart,
                DateUtil.TIME_ITEM));

        Date dateend = (DateUtil.convertStringtoDate(
                datePromotionEnd,
                DateUtil.TIME_ITEM));

        if (datestart.after(dateend))
        {
            return false;
        }

        promotion.setDatePromotionStart(datestart);
        promotion.setDatePromotionEnd(dateend);
        return true;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(String discountRate) {
        this.discountRate = discountRate;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getDatePromotionStart() {
        return datePromotionStart;
    }

    public void setDatePromotionStart(String datePromotionStart) {
        this.datePromotionStart = datePromotionStart;
    }

    public String getDatePromotionEnd() {
        return datePromotionEnd;
    }

    public void setDatePromotionEnd(String datePromotionEnd) {
        this.datePromotionEnd = datePromotionEnd;
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
