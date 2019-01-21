package in.co.techiesandeep.web_crawler.model;

import in.co.techiesandeep.web_crawler.utils.CustomUrl;

import java.util.HashSet;
import java.util.Set;

public class Page {
    private int responseCode;
    private Set<CustomUrl> urls = new HashSet<>();

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public Set<CustomUrl> getUrls() {
        return urls;
    }

    public void setUrls(Set<CustomUrl> urls) {
        this.urls = urls;
    }
}
