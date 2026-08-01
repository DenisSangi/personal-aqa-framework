CREATE TABLE users (
    id              SERIAL PRIMARY KEY,
    name            VARCHAR(50) NOT NULL,
    email           VARCHAR(254) NOT NULL UNIQUE,
    password        VARCHAR(60) NOT NULL,
    title           VARCHAR(5) DEFAULT 'Mr.',
    birth_date      DATE CHECK (birth_date <= CURRENT_DATE),
    first_name      VARCHAR(50) NOT NULL,
    last_name       VARCHAR(50) NOT NULL,
    company         VARCHAR(50) DEFAULT '',
    address1        VARCHAR(50) NOT NULL,
    address2        VARCHAR(50) DEFAULT '',
    country         VARCHAR(50) NOT NULL,
    zipcode         VARCHAR(20) NOT NULL,
    state           VARCHAR(25) NOT NULL,
    city            VARCHAR(50) NOT NULL,
    mobile_phone    VARCHAR(15) NOT NULL,
    created_at      TIMESTAMP DEFAULT now()
);