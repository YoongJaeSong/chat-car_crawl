package crawling;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ExcelDTO {
    private Map<String, String> carData = new HashMap<>();

    public void extractTitleData(Elements elements) {
        String key = null;
        for (Element element : elements.select("option")) {
            if (element.val().equals("")) {
                key = element.text();
            }

            if (element.hasAttr("selected")) {
                carData.put(key, element.text());
            }
        }
    }

    public void extractMainData(Elements elements) {
        for (Element element : elements) {
            Elements children = element.children();
            if (children.size() == 2) {
                carData.put(children.get(0).text(), children.get(1).text());
            } else {
                carData.put(children.get(1).text(), children.get(2).text());
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        Set<String> keys = carData.keySet();
        for (String key : keys) {
            String temp = "Key: " + key + ", Value: " + carData.get(key) + "\n";
            result.append(temp);
        }

        return result.toString();
    }
}
