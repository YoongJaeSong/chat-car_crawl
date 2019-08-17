package excel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WriteExcel {
    public void saveExcel(List<Map<String, String>> carList, String fileName) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("car data");
        HSSFRow titleRow = sheet.createRow(0);
        HSSFCell cell;
        Set<String> keys = carList.get(0).keySet();

        int idx = 0;
        for (String key : keys) {
            titleRow.createCell(idx).setCellValue(key);
            idx++;
        }

        int rowLine = 1;
        for (Map<String, String> carData : carList) {
            HSSFRow valueRow = sheet.createRow(rowLine);
            idx = 0;
            for (String key : keys) {
                valueRow.createCell(idx).setCellValue(carData.get(key));
                idx++;
            }
            rowLine++;
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
            System.out.println("엑셀 저장 성공");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
