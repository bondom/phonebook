
<b>SQL queries for creating all necessary tables:</b>

create table hibernate_sequences (sequence_name varchar(255) not null, 	sequence_next_hi_value bigint, primary key (sequence_name));

create table phone_book (id bigint not null, email varchar(255), first_name varchar(255) not null, home_phone varchar(255), last_name varchar(255) not null, mobile_phone varchar(255) not null, patronymic varchar(255) not null, street varchar(255), user_id bigint, primary key (id));

create table users (id bigint not null, full_name varchar(255), login varchar(255), password varchar(255), primary key (id));

alter table users add constraint UK_login unique (login);

alter table phone_book add constraint FKuser foreign key (user_id) references users (id);

<b>Example of external config file:</b>

# JPA
spring.datasource.url: jdbc:mysql://localhost:3306/defaultschema
spring.datasource.username=root
spring.datasource.password=*******
spring.datasource.driverClassName=com.mysql.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
	
# File storage
filestorage.users.path=D:/users.json
filestorage.phonebookrecords.path=D:/phonebookrecords.json

# Choosing type of data storage
spring.profiles.active=db
#spring.profiles.active=file 
