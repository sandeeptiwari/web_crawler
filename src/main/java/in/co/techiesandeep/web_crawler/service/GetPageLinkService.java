package in.co.techiesandeep.web_crawler.service;

import in.co.techiesandeep.web_crawler.model.Page;
import in.co.techiesandeep.web_crawler.utils.ApiUtil;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public interface GetPageLinkService{

    Set<Page> getPage() throws IOException, ExecutionException, InterruptedException;
}
