CREATE DATABASE IF NOT EXISTS AuthenticationLab; 
USE AuthenticationLab;

CREATE TABLE IF NOT EXISTS AccessControlMatrix(
    UserID INT UNSIGNED NOT NULL,
	Print BOOLEAN DEFAULT false,
    Queue BOOLEAN DEFAULT false,
    TopQueue BOOLEAN DEFAULT false,
    Start BOOLEAN DEFAULT false,
    Stop BOOLEAN DEFAULT false,
    Restart BOOLEAN DEFAULT false,
    Status BOOLEAN DEFAULT false,
    ReadConfig BOOLEAN DEFAULT false,
    SetConfig BOOLEAN DEFAULT false,
    FOREIGN KEY (UserID)
		REFERENCES Account(ID)
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
(IN username VARCHAR(64), IN password VARCHAR(64), IN salt VARCHAR(32), IN VARCHAR(20), OUT id INT)
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
CREATE PROCEDURE GetPermissions
(IN username VARCHAR(64))
BEGIN
	SELECT * 
	FROM AccessControlMatrix
    WHERE AccessControlMatrix.user = username; 
END$$
DELIMITER ;

INSERT INTO role (Title, Print, Queue, TopQueue, Start, Stop, Restart, Status, ReadConfig, SetConfig) VALUES 
('Admin', 1, 1, 1, 1, 1, 1, 1, 1, 1),
('ServiceTechnician', 0, 0, 0, 1, 1, 1, 1, 1, 1),
('SuperUser', 1, 1, 1, 0, 0, 1, 0, 0, 0),
('User', 1, 1, 0, 0, 0, 0, 0, 0, 0)
;

CALL RegisterAccount('Alice', '','', @result);
CALL RegisterAccount('Bob', '','', @result);
CALL RegisterAccount('Cecilia', '','', @result);
CALL RegisterAccount('Bob', '','', @result);
CALL RegisterAccount('David', '','', @result);
CALL RegisterAccount('Erica', '','', @result);
CALL RegisterAccount('Fred', '','', @result);
CALL RegisterAccount('George', '','', @result);