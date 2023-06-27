package com.automation.test;

import com.automation.TestNGApplication;
import com.automation.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@SpringBootTest(classes = TestNGApplication.class)
public class SpringTest extends AbstractTestNGSpringContextTests {
    @Autowired
    BaseService baseService;
    @Test
    void contextLoads() {
        baseService.test();
    }
}