-- ---------- Create table for validation queries from the connection pool. ----------

CREATE TABLE PingTable (foo CHAR(1));

-- ------------------------------ CREATE TABLES --------------------------------

-- -------------------------------- Category ------------------------------------

CREATE TABLE Category (categoryID VARCHAR(10) NOT NULL, 
	name VARCHAR(20) NOT NULL, parent VARCHAR(10), leaf VARCHAR(1) NOT NULL,
	CONSTRAINT CategoryPK PRIMARY KEY (categoryID),
	CONSTRAINT validLeaf CHECK (leaf = 'Y' OR leaf = 'N')) TYPE = InnoDB;

CREATE INDEX CategoryIndexByCategoryID ON Category (categoryID);

-- ------------------------------ Country ---------------------------------------

CREATE TABLE Country (countryID VARCHAR(3) NOT NULL, 
	name VARCHAR(30) NOT NULL, language VARCHAR(15) NOT NULL, 
	languageID VARCHAR(3) NOT NULL,
    CONSTRAINT CountryPK PRIMARY KEY (countryID)) TYPE = InnoDB;

CREATE INDEX CountryIndexByCountryID ON Country (countryID);

-- ----------------------------- User Profile -----------------------------------

CREATE TABLE UserProfile (login VARCHAR(20) NOT NULL,
    password VARCHAR(11) NOT NULL, type VARCHAR(1) NOT NULL, 
    firstName VARCHAR(20) NOT NULL, surname VARCHAR(40) NOT NULL, 
    email VARCHAR(60) NOT NULL, street VARCHAR(20) NOT NULL, 
    city VARCHAR(20) NOT NULL, state VARCHAR(20) NOT NULL, 
    postalCode SMALLINT NOT NULL, countryID VARCHAR(3) NOT NULL, 
    creditCardNumber VARCHAR(23), expirationDate TIMESTAMP, 
    CONSTRAINT UserProfilePK PRIMARY KEY (login),
    CONSTRAINT UserProfileCountryFK FOREIGN KEY (countryID)
    	REFERENCES Country (CountryID),
    INDEX UserProfileIndexForCountryFK (countryID)) TYPE = InnoDB;
    
CREATE INDEX UserProfileIndexByLogin ON UserProfile (login);

-- -------------------------------- Product --------------------------------------

CREATE TABLE Product (productID BIGINT NOT NULL AUTO_INCREMENT, 
    name VARCHAR(30) NOT NULL, description VARCHAR(50), endTime TIMESTAMP NOT NULL, 
    currentPrice DOUBLE NOT NULL, startTime TIMESTAMP NOT NULL, 
    startingPrice DOUBLE NOT NULL, shippingInfo VARCHAR(100) NOT NULL,
    categoryID VARCHAR(10) NOT NULL, userID VARCHAR(30) NOT NULL, winnerID VARCHAR(30), 
    winnerBid DOUBLE,
	CONSTRAINT ProductPK PRIMARY KEY (productID),
	CONSTRAINT ProductCategoryFK FOREIGN KEY (categoryID)
		REFERENCES Category (categoryID),
	CONSTRAINT ProductUserProfile1FK FOREIGN KEY (userID)
		REFERENCES UserProfile (login) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT ProductUserProfile2FK FOREIGN KEY (winnerID)
		REFERENCES UserProfile (login) ON DELETE CASCADE ON UPDATE CASCADE,
	INDEX ProductIndexForCategoryFK (categoryID),
	INDEX ProductIndexForUserProfile1FK (userID),
	INDEX ProductIndexForUserProfile2FK (winnerID)) TYPE = InnoDB;

CREATE INDEX ProductIndexByProductID ON Product (productID);
CREATE INDEX ProductIndexByDate ON Product (productID, endTime);

-- -------------------------------- Bid ----------------------------------------

