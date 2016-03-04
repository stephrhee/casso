package casso.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpUtil {

    public static String convertInputStreamToString(InputStream stream) {
        try {
            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
