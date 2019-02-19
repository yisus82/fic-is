-- ----------- Table for validation queries from the connection pool. -----------

CREATE TABLE PingTable (foo CHAR(1));

-- ------------------------------ CREATE TABLES ---------------------------------

-- -------------------------------- Question ------------------------------------

CREATE TABLE Question (questionID BIGINT NOT NULL, 
	description VARCHAR(30) NOT NULL,
	CONSTRAINT QuestionPK PRIMARY KEY (questionID)) TYPE = InnoDB;
	
CREATE INDEX QuestionIndexByQuestionID ON Question (questionID);

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
	CONSTRAINT validQuestion CHECK (leaf = 'N' OR questionID IS NOT NULL),
	INDEX CategoryIndexForCategoryFK (parent),
	INDEX CategoryIndexForQuestionFK (questionID)) TYPE = InnoDB;

CREATE INDEX CategoryIndexByCategoryID ON Category (categoryID);

-- ---------------------------- CategoryQuestion -------------------------------

CREATE TABLE CategoryQuestion (categoryQuestionID BIGINT NOT NULL, categoryID VARCHAR(10) NOT NULL,
	questionID BIGINT NOT NULL,
	CONSTRAINT CategoryQuestionPK PRIMARY KEY (categoryQuestionID),
	CONSTRAINT CategoryQuestionCategoryFK FOREIGN KEY (categoryID)
		REFERENCES Category (categoryID),
	CONSTRAINT CategoryQuestionQuestionFK FOREIGN KEY (questionID)
		REFERENCES Question (questionID),
	INDEX CategoryQuestionIndexForCategoryFK (categoryID),
	INDEX CategoryQuestionIndexForQuestionFK (questionID)) TYPE = InnoDB;

	
CREATE INDEX CategoryQuestionIndexByID ON CategoryQuestion (categoryQuestionID);

-- ------------------------------ Country ---------------------------------------

CREATE TABLE Country (countryID VARCHAR(3) NOT NULL, 
	name VARCHAR(30) NOT NULL, language VARCHAR(15) NOT NULL, 
	languageID VARCHAR(3) NOT NULL,
    CONSTRAINT CountryPK PRIMARY KEY (countryID)) TYPE = InnoDB;

CREATE INDEX CountryIndexByCountryID ON Country (countryID);

-- ----------------------------- User Profile -----------------------------------

-- NOTE: "version" column is not declared as "NOT NULL" since it is not
-- used in the plain model version. "DEFAULT O" is specified since if the
-- plain model version is used before the EJB model version, the version
-- column must have a non-null value when the EJB model version be used.

CREATE TABLE UserProfile (login VARCHAR(20) NOT NULL,
    password VARCHAR(11) NOT NULL, firstName VARCHAR(20) NOT NULL, surname VARCHAR(40) NOT NULL, 
    email VARCHAR(60) NOT NULL, countryID VARCHAR(3) NOT NULL, version BIGINT DEFAULT 0,
    CONSTRAINT UserProfilePK PRIMARY KEY (login),
    CONSTRAINT UserProfileCountryFK FOREIGN KEY (countryID)
    	REFERENCES Country (CountryID),
    INDEX UserProfileIndexForCountryFK (countryID)) TYPE = InnoDB;
    
CREATE INDEX UserProfileIndexByLogin ON UserProfile (login);

-- --------- SET FOREIGN KEY CHECKS OFF TO AVOID CIRCULAR DEPENDENCY ------------

SET FOREIGN_KEY_CHECKS = 0;

-- ---------------------------------- Event -------------------------------------

CREATE TABLE Event (eventID BIGINT NOT NULL AUTO_INCREMENT, 
    description VARCHAR(50) NOT NULL, date TIMESTAMP NOT NULL, 
    categoryID VARCHAR(10) NOT NULL, betTypeID BIGINT, highlighted VARCHAR(1) NOT NULL, 
    insertionDate DATE NOT NULL, version BIGINT DEFAULT 0,
	CONSTRAINT EventPK PRIMARY KEY (eventID),
	CONSTRAINT EventCategoryFK FOREIGN KEY (categoryID)
		REFERENCES Category (categoryID),
	CONSTRAINT EventBetTypeFK FOREIGN KEY (betTypeID)
        REFERENCES BetType (betTypeID) ON DELETE CASCADE,
    CONSTRAINT validHighlighted CHECK (highlighted = 'Y' OR highlighted = 'N' 
		OR highlighted = 'R' OR highlighted = 'W'),
	INDEX EventIndexForCategoryFK (categoryID)) TYPE = InnoDB;

CREATE INDEX EventIndexByEventID ON Event (eventID);
CREATE INDEX EventIndexByDate ON Event (eventID, date);	

-- -------------------------------- Bet Type ------------------------------------

CREATE TABLE BetType (betTypeID BIGINT NOT NULL AUTO_INCREMENT, 
	eventID BIGINT NOT NULL, questionID BIGINT NOT NULL, version BIGINT DEFAULT 0,
	CONSTRAINT BetTypePK PRIMARY KEY (betTypeID),
	CONSTRAINT BetTypeEventFK FOREIGN KEY (eventID)
		REFERENCES Event (eventID) ON DELETE CASCADE,
	CONSTRAINT BetTypeQuestionFK FOREIGN KEY (questionID)
		REFERENCES Question (questionID),
	INDEX BetTypeIndexForEventFK (eventID),
	INDEX BetTypeIndexForQuestionFK (questionID)) TYPE = InnoDB;
	
