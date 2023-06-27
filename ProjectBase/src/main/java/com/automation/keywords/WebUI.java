package com.automation.keywords;

import com.automation.constants.FrameworkConstants;
import com.automation.driver.DriverManager;
import com.automation.exceptions.BadRequestException;
import com.automation.helpers.Helpers;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.List;

@Log4j2
public class WebUI {

    /**
     * Open website with URL
     *
     * @param url
     */
    public static void getURL(String url) {
        sleep(FrameworkConstants.WAIT_SLEEP_STEP);

        DriverManager.getDriver().get(url);
        waitForPageLoaded();

        log.info("Open URL: " + url);

//        if (ExtentTestManager.getExtentTest() != null) {
//            ExtentReportManager.pass("Open URL: " + URL);
//        }
//        AllureManager.saveTextLog("Open URL: " + URL);

//        addScreenshotToReport(Thread.currentThread().getStackTrace()[1].getMethodName() + "_" + DateUtils.getCurrentDateTime());

    }

    /**
     * Chờ đợi ép buộc với đơn vị là Giây
     *
     * @param second là số nguyên dương tương ứng số Giây
     */
    public static void sleep(double second) {
        try {
            Thread.sleep((long) (second * 1000));
        } catch (InterruptedException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    // Wait Page loaded

    /**
     * Chờ đợi trang tải xong (Javascript) với thời gian mặc định từ config
     */
    public static void waitForPageLoaded() {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_PAGE_LOADED), Duration.ofMillis(500));
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();

        // wait for Javascript to loaded
        ExpectedCondition<Boolean> jsLoad = driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");

        //Get JS is Ready
        boolean jsReady = js.executeScript("return document.readyState").toString().equals("complete");

        //Wait Javascript until it is Ready!
        if (!jsReady) {
            log.info("Javascript in NOT Ready!");
            //Wait for Javascript to load
            try {
                wait.until(jsLoad);
            } catch (Throwable error) {
                error.printStackTrace();
                Assert.fail("Timeout waiting for page load (Javascript). (" + FrameworkConstants.WAIT_PAGE_LOADED + "s)");
            }
        }
    }

    public static boolean verifyContains(String value1, String value2, String message) {
        boolean result = value1.contains(value2);
        if (result == true) {
            log.info("Verify Equals: " + value1 + " CONTAINS " + value2);
//            if (ExtentTestManager.getExtentTest() != null) {
//                ExtentReportManager.pass("Verify Contains: " + value1 + " CONTAINS " + value2);
//            }
//            AllureManager.saveTextLog("Verify Contains: " + value1 + "CONTAINS" + value2);
        } else {
            log.info("Verify Contains: " + value1 + " NOT CONTAINS " + value2);
//            if (ExtentTestManager.getExtentTest() != null) {
//                ExtentReportManager.fail("Verify Contains: " + value1 + " NOT CONTAINS " + value2);
//            }
//            AllureManager.saveTextLog("Verify Contains: " + value1 + " NOT CONTAINS " + value2);

            Assert.assertEquals(value1, value2, message); //so sanh value1 va value2
        }
        return result;
    }

//    @Step("Get Current URL")
    public static String getCurrentUrl() {
        smartWait();
        log.info("Get Current URL: " + DriverManager.getDriver().getCurrentUrl());
//        if (ExtentTestManager.getExtentTest() != null) {
//            ExtentReportManager.info("Get Current URL: " + DriverManager.getDriver().getCurrentUrl());
//        }
//        AllureManager.saveTextLog("Get Current URL: " + DriverManager.getDriver().getCurrentUrl());
        return DriverManager.getDriver().getCurrentUrl();
    }

