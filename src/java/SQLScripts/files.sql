use cs_db;

DROP TABLE IF EXISTS FILES;

CREATE TABLE FILES
(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    postID INT,
    OWNER_ID INT,
    FILE_NAME VARCHAR(255),
    FILE_TYPE VARCHAR(255),
    FILE_SIZE BIGINT,
    FILE_CONTENTS LONGBLOB
);
