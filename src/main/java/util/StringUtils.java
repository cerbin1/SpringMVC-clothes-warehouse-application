package util;

public final class StringUtils {
    private StringUtils() {
    }

    /**
     * @param string string to check if it can be parsed to boolean
     * @return true if string is true/false
     */
    public static boolean isBoolean(String string) {
        return string.equals("true") || string.equals("false");
    }
}
