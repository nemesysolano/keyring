ALTER TABLE MESSAGE DROP COLUMN END_KEYPAIR_ID;
ALTER TABLE MESSAGE ADD COLUMN END_KEYPAIR_ID BIGINT NOT NULL;
ALTER TABLE MESSAGE ADD CONSTRAINT CHAIN_ROOT_FK2 FOREIGN KEY (END_KEYPAIR_ID) REFERENCES  KEY_PAIR(ID)