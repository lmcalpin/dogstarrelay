package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * TODO: Get rid of this code! It's only here so I can do a
 * synchronous lookup, because:
 * 1) AsyncResult isn't (currently?) working on Heroku
 * 2) The WS API has changed completely in the bleeding edge
 * versions of Play 2.0, so I'm going to wait until it simmers
 * down a little.
 * 
 * @author Lawrence McAlpin
 */
public class WebService {
    public static String get(String serviceURL) {
        StringBuilder response = new StringBuilder();
        InputStreamReader isr = null;
        BufferedReader in = null;
        try {
            URL url = new URL(serviceURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            conn.setRequestMethod("GET");
            conn.setDoOutput(false);
            conn.setDoInput(true);

            // Read the response and write it to standard out.
            isr = new InputStreamReader(conn.getInputStream());
            in = new BufferedReader(isr);
            String line = null;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } catch (Exception e) {
            // So this is one reason why I prefer Scala.
            throw new CheckedExceptionsAreStupidException(e);
        } finally {
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    // Oh look. Here's another reason.
                    throw new CheckedExceptionsAreStupidException(e);
                }
            }
        }
    }
}
