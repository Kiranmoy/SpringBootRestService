package com.rahulshettyacademy.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.rahulshettyacademy.entity.Books;
import org.springframework.stereotype.Component;

@Component
public class SpecificProduct {


  Books product;
  @JsonInclude(Include.NON_NULL)
  String msg;
  @JsonInclude(Include.NON_DEFAULT)
  int price;
  @JsonInclude(Include.NON_NULL)
  String category;

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public Books getProduct() {
    return product;
  }

  public void setProduct(Books product) {
    this.product = product;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }


}
