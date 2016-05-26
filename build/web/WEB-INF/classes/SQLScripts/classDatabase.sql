drop table classes;
drop table classroll;

create table classes(
    id integer not null generated always as identity
                    (start with 1, increment by 1),
    crn varchar(255),
    subject varchar(255),
    course varchar(255),
    title varchar(255),
    days varchar(255),
    times varchar(255),
    capacity integer,
    instructor varchar(255),
    primary key(id)
);

create table classroll(
    id integer not null generated always as identity
                    (start with 1, increment by 1),
    crn varchar(255),
    userid integer,
    primary key(id)
);

INSERT INTO CLASSES (CRN, SUBJECT, COURSE, TITLE, DAYS, TIMES, CAPACITY, INSTRUCTOR)
    VALUES ('22173', 'CMSC', '3613', 'Data Structures', 'MW', '07:30pm-8:45pm', 40, 'Myung-Ah Park');
INSERT INTO CLASSES (CRN, SUBJECT, COURSE, TITLE, DAYS, TIMES, CAPACITY, INSTRUCTOR)
    VALUES ('21970', 'CMSC', '4283', 'Software Engineering', 'TR', '07:30pm-8:45pm', 32, 'Jicheng Fu');
INSERT INTO CLASSES (CRN, SUBJECT, COURSE, TITLE, DAYS, TIMES, CAPACITY, INSTRUCTOR)
    VALUES ('21256', 'CMSC', '2613', 'Programming 2', 'MWF', '10:00am-10:50pm', 30, 'Thomas R. Turner');
INSERT INTO CLASSES (CRN, SUBJECT, COURSE, TITLE, DAYS, TIMES, CAPACITY, INSTRUCTOR)
    VALUES ('21784', 'CMSC', '2123', 'Discrete Structures', 'MW', '04:15pm-5:30pm', 30, 'William F Stockwell');
INSERT INTO CLASSES (CRN, SUBJECT, COURSE, TITLE, DAYS, TIMES, CAPACITY, INSTRUCTOR)
    VALUES ('22167', 'CMSC', '2413', 'Visual Programming', 'TR', '02:45pm-4:00pm', 34, 'Micheal Gourley');
INSERT INTO CLASSES (CRN, SUBJECT, COURSE, TITLE, DAYS, TIMES, CAPACITY, INSTRUCTOR)
    VALUES ('21572', 'CMSC', '2833', 'Computer Organization I', 'TR', '04:15pm-5:30pm', 40, 'Thomas R Turner');
INSERT INTO CLASSES (CRN, SUBJECT, COURSE, TITLE, DAYS, TIMES, CAPACITY, INSTRUCTOR)
    VALUES ('21257', 'CMSC', '4063', 'Networks', 'MW', '05:45pm-7:00pm', 30, 'John William McDaniel');
INSERT INTO CLASSES (CRN, SUBJECT, COURSE, TITLE, DAYS, TIMES, CAPACITY, INSTRUCTOR)
    VALUES ('21571', 'CMSC', '4153', 'Operating Systems', 'MW', '04:15pm-5:30pm', 33, 'John William McDaniel');
INSERT INTO CLASSES (CRN, SUBJECT, COURSE, TITLE, DAYS, TIMES, CAPACITY, INSTRUCTOR)
    VALUES ('21357', 'CMSC', '4373', 'Web Server Programming', 'TR', '02:45pm-4:00pm', 30, 'Hong Ki Sung');
INSERT INTO CLASSES (CRN, SUBJECT, COURSE, TITLE, DAYS, TIMES, CAPACITY, INSTRUCTOR)
    VALUES ('21570', 'CMSC', '4003', 'Applied Database Management', 'TR', '04:15pm-5:30pm', 35, 'Gang Qian');
