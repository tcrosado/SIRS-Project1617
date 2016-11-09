USE bank;
CREATE TABLE `customers`(
  `iban` VARCHAR(34) NOT NULL,
  `phoneNumber` VARCHAR(9) NOT NULL,
  primary key (`iban`));

CREATE TABLE conta(
  iban VARCHAR(34) NOT NULL,
  saldo FLOAT NOT NULL,
  PRIMARY KEY  (iban),
  FOREIGN KEY (iban) REFERENCES customers(iban));

CREATE TABLE historico(
  uid VARCHAR(36) NOT NULL,
  time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  originIban VARCHAR(34) NOT NULL,
  destIban VARCHAR(34) NOT NULL,
  valor FLOAT NOT NULL,
  PRIMARY KEY (uid),
  FOREIGN KEY (originIban) REFERENCES conta(iban),
  FOREIGN KEY (destIban) REFERENCES conta(iban)
);