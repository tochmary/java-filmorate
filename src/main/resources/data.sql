MERGE INTO PUBLIC.GENRES (ID, NAME) VALUES (1, 'Комедия');
MERGE INTO PUBLIC.GENRES (ID, NAME) VALUES (2, 'Драма');
MERGE INTO PUBLIC.GENRES (ID, NAME) VALUES (3, 'Мультфильм');
MERGE INTO PUBLIC.GENRES (ID, NAME) VALUES (4, 'Триллер');
MERGE INTO PUBLIC.GENRES (ID, NAME) VALUES (5, 'Документальный');
MERGE INTO PUBLIC.GENRES (ID, NAME) VALUES (6, 'Боевик');

MERGE INTO PUBLIC.MPA (ID, NAME, DESCRIPTION) VALUES (1, 'G', 'у фильма нет возрастных ограничений');
MERGE INTO PUBLIC.MPA (ID, NAME, DESCRIPTION) VALUES (2, 'PG', 'детям рекомендуется смотреть фильм с родителями');
MERGE INTO PUBLIC.MPA (ID, NAME, DESCRIPTION) VALUES (3, 'PG-13', 'детям до 13 лет просмотр не желателен');
MERGE INTO PUBLIC.MPA (ID, NAME, DESCRIPTION) VALUES (4, 'R', 'лицам до 17 лет просматривать фильм можно только в присутствии взрослого');
MERGE INTO PUBLIC.MPA (ID, NAME, DESCRIPTION) VALUES (5, 'NC-17', 'лицам до 18 лет просмотр запрещён');
