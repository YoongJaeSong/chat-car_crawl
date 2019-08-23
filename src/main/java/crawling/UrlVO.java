package crawling;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UrlVO {
    private String url;

    private static final String BASE_URL = "https://www.bobaedream.co.kr";

    public UrlVO(String url) {
        this.url = url;
    }

    private Map<String, String> findLoop(Elements items){
        Map<String, String> map = new HashMap<>();
        String key;
        String value;

        for(Element item : items){
            if(item.val().equals("")){
                continue;
            }

            key = StringEscapeUtils.unescapeJava(item.val());
            value = StringEscapeUtils.unescapeJava(item.text().split("<")[0]);
            System.out.println(key + " / " + value);
            map.put(key, value);
        }

        return map;
    }

    public void findModelNo(int makerNo) throws IOException {
        Document doc = postRequest("model", Integer.toString(makerNo));
        Map<String, String> modelMap = findLoop(doc.select("option"));
        findLevelNo(modelMap);
    }

    public void findLevelNo(Map<String, String> modelMap) throws IOException {
        Document doc;
        Map<String, String> levelMap = new HashMap<>();
        for(String key : modelMap.keySet()){
            doc = postRequest("level", key);
            levelMap.putAll(findLoop(doc.select("option")));
        }
    }

    private Document postRequest(String depth, String no) throws IOException {
        return Jsoup.connect(url)
                .header("Origin", BASE_URL)
                .data("depth", depth)
                .data("no", no)
                .post();
    }
}
