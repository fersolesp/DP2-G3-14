-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users(username,password,enabled) VALUES ('admin1','4dm1n',TRUE);
INSERT INTO authorities VALUES ('admin1','admin');
-- One owner user, named owner1 with passwor 0wn3r
INSERT INTO users(username,password,enabled) VALUES ('owner1','0wn3r',TRUE);
INSERT INTO authorities VALUES ('owner1','owner');
-- One vet user, named vet1 with passwor v3t
INSERT INTO users(username,password,enabled) VALUES ('vet1','v3t',TRUE);
INSERT INTO authorities VALUES ('vet1','veterinarian');

INSERT INTO users(username,password,enabled) VALUES ('owner2','0wn3r',TRUE);
INSERT INTO authorities VALUES ('owner2','owner');
INSERT INTO users(username,password,enabled) VALUES ('owner3','0wn3r',TRUE);
INSERT INTO authorities VALUES ('owner3','owner');
INSERT INTO users(username,password,enabled) VALUES ('owner4','0wn3r',TRUE);
INSERT INTO authorities VALUES ('owner4','owner');
INSERT INTO users(username,password,enabled) VALUES ('owner5','0wn3r',TRUE);
INSERT INTO authorities VALUES ('owner5','owner');
INSERT INTO users(username,password,enabled) VALUES ('owner6','0wn3r',TRUE);
INSERT INTO authorities VALUES ('owner6','owner');
INSERT INTO users(username,password,enabled) VALUES ('owner7','0wn3r',TRUE);
INSERT INTO authorities VALUES ('owner7','owner');
INSERT INTO users(username,password,enabled) VALUES ('owner8','0wn3r',TRUE);
INSERT INTO authorities VALUES ('owner8','owner');
INSERT INTO users(username,password,enabled) VALUES ('owner9','0wn3r',TRUE);
INSERT INTO authorities VALUES ('owner9','owner');
INSERT INTO users(username,password,enabled) VALUES ('owner10','0wn3r',TRUE);
INSERT INTO authorities VALUES ('owner10','owner');

INSERT INTO vets VALUES (1, 'James', 'Carter');
INSERT INTO vets VALUES (2, 'Helen', 'Leary');
INSERT INTO vets VALUES (3, 'Linda', 'Douglas');
INSERT INTO vets VALUES (4, 'Rafael', 'Ortega');
INSERT INTO vets VALUES (5, 'Henry', 'Stevens');
INSERT INTO vets VALUES (6, 'Sharon', 'Jenkins');

INSERT INTO specialties VALUES (1, 'radiology');
INSERT INTO specialties VALUES (2, 'surgery');
INSERT INTO specialties VALUES (3, 'dentistry');

INSERT INTO vet_specialties VALUES (2, 1);
INSERT INTO vet_specialties VALUES (3, 2);
INSERT INTO vet_specialties VALUES (3, 3);
INSERT INTO vet_specialties VALUES (4, 2);
INSERT INTO vet_specialties VALUES (5, 1);

INSERT INTO types VALUES (1, 'cat');
INSERT INTO types VALUES (2, 'dog');
INSERT INTO types VALUES (3, 'lizard');
INSERT INTO types VALUES (4, 'snake');
INSERT INTO types VALUES (5, 'bird');
INSERT INTO types VALUES (6, 'hamster');

INSERT INTO owners VALUES (1, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '6085551023', 'owner1');
INSERT INTO owners VALUES (2, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '6085551749', 'owner2');
INSERT INTO owners VALUES (3, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '6085558763', 'owner3');
INSERT INTO owners VALUES (4, 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '6085553198', 'owner4');
INSERT INTO owners VALUES (5, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '6085552765', 'owner5');
INSERT INTO owners VALUES (6, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '6085552654', 'owner6');
INSERT INTO owners VALUES (7, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '6085555387', 'owner7');
INSERT INTO owners VALUES (8, 'Maria', 'Escobito', '345 Maple St.', 'Madison', '6085557683', 'owner8');
INSERT INTO owners VALUES (9, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '6085559435', 'owner9');
INSERT INTO owners VALUES (10, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '6085555487', 'owner10');

