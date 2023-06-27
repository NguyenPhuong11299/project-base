package com.automation.test.seller.pages.Dashboard;

import com.automation.driver.DriverManager;
import com.automation.test.seller.pages.CommonPage;
import com.automation.test.seller.pages.Dashboard.product.ProductPage;
import lombok.NoArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.automation.keywords.WebUI.*;

@NoArgsConstructor
@Component
public class DashboardPage extends CommonPage {
    public String pageUrl = "/admin/home";

    private By menuOrder = By.xpath("//span[normalize-space()='Orders']");
    private By orderTitle = By.xpath("//h3[normalize-space()='Orders']");


    @Autowired
    private ProductPage productPage;

    @FindBy(xpath = "//span[normalize-space()='Products']")
    private WebElement menuProduct;

    @FindBy(xpath = "//p[@class='text-darkslate']")
    public WebElement textInDashboardPage;

    public void setDashboardPage(){
        PageFactory.initElements(DriverManager.getDriver(), this);
    }

    public void openProductPage(){
        menuProduct.click();

        productPage.setProductPage();
        waitForWebElementVisible(productPage.productTitle);
        verifyContainsImpl(getCurrentUrl(), productPage.pageUrl,"The url of product page not match");
    }

}
