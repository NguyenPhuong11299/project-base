package com.automation.helpers;

import com.automation.utils.ParamUtils;
import lombok.extern.log4j.Log4j2;
import org.testng.asserts.SoftAssert;

import java.io.FileWriter;

@Log4j2
public class TxtHelpers {
    public void write(String pathFile, String text){
        try {
            FileWriter fw = new FileWriter(pathFile);
            fw.write(text);
            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void writeResultIntoFile(String pathFile, SoftAssert softassert) {
//        System.out.println("================== writeResultIntoFile =======================");
        String text = null;
        try {
            softassert.assertAll(); // xem ket qua, neu k co, test se luon pass, tester se k biet case nao pass, case nao fail

            text = ParamUtils.PASS;
            write(pathFile, text);

//            System.out.println("+++++++++++++++ pass ++++++++++++++");
        } catch (AssertionError error) {

//            System.out.println("=========== compareValue 3 ================");
            text = error.getMessage();
            write(pathFile, text);

//            System.out.println("error+++++++++++++++++ " + error.getMessage());
            throw error;//k throw ra exception thi testng se hieu la testcase da pass
        }
    }
}
