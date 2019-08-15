package excel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class WriteExcel {
    public void saveExcel(Map<String, String> carMap) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("car data");
        HSSFRow row = sheet.createRow(1);
        HSSFCell cell;
        Set<String> keys = carMap.keySet();
        int idx = 0;
        for (String key : keys) {
            row.createCell(idx).setCellValue(key);
            idx++;
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream("test.xls");
            workbook.write(fileOutputStream);
            fileOutputStream.close();
            System.out.println("엑셀 저장 성공");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
