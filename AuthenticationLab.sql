CREATE DATABASE IF NOT EXISTS AuthenticationLab; 
USE AuthenticationLab;

CREATE TABLE IF NOT EXISTS Account(
	ID INT UNSIGNED NOT NULL AUTO_INCREMENT,
    Username VARCHAR(64) NOT NULL,
	Password VARCHAR(64) NOT NULL, -- Length of SHA-256 hash
    	PRIMARY KEY (ID)
);

DELIMITER $$
CREATE PROCEDURE RegisterAccount
(IN username VARCHAR(64), IN password VARCHAR(64), OUT id INT)
BEGIN
	INSERT INTO account (username, Password)
    VALUES (username, Password);
    SET id = LAST_INSERT_ID();
END$$
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE AuthenticateUser
(IN username VARCHAR(64), IN password VARCHAR(64))
BEGIN
	SELECT EXISTS(
		SELECT account.id
		FROM account
		WHERE account.username = username && account.password = password
    );
END$$
DELIMITER ;

CALL RegisterAccount('admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', @result);
CALL RegisterAccount('user', '04f8996da763b7a969b1028ee3007569eaf3a635486ddab211d512c85b9df8fb', @result);

CALL AuthenticateUser('admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918'); -- Password=admin
CALL AuthenticateUser('user', '04f8996da763b7a969b1028ee3007569eaf3a635486ddab211d512c85b9df8fb'); -- Password=user

SELECT * FROM Account;
