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

create table parse_rule(
id int not null primary key auto_increment,
section_id int not null,
class varchar(255) not null,
text_tag varchar(16),
exclude_urls varchar(4048),
foreign key (section_id) references section(id)
);

create table news_story(
id int not null primary key auto_increment,
title varchar(250) not null,
recorded timestamp not null default current_timestamp(),
url varchar(2083) not null,
source_id int not null,
foreign key (source_id) references source(id)
);
