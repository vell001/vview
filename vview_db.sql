drop database if exists vview;
create database vview;
use vview;

-- user

drop table if exists user;

create table user(
id bigint(20) unsigned not null primary key auto_increment,
nickname varchar(100),
username varchar(40) not null,
password varchar(32) not null,
email varchar(40) not null,
user_url varchar(100),
status int not null default 1,
rdate datetime not null,
ldate datetime,
ver_code tinytext
) default charset=utf8;

--  category

drop table if exists category;

create table category (
id bigint(20) unsigned not null primary key auto_increment,
name tinytext not null,
info longtext,
status int not null default 1
)default charset=utf8;

-- post

drop table if exists post;

create table post (
id bigint(20) unsigned not null primary key auto_increment,
author bigint(20) unsigned not null ,
pdate datetime not null,
content longtext not null,
title text not null,
excerpt text,
status int not null default 0,
comment_status int not null default 1,
mdate datetime not null,
category_id bigint(20) unsigned not null default 1,
parent bigint(20) unsigned not null default 0,
comment_count bigint(20) not null default 0,
click_count bigint(20) not null default 0,
excerpt_img text,
score bigint(20) not null default 1400,
uid bigint(20) not null ,
foreign key (category_id) references category(id),
foreign key (author) references user(id),
key (uid)
) default charset=utf8;


-- comment

drop table if exists comment;

create table comment (
  id bigint(20) unsigned not null primary key auto_increment,
  post_id bigint(20) unsigned not null,
  author tinytext not null,
  author_email varchar(100),
  author_url varchar(200),
  author_ip varchar(100) not null default '',
  cdate datetime not null,
  content text not null,
  status int not null default 0,
  parent_id bigint(20) unsigned default 0,
  user_id bigint(20) unsigned default 0,
  notice int default 1,
  comment_img text,
  uid bigint(20) not null ,
  foreign key (post_id) references post(id)
) default charset=utf8;



-- label

drop table if exists label;

create table label (
id bigint(20) unsigned not null primary key auto_increment,
name tinytext not null,
info longtext,
status int not null default 1,
uid bigint(20) not null,
key (uid)
)default charset=utf8;

-- label for post

drop table if exists post_label;

create table post_label (
post_uid bigint(20) not null,
label_uid bigint(20) not null,
primary key (post_uid, label_uid),
foreign key (post_uid) references post(uid),
foreign key (label_uid) references label(uid)
)default charset=utf8;

-- setting

drop table if exists setting;

create table setting (
id bigint(20) unsigned not null primary key auto_increment,
notice longtext not null,
cdate datetime not null,
status int not null default 1
)default charset=utf8;
