DROP TABLE CHAIN_ROOT;

CREATE TABLE MESSAGE (
    ID VARCHAR(72) NOT NULL,
    MESSAGE VARCHAR(512) NOT NULL,
    START_KEYPAIR_ID BIGINT NOT NULL,
    CREATION_DATE TIMESTAMP NOT NULL,
    END_KEYPAIR_ID BIGSERIAL,
    CLEARANCE_DATE TIMESTAMP,
    CONSTRAINT CHAIN_ROOT_PK PRIMARY KEY (ID),
    CONSTRAINT CHAIN_ROOT_FK1 FOREIGN KEY (START_KEYPAIR_ID) REFERENCES  KEY_PAIR(ID),
    CONSTRAINT CHAIN_ROOT_FK2 FOREIGN KEY (END_KEYPAIR_ID) REFERENCES  KEY_PAIR(ID)
);