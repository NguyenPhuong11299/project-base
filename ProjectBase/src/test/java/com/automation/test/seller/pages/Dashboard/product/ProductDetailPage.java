package com.automation.test.seller.pages.Dashboard.product;

import com.automation.constants.FrameworkConstants;
import com.automation.driver.DriverManager;
import com.automation.helpers.ExcelHelpers;
import com.automation.model.excel.AddProductModel;
import com.automation.model.excel.InfoBaseModel;
import com.automation.test.seller.pages.CommonPage;
import com.automation.utils.JSONUtil;
import com.automation.utils.ParamUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.springframework.stereotype.Component;
import org.testng.asserts.SoftAssert;

import java.util.Hashtable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.automation.keywords.WebUI.*;

@Component
@Log4j2
public class ProductDetailPage extends CommonPage {
//    private String productId;
    public String url = "/admin/products/"; // + them productId

    //edit Design
    @FindBy(xpath = "//h3[normalize-space()='Design']")
    public WebElement designTitle;

    @FindBy(xpath = "//span[normalize-space()='Add Product']")
    public WebElement addProduct;

    @FindBy(xpath = "//input[@id='search catalogue']")
    private WebElement searchBase;

    @FindBy(xpath = "//img[@class='base-img']")
    private WebElement baseSelected;

    @FindBy(xpath = "//span[normalize-space()='Save']")
    private WebElement saveBase;

    @FindBy(xpath = "//a[@class='mat-tooltip-trigger color-item active']")
    private WebElement colorSelected;

    @FindBy(xpath = "//div[@class='cdk-overlay-connected-position-bounding-box']")
    private WebElement colorTooltip;

    @FindBy(xpath = "//label[contains(text(),'Upload design')]")
    private WebElement btnUploadDesign;

    @FindBy(xpath = "//input[@id='upload_design']")
    private WebElement upFile;

    @FindBy(xpath = "//h2[normalize-space()='File Uploader']")
    private WebElement fileUploader;

    @FindBy(css = "mat-button-toggle[id^='mat-button-toggle']")
    private List<WebElement> buttonFrontBack; //[0]: front ; [1]: back

    @FindBy(xpath = "//button[@type='button'][normalize-space()='Save']")
    private WebElement saveDesign;

    //info
    @FindBy(css = "input[data-placeholder^='Type Tittle..'][aria-required='true']")
    //ghep css: https://anhtester.com/blog/selenium-java/selenium-java-bai-5-cach-xac-dinh-locators-cua-element-tren-website
    private WebElement inputProductTitle;

    @FindBy(xpath = "//div[@class='ng-star-inserted']")
    private List<WebElement> listStoreName;

    @FindBy(xpath = "//span[@class='mat-slide-toggle-bar mat-slide-toggle-bar-no-side-margin']")
    private List<WebElement> listToggleBar; //danh sách các nút chuyển trạng thái: [0] : Personnalize ; từ [1] là list store: index = listStoreName.index + 1

    @FindBy(xpath = "//button[normalize-space()='Save']")
    private WebElement saveProduct;
    
    @FindBy(xpath = "//h3[normalize-space()='edit product']")
    public WebElement editProduct;

    @FindBy(xpath = "//div[@class='list-mockup-item col ng-star-inserted']")
    public List<WebElement> listMockupElement;

    //tìm các element "tr" : Xác định với Con cháu Descendant
    //lớp cha 1: //div[@class='table-responsive rounded']
    //lớp cha 2: //table[@role='table']
    //lớp cha 3: //tbody[@role='rowgroup']
    //cụm từ k đổi: /descendant::
    //con hoặc cháu là thẻ "tr" nằm bên trong 3 lớp cha trên
    @FindBy(xpath = "//div[@class='table-responsive rounded']//table[@role='table']//tbody[@role='rowgroup']/descendant::tr")
    public List<WebElement> listVariantElement;

    @FindBy(xpath = "//a[@class='mat-tooltip-trigger btn-link text-decoration-none ng-star-inserted']")
    private WebElement storeSelectedElement;

