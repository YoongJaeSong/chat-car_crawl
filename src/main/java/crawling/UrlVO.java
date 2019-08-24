package crawling;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UrlVO {
    private List<Map<String, String>> urlList;
    private String markerNo;

    private static final String URL = "https://www.bobaedream.co.kr/dealguide/ajax_depth.php";
    private static final String BASE_URL = "https://www.bobaedream.co.kr";
    private static final String START_URL = "https://www.bobaedream.co.kr/dealguide/carinfo.php?";

    public UrlVO(String markerNo) {
        this.urlList = new ArrayList<>();
        this.markerNo = markerNo;
    }

    public void addUrl() throws IOException {
        String firstUrl = START_URL + "maker_no=" + markerNo;
        findModelNo(markerNo, firstUrl);
    }

    private void findModelNo(String makerNo, String firstUrl) throws IOException {
        Document doc = postRequest("model", makerNo);
        String modelNo;
        String second_url;

        for(Element item : doc.select("option")){
            modelNo = item.val();
            if(modelNo.equals("")){
                continue;
            }

            second_url = firstUrl + "&model_no=" + modelNo;
            findLevelNo(modelNo, second_url);
        }
    }

    private void findLevelNo(String modelNo, String secondUrl) throws IOException {
        Document doc = postRequest("level", modelNo);
        String thirdUrl;
        String levelNo;

        for(Element item : doc.select("option")){
            levelNo = item.val();
            if(levelNo.equals("")){
                continue;
            }

            thirdUrl = secondUrl + "&level_no=" + levelNo;
            findClassNo(levelNo, thirdUrl);
        }
    }

    private void findClassNo(String levelNo, String thirdUrl) throws IOException {
        Document doc = postRequest("class", levelNo);
        String classNo;
        String fourthUrl;

        for(Element item : doc.select("option")){
            classNo = item.val();
            if(classNo.equals("")){
                continue;
            }

            fourthUrl = thirdUrl + "&class_no=" + classNo;
            findYear(classNo, fourthUrl);
        }
    }

    private void findYear(String classNo, String fourthUrl) throws IOException {
        Document doc = postRequest("year", classNo);
        HashMap<String, String> urlMap = new HashMap<>();
        String year;
        String lastUrl;

        for(Element item : doc.select("option")){
            year = item.val();
            if(year.equals("") || (year.compareTo("2015") < 0)){
                continue;
            }

            lastUrl = fourthUrl + "&year_no=" +year;
            urlList.add(Map.of("url", lastUrl));
        }
    }

    public List<Map<String, String>> getUrlList() {
        return urlList;
    }

    private Document postRequest(String depth, String no) throws IOException {
        return Jsoup.connect(URL)
                .header("Origin", BASE_URL)
                .data("depth", depth)
                .data("no", no)
                .post();
    }
}
