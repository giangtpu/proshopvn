package utils;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.net.URI;
import java.security.MessageDigest;
import java.util.*;

public class StringUtil {
	protected static final char[] HEX_CHARS = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	protected static final char[] SECURE_CHARS = { 'a', 'b', 'c', 'd', 'e',
			'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
			's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E',
			'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
			'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9' };

	public static String blank = "empty";
	public static java.util.UUID getTimeUUID() {
		return TimeUUIDUtils.getUniqueTimeUUIDinMillis();
	}

	public static String getTimeUUIDString() {
		return new String(bytesToHex(getTimeUUIDByteArray()));
	}

	public static byte[] getTimeUUIDByteArray() {
		return TimeUUIDUtils.asByteArray(TimeUUIDUtils
				.getUniqueTimeUUIDinMillis());
	}

	public static String byteArrayIdToStringId(byte[] bytes) {
		return new String(bytesToHex(bytes));
	}

	public static byte[] stringIdToByteArrayId(String id) {
		if (!StringUtils.isEmpty(id)) {
			return hexToBytes(id);
		}
		return null;
	}

	public static char[] bytesToHex(byte[] raw) {
		if (raw != null) {
			int length = raw.length;
			char[] hex = new char[length * 2];
			for (int i = 0; i < length; i++) {
				int value = (raw[i] + 256) % 256;
				int highIndex = value >> 4;
				int lowIndex = value & 0x0f;
				hex[i * 2 + 0] = HEX_CHARS[highIndex];
				hex[i * 2 + 1] = HEX_CHARS[lowIndex];
			}
			return hex;
		} else
			return "".toCharArray();
	}

	public static byte[] hexToBytes(char[] hex) {
		int length = hex.length / 2;
		byte[] raw = new byte[length];
		for (int i = 0; i < length; i++) {
			int high = Character.digit(hex[i * 2], 16);
			int low = Character.digit(hex[i * 2 + 1], 16);
			int value = (high << 4) | low;
			if (value > 127)
				value -= 256;
			raw[i] = (byte) value;
		}
		return raw;
	}

	public static byte[] hexToBytes(String hex) {
		return hexToBytes(hex.toCharArray());
	}

	public static String toMD5(String original) {
		String destination = "";
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(original.getBytes());
			BigInteger dis = new BigInteger(1, md5.digest());
			destination = dis.toString(16);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return destination;
	}
	
	public static boolean validateEmail(String email) {
		// String regex =
		// "^[_A-Za-z0-9-\\.]+[_A-Za-z0-9-\\.]+@[A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
		String regex = "^[^\\s\\t@]+@[A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
		return email.matches(regex);
	}

	public static boolean validateEmailPolicy(String email) {
		// String regex =
		// "^[_A-Za-z0-9-\\.]+[_A-Za-z0-9-\\.]+@[A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
		String regex2=".*(m1s.vn)$";
		if (email.matches(regex2))
		{
			return false;
		}
		String regex3="^([Aa][Dd][Mm][Ii][Nn]).*";
		if (email.matches(regex3))
		{
			return false;
		}

		String regex = "^[^\\s\\t@]+@[A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
		return email.matches(regex);
	}


	public static boolean validatePhone(String phone) {
		String regex = "[1-9][0-9]{10,14}";
		return phone.matches(regex);
	}

	public static boolean validatePhoneVn(String phone) {
		String regex = "^0\\d{9,10}$";
		return phone.matches(regex);
	}


	
	public static long generateRandom(int length) {
	    Random random = new Random();
	    char[] digits = new char[length];
	    digits[0] = (char) (random.nextInt(9) + '1');
	    for (int i = 1; i < length; i++) {
	        digits[i] = (char) (random.nextInt(10) + '0');
	    }
	    return Long.parseLong(new String(digits));
	}

	public static List sortMapByValue(final Map m, boolean asc) {
		List keys = new ArrayList();
		keys.addAll(m.keySet());
		if (asc == true) {
			Collections.sort(keys, new Comparator() {
				public int compare(Object o1, Object o2) {
					Object v1 = m.get(o1);
					Object v2 = m.get(o2);
					if (v1 == null) {
						return (v2 == null) ? 0 : 1;
					} else if (v1 instanceof Comparable) {
						return ((Comparable) v1).compareTo(v2);
					} else {
						return 0;
					}
				}
			});
		} else {
			Collections.sort(keys, new Comparator() {
				public int compare(Object o1, Object o2) {
					Object v1 = m.get(o1);
					Object v2 = m.get(o2);
					if (v2 == null) {
						return (v1 == null) ? 0 : 1;
					} else if (v2 instanceof Comparable) {
						return ((Comparable) v2).compareTo(v1);
					} else {
						return 0;
					}
				}
			});
		}
		return keys;
	}

