import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class Crawlling {
    public static void main(String[] args) throws Exception {
        String url = "https://www.bobaedream.co.kr/dealguide/carinfo.php?cat=spec&maker_no=49&model_no=1661&level_no=12256&class_no=26460&year_no=2016";
        Crawlling.setSSL();

        Document doc = Jsoup.connect(url).get();

        Elements mainData = doc.select(".wrap-car-spec dd");
        for(Element data : mainData){
            Elements name = data.select("span.tit");
            Elements value = data.select("strong.txt");
            System.out.println("제목: " + name.text() + ", 내용: " + value.text().replaceAll("[^0-9]", ""));
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
