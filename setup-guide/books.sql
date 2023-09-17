
CREATE TABLE books(
	book_name varchar(50),
	id varchar(50),
	isbn varchar(50),
	aisle varchar(50),
	author varchar(50),
	PRIMARY KEY (id)
);

INSERT INTO books(book_name,id,isbn,aisle,author) values("Microservices","hrtge43","hrtge","43","Shetty");
INSERT INTO books(book_name,id,isbn,aisle,author) values("Selenium","khuys21","khuys","21","Shetty");
INSERT INTO books(book_name,id,isbn,aisle,author) values("Appium","ttefs36","ttefs","36","Shetty");


select * from books;
