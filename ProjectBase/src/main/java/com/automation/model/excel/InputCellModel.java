package com.automation.model.excel;

import com.automation.utils.ParamUtils;
import lombok.Data;

import java.util.Hashtable;

@Data
public class InputCellModel {
    private String text;
    private int rowNumber;
    private String columnName;
    private String excelPath;
    private String sheetName;
    private int cellNumber;

    public void setInputCell(String text, Hashtable<String, String> excelData, String columnName){
        System.out.println("===================== setInputCell =====================");
        this.text = text;
        this.rowNumber = Integer.parseInt(excelData.get(ParamUtils.ROW_NUM));
        this.columnName = columnName;
        this.excelPath = excelData.get(ParamUtils.EXCELPATH);
        this.sheetName = excelData.get(ParamUtils.SHEETNAME);
        this.cellNumber = Integer.parseInt(excelData.get(ParamUtils.CELL_NUM));

    }
}
