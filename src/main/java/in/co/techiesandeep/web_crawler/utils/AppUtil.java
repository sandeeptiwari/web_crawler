package in.co.techiesandeep.web_crawler.utils;

import in.co.techiesandeep.web_crawler.model.Page;

import java.util.Set;


public class AppUtil {

    public static boolean shouldConsider(Set<Page> pages, Page page) {
        String pageUrl = page.getPageLink();

        if(pages.contains(page)) { return false; }
        if(pageUrl.startsWith("javascript:"))  { return false; }
        if(pageUrl.startsWith("#"))            { return false; }
        if(pageUrl.endsWith(".swf"))           { return false; }
        if(pageUrl.endsWith(".pdf"))           { return false; }
        if(pageUrl.endsWith(".png"))           { return false; }
        if(pageUrl.endsWith(".gif"))           { return false; }
        if(pageUrl.endsWith(".jpg"))           { return false; }
        if(pageUrl.endsWith(".jpeg"))          { return false; }

        return true;
    }

    public static boolean isCompleteUrl(String urlString) {
        return urlString.indexOf("http://") == 0 || urlString.indexOf("https://") == 0;
    }
}
