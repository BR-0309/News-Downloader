DROP DATABASE IF EXISTS web;
CREATE DATABASE web;
USE web;

CREATE TABLE language (
  id   INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(150)
);
CREATE TABLE country (
  id   INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(150)
);
CREATE TABLE source (
  id          INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
  name        VARCHAR(64)  NOT NULL,
  homepage    VARCHAR(255) NOT NULL UNIQUE,
  language_id INT          NOT NULL,
  country_id  INT          NOT NULL,
  FOREIGN KEY (language_id) REFERENCES language (id),
  FOREIGN KEY (country_id) REFERENCES country (id)
);
CREATE TABLE section (
  id        INT           NOT NULL PRIMARY KEY AUTO_INCREMENT,
  name      VARCHAR(150)  NOT NULL,
  url       VARCHAR(2083) NOT NULL,
  source_id INT           NOT NULL,
  FOREIGN KEY (source_id) REFERENCES source (id)
);
CREATE TABLE exclude_urls (
  id  INT           NOT NULL PRIMARY KEY AUTO_INCREMENT,
  url VARCHAR(2084) NOT NULL
);
CREATE TABLE parse_rule (
  id            INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
  section_id    INT          NOT NULL,
  css_selector  VARCHAR(255) NOT NULL,
  text_selector VARCHAR(255),
  url_selector  VARCHAR(255),
  FOREIGN KEY (section_id) REFERENCES section (id)
);

CREATE TABLE news_story (
  id         INT           NOT NULL PRIMARY KEY AUTO_INCREMENT,
  title      VARCHAR(1024) NOT NULL,
  recorded   TIMESTAMP     NOT NULL             DEFAULT current_timestamp(),
  url        VARCHAR(2083) NOT NULL,
  section_id INT           NOT NULL,
  FOREIGN KEY (section_id) REFERENCES section (id)
);
