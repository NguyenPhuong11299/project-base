package com.automation.service;

import com.automation.constants.FrameworkConstants;
import com.automation.enums.BaseCollectionEnum;
import com.automation.exceptions.BadRequestException;
import com.automation.helpers.ApiHelpers;
import com.automation.helpers.ExcelHelpers;
import com.automation.helpers.TxtHelpers;
import com.automation.model.entity.Partner;
import com.automation.model.excel.InfoBaseModel;
import com.automation.model.response.BaseResponse;
import com.automation.model.responseApi.BaseInfo;
import com.automation.repository.BaseRepository;
import com.automation.model.entity.Base;
import com.automation.repository.PartnerRepository;
import com.automation.utils.ParamUtils;
import io.restassured.path.json.JsonPath;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testng.asserts.SoftAssert;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BaseService {
    private String listBaseUri = "/api/v1/base/lists";

    @Autowired
    private BaseRepository baseRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    public List<BaseResponse> getAllBase(){
        List<Base> baseDBs = baseRepository.findByState(ParamUtils.APPROVED);
        List<BaseResponse> baseResponses = new ArrayList<>();
        for(Base base : baseDBs){
            BaseResponse baseResponse = BaseResponse.convertFromBase(base);
            baseResponses.add(baseResponse);
        }

        return baseResponses;
    }

    public void test() {
        System.out.println("+++++++++++++++++++ pass ++++++++++++++ ");
    }

    public void compareSkuInApiListBase(){//thay đổi dữ liệu trong sheet "Location size color"
        SoftAssert softAssert = new SoftAssert();
        //lấy dữ liệu trong file excel : key1: shortCode ; key2: location
        Map<String, Map<String, List<InfoBaseModel>>> mapShortCodeExcel = getDataOfSheetLocationSizeColor();
        List<String> listShortCode = new ArrayList<>(mapShortCodeExcel.keySet());

        //lấy dữ liệu từ api : map có key = shortCode xuất hiện trong file excel và api
        //key1: shortCode ; key2: locationId
        Map<String, Map<String, List<InfoBaseModel>>> mapShortCodeApi = getDataOfApiListBase(softAssert, listShortCode);

        //duyệt mapShortCodeApi vì có thể có shortCode xuất hiện trong file excel nhưng không tìm thấy trong response api
        Set<String> listShortCodeInApi = mapShortCodeApi.keySet();
        //duyệt shortCode
        for(String shortCode : listShortCodeInApi){
            Map<String, List<InfoBaseModel>> mapLocationApi = mapShortCodeApi.get(shortCode);
            Set<String> listLocationIdApi = mapLocationApi.keySet();
            List<String> listLocationApi = new LinkedList<>();

            Map<String, List<InfoBaseModel>> mapLocationExcel = mapShortCodeExcel.get(shortCode);
            List<String> listLocationExcel = new ArrayList<>(mapLocationExcel.keySet());
            //duyệt location
            for(String locationId : listLocationIdApi){
                String location = null;
                //lấy location (trong excel) tương ứng với locationId (trong db) để so sánh
                Optional<Partner> partnerDB = partnerRepository.findById(locationId);
                if(partnerDB.isPresent()){
                    String tag = partnerDB.get().getTag();
                    if(ParamUtils.TAGS.equals(tag)){//có rất nhiều partner có cột s_tags = 'tag' -> chuyển qua lấy location = s_name
                        location = partnerDB.get().getName();
                    }else {
                        location = partnerDB.get().getTag();
                    }
                }
//                String location = getPartnerKeyByLocationId(locationId);
                if(StringUtils.isNotEmpty(location)){
                    listLocationApi.add(location);

                    List<InfoBaseModel> infoBaseModelApis = mapLocationApi.get(locationId);
                    List<String> listSkuApi = infoBaseModelApis.stream().map(InfoBaseModel::getSku).collect(Collectors.toList());

                    List<InfoBaseModel> infoBaseModelExcels = mapLocationExcel.get(location);
                    //nếu location có trong api nhưng k có trong excel thì infoBaseModelExcels = null
                    // -> exception: Cannot invoke "java.util.List.stream()" because "infoBaseModelExcels" is null
                    //-> phải check infoBaseModelExcels != null
                    if(CollectionUtils.isNotEmpty(infoBaseModelExcels)) {
                        List<String> listSkuExcel = infoBaseModelExcels.stream().map(InfoBaseModel::getSku).collect(Collectors.toList());
                        //duyệt sku
                        for (InfoBaseModel infoBaseModelApi : infoBaseModelApis) {
                            String skuApi = infoBaseModelApi.getSku();
                            List<InfoBaseModel> infoBaseModelExcel = infoBaseModelExcels.stream().filter(b -> skuApi.equals(b.getSku())).collect(Collectors.toList());
                            for (InfoBaseModel base : infoBaseModelExcel) {
                                softAssert.assertEquals(Float.parseFloat(infoBaseModelApi.getBaseCost()), Float.parseFloat(base.getBaseCost()),
                                        "\nWith shortCode = " + shortCode + " and sku = " + skuApi + " : excel - api : baseCost: ");
                                softAssert.assertEquals(Float.parseFloat(infoBaseModelApi.getSecondSidePrice()), Float.parseFloat(base.getSecondSidePrice()),
                                        "\nWith shortCode = " + shortCode + " and sku = " + skuApi + " : excel - api : secondSidePrice: ");
                                softAssert.assertEquals(Float.parseFloat(infoBaseModelApi.getDesignFee()), Float.parseFloat(base.getDesignFee()),
                                        "\nWith shortCode = " + shortCode + " and sku = " + skuApi + " : excel - api : designFee: ");
                                softAssert.assertTrue(Float.parseFloat(infoBaseModelApi.getDefaultProfit()) > Float.parseFloat(infoBaseModelApi.getBaseCost()),
                                        "\nWith shortCode = " + shortCode + " and sku = " + skuApi + " : defaultProfit <= baseCost => price <= 0");
                            }
                        }

                        List<String> listSkuNotInApi = listSkuExcel.stream().filter(s -> !listSkuApi.contains(s)).collect(Collectors.toList());
                        softAssert.assertTrue(CollectionUtils.isEmpty(listSkuNotInApi), "\nWith shortCode = " + shortCode + " has listSkuNotInApi = " + listSkuNotInApi);
                        List<String> listSkuNotInExcel = listSkuApi.stream().filter(s -> !listSkuExcel.contains(s)).collect(Collectors.toList());
                        softAssert.assertTrue(CollectionUtils.isEmpty(listSkuNotInExcel), "\nWith shortCode = " + shortCode + " has listSkuNotInExcel = " + listSkuNotInExcel);
                    }
                }
            }

            List<String> listLocationNotInApi = listLocationExcel.stream().filter(l -> !listLocationApi.contains(l)).collect(Collectors.toList());
            softAssert.assertTrue(CollectionUtils.isEmpty(listLocationNotInApi),"\nWith shortCode = " + shortCode + " has listLocationNotInApi = " + listLocationNotInApi);
            List<String> listLocationNotInExcel = listLocationApi.stream().filter(l -> !listLocationExcel.contains(l)).collect(Collectors.toList());
            softAssert.assertTrue(CollectionUtils.isEmpty(listLocationNotInExcel),"\nWith shortCode = " + shortCode + " has listLocationNotInExcel = " + listLocationNotInExcel);
        }

        TxtHelpers txtHelpers = new TxtHelpers();
        txtHelpers.writeResultIntoFile(FrameworkConstants.TEST_API_LIST_BASE_RESULT, softAssert);
    }

    public Map<String, Map<String, List<InfoBaseModel>>> getDataOfApiListBase(SoftAssert softAssert, List<String> listShortCode) {
        //key1: shortCode ; key2: locationId
        Map<String, Map<String, List<InfoBaseModel>>> mapShortCode = new HashMap<>();

        //lấy dữ liệu từ response api list base
        ApiHelpers apiHelpers = new ApiHelpers();
        JsonPath jsonPathEvaluator = apiHelpers.getJsonResponseOfApi(listBaseUri);
        //lấy các shortCode cần kiểm tra từ api
        for(String shortCode : listShortCode) {
            String keyStr;
            List<BaseInfo> listBaseInCollection = new LinkedList<>();
            for(BaseCollectionEnum baseCollectionEnum : BaseCollectionEnum.values()){
                //nếu trong key có chứa khoảng trống " " -> sử dụng dấu nháy đơn ('')
                //source: https://github.com/rest-assured/rest-assured/issues/531
                keyStr = "data.bases.'" + baseCollectionEnum.getValue() + "'";
                List<String> shortCodeInCollection = jsonPathEvaluator.getList(keyStr + ".short_code");
                if(shortCodeInCollection.contains(shortCode)){
                    //nếu Printed Apparel chứa shortCode -> convert collection "Printed Apparel"
                    listBaseInCollection = apiHelpers.convertJsonResponseToListPOJO(jsonPathEvaluator, keyStr, BaseInfo.class);
                    break;//tìm được rồi thì thoát khỏi vòng lặp for
                }
            }

            //lọc ra base chứa shortCode trong collection
            List<BaseInfo> baseSelecteds = listBaseInCollection.stream().filter(b -> shortCode.equals(b.getShortCode())).collect(Collectors.toList());
            if(baseSelecteds.size() == 1) {
                BaseInfo baseInfo = baseSelecteds.get(0);
                Map<String, List<InfoBaseModel>> mapLocation = baseInfo.convertToListInfoBaseModel();
                mapShortCode.put(shortCode, mapLocation);
            }else {
                //kiểm tra xem k trả lại softAssert thì kết quả có chứa dòng này k => có trả lại ( đã check)
                softAssert.assertTrue(baseSelecteds.size() == 1, "\nSize of list base in api has short code " + shortCode + " = " + baseSelecteds.size());

            }
        }
        return mapShortCode;
    }

    public Map<String, Map<String, List<InfoBaseModel>>> getDataOfSheetLocationSizeColor() {
        //lay infoBase tu file ke toan
        ExcelHelpers excelHelpers = new ExcelHelpers();
        List<Hashtable<String, String>> listBaseExcel = excelHelpers.getAllDataInSheet(FrameworkConstants.TEST_ORDER, ParamUtils.LOCATION_SIZE_COLOR, ParamUtils.FLOAT);

        //Lớp LinkedList duy trì thứ tự của phần tử được thêm vào
//        List<InfoBaseModel> listInfoBase = new LinkedList();

        Map<String, Map<String, List<InfoBaseModel>>> mapShortCode = new HashMap<>();//key1: shortCode ; key2: location
        for (Map<String, String> mapBase : listBaseExcel) {
            String shortCode = mapBase.get(ParamUtils.SHORTCODE);
            String location = mapBase.get(ParamUtils.LOCATION);
            //chuyển dữ liệu từ map -> object
            InfoBaseModel infoBaseModel = new InfoBaseModel();
            infoBaseModel.from(mapBase);

            //map theo shortCode : key1
            if (mapShortCode.containsKey(shortCode)) {
                //map theo location : key2
                Map<String, List<InfoBaseModel>> mapLocation = mapShortCode.get(shortCode);
                if (mapLocation.containsKey(location)) {
                    mapLocation.get(location).add(infoBaseModel);
                } else {
                    List<InfoBaseModel> listInfoBase = new LinkedList();
                    listInfoBase.add(infoBaseModel);
                    mapLocation.put(location, listInfoBase);
                }
                //put mapLocation sau khi thay doi vao mapShortCode
                mapShortCode.put(shortCode, mapLocation);
            } else {
                List<InfoBaseModel> listInfoBase = new LinkedList();
                listInfoBase.add(infoBaseModel);
                Map<String, List<InfoBaseModel>> mapLocation = new HashMap<>();
                mapLocation.put(location, listInfoBase);
                mapShortCode.put(shortCode, mapLocation);
            }
        }
        return mapShortCode;
    }

    public void compareValueInSheetResolution(Hashtable<String, String> excelData) {
//        System.out.println("================== compareValueInSheetResolution ================");

        String name = excelData.get(ParamUtils.SHORTCODE);
        //lay du lieu tu database
        Optional<Base> baseOptional = baseRepository.findByNameAndState(name, ParamUtils.APPROVED);

        //so sanh du lieu trong file excel va database
        if (!baseOptional.isPresent()) {
            throw new BadRequestException("Not found base");
        }

        Base base = baseOptional.get();
        base.compareValue(excelData);

    }
}
