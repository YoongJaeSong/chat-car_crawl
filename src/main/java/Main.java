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
import java.util.*;

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
            Queue<String> urlQueue = new ArrayDeque<>();

            for(int rowidx = 1; rowidx < sheet.getPhysicalNumberOfRows(); rowidx++){
                urlQueue.offer(sheet.getRow(rowidx).getCell(0).getStringCellValue());
            }

            Crawl curPage;
            Elements titleItems;
            Element mainItem;
            ExcelVO carData;
            String curUrl;
            while(!urlQueue.isEmpty()){
                curUrl = urlQueue.poll();
                try {
                    curPage = new Crawl(curUrl);
                } catch (IOException e) {
                    System.out.println(curUrl);
                    urlQueue.offer(curUrl);
                    continue;
                }
                titleItems = curPage.extractCSS("div.select-finder");
                mainItem = curPage.extractCSS("tbody").get(1);
                carData = new ExcelVO();

                carData.extractUrlData(curUrl);
                carData.extractTitleData(titleItems);
                carData.extractMainData(mainItem.select("tr"));
                dataList.add(carData.getCarData());
            }

            WriteExcel work = new WriteExcel(dataList, "new_car_data.xls");
            File file = new File("new_car_data.xls");
            if (file.exists()) {
                work.saveExistExcel();
            } else {
                work.saveNewExcel();
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
