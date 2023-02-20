DROP TABLE if exists request_additional_services;
DROP TABLE if exists connection_request;
DROP TABLE if exists user_tariffs;
DROP TABLE if exists tariff_services;
DROP TABLE if exists tariff;
DROP TABLE if exists transaction;
DROP TABLE if exists checks;
DROP TABLE if exists "user";
DROP TABLE if exists service;
DROP TABLE if exists additional_service;
DROP TYPE if exists role_type;
DROP TYPE if exists user_status_type;
DROP TYPE if exists tariff_status_type;
DROP TYPE if exists request_status_type;
DROP TYPE if exists transaction_type;
DROP TYPE if exists transaction_status_type;

CREATE TYPE role_type AS ENUM ('user', 'admin', 'main_admin');
CREATE TYPE user_status_type AS ENUM ('subscribed', 'blocked');
CREATE TYPE tariff_status_type AS ENUM ('disabled', 'active');
CREATE TYPE request_status_type AS ENUM ('in_processing', 'rejected', 'approved');
CREATE TYPE transaction_type AS ENUM ('debit', 'refill');
CREATE TYPE transaction_status_type AS ENUM ('SUCCESSFUL', 'DENIED');

CREATE TABLE "user"
(
    user_id           SERIAL PRIMARY KEY,
    email             varchar(320)     NOT NULL UNIQUE, /*Шаблон на email*/
    pass              varchar(64)      NOT NULL,
    registration_date date             NOT NULL DEFAULT (CURRENT_DATE),
    user_role         role_type                 DEFAULT ('user'),
    user_status       user_status_type NOT NULL DEFAULT ('subscribed'),
    user_balance      DECIMAL(8, 2)    NOT NULL DEFAULT 0 CHECK ( user_balance >= 0 ),
    firstname         varchar(30)      NOT NULL,
    /*middle_name       varchar(30)      NOT NULL,*/
    surname           varchar(30)      NOT NULL,
    telephone_number  varchar(30)      NOT NULL /*Шаблон на telephone*/
);

CREATE TABLE transaction
(
    transaction_id     SERIAL PRIMARY KEY,
    balance_id         INTEGER                 NOT NULL,
    type               transaction_type        NOT NULL,
    transaction_amount DECIMAL(8, 2)           NOT NULL,
    transaction_date   DATE                    NOT NULL DEFAULT CURRENT_DATE,
    transaction_status transaction_status_type NULL,
    FOREIGN KEY (balance_id) REFERENCES "user" (user_id) ON DELETE CASCADE
);

CREATE TABLE service
(
    service_id   SERIAL PRIMARY KEY,
    service_type VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE tariff
(
    tariff_id            SERIAL PRIMARY KEY,
    name                 VARCHAR(40)        NOT NULL UNIQUE,
    description          text               NOT NULL,
    cost                 DECIMAL(8, 2)      NOT NULL CHECK ( cost >= 0 ),
    frequency_of_payment INTEGER            NOT NULL CHECK ( frequency_of_payment >= 0 ),
    status               tariff_status_type NOT NULL DEFAULT 'active'
);

CREATE TABLE user_tariffs
(
    user_tariffs_id      SERIAL PRIMARY KEY,
    user_id              INTEGER NOT NULL,
    tariff_id            INTEGER NOT NULL,
    date_of_start        DATE    NOT NULL DEFAULT CURRENT_DATE,
    date_of_last_payment DATE    NULL,
    FOREIGN KEY (user_id) REFERENCES "user" (user_id) ON DELETE CASCADE,
    FOREIGN KEY (tariff_id) REFERENCES tariff (tariff_id) ON DELETE CASCADE
);

CREATE TABLE additional_service
(
    additional_service_id SERIAL PRIMARY KEY,
    name                  VARCHAR(40)   NOT NULL UNIQUE,
    description           TEXT          NOT NULL,
    cost                  DECIMAL(8, 2) NOT NULL CHECK ( cost >= 0 )
);

CREATE TABLE connection_request
(
    connection_request_id SERIAL PRIMARY KEY,
    subscriber            INTEGER             NOT NULL,
    city                  VARCHAR(25)         NOT NULL,
    address               VARCHAR(40)         NOT NULL,
    tariff                INTEGER             NOT NULL,
    date_of_change        date                NOT NULL DEFAULT CURRENT_DATE,
    status                request_status_type NOT NULL DEFAULT 'in_processing',
    FOREIGN KEY (subscriber) REFERENCES "user" (user_id) ON DELETE CASCADE,
    FOREIGN KEY (tariff) REFERENCES tariff (tariff_id) ON DELETE CASCADE
);

CREATE TABLE request_additional_services
(
    request_additional_services_id SERIAL PRIMARY KEY,
    request_id                     INTEGER NOT NULL,
    services_id                    INTEGER,
    UNIQUE (request_id, services_id),
    FOREIGN KEY (request_id) REFERENCES connection_request (connection_request_id) ON DELETE CASCADE,
    FOREIGN KEY (services_id) REFERENCES additional_service (additional_service_id) ON DELETE CASCADE
);

CREATE TABLE checks (
                        check_id SERIAL PRIMARY KEY,
                        checker_id integer,
                        users integer,
                        amount decimal,
                        date_of_check timestamp,
                        FOREIGN KEY (checker_id) REFERENCES "user" (user_id) ON DELETE CASCADE
);




CREATE TABLE tariff_services
(
    tariff_services_id SERIAL PRIMARY KEY,
    tariff_id          INTEGER NOT NULL,
    services_id        INTEGER NOT NULL,
    UNIQUE (tariff_id, services_id),
    FOREIGN KEY (tariff_id) REFERENCES tariff (tariff_id) ON DELETE CASCADE,
    FOREIGN KEY (services_id) REFERENCES service (service_id) ON DELETE CASCADE
);