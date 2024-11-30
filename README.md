# java-filmorate
Template repository for Filmorate project.

Диаграмма БД

![schemeBD](src/main/resources/schemeBD/schemeBD.png)


Примеры запросов на получения 
(.\src\main\resources\sql_queries):
- Получить топ N наиболее популярных фильмов
  [GetTopFilm](src/main/resources/sql_queries/QueryGetTopFilm.txt)
- Получить список общих друзей с другим пользователем. 
paramUserId - входной параметр.
  [GetFriendsByUserId](src/main/resources/sql_queries/QueryGetFriendsByUserId.txt)