drop table enroll;
drop table student;
drop table teaching;

create table student (
    student_id integer not null generated always as identity
                        (start with 1, increment by 1),
    username varchar(225),
    firstname varchar(225),
    lastname varchar(225),
    active varchar(225),
    primary key (student_id)
);

create table enroll (
    enrollID integer not null generated always as identity
                     
   (start with 1, increment by 1),
    
    studentID integer not null,
    courseID integer not null,
    primary key (enrollID)
);

create table teaching (
    teachID integer not null generated always as identity
                        (start with 1, increment by 1),
    facultyID integer not null,
    courseID integer not null,
    primary key (teachID)
);
    
INSERT INTO STUDENT (USERNAME, FIRSTNAME, LASTNAME,ACTIVE ) VALUES ( 'student', 'student', 'student','Y');

INSERT INTO ENROLL (STUDENTID, COURSEID) VALUES (1,1);
INSERT INTO ENROLL (STUDENTID, COURSEID) VALUES (1,2);
INSERT INTO ENROLL (STUDENTID, COURSEID) VALUES (1,3);

INSERT INTO TEACHING (FACULTYID, COURSEID) VALUES (3,2);
INSERT INTO TEACHING (FACULTYID, COURSEID) VALUES (3,3);
