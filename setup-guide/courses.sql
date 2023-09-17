
CREATE TABLE courses(
	course_name varchar(50),
	id varchar(50),
	price int,
	category varchar(50),
	PRIMARY KEY (course_name)
);


INSERT INTO courses(course_name,id,price,category) values('Microservices testing','2',23,'api');
INSERT INTO courses(course_name,id,price,category) values('Selenium','3',66,'web');
INSERT INTO courses(course_name,id,price,category) values('Appium','12',13,'mobile');

Select * from courses;
