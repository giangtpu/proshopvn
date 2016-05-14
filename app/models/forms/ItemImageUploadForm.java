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
 * Created by giangbb on 11/05/2016.
 */
public class ItemImageUploadForm {

    private String description_id;
    private String fileNameToDel;


    private Http.MultipartFormData.FilePart<File> fileData;
    private File file;
    private String contentType;
    private String fileName;
    private String fileClientPath;
    private String fileServerPath;


    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();

        play.mvc.Http.MultipartFormData<File> data = request().body().asMultipartFormData();
//        play.mvc.Http.MultipartFormData.FilePart<File> picture = body.getFile("picture");


//        Http.MultipartFormData data = request().body().asMultipartFormData();

        if (data!=null&&data.getFile("image") != null){
            if(!StringUtils.isEmpty(data.getFile("image").getFilename())){
                fileData = data.getFile("image");
                fileName = fileData.getFilename();
                contentType = ImageUtil.getImageType(fileName);
                file = (File )fileData.getFile();
                fileClientPath = file.getParent();

                if(!ImageUtil.checkValidImageType(contentType)){
                    errors.add(new ValidationError("image", "wrong format image"));
                }


//                System.out.println("fileName:" + fileName);
//                System.out.println("fileSize:" + file.length());
//
//                System.out.println("contentType:" + contentType);
//                System.out.println("fileClientPath:" + fileClientPath);
//
//                System.out.println("getPath:" + file.getPath());
//                System.out.println("getAbsolutePath:" + file.getAbsolutePath());
            }
        }


        return errors.isEmpty() ? null : errors;
    }


    public ItemImageUploadForm() {
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

    public String getDescription_id() {
        return description_id;
    }

    public void setDescription_id(String description_id) {
        this.description_id = description_id;
    }

    public String getFileNameToDel() {
        return fileNameToDel;
    }

    public void setFileNameToDel(String fileNameToDel) {
        this.fileNameToDel = fileNameToDel;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Http.MultipartFormData.FilePart<File> getFileData() {
        return fileData;
    }

    public void setFileData(Http.MultipartFormData.FilePart<File> fileData) {
        this.fileData = fileData;
    }
}
