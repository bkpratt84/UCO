drop table post;

CREATE TABLE post (
    postId integer  NOT NULL AUTO_INCREMENT,
    parentId integer,
    authorId integer NOT NULL,
    category varchar(255),
    title varchar(255),
    content varchar(225) NOT NULL,
    views integer NOT NULL,
    active boolean NOT NULL,
    dateCreated date NOT NULL,
    datedModified date,
    modifiedBy int,
    primary key (postId)
);