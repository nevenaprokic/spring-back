/*Addresses*/
insert into address (street, city, state) values ('Omladinska 19', 'Novi Sad', 'Srbija');
insert into address (street, city, state) values ('Jevrejska 44', 'Beograd', 'Srbija');
insert into address (street, city, state) values ('Bulevar kralja Aleksandra 37', 'Beograd', 'Srbija');
insert into address (street, city, state) values ('Fruskogorksa 14', 'Novi Sad', 'Srbija');
insert into address (street, city, state) values ('Bulevar oslobodjenja 119', 'Novi Sad', 'Srbija');
insert into address (street, city, state) values ('Somborska 39', 'Novi Sad', 'Srbija');
insert into address (street, city, state) values ('Lasla Gala 4', 'Beograd', 'Srbija');
insert into address (street, city, state) values ('Bulevar kralja Petra 137', 'Beograd', 'Srbija');
insert into address (street, city, state) values ('Fruskogorksa 60', 'Novi Sad', 'Srbija');
insert into address (street, city, state) values ('Mise Dimitrijevica 19', 'Novi Sad', 'Srbija');

insert into address (street, city, state) values ('Kostovac', 'Kopaonik', 'Srbija');
insert into address (street, city, state) values ('Treska 5b', 'Kopaonik', 'Srbija');
/*ROLE*/
insert into role(name) values ('CLIENT');
insert into role(name) values ('ADMIN');
insert into role(name) values ('COTTAGE_OWNER');
insert into role(name) values ('SHIP_OWNER');
insert into role(name) values ('INSTRUCTOR');


insert into client_category (name, discount, reservation_points, low_limit_points, heigh_limit_points, category_color) values ('CASUAL_CLIENT', 0.0 , 1 , 0, 5, '#444444');
insert into client_category (name, discount, reservation_points, low_limit_points, heigh_limit_points, category_color) values ('CLOSE_CLIENT', 5.0, 2, 6, 10, '#7ca253');
insert into client_category (name, discount, reservation_points, low_limit_points, heigh_limit_points, category_color) values ('BEST_CLIENT', 10.0, 3, 11, 100, '#CC7351');

insert into owner_category(name, earnings, reservation_points, low_limit_points, heigh_limit_points, category_color) values ('REGULAR', 70.0, 1, 0, 10, '#444444');
insert into owner_category(name, earnings,  reservation_points, low_limit_points, heigh_limit_points, category_color) values ('SILVER', 80.0, 2, 11, 20, '#BBBBBB' );
insert into owner_category(name, earnings,  reservation_points, low_limit_points, heigh_limit_points, category_color) values ('GOLD', 90.0, 3, 21, 100 , '#fcdc3c' );


/*CLIENTS*/

insert into my_user (role_id, email_verified, email, first_name, last_name, password, phone_number, address_id, deleted) values (1, true, 'markoooperic123+pera@gmail.com', 'Petar', 'Peric', '$2a$10$vxfy.kTR4mQxDTIQ6LmeCuoea46hQ9KmeBTTSW4BQay4QD60nXo5K', '062-111-1111', 1, false);/*sifra*/
insert into my_user (role_id, email_verified, email, first_name, last_name, password, phone_number, address_id, deleted) values (1, true, 'markoooperic123+marko@gmail.com', 'Marko', 'Slavic', '$2a$10$.lyxc9BnJGhDQMcszD2/nuZsMdi1bjP1catCqpAa5AKc0rwtMaIJ.', '065-111-1112', 2, false);/*klijent2*/
insert into my_user (role_id, email_verified, email, first_name, last_name, password, phone_number, address_id, deleted) values (1, true, 'markoooperic123+sara@gmail.com', 'Sara', 'Mirkovic', '$2a$10$GLP6wpgzOsuq316wup3UlOX7HzPTT5F/86R5PcC/MHw.NYim3vD5a', '063-111-1113', 3, false);/*klijent3*/
insert into my_user (role_id, email_verified, email, first_name, last_name, password, phone_number, address_id, deleted) values (1, true, 'markoooperic123+lela@gmail.com', 'Lela', 'Mitic', '$2a$10$E6WPiiY.RPGnUNjUjOxMKONnwolnPF490yDx0ROx083y4rRwDgJum', '063-111-1114', 4, false);/*klijent4*/


