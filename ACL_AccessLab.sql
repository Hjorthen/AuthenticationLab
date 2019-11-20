CREATE DATABASE IF NOT EXISTS AuthenticationLab; 
USE AuthenticationLab;

CREATE TABLE IF NOT EXISTS AccessControlMatrix(
    Username VARCHAR(64) NOT NULL,
	Print BOOLEAN DEFAULT false,
    Queue BOOLEAN DEFAULT false,
    TopQueue BOOLEAN DEFAULT false,
    Start BOOLEAN DEFAULT false,
    Stop BOOLEAN DEFAULT false,
    Restart BOOLEAN DEFAULT false,
    Status BOOLEAN DEFAULT false,
    ReadConfig BOOLEAN DEFAULT false,
    SetConfig BOOLEAN DEFAULT false,
    FOREIGN KEY (Username)
		REFERENCES Account(username)
);

CREATE TABLE IF NOT EXISTS Account(
	ID INT UNSIGNED NOT NULL AUTO_INCREMENT,
    Username VARCHAR(64) NOT NULL,
	Password VARCHAR(64) NOT NULL, -- Length of SHA-256 hash
	Salt VARCHAR(32) NOT NULL,
    PRIMARY KEY (ID)
);

DELIMITER $$
CREATE PROCEDURE RegisterAccount
(IN username VARCHAR(64), IN password VARCHAR(64), IN salt VARCHAR(32), IN role VARCHAR(20), OUT id INT)
BEGIN
	INSERT INTO account (Username, Password, Salt, Role)
    VALUES (username, password, salt, role);
    SET id = LAST_INSERT_ID();
    
    IF(role = 'Admin') THEN
		INSERT INTO role (Title, Print, Queue, TopQueue, Start, Stop, Restart, Status, ReadConfig, SetConfig) 
        VALUES (username, 1, 1, 1, 1, 1, 1, 1, 1, 1);
	END IF;
	
	IF(role = 'ServiceTechnician') THEN
		INSERT INTO role (Title, Print, Queue, TopQueue, Start, Stop, Restart, Status, ReadConfig, SetConfig) 
        VALUES (username, 0, 0, 0, 1, 1, 1, 1, 1, 1);
	END IF;
        
	IF(role = 'SuperUser') THEN
		INSERT INTO role (Title, Print, Queue, TopQueue, Start, Stop, Restart, Status, ReadConfig, SetConfig) 
        VALUES (username, 1, 1, 1, 0, 0, 1, 0, 0, 0);
	END IF;
        
	IF(role = 'User') THEN
		INSERT INTO role (Title, Print, Queue, TopQueue, Start, Stop, Restart, Status, ReadConfig, SetConfig) 
        VALUES (username, 1, 1, 0, 0, 0, 0, 0, 0, 0);
	END IF;
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
CREATE PROCEDURE GetPermissions
(IN username VARCHAR(64))
BEGIN
	SELECT * 
	FROM AccessControlMatrix
    WHERE AccessControlMatrix.user = username; 
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
