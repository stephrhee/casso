package casso.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by stephrhee on 5/4/16.
 */

public class NetUtil {

    public static boolean isOnline(Context ct) {
        // Check for connectivity, return true if connected or connecting.
        ConnectivityManager cm = (ConnectivityManager) ct.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnectedOrConnecting());
    }

}