insert into client (points, penal, id) values (0, 2, 1);
insert into client (points, penal, id) values (2, 0, 2);
insert into client (points, penal, id) values (7, 1, 3);
insert into client (points, penal, id) values (15, 0, 4);

/*COTTAGE OWNERS*/

insert into my_user (role_id, email_verified, email, first_name, last_name, password, phone_number, address_id, deleted) values (3, true, 'markoooperic123+mile@gmail.com', 'Mile', 'Kostic', '$2a$10$6HD4yHeA6yMoWmTuiFLw8ewp2TlXRwLsdu6CSkzYLLvFl.A17i5Cq', '063-111-1115', 5, false);/*vlasnik1*/
insert into my_user (role_id, email_verified, email, first_name, last_name, password, phone_number, address_id, deleted) values (3, true, 'markoooperic123+mara@gmail.com', 'Mara', 'Dabovic', '$2a$10$ZFhTzzIdX6.j47kW/wCaA.xIJjpt6LCq4ASEa/iUr78LTGIfJSsEC', '063-211-1166', 6, false);/*vlasnik2*/


insert  into owner(points, id) values (1, 5);
insert  into owner(points, id) values (6, 6);

insert into cottage_owner(id) values (5);
insert into cottage_owner(id) values (6);

/*SHIP OWNERS*/
insert into my_user (role_id, email_verified, email, first_name, last_name, password, phone_number, address_id, deleted) values (4, true, 'markoooperic123+nikola@gmail.com', 'Nikola', 'Nikic', '$2a$10$WVmXrwpduGmr0uLbVQ27zekrOvcpn9c6G3yEuTjbqkTBbD5xevs0i', '061-133-1113', 7, false);/*vlasnik3*/
insert into my_user (role_id, email_verified, email, first_name, last_name, password, phone_number, address_id, deleted) values (4, true, 'markoooperic123+ksenija@gmail.com', 'Ksenija', 'Sega', '$2a$10$zhlst/yvb/vPF7DXbLB0YeqjV2.tGRIQR1wftPMRYNRaHaPXKY7WW', '063-222-1113', 8, false);/*vlasnik4*/


insert  into owner(points, id) values (8, 7);
insert  into owner(points, id) values (3, 8);

insert into ship_owner(id) values (7);
insert into ship_owner(id) values (8);

/*INSTRUCTORS*/

insert into my_user (role_id, email_verified, email, first_name, last_name, password, phone_number, address_id, deleted) values (5, true, 'markoooperic123+milan@gmail.com', 'Milan', 'Lakovic', '$2a$10$/xjKdbSgVfsSHEeMbEZOJu7CVFb9dCoLcr/eParcvedbmKfOaeId2', '063-111-1333', 9, false);/*instr1*/
insert into my_user (role_id, email_verified, email, first_name, last_name, password, phone_number, address_id, deleted) values (5, true, 'markoooperic123+milica@gmail.com', 'Milica', 'Matic', '$2a$10$UwJxipMMlAZZRwxyEMuk/.2wn2UuFt.quxqu0Gj6vnGC.3goMKCu.', '063-111-1212', 10, false);/*instr2*/


insert  into owner(points, id) values (11, 9);
insert  into owner(points,  id) values (0, 10);

insert into instructor(biography, id) values ('Pecanjem se bavim od svoje 5 godine. Povedite drustvo i idemo na zabavu!',9);
insert into instructor(biography, id) values ('Svaki dan je nova avantura, pridruzi se mojoj!',10);

 /*ADMIN*/
insert into my_user (role_id, email_verified, email, first_name, last_name, password, phone_number, address_id, deleted) values (2, true, 'markoooperic123+++sava@gmail.com', 'Sava', 'Urosevic', '$2a$10$/6BuFPptMiQChPXx4GbDX.iVVpwXH9EMSVrAcbF3ZSjdt/JDMX826', '064-141-1113', 1, false);/*admin1*/

insert  into admin(id, default_admin, first_login) values (11, true, false);

/*ADDITIONAL SERVICES*/
insert into additional_service(name,price) values ('dorucak', 2);
insert into additional_service(name,price) values ('rucak', 5);
insert into additional_service(name,price) values ('klima', 5);
insert into additional_service(name,price) values ('vecera', 3);
insert into additional_service(name,price) values ('fen', 8);
insert into additional_service(name,price) values ('dorucak', 2);
insert into additional_service(name,price) values ('rucak', 5);
insert into additional_service(name,price) values ('klima', 5);
insert into additional_service(name,price) values ('vecera', 3);
insert into additional_service(name,price) values ('fen', 8);

