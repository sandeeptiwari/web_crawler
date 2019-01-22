package in.co.techiesandeep.web_crawler.model;

import java.util.Objects;
import java.util.Set;

public class Page {
    private String pageName;
    private String pageLink;
    private boolean isVisited;
    private String baseUrl;
    private Set<Page> childrenSet;


    public Page(String pageName, String pageLink, String baseUrl) {
        this.pageName = pageName;
        this.pageLink = pageLink;
        this.baseUrl = baseUrl;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getPageLink() {
        return pageLink;
    }

    public void setPageLink(String pageLink) {
        this.pageLink = pageLink;
    }

    public Set<Page> getChildrenSet() {
        return childrenSet;
    }

    public void setChildrenSet(Set<Page> childrenSet) {
        this.childrenSet = childrenSet;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Page page = (Page) o;
        return pageName.equals(page.pageName) &&
                pageLink.equals(page.pageLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageName, pageLink);
    }
}
