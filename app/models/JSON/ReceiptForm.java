package models.JSON;

import models.RecieptIssue;
import org.springframework.util.StringUtils;
import utils.DateUtil;
import utils.StringUtil;

import java.util.Date;

/**
 * Created by giangdaika on 16/05/2016.
 */
public class ReceiptForm extends  ResultForm{


    private String id;
    private int type;       //issue - reciept
    private String item_id;
    private String item_name;       //neu ko tim thay item - day la chi phi phat sinh khac
    private String description;
    private int quantity;
    private double price; //gia xuat - nhap kho
    private String datePurchase;    //ngay xuat - nhap kho


    public ReceiptForm() {
    }

    public void fillToRecieptIssue(RecieptIssue recieptIssue){

        if(!StringUtils.isEmpty(getItem_id())){
            recieptIssue.setItem_id(getItem_id());
        }
        if(!StringUtils.isEmpty(getItem_name())){
            recieptIssue.setItem_name(getItem_name());
        }
        if(!StringUtils.isEmpty(getPrice())){
            recieptIssue.setPrice(getPrice());
        }
        if(!StringUtils.isEmpty(getQuantity())){
            recieptIssue.setQuantity(getQuantity());
        }
        if(!StringUtils.isEmpty(getType())){
            recieptIssue.setType(getType());
        }
        if(!StringUtils.isEmpty(getDatePurchase())){
            Date datePurc= (DateUtil.convertStringtoDate(
                    getDatePurchase(),
                    DateUtil.TIME_ITEM));
            recieptIssue.setDatePurchase(datePurc);
        }

        if(!StringUtils.isEmpty(getDescription()))
        {
            recieptIssue.setDescription(getDescription());
        }

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getDatePurchase() {
        return datePurchase;
    }

    public void setDatePurchase(String datePurchase) {
        this.datePurchase = datePurchase;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
