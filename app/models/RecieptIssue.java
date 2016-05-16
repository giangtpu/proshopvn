package models;

import configs.ModelData;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import utils.DateUtil;
import utils.ItemHelper;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by giangbb on 26/04/2016.
 */

//XUAT KHO  DOANHTHU(BAO GOM CHI PHI BAN HANG VA CAC KHOAN THU KHAC)- NHAP KHO CHIPHI(BAO GOM NHAP HANG HOA VA CAC CHI PHI KHAC)
@ModelData(collection = "RecieptIssue", mapCacheName = "", mapCacheTTL = 86400)
@Document(collection = "RecieptIssue")
public class RecieptIssue implements Serializable {
    private static final long serialVersionUID = 1L;


    public enum Types {
        reciept(1),             //nhap kho-chi phi
        issue(2)            //xuat kho-doanh thu
        ;

        private final int code;

        private static final Map<Integer, Types> typeByCode = new HashMap<Integer, Types>();

        static {
            for(Types type: Types.values()){
                typeByCode.put(type.getCode(),type);
            }
        }

        private Types(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name();
        }

        public static Types fromCode(int code){
            return typeByCode.get(code);
        }

        public static String getNameByCode(int code){
            Types type = typeByCode.get(code);
            if(type!=null){
                return type.getName();
            }
            return "";
        }

    }

    @Id
    private String id;

    private int type;       //issue - reciept
    private String item_id;
    private String item_name;       //neu ko tim thay item - day la chi phi phat sinh khac
    private String description;
    private int quantity;
    private double price; //gia xuat - nhap kho
    private Date datePurchase;    //ngay xuat - nhap kho
    private Date lastmodifide;





    public RecieptIssue() {
        this.id= ItemHelper.generateId();
        this.lastmodifide=DateUtil.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
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

    public Date getLastmodifide() {
        return lastmodifide;
    }

    public void setLastmodifide(Date lastmodifide) {
        this.lastmodifide = lastmodifide;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getDatePurchase() {
        return datePurchase;
    }

    public void setDatePurchase(Date datePurchase) {
        this.datePurchase = datePurchase;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }
}
