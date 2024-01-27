create sequence account_seq start with 1 increment by 50;
create sequence game_transaction_seq start with 1 increment by 50;
create sequence user_seq start with 1 increment by 50;
create table account (balance float(53) not null, changed timestamp(6), id bigint not null, user_id bigint unique, version bigint, primary key (id));
create table game_transaction (bet float(53) not null, drawn integer not null, winnings float(53) not null, changed timestamp(6), child_id bigint unique, execution_time timestamp(6), id bigint not null, parent_id bigint unique, user_id bigint, version bigint, choice varchar(255) check (choice in ('SMALL','BIG')), status varchar(255) check (status in ('OPEN','SETTLED')), primary key (id));
create table user (changed timestamp(6), id bigint not null, version bigint, firstname varchar(255), lastname varchar(255), primary key (id));
alter table account add constraint FK7m8ru44m93ukyb61dfxw0apf6 foreign key (user_id) references user;
alter table game_transaction add constraint FKf0hkgguhdw3o4o9uifm40disl foreign key (child_id) references game_transaction;
alter table game_transaction add constraint FK7jd3vai3tf6a291tjnwg741e8 foreign key (parent_id) references game_transaction;
alter table game_transaction add constraint FKl8t3wkllo3cusxh3ghwo5y893 foreign key (user_id) references user;