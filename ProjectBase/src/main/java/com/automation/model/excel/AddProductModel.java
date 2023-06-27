package com.automation.model.excel;

import com.automation.utils.ParamUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AddProductModel {
    @JsonProperty(value = ParamUtils.PRODUCT_NAME)
    private String productName;

    @JsonProperty(value = ParamUtils.SHORTCODE)
    private String shortCode;

    @JsonProperty(value = ParamUtils.DESIGN_FRONT_URL)
    private String designFrontUrl;

    @JsonProperty(value = ParamUtils.DESIGN_BACK_URL)
    private String designBackUrl;

    private String colorSelected;

    private String productTitle;

    private boolean has2Side;

    private String storeSelected;

    @JsonProperty(value = ParamUtils.IS_BASE_3D)
    private boolean isBase3D;

    @JsonProperty(value = ParamUtils.IS_GEN_MOCKUP)
    private boolean isGenMockup;
}
