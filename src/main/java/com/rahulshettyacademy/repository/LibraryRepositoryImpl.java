package com.rahulshettyacademy.repository;

import com.rahulshettyacademy.entity.Books;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class LibraryRepositoryImpl implements LibraryRepositoryCustom {

  @Autowired
  LibraryRepository repository;

  @Override
  public List<Books> findAllByAuthor(String authorName) {
    List<Books> bookswithAuthor = new ArrayList<Books>();
    // TODO Auto-generated method stub
    List<Books> books = repository.findAll();
    for (Books item : books){
      if (item.getAuthor().equalsIgnoreCase(authorName)) {
        bookswithAuthor.add(item);
      }
    }

    return bookswithAuthor;
  }

  @Override
  public Books findByName(String bookName) {
    List<Books> bookswithAuthor = new ArrayList<Books>();
    // TODO Auto-generated method stub
    List<Books> books = repository.findAll();
    for (Books item : books){
      if (item.getBook_name().equalsIgnoreCase(bookName)) {
        return item;
      }
    }
    return null;


  }


}
