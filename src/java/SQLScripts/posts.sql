use cs_db;

DROP TABLE IF EXISTS post;

CREATE TABLE post (
    postId integer  NOT NULL AUTO_INCREMENT,
    parentId integer DEFAULT 0,
    authorId integer NOT NULL,
    categoryId integer,
    title varchar(255),
    content text NOT NULL,
    views integer NOT NULL,
    active boolean NOT NULL,
    dateCreated datetime NOT NULL,
    dateModified datetime,
    modifiedBy int,
    fileCount long NOT NULL,
    primary key (postId)
);