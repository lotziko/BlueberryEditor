package utils;

public class StringUtils {
	public static String join(String separator, String[] array) {
		String result = "";
		if (array.length > 0) {
			result += array[0];
			for (int i = 1; i < array.length; i++) {
				result += separator + array[i];
			}
		}
		return result;
	}
}
