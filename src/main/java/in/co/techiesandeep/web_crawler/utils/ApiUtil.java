package in.co.techiesandeep.web_crawler.utils;

import in.co.techiesandeep.web_crawler.model.Page;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class ApiUtil {

    private static final String USER_AGENT = "Mozilla/5.0";
    private CallHandler handler;




    //ToDo replace with retrofit
    public Page fetchPageSource(String pageUrl){
        handler.init();
        BufferedReader in = null;

        try {
            URL obj = new URL(pageUrl);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("content-type", "text/html; charset=utf-8");
            int responseCode = con.getResponseCode();


            in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            handler.success();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            handler.onResult(response.toString());

        } catch (IOException e) {
            handler.onError(e.getMessage());
        }finally {
            try {
                if(in != null)
                 in.close();

            } catch (IOException e) {
            }
        }

        return null;
    }

    public void setHandler(CallHandler handler) {
        this.handler = handler;
    }





    public interface CallHandler{
        void init();
        void success();
        void onResult(String response);
        void onError(String msg);
    }
}
