

DROP TABLE IF EXISTS Person;
CREATE TABLE Person
(
	id integer, 
	displayedName text,  
	firstName text, 
	lastName text COLLATE NOCASE, --compare case insensitive 
	
	primary key(id asc)
);

DROP TABLE IF EXISTS Phone;
CREATE TABLE Phone 
(
	id integer, 
	person_id integer constraint fk_phone_person references Person(id),
	phoneType_flag integer not null, -- mobile, isdn, private, work, ...
	displayedNumber text, 
	normalizedNumber integer, -- für Rufnummernauflösung
	
	primary key(id asc)
);
DROP INDEX IF EXISTS idx_phone_person_id;
CREATE INDEX idx_phone_person_id ON Phone (person_id);


