package in.co.techiesandeep.web_crawler.service;

import in.co.techiesandeep.web_crawler.model.Page;
import in.co.techiesandeep.web_crawler.utils.AppUtil;
import in.co.techiesandeep.web_crawler.utils.Constant;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class GetPageLinkServiceImpl implements GetPageLinkService {
    private Logger logger = LoggerFactory.getLogger(GetPageLinkServiceImpl.class);

    private Set<Page> pages = new LinkedHashSet<>();
    private LinkedBlockingQueue<Page> queue = new LinkedBlockingQueue<>();
    private CyclicBarrier barrier = new CyclicBarrier(2);
    private ExecutorService executor = null;


    @Override
    public Map<String, List<Page>> getPage() throws IOException, ExecutionException, InterruptedException {
        Page page = new Page("Home", Constant.GET_URL, "");
        queue.add(page);
        executor = Executors.newFixedThreadPool(5);
        submitNewPage(page);
        init();
        Map<String, List<Page>> pageMap = pages.stream().filter(p -> p != null).collect(Collectors.groupingBy(p -> p.getPageName()));

        logger.info("final result ===> " + pageMap);
        return pageMap;
    }

    private Set<Page> init() {

        while (!queue.isEmpty()) {
            Page currentNode = null;
            try {
                currentNode = queue.take();
            } catch (InterruptedException e) {
                logger.error("Service ==> " + e.getMessage());
            }
            //logger.info("===> " + currentNode.getPageName());

            try {
                submitNewPage(currentNode);

                if (queue.isEmpty()) {
                    int party = barrier.await();
                    logger.info("Parties reached " + party);
                }
            } catch (BrokenBarrierException | InterruptedException e) {
                logger.error("Service ==> " + e.getMessage());
            }
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            logger.error("Service ==> " + e.getMessage());
        }
        return pages;
    }

    private void submitNewPage(Page page) {
        if (AppUtil.shouldConsider(pages, page)) {
            PageCrawlerTask task = new PageCrawlerTask(page.getPageLink(), page.getPageName(), page.getBaseUrl());
            executor.submit(task);
        }
    }


    class PageCrawlerTask implements Runnable{
        private String url;
        private String pageName;
        private String baseUrl;

        PageCrawlerTask(String url, String pageName, String baseUrl) {
            this.url = url;
            this.pageName = pageName;
            this.baseUrl = baseUrl;
        }

        @Override
        public void run() {
            //logger.error("Run Name ==> " + Thread.currentThread().getName());
            Page page = new Page(pageName, url, baseUrl);
            page.setVisited(true);
            if (!AppUtil.isCompleteUrl(url)) {
                url = url.substring(1);
                url = baseUrl + url;
            }
            Document doc = null;
            try {
                doc = Jsoup.connect(url).get();
                Elements links = doc.select("a[href]"); // a with href
                Set<Page> newPages = processLink(links);
                page.setChildrenSet(newPages);
                pages.add(page);
                } catch (IOException e) {
                logger.error("page skip ==> "+e.getMessage());
            }

            if(barrier.getNumberWaiting()==1){
                try {
                    barrier.await();
                } catch (BrokenBarrierException | InterruptedException e) {
                    logger.error("Service ==> " + e.getMessage());
                }

            }
        }
    }

    private Set<Page> processLink(Elements links) {
        return links.stream()
                .map(ele -> {
                    Page p = new Page(ele.text(), ele.attr("href"), ele.attr("title"));
                    if(AppUtil.shouldConsider(pages, p)){
                        queue.add(p);
                        return p;
                    }
                    return null;
         }).filter(ele -> ele != null).collect(Collectors.toSet());
    }


}
