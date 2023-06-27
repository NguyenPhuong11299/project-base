package com.automation.test.seller.pages.SignIn;

import com.automation.constants.FrameworkConstants;
import com.automation.driver.DriverManager;
import com.automation.helpers.ExcelHelpers;
import com.automation.test.seller.models.SignInModel;
import com.automation.test.seller.pages.Dashboard.DashboardPage;
import com.automation.utils.ParamUtils;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Hashtable;

import static com.automation.keywords.WebUI.*;

@Log4j2
@Component
//@NoArgsConstructor
public class SignInPage {//extends CommonPage {

    private String pageUrl = "/authentication/login";
    private String pageTitle = "Login";
    public By inputEmail = By.xpath("//input[@id='email']");
    public By inputPassword = By.xpath("//input[@id='password']");
    public By buttonSignIn = By.xpath("//button[normalize-space()='Log in']");
    public By textInDashBoard = By.xpath("//p[@class='text-darkslate']");

    @FindBy(xpath = "//h2[normalize-space()='Login']")
    private WebElement titleLogin;

    @FindBy(xpath = "//input[@id='email']")
    private WebElement inputEmailE;

    @FindBy(xpath = "//input[@id='password']")
    private WebElement inputPasswordE;

    @FindBy(xpath = "//button[normalize-space()='Log in']")
    private WebElement buttonSignInE;

    @Autowired
    private DashboardPage dashboardPage;

    //https://anhtester.com/blog/selenium-java-bai-18-su-dung-page-factory-trong-pom-b381.html
    public void setSignInPage(){
        PageFactory.initElements(DriverManager.getDriver(), this);
    }

    public void openDashboardPage(String excelPath){
        ExcelHelpers excelHelpers = new ExcelHelpers();
        excelHelpers.setExcelFile(excelPath, ParamUtils.SIGNIN);

        setSignInPage();
        getURL(FrameworkConstants.URL_CRM);
        verifyContainsImpl(getCurrentUrl(), pageUrl,"The url of sign in page not match.");
        waitForWebElementVisible(titleLogin);
        inputEmailE.clear();
        inputEmailE.sendKeys(excelHelpers.getCellData(1, SignInModel.email));
        inputPasswordE.clear();
        inputPasswordE.sendKeys(excelHelpers.getCellData(1, SignInModel.password));
        buttonSignInE.click();

        dashboardPage.setDashboardPage();
        waitForWebElementVisible(dashboardPage.textInDashboardPage);
        verifyContainsImpl(getCurrentUrl(), dashboardPage.pageUrl,"Sign in failed. Can not redirect to Dashboard page.");
    }

    public DashboardPage signIn(String excelPath){
        ExcelHelpers excelHelpers = new ExcelHelpers();
        excelHelpers.setExcelFile(excelPath, ParamUtils.SIGNIN);

        getURL(FrameworkConstants.URL_CRM);
        verifyContains(getCurrentUrl(), pageUrl, "The url of sign in page not match.");
        verifyEquals(getPageTitle(), pageTitle, "The title of sign in page not match.");
        clearText(inputEmail);
        clearText(inputPassword);
        setText(inputEmail, excelHelpers.getCellData(1, SignInModel.email));
        setText(inputPassword, excelHelpers.getCellData(1, SignInModel.password)); // mat khau k ma hoa nen k dung : DecodeUtils.decrypt(data.get(SignInModel.getPassword()))
        clickElement(buttonSignIn);
        waitForElementVisible(textInDashBoard);//thoi gian cho de getCurrentUrl() cap nhat dc url cua page hien tai
//        verifyContains(getCurrentUrl(), getDashboardPage().pageUrl, "Sign in failed. Can not redirect to Dashboard page.");
        sleep(5);//thêm thời gian chờ để call api list-base success

        return dashboardPage;
    }

}