INSERT INTO pets(id,name,birth_date,is_dangerous,is_vaccinated,type_id,owner_id) VALUES (1, 'Leo', '2010-09-07',false,true, 1, 1);
INSERT INTO pets(id,name,birth_date,is_dangerous,is_vaccinated,type_id,owner_id) VALUES (2, 'Basil', '2012-08-06',false,true, 6, 2);
INSERT INTO pets(id,name,birth_date,is_dangerous,is_vaccinated,type_id,owner_id) VALUES (3, 'Rosy', '2011-04-17',false,true, 2, 3);
INSERT INTO pets(id,name,birth_date,is_dangerous,is_vaccinated,type_id,owner_id) VALUES (4, 'Jewel', '2010-03-07',false,false, 2, 3);
INSERT INTO pets(id,name,birth_date,is_dangerous,is_vaccinated,type_id,owner_id) VALUES (5, 'Iggy', '2010-11-30',false,true, 3, 4);
INSERT INTO pets(id,name,birth_date,is_dangerous,is_vaccinated,type_id,owner_id) VALUES (6, 'George', '2010-01-20',false,true, 4, 5);
INSERT INTO pets(id,name,birth_date,is_dangerous,is_vaccinated,type_id,owner_id) VALUES (7, 'Samantha', '2012-09-04',false,true, 1, 6);
INSERT INTO pets(id,name,birth_date,is_dangerous,is_vaccinated,type_id,owner_id) VALUES (8, 'Max', '2012-09-04',false,true, 1, 6);
INSERT INTO pets(id,name,birth_date,is_dangerous,is_vaccinated,type_id,owner_id) VALUES (9, 'Lucky', '2011-08-06',false,true, 5, 7);
INSERT INTO pets(id,name,birth_date,is_dangerous,is_vaccinated,type_id,owner_id) VALUES (10, 'Mulligan', '2007-02-24',false,true, 2, 8);
INSERT INTO pets(id,name,birth_date,is_dangerous,is_vaccinated,type_id,owner_id) VALUES (11, 'Freddy', '2010-03-09',false,true, 5, 9);
INSERT INTO pets(id,name,birth_date,is_dangerous,is_vaccinated,type_id,owner_id) VALUES (12, 'Lucky', '2010-06-24',true,true, 2, 10);
INSERT INTO pets(id,name,birth_date,is_dangerous,is_vaccinated,type_id,owner_id) VALUES (13, 'Sly', '2012-06-08',false,true, 1, 10);

INSERT INTO visits(id,pet_id,visit_date,description) VALUES (1, 7, '2013-01-01', 'rabies shot');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (2, 8, '2013-01-02', 'rabies shot');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (3, 8, '2013-01-03', 'neutered');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (4, 7, '2013-01-04', 'spayed');

INSERT INTO ANNOUNCEMENT VALUES (1,'Anuncio1',true,'Hola','Fox',1,1);
INSERT INTO ANNOUNCEMENT VALUES (2,'Anuncio2',true,'Hola','Fufu',2,3);
INSERT INTO ANNOUNCEMENT VALUES (3,'Anuncio3',false,'Hola','Lulu',3,2);

INSERT INTO ANSWER VALUES (1,'Respuesta1','2010-03-09','Hola',1,1);
INSERT INTO ANSWER VALUES (2,'Respuesta2','2010-03-09','Hola',1,1);
INSERT INTO ANSWER VALUES (3,'Respuesta3','2010-03-09','Hola',2,1);

INSERT INTO TRAINER VALUES (1,'Adolfo Fernandez Ruiz');
INSERT INTO TRAINER VALUES (2,'Laura Rio Caballero');
INSERT INTO TRAINER VALUES (3,'Pedro Suarez García');

INSERT INTO PAYMENT VALUES(1,'Payment1',200.00,'2020-05-03');
INSERT INTO PAYMENT VALUES(2,'Payment2',25.00,'2020-05-03');
INSERT INTO PAYMENT VALUES(3,'Payment3',25.00,'2020-05-03');

INSERT INTO COURSE (ID,NAME,DANGEROUS_ALLOWED,FINISH_DATE,START_DATE,CAPACITY,PET_TYPE_ID,TRAINER_ID,COST) VALUES (1,'Curso para gatos',false,'2020-07-03','2020-06-03',20,1,1,200);
INSERT INTO COURSE (ID,NAME,DANGEROUS_ALLOWED,FINISH_DATE,START_DATE,CAPACITY,PET_TYPE_ID,TRAINER_ID,COST) VALUES (2,'Curso para perros',false,'2020-07-03','2020-06-03',20,2,2,150);
INSERT INTO COURSE (ID,NAME,DANGEROUS_ALLOWED,FINISH_DATE,START_DATE,CAPACITY,PET_TYPE_ID,TRAINER_ID,COST) VALUES (3,'Curso para perros peligrosos',true,'2020-07-03','2020-06-03',10,2,3,250);

INSERT INTO INSCRIPTION VALUES(1,'Inscription1','2020-05-03',true, 1,1,1,1);
INSERT INTO INSCRIPTION VALUES(2,'Inscription2','2020-05-03',false, 2,2,2,2);
INSERT INTO INSCRIPTION VALUES(3,'Inscription3','2020-05-03',false, 3,3,3,3);