/*PHOTO*/
insert into photo(path,deleted) values('pecanje6.jpg',false);
insert into photo(path,deleted) values('bela ladja1.jpg',false);
insert into photo(path,deleted) values('brvnara1.jpg',false);
insert into photo(path,deleted) values('brvnara2.jpg',false);
insert into photo(path,deleted) values('original.jpg',false);
insert into photo(path,deleted) values('pecanje1.jpg',false);
insert into photo(path,deleted) values('pecanje2.jpg',false);
insert into photo(path,deleted) values('pecanje3.jpg',false);
insert into photo(path,deleted) values('pecanje4.jpg',false);
insert into photo(path,deleted) values('pecanje5.jpg',false);
insert into photo(path,deleted) values('pecanje7.jpg',false);
insert into photo(path,deleted) values('pecanje8.jpg',false);
insert into photo(path,deleted) values('pecanje9.jpg',false);
insert into photo(path,deleted) values('pecanje10.jpg',false);

insert into photo(path,deleted) values('galeb.jpg',false);
insert into photo(path,deleted) values('sidro.jpeg',false);
insert into photo(path,deleted) values('jela1.jpg',false);
insert into photo(path,deleted) values('sumska vila1.jpg',false);
insert into photo(path,deleted) values('sumska vila2.jpg',false);
insert into photo(path,deleted) values('sumska vila3.jpg',false);
insert into photo(path,deleted) values('sumska vila4.jpg',false);
insert into photo(path,deleted) values('hmf1.jpg',false);
insert into photo(path,deleted) values('hmf2.jpg',false);

/*COTTAGE*/
insert into offer(cancellation_conditions, deleted, description, name, number_of_person, price, rules_of_conduct, address_id, number_of_reservations, number_of_quick_reservations, number_of_modify) values ('Dozvoljeno je otkazivanje 3 dana pre.',false,'Sjajan pogled na sumu i planinu! U blizini se nalazi ski staza.','Vikendica Raj',2,30,'Nije dozvoljeno bilo kakvo unistavanje imovine.',1, 20,2, 0);
insert into offer(cancellation_conditions, deleted, description, name, number_of_person, price, rules_of_conduct, address_id, number_of_reservations, number_of_quick_reservations, number_of_modify) values ('Dozvoljeno je otkazivanje 3 dana pre.',false,'Sjajan pogled na sumu i planinu!','Brvnara',4,40,'Nije dozvoljeno bilo kakvo unistavanje imovine.',2, 14, 1, 0);
insert into offer(cancellation_conditions, deleted, description, name, number_of_person, price, rules_of_conduct, address_id, number_of_reservations, number_of_quick_reservations, number_of_modify) values ('Dozvoljeno je otkazivanje 3 dana pre.',false,'Uzivajte u miru i tisini u prirodi.','Jela',3,25,'Nije dozvoljeno bilo kakvo unistavanje imovine.',3, 1, 0, 0);

insert into cottage(bed_number, room_number, id, my_user_id) values (2,2,1,5);
insert into cottage(bed_number, room_number, id, my_user_id) values (4,3,2,5);
insert into cottage(bed_number, room_number, id, my_user_id) values (3,2,3,6);

/*SHIP*/
insert into offer(cancellation_conditions, deleted, description, name, number_of_person, price, rules_of_conduct, address_id, number_of_reservations, number_of_quick_reservations, number_of_modify) values ('Dozvoljeno je otkazivanje 3 dana pre.',false,'Bela jahta na obali Dunava','Galeb',2,60,'Nije dozvoljeno bilo kakvo unistavanje imovine.',4, 0, 1, 0);
insert into offer(cancellation_conditions, deleted, description, name, number_of_person, price, rules_of_conduct, address_id, number_of_reservations, number_of_quick_reservations, number_of_modify) values ('Dozvoljeno je otkazivanje 3 dana pre.',false,'Uzivanje krstarenjem Dunava uz prijatnu palubu i kabinu za odmor.','Bela ladja',15,120,'Nije dozvoljeno bilo kakvo unistavanje imovine.',5, 1, 1, 0);
insert into offer(cancellation_conditions, deleted, description, name, number_of_person, price, rules_of_conduct, address_id, number_of_reservations, number_of_quick_reservations, number_of_modify) values ('Dozvoljeno je otkazivanje 3 dana pre.',false,'Uzivajte u zvucima talasanja vode!','Sidro',3,50,'Nije dozvoljeno bilo kakvo unistavanje imovine.',6, 1,1, 0);

