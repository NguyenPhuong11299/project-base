package com.automation.model.responseApi;

import com.automation.model.excel.InfoBaseModel;
import com.automation.utils.JsonPropertyUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
public class BaseInfo { //object trong response api list base tren trang seller
    //các key được trả lại trong json response api bắt buộc phải xuất hiện trong model response nếu muốn chuyển đổi
    // nếu k muốn chuyển đổi ( bỏ qua key đó) thì phải thêm @JsonIgnore vào key đó -> value của key đó sẽ bị bỏ qua
    @JsonProperty(value = JsonPropertyUtil.SHORT_CODE)
    private String shortCode;

    @JsonProperty(value = JsonPropertyUtil.BASE_NAME)
    private String baseName;

    @JsonProperty(value = JsonPropertyUtil.CLONE_PRICE)
    private String clonePrice;

    private String resolution;

    private List<Variant> variants;

    @JsonIgnore//nếu k muốn chuyển đổi object printable
    private String printable;
    @JsonIgnore
    private String colors;
    @JsonIgnore
    private String sizes;
    @JsonIgnore
    private String locations;
    @JsonIgnore
    private String metadata;

    @JsonIgnore
//        @JsonProperty(value = JsonPropertyUtil.BASE_ID)
    private String base_id;

    @JsonIgnore
//        @JsonProperty(value = JsonPropertyUtil.CATALOG_NAME)
    private String catalog_name;

    @JsonIgnore
//        @JsonProperty(value = JsonPropertyUtil.BASE_GROUP_ID)
    private String base_group_id;

    @JsonIgnore
//        @JsonProperty(value = JsonPropertyUtil.BASE_GROUP_NAME)
    private String base_group_name;

    @JsonIgnore
//        @JsonProperty(value = JsonPropertyUtil.MIN_ITEM)
    private String min_item;

    @JsonIgnore
//        @JsonProperty(value = JsonPropertyUtil.IS_GEN_MOCKUP_SUPPORT)
    private String is_gen_mockup_support;

    @JsonIgnore
//        @JsonProperty(value = JsonPropertyUtil.MOCKUP_API)
    private String mockup_api;

    @Data
    public static class Variant {
        //        @JsonProperty(value = "sku") //tên biến giống key json rồi thì k cần đặt @JsonProperty
        private String sku;

        @JsonProperty(value = JsonPropertyUtil.LOCATION_ID)
        private String locationId;

        @JsonProperty(value = JsonPropertyUtil.BASE_COST)
        private String baseCost;

        @JsonProperty(value = JsonPropertyUtil.SECOND_SIDE_PRICE)
        private String secondSidePrice;

        //2 annotation @JsonProperty và @JsonIgnore không dùng được cùng nhau
//        @JsonProperty(value = JsonPropertyUtil.COLOR_ID)
        @JsonIgnore
        private String color_id;

        @JsonIgnore
//        @JsonProperty(value = JsonPropertyUtil.SIZE_ID)
        private String size_id;

//        @JsonIgnore
        @JsonProperty(value = JsonPropertyUtil.DEFAULT_PROFIT)
        private String defaultProfit;

        @JsonIgnore
        private String stock;
    }

    public Map<String, List<InfoBaseModel>> convertToListInfoBaseModel(){
        Map<String, List<InfoBaseModel>> mapLocation = new HashMap<>();

        List<Variant> variantList = this.variants;
        for(Variant variant : variantList){
            String location = variant.locationId;
            InfoBaseModel infoBaseModel = new InfoBaseModel();
            infoBaseModel.setProductName(this.baseName);
            infoBaseModel.setShortCode(this.shortCode);
            infoBaseModel.setLocation(location);
            infoBaseModel.setSku(variant.sku);
            infoBaseModel.setBaseCost(variant.baseCost);
            infoBaseModel.setSecondSidePrice(variant.secondSidePrice);
            infoBaseModel.setDesignFee(this.clonePrice);
            infoBaseModel.setDefaultProfit(variant.defaultProfit);

            if(mapLocation.containsKey(location)){
                mapLocation.get(location).add(infoBaseModel);
            }else {
                List<InfoBaseModel> infoBaseModelList = new LinkedList<>();
                infoBaseModelList.add(infoBaseModel);
                mapLocation.put(location, infoBaseModelList);
            }
        }

        return mapLocation;
    }
}
