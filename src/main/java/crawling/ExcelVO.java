package crawling;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ExcelVO {
    private Map<String, String> carData = new HashMap<>();

    public void extractUrlData(String url){
        String[] partOfUrl = url.split("\\?")[1].split("&");
        String key;
        String value;

        for (String subUrl : partOfUrl) {
            key = subUrl.split("=")[0];
            value = subUrl.split("=")[1];
            carData.put(key, value);
        }
    }

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
            if(element.hasClass("color")){
                saveColors(element.select("li"));
                continue;
            }

            Elements children = element.children();
            if (children.size() == 2) {
                carData.put(children.get(0).text(), children.get(1).text());
            } else {
                carData.put(children.get(1).text(), children.get(2).text());
            }
        }
    }

    private void saveColors(Elements colors){
        Map<String, String> colorMap = new HashMap<>();
        String key;
        String value;

        for(Element color : colors){
            key = color.select("span.name").text();
            value = color.select("span.color").first().attr("style").split(":")[1];
            colorMap.put(key, value);
        }

        carData.put("색상", String.join(",", colorMap.keySet()));
        carData.put("색상 표", String.join(",", colorMap.values()));
    }

    public Map<String, String> getCarData() {
        return carData;
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
