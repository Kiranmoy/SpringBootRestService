package com.rahulshettyacademy.repository;

import com.rahulshettyacademy.entity.Books;

import java.util.List;

public interface LibraryRepositoryCustom {

  List<Books> findAllByAuthor(String authorName);

  Books findByName(String bookName);

}