CREATE TABLE Bid (bidID BIGINT NOT NULL AUTO_INCREMENT, userID VARCHAR(30) NOT NULL,
	productID BIGINT NOT NULL, currentBid DOUBLE NOT NULL, maxBid DOUBLE NOT NULL, 
	date TIMESTAMP NOT NULL,
	CONSTRAINT BidPK PRIMARY KEY (bidID),
	CONSTRAINT BidUserProfileFK FOREIGN KEY (userID)
		REFERENCES UserProfile (login) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT BidProductFK FOREIGN KEY (productID)
		REFERENCES Product (productID) ON DELETE CASCADE,
	INDEX BidIndexForUserProfileFK (userID),
	INDEX BidIndexForProductFK (productID)) TYPE = InnoDB;

CREATE INDEX BidIndexByBidID ON Bid (bidID);
CREATE INDEX BidIndexByDate ON Bid (bidID, date);

-- ------------------------------- Increment -----------------------------------

CREATE TABLE Increment (minValue DOUBLE NOT NULL, maxValue DOUBLE NOT NULL, 
    increment DOUBLE NOT NULL,
	CONSTRAINT IncrementRankPK PRIMARY KEY (minValue)) TYPE = InnoDB;

CREATE INDEX IncrementIndexByMinValue ON Increment (minValue);

-- --------------------------------- Vote --------------------------------------

CREATE TABLE Vote (voteID BIGINT NOT NULL AUTO_INCREMENT, rating INT NOT NULL,
	comment VARCHAR(100) NOT NULL, voterID VARCHAR(30) NOT NULL, type INT NOT NULL,
	date TIMESTAMP NOT NULL, votedID VARCHAR(30) NOT NULL, productID BIGINT NOT NULL,
	CONSTRAINT VotePK PRIMARY KEY (voteID),
	CONSTRAINT VoteUserProfile1FK FOREIGN KEY (voterID)
		REFERENCES UserProfile (login) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT VoteUserProfile2FK FOREIGN KEY (votedID)
		REFERENCES UserProfile (login) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT VoteProductFK FOREIGN KEY (productID)
		REFERENCES Product (productID),
	INDEX VoteIndexForUserProfile1FK (voterID),
	INDEX VoteIndexForUserProfile2FK (votedID),
	INDEX VoteIndexForProductFK (productID)) TYPE = InnoDB;
	
CREATE INDEX VoteIndexByVoteID ON Vote (voteID);	

-- ------------------------------ INSERT ROWS ----------------------------------

INSERT INTO Country VALUES('ES','Spain','Spanish', 'es');
INSERT INTO Country VALUES('GB','United Kingdom','English', 'en');
INSERT INTO Country VALUES('DE','Germany','German', 'de');
INSERT INTO Country VALUES('US','United States of America','English', 'en');
INSERT INTO Country VALUES('JP','Japan','Japanese', 'ja');

INSERT INTO Category VALUES('Root', 'All', null, 'N');
INSERT INTO Category VALUES('Mov', 'Movies', 'Root', 'N');
INSERT INTO Category VALUES('VHS', 'VHS', 'Mov', 'Y');
INSERT INTO Category VALUES('DVD', 'DVD', 'Mov', 'Y');
INSERT INTO Category VALUES('Com', 'Computers', 'Root', 'N');
INSERT INTO Category VALUES('PC', 'PC', 'Com', 'N');
INSERT INTO Category VALUES('Ori', 'Originals', 'PC', 'Y');
INSERT INTO Category VALUES('Clon', 'Clonics', 'PC', 'Y');
INSERT INTO Category VALUES('Mac', 'Mac', 'Com', 'Y');

INSERT INTO Increment VALUES(0, 10, 0.1);
INSERT INTO Increment VALUES(10, 30, 0.5);
INSERT INTO Increment VALUES(30, 60, 1);
INSERT INTO Increment VALUES(60, 100, 1.5);
INSERT INTO Increment VALUES(100, 500, 2);
INSERT INTO Increment VALUES(500, 1000, 5);
INSERT INTO Increment VALUES(1000, 0, 10);