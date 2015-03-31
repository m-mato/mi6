CREATE TABLE AGENTS (
    ID integer NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    NICKNAME varchar(255),
    AGE integer,
    PHONE_NUMBER varchar(15)
);

CREATE TABLE MISSIONS (
    ID integer NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    CODENAME varchar(255),
    OBJECTIVE varchar(255),
    LOCATION varchar(255),
    NOTES varchar(255)
);

CREATE TABLE ASSIGNMENTS (
    ID integer NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    AGENT_ID integer,
    MISSION_ID integer,
    START_DATE date,
    END_DATE date
);

    