    @FindBy(xpath = "//div[@class='ng-value ng-star-inserted']")
    private WebElement statusElement;

    public void setProductDetailPage(){
        PageFactory.initElements(DriverManager.getDriver(), this);
    }

    public String setPageUrl(String productId){
//        this.productId = productId;
        return url + productId;
    }

    public AddProductModel createProduct(Hashtable<String, String> data){
        //chuyển dữ liệu trong file excel thành object cho dễ sử dụng
        AddProductModel addProductModel = JSONUtil.objectMapper(data, AddProductModel.class);

        //chon product: search by name in file
        searchBase.sendKeys(addProductModel.getProductName());
        baseSelected.click();
        saveBase.click();

        waitForWebElementVisible(colorSelected);//chờ lưu dc base
        //màu dc chọn
        String colorSelectedStr = getValueInTooltipImpl(colorSelected, colorTooltip);
        addProductModel.setColorSelected(colorSelectedStr);

        //up design
        uploadFileByButtonImpl(btnUploadDesign, FrameworkConstants.CREATE_ORDER_DESIGN_URL + addProductModel.getDesignFrontUrl(), upFile);
        while (isWebElementVisible(fileUploader,3)){//chờ up design xong ~ k hien thi pop up file uploader
            log.info("+++++++++++++++++++++++++ wait file uploader: front");
        }
        waitForWebElementVisible(saveDesign);
        if(CollectionUtils.isNotEmpty(buttonFrontBack) && StringUtils.isNotEmpty(addProductModel.getDesignBackUrl())) {
            //có nút Front, Back && trong file excel có designBackUrl -> click Back -> up design back
            buttonFrontBack.get(1).click();
            uploadFileByButtonImpl(btnUploadDesign, FrameworkConstants.CREATE_ORDER_DESIGN_URL + addProductModel.getDesignBackUrl(), upFile);
            while (isWebElementVisible(fileUploader, 3)) {//chờ up design xong ~ k hien thi pop up file uploader
                log.info("+++++++++++++++++++++++++ wait file uploader: back");
            }
            waitForWebElementVisible(saveDesign);
            addProductModel.setHas2Side(true);
        }

        saveDesign.click();
        //chờ gen mockup -> thao tác dc với nút Save ~ hoan thanh gen mockup
        while (!isWebElementVisible(saveProduct, 2)) {
            log.info("+++++++++++++++++++++++ wait gen mockup");
        }

        //info
        String productTitle = addProductModel.getProductName() + "-" + System.currentTimeMillis();
        inputProductTitle.sendKeys(productTitle);
        addProductModel.setProductTitle(productTitle);
        //đang xử lý lấy add product vào store đầu tiên trong list store
        // ( nếu muốn chọn store thì thêm cột store vào file excel -> duyệt list store: đúng store thì lấy index của listStoreName & listToggleBar)
        String storeName = listStoreName.get(0).getText();
        addProductModel.setStoreSelected(storeName);
        //chon store
        listToggleBar.get(1).click();// listToggleBar.index = listStoreName + 1

        while (!isWebElementVisible(saveProduct, 5)){
            log.info("++++++++++++++++++++++++ wait save product");
        }
        moveToElementToBeClickableImpl(saveProduct);

        return addProductModel;
    }

