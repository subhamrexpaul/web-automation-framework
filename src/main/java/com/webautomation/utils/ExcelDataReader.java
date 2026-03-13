package com.webautomation.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ExcelDataReader - Utility class for reading test data from Excel (.xlsx) files.
 * Returns data in Object[][] format suitable for TestNG @DataProvider.
 */
public class ExcelDataReader {

    private static final Logger logger = LogManager.getLogger(ExcelDataReader.class);

    private ExcelDataReader() {
        // Prevent instantiation
    }

    /**
     * Reads all rows from a specified sheet in an Excel file.
     * First row is treated as header and excluded from the returned data.
     *
     * @param filePath  path to the .xlsx file
     * @param sheetName name of the sheet to read
     * @return Object[][] containing test data (excluding header row)
     */
    public static Object[][] readTestData(String filePath, String sheetName) {
        logger.info("Reading test data from: {} (Sheet: {})", filePath, sheetName);

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                logger.error("Sheet '{}' not found in {}", sheetName, filePath);
                throw new RuntimeException("Sheet '" + sheetName + "' not found in " + filePath);
            }

            int totalRows = sheet.getPhysicalNumberOfRows();
            int totalCols = sheet.getRow(0).getPhysicalNumberOfCells();

            logger.info("Found {} data rows and {} columns", totalRows - 1, totalCols);

            // Exclude header row
            Object[][] data = new Object[totalRows - 1][totalCols];

            for (int i = 1; i < totalRows; i++) {
                Row row = sheet.getRow(i);
                for (int j = 0; j < totalCols; j++) {
                    Cell cell = row.getCell(j);
                    data[i - 1][j] = getCellValue(cell);
                }
            }

            logger.info("Test data loaded successfully: {} records", data.length);
            return data;

        } catch (IOException e) {
            logger.error("Failed to read Excel file '{}': {}", filePath, e.getMessage());
            throw new RuntimeException("Failed to read Excel file: " + filePath, e);
        }
    }

    /**
     * Reads data from a specific column range.
     *
     * @param filePath  path to the .xlsx file
     * @param sheetName name of the sheet to read
     * @param startCol  starting column index (0-based)
     * @param endCol    ending column index (0-based, inclusive)
     * @return Object[][] containing test data for specified columns
     */
    public static Object[][] readTestData(String filePath, String sheetName, int startCol, int endCol) {
        logger.info("Reading columns {}-{} from: {} (Sheet: {})", startCol, endCol, filePath, sheetName);

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet '" + sheetName + "' not found in " + filePath);
            }

            int totalRows = sheet.getPhysicalNumberOfRows();
            int colCount = endCol - startCol + 1;

            Object[][] data = new Object[totalRows - 1][colCount];

            for (int i = 1; i < totalRows; i++) {
                Row row = sheet.getRow(i);
                for (int j = startCol; j <= endCol; j++) {
                    Cell cell = row.getCell(j);
                    data[i - 1][j - startCol] = getCellValue(cell);
                }
            }

            return data;

        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file: " + filePath, e);
        }
    }

    /**
     * Returns a list of all sheet names in the workbook.
     */
    public static List<String> getSheetNames(String filePath) {
        List<String> sheetNames = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                sheetNames.add(workbook.getSheetName(i));
            }
        } catch (IOException e) {
            logger.error("Failed to read sheet names from '{}': {}", filePath, e.getMessage());
        }
        return sheetNames;
    }

    /**
     * Extracts the value from a cell, handling different cell types.
     */
    private static Object getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                double numVal = cell.getNumericCellValue();
                // Return as int if no decimal part
                if (numVal == Math.floor(numVal) && !Double.isInfinite(numVal)) {
                    return String.valueOf((int) numVal);
                }
                return String.valueOf(numVal);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }
}
