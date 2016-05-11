package models.forms;

import org.apache.commons.lang.StringUtils;
import play.data.validation.ValidationError;
import play.mvc.Http;
import utils.ImageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static play.mvc.Controller.request;

/**
 * Created by giangbb on 11/05/2016.
 */
public class ItemForm {
    private String id;
    private String name;
    private String category_id;
    private String description;
    private String description_id;          //su dung de xoa cac data image... da up len
    private String material;
    private String image;
    private String producer;    //nha san xuat
    private String origin; //xuat xu
    private int warrantyTime;       //thoi gian bao hanh - tinh theo thang

    private int quantity;
    private double price_receipt;   //gia nhap hang
    private double price_sell;      //gia ban
    private Date lastModified;

    private boolean promotion=false;    //khuyen mai
    private double discountRate=0;        // tinh theo %
    private String datePromotionStart;        //ngay bat dau khuyen mai
    private String datePromotionEnd;        //ngay ket thuc khuyen mai

    private String[] techkey;
    private String[] techvalue;

    private Http.MultipartFormData.FilePart fileData;
    private String contentType;
    private String fileName;
    private String fileClientPath;
    private String fileServerPath;

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        Http.MultipartFormData data = request().body().asMultipartFormData();

        if (data!=null&&data.getFile("image") != null){
            if(!StringUtils.isEmpty(data.getFile("image").getFilename())){
                fileData = data.getFile("image");
                fileName = fileData.getFilename();
                contentType = ImageUtil.getImageType(fileName);
                File file = (File )fileData.getFile();
                fileClientPath = file.getParent();

                if(!ImageUtil.checkValidImageType(contentType)){
                    errors.add(new ValidationError("image", "wrong format image"));
                }

                System.out.println("fileName:" + fileName);
                System.out.println("contentType:" + contentType);
                System.out.println("fileClientPath:" + fileClientPath);

                System.out.println("getPath:" + file.getPath());
                System.out.println("getAbsolutePath:" + file.getAbsolutePath());
            }
        }


        return errors.isEmpty() ? null : errors;
    }

    public ItemForm() {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public Http.MultipartFormData.FilePart getFileData() {
        return fileData;
    }

    public void setFileData(Http.MultipartFormData.FilePart fileData) {
        this.fileData = fileData;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileClientPath() {
        return fileClientPath;
    }

    public void setFileClientPath(String fileClientPath) {
        this.fileClientPath = fileClientPath;
    }

    public String getFileServerPath() {
        return fileServerPath;
    }

    public void setFileServerPath(String fileServerPath) {
        this.fileServerPath = fileServerPath;
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

    public String getDescription_id() {
        return description_id;
    }

    public void setDescription_id(String description_id) {
        this.description_id = description_id;
    }
}
