drop table if exists category CASCADE;
drop table if exists lesson CASCADE;
drop table if exists lesson_aud CASCADE;
drop table if exists member CASCADE;
drop table if exists member_token CASCADE;
drop table if exists site CASCADE;
drop table if exists nickname CASCADE;
drop sequence if exists hibernate_sequence;
create sequence hibernate_sequence start 1 increment 1;

create table category (
    category_id bigint not null,
    category_lvl integer not null,
    name varchar(255) not null,
    root_category_id bigint,
    root_category_name varchar(255),
    primary key (category_id)
);

create table lesson (
   lesson_id bigint not null,
    reg_time timestamp,
    update_time timestamp,
    created_by varchar(255),
    modified_by varchar(255),
    alert_days varchar(255) not null,
    end_date date not null,
    is_finished boolean not null,
    message varchar(200),
    name varchar(150) not null,
    present_number integer not null,
    price integer not null,
    start_date date not null,
    total_number integer not null,
    category_id bigint,
    member_id bigint,
    site_id bigint,
    primary key (lesson_id)
);

create table lesson_aud (
    lesson_id bigint not null,
    rev integer not null,
    revtype tinyint,
    reg_time timestamp,
    update_time timestamp,
    created_by varchar(255),
    modified_by varchar(255),
    alert_days varchar(255),
    end_date date,
    is_finished boolean,
    lesson_name varchar(150),
    message varchar(200),
    present_number integer,
    price integer,
    start_date date,
    total_number integer,
    category_id bigint,
    member_id bigint,
    site_id bigint,
    primary key (lesson_id, rev)
);

create table member (
   member_id bigint not null
       generated by default as identity
       constraint member_pkey
           primary key,
    reg_time timestamp,
    update_time timestamp,
    created_by varchar(255),
    modified_by varchar(255),
    email varchar(255) not null,
    is_delete boolean not null,
    member_name varchar(255) not null,
    password varchar(255) not null,
    picture varchar(255),
    role varchar(255) not null,
    social_type varchar(255) not null,
    email_confirm_code varchar(255),
    is_email_confirm   boolean default true,
    email_confirm_code_created_at timestamp,
    primary key (member_id)
);

create table member_token (
   member_token_id bigint not null,
    reg_time timestamp,
    update_time timestamp,
    created_by varchar(255),
    modified_by varchar(255),
    refresh_token varchar(255),
    token_expiration_time timestamp,
    member_id bigint,
    member_token_type varchar(20),
    primary key (member_token_id)
);

create table site (
   site_id bigint not null,
    name varchar(100) not null,
    primary key (site_id)
);

create table nickname
(
    nickname_id bigint  not null
        constraint nickname_pkey
            primary key,
    is_used     boolean not null,
    name        varchar(50)
);


alter table member
add constraint uk_mbmcqelty0fbrvxp1q58dn57t unique (email)
;

alter table lesson
add constraint FK5fh9e98erf9mfa9udc11vlxtr
foreign key (category_id)
references category
;

alter table lesson
add constraint FKkx2caj5sqlomiff494dug8jio
foreign key (member_id)
references member
;

alter table lesson
add constraint FK62tlmbxrjw0o0jw79s3bbheke
foreign key (site_id)
references site
;

alter table member_token
add constraint FKt02uutgl1v2am5mshqqdk1cvd
foreign key (member_id)
references member
;



