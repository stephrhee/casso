package casso.util;

public class StringUtil {

    public static String getStringOrNull(String string) {
        return (string == null || string.equals("null") || string.equals("[]")) ? null : string;
    }

    public static Integer getIntegerOrNull(String string) {
        try {
            return (string == null || string.equals("null")) ? null : Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