    public static void smartWait() {
        if (FrameworkConstants.ACTIVE_PAGE_LOADED.trim().toLowerCase().equals("true")) {
            waitForPageLoaded();
        }
        sleep(FrameworkConstants.WAIT_SLEEP_STEP);
    }

//    @Step("Verify Equals: {0} ---AND--- {1}")
    public static boolean verifyEquals(Object value1, Object value2, String message) {
        boolean result = value1.equals(value2);
        if (result == true) {
            log.info("Verify Equals: " + value1 + " = " + value2);
//            if (ExtentTestManager.getExtentTest() != null) {
//                ExtentReportManager.pass("Verify Equals: " + value1 + " = " + value2);
//            }
//            AllureManager.saveTextLog("Verify Equals: " + value1 + " = " + value2);
        } else {
            log.info("Verify Equals: " + value1 + " != " + value2);
//            if (ExtentTestManager.getExtentTest() != null) {
//                ExtentReportManager.fail("Verify Equals: " + value1 + " != " + value2);
//            }
//            AllureManager.saveTextLog("Verify Equals: " + value1 + " != " + value2);
            Assert.assertEquals(value1, value2, message);
        }
        return result;
    }

//    @Step("Get Page Title")
    public static String getPageTitle() {
        smartWait();
        String title = DriverManager.getDriver().getTitle();
        log.info("Get Page Title: " + DriverManager.getDriver().getTitle());
//        if (ExtentTestManager.getExtentTest() != null) {
//            ExtentReportManager.info("Get Page Title: " + DriverManager.getDriver().getTitle());
//        }
//        AllureManager.saveTextLog("Get Page Title: " + DriverManager.getDriver().getTitle());
        return title;
    }

    /**
     * Xóa giá trị trong ô Text
     *
     * @param by element dạng đối tượng By
     */
//    @Step("Clear value in textbox")
    public static void clearText(By by) {
        waitForElementVisible(by).clear();
        log.info("Clear value in textbox " + by.toString());

//        if (ExtentTestManager.getExtentTest() != null) {
//            ExtentReportManager.pass("Clear value in textbox " + by.toString());
//        }
//        AllureManager.saveTextLog("Clear value in textbox");
//        addScreenshotToReport(Thread.currentThread().getStackTrace()[1].getMethodName() + "_" + DateUtils.getCurrentDateTime());

    }

    /**
     * Chờ đợi element sẵn sàng hiển thị để thao tác
     *
     * @param by element dạng đối tượng By
     * @return một đối tượng WebElement đã sẵn sàng để thao tác
     */
    public static WebElement waitForElementVisible(By by) {
        smartWait();
        waitForElementPresent(by);

        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
            boolean check = isElementVisible(by, 1);
            if (check == true) {
                return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            } else {
                scrollToElement(by);
                return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            }
        } catch (Throwable error) {
            Assert.fail("Timeout waiting for the element Visible. " + by.toString());
            log.error("Timeout waiting for the element Visible. " + by.toString());
        }
        return null;
    }

    public static void moveToElementToBeClickable(By by) { //fix lỗi: org.openqa.selenium.ElementClickInterceptedException: element click intercepted ( dính chuột)
        WebElement element = DriverManager.getDriver().findElement(by);
        Actions actions = new Actions(DriverManager.getDriver());
        actions.moveToElement(element).click().build().perform();
    }

    public static void moveToElementToBeClickableImpl(WebElement element) { //fix lỗi: org.openqa.selenium.ElementClickInterceptedException: element click intercepted ( dính chuột)
        Actions actions = new Actions(DriverManager.getDriver());
        actions.moveToElement(element).click().build().perform();
    }

    public static void waitForElementClickable(By by) { // chờ xong click luôn
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT));
        wait.until(ExpectedConditions.elementToBeClickable(by)).click();
    }

    //Reload page
    public static void reloadPage() {
        smartWait();

        DriverManager.getDriver().navigate().refresh();
        waitForPageLoaded();
        log.info("Reloaded page " + DriverManager.getDriver().getCurrentUrl());
    }

    /**
     * Chờ đợi element sẵn sàng tồn tại trong DOM theo thời gian tuỳ ý
     *
     * @param by element dạng đối tượng By
     * @return một đối tượng WebElement đã tồn tại
     */
    public static WebElement waitForElementPresent(By by) {
        smartWait();

        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
            return wait.until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (Throwable error) {
            log.error("Element not exist. " + by.toString());
            Assert.fail("Element not exist. " + by.toString());
        }
        return null;
    }

//    @Step("Verify element visible {0}")
    public static boolean isElementVisible(By by, long timeout) {
        smartWait();

        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeout));
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            log.info("Verify element visible " + by);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

