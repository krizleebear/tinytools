

DROP TABLE IF EXISTS Person;
CREATE TABLE Person
(
	id integer, 
	displayedName text, 
	firstName text, 
	lastName text, 
	
	primary key(id asc)
);

DROP TABLE IF EXISTS Phone;
CREATE TABLE Phone 
(
	id integer, 
	person_id integer constraint fk_phone_person references Person(id), 
	displayedNumber varchar, 
	
	primary key(id asc)
);
DROP INDEX IF EXISTS idx_phone_person_id;
CREATE INDEX idx_phone_person_id ON Phone (person_id);