insert into ship(additional_equipment, max_speed, motor_number, motor_power, navigation_equipment, size, type, id, ship_owner_id) values ('Ne poseduje pecarosku opremu',20,2,200,'GPS, VHF RADIO',8,'Jahta',4,7);
insert into ship(additional_equipment, max_speed, motor_number, motor_power, navigation_equipment, size, type, id, ship_owner_id) values ('Ne poseduje pecarosku opremu',20,2,200,'GPS, VHF RADIO',12,'Jahta',5,8);
insert into ship(additional_equipment, max_speed, motor_number, motor_power, navigation_equipment, size, type, id, ship_owner_id) values ('Poseduje pecarosku opremu',18,1,200,'GPS, VHF RADIO',7,'Jahta',6,8);

/*ADVENTURE*/
insert into offer(cancellation_conditions, deleted, description, name, number_of_person, price, rules_of_conduct, address_id, number_of_reservations, number_of_quick_reservations, number_of_modify) values ('Dozvoljeno je otkazivanje 3 dana pre.',false,'Uzivajte u pecanju ribe.','Pecanje na Ibru',5,6000,'Nije dozvoljeno bilo kakvo unistavanje imovine i prirode.',7, 3, 2, 0);
insert into offer(cancellation_conditions, deleted, description, name, number_of_person, price, rules_of_conduct, address_id, number_of_reservations, number_of_quick_reservations, number_of_modify) values ('Dozvoljeno je otkazivanje 3 dana pre.',false,'Pecanje kao vid opustanja','Avantura na Tisi',10,12000,'Nije dozvoljeno bilo kakvo unistavanje imovine i prirode.',8, 2, 0, 0);
insert into offer(cancellation_conditions, deleted, description, name, number_of_person, price, rules_of_conduct, address_id, number_of_reservations, number_of_quick_reservations, number_of_modify) values ('Dozvoljeno je otkazivanje 3 dana pre.',false,'Uzivajte u zvucima talasanja vode!','Avantura na Dunavu',3,5000,'Nije dozvoljeno bilo kakvo unistavanje imovine i prirode.',9, 0, 0, 0);
insert into offer(cancellation_conditions, deleted, description, name, number_of_person, price, rules_of_conduct, address_id, number_of_reservations, number_of_quick_reservations, number_of_modify) values ('Dozvoljeno je otkazivanje 3 dana pre.',false,'Uzivajte u zvucima talasanja vode!','Avantura na Savi',3,5000,'Nije dozvoljeno bilo kakvo unistavanje imovine i prirode.',10, 0, 0, 0);
insert into offer(cancellation_conditions, deleted, description, name, number_of_person, price, rules_of_conduct, address_id, number_of_reservations, number_of_quick_reservations, number_of_modify) values ('Dozvoljeno je otkazivanje 3 dana pre.',false,'Uzivajte u zvucima talasanja vode!','Mušičarenje',3,5000,'Nije dozvoljeno bilo kakvo unistavanje imovine i prirode.',10, 3, 0, 0);

insert into adventure(additional_equipment, id, my_user_id) values ('Poseduje pecarosku opremu',7,9);
insert into adventure(additional_equipment, id, my_user_id) values ('Ne poseduje pecarosku opremu',8,9);
insert into adventure(additional_equipment, id, my_user_id) values ('Poseduje pecarosku opremu',9,10);
insert into adventure(additional_equipment, id, my_user_id) values ('Poseduje pecarosku opremu',10,9);
insert into adventure(additional_equipment, id, my_user_id) values ('Poseduje pecarosku opremu',11,9);

