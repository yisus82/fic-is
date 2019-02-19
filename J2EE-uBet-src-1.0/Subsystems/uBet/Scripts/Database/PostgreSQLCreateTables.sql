-- ----------- Table for validation queries from the connection pool. -----------

CREATE TABLE PingTable (foo CHAR(1));

-- ------------------------------ CREATE TABLES ---------------------------------

-- -------------------------------- Question ------------------------------------

CREATE TABLE Question (questionID BIGINT NOT NULL, 
	description VARCHAR(30) NOT NULL,
	CONSTRAINT QuestionPK PRIMARY KEY (questionID));
	
-- -------------------------------- Category ------------------------------------

CREATE TABLE Category (categoryID VARCHAR(10) NOT NULL, 
	name VARCHAR(20) NOT NULL, parent VARCHAR(10) NOT NULL, leaf VARCHAR(1) NOT NULL,
	questionID BIGINT,
	CONSTRAINT CategoryPK PRIMARY KEY (categoryID),
	CONSTRAINT CategoryCategoryFK FOREIGN KEY (parent)
		REFERENCES Category (categoryID),
	CONSTRAINT CategoryQuestionFK FOREIGN KEY (questionID)
		REFERENCES Question (questionID),
	CONSTRAINT validLeaf CHECK (leaf = 'Y' OR leaf = 'N'),
	CONSTRAINT validQuestion CHECK (leaf = 'N' OR questionID IS NOT NULL));

-- ---------------------------- CategoryQuestion -------------------------------

CREATE TABLE CategoryQuestion (categoryQuestionID BIGINT NOT NULL, categoryID VARCHAR(10) NOT NULL,
	questionID BIGINT NOT NULL,
	CONSTRAINT CategoryQuestionPK PRIMARY KEY (categoryQuestionID)
	CONSTRAINT CategoryQuestionCategoryFK FOREIGN KEY (categoryID)
		REFERENCES Category (categoryID),
	CONSTRAINT CategoryQuestionQuestionFK FOREIGN KEY (questionID)
		REFERENCES Question (questionID));
	
-- ------------------------------ Country ---------------------------------------

CREATE TABLE Country (countryID VARCHAR(3) NOT NULL, 
    name VARCHAR(30) NOT NULL, language VARCHAR(15) NOT NULL,
    languageID VARCHAR(3) NOT NULL,
    CONSTRAINT CountryPK PRIMARY KEY (countryID));

-- ----------------------------- User Profile -----------------------------------

-- NOTE: "version" column is not declared as "NOT NULL" since it is not
-- used in the plain model version. "DEFAULT O" is specified since if the
-- plain model version is used before the EJB model version, the version
-- column must have a non-null value when the EJB model version be used.

CREATE TABLE UserProfile (login VARCHAR(20) NOT NULL,
    password VARCHAR(11) NOT NULL, firstName VARCHAR(20) NOT NULL, surname VARCHAR(40) NOT NULL, 
    email VARCHAR(60) NOT NULL, countryID VARCHAR(3) NOT NULL, version BIGINT DEFAULT 0,
    CONSTRAINT UserProfilePK PRIMARY KEY (login),
    CONSTRAINT UserCountryFK FOREIGN KEY (countryID)
    	REFERENCES Country (CountryID) ON DELETE CASCADE);

-- --------------------------------- Account ------------------------------------

DROP SEQUENCE AccountSeq;
CREATE SEQUENCE AccountSeq;

CREATE TABLE Account (accountID BIGINT NOT NULL, 
	userID VARCHAR(20) NOT NULL, creditCardNumber VARCHAR(23) NOT NULL, 
	expirationDate DATE NOT NULL, balance DOUBLE PRECISION NOT NULL, version BIGINT DEFAULT 0,
    CONSTRAINT AccountPK PRIMARY KEY (accountID),
    CONSTRAINT AccountUserProfileFK FOREIGN KEY (userID)
    	REFERENCES UserProfile (login) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT validBalance CHECK (balance >= 0));

-- ---------------------------- Account Operation -------------------------------

