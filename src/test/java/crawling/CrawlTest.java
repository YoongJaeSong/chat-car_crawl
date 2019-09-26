package crawling;

import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CrawlTest {

    @Test
    public void 사이트연결_확인() {
        String url = "https://www.naver.com/";
        Crawl crawl = new Crawl(url);
        try {
            crawl.connect();
            Elements elements = crawl.extractCSS("a.al_favorite");
            assertThat(elements.text(), is("네이버를 시작페이지로"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}