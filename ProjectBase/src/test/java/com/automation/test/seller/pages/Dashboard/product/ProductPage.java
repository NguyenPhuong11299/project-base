package com.automation.test.seller.pages.Dashboard.product;

import com.automation.driver.DriverManager;
import com.automation.test.seller.pages.CommonPage;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.automation.keywords.WebUI.*;

@Component
@Log4j2
public class ProductPage extends CommonPage {
    public String pageUrl = "/admin/products";

    @FindBy(xpath = "//h3[normalize-space()='Products']")
    public WebElement productTitle;

    @FindBy(xpath = "//button[normalize-space()='Create Product']")
    private WebElement buttonCreateProduct;

    @FindBy(css = "input[data-placeholder='Search products']")
    private WebElement inputSearch;

    @FindBy(xpath = "//a[@class='text-reset text-decoration-none']")
    private WebElement productSelected;

    @Autowired
    private ProductDetailPage productDetailPage;

    // Khởi tạo class khi được gọi và truyền driver vào để các thành phần trong
    // Và khởi tạo initElements
    public void setProductPage() {
        PageFactory.initElements(DriverManager.getDriver(), this);
    }

    public void openCreateProductPage() {
        buttonCreateProduct.click();

        productDetailPage.setProductDetailPage();
        waitForWebElementVisible(productDetailPage.designTitle);
        verifyContainsImpl(getCurrentUrl(), productDetailPage.setPageUrl("new"), "The url of create product page not match");
    }

    public void openDetailProduct(String productTitle) {
        verifyContainsImpl(getCurrentUrl(), pageUrl, "The url of create product page not match");
        waitForWebElementVisible(inputSearch);
        inputSearch.clear();
        setTextWithActionImpl(inputSearch, productTitle);
        int countTimes = 0;
        while (!isWebElementVisible(productSelected, 3) && countTimes <= 5) {
            inputSearch.clear();
            setTextWithActionImpl(inputSearch, productTitle);
            countTimes++;
            log.info("++++++++++++++++++++++++ wait search product");
        }
        productSelected.click();
        waitForWebElementVisible(productDetailPage.editProduct);
        verifyContainsImpl(getCurrentUrl(), productDetailPage.url, "The url of detail product page not match"); // k biet productId nen k kiem tra o url dc
    }
}
