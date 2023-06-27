package com.automation.helpers;

import com.automation.constants.FrameworkConstants;
import com.automation.utils.ParamUtils;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.log4j.Log4j2;
import org.testng.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Log4j2
public class ApiHelpers {
    //source: https://www.toolsqa.com/rest-assured/rest-api-end-to-end-test/
    //request: https://www.toolsqa.com/rest-assured/convert-json-to-java-object/
    //response: https://www.toolsqa.com/rest-assured/json-response-body-to-java-object/
    List<Integer> listStatusSuccess = Arrays.asList(200, 201);

    public <T> T mapResponseToClass(Response response, Class<T> clazz) {
        // Deserializing the Response body into clazz
        return response.getBody().as(clazz);
    }

    public <T, R> R callPostMethod(String uri, T requestBody, Map<String, String> mapHeader, Class<R> rClass) {
        RequestSpecification request = requestInitialization(mapHeader);
        Response response = request.body(requestBody).post(uri);
        //nếu gọi api không thành công -> dừng test
        Assert.assertTrue(listStatusSuccess.contains(response.getStatusCode()), "\nResponse Status Code = " + response.getStatusCode() + " : uri = " + uri + " : requestBody : " + requestBody);

//        //cách 1 chạy dc
//        JsonPath jsonPathEvaluator = response.jsonPath();
//        R r = jsonPathEvaluator.get();
        //cách 2
        //source: https://stackoverflow.com/questions/59249709/java-lang-noclassdeffounderror-io-restassured-internal-common-assertion-assertp
        return response.getBody().as(rClass);
    }

    public <T, R> R callPutMethod(String uri, T requestBody, Map<String, String> mapHeader, Class<R> rClass) {
        RequestSpecification request = requestInitialization(mapHeader);
        Response response = request.body(requestBody).put(uri);
        //nếu gọi api không thành công -> dừng test
        Assert.assertTrue(listStatusSuccess.contains(response.getStatusCode()), "\nResponse Status Code = " + response.getStatusCode() + " : uri = " + uri + " : requestBody : " + requestBody);

//        //cách 1 chạy dc
//        JsonPath jsonPathEvaluator = response.jsonPath();
//        R r = jsonPathEvaluator.get();
        //cách 2
        //source: https://stackoverflow.com/questions/59249709/java-lang-noclassdeffounderror-io-restassured-internal-common-assertion-assertp
        return response.getBody().as(rClass);
    }

    public RequestSpecification requestInitialization(Map<String, String> mapHeader) {
        RestAssured.baseURI = FrameworkConstants.URL_PREFIX;
        RequestSpecification request = RestAssured.given();

        request.header(ParamUtils.CONTENT_TYPE, ParamUtils.APPLICATION_JSON);
        Set<String> listHeader = mapHeader.keySet();
        for (String keyHeader : listHeader) {
            request.header(keyHeader, mapHeader.get(keyHeader));
        }

        return request;
    }

    public JsonPath getJsonResponseOfApi(String uri) {
        // Specify the base URL to the RESTful web service
        RestAssured.baseURI = FrameworkConstants.URL_PREFIX + uri;

        // Get the RequestSpecification of the request that is to be sent
        // to the server.
        RequestSpecification httpRequest = RestAssured.given();

        // Call RequestSpecification.get() method to get the response.
        // Make sure you specify the resource name.
        Response response = httpRequest.get("");

        // First get the JsonPath object instance from the Response interface
        JsonPath jsonPathEvaluator = response.jsonPath();

        //đoạn này chạy dc
//        //source: https://www.toolsqa.com/rest-assured/deserialize-json-array/
//        // Read all the bases as a List of String. Each item in the list
//        // represent a bases node in the REST service Response
//        List<String> s = jsonPathEvaluator.getList("data.bases.Footwear.short_code");
//        List<BaseInfo> allBases = jsonPathEvaluator.getList("data.bases.Footwear", BaseInfo.class);
//
//        // Iterate over the list and print individual book item
//        // Note that every book entry in the list will be complete Json object of book
//        for(ListBase base : allBases)
//        {
//            System.out.println("Base: " + base.getShortCode());
//        }

        return jsonPathEvaluator;
    }

    //keyStr: chuỗi các key ( nối với nhau = dấu chấm "." ) tính từ root đến object có type = T ; vd: "data.bases.F"
    public <T> List<T> convertJsonResponseToListPOJO(JsonPath jsonPathEvaluator, String keyStr, Class<T> clazz) {
        //exception: java.lang.NoClassDefFoundError: io/restassured/internal/common/mapper/ObjectDeserializationContextImpl
        //solution: The same issue is resolved by switching back to the 4.1.1 version
        //source: https://github.com/rest-assured/rest-assured/issues/1220
        List<T> objectResponse = jsonPathEvaluator.getList(keyStr, clazz);
        return objectResponse;
    }
}
