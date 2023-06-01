package com.service;

import com.model.entity.Base;
import com.model.response.BaseResponse;
import com.repository.BaseReposiory;
import com.util.ParamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BaseService {
    @Autowired
    private BaseReposiory baseReposiory;

    public List<BaseResponse> getAllBase(){
        List<Base> baseDBs = baseReposiory.findByState(ParamUtils.APPROVED);
        List<BaseResponse> baseResponses = new ArrayList<>();
        for(Base base : baseDBs){
            BaseResponse baseResponse = BaseResponse.convertFromBase(base);
            baseResponses.add(baseResponse);
        }

        return baseResponses;
    }
}