insert into offer(cancellation_conditions, deleted, description, name, number_of_person, price, rules_of_conduct, address_id, number_of_reservations, number_of_quick_reservations, number_of_modify) values ('Dozvoljeno je otkazivanje 3 dana pre.',false,'Objekat Sumska vila se nalazi na Kopaoniku, na 6 km od ski-centra Kopaonik i nudi smeštaj sa barom, besplatnim WiFi internetom, zajedničkom kuhinjom i zajedničkim salonom','Sumska vila',8,120,'Nije dozvoljeno bilo kakvo unistavanje imovine.',11, 0, 0, 0);
insert into offer(cancellation_conditions, deleted, description, name, number_of_person, price, rules_of_conduct, address_id, number_of_reservations, number_of_quick_reservations, number_of_modify) values ('Dozvoljeno je otkazivanje 3 dana pre.',false,'Objekat Holiday Home Floris nudi smeštaj sa terasom i pogledom na planinu, a nalazi se na oko 6 km od ski-centra Kopaonik. Gosti ove vikendice mogu da koriste vrt, pribor za pripremu roštilja, besplatan WiFi i besplatan privatni parking.','Holiday Home Floris',5,150,'Nije dozvoljeno bilo kakvo unistavanje imovine.',12, 0, 0, 0);

insert into cottage(bed_number, room_number, id, my_user_id) values (4,4,12,5);
insert into cottage(bed_number, room_number, id, my_user_id) values (3,3,13,5);

/*RESERVATION*/
insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-04-18',4,600,'2022-04-17',1,7);

insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-06-11',2,300,'2022-06-10',2,1);
insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-04-25',3,250,'2022-04-17',3,3);
insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-04-11',2,300,'2022-04-10',2,1);
insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-04-25',3,250,'2022-04-17',3,2);
insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-06-01',2,300,'2022-05-29',2,5);
insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-05-25',3,250,'2022-05-17',3,6);

insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-06-02',2,300,'2022-05-24',2,1);
insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-06-29',2,300,'2022-06-27',3,1);
insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-06-07',3,250,'2022-05-25',3,2);


insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2021-08-18',4,670,'2021-07-17',1,7);
insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-03-18',4,600,'2022-03-17',2,7);
insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-02-18',4,650,'2022-02-17',1,8);
insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-07-22',4,600,'2022-07-17',2,8);
insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2021-07-18',4,800,'2021-07-17',1,9);

insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-01-11',2,300,'2022-01-10',2,1);
insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-02-25',3,250,'2022-02-17',3,2);
insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-02-11',2,300,'2022-02-10',2,1);
insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-03-25',3,250,'2022-03-17',3,2);
insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-04-01',2,300,'2022-03-29',2,1);
insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-04-25',3,250,'2022-04-17',3,12);

insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-07-11',2,300,'2022-07-10',2,1);
insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-07-25',3,250,'2022-07-17',3,2);
insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-07-20',2,300,'2022-07-15',2,1);
insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-08-25',3,250,'2022-08-17',3,2);
insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-08-01',2,300,'2022-07-29',2,1);
insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-08-25',3,250,'2022-08-17',3,12);

insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-07-08',2,300,'2022-07-01',2,1);
insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-07-08',3,250,'2022-07-06',3,2);
insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-08-20',2,300,'2022-08-15',2,1);
insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-09-25',3,250,'2022-09-17',3,2);
insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-09-01',2,300,'2022-08-29',2,1);
insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-09-25',3,250,'2022-09-17',3,12);

insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-01-09',2,300,'2022-01-01',2,1);
insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-01-27',2,300,'2022-01-20',2,1);
insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-01-09',2,300,'2022-01-01',1,2);
insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-01-27',2,300,'2022-01-20',1,2);

insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-02-09',2,300,'2022-02-01',2,1);
insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-02-27',2,300,'2022-02-20',2,1);
insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-02-09',2,300,'2022-02-01',1,2);
insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-02-28',2,300,'2022-02-26',1,2);

insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-04-09',2,300,'2022-04-03',2,1);
insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-04-27',2,300,'2022-04-20',2,1);
insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-04-19',2,300,'2022-04-14',2,1);
insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-04-09',2,300,'2022-04-01',1,2);

insert into reservation(deleted,end_date,number_of_person,price,start_date,my_user_id,offer_id) values (false,'2022-05-08',2,300,'2022-05-06',1,2);

