package utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;

public class DateUtil {

    public static final String FORMAT_YEAR_DEFAULT = "yyyy";
    public static final String FORMAT_DATE_DEFAULT = "yyyyMMdd";
    public static final String FORMAT_DATE_VN_DEFAULT = "dd-MM-yyyy";
    public static final String FORMAT_YMDH = "yyyyMMddHH";
    public static final String FORMAT_YMDHM = "yyyyMMddHHmm";
    public static final String DATE_FORMAT_NOW = "yyyyMMddHHmmss";
    public static final String FORMAT_DATE_BIRTHDATE = "yyyy/MM/dd";
    public static final String FORMAT_DATE_BIRTHDATE_JP = "yyyy-MM-dd";
    public static final String FORMAT_DATE_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String FORMAT_DATE_YYYY_MM_DD_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_DAYOFWEEK = "EEE";
    public static final String FORMAT_DAYOFWEEK_FULL = "EEEE";
    public static final String FORMAT_MAP_HISTORY = "dd/MM/yyyy";


    public static final String DATE_DEFAULT = "1900/01/01";
    public static final String TIME_DEFAULT = "HH:mm:ss";
    public static final String TIME_GPS = "ddMMyyHHmmss.SSS";
    public static final String TIME_DATA_MAP = "dd-MM-yyyy,HH:mm";
    public static final String TIME_DATA_MAP_24 = "dd-MM-yyyy,HH:mm:ss.SSS";
    public static final String TIME_DATA_POSITION = "HH:mm - dd/MM/yyyy";
    public static final String TIME_PICKUP = "HH:mm";
    public static final String TIME_CALENDAR_EVENT = "dd-MM-yyyy,HH:mm";
    public static final String TIME_TABLE_DISPLAY = "dd/MM/yyyy HH:mm:ss";

    public static final String TIME_ITEM = "dd/MM/yyyy HH:mm";

    // second unit
    public static final int SECOND_OF_DAY_UNIT = 24 * 3600; // 1 day

    // milisecond unit
    public static final int MILISECOND_OF_DAY_UNIT = 24 * 3600 * 1000; // 1 day
    public static final int MILISECOND_OF_HOUR_UNIT = 3600 * 1000; // 1 hour
    public static final int MILISECOND_OF_MINUTE_UNIT = 60 * 1000; // 1 minute

    public static final String BEGIN_OF_DAY = "00:00:00";
    public static final String END_OF_DAY = "23:59:59";

    public static final String YEAR = "year";
    public static final String MONTH = "month";
    public static final String WEEK = "week";
    public static final String DAY = "day";
    public static final String RANGE = "range";

