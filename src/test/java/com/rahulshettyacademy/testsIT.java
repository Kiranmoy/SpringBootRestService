package com.rahulshettyacademy;

import com.rahulshettyacademy.entity.Books;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

@SpringBootTest
public class testsIT {

  //mvn test
  //TestRestTemplate Rest Assured
  @org.junit.jupiter.api.Test
  public void getAuthorNameBooksTest() throws JSONException {
    String expected = "[\r\n" +
      "    {\r\n" +
      "        \"book_name\": \"Cypress\",\r\n" +
      "        \"id\": \"abcd4\",\r\n" +
      "        \"isbn\": \"abcd\",\r\n" +
      "        \"aisle\": 4,\r\n" +
      "        \"author\": \"Rahul\"\r\n" +
      "    },\r\n" +
      "    {\r\n" +
      "        \"book_name\": \"Devops\",\r\n" +
      "        \"id\": \"fdsefr343\",\r\n" +
      "        \"isbn\": \"fdsefr3\",\r\n" +
      "        \"aisle\": 43,\r\n" +
      "        \"author\": \"Rahul\"\r\n" +
      "    }\r\n" +
      "]";
    TestRestTemplate restTemplate = new TestRestTemplate();
    ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/getBooks/author?authorname=Rahul", String.class);
    System.out.println(response.getStatusCode());
    System.out.println(response.getBody());
    JSONAssert.assertEquals(expected, response.getBody(), false);


  }

  @Test
  public void addBookIntegrationTest() {
    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<Books> request = new HttpEntity<Books>(buildLibrary(), headers);
    ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/addBook", request, String.class);
    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
		Assertions.assertEquals(buildLibrary().getId(), response.getHeaders().get("unique").get(0));


  }

  public Books buildLibrary() {
    Books lib = new Books();
    lib.setAisle(322);
    lib.setBook_name("Spring");
    lib.setIsbn("sfes");
    lib.setAuthor("Rahul Shetty");
    lib.setId("sfes322");
    return lib;

  }


}
