CREATE TABLE KEY_PAIR (
     ID BIGSERIAL NOT NULL,
     PUBLIC_KEY bytea NOT NULL,
     PRIVATE_KEY bytea NOT NULL,
     PASSWORD VARCHAR(64) NOT NULL,
     NAME VARCHAR(64) NOT NULL,
     CONSTRAINT KEYPAIR_PK PRIMARY KEY (ID),
     CONSTRAINT KEYPAIR_UQ1 UNIQUE (NAME)
);

CREATE TABLE CHAIN_ROOT (
    ID BIGSERIAL NOT NULL,
    START_KEYPAIR_ID BIGINT NOT NULL,
    CREATION_DATE TIMESTAMP NOT NULL,
    END_KEYPAIR_ID BIGSERIAL,
    CLEARANCE_DATE TIMESTAMP,
    CONSTRAINT CHAIN_ROOT_PK PRIMARY KEY (ID),
    CONSTRAINT CHAIN_ROOT_FK1 FOREIGN KEY (START_KEYPAIR_ID) REFERENCES  KEY_PAIR(ID),
    CONSTRAINT CHAIN_ROOT_FK2 FOREIGN KEY (END_KEYPAIR_ID) REFERENCES  KEY_PAIR(ID)
);
