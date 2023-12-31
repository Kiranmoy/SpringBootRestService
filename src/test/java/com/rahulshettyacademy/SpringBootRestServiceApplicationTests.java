package com.rahulshettyacademy;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahulshettyacademy.model.AddResponse;
import com.rahulshettyacademy.entity.Books;
import com.rahulshettyacademy.controller.LibraryController;
import com.rahulshettyacademy.repository.LibraryRepository;
import com.rahulshettyacademy.service.LibraryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class SpringBootRestServiceApplicationTests {

  @Autowired
  LibraryController con;
  @MockBean
  LibraryRepository repository;
  @MockBean
  LibraryService libraryService;
  @Autowired
  private MockMvc mockMvc;


  @Test
  void contextLoads() {

  }

  @Test
  public void checkBuildIDLogic() {
    LibraryService lib = new LibraryService();
    String id = lib.buildId("ZMAN", 24);
    assertEquals(id, "OLDZMAN24");
    String id1 = lib.buildId("MAN", 24);
    assertEquals(id1, "MAN24");

  }

  @Test
  public void addBookTest() {
    //mock

    Books lib = buildLibrary();
    when(libraryService.buildId(lib.getIsbn(), lib.getAisle())).thenReturn(lib.getId());
    when(libraryService.checkBookAlreadyExist(lib.getId())).thenReturn(false);
    when(repository.save(any())).thenReturn(lib);
    ResponseEntity response = con.addBookImplementation(buildLibrary());//step
    System.out.println(response.getStatusCode());
    assertEquals(response.getStatusCode(), HttpStatus.CREATED);
    AddResponse ad = (AddResponse) response.getBody();
    ad.getId();
    assertEquals(lib.getId(), ad.getId());
    assertEquals("Success Book is Added", ad.getMsg());

    //call Mock service from code

  }

  @Test
  public void addBookControllerTest() throws Exception {
    Books lib = buildLibrary();
    ObjectMapper map = new ObjectMapper();
    String jsonString = map.writeValueAsString(lib);


    when(libraryService.buildId(lib.getIsbn(), lib.getAisle())).thenReturn(lib.getId());
    when(libraryService.checkBookAlreadyExist(lib.getId())).thenReturn(false);
    when(repository.save(any())).thenReturn(lib);

    this.mockMvc.perform(post("/addBook").contentType(MediaType.APPLICATION_JSON)
        .content(jsonString)).andDo(print()).andExpect(status().isCreated())
      .andExpect(jsonPath("$.id").value(lib.getId()));

  }

  @Test
  public void getBookByAuthorTest() throws Exception {
    List<Books> li = new ArrayList<Books>();
    li.add(buildLibrary());
    li.add(buildLibrary());
    when(repository.findAllByAuthor(any())).thenReturn(li);
    this.mockMvc.perform(get("/getBooks/author").param("authorname", "Rahul Shetty"))
      .andDo(print()).andExpect(status().isOk()).
      andExpect(jsonPath("$.length()", is(2))).
      andExpect(jsonPath("$.[0].id").value("sfe3b"));

  }

  @Test
  public void updateBookTest() throws Exception {
    Books lib = buildLibrary();
    ObjectMapper map = new ObjectMapper();
    String jsonString = map.writeValueAsString(UpdateLibrary());
    when(libraryService.getBookById(any())).thenReturn(buildLibrary());
    this.mockMvc.perform(put("/updateBook/" + lib.getId()).contentType(MediaType.APPLICATION_JSON)
        .content(jsonString)).andDo(print()).andExpect(status().isOk())
      .andExpect(content().json("{\"book_name\":\"Boot\",\"id\":\"sfe3b\",\"isbn\":\"sfe\",\"aisle\":322,\"author\":\"Shetty\"}"));

  }

  @Test
  public void deleteBookControllerTest() throws Exception {
    when(libraryService.getBookById(any())).thenReturn(buildLibrary());
    doNothing().when(repository).delete(buildLibrary());
    this.mockMvc.perform(delete("/deleteBook").contentType(MediaType.APPLICATION_JSON)
        .content("{\"id\" : \"sfe3b\"}")).andDo(print())
      .andExpect(status().isCreated()).andExpect(content().string("Book is deleted"));


  }


  public Books buildLibrary() {
    Books lib = new Books();
    lib.setAisle(322);
    lib.setBook_name("Spring");
    lib.setIsbn("sfe");
    lib.setAuthor("Rahul Shetty");
    lib.setId("sfe3b");
    return lib;

  }

  public Books UpdateLibrary() {
    Books lib = new Books();
    lib.setAisle(322);
    lib.setBook_name("Boot");
    lib.setIsbn("rain");
    lib.setAuthor("Shetty");
    lib.setId("rain322");
    return lib;

  }


}
