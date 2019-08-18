package excel;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WriteExcel {
    private List<Map<String, String>> carList;
    private Set<String> keys;
    private String fileName;

    public WriteExcel(List<Map<String, String>> carList, String fileName) {
        this.carList = carList;
        this.fileName = fileName;
        keys = carList.get(0).keySet();
    }

    public void saveNewExcel() throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("car data");
        HSSFRow titleRow = sheet.createRow(0);

        int idx = 0;
        for (String key : keys) {
            titleRow.createCell(idx).setCellValue(key);
            idx++;
        }

        writeRows(sheet, 1);
        saveExcel(workbook);
    }

    public void saveExistExcel() throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(fileName));
        HSSFSheet sheet = workbook.getSheetAt(0);
        int lastRow = sheet.getPhysicalNumberOfRows();

        writeRows(sheet, lastRow);
        saveExcel(workbook);
    }

    private void writeRows(HSSFSheet sheet, int rowIdx) {
        HSSFRow row = null;
        int cellIdx;

        for (Map<String, String> carData : carList) {
            row = sheet.createRow(rowIdx);
            cellIdx = 0;
            for (String key : keys) {
                row.createCell(cellIdx).setCellValue(carData.get(key));
                cellIdx++;
            }
            rowIdx++;
        }
    }

    private void saveExcel(HSSFWorkbook workbook) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        System.out.println("엑셀 저장 성공");
    }
}
