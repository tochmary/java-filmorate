--Создание таблицы USERS
create table IF NOT EXISTS USERS
(
    ID       INTEGER auto_increment,
    EMAIL    VARCHAR(100),
    LOGIN    VARCHAR(100) not null,
    NAME     VARCHAR(100),
    BIRTHDAY DATE,
    constraint USERS_PK
        primary key (ID)
);
--Создание таблицы FILMS
create table IF NOT EXISTS FILMS
(
    ID INTEGER auto_increment,
    NAME VARCHAR(300),
    DESCRIPTION VARCHAR(1000),
    RELEASE_DATE DATE,
    DURATION INTEGER,
    constraint FILMS_PK
    primary key (id)
);
--Создание таблицы FRIENDS
create table IF NOT EXISTS FRIENDS
(
    USER_ID INTEGER not null,
    FRIEND_ID INTEGER not null,
    constraint FRIENDS_PK
        unique (USER_ID, FRIEND_ID),
    constraint FRIENDS_USERS_ID_FK
        foreign key (USER_ID) references USERS,
    constraint FRIENDS_USERS_ID_FK_2
        foreign key (FRIEND_ID) references USERS
);
--Создание таблицы LIKES
create table IF NOT EXISTS LIKES
(
    FILM_ID INTEGER not null,
    USER_ID INTEGER not null,
    constraint LIKES_PK
    unique (FILM_ID, USER_ID),
    constraint LIKES_FILM_ID_FK
    foreign key (FILM_ID) references FILMS,
    constraint LIKES_USERS_ID_FK
    foreign key (USER_ID) references USERS
    );