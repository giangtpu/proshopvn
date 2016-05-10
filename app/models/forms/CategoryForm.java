package models.forms;

import org.apache.commons.lang.StringUtils;
import play.data.validation.ValidationError;
import play.mvc.Http;
import utils.ImageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static play.mvc.Controller.request;

/**
 * Created by giangdaika on 05/05/2016.
 */
public class CategoryForm {
    private String id;
    private String name;
    private int isItemCategory;
    private String fatherCategoryId;
    private String description;

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

//                System.out.println("fileName:" + fileName);
//                System.out.println("contentType:" + contentType);
//                System.out.println("fileClientPath:" + fileClientPath);
//
//                System.out.println("getPath:" + file.getPath());
//                System.out.println("getAbsolutePath:" + file.getAbsolutePath());
            }
        }


        return errors.isEmpty() ? null : errors;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIsItemCategory() {
        return isItemCategory;
    }

    public void setIsItemCategory(int isItemCategory) {
        this.isItemCategory = isItemCategory;
    }

    public String getFatherCategoryId() {
        return fatherCategoryId;
    }

    public void setFatherCategoryId(String fatherCategoryId) {
        this.fatherCategoryId = fatherCategoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
