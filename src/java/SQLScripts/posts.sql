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


--Testing only
-- INSERT INTO post (parentID, authorID, category, title, content, views, active, dateCreated, fileCount) VALUES
--     (0, 1, 'News', 'Announcement 1', 'This is my content', 0, true, '2016-01-01', 0),
--     (1, 1, 'News', 'Comment 1', 'This is my content for comment 1', 0, true, '2016-01-02', 0),
--     (1, 1, 'News', 'Comment 2', 'This is my content for comment 2', 0, true, '2016-01-03', 0),
--     (0, 1, 'News', 'Announcement 2', 'This is my content', 0, true, '2016-01-04', 0);