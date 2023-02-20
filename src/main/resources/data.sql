INSERT INTO service (service_type)
VALUES ('IP-TV'),
       ('Internet'),
       ('Telephone'),
       ('Cabel TV');

INSERT INTO tariff (name, description, cost, frequency_of_payment)
VALUES ('IP-TV1', 'best ip-tv', 120, 28),
       ('Super Internet', 'best internet', 180, 28),
       ('IP-TV + Super Internet', 'best ip-tv and best internet', 250, 28),
       ('Basic Telephone', 'basic telephone', 50, 28),
       ('Super Telephone', 'super telephone', 80, 28),
       ('Cabel TV + Telephone', 'cabel tv + telephone', 100, 28);

INSERT INTO tariff_services(tariff_id, services_id)
VALUES (1, 1),
       (2, 2),
       (3, 1),
       (3, 2),
       (4, 3),
       (5, 3),
       (6, 3),
       (6, 4);


INSERT INTO "user" (email, pass, registration_date, user_role, user_balance, firstname,
                    /*middle_name,*/ surname, telephone_number)
VALUES ('example1@gmail.com', '$2a$10$8Z/FaHQXO2Wysrl56d1G4.t0is4eJ1uMTmDbT4XNFGgv2KZxGghA2',
        CURRENT_DATE, DEFAULT, 680, 'Vasya', /*'Ivanovich',*/ 'Pupkin', '+380634325657'),
       ('manager@gmail.com', '$2a$10$8Z/FaHQXO2Wysrl56d1G4.t0is4eJ1uMTmDbT4XNFGgv2KZxGghA2',
        CURRENT_DATE, 'admin', DEFAULT, 'Kiriil', /*'Bubenovich',*/ 'Karapuzin',
        '+380634325657'),
       ('admin@gmail.com', '$2a$10$8Z/FaHQXO2Wysrl56d1G4.t0is4eJ1uMTmDbT4XNFGgv2KZxGghA2',
        CURRENT_DATE, 'main_admin', DEFAULT, 'Ivan', /*'Kulebovich',*/ 'Antonov',
        '+380764325621'),
       ('example2@gmail.com', '$2a$10$8Z/FaHQXO2Wysrl56d1G4.t0is4eJ1uMTmDbT4XNFGgv2KZxGghA2',
        CURRENT_DATE, DEFAULT, 500, 'Danya', /*'Ivanovich',*/ 'Karapuzov', '+380634325657'),
       ('example3@gmail.com', '$2a$10$8Z/FaHQXO2Wysrl56d1G4.t0is4eJ1uMTmDbT4XNFGgv2KZxGghA2',
        CURRENT_DATE, DEFAULT, DEFAULT, 'Kiriil', /*'Bubenovich',*/ 'Karapuzin',
        '+380634325657'),
       ('example4@gmail.com', '$2a$10$8Z/FaHQXO2Wysrl56d1G4.t0is4eJ1uMTmDbT4XNFGgv2KZxGghA2',
        CURRENT_DATE, DEFAULT, DEFAULT, 'Ivan', /*'Kulebovich',*/ 'Antonov',
        '+380764325621'),
       ('example5@gmail.com', '$2a$10$8Z/FaHQXO2Wysrl56d1G4.t0is4eJ1uMTmDbT4XNFGgv2KZxGghA2',
        CURRENT_DATE, DEFAULT, DEFAULT, 'Ivan', /*'Kulebovich',*/ 'Antonov',
        '+380764325621'),
       ('example6@gmail.com', '$2a$10$8Z/FaHQXO2Wysrl56d1G4.t0is4eJ1uMTmDbT4XNFGgv2KZxGghA2',
        CURRENT_DATE, DEFAULT, DEFAULT, 'Ivan', /*'Kulebovich',*/ 'Antonov',
        '+380764325621'),
       ('example7@gmail.com', '$2a$10$8Z/FaHQXO2Wysrl56d1G4.t0is4eJ1uMTmDbT4XNFGgv2KZxGghA2',
        CURRENT_DATE, DEFAULT, DEFAULT, 'Ivan', /*'Kulebovich',*/ 'Antonov',
        '+380764325621'),
       ('example8@gmail.com', '$2a$10$8Z/FaHQXO2Wysrl56d1G4.t0is4eJ1uMTmDbT4XNFGgv2KZxGghA2',
        CURRENT_DATE, DEFAULT, DEFAULT, 'Ivan', /*'Kulebovich',*/ 'Antonov',
        '+380764325621'),
       ('example9@gmail.com', '$2a$10$8Z/FaHQXO2Wysrl56d1G4.t0is4eJ1uMTmDbT4XNFGgv2KZxGghA2',
        CURRENT_DATE, DEFAULT, DEFAULT, 'Ivan', /*'Kulebovich',*/ 'Antonov',
        '+380764325621'),
       ('example10@gmail.com', '$2a$10$8Z/FaHQXO2Wysrl56d1G4.t0is4eJ1uMTmDbT4XNFGgv2KZxGghA2',
        CURRENT_DATE, DEFAULT, DEFAULT, 'Ivan', /*'Kulebovich',*/ 'Antonov',
        '+380764325621');

INSERT INTO additional_service (name, description, cost)
VALUES ('Podkluchenie', 'viezd i ustanovka oborudovaniya', 200),
       ('Router', 'router', 100);

INSERT INTO connection_request (subscriber, city, address, tariff)
VALUES (1, 'Odessa', 'Bocharova 45 214', 2),
       (4, 'Odessa', 'Saharovo 21 232', 1),
       (5, 'Odessa', 'Krimskaya 65 174', 2),
       (6, 'Odessa', 'Bocharova 45 214', 2);

INSERT INTO user_tariffs(user_id, tariff_id, date_of_start, date_of_last_payment)
VALUES (1, 2, '2022-09-19'::DATE, '2022-09-19'::DATE);

INSERT INTO transaction(balance_id, type, transaction_amount, transaction_date, transaction_status)
VALUES (1, 'debit', 180.00, '2022-09-19'::DATE, 'SUCCESSFUL');

INSERT INTO request_additional_services (request_id, services_id)
VALUES (1, 1),
       (1, 2);