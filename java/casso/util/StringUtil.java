package casso.util;

import casso.model.Tag;
import com.google.common.base.Function;

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

    public static String stripParentheses(String string) {
        return string.split(" \\(")[0];
    }

    public static Function<Tag, String> tagsToTagStrings = new Function<Tag, String>() {
        public String apply(Tag tag) {
            return tag.mName;
        }
    };

}
