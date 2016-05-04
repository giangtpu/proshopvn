package utils;



import play.Application;
import play.Configuration;

import java.util.Date;
import java.util.regex.Pattern;

public class ItemHelper {

	private static final Pattern latpattern = Pattern.compile(
			"(\\d{2})(\\d{2}\\.\\d+)");
	private static final Pattern lonpattern = Pattern.compile(
			"(\\d{3})(\\d{2}\\.\\d+)");

	public static String generateToken(){
		return Long.toString(StringUtil.generateRandom(6));
	}


	public static String getTimeStampID(int i){

		long millis = System.currentTimeMillis()+i;
		return Long.toString(millis);
	}
	public static String getTimeStampID(){

		long millis = System.currentTimeMillis();
		return Long.toString(millis);
	}
	public static String generateId() {
		return StringUtil.getTimeUUIDString();
	}


	
	public static boolean checkSessionExpired(Date sessionDate){
		Date now = DateUtil.now();
		long diffMills = DateUtil.diffMillis(now, sessionDate);
		if(diffMills > StaticData.SSID_TIME_EXPIRED){
			return true;
		}
		return false;
	}
	public class StaticContent{
		public static final String LINK_ITEM_IMAGE ="content.link.image.item";
		public static final String LINK_ITEM_DEFAULT ="content.default.linkitem";
		public static final String LINK_CATEGORY_IMAGE ="content.link.image.category";
		public static final String LINK_CATEGORY_DEFAULT ="content.default.linkcategory";

	}

	public static String itemImageLinkPath = AppHelper.getAppConfigString(StaticContent.LINK_ITEM_IMAGE);
	public static String itemDefaultLinkPath = AppHelper.getAppConfigString(StaticContent.LINK_ITEM_DEFAULT);
	public static String categoryImageLinkPath = AppHelper.getAppConfigString(StaticContent.LINK_CATEGORY_IMAGE);
	public static String categoryDefaultLinkPath = AppHelper.getAppConfigString(StaticContent.LINK_CATEGORY_DEFAULT);




}
