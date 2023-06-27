package com.automation.test.dataProvider;

import com.automation.constants.FrameworkConstants;
import com.automation.helpers.ExcelHelpers;
import com.automation.helpers.Helpers;
import com.automation.utils.ParamUtils;
import org.testng.annotations.DataProvider;

public class DataProviderManager {

    //@DataProvider works at testmethod level and @Factory works at class level
    //source: https://stackoverflow.com/questions/57345545/how-to-execute-tests-sequentially-using-testng-and-dataprovider

    @DataProvider(name = "getDataCreateProduct", parallel = true)
    public Object[][] getDataCreateProduct(){
        ExcelHelpers excelHelpers = new ExcelHelpers();
        Object[][] data = excelHelpers.getDataHashTableAllRow(Helpers.getCurrentDir() + FrameworkConstants.INFO_BASE_EXCEL_PATH, ParamUtils.ADD_PRODUCT);
        return data;
    }

}
