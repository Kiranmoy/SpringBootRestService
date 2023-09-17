package com.rahulshettyacademy.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahulshettyacademy.entity.Books;
import com.rahulshettyacademy.model.*;
import com.rahulshettyacademy.repository.LibraryRepository;
import com.rahulshettyacademy.service.LibraryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
public class LibraryController {

  private static final Logger logger = LoggerFactory.getLogger(LibraryController.class);
  @Autowired
  LibraryRepository repository;
  @Autowired
  ProductsPrices productPrices;
  @Autowired
  LibraryService libraryService;

  String coursesBaseUrl = "http://localhost:9191";

  @PostMapping("/addBook")
  public ResponseEntity addBookImplementation(@RequestBody Books books) {
    String id = libraryService.buildId(books.getIsbn(), books.getAisle()); //dependency Mock
    AddResponse ad = new AddResponse();

    if (!libraryService.checkBookAlreadyExist(id)){
      logger.info("Book do not exist so creating one");
      books.setId(id);
      repository.save(books);
      HttpHeaders headers = new HttpHeaders();
      headers.add("unique", id);
      ad.setMsg("Success Book is Added");
      ad.setId(id);
      return new ResponseEntity<AddResponse>(ad, headers, HttpStatus.CREATED);
    } else {
      logger.info("Book  exist so skipping creation");
      ad.setMsg("Book already exist");
      ad.setId(id);
      return new ResponseEntity<AddResponse>(ad, HttpStatus.ACCEPTED);
    }
  }

  @CrossOrigin
  @RequestMapping("/getBooks/{id}")
  public Books getBookById(@PathVariable(value = "id") String id) {
    try {
      Books lib = repository.findById(id).get();
      return lib;
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }

  @CrossOrigin
  @GetMapping("getBooks/author")
  public List<Books> getBookByAuthorName(@RequestParam(value = "authorname") String authorname) {
    return repository.findAllByAuthor(authorname);
  }

  @PutMapping("/updateBook/{id}")
  public ResponseEntity<Books> updateBook(@PathVariable(value = "id") String id, @RequestBody Books books) {
    //	Library existingBook = repository.findById(id).get();
    Books existingBook = libraryService.getBookById(id);
    existingBook.setAisle(books.getAisle());
    existingBook.setAuthor(books.getAuthor());
    existingBook.setBook_name(books.getBook_name());
    repository.save(existingBook);
    return new ResponseEntity<Books>(existingBook, HttpStatus.OK);
  }

  @DeleteMapping("/deleteBook")
  public ResponseEntity<String> deleteBookById(@RequestBody Books books) {
    Books libdelete = libraryService.getBookById(books.getId());
    repository.delete(libdelete);
    logger.info("Book  is deleted ");
    return new ResponseEntity<>("Book is deleted", HttpStatus.CREATED);
  }

  @GetMapping("/getBooks")
  public List<Books> getBooks() {
    return repository.findAll();
  }


  @GetMapping("/getProductDetails/{name}")
  public SpecificProduct getProductFullDetails(@PathVariable(value = "name") String name)
    throws JsonMappingException, JsonProcessingException {
    SpecificProduct specificProduct = new SpecificProduct();
    TestRestTemplate restTemplate = new TestRestTemplate();
    Books lib = repository.findByName(name);
    specificProduct.setProduct(lib);
    ResponseEntity<String> response = restTemplate.getForEntity(coursesBaseUrl + "/getCourseByName/" + name, String.class);
    if (response.getStatusCode().is4xxClientError()) {
      specificProduct.setMsg(name + "Category and price details are not available at this time");
    } else {
      ObjectMapper mapper = new ObjectMapper();
      AllCourseDetails allCourseDetails = mapper.readValue(response.getBody(), AllCourseDetails.class);
      specificProduct.setCategory(allCourseDetails.getCategory());
      specificProduct.setPrice(allCourseDetails.getPrice());
    }
    return specificProduct;
  }


  @CrossOrigin
  @GetMapping("/getProductPrices")
  public ProductsPrices getProductPrices() throws JsonMappingException, JsonProcessingException {
    productPrices.setBooksPrice(250);
    long sum = 0;
    for (int i = 0; i < getAllCoursesDetails().length; i++) {
      sum = sum + getAllCoursesDetails()[i].getPrice();
    }
    productPrices.setCoursesPrice(sum);
    return productPrices;
  }

  public void setCoursesBaseUrl(String url) {
    coursesBaseUrl = url;
  }

  public AllCourseDetails[] getAllCoursesDetails() throws JsonMappingException, JsonProcessingException {
    TestRestTemplate restTemplate = new TestRestTemplate();
    ResponseEntity<String> response = restTemplate.getForEntity(coursesBaseUrl + "/allCourseDetails", String.class);
    ObjectMapper mapper = new ObjectMapper();
    AllCourseDetails[] allCourseDetails = mapper.readValue(response.getBody(), AllCourseDetails[].class);
    return allCourseDetails;
  }

}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

