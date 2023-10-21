insert into users (name, username, password, created_at)
values ('John Doe', 'johndoee@gmail.com', '$2a$10$Xl0yhvzLIaJCDdKBS0Lld.ksK7c2Zytg/ZKFdtIYYQUv8rUfvCR4W',
        '2023-01-01 00:00:00'),
       ('Mike Smith', 'mikesmitsh@yahoo.com', '$2a$10$fFLij9aYgaNCFPTL9WcA/uoCRukxnwf.vOQ8nrEEOskrCNmGsxY7m',
        '2023-01-11 00:00:00');

insert into tasks (title, user_id, description, status, expiration_date, created_at)
values ('Buy cheese', 2, null, 'TODO', '2023-01-29 12:00:00', '2023-01-25 00:00:00'),
       ('Do homework', 2, 'Math, Physics, Literature', 'IN_PROGRESS', '2023-01-31 00:00:00', '2023-04-02 00:00:00'),
       ('Clean rooms', 2, null, 'DONE', null, '2023-02-01 00:00:00'),
       ('Call Mike', 1, 'Ask about meeting', 'TODO', '2023-02-01 00:00:00', '2023-2-12 00:00:00');


insert into users_roles (user_id, role)
values (1, 'ROLE_ADMIN'),
       (1, 'ROLE_USER'),
       (2, 'ROLE_USER');