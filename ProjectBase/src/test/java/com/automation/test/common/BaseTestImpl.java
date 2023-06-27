package com.automation.test.common;

import com.automation.driver.DriverManager;
import com.automation.driver.TargetFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ThreadGuard;
import org.springframework.stereotype.Component;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

@Component
public class BaseTestImpl extends AbstractTestNGSpringContextTests {
    @Parameters("BROWSER")
    @BeforeMethod(alwaysRun = true)
    //https://anhtester.com/blog/selenium-java/selenium-java-bai-10-cach-su-dung-chu-thich-annotation-trong-testng
    public void createDriver(@Optional("chrome") String browser){
        WebDriver driver = ThreadGuard.protect(new TargetFactory().createInstance(browser));
        DriverManager.setDriver(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void closeDriver(){
        DriverManager.quit();
    }
}