/*MARK*/
insert into mark(approved, comment, mark, reservation_id, sending_time, reviewed) values (false,'Odlican ambijent!',5,3, '2022-05-09', false);
insert into mark(approved, comment, mark, reservation_id, sending_time, reviewed) values (false ,'Onako!',3,18, '2022-04-29', false);
insert into mark(approved, comment, mark, reservation_id, sending_time, reviewed) values (false ,'Nista posebno!',4,6, '2022-05-31', false);
insert into mark(approved, comment, mark, reservation_id, sending_time, reviewed) values (true,'okej!',4,10, '2022-05-19', true);
insert into mark(approved, comment, mark, reservation_id, sending_time, reviewed) values (true,'Odlican ambijent!',5,11, '2022-05-09', true);

insert into mark(approved, comment, mark, reservation_id, sending_time, reviewed) values (true,'Lep provod, nije dobar ambijent!',4,12, '2022-04-09', true);
insert into mark(approved, comment, mark, reservation_id, sending_time, reviewed) values (true,'Odlican avantura!',5,14, '2022-05-01', true);
insert into mark(approved, comment, mark, reservation_id, sending_time, reviewed) values (true,'Sjajno!',5,15, '2022-05-26', true);
insert into mark(approved, comment, mark, reservation_id, sending_time, reviewed) values (true,'Nista posebno!',3,16, '2022-05-27', true);

/*DELETE REQUEST*/
insert into delete_request(description, my_user_id, deleted) values('Ne zelim vise da poslujem!',7, false);
insert into delete_request(description, my_user_id, deleted) values('Ne svidjaju mi se ponude!',4, false);
/*QUICK RESERVATION*/

insert into quick_reservation(end_date,end_date_action,number_of_person,price,start_date,start_date_action,offer_id, deleted) values('2022-05-26','2022-06-20',2,250,'2022-05-23','2022-05-01',6, false);
insert into quick_reservation(end_date,end_date_action,number_of_person,price,start_date,start_date_action,offer_id, deleted) values('2022-07-26','2022-07-17',2,255,'2022-07-23','2022-05-02',1, false);

insert into quick_reservation(end_date,end_date_action,number_of_person,price,start_date,start_date_action,offer_id, deleted) values('2022-05-18','2022-05-10',10,1000,'2022-05-15','2022-05-02',2, false);
insert into quick_reservation(end_date,end_date_action,number_of_person,price,start_date,start_date_action,offer_id, deleted) values('2022-06-26','2022-05-25',2,257,'2022-06-23','2022-05-15',1, false);
insert into quick_reservation(end_date,end_date_action,number_of_person,price,start_date,start_date_action,offer_id, deleted) values('2022-08-26','2022-07-30',2,250,'2022-08-23','2022-05-30',5, false);
insert into quick_reservation(end_date,end_date_action,number_of_person,price,start_date,start_date_action,offer_id, deleted) values('2022-05-26','2022-05-20',2,250,'2022-05-23','2022-05-01',4, false);
/*REGISTRATION REQUEST*/
/*id, deleted, description, email, first_name, last_name, password, person_type,phone_number, address_id*/
insert into registration_request(deleted, description, email,first_name,last_name,password,person_type,phone_number,address_id, sending_time) values(false, 'Zelim da poslujem u ovoj aplikaciji jer sam iskusni pecaros', 'natasha.lakovic@gmail.com', 'Natasa', 'Lakovic', '$2a$10$vxfy.kTR4mQxDTIQ6LmeCuoea46hQ9KmeBTTSW4BQay4QD60nXo5K', 'INSTRUCTOR', '111-111-1111',10, '2022-05-05');/*sifra*/
insert into registration_request(deleted, description, email,first_name,last_name,password,person_type,phone_number,address_id, sending_time) values(false, 'Imam veoma moderne vikendice koje zelim da izdajem', 'antic4235@gmail.com', 'Ivan', 'Antic', '$2a$10$vxfy.kTR4mQxDTIQ6LmeCuoea46hQ9KmeBTTSW4BQay4QD60nXo5K', 'COTTAGE_OWNER', '123-132-1231',10, '2022-05-06');/*sifra*/

/*UNAVAILABLE OFFER DATES*/
insert into unavailable_offer_dates(offer_id, start_date, end_date) values (1, '2022-06-15', '2022-06-20');
insert into unavailable_offer_dates(offer_id, start_date, end_date) values (7, '2022-06-27', '2022-06-29');
insert into unavailable_offer_dates(offer_id, start_date, end_date) values (7, '2022-06-10', '2022-06-16');


