ALTER TABLE MESSAGE DROP COLUMN CONTENT;
ALTER TABLE MESSAGE ADD COLUMN CONTENT BYTEA NOT NULL;