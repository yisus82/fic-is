-- -------- Drop table for validation queries from the connection pool. ---------

DROP TABLE PingTable;

-- ------------------------------ DROP TABLES -----------------------------------

-- --------- SET FOREIGN KEY CHECKS OFF TO AVOID CIRCULAR DEPENDENCY ------------
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE Bet;
DROP TABLE BetOption;
DROP TABLE BetType;
DROP TABLE Event;
DROP TABLE AccountOperation;
DROP TABLE Account;
DROP TABLE UserProfile;
DROP TABLE Country;
DROP TABLE CategoryQuestion;
DROP TABLE Category;
DROP TABLE Question;

-- ----------------- SET FOREIGN KEY CHECKS ON AGAIN ----------------------------
SET FOREIGN_KEY_CHECKS = 1;

