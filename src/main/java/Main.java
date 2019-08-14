import crawling.Crawl;
import crawling.ExcelDTO;
import org.jsoup.nodes.Element;

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
        String url = "https://www.bobaedream.co.kr/dealguide/carinfo.php?cat=spec&maker_no=49&model_no=1661&level_no=12256&class_no=26460&year_no=2016";
        Element element;

        Crawl carPage = new Crawl(url);

        try {
            carPage.connect();
            element = carPage.extractCSS("tbody").get(1);
            ExcelDTO carDTO = new ExcelDTO(element.select("tr"));
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
