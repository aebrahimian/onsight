drop database if exists onsight;
create database onsight;

USE mysql;
DROP USER IF EXISTS 'onsight_access'@'localhost';
CREATE USER 'onsight_access'@'localhost'  IDENTIFIED BY 'onsightpass';
GRANT ALL PRIVILEGES ON onsight.* TO 'onsight_access'@'localhost';

USE onsight;

create table user (
    username varchar(50) not null,
    password varchar(100) not null,
    name varchar(100),
    family varchar(100),
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

insert into role values ('user');
insert into role values ('admin');
insert into user values ('admin','admin','admin','adminy',1);
insert into user_role values ('admin','admin');

-- create table post (
--     id varchar(100) not null,
--     user_id varchar(20) not null,
--     type varchar(10),
--     created_time timestamp,
--     follower_count integer,
--     caption varchar(2500),
--     foreign key(user_id) references user(id),
--     primary key (id)
-- );

-- create table post_stat (
--     post_id varchar(100) not null,
--     stat_time timestamp not null,
--     like_count integer,
--     view_count integer,
--     comment_count integer,
--     foreign key(post_id) references post(id),
--     primary key (post_id,stat_time)
-- ); 






