use cs_db;

CREATE TABLE `user_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `firstName` varchar(255) DEFAULT NULL,
  `lastName` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `active` tinyint(1) DEFAULT NULL,
  `activationKey` varchar(6) DEFAULT NULL,
  `subscribeToAnnouncements` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
)

create table user_groups (
    id integer not null auto_increment,
    groupname varchar(255),
    username varchar(255),
    primary key (id)
);

CREATE TABLE DEGREES(
    DEGREE_ID INTEGER NOT NULL auto_increment,
    DEGREE_NAME VARCHAR(255),
    DEGREE_DESC VARCHAR(1000),
    DEGREE_CODE VARCHAR(64),
    PRIMARY KEY(DEGREE_ID)
);

CREATE TABLE FACULTYADVISEMENT(
    ID INTEGER NOT NULL auto_increment,
    TITLE VARCHAR(255),
    CONTENT VARCHAR(2000),
    PRIMARY KEY(ID)
);

INSERT INTO FACULTYADVISEMENT (TITLE, CONTENT) VALUES('Current Students',
'<ul>
<li>
  CS (6100) and SE (6110) students: 
  degree advisement is required every semester before class enrollment
</li>
<li>
  CS-Applied (6101) and CS-Information Science (6102) students: advisement is not
  required, but students are strongly encouraged to meet with the advisor before class enrollment.
</li>
<li>
  The Advisement Period for Class Enrollment:
  <ul>
    <li> For Fall/Summer classes: During the prior Spring semester after the spring break through
         the end of the Spring semester.
    </li>
    <li> For Spring classes: During the prior Fall semester after the fall break through
         the end of the Fall semester.
    </li>
    <li> Sign up <a href="http://www.signupgenius.com/go/60b0d4ba8ae2fa02-csacademic">
         HERE</a> for advisement. The link is open only during the advisement period.
    </li>
    <li> Bring in the printed copy of this form <a href="/Home4/files/Enroll.docx">
         Enroll.docx</a> to the advisement.
    </li>
  </ul>
</li>
</ul>');

INSERT INTO FACULTYADVISEMENT (TITLE, CONTENT) VALUES(
'New/Transfer/Exchange Students',
'Meet with the faculty advisor, Dr. Sung, during or before the first week of the first semester at UCO.');

INSERT INTO FACULTYADVISEMENT (TITLE, CONTENT) VALUES(
'Graduate Students',
'Meet with the faculty advisor, Dr. Sung, every semester before class enrollment to update the plan of study.');

INSERT INTO FACULTYADVISEMENT (TITLE, CONTENT) VALUES(
'Future Students','Contact the faculty advisor for any academic questions of CS degree.
                                        For general admission related questions, 
                                        contact the Admission''s Office (undergraduate students) or 
                                        Office of Global Affairs (international students) or
                                        the Jackson College of Graduate Studies (graduate students).');

CREATE TABLE NEWSITEM(
    ID INTEGER NOT NULL auto_increment,
    TITLE VARCHAR(255),
    CONTENT VARCHAR(1000),
    DATEPUBLISHED TIMESTAMP,

    PRIMARY KEY(ID)
);

INSERT INTO NEWSITEM (TITLE, CONTENT) VALUES('Facebook Group Open','<p>
                            News, jobs or internship announcements are currently posted
                            at the official Facebook group of the Computer Science Department.
                        </p>
                        <p>
                            Join the Facebook group to stay current with the news from the Department: 
                            <a href="https://www.facebook.com/groups/cs.uco/">UCO CS Students</a>
                        </p>
                        <p>
                            UCO CS Students is a closed group for the current UCO students
                            majoring in computer science or software engineering degrees.
                        </p>');


CREATE TABLE FACULTY(
    FACULTY_ID INTEGER NOT NULL auto_increment,
    NAMEPREFIX VARCHAR(64),
    FIRSTNAME VARCHAR(64),
    LASTNAME VARCHAR(64),
    JOBTITLE VARCHAR(128),
    EMAIL VARCHAR(255),
    WEBSITE VARCHAR(255),
    OFFICE VARCHAR(64),
    PHONE VARCHAR(64),
    # FOREIGN KEY(FACULTY_ID) REFERENCES USER_INFO(ID),
    STATUS VARCHAR(64),
    PRIMARY KEY (FACULTY_ID)
);


--In it's own script file
-- CREATE TABLE post (
--     postId integer  NOT NULL AUTO_INCREMENT,
--     parentId integer,
--     authorId integer NOT NULL,
--     category varchar(255),
--     title varchar(255),
--     content varchar(225) NOT NULL,
--     views integer NOT NULL,
--     active boolean NOT NULL,
--     dateCreated date NOT NULL,
--     datedModified date,
--     modifiedBy int,
--     primary key (postId)
-- );

CREATE TABLE RESOURCE(
    RESOURCEID INTEGER NOT NULL auto_increment,
    TITLE VARCHAR(64),
    PRIMARY KEY (RESOURCEID)
);


CREATE TABLE RESOURCEITEM(
    ITEMID INTEGER NOT NULL auto_increment,
    RESOURCEID INTEGER,
    TITLE VARCHAR(64),
    CONTENTS VARCHAR(255),
    PRIMARY KEY (ITEMID),
    FOREIGN KEY (RESOURCEID) REFERENCES RESOURCE(RESOURCEID)
);

INSERT INTO RESOURCE (TITLE) VALUES('Scholarships');
INSERT INTO RESOURCE (TITLE) VALUES('Work Study Opportunities in the Department');

INSERT INTO RESOURCEITEM (TITLE, CONTENTS, RESOURCEID) VALUES(
'Tuition Waiver',
'Applications are accepted during the spring semester. Look for announcements.
Available to Oklahoma resident students only.', 
1);

INSERT INTO RESOURCEITEM (TITLE, CONTENTS, RESOURCEID) VALUES(
'John Taylor Beresford Endowed Scholarship',
'Apply online at http://www.uco.edu/cms/development/scholarship.asp<br/>
Applications are accepted annualy and due is normally on February.
Available to residents, non-residents, and international students.', 
1);

INSERT INTO RESOURCEITEM (TITLE, CONTENTS, RESOURCEID) VALUES(
'Jan Douglas Scholarship',
'Apply online at http://www.uco.edu/cms/development/scholarship.asp<br/>
Applications are accepted annualy and due is normally on February.
Available to residents, non-residents, and international students.', 
1);

INSERT INTO RESOURCEITEM (TITLE, CONTENTS, RESOURCEID) VALUES(
'Computer Science Scholarship',
'Apply online at http://www.uco.edu/cms/development/scholarship.asp<br/>
Applications are accepted annualy and due is normally on February.
Available to residents, non-residents, and international students.', 
1);

INSERT INTO RESOURCEITEM (TITLE, CONTENTS, RESOURCEID) VALUES(
'Tutor',
'Applications are accepted every semester. 
Requirements: Completion of CMSC 3613 with an excellent grade.
Contact the administrative assistant for the application', 
2);

INSERT INTO RESOURCEITEM (TITLE, CONTENTS, RESOURCEID) VALUES(
'Grader',
'Some instructors hire graders for their classes.
Contact instructors if they are hiring graders.
Requirements: Completion of the corresponding course with an excellent grade', 
2);

INSERT INTO RESOURCEITEM (TITLE, CONTENTS, RESOURCEID) VALUES(
'Lab Monitor',
'Applications are accepted every semester. 
Requirements: computer science or software engineering students.
Contact the administrative assistant for the application', 
2);




INSERT INTO DEGREES (DEGREE_NAME, DEGREE_DESC, DEGREE_CODE) VALUES('B.S. in Computer Science','We suggest the Computer Science program of study for those
                                        who are interested in Computer Science in general.
                                        This program is accredited by the Computing Accreditation
                                        Commission of ABET, www.abet.org','6100');
INSERT INTO DEGREES (DEGREE_NAME, DEGREE_DESC, DEGREE_CODE) VALUES('B.S. in Computer Science - Applied','We also offer the Computer Science - Applied 
                                        degree program for students who have an interest
                                        in a related discipline. The Department recognizes
                                        that computer professionals write applications for others 
                                        and this degree program gives the student an opportunity to
                                        select a discipline of his or her interest.','6101');
INSERT INTO DEGREES (DEGREE_NAME, DEGREE_DESC, DEGREE_CODE) VALUES('B.S. in Computer Science - Information Science','The Computer Science - Information Science is a program of 
                                        study that will appeal to the student who has a specific 
                                        interest in business applications. The Department recognizes
                                        computers have been employed to benefit worldwide commerce
                                        and business for many years. The University of Central
                                        Oklahoma has a particularly fine College of Business 
                                        whose faculty members greatly assist in this degree program.','6102');
INSERT INTO DEGREES (DEGREE_NAME, DEGREE_DESC, DEGREE_CODE) VALUES('B.S. in Software Engineering','We suggest the Software Engineering program of study for 
                                            those who are interested in software engineering in general. 
                                            Software engineering is the study and application of 
                                            engineering to the design, development, and maintenance of software.','6110');
INSERT INTO DEGREES (DEGREE_NAME, DEGREE_DESC, DEGREE_CODE) VALUES('M.S. in Applied Mathematics and Computer Science','This graduate degree is jointly offered with the Department of
                                            Mathematics and Statistics. This degree is designed to prepare students to meet
                                            the demands of industry, business, and government for individuals
                                            with expertise in advanced applications of mathematics and 
                                            computer science. While ensuring sound mathematical training, 
                                            the degree program also concentrates on widely applicable 
                                            computer science principles and provides further development
                                            of problem-solving skills.','6660');

CREATE TABLE COURSES(
    COURSE_ID INTEGER NOT NULL auto_increment,
    COURSE_CRN VARCHAR(255),
    COURSE_NAME VARCHAR(255),
    COURSE_DESC VARCHAR(1000),
    COURSE_PREREQ VARCHAR(1000),
    PRIMARY KEY(COURSE_ID)
);

INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'CMSC 1053',
'Professional Computer Applications and Problem Solving',
'This course provides a hands-on introduction to current 
                        professional computer applications such as word processing, 
                        spreadsheets, Web authoring and presentation. 
                        Essential concepts of computer hardware, software, 
                        network and security issues are covered. 
                        Special attention is devoted toward problem 
                        solving using software applications in both 
                        personal and workplace computing environments. 
                        Legal and ethical issues related to the use 
                        of computers are also addressed.',
'None'
);
INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'CMSC 1103','Intro to Computing Systems',
' This course includes the history of computers, microcomputers, 
                        stored program principles, hardware organization, number systems, 
                        types and uses of computers, programs and programming, 
                        terminology of the trade and an introduction to time-sharing.',
'Two years high school algebra'
);
INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'CMSC 1513',
'Beginning Programming',
'This course includes an introduction to programming concepts, 
                        problem identification and problem solving techniques. 
                        A specific computer language will be used for the implementation 
                        of the problem solving process, and programming assignments will 
                        be given so the student can demonstrate mastery of the language 
                        and the problem solving techniques used. 
                        Java language is currently used in all sections. ',
'Two years high school algebra');

INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'CMSC 1521',
'Beginning Programming Lab',
'This is a laboratory for CMSC 1513 Beginning Programming. 
                        It allows students to practice basic programming techniques 
                        using a specific computer language.',
'CMSC 1513 or concurrent enrollment');

INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'CMSC 1613',
'Programming I',
'Programming I introduces basic computer programming language
                        constructs. 
                        Scalar and aggregate data types are discussed. 
                        Expressions, assignment, selection and iteration statements, 
                        and subprograms are presented. C++ language is currently used.',
'(MATH 1513 or 1555) and (CMSC 1513 or Advanced Placement high school programming course)');

INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'CMSC 1621',
'Programming I Lab',
'This is a laboratory for CMSC 1613 Programming I. 
                        It allows students to practice programming with basic 
                        computer language constructs.',
'CMSC 1613 or concurrent enrollment');

INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'CMSC 2123',
'Discrete Structures',
'Discrete Structures introduces the theoretical foundation 
                        for the discipline of computer science and its application 
                        to computing.',
'CMSC 1613 AND MATH 2313');

INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'CMSC 2413',
'Visual Programming',
'This course is an introduction to graphical user interfaces, 
                        event driven programming and windows on screen objects 
                        such as command buttons, text boxes, option buttons 
                        and graphics. Programming projects will require students 
                        to design interactive screens as well as code subroutines 
                        to implement the programs. VB.NET is currently used.',
'CMSC 1513');

INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'CMSC 2613', 'Programming II',
'Programming II introduces students to common programming 
                        components including stacks, queues, lists, and trees. 
                        Time complexity and algorithm analysis are discussed and 
                        applied to sorting algorithms. C++ language is currently used.',
'CMSC 1613');

INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'CMSC 2621', 'Programming II Lab',
'This is a laboratory for CMSC 2613 Programming II. 
                        It allows students to practice with common programming 
                        components and algorithms.',
'CMSC 2613 or concurrent enrollment');

INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'CMSC 2833', 'Computer Organization I',
'The study of computer organization will include digital logic 
                        and digital systems, machine level representation of data and 
                        instructions, assembly language level machine organization, 
                        and memory system organization.',
'CMSC 1613');

INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'CMSC 3000', 'Workshop In Computer Science',
'Credit will vary from 1 to 6 hours. 
                        Subject matter will vary within the department’s field of study. 
                        Normally involves lecture, films, guest speaker, etc. 
                        A grade of “P” or “F” is given. 
                        No more than 6 hours of workshop may be counted toward a bachelor’s degree.',
'None');

INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'SE 3103', 'Object Oriented SW Design and Construct',
'This course introduces principles and practices of 
                        object oriented software design and implementation. 
                        Also introduced are concepts of design patterns. 
                        Java language is currently used.',
'CMSC 2613');

INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'CMSC 3303', 'Systems Analysis and Design',
'This course examines the spectrum of requirements for the design, 
                        planning, and implementation of computer systems. 
                        Through case studies, students will analyze existing 
                        situations in order to propose new systems solutions. 
                        Credit may not be earned for both CMSC 3303 and CMSC 4283.',
'CMSC 2413 or 2613');

INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'CMSC 3413', 'Enterprise Programming',
'This course introduces enterprise programming concepts including building and using classes, 
 database access, client/server systems, web forms, Windows forms, and security features.',
'CMSC 2613');

INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'CMSC 3613', 'Data Structures and Algorithms',
'This course is a continuation of Programming II and 
                        is a study of more efficient algorithms for storing and 
                        retrieving information. 
                        The theory and application of graphs are presented. 
                        Time and space complexity analysis are performed on all algorithms. 
                        C++ language is currently used.',
'CMSC 2123 and 2613 and MATH 2323 and (STAT 2103 or 2113 or 4113)');

INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'CMSC 3621', 'Data Structures and Algorithms Lab',
'This is a laboratory for CMSC 3613 Data Structures and Algorithms. 
                        It allows students to practice programming with efficient 
                        algorithms for storing and retrieving information.',
'CMSC 3613 or concurrent enrollment');

INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'CMSC 3833', 'Computer Organization II',
'The study of computer organization will be a continuation 
                        of Computer Organization I and will include memory system architecture, 
                        interfacing and communication of computer elements, 
                        functional organization, multiprocessing and 
                        alternate architectures, and performance enhancements.',
'CMSC 2833');

INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'CMSC 4003', 'Applications of Database Management',
'This course covers the integration of theory and practice 
                        in the use of current database systems and the access of those systems. 
                        The course covers the relational and E-R models for database organization. 
                        Topics presented in this course include query languages (e.g. SQL), 
                        normalization, database integrity and security, 
                        file access methods, query processing, transaction processing, 
                        and backup/recovery. 
                        Credit may not be earned for both CMSC 4003 and ISOM 4263.',
'CMSC 2613 and MATH 2313 and (STAT 2103 or 2113 or 4113)');

INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'CMSC 4023', 'Programming Languages',
'The course provides a study of the underlying concepts of 
                        programming languages such as automata, grammars, 
                        translation, bindings, scope, data types, control, 
                        subprogramming, concurrency, and exception handling. 
                        Languages representing contrasting paradigms are studied.',
'CMSC 3613');

INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'CMSC 4063', 'Networks',
'The course is a study of local and wide area networks and 
                        their implementations. Included is the theory governing 
                        layered network architectures, the ISO-OSI communications 
                        interface, the TCP/IP protocol, packet transmission, 
                        error-correction techniques, addressing and routing, 
                        and the use of existing communications software.',
'CMSC 3613');

INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'CMSC 4133', 'Concepts of Artificial Intelligence',
'This course is a study of the basic concepts and techniques 
                        of artificial intelligence or intelligent systems. 
                        Some of the topics covered are search techniques, 
                        heuristics, expert systems, systems of logical inference, 
                        methods of representing knowledge, and AI programming.',
'CMSC 3613');

INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'CMSC 4153', 'Operating Systems',
'This course is a study of operating system theory. 
                        Topics include process management, mutual exclusion 
                        between concurrent processes, process deadlock, 
                        scheduling strategies, management of real, virtual, 
                        and external memories, parallel processing, and network systems.',
'CMSC 3613');

INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'CMSC 4173', 'Translator Design',
'Translator Design introduces the principles, tools and 
                        techniques used to design a programming language compiler. 
                        Topics covered include lexical, syntax, and semantic analysis, 
                        finite automata, regular expressions, LL and LR grammars, 
                        type systems and checkers, code generation, interpreters, 
                        optimization and code improvement.',
'CMSC 3613');

INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'CMSC 4273', 'Theory Of Computing',
'Theory of Computing is a study of computation theory 
                        encompassing three broad categories: formal languages 
                        and automata theory, computability theory, and complexity 
                        theory. The topics covered include Turing machines, 
                        finite automata, nondeterminism, pushdown automata, 
                        decidability, and NP-completeness. ',
'CMSC 3613');

INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'SE 4283', 'Software Engineering I',
'This course covers the application of engineering and 
                        management disciplines to computer software projects. 
                        Topics discussed are the software lifecycle, 
                        CASE tools, requirement engineering, software models 
                        and architectures, software design and development, 
                        testing and validation, maintenance and evolution, 
                        project organization, management and cost estimation, 
                        and software quality assurance and risk analysis. 
                        Credit may not be earned for both CMSC 3303 and CMSC 4283.
Students are encouraged to take this course after CMSC 3103',
'CMSC 2613 and MATH 2313 and (STAT 2103 or 2113 or 4113)');

INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'CMSC 4303', 'Mobile Application Programming',
'Theory and practice of mobile application programming are 
                        studied, which includes the study of mobile computing platforms, 
                        mobile user interfaces, animation, graphics, and 
                        the use of the media framework and telephony APIs. 
                        Extensive practice on a specific mobile development 
                        platform is included. Android platform is currently used.',
'CMSC 3103');

INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'CMSC 4323', 'Computer and Network Security',
'This course examines principles of computer and network 
                        security. Topics include security principles, 
                        application security, web security, cryptography and 
                        its applications, network security, and privacy issues. 
                        Students will learn practical knowledge and skills to 
                        identify and defend against security threats.',
'CMSC 3613');

INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'CMSC 4373', 'Web Server Programming',
'Theory and practice of web server-side programming, 
                        including materials on presentation logic, business logic, 
                        session control, and database management are studied. 
                        Extensive practice in a specific server-side programming 
                        language is included. JavaServer Faces (JSF) is currently used.
Students are encouraged to take this course after CMSC 4003',
'CMSC 3103');

INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'CMSC 4383', 'File Structures',
'File structures is a study of the physical characteristics 
                        of direct storage devices and the data structures that 
                        provide for efficient storage and access of data. 
                        It includes analysis of the efficiency of the access 
                        methods with respect to the time and space requirements.',
'CMSC 3613');

INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'CMSC 4401', 'Ethics in Computing',
'Ethics in Computing is a study of social, ethical, and 
                        professional issues related to computing.',
'CMSC 3613');

INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'SE 4423', 'Software Engineering II',
'This course covers all aspects of software engineering 
                        with emphasis on requirements elicitation and analysis, 
                        software testing, and project management.',
'CMSC 4283');

INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'SE 4433', 'Software Architecture and Design',
'This course covers software design with emphasis on architectural 
                        design, reuse of software architectures, and patterns.',
'CMSC 4283');

INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'CMSC 4513', 'Software Design and Development',
'Software Design and Development is the capstone course 
                        in computer science. Students have an opportunity to 
                        demonstrate and integrate skills acquired in their program 
                        of study to a project. Oral and written presentation of 
                        project concepts is emphasized. 
                        Students are required to complete the department’s assessment instrument.',
'CMSC 4003 and (4283 or 3303). 
 Instructor permission is required.
This is the capstone course for all computer science majors.
Students should take this course in their last semester prior to graduation.');

INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'SE 4513', 'Software Engineering Senior Project',
'In this capstone course, students will have the opportunity 
                        to demonstrate and integrate skills acquired in their program 
                        of study to a project. Oral and written presentation of 
                        project concepts is emphasized. Students will be required 
                        to complete the department’s assessment instrument.',
'CMSC 4003 and CMSC 4283 and (concurrent enrollment with SE 4423 and SE 4433). Instructor permission is required.
Students should take this course in their last semester prior to graduation.');

INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'CMSC 4910', 'Seminar In Computer Science',
'Credit will vary from 1 to 4 hours. 
                        Subject matter will vary within the department’s field of study.',
'CMSC 3613');

INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'CMSC 4930', 'Individual Study',
'Credit will vary from 1 to 4 hours. 
                        Directed study in various problem areas and applications of 
                        computers and the computer sciences. May include working 
                        in a computer installation.',
'9 hours of computer science and written permission of instructor');

INSERT INTO COURSES (COURSE_CRN, COURSE_NAME, COURSE_DESC, COURSE_PREREQ) VALUES(
'CMSC 4950', 'Internship In Computer Science',
'Credit will vary from 1 to 8 hours. 
                        Students may claim academic credits by enrolling in this course
                        while working as intern. Contact Internship Coordinator for details.',
'CMSC 3613. Internship Coordinator: Ms. Dawn Holt');

insert into user_info (username, password, email, active)
    values ('admin',
        '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918',
        'admin', 1);

insert into user_groups (groupname, username) values ('admin', 'admin');

insert into user_info (username, password, email, active)
    values ('student',
        '264c8c381bf16c982a4e59b0dd4c6f7808c51a05f64c35db42cc78a2a72875bb',
        'student', 1);

insert into user_groups (groupname, username) values ('student', 'student');

/*
ALL default instructors have password: instructor
*/

insert into user_info (username, password, email, active)
    values ('gqian@uco.edu',
        'cf2eb894cc40f5c6e781910063859278a3b214a3a14fedb9ca336ca39962b856',
        'gqian@uco.edu', 1);
insert into user_groups (groupname, username) values ('instructor', 'gqian@uco.edu');

insert into user_info (username, password, email, active)
    values ('hsung@uco.edu',
        'cf2eb894cc40f5c6e781910063859278a3b214a3a14fedb9ca336ca39962b856',
        'hsung@uco.edu', 1);
insert into user_groups (groupname, username) values ('instructor', 'hsung@uco.edu');

insert into user_info (username, password, email, active)
    values ('jfu@uco.edu',
        'cf2eb894cc40f5c6e781910063859278a3b214a3a14fedb9ca336ca39962b856',
        'jfu@uco.edu', 1);
insert into user_groups (groupname, username) values ('instructor', 'jfu@uco.edu');

insert into user_info (username, password, email, active)
    values ('mgourley@uco.edu',
        'cf2eb894cc40f5c6e781910063859278a3b214a3a14fedb9ca336ca39962b856',
        'mgourley@uco.edu', 1);
insert into user_groups (groupname, username) values ('instructor', 'mgourley@uco.edu');

insert into user_info (username, password, email, active)
    values ('dholt@uco.edu',
        'cf2eb894cc40f5c6e781910063859278a3b214a3a14fedb9ca336ca39962b856',
        'dholt@uco.edu', 1);
insert into user_groups (groupname, username) values ('instructor', 'dholt@uco.edu');

insert into user_info (username, password, email, active)
    values ('wmcdaniel@uco.edu',
        'cf2eb894cc40f5c6e781910063859278a3b214a3a14fedb9ca336ca39962b856',
        'wmcdaniel@uco.edu', 1);
insert into user_groups (groupname, username) values ('instructor', 'wmcdaniel@uco.edu');

insert into user_info (username, password, email, active)
    values ('mpark5@uco.edu',
        'cf2eb894cc40f5c6e781910063859278a3b214a3a14fedb9ca336ca39962b856',
        'mpark5@uco.edu', 1);
insert into user_groups (groupname, username) values ('instructor', 'mpark5@uco.edu');

insert into user_info (username, password, email, active)
    values ('drstockwell@gmail.com',
        'cf2eb894cc40f5c6e781910063859278a3b214a3a14fedb9ca336ca39962b856',
        'drstockwell@gmail.com', 1);
insert into user_groups (groupname, username) values ('instructor', 'drstockwell@gmail.com');

insert into user_info (username, password, email, active)
    values ('trturner@uco.edu',
        'cf2eb894cc40f5c6e781910063859278a3b214a3a14fedb9ca336ca39962b856',
        'trturner@uco.edu', 1);
insert into user_groups (groupname, username) values ('instructor', 'trturner@uco.edu');

insert into user_info (username, password, email, active)
    values ('bockus@gmail.com',
        'cf2eb894cc40f5c6e781910063859278a3b214a3a14fedb9ca336ca39962b856',
        'bockus@gmail.com', 1);
insert into user_groups (groupname, username) values ('instructor', 'bockus@gmail.com');

insert into user_info (username, password, email, active)
    values ('ralph.deboard@oc.edu',
        'cf2eb894cc40f5c6e781910063859278a3b214a3a14fedb9ca336ca39962b856',
        'ralph.deboard@oc.edu', 1);
insert into user_groups (groupname, username) values ('instructor', 'ralph.deboard@oc.edu');

insert into user_info (username, password, email, active)
    values ('bdong@uco.edu',
        'cf2eb894cc40f5c6e781910063859278a3b214a3a14fedb9ca336ca39962b856',
        'bdong@uco.edu', 1);
insert into user_groups (groupname, username) values ('instructor', 'bdong@uco.edu');

insert into user_info (username, password, email, active)
    values ('mcastle3@uco.edu',
        'cf2eb894cc40f5c6e781910063859278a3b214a3a14fedb9ca336ca39962b856',
        'mcastle3@uco.edu', 1);
insert into user_groups (groupname, username) values ('instructor', 'mcastle3@uco.edu');

insert into user_info (username, password, email, active)
    values ('phyl_thornton@yahoo.com',
        'cf2eb894cc40f5c6e781910063859278a3b214a3a14fedb9ca336ca39962b856',
        'phyl_thornton@yahoo.com', 1);
insert into user_groups (groupname, username) values ('instructor', 'phyl_thornton@yahoo.com');

insert into user_info (username, password, email, active)
    values ('visor1@cox.net',
        'cf2eb894cc40f5c6e781910063859278a3b214a3a14fedb9ca336ca39962b856',
        'visor1@cox.net', 1);
insert into user_groups (groupname, username) values ('instructor', 'visor1@cox.net');


insert into FACULTY (NAMEPREFIX, FIRSTNAME, LASTNAME, JOBTITLE, EMAIL, WEBSITE, 
                    OFFICE, PHONE, STATUS) 
    VALUES ('Dr.','Gang','Qian','Professor (Chairperson)',
                'gqian@uco.edu', 
                'http://cs2.uco.edu/~gqian',
                'MCS117B', '405.974.5716', 'fulltime');

insert into FACULTY (NAMEPREFIX, FIRSTNAME, LASTNAME, JOBTITLE, EMAIL, WEBSITE, 
                    OFFICE, PHONE, STATUS) 
    VALUES ('Dr.','Hong','Sung',
    'Professor (Assistant Chairperson, Graduate/Undergraduate Advisor)',
    'hsung@uco.edu', 'http://cs3.uco.edu',
            'MCS132', '405.509.5090', 'fulltime');

insert into FACULTY (NAMEPREFIX, FIRSTNAME, LASTNAME, JOBTITLE, EMAIL, WEBSITE, 
                    OFFICE, PHONE, STATUS) 
    VALUES ('Dr.','Jicheng','Fu','Associate Professor (John T. Beresford Endowed Chair)',
                'jfu@uco.edu', 
                'http://cs2.uco.edu/~fu',
                'MCS127', '405.974.5704', 'fulltime');

insert into FACULTY (NAMEPREFIX, FIRSTNAME, LASTNAME, JOBTITLE, EMAIL, WEBSITE, 
                    OFFICE, PHONE, STATUS) 
    VALUES ('Mr.','Michael','Gourley','Assistant Professor',
                'mgourley@uco.edu', 
                '',
                'MCS130', '405.974.5387', 'fulltime');

insert into FACULTY (NAMEPREFIX, FIRSTNAME, LASTNAME, JOBTITLE, EMAIL, WEBSITE, 
                    OFFICE, PHONE, STATUS) 
    VALUES ('Ms.','Dawn','Holt','Lecturer (Internship Coordinator)',
                'dholt@uco.edu', 
                'http://cs2.uco.edu/~holt',
                'MCS117C', '405.974.5382', 'fulltime');

insert into FACULTY (NAMEPREFIX, FIRSTNAME, LASTNAME, JOBTITLE, EMAIL, WEBSITE, 
                    OFFICE, PHONE, STATUS) 
    VALUES ('Dr.','Bill','McDaniel','Professor',
                'wmcdaniel@uco.edu', 
                'http://cs2.uco.edu/~mcdaniel',
                'MCS129', '405.974.5388', 'fulltime');

insert into FACULTY (NAMEPREFIX, FIRSTNAME, LASTNAME, JOBTITLE, EMAIL, WEBSITE, 
                    OFFICE, PHONE, STATUS) 
    VALUES ('Dr.','Myung-Ah (Grace)','Park','Associate Professor',
                'mpark5@uco.edu', 
                'http://cs2.uco.edu/~gp',
                'MCS125', '405.974.5292', 'fulltime');

insert into FACULTY (NAMEPREFIX, FIRSTNAME, LASTNAME, JOBTITLE, EMAIL, WEBSITE, 
                    OFFICE, PHONE, STATUS) 
    VALUES ('Dr.','Bill','Stockwell','Associate Professor',
                'drstockwell@gmail.com', 
                'http://cs2.uco.edu/~stockwel',
                'MCS124', '405.974.5240', 'fulltime');

insert into FACULTY (NAMEPREFIX, FIRSTNAME, LASTNAME, JOBTITLE, EMAIL, WEBSITE, 
                    OFFICE, PHONE, STATUS) 
    VALUES ('Dr.','Thomas','Turner','Professor (ABET Coordinator)',
                'trturner@uco.edu', 
                'http://cs2.uco.edu/~trt',
                'MCS123', '405.974.5383', 'fulltime');

insert into FACULTY (NAMEPREFIX, FIRSTNAME, LASTNAME, JOBTITLE, EMAIL, 
                     PHONE, STATUS) 
    VALUES ('Mr.','Mike','Bockus','Instructor','bockus@gmail.com',
            '405.974.5717', 'adjunct');

insert into FACULTY (NAMEPREFIX, FIRSTNAME, LASTNAME, JOBTITLE, EMAIL, 
                     PHONE, STATUS) 
    VALUES ('Mr.','Ralph','DeBoard','Instructor','ralph.deboard@oc.edu',
            '405.974.5717', 'adjunct');

insert into FACULTY (NAMEPREFIX, FIRSTNAME, LASTNAME, JOBTITLE, EMAIL, 
                     PHONE, STATUS) 
    VALUES ('Mr.','Bo','Dong','Instructor','bdong@uco.edu',
            '405.974.5717', 'adjunct');

insert into FACULTY (NAMEPREFIX, FIRSTNAME, LASTNAME, JOBTITLE, OFFICE, EMAIL, 
                     PHONE, STATUS) 
    VALUES ('Ms.','Megan','Castle','Administrative Assistant','MCS117- Computer Science Department Office','mcastle3@uco.edu',
            '405.974.5717', 'administrative');

insert into FACULTY (NAMEPREFIX, FIRSTNAME, LASTNAME, JOBTITLE, EMAIL, 
                    STATUS) 
    VALUES ('Dr.','Phyllis','Thornton','Emeritus Professor','phyl_thornton@yahoo.com',
            'former');

insert into FACULTY (NAMEPREFIX, FIRSTNAME, LASTNAME, JOBTITLE, EMAIL, 
                    STATUS) 
    VALUES ('Ms.','Diane','Visor','Emeritus Professor','visor1@cox.net',
            'former');
