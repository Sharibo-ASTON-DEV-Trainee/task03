CREATE DATABASE IF NOT EXISTS alexej_krawez_bank_db CHARACTER SET utf8 COLLATE utf8_general_ci;
USE alexej_krawez_bank_db;
CREATE TABLE alexej_krawez_bank_db.clients(
  client_id int(9) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  first_name varchar(50) NOT NULL,
  last_name varchar(50) NOT NULL,
  pass_series char(2) NOT NULL,
  pass_id int(7) NOT NULL,
  email char(50) NOT NULL
) ENGINE=InnoDB;
CREATE UNIQUE INDEX clients_client_id_uindex ON alexej_krawez_bank_db.clients (client_id);
CREATE UNIQUE INDEX clients_pass_id_uindex ON alexej_krawez_bank_db.clients (pass_id);
CREATE UNIQUE INDEX clients_email_uindex ON alexej_krawez_bank_db.clients (email);
SET time_zone = '+00:00';
CREATE TABLE alexej_krawez_bank_db.cards (
  client_id int(12) NOT NULL,
  id int(12) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  card_type ENUM('VISA', 'MASTERCARD') DEFAULT 'VISA' NOT NULL,
  card_number char(16) NOT NULL,
  money DECIMAL(12, 2) NOT NULL,
  date_start DATE DEFAULT (CURRENT_DATE),
  date_end DATE DEFAULT (CURRENT_DATE + INTERVAL 3 YEAR),
  FOREIGN KEY cards_clients__fk(client_id)
  REFERENCES clients(client_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB;
CREATE UNIQUE INDEX cards_id_uindex ON alexej_krawez_bank_db.cards (id);