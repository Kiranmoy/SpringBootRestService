package com.rahulshettyacademy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rahulshettyacademy.controller.Books;
@Repository
public interface LibraryRepository extends JpaRepository<Books, String>,LibraryRepositoryCustom{

}
