--Создание таблицы пользователей
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
--Создание таблицы справочника рейтинга Ассоциации кинокомпаний (англ. Motion Picture Association, сокращённо МРА)
create table IF NOT EXISTS MPA
(
    ID   INTEGER auto_increment,
    NAME VARCHAR(10) not null,
    DESCRIPTION VARCHAR(100),
    constraint MPA_PK
        primary key (ID)
);
--Создание таблицы фильмов
create table IF NOT EXISTS FILMS
(
    ID INTEGER auto_increment,
    NAME VARCHAR(300),
    DESCRIPTION VARCHAR(1000),
    RELEASE_DATE DATE,
    DURATION INTEGER,
    MPA_ID       INTEGER,
    constraint FILMS_PK
    primary key (id),
    constraint FILMS_MPA_ID_FK
        foreign key (MPA_ID) references MPA
);
--Создание таблицы FRIENDS
create table IF NOT EXISTS FRIENDS
(
    USER_ID INTEGER not null,
    FRIEND_ID INTEGER not null,
    IS_CONFIRMED BOOLEAN default FALSE,
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
--Создание таблицы справочника жанров
create table IF NOT EXISTS GENRES
(
    ID   INTEGER auto_increment,
    NAME VARCHAR(50) not null,
    constraint GENRES_PK
        primary key (ID)
);
--Создание таблицы связей фильмов с жанрами
create table IF NOT EXISTS FILM_GENRES
(
    FILM_ID  INTEGER,
    GENRE_ID INTEGER,
    constraint FILM_GENRES_PK
        primary key (FILM_ID, GENRE_ID),
    constraint FILM_GENRES_FILMS_ID_FK
        foreign key (FILM_ID) references FILMS,
    constraint FILM_GENRES_GENRES_ID_FK
        foreign key (GENRE_ID) references GENRES
);
