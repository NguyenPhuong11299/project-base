package com.model.response;

import com.model.entity.Base;
import lombok.Data;

@Data
public class BaseResponse {
    private String id;

    private String name;

    public static BaseResponse convertFromBase(Base base){
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setId(base.getId());
        baseResponse.setName(base.getName());
        return baseResponse;
    }
}
