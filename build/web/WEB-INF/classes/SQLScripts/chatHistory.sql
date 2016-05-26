
drop table chat_history;

create table chat_history (
    msg_id integer primary key 
        generated always as identity 
        (start with 1, increment by 1),

    local_id       varchar(255) not null,
    local_username varchar(255) not null,
    remote_username varchar(255) not null,
    remote_id      varchar(255) not null,
    message        long varchar
);