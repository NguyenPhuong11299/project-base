package com.automation.driver;

import com.automation.constants.FrameworkConstants;
import com.automation.enums.Target;
import com.automation.exceptions.TargetNotValidException;
import org.openqa.selenium.WebDriver;

public class TargetFactory {

    public WebDriver createInstance(String browser){
        Target target = Target.valueOf(FrameworkConstants.TARGET.toUpperCase());
        WebDriver webDriver;

        switch (target){
            case LOCAL:
                //Create new driver from Enum setup in BrowserFactory class
                webDriver = BrowserFactory.valueOf(browser.toUpperCase()).createDriver();
                break;
            default:
                throw new TargetNotValidException(target.toString());
        }

        return webDriver;
    }

}
