DROP TABLE EVENT_SLOTS;
DROP TABLE EVENTS;


CREATE TABLE EVENTS(
	EVENT_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY
		(START WITH 1, INCREMENT BY 1),
        FACULTY_EMAIL           VARCHAR(255),
        FACULTY_DISPLAY_NAME    VARCHAR(255),
        FACULTY_USERNAME        VARCHAR(255),
	EVENT_NAME              VARCHAR(50),
	START_TIME              TIMESTAMP,
	END_TIME                TIMESTAMP,
        DESCRIPTION             VARCHAR(1000),
	SLOT_SIZE               INTEGER, 
	ACTIVE                  CHAR,
        STYLE_CLASS             VARCHAR(255),
        PRIMARY KEY(EVENT_ID)
);

CREATE TABLE EVENT_SLOTS(
    EVENT_SLOT_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY
                  (START WITH 1, INCREMENT BY 1),
    EVENT_ID INTEGER,
    USER_INFO_USERNAME VARCHAR(255),
    USER_INFO_EMAIL VARCHAR(255),
    START_TIME TIMESTAMP,
    END_TIME TIMESTAMP,
    STATUS CHAR,
    PRIMARY KEY(EVENT_SLOT_ID),
    FOREIGN KEY(EVENT_ID) REFERENCES EVENTS (EVENT_ID)
);