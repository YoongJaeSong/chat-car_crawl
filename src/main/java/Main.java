import crawling.Crawl;
import crawling.ExcelVO;
import crawling.UrlVO;
import excel.WriteExcel;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        Main.setSSL();
        List<Map<String, String>> dataList = new ArrayList<>();

        if (args.length == 2) {
            String makerNo = args[0];
            String fileName = args[1];

            UrlVO urlVo = new UrlVO(makerNo);

            try {
                urlVo.addUrl();
                List<Map<String, String>> urlList = urlVo.getUrlList();

                WriteExcel work = new WriteExcel(urlList, fileName);
                File file = new File(fileName);
                if (file.exists()) {
                    work.saveExistExcel();
                } else {
                    work.saveNewExcel();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (args.length == 1) {
            String readFileName = args[0];
            FileInputStream fis = new FileInputStream(readFileName);
            HSSFWorkbook workbook = new HSSFWorkbook(fis);
            HSSFSheet sheet = workbook.getSheetAt(0);

            try {
                for (int rowidx = 1; rowidx < sheet.getPhysicalNumberOfRows(); rowidx++) {
                    System.out.println(rowidx);
                    String url = sheet.getRow(rowidx).getCell(0).getStringCellValue();
                    Crawl carPage = new Crawl(url);
                    Elements titleElements = carPage.extractCSS("div.select-finder");
                    Element mainElements = carPage.extractCSS("tbody").get(1);
                    ExcelVO carDTO = new ExcelVO();
                    carDTO.extractUrlData(url);
                    carDTO.extractTitleData(titleElements);
                    carDTO.extractMainData(mainElements.select("tr"));

                    dataList.add(carDTO.getCarData());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                WriteExcel work = new WriteExcel(dataList, "new_car_data.xls");
                File file = new File("new_car_data.xls");
                if (file.exists()) {
                    work.saveExistExcel();
                } else {
                    work.saveNewExcel();
                }
            }
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