    public void verifyInfoDetailProduct(AddProductModel addProductModel, Hashtable<String, String> data) {
        SoftAssert softAssert = new SoftAssert();

        //check title
        String productTitle = inputProductTitle.getAttribute(ParamUtils.VALUE);
        softAssert.assertEquals(productTitle, addProductModel.getProductTitle(),"\nProduct title is not match: ");

        //check status
        String status = statusElement.getAttribute(ParamUtils.OUTER_TEXT);
        softAssert.assertEquals(status, ParamUtils.ACTIVE,"\nProduct: " + productTitle + " : status");

        //check gen mockup
        if(addProductModel.isGenMockup()){//nếu base đó đc gen mockup tự động thì mới ktra phần gen mockup
            if(addProductModel.isBase3D() || addProductModel.isHas2Side()){//nếu là base 3D hoặc thêm design vào 2 mặt (base 2D) -> có 2 mockup dc gen
                softAssert.assertEquals(listMockupElement.size(), 2, "\nIncorrect number of mockups: ");
            }else if(!addProductModel.isHas2Side()){
                //nếu chỉ up design 1 mặt (base 2D)
                softAssert.assertEquals(listMockupElement.size(), 1,"\nIncorrect number of mockups: ");
            }
        }

        String subSku = addProductModel.getShortCode() + "-" + addProductModel.getColorSelected() + "-";
        //lay thong tin variant tu file ke toan
        ExcelHelpers excelHelpers = new ExcelHelpers();
        List<Hashtable<String, String>> listBaseExcel = excelHelpers.getAllDataInSheet(FrameworkConstants.TEST_ORDER, ParamUtils.LOCATION_SIZE_COLOR, ParamUtils.FLOAT);
        List<InfoBaseModel> infoBaseModelList = JSONUtil.listObjectMapper(listBaseExcel, InfoBaseModel.class);
        List<InfoBaseModel> listVariantExcel = infoBaseModelList.stream().filter(i -> i.getSku().contains(subSku)).collect(Collectors.toList());

        //check variant
        //example: "  Unisex T-Shirt | G5000/Azalea/S $ $  $11.25  $16.70 "
        //kiểm tra productName.contains("Unisex T-Shirt | G5000"), color = "Azalea", size = "S"
        //cost = "11.25" : 1 mặt = base cost ; 2 mặt = base cost + 2 side price
        //profit = "16.70" : profit phải lớn hơn 0
        for(WebElement variantE : listVariantElement){
            String variantText = variantE.getAttribute(ParamUtils.TEXT_CONTENT);
            String[] variantArr = variantText.split("\\$");//nhìn ví dụ thấy mảng có 5 phần tử
            String variantName = variantArr[0].trim();
            log.info("++++++++++++++++ String.trim() :" + variantName.trim() + "; arr[].trim() :" + variantArr[0].trim() + ";");
            String[] variantNameArr = variantName.split("/");

            String subProductName = variantNameArr[0].substring(2);
            String color = variantNameArr[1].trim();
            String size = variantNameArr[2].trim();

            String cost = variantArr[3].trim();
            String profit = variantArr[4].trim();

            softAssert.assertTrue(addProductModel.getProductName().contains(subProductName),"\nVariant = " + variantName + " : Excel's product name is not contains display's product name: ");
            softAssert.assertEquals(color, addProductModel.getColorSelected(),"\nVariant = " + variantName + " : Color is not match: ");
            softAssert.assertTrue(Double.parseDouble(profit) > 0,"\nVariant = " + variantName + " : profit <= 0 : ");

            Optional<InfoBaseModel> variantExcels = listVariantExcel.stream().filter(v -> v.getSize().equals(size)).findFirst();
            if(variantExcels.isPresent()){
                float costExcel = 0;
                if(addProductModel.isHas2Side()){
                    costExcel = Float.parseFloat(variantExcels.get().getBaseCost()) + Float.parseFloat(variantExcels.get().getSecondSidePrice());
                }else {
                    costExcel = Float.parseFloat(variantExcels.get().getBaseCost());
                }

                softAssert.assertEquals(Float.parseFloat(cost), costExcel,"\nVariant = " + variantName + " : cost : ");
            }else {
                softAssert.assertTrue(variantExcels.isPresent(),"\nVariant = " + variantName + " isn't in excel");
            }

        }

        String storeSelected = storeSelectedElement.getText();
        softAssert.assertEquals(storeSelected, addProductModel.getStoreSelected(), "\nStore name is not match");

        //kiem tra xem product xuat hien tren buyer chua
        storeSelectedElement.click();

        excelHelpers.writeResultIntoFile(data, softAssert);
    }
}
