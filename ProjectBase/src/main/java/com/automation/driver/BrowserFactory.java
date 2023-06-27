package com.automation.driver;

import com.automation.constants.FrameworkConstants;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public enum BrowserFactory {
    //An enum class can include methods and fields just like regular classes.
    CHROME{
        @Override
        public WebDriver createDriver(){
            WebDriverManager.getInstance(DriverManagerType.CHROME).setup();

            //source: https://stackoverflow.com/questions/75680149/unable-to-establish-websocket-connection
            return new ChromeDriver(getOptions().addArguments("--remote-allow-origins=*"));
        }

        @Override
        public ChromeOptions getOptions(){
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--no-sandbox");
            chromeOptions.addArguments("--disable-infobars");
            chromeOptions.addArguments("--disable-notifications");
            chromeOptions.addArguments("--window-size=1920,1020");
//            chromeOptions.setHeadless(Boolean.parseBoolean(FrameworkConstants.HEADLESS));
            //source: https://itnext.io/how-to-run-a-headless-chrome-browser-in-selenium-webdriver-c5521bc12bf0
            // https://stackoverflow.com/questions/68111321/problem-with-headless-when-running-selenium-test-using-java
            // https://stackoverflow.com/questions/63623917/selenium-in-java-is-not-finding-element-when-using-headless-chrome
            //kích thước cửa sổ rất quan trọng: --window-size=1920,1020 ; nếu kích thước k phù hợp, sẽ có 1 số element k thể nhìn thấy
            if(Boolean.parseBoolean(FrameworkConstants.HEADLESS)){
                chromeOptions.addArguments("--headless", "--disable-dev-shm-usage", "--disable-gpu", "--ignore-certificate-errors", "--disable-extensions");
            }

            return chromeOptions;
        }

    }, FIREFOX{
        @Override
        public WebDriver createDriver() {
            WebDriverManager.getInstance(DriverManagerType.FIREFOX).setup();

            return new FirefoxDriver(getOptions());
        }

        @Override
        public FirefoxOptions getOptions() {
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.addArguments("--no-sandbox");
            firefoxOptions.addArguments("--disable-infobars");
            firefoxOptions.addArguments("--disable-notifications");
            firefoxOptions.addArguments("--window-size=1920,1020");
//            firefoxOptions.setHeadless(Boolean.parseBoolean(FrameworkConstants.HEADLESS));
            if(Boolean.parseBoolean(FrameworkConstants.HEADLESS)){
                firefoxOptions.addArguments("--headless", "--disable-gpu", "--ignore-certificate-errors");
            }

            return firefoxOptions;
        }
    };
    public abstract WebDriver createDriver();

    public abstract MutableCapabilities getOptions();
}