    public static String getDateByFormat(String dateFormat) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(cal.getTime());
    }

    public static Date now() {
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }

    public static long getCurrentTimeStamp() {
        Calendar cal = Calendar.getInstance();
        return cal.getTimeInMillis();
    }

    /**
     * if date1 = date2 return 0.
     * if date1 > date2 return 1.
     * if date1 < date2 return -1.
     * @param date1
     * @param date2
     * @param dateFormat1
     * @param dateFormat2
     * @return
     */
    public static int compareDateByStringFormat(String date1, String date2, String dateFormat1, String dateFormat2) {
        try {
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            SimpleDateFormat sdf1 = new SimpleDateFormat(dateFormat1);
            Date d1 = sdf1.parse(date1);

            SimpleDateFormat sdf2 = new SimpleDateFormat(dateFormat2);
            Date d2 = sdf2.parse(date2);

            cal1.setTime(d1);
            cal2.setTime(d2);

            int result = cal1.compareTo(cal2);
            return result;
        } catch (ParseException e) {
            return 0;
        }
    }

    public static long diffMillis(Date date1, Date date2){
        long diff = Math.abs(date1.getTime() - date2.getTime());
        return diff;
    }

    public static long diffSeconds(Date date1, Date date2){
        long diff = diffMillis(date1, date2) / 1000;
        return diff;
    }

    public static long diffMinutes(Date date1, Date date2){
        long diff = diffMillis(date1, date2) / (60 * 1000);
        return diff;
    }

    public static long diffHours(Date date1, Date date2){
        long diff = diffMillis(date1, date2) / (60 * 60 * 1000);
        return diff;
    }

    public static long diffDays(Date date1, Date date2){
        long diff = diffMillis(date1, date2) / (24 * 60 * 60 * 1000);
        return diff;
    }

    public static long getMillisFromDate(String dateString, String dateFormat) {
        Calendar cal = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            Date date = sdf.parse(dateString);
            cal.setTime(date);
        } catch (Exception ex) {
            return getCurrentTimeStamp();
        }
        return cal.getTimeInMillis();
    }

    public static String getDateByFormat(String dateFormat, int unit, int amount) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        cal.add(unit, amount);
        return sdf.format(cal.getTime());
    }

    public static String getDateByFormat(long time, String dateFormat) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            Date date = new Date(ConvertTimeStamp(time).getTime());
            cal.setTime(date);
        } catch (Exception ex) {

        }
        return sdf.format(cal.getTime());
    }

    public static String getDateByFormat(Date date, String dateFormat) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            cal.setTime(date);
        } catch (Exception ex) {

        }
        return sdf.format(cal.getTime());
    }


    public static String getDateFromMicroSecondByFormat(long time, String dateFormat) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            Date date = new Date(ConvertTimeStamp(time/1000).getTime());
            cal.setTime(date);
        } catch (Exception ex) {

        }
        return sdf.format(cal.getTime());
    }

    public static String dateAddByFormat(String dateString, String dateFormat,
                                         int unit, int amount) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            Date date = sdf.parse(dateString);
            cal.setTime(date);
            cal.add(unit, amount);
        } catch (Exception ex) {

        }
        return sdf.format(cal.getTime());
    }

    public static long CurrentTimeStamp() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public static Timestamp ConvertTimeStamp(long timevalue) {
        Timestamp time = new Timestamp(timevalue);
        return time;
    }


    public static Date convertDate(long time) {
        Date date = new Date(ConvertTimeStamp(time).getTime());
        return date;
    }

    public static String convertDateFormat(String dateString, String dateFormat){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            Date date = new Date(dateString);
            cal.setTime(date);
        } catch (Exception ex) {

        }
        return sdf.format(cal.getTime());
    }

    public static Date convertStringtoDate(String input, String format){
        try {
            SimpleDateFormat formater = new SimpleDateFormat(format);
            Date date = formater.parse(input);

            return date;
        } catch (Exception e){
            return null;
        }
    }

    public static Date convertStringtoDate2(String input, String format){
        try {
            SimpleDateFormat formater = new SimpleDateFormat(format);
            TimeZone timeZone = TimeZone.getDefault();
            formater.setTimeZone(timeZone);
            Date date = formater.parse(input);

            return date;
        } catch (Exception e){
            return null;
        }
    }

    public static String convertDatetoString(Date input, String format){
        try {
            SimpleDateFormat formater = new SimpleDateFormat(format);
            String date = formater.format(input);
            return date;
        } catch (Exception e){
            return null;
        }
    }

    public static boolean isValidDate(String dateString, String dateFormat){
        if(dateString == null || dateString.equals(""))
            return false;
        if(dateFormat.equals(FORMAT_DATE_BIRTHDATE)){
            String[] field = dateString.split("/");
            if((field.length != 3) || (field[0].length() != 4))
                return false;
        }
        if(dateFormat.equals(FORMAT_DATE_BIRTHDATE_JP)){
            String[] field = dateString.split("-");
            if((field.length != 3) || (field[0].length() != 4))
                return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date testdate = null;
        // we will now try to parse the string into date form
        try {
            testdate = sdf.parse(dateString);
        }
        // if the format of the string provided doesn't match the format we
        // declared in SimpleDateFormat() we will get an exception
        catch (Exception ex) {
            return false;
        }
        // dateformat.parse will accept any date as long as it's in the format
        // you defined, it simply rolls dates over, for example, december 32
        // becomes jan 1 and december 0 becomes november 30
        // This statement will make sure that once the string
        // has been checked for proper formatting that the date is still the
        // date that was entered, if it's not, we assume that the date is invalid

/*		if(!sdf.format(testdate).equals(dateString)){
			return false;
		}*/
        // if we make it to here without getting an error it is assumed that
        // the date was a valid one and that it's in the proper format
        return true;
    }

    public static long convertByteArrayToDate(byte[] byteArrayTime){
        return TimeUUIDUtils.getTimeFromUUID(byteArrayTime);
    }

    public static String convertByteArrayToDate(byte[] byteArrayTime, String dateFormat){
        long time = TimeUUIDUtils.getTimeFromUUID(byteArrayTime);
        return getDateByFormat(time, dateFormat);
    }

    public static long convertStringByteArrayToDate(String strByteArrayTime){
        return TimeUUIDUtils.getTimeFromUUID(utils.StringUtil.hexToBytes(strByteArrayTime));
    }

    public static Date convertTimeUUIDStrToDate(String strByteArrayTime){
        long time = TimeUUIDUtils.getTimeFromUUID(StringUtil.hexToBytes(strByteArrayTime));
        return convertDate(time);
    }
    public static String convertStringByteArrayToDate(String strByteArrayTime, String dateFormat){
        long time = TimeUUIDUtils.getTimeFromUUID(StringUtil.hexToBytes(strByteArrayTime));
        return getDateByFormat(time, dateFormat);
    }

    public static byte[] convertDateToTimeUUID(Date date){

        return TimeUUIDUtils.asByteArray(TimeUUIDUtils.getTimeUUID(date.getTime()));
    }

    public static int getSecondsOfCurrentDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime( new Date(System.currentTimeMillis()) );

        int hour = cal.get( Calendar.HOUR_OF_DAY );
        int minute = cal.get( Calendar.MINUTE );
        int second = cal.get( Calendar.SECOND );

        return hour * 60 * 60 + minute * 60 + second;
    }

    public static Date getFirstDay(String strDate, String type){
        try{
            Date date = convertStringtoDate(strDate, FORMAT_DATE_YYYY_MM_DD);


            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            if(type.equals(YEAR)){
                cal.set(Calendar.DAY_OF_YEAR, 1);
            }else if(type.equals(MONTH)){
                cal.set(Calendar.DAY_OF_MONTH, 1);
            }else if(type.equals(WEEK)){
                cal.set(Calendar.DAY_OF_WEEK, 1);
            }

            return cal.getTime();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Date getLastDay(String strDate, String type){
        try{
            Date date = convertStringtoDate(strDate, FORMAT_DATE_YYYY_MM_DD);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);


            if(type.equals(YEAR)){
                cal.set(Calendar.DAY_OF_YEAR, cal.getActualMaximum(Calendar.DAY_OF_YEAR));
            }else if(type.equals(MONTH)){
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            }else if(type.equals(WEEK)){
                cal.set(Calendar.DAY_OF_WEEK, cal.getActualMaximum(Calendar.DAY_OF_WEEK));
            }


            return getEndOfDate(cal.getTime());
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public static Date getBeginOfDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        return cal.getTime();
    }

    public static Date getEndOfDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        cal.set(Calendar.HOUR, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);

        return cal.getTime();
    }

    public static String getStartDayOfLastMonth(){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return dateFormat.format(cal.getTime());
    }

    public static String getEndDayOfLastMonth(){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return dateFormat.format(cal.getTime());
    }

    public static String getStartDayOfLastWeek(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, -(cal.DAY_OF_WEEK + 7));
        return dateFormat.format(cal.getTime());
    }

    public static String getEndDayOfLastWeek(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, -(cal.DAY_OF_WEEK));
        return dateFormat.format(cal.getTime());
    }

    public static String getStartDateOfWeek(String format, int amount){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        // set  first day of week is monday
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // get start of this week in milliseconds
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.add(Calendar.WEEK_OF_MONTH, amount);
        return sdf.format(cal.getTime());
    }

    public static String getEndDateOfWeek(String format, int amount){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        // set  first day of week is monday
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // get start of this week in milliseconds
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        cal.set(Calendar.HOUR, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.add(Calendar.WEEK_OF_MONTH, amount);
        return sdf.format(cal.getTime());
        //String startDateOfWeek = getStartDateOfWeek(format, amount);
        //String endDateOfWeek = dateAddByFormat(startDateOfWeek, format, Calendar.DATE, 6);
        //return endDateOfWeek;
    }

    public static boolean isWeekday(){
        Calendar cal = Calendar.getInstance();
        int dow = cal.get (Calendar.DAY_OF_WEEK);
        boolean isWeekday = ((dow >= Calendar.MONDAY) && (dow <= Calendar.FRIDAY));
        return isWeekday;
    }

    public static boolean isWeekday(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dow = cal.get (Calendar.DAY_OF_WEEK);
        boolean isWeekday = ((dow >= Calendar.MONDAY) && (dow <= Calendar.FRIDAY));
        return isWeekday;
    }

    public static int getDayOfWeek(){
        Calendar cal = Calendar.getInstance();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek;
    }

    public static int getDayOfWeek(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek;
    }

    public static int getMinuteOfCurrentDate() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE );

        return hour * 60 + minute;
    }

    public static int getMinuteOfDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY );
        int minute = cal.get(Calendar.MINUTE );

        return hour * 60 + minute;
    }

    public static long getMinuteOfTimeStr(String timeStr) {
        // HH:mm
        String[] timeSplit = timeStr.split(":");
        String hourStr =timeSplit[0];
        String minuteStr =timeSplit[1];
        long hour = ParseUtil.parseLong(hourStr);
        long minute = ParseUtil.parseLong(minuteStr);

        return hour * 60 + minute;
    }
}