/*RESERVATION REPORT*/
insert into reservation_report(automatically_penal, comment, penal_option, reservation_id, reviewed, sent_date) values(false,'Jako prijatni ljudi',false,6, false, '2022-06-05');
insert into reservation_report(automatically_penal, comment, penal_option, reservation_id, reviewed, sent_date)values(false,'Jako neprijatni ljudi, slomili su pola tanjira i casa i odbilid da to nadoknade.',true,3, false, '2022-04-23');
insert into reservation_report(automatically_penal, comment, penal_option, reservation_id, reviewed, sent_date)values(false,'Doveli su ljubimce iako to nije dozvoljeno',true,1, false, '2022-04-20');
insert into reservation_report(automatically_penal, comment, penal_option, reservation_id, reviewed, sent_date)values(false,'Pravili su zurku iako je yabranjena muzika posle 10',true,7, false, '2022-05-29');

/*ADDITIONAL SERVICES*/
insert into additional_service(name,price) values ('dorucak', 10);
insert into additional_service(name,price) values ('rucak', 20);
insert into additional_service(name,price) values ('klima', 30);
insert into additional_service(name,price) values ('vecera', 30);
insert into additional_service(name,price) values ('fen', 10);

/*QUICK RESERVATION*/
insert into quick_reservation(end_date,end_date_action,number_of_person,price,start_date,start_date_action,offer_id,deleted) values('2022-04-26','2022-04-20',2,250,'2022-04-23','2022-04-11',7, false);
insert into quick_reservation(end_date,end_date_action,number_of_person,price,start_date,start_date_action,offer_id,deleted) values('2022-05-10','2022-05-01',10,1000,'2022-05-08','2022-04-25',7, false);

/*TRANSACTION*/
insert into transaction (date,price,reservation_id) values ('2022-04-01',600,1);
insert into transaction (date,price,reservation_id) values ('2022-03-27',300,2);
insert into transaction (date,price,reservation_id) values ('2022-03-29',250,3);

/*OFFER ADDITIONAL SERVICES*/
insert into offer_additional_services(offer_id,additional_services_id) values (1,1);
insert into offer_additional_services(offer_id,additional_services_id) values (1,2);
insert into offer_additional_services(offer_id,additional_services_id) values (1,3);
insert into offer_additional_services(offer_id,additional_services_id) values (5,5);
insert into offer_additional_services(offer_id,additional_services_id) values (5,6);
insert into offer_additional_services(offer_id,additional_services_id) values (6,4);
insert into offer_additional_services(offer_id,additional_services_id) values (6,7);
insert into offer_additional_services(offer_id,additional_services_id) values (7,8);
--
-- /*OFFER PHOTO*/
insert into offer_photos(offer_id,photos_id) values (9,1);
insert into offer_photos(offer_id,photos_id) values (5,2);
insert into offer_photos(offer_id,photos_id) values (2,3);
insert into offer_photos(offer_id,photos_id) values (2,4);
insert into offer_photos(offer_id,photos_id) values (1,5);

insert into offer_photos(offer_id,photos_id) values (7,6);
insert into offer_photos(offer_id,photos_id) values (7,7);
insert into offer_photos(offer_id,photos_id) values (7,8);
insert into offer_photos(offer_id,photos_id) values (8,9);
insert into offer_photos(offer_id,photos_id) values (8,10);
insert into offer_photos(offer_id,photos_id) values (9,11);

insert into offer_photos(offer_id,photos_id) values (10,12);
insert into offer_photos(offer_id,photos_id) values (10,13);
insert into offer_photos(offer_id,photos_id) values (11,14);

insert into offer_photos(offer_id,photos_id) values (4,15);
insert into offer_photos(offer_id,photos_id) values (6,16);
insert into offer_photos(offer_id,photos_id) values (3,17);

insert into offer_photos(offer_id,photos_id) values (12,18);
insert into offer_photos(offer_id,photos_id) values (12,19);
insert into offer_photos(offer_id,photos_id) values (12,20);
insert into offer_photos(offer_id,photos_id) values (12,21);

insert into offer_photos(offer_id,photos_id) values (13,22);
insert into offer_photos(offer_id,photos_id) values (13,23);

