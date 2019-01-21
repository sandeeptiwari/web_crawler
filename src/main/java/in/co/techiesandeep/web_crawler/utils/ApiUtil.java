package in.co.techiesandeep.web_crawler.utils;

import in.co.techiesandeep.web_crawler.model.Page;
import io.reactivex.Observable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ApiUtil {
    private static String GET_URL = "https://www.prudential.co.uk/";
    private static final String USER_AGENT = "Mozilla/5.0";

    public Page fetchPageSource(){
        Page pageSource = new Page();

        try {
            URL obj = new URL(GET_URL);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("content-type", "text/html; charset=utf-8");
            int responseCode = con.getResponseCode();
            pageSource.setResponseCode(responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();



        } catch (IOException e) {
            e.printStackTrace();
        }

        return pageSource;
    }
}
