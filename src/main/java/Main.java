import crawling.Crawl;
import crawling.ExcelDTO;
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

public class Main {
    public static void main(String[] args) throws Exception {
        Main.setSSL();
        String url = "https://www.bobaedream.co.kr/dealguide/carinfo.php?maker_no=49&model_no=1779&level_no=12888&class_no=27921&year_no=2018";

        Crawl carPage = new Crawl(url);

        try {
            carPage.connect();
            Elements titleElements = carPage.extractCSS("div.select-finder");
            Element mainElements = carPage.extractCSS("tbody").get(1);
            ExcelDTO carDTO = new ExcelDTO();
            carDTO.extractTitleData(titleElements);
            carDTO.extractMainData(mainElements.select("tr"));
            System.out.println(carDTO.toString());
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
