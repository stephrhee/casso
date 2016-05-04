package casso.util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by stephrhee on 5/3/16.
 */

public class FontUtil {

    public static final String mAlegreyaRegularTypefaceString = "Alegreya-Regular.otf";
    public static final String mAlegreyaItalicTypefaceString = "Alegreya-Italic.otf";
    public static final String mGoudyStMRegularTypefaceString = "GoudyStM.otf";
    public static final String mGoudyStMItalicTypefaceString = "GoudyStM-Italic.otf";
    public static final String mMateRegularTypefaceString = "Mate-Regular.otf";
    public static final String mMateItalicTypefaceString = "Mate-Italic.otf";
    public static final String mCardoRegularTypefaceString = "Cardo-Regular.ttf";
    public static final String mCardoItalicTypefaceString = "Cardo-Italic.ttf";

    public static Typeface getTypeface(Context context, String typefaceString) {
        return Typeface.createFromAsset(context.getAssets(), typefaceString);
    }

}
