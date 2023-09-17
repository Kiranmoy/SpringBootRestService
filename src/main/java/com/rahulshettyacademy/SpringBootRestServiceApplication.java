package com.rahulshettyacademy;

import com.rahulshettyacademy.repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootRestServiceApplication {

  @Autowired
  LibraryRepository repository;

  public static void main(String[] args) {
    SpringApplication.run(SpringBootRestServiceApplication.class, args);
  }
}
