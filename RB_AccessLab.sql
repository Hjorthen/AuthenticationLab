CREATE DATABASE IF NOT EXISTS AuthenticationLab; 
USE AuthenticationLab;

CREATE TABLE IF NOT EXISTS Role(
    Title VARCHAR(20) NOT NULL,
	Print boolean,
	Queue boolean,
	TopQueue boolean,
	Start boolean,
	Stop boolean,
	Restart boolean,
	Status boolean,
	ReadConfig boolean,
	SetConfig boolean,
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
	INSERT INTO Account (Username, Password, Salt, Role)
    VALUES (username, password, salt, role);
    SET id = LAST_INSERT_ID();
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE LookupSalt
(IN username VARCHAR(64))
BEGIN 
	select Account.salt from account where Account.username = username;
END$$
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE AuthenticateUser
(IN username VARCHAR(64), IN password VARCHAR(64))
BEGIN
	SELECT EXISTS(
		SELECT account.id
		FROM Account
		WHERE Account.username = username AND Account.password = password
    );
END$$
DELIMITER ;

DELIMITER $$
CREATE OR REPLACE PROCEDURE IsAuthorized
(IN username VARCHAR(64), IN object VARCHAR(64))
BEGIN
    SET @query = CONCAT('SELECT ', object, ' INTO @Authorization from UserPermissions where Username = ?');
    PREPARE GetAuthorization FROM @query;
    EXECUTE GetAuthorization USING username;
    SELECT @Authorization as authorized;
END$$
DELIMITER ;

INSERT INTO Role VALUES ('user', true, true, false, false, false, false, false, false, false);
INSERT INTO Role VALUES ('poweruser', true, true, true, false, false, true, false, false, false);
INSERT INTO Role VALUES ('servicetechnician', false, false, false, true, true, true, true, true, true);
INSERT INTO Role VALUES ('admin', true, true, true, true, true, true, true, true, true);

CALL RegisterAccount('Alice', '','','Admin', @result);
CALL RegisterAccount('Bob', '','','ServiceTechnician', @result);
CALL RegisterAccount('Cecilia', '','','SuperUser', @result);
CALL RegisterAccount('David', '','','User', @result);
CALL RegisterAccount('Erica', '','','User', @result);
CALL RegisterAccount('Fred', '','','User', @result);
CALL RegisterAccount('George', '','','User', @result);

CREATE OR REPLACE VIEW UserPermissions as select Username, Role.* from Account join Role on Account.Role = Role.Title;
