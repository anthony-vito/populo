create table users (
  user_name         varchar(100) not null primary key,
  user_pass         varchar(50) not null
);

create table user_roles (
  user_name         varchar(100) not null,
  role_name         varchar(100) not null,
  primary key (user_name, role_name)
);

create table roles_permissions (
  role_name         varchar(100) not null,
  permission         varchar(100) not null,
  primary key (role_name, permission)
);

insert into users values ('admin', 'admin');
insert into user_roles values ('admin', 'ADMIN');
insert into users values ('user', 'user');
insert into user_roles values ('user', 'USER');
insert into roles_permissions values ('ADMIN', 'level_9');
insert into roles_permissions values ('USER', 'level_1');