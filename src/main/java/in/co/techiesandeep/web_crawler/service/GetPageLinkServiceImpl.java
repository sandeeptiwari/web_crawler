package in.co.techiesandeep.web_crawler.service;

import in.co.techiesandeep.web_crawler.model.Page;
import in.co.techiesandeep.web_crawler.utils.Constant;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class GetPageLinkServiceImpl implements GetPageLinkService {
    private Logger logger = LoggerFactory.getLogger(GetPageLinkServiceImpl.class);

    private Set<Page> pages = new LinkedHashSet<>();
    private Queue<Page> queue = new LinkedList<>();
    public static final int THREAD_COUNT = 5;
    private static final long PAUSE_TIME = 5000;
    private List<Future<Page>> futures = new ArrayList<>();
    private ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);


    @Override
    public Set<Page> getPage() throws IOException, ExecutionException, InterruptedException {
        Page page = new Page("Home", Constant.GET_URL, "");
        submitNewPage(page);
        init();
        return pages;
    }

    private Set<Page> init() {
        synchronized (this){
            while (!queue.isEmpty()) {
                Page currentNode = queue.poll();
                logger.info("===> "+currentNode.getPageName());

                if (isCompleteUrl(currentNode.getPageLink()))
                    continue;

                submitNewPage(currentNode);
            }
        }

        return pages;
    }

    private void submitNewPage(Page page) {
        if (!shouldVisit(page)) {
            PageCrawlerTask task = new PageCrawlerTask(page.getPageLink(), page.getPageName(), page.getBaseUrl());
            try {
                Future<Page> future = executor.submit(task);
                Page p = future.get();
                pages.add(p);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    class PageCrawlerTask implements Callable<Page> {
        private String url;
        private String pageName;
        private String baseUrl;

        PageCrawlerTask(String url, String pageName, String baseUrl) {
            this.url = url;
            this.pageName = pageName;
            this.baseUrl = baseUrl;
        }

        public Page run() {
            System.out.print("execute ==========>  ");

            Page page = new Page(pageName, url, baseUrl);
            page.setVisited(true);
            if (!isCompleteUrl(url)) {
                url = url.substring(1);
                url = baseUrl + url;
            }
            Document doc = null;
            try {
                doc = Jsoup.connect(url).get();
                Elements links = doc.select("a[href]"); // a with href
                Set<Page> newPages = processLink(links);
                page.setChildrenSet(newPages);
                addIntoQueue(newPages);
                pages.add(page);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return page;
        }

        @Override
        public Page call() throws Exception {
            return run();
        }
    }

    private void addIntoQueue(Set<Page> newPages) {
        newPages.stream().forEach(ele -> queue.add(ele));
    }

    private Set<Page> processLink(Elements links) {
        return links.stream()
                .filter(ele -> !ele.attr("href").contains("#") &&
                        !StringUtils.isBlank(ele.attr("href")) && !ele.attr("href")
                        .contains("javascript"))
                .map(ele -> {
                    Page p = new Page(ele.text(), ele.attr("href"), ele.baseUri());
                    if (!pages.contains(p))
                        queue.add(p);
                    return p;
                }).collect(Collectors.toSet());
    }

    private boolean shouldVisit(Page page) {
        return pages.contains(page);
    }

    private boolean isCompleteUrl(String urlString) {
        return urlString.indexOf("http://") == 0 || urlString.indexOf("https://") == 0;
    }

}
