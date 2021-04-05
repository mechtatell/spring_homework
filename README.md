# Здравствуйте!

Хотел сделать все что можно, но физически не хватило времени на тесты, как - нибудь в другой раз)
Кратко по работе:
1. В качестве хранимой сущности решил использовать клиента с именем, возрастом и суммой
1. При отправке запроса на поиск, например **http://localhost:8080/lists/2/find?element="age":19,"name":"Ivan","sum":500** не указываются фигурные скобки,
так как постман ругается и выдает ошибку. Без них поиск нормально отрабатывает, к тому же только по введеным полям, то есть можно указать только имя или только сумму, ну или все вместе
1. Проверка на авторизацию, ну или получение пользователя, реализована через аргумент Principal user который сам биндится к запросу даже без аннотаций и я его пробрасываю дальше
в сервисы, где по логину нахожу пользователя в БД и работаю с ним. К сожалению, не смог присутствовать на последнем вебинаре и сделал как разобрался сам.
1. Решил полностью отказаться от DAO слоя, я понимаю зачем он нужен, но были ограниченные сроки и подгонять даошку под авторизацию было ужасно лень
1. Как и было сказано в задании, авторизированный пользователь имеет доступ только к листам, которые ему принадлежат, то есть биндятся при создании
1. В запросе **http://localhost:8080/lists/5/elements**, где добавляются сразу несколько клиентов, если не был найден список по такому айдишнику (или его просто нет или он принадлежит
другому юзеру), создается новый список, в него закидываются все клиенты, и список автоматически биндится к юзеру который делал запрос
1. При получении списка списков, выводятся только имена списков и количества клиентов в них, краткая информация

И под конец хотел выразить огромную благодарность за ваш труд, курсы были очень полезны и я узнал для себя очень много нового)
