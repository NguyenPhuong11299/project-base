package com.automation.model.excel;

import com.automation.utils.ParamUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class InfoBaseModel {//object in sheet Location Size Color
    @JsonProperty(value = ParamUtils.PRODUCT_NAME)
    private String productName;

    @JsonProperty(value = ParamUtils.SHORTCODE)
    private String shortCode;

    @JsonProperty(value = ParamUtils.LOCATION)
    private String location;

    @JsonProperty(value = ParamUtils.SIZE)
    private String size;

    @JsonProperty(value = ParamUtils.COLOR)
    private String color;

    @JsonProperty(value = ParamUtils.BG_SKU)
    private String sku;

    @JsonProperty(value = ParamUtils.BASE_COST)
    private String baseCost;

    @JsonProperty(value = ParamUtils.SECOND_SIDE_PRICE)
    private String secondSidePrice;

    @JsonProperty(value = ParamUtils.DESIGN_FEE)
    private String designFee;

    private String defaultProfit;

    public void from(Map<String, String> mapData){
        this.productName = mapData.get(ParamUtils.PRODUCT_NAME);
        this.shortCode = mapData.get(ParamUtils.SHORTCODE);
        this.location = mapData.get(ParamUtils.LOCATION);
        this.size = mapData.get(ParamUtils.SIZE);
        this.color = mapData.get(ParamUtils.COLOR);
        this.sku = mapData.get(ParamUtils.BG_SKU);
        this.baseCost = mapData.get(ParamUtils.BASE_COST);
        this.secondSidePrice = mapData.get(ParamUtils.SECOND_SIDE_PRICE);
        this.designFee = mapData.get(ParamUtils.DESIGN_FEE);
    }
}
