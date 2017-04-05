drop database if exists web;
create database web;
use web;

create table language(
id int not null primary key auto_increment,
name varchar(150)
);
create table country(
id int not null primary key auto_increment,
name varchar(150)
);
create table source(
id int not null primary key auto_increment,
name varchar(64) not null,
homepage varchar(255) not null unique,
language_id int not null,
country_id int not null,
foreign key (language_id) references language(id),
foreign key (country_id) references country(id)
);
create table section(
id int not null primary key auto_increment,
name varchar(150) not null,
url varchar(2083) not null,
source_id int not null,
foreign key (source_id) references source(id)
);
create table exclude_urls(
id int not null primary key auto_increment,
url varchar(2084) not null
);
create table parse_rule(
  id            int not null primary key auto_increment,
  section_id    int not null,
  css_selector  varchar(255) not null,
  text_selector VARCHAR(255),
  url_selector  VARCHAR(255),
foreign key (section_id) references section(id)
);

create table news_story(
id int not null primary key auto_increment,
title varchar(250) not null,
recorded timestamp not null default current_timestamp(),
url varchar(2083) not null,
section_id int not null,
foreign key (section_id) references section(id)
);
