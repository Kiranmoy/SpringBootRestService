package com.rahulshettyacademy;


import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonArray;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahulshettyacademy.controller.LibraryController;
import com.rahulshettyacademy.model.ProductsPrices;
import com.rahulshettyacademy.model.SpecificProduct;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "CoursesCatalogue")
public class PactConsumerTest {

  @Autowired
  private LibraryController libraryController;

  @Pact(consumer = "BooksCatalogue")
  public RequestResponsePact PactallCoursesDetailsPriceCheck(PactDslWithProvider builder) {
    return builder.given("courses exist") // provider state name
      .uponReceiving("getting all courses details")// interaction name or description
      .path("/allCourseDetails")
      .willRespondWith()
      .status(200)
      .body(PactDslJsonArray.arrayMinLike(3)
        .integerType("price", 10)
        .closeObject()).toPact();
  }

  @Test
  @PactTestFor(pactMethod = "PactallCoursesDetailsPriceCheck", port = "9999")
  public void testAllProductsSum(MockServer mockServer) throws JsonMappingException, JsonProcessingException {
    String expectedJson = "{\"booksPrice\":250,\"coursesPrice\":30}";
    libraryController.setCoursesBaseUrl(mockServer.getUrl());

    ProductsPrices productsPrices = libraryController.getProductPrices();
    ObjectMapper obj = new ObjectMapper();
    String jsonActual = obj.writeValueAsString(productsPrices);

    Assertions.assertEquals(expectedJson, jsonActual);
  }

  @Pact(consumer = "BooksCatalogue")
  public RequestResponsePact getAppiumCourseByName(PactDslWithProvider builder){
    return builder
      .given("appium course exists") // provider state name
      .uponReceiving("get appium course by name") // interaction name or description
      .path("/getCourseByName/Appium")
      .willRespondWith()
      .status(200)
      .body(
       new PactDslJsonBody()
         .integerType("price",44)
         .stringType("category","mobile")
      ).toPact();
  }

  @Test
  @PactTestFor(pactMethod = "getAppiumCourseByName", port = "9999")
  public void getProductDetailsByName(MockServer mockServer) throws JsonProcessingException {
    libraryController.setCoursesBaseUrl(mockServer.getUrl());
    String expectedJson = "{\"product\":{\"book_name\":\"Appium\",\"id\":\"ttefs36\",\"isbn\":\"ttefs\",\"aisle\":36,\"author\":\"Shetty\"},\"price\":44,\"category\":\"mobile\"}";
    SpecificProduct specificProduct = libraryController.getProductFullDetails("Appium");
    ObjectMapper obj = new ObjectMapper();
    String jsonActual = obj.writeValueAsString(specificProduct);
    Assertions.assertEquals(expectedJson, jsonActual);
  }

  @Pact(consumer = "BooksCatalogue")
  public RequestResponsePact getAppiumCourseByNameNotExist(PactDslWithProvider builder){
    return builder
      .given("course appium not exists")
      .uponReceiving("get appium course by name not exists")
      .path("/getCourseByName/appium")
      .willRespondWith()
      .status(404)
      .toPact();
  }


  @Test
  @PactTestFor(pactMethod = "getAppiumCourseByNameNotExist",port="9999")
  public void getProductDetailsByNameNotExist(MockServer mockServer) throws JsonProcessingException {
    libraryController.setCoursesBaseUrl(mockServer.getUrl());
    String expectedJson = "{\"product\":{\"book_name\":\"Appium\",\"id\":\"ttefs36\",\"isbn\":\"ttefs\",\"aisle\":36,\"author\":\"Shetty\"},\"msg\":\"appium : Category and price details are not available at this time\"}";
    SpecificProduct specificProduct = libraryController.getProductFullDetails("appium");
    ObjectMapper obj = new ObjectMapper();
    String actualJson = obj.writeValueAsString(specificProduct);
    Assertions.assertEquals(expectedJson, actualJson);

  }

}
