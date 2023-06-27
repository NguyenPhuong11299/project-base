package com.automation.test.seller.testcases.api;

import com.automation.TestNGApplication;
import com.automation.service.BaseService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@SpringBootTest(classes = TestNGApplication.class)
@Log4j2
public class ListBaseApiTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private BaseService baseService;

    @Test
    public void validateSkuOfBase(){ //update thong tin cua base can test trong sheet "Location Size Color"
        log.info("+++++++++++ listBaseApiTest 1");
        baseService.compareSkuInApiListBase();
    }

//    public static void main(String[] args){
//        log.info("+++++++++++ listBaseApiTest 2");
////        new ListBaseApiTest().validateSkuOfBase();//k thể dùng cách này vì: Cannot invoke "com.automation.service.BaseService.compareSkuInApiListBase()" because "this.baseService" is null
//    }
}
