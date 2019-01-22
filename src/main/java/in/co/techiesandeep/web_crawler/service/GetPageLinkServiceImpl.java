package in.co.techiesandeep.web_crawler.service;

import in.co.techiesandeep.web_crawler.model.Page;
import in.co.techiesandeep.web_crawler.utils.ApiUtil;
import in.co.techiesandeep.web_crawler.utils.Constant;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class GetPageLinkServiceImpl  implements GetPageLinkService{
    private Set<Page> pages = new LinkedHashSet<>();
    private Queue<Page> queue = new LinkedList<>();
    private Map<String, Boolean> traverseMap = new HashMap<>();
    private ApiUtil util;


    GetPageLinkServiceImpl(ApiUtil util){
        this.util = util;
    }

    @Override
    public Set<Page> getPage() throws IOException {
        Page page = new Page("Home", Constant.GET_URL, "");

        queue.add(page);

        while(!queue.isEmpty()){
            Page currentNode = queue.poll();
            if(pages.contains(currentNode)){
                continue;
            }

            while (!currentNode.isVisited()){
                try {
                    String url = currentNode.getPageLink();
                    if(isCompleteUrl(url) && !queue.isEmpty()){
                        currentNode.setVisited(true);
                        pages.add(currentNode);
                        continue;
                    }
                    if(url.contains("#") || url.contains("Javascript")) {
                        currentNode.setVisited(true);
                        continue;
                    }

                    url = currentNode.getBaseUrl() + url;
                    Document doc = Jsoup.connect(url).get();
                    Elements links = doc.select("a[href]"); // a with href

                    currentNode.setVisited(true);
                    List<String> urls = links.eachAttr("href");

                    Set<Page> pageSet = links.stream().filter(ele -> !ele.attr("href").contains("#") &&
                            !ele.attr("href").contains("Javascript")).map(ele -> {
                        Page p = new Page(ele.text(), ele.attr("href"), ele.baseUri());
                        if(!pages.contains(p))
                             queue.add(p);
                        return p;
                    }).collect(Collectors.toSet());

                    page.setChildrenSet(pageSet);
                    pages.add(currentNode);
                }catch (Exception e){
                    currentNode.setVisited(true);
                }
            }
        }
        return pages;
    }



    private boolean isCompleteUrl(String urlString){
        return urlString.indexOf("http://") == 0 || urlString.indexOf("https://") == 0;
    }

}
