import crawling.Crawl;
import crawling.ExcelDTO;
import excel.WriteExcel;
import org.jsoup.nodes.Element;
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
//        String url = "https://www.bobaedream.co.kr/dealguide/carinfo.php?maker_no=49&model_no=1779&level_no=12888&class_no=27921&year_no=2018";
//        List<String> urlList = new ArrayList<>();
        String[] urlList = {"https://www.bobaedream.co.kr/dealguide/carinfo.php?cat=spec&maker_no=49&model_no=1646&level_no=12181&class_no=26263&year_no=2016",
                "https://www.bobaedream.co.kr/dealguide/carinfo.php?maker_no=49&model_no=1779&level_no=12888&class_no=27921&year_no=2018"};
        List<Map<String, String>> dataList = new ArrayList<>();

        try {
            for (String url : urlList) {
                Crawl carPage = new Crawl(url);
                carPage.connect();
                Elements titleElements = carPage.extractCSS("div.select-finder");
                Element mainElements = carPage.extractCSS("tbody").get(1);
                ExcelDTO carDTO = new ExcelDTO();
                carDTO.extractTitleData(titleElements);
                carDTO.extractMainData(mainElements.select("tr"));

                dataList.add(carDTO.getCarData());
            }

            WriteExcel work = new WriteExcel();
            work.saveExcel(dataList);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
