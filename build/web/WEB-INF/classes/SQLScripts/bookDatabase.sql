drop table books;

create table books(
    id integer not null generated always as identity
                    (start with 1, increment by 1),
    crn varchar(255),
    coursename varchar(255),
    title varchar(255),
    edition varchar (255),
    isbn varchar (255),
    required boolean,
    primary key(id)
);

INSERT INTO BOOKS (CRN, COURSENAME, TITLE, EDITION, ISBN, REQUIRED)
    VALUES ('22173', 'Data Structures', 'Data Structures+Program Design in c++', '2', '9780137689958',  'true'); 
INSERT INTO BOOKS (CRN, COURSENAME, TITLE, EDITION, ISBN, REQUIRED)
    VALUES ('21970', 'Software Engineering', 'Object-Oriented Software Engineering', '1', '9780136061250', 'false');
INSERT INTO BOOKS (CRN, COURSENAME, TITLE, EDITION, ISBN, REQUIRED)
    VALUES ('21256', 'Programming 2', 'Big C++', '2',  '9780470383285', 'false');
INSERT INTO BOOKS (CRN, COURSENAME, TITLE, EDITION, ISBN, REQUIRED)
    VALUES ('21784', 'Discrete Structures', 'Discrete math+Its Applications', '7', '9780073383095', 'true');
INSERT INTO BOOKS (CRN, COURSENAME, TITLE, EDITION, ISBN, REQUIRED)
    VALUES ('22167', 'Visual Programming', 'Starting Out W/Vis.Basic', '6', '9780133128086', 'false');
INSERT INTO BOOKS (CRN, COURSENAME, TITLE, EDITION, ISBN, REQUIRED)
    VALUES ('21572', 'Essentials Of Computer Organization', 'Computer Organization 1', '4', '9781284045611', 'true');
INSERT INTO BOOKS (CRN, COURSENAME, TITLE, EDITION, ISBN, REQUIRED)
    VALUES ('21257', 'Networks', 'Computer Networks', '4',  '9780130661029', 'false');
INSERT INTO BOOKS (CRN, COURSENAME, TITLE, EDITION, ISBN, REQUIRED)
    VALUES ('21571', 'Operating Systems', 'Operating System Concepts', '7', '9780471694663', 'true');
INSERT INTO BOOKS (CRN, COURSENAME, TITLE, EDITION, ISBN, REQUIRED)
    VALUES ('21357', 'Web Server Programming', 'Mastering JavaServer Faces 2.2', '1', '9781782176466', 'true');
INSERT INTO BOOKS (CRN, COURSENAME, TITLE, EDITION, ISBN, REQUIRED)
    VALUES ('21570', 'Appl Database Management', 'Fund. Of Database Systems', '6', '9780136086208', 'false');