# java-explore-with-me

https://github.com/axoryl/java-explore-with-me/pull/3

Приложение - афиша. Оно позволяет пользователям делиться информацией о интересных событиях и находить компанию для участия в них.
Стек: Java 11, Hibernate, Spring Boot, PostgreSQL, Maven, Swagger, Docker, Lombok.

Два сервиса
- основной сервис будет содержать всё необходимое для работы продукта;
- сервис статистики будет хранить количество просмотров и позволит делать различные выборки для анализа работы приложения.


Основной сервис
API основного сервиса разделено на три части:
- публичная будет доступна без регистрации любому пользователю сети;
- закрытая будет доступна только авторизованным пользователям;
- административная — для администраторов сервиса.
Аутентификация и авторизация не реализованы. Все запросы, поступающие на private и admin, считаются уже аутентифицированными и авторизованными.

Закрытая часть API:
- авторизованные пользователи имеют возможность добавлять в приложение новые мероприятия, редактировать их и просматривать после добавления;
- подача заявок на участие в интересующих мероприятиях;
- создатель мероприятия имеет возможность подтверждать заявки, которые отправили другие пользователи сервиса.

API для администратора
- добавление, изменение и удаление категорий для событий;
- возможность добавлять, удалять и закреплять на главной странице подборки мероприятий;
- модерация событий, размещённых пользователями, — публикация или отклонение;
- управление пользователями — добавление, активация, просмотр и удаление.

Модель данных
Жизненный цикл события включает несколько этапов.
Создание.
Ожидание публикации. В статус ожидания публикации событие переходит сразу после создания.
Публикация. В это состояние событие переводит администратор.
Отмена публикации. В это состояние событие переходит в двух случаях. Первый — если администратор решил, что его нельзя публиковать. Второй — когда инициатор события решил отменить его на этапе ожидания публикации.


Сервис статистики
Второй сервис — сервис статистики. Он собирает информацию. Во-первых, о количестве обращений пользователей к спискам событий и, во-вторых, о количестве запросов к подробной информации о событии. На основе этой информации  формируется статистика о работе приложения.

Функционал сервиса статистики:
- запись информации о том, что был обработан запрос к эндпоинту API;
- предоставление статистики за выбранные даты по выбранному эндпоинту.


Спецификация API
Для обоих сервисов есть подробные спецификации API /postman:
- спецификация основного сервиса: ewm-main-service-spec.json;
- спецификация сервиса статистики: ewm-stats-service.json.
- спецификация для дополнительной фичи (подписка на пользователей): feature.json.
