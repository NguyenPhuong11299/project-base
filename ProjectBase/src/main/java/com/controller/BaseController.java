package com.controller;

import com.model.response.BaseResponse;
import com.service.BaseService;
import com.util.ParamUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/bases")
@Log4j2
public class BaseController {
    @Autowired
    BaseService baseService;

    @GetMapping()
    public ResponseEntity<List<BaseResponse>> getAllBase(
            @RequestHeader(name = ParamUtils.AUTHORIZE_HEADER) String accessToken
    ){
        List<BaseResponse> result = baseService.getAllBase();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