CREATE INDEX BetTypeIndexByBetTypeID ON BetType (betTypeID);

-- --------------------------------- Account ------------------------------------

CREATE TABLE Account (accountID BIGINT NOT NULL AUTO_INCREMENT, 
	userID VARCHAR(20) NOT NULL, creditCardNumber VARCHAR(23) NOT NULL, 
	expirationDate DATE NOT NULL, balance DOUBLE NOT NULL, version BIGINT DEFAULT 0,
    CONSTRAINT AccountPK PRIMARY KEY (accountID),
    CONSTRAINT AccountUserProfileFK FOREIGN KEY (userID)
    	REFERENCES UserProfile (login) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT validBalance CHECK (balance >= 0),
    INDEX AccountIndexForUserProfileFK (userID)) TYPE = InnoDB;
    
CREATE INDEX AccountIndexByAccountID ON Account (accountID);

-- ---------------------------- Account Operation -------------------------------

CREATE TABLE AccountOperation (accountOperationID BIGINT NOT NULL AUTO_INCREMENT, 
	accountID BIGINT NOT NULL, amount DOUBLE NOT NULL, 
	type VARCHAR(1) NOT NULL, description VARCHAR(45) NOT NULL, date TIMESTAMP NOT NULL,
	betID BIGINT, version BIGINT DEFAULT 0,
    CONSTRAINT AccountOperationPK PRIMARY KEY (accountOperationID),
    CONSTRAINT AccountOperationAccountFK FOREIGN KEY (accountID)
    	REFERENCES Account (accountID) ON DELETE CASCADE,
    CONSTRAINT AccountOperationBetFK FOREIGN KEY (betID)
    	REFERENCES Bet(betID) ON DELETE CASCADE,
    CONSTRAINT validAmountAccountOperation CHECK (amount > 0),
    CONSTRAINT validType CHECK (type = '+' OR type = '-'),
    INDEX AccountOperationIndexForAccountFK (accountID),
    INDEX AccountOperationIndexForBetFK (betID)) TYPE = InnoDB;
    
CREATE INDEX AccountOperationIndexByAccountOperationID ON 
	AccountOperation (accountOperationID);
CREATE INDEX AccountOperationIndexByDate ON AccountOperation (accountOperationID, date);

-- ------------------------------- Bet Option -----------------------------------

CREATE TABLE BetOption (betOptionID BIGINT NOT NULL AUTO_INCREMENT, 
	description VARCHAR(30) NOT NULL, odds DOUBLE NOT NULL, 
	betTypeID BIGINT NOT NULL, status VARCHAR(1) NOT NULL, version BIGINT DEFAULT 0,
	CONSTRAINT BetOptionPK PRIMARY KEY (betOptionID),
	CONSTRAINT BetOptionBetTypeFK FOREIGN KEY (betTypeID)
		REFERENCES BetType (betTypeID) ON DELETE CASCADE,
	CONSTRAINT validOdds CHECK (odds > 1),
	CONSTRAINT validStatus CHECK (status = 'W' OR status = 'P' OR
	 	status = 'L')) TYPE = InnoDB;
	
CREATE INDEX BetOptionIndexByBetOptionID ON BetOption (betOptionID);

-- ---------------------------------- Bet ---------------------------------------

CREATE TABLE Bet (betID BIGINT NOT NULL AUTO_INCREMENT, 
	betTypeID BIGINT NOT NULL, betOptionID BIGINT NOT NULL, 
	accountID BIGINT NOT NULL, eventID BIGINT NOT NULL, 
	amount DOUBLE NOT NULL,	date TIMESTAMP NOT NULL, status VARCHAR(1) NOT NULL, version BIGINT DEFAULT 0,
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
	CONSTRAINT validStatus CHECK (status = 'G' OR status = 'P' OR
		status = 'L'),
	INDEX BetIndexForBetTypeFK (betTypeID),
	INDEX BetIndexForBetOptionFK (betOptionID),
	INDEX BetIndexForAccountFK (accountID),
	INDEX BetIndexForEventFK (eventID)) TYPE = InnoDB;
	
CREATE INDEX BetIndexByBetID ON Bet (betID);
CREATE INDEX BetIndexByDate ON Bet (betID, date);

-- ----------------- SET FOREIGN KEY CHECKS ON AGAIN ----------------------------

SET FOREIGN_KEY_CHECKS = 1;

-- ------------------------------ INSERT ROWS -----------------------------------

INSERT INTO Country VALUES('ES','Spain','Spanish', 'es');
INSERT INTO Country VALUES('GB','United Kingdom','English', 'en');
INSERT INTO Country VALUES('DE','Germany','German', 'de');
INSERT INTO Country VALUES('US','United States of America','English', 'en');
INSERT INTO Country VALUES('JP','Japan','Japanese', 'ja');

INSERT INTO Question VALUES('0', 'No question');
INSERT INTO Question VALUES('1', '1X2');
INSERT INTO Question VALUES('2', '12');
INSERT INTO Question VALUES('3', 'Final Score');
INSERT INTO Question VALUES('4', 'Which team will score first?');
INSERT INTO Question VALUES('5', 'Which player will score first?');

INSERT INTO Category VALUES('Root', 'Sports', 'Root', 'N', '0');
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
    'admin@ubet.com', 'ES', '0');
