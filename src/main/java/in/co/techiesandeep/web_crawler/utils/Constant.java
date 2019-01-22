package in.co.techiesandeep.web_crawler.utils;

public interface Constant {
    String GET_URL = "https://www.prudential.co.uk/";
    String REGEX_FOR_HREF = "http[s]*://(\\w+\\.)*(\\w+)";
    String HTML_A_TAG_PATTERN = "(?i)<a([^>]+)>(.+?)</a>";
    String HTML_A_HREF_TAG_PATTERN =
            "\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";
}