DROP SEQUENCE AccountOperationSeq;
CREATE SEQUENCE AccountOperationSeq;

CREATE TABLE AccountOperation (accountOperationID BIGINT NOT NULL, 
	accountID BIGINT NOT NULL, amount DOUBLE PRECISION NOT NULL,
	type VARCHAR(1) NOT NULL, description VARCHAR(45) NOT NULL, date TIMESTAMP NOT NULL, 
	betID BIGINT NOT NULL, version BIGINT DEFAULT 0,
    CONSTRAINT AccountOperationPK PRIMARY KEY (accountOperationID),
    CONSTRAINT AccountOperationAccountFK FOREIGN KEY (accountID)
    	REFERENCES Account (accountID) ON DELETE CASCADE,
    CONSTRAINT AccountOperationBetFK FOREIGN KEY (betID)
    	REFERENCES Bet(betID) ON DELETE CASCADE,
    CONSTRAINT validAmountAccountOperation CHECK (amount > 0),
    CONSTRAINT validType CHECK (type = '+' OR type = '-'));
    
CREATE INDEX AccountOperationIndexByDate ON AccountOperation (accountOperationID, date);	
	
-- ---------------------------------- Event -------------------------------------

DROP SEQUENCE EventSeq;
CREATE SEQUENCE EventSeq;

CREATE TABLE Event (eventID BIGINT NOT NULL, 
    description VARCHAR(50) NOT NULL, date TIMESTAMP NOT NULL,
    categoryID VARCHAR(10) NOT NULL, betTypeID BIGINT, highlighted VARCHAR(1) NOT NULL, 
    insertionDate DATE NOT NULL, version BIGINT DEFAULT 0,
	CONSTRAINT EventPK PRIMARY KEY (eventID),
	CONSTRAINT EventCategoryFK FOREIGN KEY (categoryID)
		REFERENCES Category (categoryID) ON DELETE CASCADE,
	CONSTRAINT validHighlighted CHECK (highlighted = 'Y' OR highlighted = 'N' 
		OR highlighted = 'R' OR highlighted = 'W'));
	
CREATE INDEX EventIndexByDate ON Event (eventID, date);	

-- -------------------------------- Bet Type ------------------------------------

DROP SEQUENCE BetTypeSeq;
CREATE SEQUENCE BetTypeSeq;

CREATE TABLE BetType (betTypeID BIGINT NOT NULL, eventID BIGINT NOT NULL,
	questionID BIGINT NOT NULL, version BIGINT DEFAULT 0,
	CONSTRAINT BetTypePK PRIMARY KEY (betTypeID),
	CONSTRAINT BetTypeEventFK FOREIGN KEY (eventID)
		REFERENCES Event (eventID) ON DELETE CASCADE,
	CONSTRAINT BetTypeQuestionFK FOREIGN KEY (questionID)
		REFERENCES Question (questionID) ON DELETE CASCADE);
		
ALTER TABLE Event ADD FOREIGN KEY (betTypeID)
        REFERENCES BetType (betTypeID) ON DELETE CASCADE;

-- ------------------------------- Bet Option -----------------------------------

DROP SEQUENCE BetOptionSeq;
CREATE SEQUENCE BetOptionSeq;

CREATE TABLE BetOption (betOptionID BIGINT NOT NULL, 
	description VARCHAR(30) NOT NULL, odds DOUBLE PRECISION NOT NULL,
	betTypeID BIGINT NOT NULL, status VARCHAR(1) NOT NULL, version BIGINT DEFAULT 0,
	CONSTRAINT BetOptionPK PRIMARY KEY (betOptionID),
	CONSTRAINT BetOptionBetTypeFK FOREIGN KEY (betTypeID)
		REFERENCES BetType (betTypeID) ON DELETE CASCADE,
	CONSTRAINT validOdds CHECK (odds > 1),
	CONSTRAINT validStatus CHECK (status = 'W' OR status = 'P' OR status = 'L'));

-- ---------------------------------- Bet ---------------------------------------

DROP SEQUENCE BetSeq;
CREATE SEQUENCE BetSeq;

