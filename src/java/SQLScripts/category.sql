use cs_db;

-- SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS Category;
-- SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE Category (
    categoryID integer  NOT NULL AUTO_INCREMENT PRIMARY KEY,
    category varchar(25) NOT NULL,
    colorCode varchar(6) NOT NULL,
    inactive boolean NOT NULL
);

INSERT INTO Category (category, colorCode, inactive) VALUES
    ('General', '0099ff', false),
    ('Important', 'ff0000', false);