package crawling;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UrlVO {
    private List<Map<String , String>> urlMapList;

    private static final String BASE_URL = "https://www.bobaedream.co.kr";
    private static final String HREF = "href";

    public UrlVO() {
        this.urlMapList = new ArrayList<>();
    }

    public List<Map<String, String>> findNextUrls(Elements elements) throws IOException {
        for(Element data : elements){
            String nextUrl = BASE_URL + data.attr(HREF);
            Crawl nextPage = new Crawl(nextUrl);
            Map<String, String> obj = new HashMap<>();

            obj.put("URL", BASE_URL + nextPage.extractCSS("a.btn-more-round").attr(HREF));
            urlMapList.add(obj);
        }

        return urlMapList;
    }
}