//    @Step("Scroll to element {0}")
    public static void scrollToElement(By by) {
        smartWait();

        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("arguments[0].scrollIntoView(true);", getWebElement(by));
        log.info("Scroll to element " + by);
    }

    /**
     * Chuyển đổi đối tượng dạng By sang WebElement
     * Để tìm kiếm một element
     *
     * @param by là element thuộc kiểu By
     * @return Trả về là một đối tượng WebElement
     */
    public static WebElement getWebElement(By by) {
        return DriverManager.getDriver().findElement(by);
    }

    public static void uploadFileByButton(By by, String pathFile, By uploadFileElement) {
        WebElement element;
        String sourceFile = Helpers.getCurrentDir() + pathFile;
        try {
            // Lưu lại lớp window đầu tiên - mã ID hơi dài, in ra sẽ thấy :)
//            String MainWindow = DriverManager.getDriver().getWindowHandle();
//            System.out.println(MainWindow);

            element = getWebElement(by);
            element.click();
            //đợi 1s rồi mới đóng popup,k đợi thì hành động đóng có thể xảy ra trước khi popup hiện ra -> k đóng dc popup
            sleep(1);
            Robot robot = new Robot();
//            robot.keyPress(KeyEvent.VK_ALT);
//            robot.keyPress(KeyEvent.VK_F4);
//            robot.keyRelease(KeyEvent.VK_ALT);
            robot.keyPress(KeyEvent.VK_ESCAPE);
            robot.keyRelease(KeyEvent.VK_ESCAPE);

            element = getWebElement(uploadFileElement);
//            log.info("++++++++++++++++++++ element up file isElementVisible() : " + isElementVisible(uploadFileElement,2));
            // dùng replace, k dùng replaceAll: https://www.daniweb.com/programming/software-development/threads/287172/regex-problem
            //nếu để "/" trong pathFile thì chrome tìm thấy file, nhưng firefox k tìm thấy file -> firefox bị lỗi nhiều đoạn -> dùng chrome
            element.sendKeys(sourceFile);
            log.info("++++++++++++++++++++ Upload file success : " + sourceFile);

        } catch (Exception e) {
            log.info("Upload file fail: " + sourceFile);
            log.info(e.getMessage());
        }

    }

    public static void uploadFileByButtonImpl(WebElement btnUpload, String pathFile, WebElement upFile){
        String sourceFile = Helpers.getCurrentDir() + pathFile;
        try{
            btnUpload.click();
            sleep(1);

            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ESCAPE);
            robot.keyRelease(KeyEvent.VK_ESCAPE);


            upFile.sendKeys(sourceFile);
            log.info("+++++++++++++++++++++++ Upload file success: " + sourceFile);
        }catch (Exception e){
            log.info("Upload file fail: " + sourceFile);
            log.info(e.getMessage());
        }
    }

    public static void uploadFileByButtonWithAction(By by, String pathFile, By uploadFileElement) {
        WebElement element;
        try {
            // Lưu lại lớp window đầu tiên - mã ID hơi dài, in ra sẽ thấy :)
//            String MainWindow = DriverManager.getDriver().getWindowHandle();
//            System.out.println(MainWindow);

            element = getWebElement(by);
            element.click();
            //đợi 1s rồi mới đóng popup,k đợi thì hành động đóng có thể xảy ra trước khi popup hiện ra -> k đóng dc popup
            sleep(1);
//            Robot robot = new Robot();
////            robot.keyPress(KeyEvent.VK_ALT);
////            robot.keyPress(KeyEvent.VK_F4);
////            robot.keyRelease(KeyEvent.VK_ALT);
//            robot.keyPress(KeyEvent.VK_ESCAPE);
//            robot.keyRelease(KeyEvent.VK_ESCAPE);
            log.info("Start upload file : " + pathFile);

            Actions actions = new Actions(DriverManager.getDriver());//k hoạt động với nút ESCAPE
            actions.sendKeys(Keys.ESCAPE).build().perform();
            actions.sendKeys(Keys.ESCAPE).build().perform();

            String sourceFile = Helpers.getCurrentDir() + pathFile;
            element = getWebElement(uploadFileElement);
            // dùng replace, k dùng replaceAll: https://www.daniweb.com/programming/software-development/threads/287172/regex-problem
            //nếu để "/" trong pathFile thì chrome tìm thấy file, nhưng firefox k tìm thấy file
            element.sendKeys(sourceFile.replace("/", "\\"));
            log.info("++++++++++++++++++++ Success upload file : " + sourceFile.replace("/", "\\"));

//            //source: https://www.toolsqa.com/selenium-webdriver/robot-class-keyboard-events/
//            //https://anhtester.com/lesson/selenium-java-bai-12-xu-ly-hanh-dong-voi-actions-class-robot-class
            //https://viblo.asia/p/xu-ly-alertpopup-trong-selenium-webdriver-selenium-tutorial-16-L4x5xaRwKBM
//            // Tạo đối tượng của Actions class và để driver vào
//            Actions action = new Actions(DriverManager.getDriver());
//
//            // Dùng action để gọi hàm sendkeys điền dữ liệu. Không dùng sendKeys của WebElement
//            action.sendKeys(Keys.ESCAPE).build().perform();

        } catch (Exception e) {
            log.info("++++++++++++++++++ up file action : exception : " + e.getMessage());
        }
//        log.info("Upload file success: " + pathFile);
    }

    public static void setTextWithRobot(By inputSearch, String text) {
        clearText(inputSearch);
        setText(inputSearch, text);
        //an Enter
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            sleep(2);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    // robot không hoạt động với chrome headless -> thay thế = action
    //source: https://stackoverflow.com/questions/58231557/robot-keypress-not-working-in-headless-mode
    // https://github.com/serenity-bdd/serenity-core/issues/1519

    public static void setTextWithAction(By inputSearch, String text) {
        clearText(inputSearch);
        Actions actions = new Actions(DriverManager.getDriver());
        actions.sendKeys(getWebElement(inputSearch), text).build().perform();
        actions.sendKeys(Keys.ENTER).build().perform();
        actions.sendKeys(Keys.ENTER).build().perform();
        sleep(2);
    }

    public static void setTextWithActionImpl(WebElement inputSearch, String text) {
        inputSearch.clear();
        Actions actions = new Actions(DriverManager.getDriver());
        actions.sendKeys(inputSearch, text).build().perform();
        actions.sendKeys(Keys.ENTER).build().perform();
        actions.sendKeys(Keys.ENTER).build().perform();
        sleep(2);
    }

    public static void clearAndSetText(By by, String value) {
        clearText(by);
        setText(by, value);
    }

    /**
     * Điền giá trị vào ô Text
     *
     * @param by    element dạng đối tượng By
     * @param value giá trị cần điền vào ô text
     */
//    @Step("Set text on textbox")
    public static void setText(By by, String value) {
        waitForElementVisible(by).sendKeys(value);
        log.info("===============    Set text " + value + " on " + by.toString());

//        if (ExtentTestManager.getExtentTest() != null) {
//            ExtentReportManager.pass("Set text " + value + " on " + by.toString());
//        }
//        AllureManager.saveTextLog("Set text " + value + " on " + by.toString());
//
//        addScreenshotToReport(Thread.currentThread().getStackTrace()[1].getMethodName() + "_" + DateUtils.getCurrentDateTime());

    }

    /**
     * Click chuột vào đối tượng Element trên web
     *
     * @param by element dạng đối tượng By
     */
//    @Step("Click on the element {0}")
    public static void clickElement(By by) {
        waitForElementVisible(by).click();
        log.info("Click on element " + by.toString());

//        if (ExtentTestManager.getExtentTest() != null) {
//            ExtentReportManager.pass("Clicked on the object " + by.toString());
//        }
//        AllureManager.saveTextLog("Clicked on the object " + by.toString());
//
//        addScreenshotToReport(Thread.currentThread().getStackTrace()[1].getMethodName() + "_" + DateUtils.getCurrentDateTime());

    }

    /**
     * Get text of a element
     *
     * @param by element dạng đối tượng By
     * @return text of a element
     */
//    @Step("Get text of element {0}")
    public static String getTextElement(By by) {
        smartWait();
//        AllureManager.saveTextLog("Get text of element " + by.toString());
//        AllureManager.saveTextLog("==> The Text is: " + waitForElementVisible(by).getText());
        return waitForElementVisible(by).getText().trim();
    }

    public static boolean verifyElementText(By by, String text) {
        smartWait();
        waitForElementVisible(by);

        return getTextElement(by).trim().equals(text.trim());
    }

    /**
     * Chọn giá trị trong dropdown với dạng động (không phải Select Option thuần)
     *
     * @param objectListItem là locator của list item dạng đối tượng By
     * @param text           giá trị cần chọn dạng Text của item
     * @return click chọn một item chỉ định với giá trị Text
     */
    public static boolean selectOptionDynamic(By objectListItem, String text) {
        smartWait();
        //Đối với dropdown động (div, li, span,...không phải dạng select option)
        try {
            List<WebElement> elements = getWebElements(objectListItem);

            for (WebElement element : elements) {
                log.info(element.getText());
                if (element.getText().toLowerCase().trim().contains(text.toLowerCase().trim())) {
                    element.click();
                    return true;
                }
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            e.getMessage();
        }
        return false;
    }

    public static String selectFirstOptionDynamic(By objectListItem) {
        String text = null;
        smartWait();
        //Đối với dropdown động (div, li, span,...không phải dạng select option)
        try {
            List<WebElement> elements = getWebElements(objectListItem);
            WebElement element = elements.get(0);
            text = element.getText();
            System.out.println(text);

            element.click();

        } catch (Exception e) {
            log.info(e.getMessage());
            e.getMessage();
        }
        return text;
    }

    /**
     * Chuyển đổi đối tượng dạng By sang WebElement
     * Để tìm kiếm nhiều element
     *
     * @param by là element thuộc kiểu By
     * @return Trả về là Danh sách đối tượng WebElement
     */
    public static List<WebElement> getWebElements(By by) {
        return DriverManager.getDriver().findElements(by);
    }

    //Handle checkbox and radio button

    public static boolean verifyElementChecked(By by) {
        smartWait();

        boolean checked = getWebElement(by).isSelected();

        if (checked) {
            return true;
        } else {
            return false;
        }
    }

    public static String getValueInTooltip(WebElement element, By tooltipElement) {
        // Use action class to mouse hover on Text box field
        Actions action = new Actions(DriverManager.getDriver());
        action.moveToElement(element).build().perform();//tìm element chứa tooltip
        log.info("================= action 2 ; element : " + element.isDisplayed() + " ; tooltipElement : " + isElementVisible(tooltipElement, 1));
        if (isElementVisible(tooltipElement, 3)) {
            WebElement toolTipElement = getWebElement(tooltipElement);//nếu k xuất hiện tooltip -> lấy lại element chứa tooltip

            // To get the tool tip text and assert
            return toolTipElement.getText();
        } else {
            return null;
        }
    }

    public static String getValueInTooltipImpl(WebElement element, WebElement tooltipElement) {
        // Use action class to mouse hover on Text box field
        Actions action = new Actions(DriverManager.getDriver());
        action.moveToElement(element).build().perform();//tìm element chứa tooltip
        log.info("================= action 2 ; element : " + element.isDisplayed() + " ; tooltipElement : " + isWebElementVisible(tooltipElement, 1));
        if (isWebElementVisible(tooltipElement, 3)) {
//            WebElement toolTipElement = getWebElement(tooltipElement);//nếu k xuất hiện tooltip -> lấy lại element chứa tooltip

            // To get the tool tip text and assert
            return tooltipElement.getText();
        } else {
            return null;
        }
    }

    public static boolean isWebElementVisible(WebElement webElement, long timeout) {
        smartWait();

        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeout));
            wait.until(ExpectedConditions.visibilityOf(webElement));
            log.info("Verify webElement visible " + webElement);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static WebElement waitForWebElementVisible(WebElement webElement) {
        smartWait();

        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(FrameworkConstants.WAIT_EXPLICIT), Duration.ofMillis(500));
            boolean check = isWebElementVisible(webElement, 2);
            if (check == true) {
                return wait.until(ExpectedConditions.visibilityOf(webElement));
            } else {
                scrollToWebElement(webElement);
                return wait.until(ExpectedConditions.visibilityOf(webElement));
            }
        } catch (Throwable error) {
            Assert.fail("Timeout waiting for the webElement Visible. " + webElement);
            log.error("Timeout waiting for the webElement Visible. " + webElement);
        }
        return null;
    }

    public static void scrollToWebElement(WebElement webElement) {
        smartWait();

        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("arguments[0].scrollIntoView(true);", webElement);
        log.info("Scroll to webElement " + webElement);
    }

    public static boolean verifyContainsImpl(String value1, String value2, String message) {
        boolean result = value1.contains(value2);
        if (!result) {
            log.info("Verify Contains: " + value1 + " NOT CONTAINS " + value2);

            Assert.assertEquals(value1, value2, message); //so sanh value1 va value2
        }
        return result;
    }
}