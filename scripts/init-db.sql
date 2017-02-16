drop database if exists onsight;
create database onsight;

USE mysql;
DROP USER IF EXISTS 'onsight_access'@'localhost';
CREATE USER 'onsight_access'@'localhost'  IDENTIFIED BY 'onsightpass';
GRANT ALL PRIVILEGES ON onsight.* TO 'onsight_access'@'localhost';

USE onsight;

create table user (
    username varchar(20) not null,
    password varchar(20) not null,
    name varchar(30),
    family varchar(30),
    is_confirmed bit not null,    
    primary key (username)
);

create table role (    
    role varchar(20) not null,    
    primary key (role)
);

create table user_role (
    username varchar(50) not null,
    role varchar(20) not null,
    foreign key(username) references user(username),
    foreign key(role) references role(role),
    primary key (username,role)
);

create table account (
    username varchar(20) not null,
    password varchar(20) not null,
    primary key (username)
);

create table post (
    id integer not null,
    creator_username varchar(50) not null,
    confirmer_username varchar(50) ,
    created_time datetime not null,
    release_time datetime,
    account_username varchar(50),
    post_status enum('unconfirmed','confirmed','rejected','posted','deleted') not null,
    is_edited bit not null,
    edit_note varchar(500),
    media_type enum('video','image') not null,
    media_url varchar(200),
    project_name_fa varchar(50),
    project_name_en varchar(50),
    code varchar(20),
    program_fa varchar(50),
    program_en varchar(50),
    location_fa varchar(50),
    location_en varchar(50),
    architect_fa varchar(50),
    architect_en varchar(50),
    year integer,
    size integer,
    project_status_fa varchar(20),
    project_status_en varchar(20),
    description_fa varchar(1000),
    description_en varchar(1000),
    keywords_fa varchar(200),
    keywords_en varchar(200),
    foreign key(creator_username) references user(username),
    foreign key(confirmer_username) references user(username),
    foreign key(account_username) references account(username),
    primary key (id)
);

-- create table post_stat (
--     post_id varchar(100) not null,
--     stat_time timestamp not null,
--     like_count integer,
--     view_count integer,
--     comment_count integer,
--     foreign key(post_id) references post(id),
--     primary key (post_id,stat_time)
-- ); 

insert into role values ('user');
insert into role values ('admin');
insert into user values ('admin','admin','admin','adminy',1);
insert into user_role values ('admin','admin');






