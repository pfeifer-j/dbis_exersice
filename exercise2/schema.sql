CREATE TABLE estate_agent (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
	address VARCHAR(255),
    login VARCHAR(255),
    password VARCHAR(255),
    UNIQUE(login)
);

CREATE TABLE estate (
    id SERIAL PRIMARY KEY,
    square DECIMAL(10,2),
    agent_id INT,
	city VARCHAR(255),
	postalcode VARCHAR(255),
	street VARCHAR(255),
	streetnumber VARCHAR(255),
    FOREIGN KEY (agent_id) REFERENCES estate_agent(id)
);

CREATE TABLE apartment (
	id INT,
    floor INT,
    rent DECIMAL(10,2),
    rooms INT,
    balcony BOOLEAN,
    kitchen BOOLEAN,
    FOREIGN KEY (id) REFERENCES estate(id)
);

CREATE TABLE house (
    id INT,
    floors INT,
    price DECIMAL(10,2),
    garden BOOLEAN,
    estate_id INT,
    FOREIGN KEY (id) REFERENCES estate(id)
);

CREATE TABLE person (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
	address VARCHAR(255)
);

CREATE TABLE contract (
    contract_number SERIAL PRIMARY KEY,
    date DATE,
    place VARCHAR(255)
);

CREATE TABLE tenancy_contract (
    id int,
    start_date DATE,
    duration DECIMAL(5,2),
    additional_costs DECIMAL(10,2),
    FOREIGN KEY (id) REFERENCES contract(contract_number) primary key
);

CREATE TABLE purchase_contract (
    id int,
    number_of_installments INT,
    interest_rate DECIMAL(5,2),
    FOREIGN KEY (id) REFERENCES contract(contract_number) primary key
);

CREATE TABLE sells (
    seller_id INT,
    house_id INT,
    contract_number INT PRIMARY KEY,
    FOREIGN KEY (seller_id) REFERENCES person(id),
    FOREIGN KEY (house_id) REFERENCES estate(id),
    FOREIGN KEY (contract_number) REFERENCES contract(contract_number)
);

CREATE TABLE rents (
    tenant_id INT,
    apartment_id INT,
    contract_number INT PRIMARY KEY,
    FOREIGN KEY (tenant_id) REFERENCES person(id),
    FOREIGN KEY (apartment_id) REFERENCES estate(id),
    FOREIGN KEY (contract_number) REFERENCES contract(contract_number)
);