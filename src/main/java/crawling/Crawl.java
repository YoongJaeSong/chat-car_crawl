package crawling;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Crawl {
    private Document document;

    public Crawl(String url) throws IOException {
        this.document = Jsoup.connect(url).get();
    }

    public Elements extractCSS(String css) {
        return document.select(css);
    }
}
