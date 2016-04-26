package models;

import configs.ModelData;
import models.Template.OrderTemplate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import utils.DateUtil;
import utils.ItemHelper;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by giangbb on 26/04/2016.
 */
//DON HANG
@ModelData(collection = "Order", mapCacheName = "", mapCacheTTL = 86400)
@Document(collection = "Order")
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    public enum OrderStatus {
        newOne(1),
        inprogress(2),
        done(3),
        cancel(4)
        ;

        private final int code;

        private static final Map<Integer, OrderStatus> statusByCode = new HashMap<Integer, OrderStatus>();

        static {
            for(OrderStatus status: OrderStatus.values()){
                statusByCode.put(status.getCode(),status);
            }
        }

        private OrderStatus(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name();
        }

        public static OrderStatus fromCode(int code){
            return statusByCode.get(code);
        }

        public static String getNameByCode(int code){
            OrderStatus status = statusByCode.get(code);
            if(status!=null){
                return status.getName();
            }
            return "";
        }

    }

    @Id
    private String id;

    private String customer_id;
    private List<OrderTemplate> orderTemplateList;
    private int status;
    private Date date_order;

    public Order() {
        this.id= ItemHelper.generateId();
        this.status=OrderStatus.newOne.getCode();
        this.date_order= DateUtil.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public List<OrderTemplate> getOrderTemplateList() {
        return orderTemplateList;
    }

    public void setOrderTemplateList(List<OrderTemplate> orderTemplateList) {
        this.orderTemplateList = orderTemplateList;
    }

    public Date getDate_order() {
        return date_order;
    }

    public void setDate_order(Date date_order) {
        this.date_order = date_order;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
