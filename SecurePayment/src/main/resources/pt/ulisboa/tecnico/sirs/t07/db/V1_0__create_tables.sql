USE bank;

CREATE TABLE `customers`(
`phoneNumber` VARCHAR(9) NOT NULL,
  iv VARCHAR(24) NOT NULL,
`bankCode` VARCHAR(44) NOT NULL,
primary key (`phoneNumber`));

CREATE TABLE phone_iban(
  iban VARCHAR(34) NOT NULL,
  `phoneNumber` VARCHAR(9) NOT NULL,
  primary key (iban),
  FOREIGN KEY (`phoneNumber`) REFERENCES `customers`(`phoneNumber`));


CREATE TABLE accounts(
  iban VARCHAR(34) NOT NULL,
  balance INT NOT NULL,
  PRIMARY KEY  (iban),
  FOREIGN KEY (iban) REFERENCES phone_iban(iban));

CREATE TABLE transactionHistory(
  tid VARCHAR(36) NOT NULL,
  time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  originIban VARCHAR(34) NOT NULL,
  destIban VARCHAR(34) NOT NULL,
  value INT NOT NULL,
  PRIMARY KEY (tid),
  FOREIGN KEY (originIban) REFERENCES accounts(iban),
  FOREIGN KEY (destIban) REFERENCES accounts(iban)
);


CREATE TABLE bank.pendingTransactions(
  tid VARCHAR(36) NOT NULL,
  time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  originIban VARCHAR(34) NOT NULL,
  destIban VARCHAR(34) NOT NULL,
  value INT NOT NULL,
  PRIMARY KEY (tid),
  FOREIGN KEY (originIban) REFERENCES accounts(iban),
  FOREIGN KEY (destIban) REFERENCES accounts(iban)
);

CREATE TABLE `bank`.`pendingChallenges`(
  tid VARCHAR(36) NOT NULL,
  `order` INT NOT NULL,
  `row` ENUM('A','B','C','D','E','F','G','H') NOT NULL,
  `column` INT NOT NULL,
  position INT NOT NULL,
  FOREIGN KEY (tid) REFERENCES bank.pendingTransactions(tid)
  ON DELETE CASCADE
  ON UPDATE NO ACTION
);

CREATE TABLE `bank`.`accountmatrix` (
  `row` ENUM('A','B','C','D','E','F','G','H') NOT NULL,
  `column` INT NOT NULL,
  `value` INT NULL,
  `iban` VARCHAR(34) NOT NULL,
  PRIMARY KEY (`row`, `column`, `iban`),
  INDEX `ibanMatrix_idx` (`iban` ASC),
  CONSTRAINT `ibanMatrix`
    FOREIGN KEY (`iban`)
    REFERENCES `bank`.`accounts` (`iban`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
COMMENT = 'Table that stores matrix cards information';