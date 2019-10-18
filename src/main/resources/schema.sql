drop table if exists ride_request;
drop table if exists user;
drop table if exists user_roles;

create table ride_request (
        id bigint not null,
        address varchar(255) not null,
        driver_id bigint,
        passenger_id bigint not null,
        requested_at datetime not null,
        responded_at datetime,
        version bigint,
        primary key (id)
);

create table user (
        username varchar(255) not null,
        password varchar(255) not null,
        primary key (username)
);

create table user_roles (
        user_username varchar(255) not null,
        roles integer
);
