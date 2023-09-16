package com.rahulshettyacademy.repository;

import com.rahulshettyacademy.controller.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryRepository extends JpaRepository<Books, String>, LibraryRepositoryCustom {

}
