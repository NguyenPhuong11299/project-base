package com.automation.helpers;

import com.automation.exceptions.InvalidPathForExcelException;
import com.automation.model.excel.InputCellModel;
import com.automation.utils.ParamUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.scheduling.annotation.Async;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Log4j2
public class ExcelHelpers {
    private FileInputStream fis;
    private FileOutputStream fileOut;
    private Workbook workbook;
    private Sheet sheet;
    private Cell cell;
    private Row row;
    private String excelFilePath;
    private Map<String, Integer> columns = new HashMap<>();//adding all the column header names to the map 'columns'

    //Set Excel File
    public void setExcelFile(String excelPath, String sheetName) {
        log.info("Set Excel File: " + excelPath);
        log.info("Sheet Name: " + sheetName);

        try {
            File f = new File(excelPath);

            if (!f.exists()) {
                try {
                    log.info("File Excel path not found.");
                    throw new InvalidPathForExcelException("File Excel path not found.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (sheetName.isEmpty()) {
                try {
                    log.info("The Sheet Name is empty.");
                    throw new InvalidPathForExcelException("The Sheet Name is empty.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            fis = new FileInputStream(excelPath);
            workbook = WorkbookFactory.create(fis);
            sheet = workbook.getSheet(sheetName);
            //sh = wb.getSheetAt(0); //0 - index of 1st sheet
            if (sheet == null) {
                //sh = wb.createSheet(sheetName);
                try {
                    log.info("Sheet name not found.");
                    throw new InvalidPathForExcelException("Sheet name not found.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            excelFilePath = excelPath;

            //adding all the column header names to the map 'columns'
            sheet.getRow(0).forEach(cell -> {
                columns.put(cell.getStringCellValue(), cell.getColumnIndex());
            });

        } catch (Exception e) {
            e.getMessage();
            log.error(e.getMessage());
        }
    }

    public void setCellData(String text, int rowNumber, String columnName) {
        try {
            row = sheet.getRow(rowNumber);
            if (row == null) {
                row = sheet.createRow(rowNumber);
            }
            cell = row.getCell(columns.get(columnName));

            if (cell == null) {
                cell = row.createCell(columns.get(columnName));
            }
            cell.setCellValue(text);

            XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
            text = text.trim().toLowerCase();
            if (text == "pass" || text == "passed" || text == "success") {
                style.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
            }
            if (text == "fail" || text == "failed" || text == "failure") {
                style.setFillForegroundColor(IndexedColors.RED.getIndex());
            }

            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);

            cell.setCellStyle(style);

            fileOut = new FileOutputStream(excelFilePath);
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (Exception e) {
            e.getMessage();
            log.error(e.getMessage());
        }
    }

    @Async
    public void setCellDataImpl(InputCellModel inputCellModel) {
        try {
            openFile(inputCellModel.getExcelPath(), inputCellModel.getSheetName());

            row = sheet.getRow(inputCellModel.getRowNumber());
            if (row == null) {
                row = sheet.createRow(inputCellModel.getRowNumber());
            }
            cell = row.getCell(inputCellModel.getCellNumber());

            if (cell == null) {
                cell = row.createCell(inputCellModel.getCellNumber());
            }
            String text = inputCellModel.getText();
            cell.setCellValue(text);

            XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
            text = text.trim().toLowerCase();
            if (text == "pass" || text == "passed" || text == "success") {
                style.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());

                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                style.setAlignment(HorizontalAlignment.CENTER);
                style.setVerticalAlignment(VerticalAlignment.CENTER);
            }
            if (text == "fail" || text == "failed" || text == "failure") {
                style.setFillForegroundColor(IndexedColors.RED.getIndex());

                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                style.setAlignment(HorizontalAlignment.CENTER);
                style.setVerticalAlignment(VerticalAlignment.CENTER);
            }

//                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//                style.setAlignment(HorizontalAlignment.CENTER);
//                style.setVerticalAlignment(VerticalAlignment.CENTER);

            cell.setCellStyle(style);

            excelFilePath = inputCellModel.getExcelPath();
            fileOut = new FileOutputStream(excelFilePath);//k dc mo file khi ghi file
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();

            System.out.println("================ setCellDataImpl =========================");
        } catch (Exception e) {
            e.getMessage();
            log.error(e.getMessage());
        }
    }

    private void openFile(String excelPath, String sheetName) {
        log.info("Excel File: " + excelPath);
        log.info("Sheet Name: " + sheetName);

        try {

            File f = new File(excelPath);

            if (!f.exists()) {
                try {
                    log.info("File Excel path not found.");
                    throw new InvalidPathForExcelException("File Excel path not found.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            fis = new FileInputStream(excelPath);
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheet(sheetName);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    //Hashtable không cho phép bất kỳ key hoặc value null.
    public List<Hashtable<String, String>> getAllDataInSheet(String excelPath, String sheetName, String numberFormat) {
        log.info("Excel File: " + excelPath);
        log.info("Sheet Name: " + sheetName);

        List<Hashtable<String, String>> data = new ArrayList<>();

        try {

            File f = new File(excelPath);

            if (!f.exists()) {
                try {
                    log.info("File Excel path not found.");
                    throw new InvalidPathForExcelException("File Excel path not found.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            fis = new FileInputStream(excelPath);
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheet(sheetName);

            int rows = getRows();
            int columns = getColumns();

            log.info("Row: " + rows + " - Column: " + columns);

            for (int rowNums = 1; rowNums <= rows; rowNums++) {
                Hashtable<String, String> table = new Hashtable<>();
                for (int colNum = 0; colNum < columns; colNum++) {
                    if(ParamUtils.FLOAT.equals(numberFormat)) {
//                        log.info("rowNums = " + rowNums + " ; colNum = " + colNum);
                        table.put(getCellData(0, colNum), getCellDataImpl(rowNums, colNum));
                    }else if(ParamUtils.LONG.equals(numberFormat)){
                        table.put(getCellData(0, colNum), getCellData(rowNums, colNum));
                    }
                }
                data.add(table);
            }

        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        return data;
    }

    public Object[][] getDataHashTableAllRow(String excelPath, String sheetName) {
        log.info("Excel File: " + excelPath);
        log.info("Sheet Name: " + sheetName);

        Object[][] data = null;

        try {

            File f = new File(excelPath);

            if (!f.exists()) {
                try {
                    log.info("File Excel path not found.");
                    throw new InvalidPathForExcelException("File Excel path not found.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            fis = new FileInputStream(excelPath);
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheet(sheetName);

            int rows = getRows();
            int columns = getColumns() - 1;//k lay cot Actual, value cua cot Actual = null -> exception java.lang.NoSuchMethodError

            log.info("Row: " + rows + " - Column: " + columns);

            data = new Object[rows][1];
            Hashtable<String, String> table = null;

            //tim vi tri cua cell co columnName = "Actual"
            int numCellActual = columns + 1;
            for (Cell cell : sheet.getRow(0)) {
                if (ParamUtils.ACTUAL.equalsIgnoreCase(cell.getStringCellValue())) {
                    numCellActual = cell.getColumnIndex();
                    break;
                }
            }

//            Hashtable<String, String> table = null;
            for (int rowNums = 1; rowNums <= rows; rowNums++) {
                table = new Hashtable<>();
                for (int colNum = 0; colNum < columns; colNum++) {
                    table.put(getCellData(0, colNum), getCellData(rowNums, colNum));

                }

                //them thong tin row vao table de lam input cho ham set data vao file excel
                table.put(ParamUtils.ROW_NUM, String.valueOf(rowNums));
                table.put(ParamUtils.EXCELPATH, excelPath);
                table.put(ParamUtils.SHEETNAME, sheetName);
                table.put(ParamUtils.CELL_NUM, String.valueOf(numCellActual));

                data[rowNums - 1][0] = table;
            }

        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        return data;

    }

    //nhóm các object cùng value cột đầu tiên vào chung 1 list
    public Object[][] getDataHashTableAllRowImpl(String excelPath, String sheetName) {
        log.info("Excel File: " + excelPath);
        log.info("Sheet Name: " + sheetName);

        Object[][] data = null;

        try {

            File f = new File(excelPath);

            if (!f.exists()) {
                try {
                    log.info("File Excel path not found.");
                    throw new InvalidPathForExcelException("File Excel path not found.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            fis = new FileInputStream(excelPath);
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheet(sheetName);

            int rows = getRows();
            int columns = getColumns() - 1;//k lay cot Actual, value cua cot Actual = null -> exception java.lang.NoSuchMethodError

            log.info("Row: " + rows + " - Column: " + columns);

//            data = new Object[rows][1];
            Hashtable<String, String> table = null;

            //tim vi tri cua cell co columnName = "Actual"
            int numCellActual = columns + 1;
            for (Cell cell : sheet.getRow(0)) {
                if (ParamUtils.ACTUAL.equalsIgnoreCase(cell.getStringCellValue())) {
                    numCellActual = cell.getColumnIndex();
                    break;
                }
            }

            //key: value của ô đầu tiên trong dòng ; value: list các dòng có cùng "value của ô đầu tiên trong dòng"
            Hashtable<String, List> tableSameValueOfFirstColumn = new Hashtable<>();
//            String valueOfFirstCell = "";
            for (int rowNums = 1; rowNums <= rows; rowNums++) {
                //table tuong ung vs 1 dong trong file
                table = new Hashtable<>();

                String valueOfFirstCellInRow = getCellData(rowNums,0);
                //nếu chưa có key = valueOfFirstCellInRow -> thêm 1 key-value mới vào tableSameValueOfFirstColumn
                if(!tableSameValueOfFirstColumn.containsKey(valueOfFirstCellInRow)){
                    //Lớp LinkedList duy trì thứ tự của phần tử được thêm vào
                    List listRowHaveSameValueOfFirstCell = new LinkedList();
                    listRowHaveSameValueOfFirstCell.add(table);

                    tableSameValueOfFirstColumn.put(valueOfFirstCellInRow, listRowHaveSameValueOfFirstCell);

                }else {
                    //nếu key đã tồn tại thì thêm table vào list value của tableSameValueOfFirstColumn
                    tableSameValueOfFirstColumn.get(valueOfFirstCellInRow).add(table);
                }

                for (int colNum = 0; colNum < columns; colNum++) {
                    table.put(getCellData(0, colNum), getCellData(rowNums, colNum));

                }

                //them thong tin row vao table de lam input cho ham set data vao file excel
                table.put(ParamUtils.ROW_NUM, String.valueOf(rowNums));
                table.put(ParamUtils.EXCELPATH, excelPath);
                table.put(ParamUtils.SHEETNAME, sheetName);
                table.put(ParamUtils.CELL_NUM, String.valueOf(numCellActual));

//                data[rowNums - 1][0] = table;
            }

            //duyệt tableSameValueOfFirstColumn, mỗi cặp key-value tương ứng vs 1 testcase
            Set<String> listKeyOfTable = tableSameValueOfFirstColumn.keySet();
            int count = 0;
            data = new Object[tableSameValueOfFirstColumn.size()][1];
            for(String key : listKeyOfTable){
                Hashtable<String, List> testCase = new Hashtable<>();
                testCase.put(key, tableSameValueOfFirstColumn.get(key));
                data[count][0] = testCase;
                count++;
            }

        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        return data;

    }

    public Object[][] getDataHashTable(String excelPath, String sheetName, int startRow, int endRow) {
        log.info("Excel File: " + excelPath);
        log.info("Sheet Name: " + sheetName);

        Object[][] data = null;

        try {

            File f = new File(excelPath);

            if (!f.exists()) {
                try {
                    log.info("File Excel path not found.");
                    throw new InvalidPathForExcelException("File Excel path not found.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            fis = new FileInputStream(excelPath);
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheet(sheetName);

            int rows = getRows();
            int columns = getColumns();

            log.info("Row: " + rows + " - Column: " + columns);
            log.info("StartRow: " + startRow + " - EndRow: " + endRow);

            data = new Object[(endRow - startRow) + 1][1];
            Hashtable<String, String> table = null;
            for (int rowNums = startRow; rowNums <= endRow; rowNums++) {
                table = new Hashtable<>();
                for (int colNum = 0; colNum < columns; colNum++) {
                    table.put(getCellData(0, colNum), getCellData(rowNums, colNum));
                }
                data[rowNums - startRow][0] = table;
            }

        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        return data;

    }

    public int getRows() {
        try {
            return sheet.getLastRowNum();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw (e);
        }
    }

    public int getColumns() {
        try {
            row = sheet.getRow(0);
            return row.getLastCellNum();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw (e);
        }
    }

    // Get cell data
    public String getCellData(int rowNum, int colNum) {
        try {
            cell = sheet.getRow(rowNum).getCell(colNum);
            String CellData = null;
            switch (cell.getCellType()) {
                case STRING:
                    CellData = cell.getStringCellValue();
                    break;
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        CellData = String.valueOf(cell.getDateCellValue());
                    } else {
                        CellData = String.valueOf((long) cell.getNumericCellValue());
                    }
                    break;
                case BOOLEAN:
                    CellData = Boolean.toString(cell.getBooleanCellValue());
                    break;
                case BLANK:
                    CellData = "";
                    break;
            }
            return CellData;
        } catch (Exception e) {
            return "";
        }
    }

    // Get cell data // number dang float
    public String getCellDataImpl(int rowNum, int colNum) {
        try {
            cell = sheet.getRow(rowNum).getCell(colNum);
            String CellData = null;
            switch (cell.getCellType()) { //nếu là FORMULA: công thức -> thì copy -> paste value của ô đó, cho mất công thức
                case STRING:
                    CellData = cell.getStringCellValue();
                    break;
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        CellData = String.valueOf(cell.getDateCellValue());
                    } else {
                        CellData = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                case BOOLEAN:
                    CellData = Boolean.toString(cell.getBooleanCellValue());
                    break;
                case BLANK:
                    CellData = "";
                    break;
            }
            return CellData;
        } catch (Exception e) {
            return "";
        }
    }

    public String getCellData(int rowNum, String columnName) {
        return getCellData(rowNum, columns.get(columnName));
    }

    public void writeResultIntoFile(Hashtable<String, String> excelData, SoftAssert softassert) {
//        System.out.println("================== writeResultIntoFile =======================");
        String text = null;
        InputCellModel inputCellModel = new InputCellModel();
        ExcelHelpers excelHelpers = new ExcelHelpers();
        try {
            softassert.assertAll(); // xem ket qua, neu k co, test se luon pass, tester se k biet case nao pass, case nao fail

            text = ParamUtils.PASS;
//            System.out.println("=========== compareValue 2 ================");
            inputCellModel.setInputCell(text, excelData, ParamUtils.ACTUAL);
            excelHelpers.setCellDataImpl(inputCellModel);

//            System.out.println("+++++++++++++++ pass ++++++++++++++");
        } catch (AssertionError error) {

//            System.out.println("=========== compareValue 3 ================");
            text = error.getMessage();
            inputCellModel.setInputCell(text, excelData, ParamUtils.ACTUAL);
            excelHelpers.setCellDataImpl(inputCellModel);

//            System.out.println("error+++++++++++++++++ " + error.getMessage());
            throw error;//k throw ra exception thi testng se hieu la testcase da pass
        }
    }

    public void writeResultIntoFileImpl(Hashtable<String, String> excelData, String text) {
        InputCellModel inputCellModel = new InputCellModel();
        inputCellModel.setInputCell(text, excelData, ParamUtils.ACTUAL);
        setCellDataImpl(inputCellModel);
    }
}
