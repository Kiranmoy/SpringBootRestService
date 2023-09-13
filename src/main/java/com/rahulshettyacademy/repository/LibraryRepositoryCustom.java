package com.rahulshettyacademy.repository;

import java.util.List;

import com.rahulshettyacademy.controller.Books;

public interface LibraryRepositoryCustom {
	
	List<Books> findAllByAuthor(String authorName);

	Books findByName(String bookName);

}
