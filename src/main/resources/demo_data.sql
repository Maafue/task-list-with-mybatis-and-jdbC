insert into users (id, name, username, password)
values  (1, 'John Doe', 'johndoe@gmail.com', '$2$10$XLOyhvzLIJCDdKBSOLLd.ksK7c2Zytg/ZKFdtIYYQUv8rUfvCR4W'),
        (2, 'Mike Smith', 'mikesmith@yahoo.com', '$2a$10$fFLij9aYgaNCFPTL9WcA/uoCRukxnwf.v0Q8nrEE0skrCNmGsxY7m');

 insert into tasks (id, title, description, status, expiration_date)
 values (1, 'Buy cheese', null, 'TODO', '2023-01-29 12:00:00'),
        (2, 'Do homework', 'Math, Physics, Literature', 'IN_PROGRESS', '2023-01-31 00:00:00'),
        (3, 'Clean rooms', null, 'DONE', null),
        (4, 'Call Mike', 'Ask ablut meeting', 'TODO', '2023-02-01 00:00:00');

insert into users_tasks (task_id, user_id)
values  (1, 2),
        (2, 2),
        (3, 2),
        (4, 1);

insert into users_roles (user_id, role)
values  (1, 'ROLE_ADMIN'),
        (1, 'ROLE_USER'),
        (2, 'ROLE_USER');