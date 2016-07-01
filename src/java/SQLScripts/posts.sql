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
    dateModified date,
    modifiedBy int,
    primary key (postId)
);

INSERT INTO post (parentID, authorID, category, title, content, views, active, dateCreated) VALUES
    (NULL, 1, 'News', 'Announcement 1', 'This is my content', 1, true, '2015-01-01');