package models.forms;

import models.Item;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.util.StringUtils;
import play.data.validation.ValidationError;
import play.mvc.Http;
import utils.DateUtil;
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
    private String[] description_img;

    private String material;
    private String image;
    private String image2;
    private String image3;
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

    private String[] relatedItems; //cac san pham co lien quan

    private Http.MultipartFormData.FilePart fileData;
    private String contentType;
    private String fileName;
    private String fileClientPath;
    private String fileServerPath;

    private Http.MultipartFormData.FilePart fileData2;
    private String contentType2;
    private String fileName2;
    private String fileClientPath2;
    private String fileServerPath2;

    private Http.MultipartFormData.FilePart fileData3;
    private String contentType3;
    private String fileName3;
    private String fileClientPath3;
    private String fileServerPath3;

    private Http.MultipartFormData.FilePart fileData4;
    private String contentType4;
    private String fileName4;
    private String fileClientPath4;
    private String fileServerPath4;

    private Http.MultipartFormData.FilePart fileData5;
    private String contentType5;
    private String fileName5;
    private String fileClientPath5;
    private String fileServerPath5;

    private Http.MultipartFormData.FilePart fileData6;
    private String contentType6;
    private String fileName6;
    private String fileClientPath6;
    private String fileServerPath6;

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        Http.MultipartFormData data = request().body().asMultipartFormData();
        if (data!=null){

            if (data.getFile("image") != null){
                if(!StringUtils.isEmpty(data.getFile("image").getFilename())){
                    fileData = data.getFile("image");
                    fileName = fileData.getFilename();
                    contentType = ImageUtil.getImageType(fileName);
                    File file = (File )fileData.getFile();
                    fileClientPath = file.getParent();

                    if(!ImageUtil.checkValidImageType(contentType)){
                        errors.add(new ValidationError("image", "wrong format image"));
                    }
                }
            }

            if (data.getFile("image2") != null){
                if(!StringUtils.isEmpty(data.getFile("image2").getFilename())){
                    fileData2 = data.getFile("image2");
                    fileName2 = fileData2.getFilename();
                    contentType2 = ImageUtil.getImageType(fileName2);
                    File file = (File )fileData2.getFile();
                    fileClientPath2 = file.getParent();

                    if(!ImageUtil.checkValidImageType(contentType2)){
                        errors.add(new ValidationError("image2", "wrong format image"));
                    }
                }
            }

            if (data.getFile("image3") != null){
                if(!StringUtils.isEmpty(data.getFile("image3").getFilename())){
                    fileData3 = data.getFile("image3");
                    fileName3 = fileData3.getFilename();
                    contentType3 = ImageUtil.getImageType(fileName3);
                    File file = (File )fileData3.getFile();
                    fileClientPath3 = file.getParent();

                    if(!ImageUtil.checkValidImageType(contentType3)){
                        errors.add(new ValidationError("image3", "wrong format image"));
                    }
//                System.out.println("fileName:" + fileName);
//                System.out.println("contentType:" + contentType);
//                System.out.println("fileClientPath:" + fileClientPath);
//
//                System.out.println("getPath:" + file.getPath());
//                System.out.println("getAbsolutePath:" + file.getAbsolutePath());
                }
            }

            if (data.getFile("image4") != null){
                if(!StringUtils.isEmpty(data.getFile("image4").getFilename())){
                    fileData4 = data.getFile("image4");
                    fileName4 = fileData4.getFilename();
                    contentType4 = ImageUtil.getImageType(fileName4);
                    File file = (File )fileData4.getFile();
                    fileClientPath4 = file.getParent();

                    if(!ImageUtil.checkValidImageType(contentType4)){
                        errors.add(new ValidationError("image4", "wrong format image"));
                    }
                }
            }

            if (data.getFile("image5") != null){
                if(!StringUtils.isEmpty(data.getFile("image5").getFilename())){
                    fileData5 = data.getFile("image5");
                    fileName5 = fileData5.getFilename();
                    contentType5 = ImageUtil.getImageType(fileName5);
                    File file = (File )fileData5.getFile();
                    fileClientPath5 = file.getParent();

                    if(!ImageUtil.checkValidImageType(contentType5)){
                        errors.add(new ValidationError("image5", "wrong format image"));
                    }
                }
            }

            if (data.getFile("image6") != null){
                if(!StringUtils.isEmpty(data.getFile("image6").getFilename())){
                    fileData6 = data.getFile("image6");
                    fileName6 = fileData6.getFilename();
                    contentType6 = ImageUtil.getImageType(fileName6);
                    File file = (File )fileData6.getFile();
                    fileClientPath6 = file.getParent();

                    if(!ImageUtil.checkValidImageType(contentType6)){
                        errors.add(new ValidationError("image6", "wrong format image"));
                    }
                }
            }
        }


        return errors.isEmpty() ? null : errors;
    }


    public boolean
    fillToItem(Item item){

        item.setCategory_id(category_id);

        if (!StringUtils.isEmpty(description))
        {
            String htmlescape= StringEscapeUtils.escapeHtml4(description);
            item.setDescription(htmlescape);
        }

        item.setDescription_id(description_id);

        if (!StringUtils.isEmpty(material))
        {
            item.setMaterial(material);
        }

        if (!StringUtils.isEmpty(producer))
        {
            item.setProducer(producer);
        }

        if (!StringUtils.isEmpty(origin))
        {
            item.setOrigin(origin);
        }

        if (!StringUtils.isEmpty(warrantyTime))
        {
            item.setWarrantyTime(warrantyTime);
        }

        if (!StringUtils.isEmpty(quantity))
        {
            item.setQuantity(quantity);
        }

        if (!StringUtils.isEmpty(price_receipt))
        {
            item.setPrice_receipt(price_receipt);
        }

        if (!StringUtils.isEmpty(price_sell))
        {
            item.setPrice_sell(price_sell);
        }

        if (promotion){

            item.setPromotion(promotion);
            if (!StringUtils.isEmpty(discountRate))
            {
                item.setDiscountRate(discountRate);
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

            item.setDatePromotionStart(datestart);
            item.setDatePromotionEnd(dateend);


        }

        if (techkey!=null&&techvalue!=null){
            item.setTechkey(techkey);
            item.setTechvalue(techvalue);
        }

//        if (relatedItems!=null){
//            item.setRelatedItems(relatedItems);
//            for (int i=0;i<relatedItems.length;i++)
//            {
//                System.out.println(relatedItems[i]);
//            }
//
//        }
//        else{
//            System.out.println("not found relateditems");
//        }




//        if (description_img!=null){
//            item.setDescription_img(description_img);
//        }


        return true;



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

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public Http.MultipartFormData.FilePart getFileData2() {
        return fileData2;
    }

    public void setFileData2(Http.MultipartFormData.FilePart fileData2) {
        this.fileData2 = fileData2;
    }

    public String getContentType2() {
        return contentType2;
    }

    public void setContentType2(String contentType2) {
        this.contentType2 = contentType2;
    }

    public String getFileName2() {
        return fileName2;
    }

    public void setFileName2(String fileName2) {
        this.fileName2 = fileName2;
    }

    public String getFileClientPath2() {
        return fileClientPath2;
    }

    public void setFileClientPath2(String fileClientPath2) {
        this.fileClientPath2 = fileClientPath2;
    }

    public String getFileServerPath2() {
        return fileServerPath2;
    }

    public void setFileServerPath2(String fileServerPath2) {
        this.fileServerPath2 = fileServerPath2;
    }

    public Http.MultipartFormData.FilePart getFileData3() {
        return fileData3;
    }

    public void setFileData3(Http.MultipartFormData.FilePart fileData3) {
        this.fileData3 = fileData3;
    }

    public String getContentType3() {
        return contentType3;
    }

    public void setContentType3(String contentType3) {
        this.contentType3 = contentType3;
    }

    public String getFileName3() {
        return fileName3;
    }

    public void setFileName3(String fileName3) {
        this.fileName3 = fileName3;
    }

    public String getFileClientPath3() {
        return fileClientPath3;
    }

    public void setFileClientPath3(String fileClientPath3) {
        this.fileClientPath3 = fileClientPath3;
    }

    public String getFileServerPath3() {
        return fileServerPath3;
    }

    public void setFileServerPath3(String fileServerPath3) {
        this.fileServerPath3 = fileServerPath3;
    }

    public String[] getDescription_img() {
        return description_img;
    }

    public void setDescription_img(String[] description_img) {
        this.description_img = description_img;
    }

    public Http.MultipartFormData.FilePart getFileData4() {
        return fileData4;
    }

    public void setFileData4(Http.MultipartFormData.FilePart fileData4) {
        this.fileData4 = fileData4;
    }

    public String getContentType4() {
        return contentType4;
    }

    public void setContentType4(String contentType4) {
        this.contentType4 = contentType4;
    }

    public String getFileName4() {
        return fileName4;
    }

    public void setFileName4(String fileName4) {
        this.fileName4 = fileName4;
    }

    public String getFileClientPath4() {
        return fileClientPath4;
    }

    public void setFileClientPath4(String fileClientPath4) {
        this.fileClientPath4 = fileClientPath4;
    }

    public String getFileServerPath4() {
        return fileServerPath4;
    }

    public void setFileServerPath4(String fileServerPath4) {
        this.fileServerPath4 = fileServerPath4;
    }

    public Http.MultipartFormData.FilePart getFileData5() {
        return fileData5;
    }

    public void setFileData5(Http.MultipartFormData.FilePart fileData5) {
        this.fileData5 = fileData5;
    }

    public String getContentType5() {
        return contentType5;
    }

    public void setContentType5(String contentType5) {
        this.contentType5 = contentType5;
    }

    public String getFileName5() {
        return fileName5;
    }

    public void setFileName5(String fileName5) {
        this.fileName5 = fileName5;
    }

    public String getFileClientPath5() {
        return fileClientPath5;
    }

    public void setFileClientPath5(String fileClientPath5) {
        this.fileClientPath5 = fileClientPath5;
    }

    public String getFileServerPath5() {
        return fileServerPath5;
    }

    public void setFileServerPath5(String fileServerPath5) {
        this.fileServerPath5 = fileServerPath5;
    }

    public Http.MultipartFormData.FilePart getFileData6() {
        return fileData6;
    }

    public void setFileData6(Http.MultipartFormData.FilePart fileData6) {
        this.fileData6 = fileData6;
    }

    public String getContentType6() {
        return contentType6;
    }

    public void setContentType6(String contentType6) {
        this.contentType6 = contentType6;
    }

    public String getFileName6() {
        return fileName6;
    }

    public void setFileName6(String fileName6) {
        this.fileName6 = fileName6;
    }

    public String getFileClientPath6() {
        return fileClientPath6;
    }

    public void setFileClientPath6(String fileClientPath6) {
        this.fileClientPath6 = fileClientPath6;
    }

    public String getFileServerPath6() {
        return fileServerPath6;
    }

    public void setFileServerPath6(String fileServerPath6) {
        this.fileServerPath6 = fileServerPath6;
    }

    public String[] getRelatedItems() {
        return relatedItems;
    }

    public void setRelatedItems(String[] relatedItems) {
        this.relatedItems = relatedItems;
    }
}
