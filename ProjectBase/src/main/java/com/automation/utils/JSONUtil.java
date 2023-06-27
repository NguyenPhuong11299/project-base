package com.automation.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.ArrayList;
import java.util.List;

public class JSONUtil {

    private static ObjectMapper mapper;
    static {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> T objectMapper(Object objectInput, Class<T> clazz){
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        T objectOutput = mapper.convertValue(objectInput, clazz);
        return objectOutput;
    }

    public static <T,R> List<T> listObjectMapper(List<R> listObject, Class<T> clazz){
        List<T> result = new ArrayList<>();
        for(Object object : listObject){
            T objectOutput = objectMapper(object, clazz);
            result.add(objectOutput);
        }
        return result;
    }
}
