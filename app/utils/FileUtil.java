package utils;

/**
 * Created by giangbb on 11/09/2015.
 */
public class FileUtil {
    public static String getFileType(String fileName) {
        String[] FileNameSplit   = fileName.split("\\.");
        if(FileNameSplit.length <=1){
            return null;
        }
        return FileNameSplit[FileNameSplit.length - 1];
    }

}
