package crawling;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ExcelDTO {
    private Elements elements;
    private Map<String, String> carData = new HashMap<>();

    public ExcelDTO(Elements elements) {
        this.elements = elements;
        extractData();
    }

    private void extractData() {
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
