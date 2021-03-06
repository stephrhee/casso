package casso.util;

import casso.model.Tag;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringUtil {

    private static String periodSymbol = ".";
    private static String hashtagSymbol = "#";
    private static String dollarSignSymbol = "$";
    private static String leftBracketSymbol = "[";
    private static String rightBracketSymbol = "]";

    private static String PERIOD = "PERIOD";
    private static String HASHTAG = "HASHTAG";
    private static String DOLLARSIGN = "DOLLARSIGN";
    private static String LEFTBRACKET = "LEFTBRACKET";
    private static String RIGHTBRACKET = "RIGHTBRACKET";

    private static String marker = "_";

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

    /**
     * The max length is arbitrarily set to 21 as there are 21 characters in "architectural subject"
     */
    public static String shorten(String string) {
        if (string.length() > 21) {
            string = string.substring(0, 21) + "...";
        }
        return string;
    }

    public static Function<Tag, String> tagsToTagDisplayableStrings = new Function<Tag, String>() {
        public String apply(Tag tag) {
            return "\u00A0\u00A0" + shorten(tag.mName) + "\u00A0\u00A0";
        }
    };

    public static Integer getIdFromIdentifier(String identifier) {
        String[] splitSubStrings = identifier.split(":");
        for (String subString : splitSubStrings) {
            Integer integer = getIntegerOrNull(subString);
            if (integer != null) {
                return integer;
            }
        }
        return null;
    }

    public static String getEncodedFirebasePath(String string) {
        return string
                .replace(periodSymbol, marker + PERIOD + marker)
                .replace(hashtagSymbol, marker + HASHTAG + marker)
                .replace(dollarSignSymbol, marker + DOLLARSIGN + marker)
                .replace(leftBracketSymbol, marker + LEFTBRACKET + marker)
                .replace(rightBracketSymbol, marker + RIGHTBRACKET + marker);
    }

    public static String getDecodedFirebasePath(String string) {
        return string
                .replace(marker + PERIOD + marker, periodSymbol)
                .replace(marker + HASHTAG + marker, hashtagSymbol)
                .replace(marker + DOLLARSIGN + marker, dollarSignSymbol)
                .replace(marker + LEFTBRACKET + marker, leftBracketSymbol)
                .replace(marker + RIGHTBRACKET + marker, rightBracketSymbol);
    }

    @JsonIgnore
    public static List<String> getSearchableStrings(String string) {
        List<String> splitWords = new ArrayList<>();
        if (string != null) {
            splitWords.addAll(Arrays.asList(string.split("\\s+")));
        }
        return splitWords;
    }

    public static String joinListIntoString(List<String> list, String delimiter) {
        if (list == null || list.size() == 0) {
            return null;
        }
        String string = list.get(0);
        if (list.size() > 1) {
            for (int i = 1; i < list.size(); i++) {
                if (list.get(i) != null && !list.get(i).equals("")) {
                    string += delimiter + list.get(i);
                }
            }
        }
        return string;
    }

}
