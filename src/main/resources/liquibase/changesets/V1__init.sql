create table if not exists users
(
    id         bigserial primary key,
    name       varchar(255) not null,
    username   varchar(255) not null unique,
    password   varchar(255) not null,
    created_at timestamp    not null
);

create table if not exists tasks
(
    id              bigserial primary key,
    user_id         bigint,
    title           varchar(255) not null,
    description     varchar(255) null,
    status          varchar(255) not null,
    expiration_date timestamp    null,
    created_at      timestamp    not null,
    constraint fk_tasks_users foreign key (user_id) references users (id) on delete cascade on update no action
);

create table if not exists users_roles
(
    user_id bigint       not null,
    role    varchar(255) not null,
    primary key (user_id, role),
    constraint fk_users_roles_users foreign key (user_id) references users (id) on delete cascade on update no action
);