CREATE TABLE Bet (betID BIGINT NOT NULL, betTypeID BIGINT NOT NULL,
	betOptionID BIGINT NOT NULL, accountID BIGINT NOT NULL,
	eventID BIGINT NOT NULL, amount DOUBLE PRECISION NOT NULL,
	date TIMESTAMP NOT NULL, status VARCHAR(1) NOT NULL, version BIGINT DEFAULT 0,
	CONSTRAINT BetPK PRIMARY KEY (betID),
	CONSTRAINT BetBetTypeFK FOREIGN KEY (betTypeID)
		REFERENCES BetType (betTypeID) ON DELETE CASCADE,
	CONSTRAINT BetBetOptionFK FOREIGN KEY (betOptionID)
		REFERENCES BetOption (betOptionID) ON DELETE CASCADE,
	CONSTRAINT BetAccountFK FOREIGN KEY (accountID)
		REFERENCES Account (accountID) ON DELETE CASCADE,
	CONSTRAINT BetEventFK FOREIGN KEY (eventID)
		REFERENCES Event (eventID) ON DELETE CASCADE,
	CONSTRAINT validAmountBet CHECK (amount > 0),
	CONSTRAINT validStatus CHECK (status = 'G' OR status = 'P' OR status = 'L'));
	
CREATE INDEX BetIndexByDate ON Bet (betID, date);


-- ------------------------------ INSERT ROWS -----------------------------------

INSERT INTO Country VALUES('ES','Spain','Spanish', 'es');
INSERT INTO Country VALUES('GB','United Kingdom','English', 'en');
INSERT INTO Country VALUES('DE','Germany','German', 'de');
INSERT INTO Country VALUES('US','United States of America','English', 'en');
INSERT INTO Country VALUES('JP','Japan','Japanese', 'ja');

INSERT INTO Question VALUES('1', '1X2');
INSERT INTO Question VALUES('2', '12');
INSERT INTO Question VALUES('3', 'Final Score');
INSERT INTO Question VALUES('4', 'Which team will score first?');
INSERT INTO Question VALUES('5', 'Which player will score first?');

INSERT INTO Category VALUES('Root', 'Sports', 'Root', 'N', null);
INSERT INTO Category VALUES('Foot', 'Football', 'Root', 'N', '1');
INSERT INTO Category VALUES('FootEs1d', 'Primera Division', 'Foot', 'Y', '1');
INSERT INTO Category VALUES('FootEs2d', 'Segunda Division', 'Foot', 'Y', '1');
INSERT INTO Category VALUES('FootEnPrem', 'Premier League', 'Foot', 'Y', '1');
INSERT INTO Category VALUES('Basket', 'Basketball', 'Root', 'N', '2');
INSERT INTO Category VALUES('NBA', 'NBA', 'Basket', 'Y', '2');
INSERT INTO Category VALUES('ACB', 'ACB', 'Basket', 'Y', '2');
INSERT INTO Category VALUES('RollHockey', 'Roller Hockey', 'Root', 'N', '1');
INSERT INTO Category VALUES('OKLiga', 'OK Liga', 'RollHockey', 'Y', '1');
INSERT INTO Category VALUES('1Div', '1 Division', 'RollHockey', 'Y', '1');

INSERT INTO CategoryQuestion VALUES('1', 'Foot', '1');
INSERT INTO CategoryQuestion VALUES('2', 'Foot', '3');
INSERT INTO CategoryQuestion VALUES('3', 'Foot', '4');
INSERT INTO CategoryQuestion VALUES('4', 'Foot', '5');
INSERT INTO CategoryQuestion VALUES('5', 'Basket', '2');
INSERT INTO CategoryQuestion VALUES('6', 'RollHockey', '1');
INSERT INTO CategoryQuestion VALUES('7', 'RollHockey', '3');
INSERT INTO CategoryQuestion VALUES('8', 'RollHockey', '4');
INSERT INTO CategoryQuestion VALUES('9', 'RollHockey', '5');

INSERT INTO UserProfile VALUES('admin', 'pexzg3FUZAk', 'uBet', 'administrator',
	'admin@ubet.com', 'ES');