/*COMPLAINT*/
insert into complaint(my_user_id, reservation_id, text, deleted, recived_time) values (2, 20, 'Vlasnik je bio veoma neljubazan, ne zelimo vise da dolazimo na takva mesta.', false, '2022-06-01');
insert into complaint(my_user_id, reservation_id, text, deleted,  recived_time) values (3, 21, 'Lose ophodjenje prema gostima.', false, '2022-06-02');
insert into complaint(my_user_id, reservation_id, text, deleted, recived_time) values (1, 45, 'Veoma smo nezadovoljni saradnjom. Uslovi nisu bili kao na slikama.', false, '2022-05-30');
insert into complaint(my_user_id, reservation_id, text, deleted, recived_time) values (1, 46, 'Ovaj vlasnik je jako neljubazan i nekulturan covek!', false, '2022-05-31');

-- /*OWNER TRANSACTION*/
-- insert into owner_transaction(owner_id,transaction_id) values (9,1);
-- insert into owner_transaction(owner_id,transaction_id) values (5,2);
-- insert into owner_transaction(owner_id,transaction_id) values (6,3);

-- -- /*TRANSACTION*/
-- -- insert into transaction (date,price,reservation_id) values ('2022-04-01',6000,1);
-- -- insert into transaction (date,price,reservation_id) values ('2022-03-27',3000,2);
-- -- insert into transaction (date,price,reservation_id) values ('2022-03-29',2500,3);
-- --
-- --
-- -- /*OFFER ADDITIONAL SERVICES*/
-- -- insert into offer_additional_services(offer_id,additional_services_id) values (1,1);
-- -- insert into offer_additional_services(offer_id,additional_services_id) values (1,2);
-- -- insert into offer_additional_services(offer_id,additional_services_id) values (1,3);
-- -- insert into offer_additional_services(offer_id,additional_services_id) values (2,3);
-- -- insert into offer_additional_services(offer_id,additional_services_id) values (3,1);
-- -- insert into offer_additional_services(offer_id,additional_services_id) values (3,4);
-- -- insert into offer_additional_services(offer_id,additional_services_id) values (4,4);
-- -- insert into offer_additional_services(offer_id,additional_services_id) values (4,2);
-- -- insert into offer_additional_services(offer_id,additional_services_id) values (5,4);
-- -- insert into offer_additional_services(offer_id,additional_services_id) values (6,4);
-- -- insert into offer_additional_services(offer_id,additional_services_id) values (7,2);
-- -- insert into offer_additional_services(offer_id,additional_services_id) values (9,2);
-- --
-- --
-- -- /*OWNER TRANSACTION*/
-- -- insert into owner_transaction(owner_id,transaction_id) values (9,1);
-- -- insert into owner_transaction(owner_id,transaction_id) values (5,2);
-- -- insert into owner_transaction(owner_id,transaction_id) values (6,3);

-- --
-- -- /*QUICK RESERVATION ADDITIONAL SERVICES*/
-- -- insert into quick_reservation_additional_services(quick_reservation_id,additional_services_id) values (1,1);
-- -- insert into quick_reservation_additional_services(quick_reservation_id,additional_services_id) values (1,2);
-- -- insert into quick_reservation_additional_services(quick_reservation_id,additional_services_id) values (1,4);
-- -- insert into quick_reservation_additional_services(quick_reservation_id,additional_services_id) values (2,1);
-- --
-- -- /*RESERVATION ADDITIONAL SERVICES*/
-- -- insert into reservation_additional_services(reservation_id,additional_services_id) values (7,1);
-- -- insert into reservation_additional_services(reservation_id,additional_services_id) values (1,1);
-- -- insert into reservation_additional_services(reservation_id,additional_services_id) values (1,2);
-- -- insert into reservation_additional_services(reservation_id,additional_services_id) values (1,5);
-- -- insert into reservation_additional_services(reservation_id,additional_services_id) values (3,1);
-- --
-- -- /*SUBSCRIBE*/
insert into subscribe(my_user_id, offer_id) values (1,1);
-- -- insert into subscribe(my_user_id, offer_id) values (1,3);
-- -- insert into subscribe(my_user_id, offer_id) values (1,5);
-- -- insert into subscribe(my_user_id, offer_id) values (1,7);
-- -- insert into subscribe(my_user_id, offer_id) values (2,2);
-- -- insert into subscribe(my_user_id, offer_id) values (3,3);
-- -- insert into subscribe(my_user_id, offer_id) values (3,4);
-- -- insert into subscribe(my_user_id, offer_id) values (4,9);
-- -- insert into subscribe(my_user_id, offer_id) values (4,6);
-- --
