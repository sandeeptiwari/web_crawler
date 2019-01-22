package in.co.techiesandeep.web_crawler.service;

import in.co.techiesandeep.web_crawler.model.Page;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface GetPageLinkService{

    Map<String, List<Page>> getPage() throws IOException, ExecutionException, InterruptedException;
}
