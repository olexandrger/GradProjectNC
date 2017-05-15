package com.grad.project.nc.service.reports;


import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.RichTextString;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class XlsWorkbook {

    private HSSFWorkbook workbook;
    private Map<String, HSSFSheet> sheets;

    private HSSFSheet currentSheet;

    public XlsWorkbook() {
        workbook = new HSSFWorkbook();
        sheets = new HashMap<>();
    }

    public void setCurrentSheet(String name) {
        if (!sheets.containsKey(name)) {
            sheets.put(name, workbook.createSheet(name));
        }
        currentSheet = sheets.get(name);
    }

    private Cell getCell(int x, int y) {
        if (currentSheet == null) {
            throw new IllegalStateException("You must select sheet first");
        }

        if (currentSheet.getRow(x) == null) {
            currentSheet.createRow(x);
        }

        if (currentSheet.getRow(x).getCell(y) == null) {
            currentSheet.getRow(x).createCell(y);
        }

        return currentSheet.getRow(x).getCell(y);
    }

    public void writeCell(int x, int y, String value) {
        getCell(x, y).setCellValue(value);
    }

    private void resizeColumns() {
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            HSSFSheet sheet = workbook.getSheetAt(i);

            int columnCount = 0;
            for (int rowIndex = 0; rowIndex < sheet.getLastRowNum(); rowIndex++) {
                columnCount = Math.max(columnCount, sheet.getRow(rowIndex).getLastCellNum());
            }

            for (int colIndex = 0; colIndex < columnCount; colIndex++) {
                sheet.autoSizeColumn(colIndex);
            }
        }
    }

    public void writeToOutputStream(OutputStream stream) throws IOException {
        resizeColumns();
        workbook.write(stream);
    }
}