	public static List sortMapByValue(final Map m) {
		List keys = new ArrayList();
		keys.addAll(m.keySet());

		Collections.sort(keys, new Comparator() {
			public int compare(Object o1, Object o2) {
				Object v1 = m.get(o1);
				Object v2 = m.get(o2);
				if (v1 == null) {
					return (v2 == null) ? 0 : 1;
				} else if (v1 instanceof Comparable) {
					return ((Comparable) v1).compareTo(v2);
				} else {
					return 0;
				}
			}
		});

		return keys;
	}
	
	public static double distance(double lat1, double lon1, double lat2,
			double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		dist = dist * 1.609344;
		return (dist);
	}

	public static boolean nearbyPlace(String placeLat, String placeLon,
			String pointlat, String pointLon, String radius) {

		try {
			double placeLatDouble = Double.parseDouble(placeLat);
			double placeLonDouble = Double.parseDouble(placeLon);
			double pointLatDouble = Double.parseDouble(pointlat);
			double pointLonDouble = Double.parseDouble(pointLon);
			double radiusDouble = Double.parseDouble(radius);

			double distance = distance(placeLatDouble, placeLonDouble,
					pointLatDouble, pointLonDouble);

			if (distance > radiusDouble) {
				return false;
			}

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean nearbyPlace(String pointlat, String pointLon,
			String placeLat, String placeLon, String deltaLat, String deltaLon) {

		try {
			double placeLatDouble = Double.parseDouble(placeLat);
			double placeLonDouble = Double.parseDouble(placeLon);
			double pointLatDouble = Double.parseDouble(pointlat);
			double pointLonDouble = Double.parseDouble(pointLon);
			double deltaLatDouble = Double.parseDouble(deltaLat);
			double deltaLonDouble = Double.parseDouble(deltaLon);

			double distanceLat = distance(placeLatDouble, pointLonDouble,
					pointLatDouble, pointLonDouble);
			if (distanceLat > deltaLatDouble) {
				return false;
			}

			double distanceLon = distance(pointLatDouble, placeLonDouble,
					pointLatDouble, pointLonDouble);
			if (distanceLon > deltaLonDouble) {
				return false;
			}

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static String getDiagonal(String strSide1, String strSide2) {
		try {
			double side1 = Double.parseDouble(strSide1);
			double side2 = Double.parseDouble(strSide2);

			double radiusDouble = Math.sqrt(side1 * side1 + side2 * side2);

			return String.valueOf(radiusDouble);

		} catch (Exception e) {
			return "";
		}
	}

	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private static double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}

	public static URI getUriFromURL(String url){
		URI uri = URI.create(url);
		return uri;
	}
	public static String getPathFromURL(String url){
		URI uri = getUriFromURL(url);
		if(uri==null){
			return "";
		}
		return uri.getPath();
	}
    public  static  double randomInRange(double min , double max){
        double random = (new Random().nextDouble())*2 -1;
        double result = min + (random * (max - min));

        return  result;
    }
    /*min max in meter unit*/
    public  static double getLatNearPlace(double lat, double min, double max) {
        double start = min/30.82/3600;
        double end =max/30.82/3600;
        double delta = randomInRange(start, end);
        double result =lat +delta;
        return  result;
    }

    /*min max in meter unit*/
    public  static double getLonNearPlace(double lat, double lon, double min, double max) {
        double deviationByLat =(Math.PI/360)*(Math.cos(deg2rad(lat)))*6367449;
        double start = min/deviationByLat;
        double end =max/deviationByLat;
        double delta = randomInRange(start, end);
        double result =lon +delta;
        return  result;
    }

	public static int randomNumber(int min, int max){
		Random random = new Random();
		int rand = random.nextInt(max-min+1) + min;
		return rand;
	}
	public static boolean isExistComma(String s){
		if(s==null){
			return true;
		}
		int i = s.indexOf(",");
		if(i<0){
			return false;
		}

		return true;
	}

	public static boolean isComplicatedPassword(String password)
	{
		String pattern="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

//		^                 # start-of-string
//		(?=.*[0-9])       # a digit must occur at least once
//		(?=.*[a-z])       # a lower case letter must occur at least once
//		(?=.*[A-Z])       # an upper case letter must occur at least once
//		(?=.*[@#$%^&+=])  # a special character must occur at least once
//		(?=\S+$)          # no whitespace allowed in the entire string
//		.{8,}             # anything, at least eight places though
//		$                 # end-of-string

		return password.matches(pattern);
	}

	public static boolean isInteger(String s) {
		return isInteger(s,10);
	}

	public static boolean isInteger(String s, int radix) {
		if(s.isEmpty()) return false;
		for(int i = 0; i < s.length(); i++) {
			if(i == 0 && s.charAt(i) == '-') {
				if(s.length() == 1) return false;
				else continue;
			}
			if(Character.digit(s.charAt(i),radix) < 0) return false;
		}
		return true;
	}
}
