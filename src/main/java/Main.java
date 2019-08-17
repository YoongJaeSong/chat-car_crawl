import crawling.Crawl;
import crawling.UrlVO;
import excel.WriteExcel;
import org.jsoup.select.Elements;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.bind.Element;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        Main.setSSL();

        String url = "https://www.bobaedream.co.kr/mycar/mycar_list.php?gubun=K&maker_no=49&buyYearChk=2019&page=1";
        List<Map<String, String>> dataList = new ArrayList<>();

        try {
            Crawl urlCrawling = new Crawl(url);
            String lastPage = urlCrawling.extractCSS("a.last").attr("href").replaceAll("[^0-9]", "");

            Elements data;
            UrlVO urlVO;
            Crawl crawl;
            for (int page = 1; page <= Integer.parseInt(lastPage); page++) {
                crawl = new Crawl("https://www.bobaedream.co.kr/mycar/mycar_list.php?gubun=K&maker_no=49&buyYearChk=2019&page=" + page);
                data = crawl.extractCSS("a.img");
                urlVO = new UrlVO();
                dataList.addAll(urlVO.findNextUrls(data));
            }

            WriteExcel work = new WriteExcel();
            work.saveExcel(dataList, "hyundai_2019.xls");
        } catch (IOException e) {
            e.printStackTrace();
        }


//        try {
//            for (String url : urlList) {
//                Crawl carPage = new Crawl(url);
//                carPage.connect();
//                Elements titleElements = carPage.extractCSS("div.select-finder");
//                Element mainElements = carPage.extractCSS("tbody").get(1);
//                ExcelVO carDTO = new ExcelVO();
//                carDTO.extractTitleData(titleElements);
//                carDTO.extractMainData(mainElements.select("tr"));
//
//                dataList.add(carDTO.getCarData(), "car_data.xls);
//            }
//
//            WriteExcel work = new WriteExcel();
//            work.saveExcel(dataList);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private static void setSSL() throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultHostnameVerifier(
                (hostname, session) -> true
        );
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }
}
