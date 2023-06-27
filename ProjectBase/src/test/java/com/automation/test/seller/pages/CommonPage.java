package com.automation.test.seller.pages;

import org.springframework.stereotype.Component;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

@Component
public class CommonPage extends AbstractTestNGSpringContextTests {
//    //source: https://stackoverflow.com/questions/13487025/headless-environment-error-in-java-awt-robot-class-with-mac-os
    static {
        System.setProperty("java.awt.headless", "false");
    }

}
