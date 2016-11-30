USE bank;
CREATE TABLE `customers`(
  `iban` VARCHAR(34) NOT NULL,
  `phoneNumber` VARCHAR(9) NOT NULL,
  primary key (`iban`));

CREATE TABLE accounts(
  iban VARCHAR(34) NOT NULL,
  balance DOUBLE NOT NULL,
  PRIMARY KEY  (iban),
  FOREIGN KEY (iban) REFERENCES customers(iban));

CREATE TABLE transactionHistory(
  tid VARCHAR(36) NOT NULL,
  time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  originIban VARCHAR(34) NOT NULL,
  destIban VARCHAR(34) NOT NULL,
  value DOUBLE NOT NULL,
  PRIMARY KEY (tid),
  FOREIGN KEY (originIban) REFERENCES accounts(iban),
  FOREIGN KEY (destIban) REFERENCES accounts(iban)
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