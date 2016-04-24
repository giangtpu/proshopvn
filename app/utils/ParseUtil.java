package utils;

import java.util.Date;

/**
 * Created by giangdaika on 24/04/2016.
 */
public class ParseUtil {
    public static int parseInt(String s) {
        int i = 0;
        try {
            i = Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }
        return i;
    }

    public static double parseDouble(String s) {
        double i = 0D;
        try {
            i = Double.parseDouble(s);
        } catch (Exception e) {
            return 0D;
        }
        return i;
    }

    public static long parseLong(String s) {
        long  i = 0l;
        try {
            i = Long.parseLong(s);
        } catch (Exception e) {
            return 0l;
        }
        return i;
    }
    public static float parseFloat(String s) {
        float  i = 0f;
        try {
            i = Float.parseFloat(s);
        } catch (Exception e) {
            return 0f;
        }
        return i;
    }
    public static long parseDate(Date date){
        if(date == null)
            return 0l;
        else
            return date.getTime();
    }

    public static String parseString(Object obj) {
        String str ="";
        try {
            str = String.valueOf(obj);
        } catch (Exception e) {
            return "";
        }
        return str;
    }
}
