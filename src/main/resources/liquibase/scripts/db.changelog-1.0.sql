--liquibase formatted sql

--changeset viktor-vv:20240728-1 failOnError:true
--comment: Create users table.
--preconditions onFail:HALT onError:HALT
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name = 'users';
create table if not exists users
(
    id       BIGINT generated by default as identity PRIMARY KEY,
    username VARCHAR(32)  NOT NULL,
    email    VARCHAR(32)  NOT NULL unique,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(16)  NOT NULL
);