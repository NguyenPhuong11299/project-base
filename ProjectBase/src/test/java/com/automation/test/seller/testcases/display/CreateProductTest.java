package com.automation.test.seller.testcases.display;

import com.automation.TestNGApplication;
import com.automation.constants.FrameworkConstants;
import com.automation.model.excel.AddProductModel;
import com.automation.test.common.BaseTestImpl;
import com.automation.test.dataProvider.DataProviderManager;
import com.automation.test.seller.pages.Dashboard.DashboardPage;
import com.automation.test.seller.pages.Dashboard.product.ProductDetailPage;
import com.automation.test.seller.pages.Dashboard.product.ProductPage;
import com.automation.test.seller.pages.SignIn.SignInPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import java.util.Hashtable;

@SpringBootTest(classes = TestNGApplication.class)
public class CreateProductTest extends BaseTestImpl {
    private Hashtable<String, String> data;

    @Factory(dataProvider = "getDataCreateProduct", dataProviderClass = DataProviderManager.class)
    public CreateProductTest(Hashtable<String, String> data){
        this.data = data;
    }

    @Autowired
    private SignInPage signInPage;

    @Autowired
    private DashboardPage dashboardPage;

    @Autowired
    private ProductPage productPage;

    @Autowired
    private ProductDetailPage productDetailPage;

    //create product trong sheet "Add product" , file: "TestPriceBase.xlsx"
    //check variant trong sheet "Location Size Color" , file: "test-order.xlsx"
    //note: trong acc phải có store trước khi create product
    @Test
    public void createProduct(){
        signInPage.openDashboardPage(FrameworkConstants.TEST_ORDER);
        dashboardPage.openProductPage();
        productPage.openCreateProductPage();
        AddProductModel addProductModel = productDetailPage.createProduct(data);
        productPage.openDetailProduct(addProductModel.getProductTitle());
        productDetailPage.verifyInfoDetailProduct(addProductModel, data);
    }
}

