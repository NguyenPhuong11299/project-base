package com.automation.model.entity;

import com.automation.helpers.ExcelHelpers;
import com.automation.utils.DBParams;
import com.automation.utils.ParamUtils;
import lombok.Data;
import org.testng.asserts.SoftAssert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Hashtable;

@Entity(name = DBParams.TB_BASE)
@Data
public class Base {
    @Id
    @Column(name = DBParams.S_ID)
    private String id;

    @Column(name = DBParams.S_NAME)
    private String name;

    @Column(name = DBParams.S_STATE)
    private String state;

    @Column(name = DBParams.RESOLUTION)
    private String resolutionRequire;

    @Column(name = DBParams.CLONE_PRICE)
    private String clonePrice;

    public void compareValue(Hashtable<String, String> excelData) {
//        System.out.println("++++++++++++++++++ compareValue +++++++++++++++++");
        SoftAssert softassert = new SoftAssert();//khang dinh mem
        softassert.assertEquals(this.resolutionRequire, excelData.get(ParamUtils.DEMENSION), ParamUtils.DEMENSION); //, "The " + ParamUtils.DEMENSION + " in sheet " + nameSheet + " is not the same as in the database");
        softassert.assertEquals(this.clonePrice, excelData.get(ParamUtils.DESIGN_FEE), ParamUtils.DESIGN_FEE); // "The " + ParamUtils.DESIGN_FEE + " in sheet " + nameSheet + " is not the same as in the database");

        ExcelHelpers excelHelpers = new ExcelHelpers();
        excelHelpers.writeResultIntoFile(excelData, softassert);
    }
}
