import crawling.Crawl;
import crawling.UrlVO;
import excel.WriteExcel;
import org.jsoup.select.Elements;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
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

        String baseUrl = "https://www.bobaedream.co.kr";
        String url = "https://www.bobaedream.co.kr/mycar/mycar_list.php?gubun=K&maker_no=49&buyYearChk=2016&page=1&order=S11&view_size=20";
        List<Map<String, String>> dataList = new ArrayList<>();

        try {
            Crawl urlCrawling = new Crawl(url);
            Elements data = urlCrawling.extractCSS("a.img");
            UrlVO urlVO = new UrlVO();
            WriteExcel work = new WriteExcel();
            work.saveExcel(urlVO.findNextUrls(data), "next_url.xls");
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
