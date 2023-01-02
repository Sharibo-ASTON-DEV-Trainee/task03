CREATE TABLE alexej_krawez_todo_db.users (
  user_id int(9) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  email char(50) NOT NULL,
  login char(30) NOT NULL,
  password char(30) NOT NULL
) ENGINE=InnoDB;
CREATE UNIQUE INDEX users_user_id_uindex ON alexej_krawez_todo_db.users (user_id);
CREATE UNIQUE INDEX users_email_uindex ON alexej_krawez_todo_db.users (email);
CREATE UNIQUE INDEX users_login_uindex ON alexej_krawez_todo_db.users (login);
SET time_zone = '+00:00';

CREATE TABLE alexej_krawez_todo_db.notes (
  user_id int(9) NOT NULL,
  id int(9) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  date DATETIME DEFAULT CURRENT_TIMESTAMP,
  target_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  note TEXT(500) DEFAULT NULL,
  file_path varchar(255) DEFAULT NULL,
  status TINYINT(1) DEFAULT 1 NOT NULL,
  FOREIGN KEY notes_users__fk(user_id)
  REFERENCES users(user_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB;
CREATE UNIQUE INDEX notes_id_uindex ON notes (id);


INSERT INTO alexej_krawez_todo_db.users (email, login, password)
VALUES('tomas24@mail.ru', 'Tomas', 'usaForever123');


INSERT INTO alexej_krawez_todo_db.users (email, login, `password`)
VALUES('ivan_pupkin@gmail.com', 'Pupkin', 'donotpupkin');
-- 1
INSERT INTO alexej_krawez_todo_db.notes (user_id, note, `date`, target_date, file_path, status)
VALUES(2, 'breakfast', '2022-12-30 09:26:00', '2022-12-31 09:26:00', '8888Bacon_sandwich.jpg', 3);
-- 2
INSERT INTO alexej_krawez_todo_db.notes (user_id, note, `date`, target_date, file_path, status)
VALUES(2, 'training', '2022-12-30 09:28:00', '2022-12-31 09:28:00', 'null', 3);
-- 3
INSERT INTO alexej_krawez_todo_db.notes (user_id, note, `date`, target_date, file_path, status)
VALUES(2, 'lunch', '2022-12-30 09:40:34', '2022-12-31 09:40:34', '6633Pasta_salad.jpg', 3);
-- 4
INSERT INTO alexej_krawez_todo_db.notes (user_id, note, `date`, target_date, file_path, status)
VALUES(2, 'KENO: 18 21 23 2 49 37 26 43 41 45\\n20 52 28 47 17 5 30 25 48 38', '2022-12-30 10:42:16', '2022-12-31 10:42:16', 'null', 3);
-- 5
INSERT INTO alexej_krawez_todo_db.notes (user_id, note, `date`, target_date, file_path, status)
VALUES(2, 'training', '2022-12-30 10:48:48', '2022-12-31 10:48:48', 'null', 3);
-- 6
INSERT INTO alexej_krawez_todo_db.notes (user_id, note, `date`, target_date, file_path, status)
VALUES(2, 'dinner', '2022-12-30 10:54:03', '2022-12-31 10:54:03', '2212Ratatouille_sheet_pan_dinner_with_sausage.png', 3);
-- 7
INSERT INTO alexej_krawez_todo_db.notes (user_id, note, `date`, target_date, file_path, status)
VALUES(2, 'anime: megalo box s03-06', '2022-12-30 11:01:28', '2022-12-31 11:01:28', 'null', 3);


INSERT INTO alexej_krawez_todo_db.users (email, login, password)
VALUES('375251881818@invest.net', 'InvestProject', 'qUia2afWD3ql1p');
-- 8
INSERT INTO alexej_krawez_todo_db.notes (user_id, note, file_path, status)
VALUES(3, 'Sokolov.A. - 1_523.03', 'Sokolov.A..xml', 1);
-- 9
INSERT INTO alexej_krawez_todo_db.notes (user_id, note, file_path, status)
VALUES(3, 'Pupkin.I. - 300.03', 'Pupkin.I..xml', 1);
-- 10
INSERT INTO alexej_krawez_todo_db.notes (user_id, note, file_path, status)
VALUES(3, 'Koroleva.Z. - 3_000_000_000.64', 'Koroleva.Z..xml', 1);
-- 11
INSERT INTO alexej_krawez_todo_db.notes (user_id, note, file_path, status)
VALUES(3, 'Ihtiandr.O. - 0.00', 'Ihtiandr.O..xml', 1);
-- 12
INSERT INTO alexej_krawez_todo_db.notes (user_id, note, file_path, status)
VALUES(3, '\\"OAO\\" FinTrest - 236_283_758_938.34', 'OAO_FinTrest.doc', 1);
-- 13
INSERT INTO alexej_krawez_todo_db.notes (user_id, note, file_path, status)
VALUES(3, 'Belyablya.B. - 888_666.22', 'Belyablya.B..xml', 1);
-- 14
INSERT INTO alexej_krawez_todo_db.notes (user_id, note, file_path, status)
VALUES(3, 'Ostrouhin.L. - -2_000.00', 'Ostrouhin.L..xml', 3);
-- 15
INSERT INTO alexej_krawez_todo_db.notes (user_id, note, file_path, status)
VALUES(3, 'Ostrouhin.L. - -2_000.00', 'Ostrouhin.L..xml', 3);
-- 16
INSERT INTO alexej_krawez_todo_db.notes (user_id, note, file_path, status)
VALUES(3, 'Ostrouhin.L. - -2_000.00', 'Ostrouhin.L..xml', 3);
-- 17
INSERT INTO alexej_krawez_todo_db.notes (user_id, note, file_path, status)
VALUES(3, 'Ostrouhin.L. - -4_000.00', 'Ostrouhin.L..xml', 3);
-- 18
INSERT INTO alexej_krawez_todo_db.notes (user_id, note, file_path, status)
VALUES(3, 'Druzj.J. - 15_374.13', 'Druzj.J..xml', 2);
-- 19
INSERT INTO alexej_krawez_todo_db.notes (user_id, note, file_path, status)
VALUES(3, 'Kraken.R. - 1_437.20', 'Kraken.R..xml', 2);
-- 20
INSERT INTO alexej_krawez_todo_db.notes (user_id, note, file_path, status)
VALUES(3, 'Romanenko.O. - 32.11', 'Romanenko.O..xml', 2);
-- 21
INSERT INTO alexej_krawez_todo_db.notes (user_id, note, file_path, status)
VALUES(3, 'Ivanov.I. - 184.10', 'Ivanov.I..xml', 2);
-- 22
INSERT INTO alexej_krawez_todo_db.notes (user_id, note, file_path, status)
VALUES(3, 'Chromova.S. - 0.00', 'Chromova.S..xml', 4);
-- 23
INSERT INTO alexej_krawez_todo_db.notes (user_id, note, file_path, status)
VALUES(3, 'Chromova.T. - 0.00', 'Chromova.T..xml', 4);
-- 24
INSERT INTO alexej_krawez_todo_db.notes (user_id, note, file_path, status)
VALUES(3, '\\"OOO\\"Trikontinental - 19_293_294.59', 'OOO_Trikontinental.doc', 5);
-- 25
INSERT INTO alexej_krawez_todo_db.notes (user_id, note, file_path, status)
VALUES(3, 'Gruntov.G. - 999.99', 'Gruntov.G..xml', 4);
-- 26
INSERT INTO alexej_krawez_todo_db.notes (user_id, note, file_path, status)
VALUES(3, 'Lionidov.P. - 69.96', 'Lionidov.P..xml', 4);
-- 27
INSERT INTO alexej_krawez_todo_db.notes (user_id, note, file_path, status)
VALUES(3, 'Wertihvostov.K. - 66.6', 'Wertihvostov.K..xml', 2);