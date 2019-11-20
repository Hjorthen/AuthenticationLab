CREATE DATABASE IF NOT EXISTS AuthenticationLab; 
USE AuthenticationLab;

CREATE TABLE IF NOT EXISTS Role(
    Title VARCHAR(20) NOT NULL,

    PRIMARY KEY (Title)
);

CREATE TABLE IF NOT EXISTS Account(
	ID INT UNSIGNED NOT NULL AUTO_INCREMENT,
    Username VARCHAR(64) NOT NULL,
	Password VARCHAR(64) NOT NULL, -- Length of SHA-256 hash
	Salt VARCHAR(32) NOT NULL,
    Role VARCHAR(20) NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (Role)
		REFERENCES Role(Title)
);

CREATE TABLE IF NOT EXISTS RBAC(
	Title VARCHAR(20) NOT NULL,
	Print boolean,
	Queue boolean,
	TopQueue boolean,
	Start boolean,
	Stop boolean,
	Restart boolean,
	Status boolean,
	ReadConfig boolean,
	SetConfig boolean
	FOREIGN KEY (Title)
		REFERENCES Role(Title)
);

CREATE TABLE IF NOT EXISTS ACL(
	UserID INT UNSIGNED NOT NULL,
    Print boolean,
    Queue boolean,
    TopQueue boolean,
    Start boolean,
    Stop boolean,
    Restart boolean,
    Status boolean,
    ReadConfig boolean,
    SetConfig boolean,
    FOREIGN KEY (UserID)
		REFERENCES Account(ID)
);

CREATE TABLE IF NOT EXISTS RoleRelation(
	UserID INT UNSIGNED NOT NULL,
    Role VARCHAR(20) NOT NULL,
	FOREIGN KEY (UserID)
		REFERENCES Account(ID),
	FOREIGN KEY (Role)
		REFERENCES Role(Title)
);

DELIMITER $$
CREATE PROCEDURE RegisterAccount
(IN username VARCHAR(64), IN password VARCHAR(64), IN salt VARCHAR(32), IN role VARCHAR(20), OUT id INT)
BEGIN
	INSERT INTO account (Username, Password, Salt, Role)
    VALUES (username, password, salt, role);
    SET id = LAST_INSERT_ID();
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE LookupSalt
(IN username VARCHAR(64))
BEGIN 
	select Account.salt from account where account.username = username;
END$$
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE AuthenticateUser
(IN username VARCHAR(64), IN password VARCHAR(64))
BEGIN
	SELECT EXISTS(
		SELECT account.id
		FROM account
		WHERE account.username = username AND account.password = password
    );
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE GetRole
(IN username VARCHAR(64))
BEGIN
	
END$$
DELIMITER ;

CALL RegisterAccount('Alice', '','','Admin', @result);
CALL RegisterAccount('Bob', '','','ServiceTechnician', @result);
CALL RegisterAccount('Cecilia', '','','SuperUser', @result);
CALL RegisterAccount('Bob', '','','User', @result);
CALL RegisterAccount('David', '','','User', @result);
CALL RegisterAccount('Erica', '','','User', @result);
CALL RegisterAccount('Fred', '','','User', @result);
CALL RegisterAccount('George', '','','User', @result);
