package org.example.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.utility.MyElement;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Excel {
    private static final Logger logger = Logger.getLogger(Excel.class.getName());
    private final Workbook workbook;
    private int rowCount = 0;
    private int nameCount = 0;

    public Excel() {
        this.workbook = new XSSFWorkbook();
    }

    public Workbook getInstance() {
        return workbook;
    }

    public Sheet createSheet(List<MyElement> column) {
        Sheet sheet = workbook.createSheet("Page" + ++nameCount);
        Row header = sheet.createRow(rowCount);

        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        headerStyle.setFont(font);
        for (int i = 0; i < column.size(); i++) {
            Cell headerCell = header.createCell(i);
            headerCell.setCellValue(column.get(i).getProperty());
            headerCell.setCellStyle(headerStyle);
        }
        logger.info("Sheet created with name: " + sheet.getSheetName());
        return sheet;
    }

    public void writeData(Sheet sheet, List<String> value) {
        Row row = sheet.createRow(++rowCount);
        for (int i = 0; i < value.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(value.get(i));
        }
        logger.info("Data written to sheet: " + sheet.getSheetName());
    }

    public void writeExcel() {
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String fileLocation = path + File.separator + "temp.xlsx";

        try (FileOutputStream outputStream = new FileOutputStream(fileLocation)) {
            workbook.write(outputStream);
            logger.info("Excel file written successfully to: " + fileLocation);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error while writing to Excel file: " + e.getMessage(), e);
        } finally {
            try {
                workbook.close();
                logger.info("Workbook closed successfully.");
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error while closing workbook: " + e.getMessage(), e);
            }
        }
    }
}
