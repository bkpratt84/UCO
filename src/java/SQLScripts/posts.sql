use cs_db;

DROP TABLE IF EXISTS post;

CREATE TABLE post (
    postId integer  NOT NULL AUTO_INCREMENT,
    parentId integer DEFAULT 0,
    authorId integer NOT NULL,
    category varchar(255),
    title varchar(255),
    content text NOT NULL,
    views integer NOT NULL,
    active boolean NOT NULL,
    dateCreated date NOT NULL,
    dateModified date,
    modifiedBy int,
    fileCount long NOT NULL,
    primary key (postId)
);

INSERT INTO post (parentID, authorID, category, title, content, views, active, dateCreated, fileCount) VALUES
    (0, 1, 'News', 'Announcement 1', 'This is my content', 0, true, '2016-01-01', 0),
    (1, 1, 'News', 'Comment 1', 'This is my content for comment 1', 0, true, '2016-01-02', 0),
    (1, 1, 'News', 'Comment 2', 'This is my content for comment 2', 0, true, '2016-01-03', 0),
    (0, 1, 'News', 'Announcement 2', 'This is my content', 0, true, '2016-01-04', 0);


-- DROP TRIGGER IF EXISTS trigFileCountOnInsert;
-- DROP TRIGGER IF EXISTS trigFileCountOnDelete;

-- DELIMITER $$
-- CREATE TRIGGER trigFileCountOnInsert AFTER INSERT ON FILES
--     FOR EACH ROW
--         BEGIN
--             UPDATE post set fileCount = (SELECT COUNT(*) FROM FILES WHERE postID = NEW.postID) WHERE postId = NEW.postID;
--         END;
-- 
-- $$
-- 
-- CREATE TRIGGER trigFileCountOnDelete AFTER DELETE ON FILES
--     FOR EACH ROW
--         BEGIN
--             UPDATE post set fileCount = (SELECT COUNT(*) FROM FILES WHERE postID = OLD.postID) WHERE postId = OLD.postID;
--         END;

--DELIMITER ;