package com.automation.test.seller.testcases.database;

import com.automation.TestNGApplication;
import com.automation.service.BaseService;
import com.automation.test.dataProvider.DataProviderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import java.util.Hashtable;

@SpringBootTest (classes = TestNGApplication.class)
public class InfoBaseTest extends AbstractTestNGSpringContextTests{
    private Hashtable<String, String> data;
        @Autowired
    private BaseService baseService;

    @Test
    public void infoBaseTest(){// thay đổi dữ liệu trong file "TestPriceBase.xlsx", sheet : "Resolution"
//        System.out.println("================================== infoBaseTest +++++++++++++++++++");
        baseService.compareValueInSheetResolution(data);
    }

    //sour: https://howtodoinjava.com/testng/testng-factory-annotation-tutorial/
    @Factory(dataProvider = "getDataHashTableAllRow", dataProviderClass = DataProviderManager.class)
    public InfoBaseTest(Hashtable<String, String> data) {
        this.data = data;
    }

    //cach nay k ghi dc ket qua test ra file excel cua tung case
//    @Test(priority = 1, description = "Step 1: test data in sheet resolution", dataProvider = "getDataHashTableAllRow", dataProviderClass = DataProviderManager.class)
//    public void infoBaseTest(Hashtable<String, String> data){
////        System.out.println("================================== infoBaseTest +++++++++++++++++++");
//        baseService.compareValueInSheetResolution(data);
//    }
}
