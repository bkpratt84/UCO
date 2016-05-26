DROP TABLE FILES;

CREATE TABLE FILES
(
id INT Not NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
OWNER_USERNAME VARCHAR(255),
FILE_NAME VARCHAR(255),
FILE_TYPE VARCHAR(255),
FILE_SIZE BIGINT,
FILE_CONTENTS BLOB);
