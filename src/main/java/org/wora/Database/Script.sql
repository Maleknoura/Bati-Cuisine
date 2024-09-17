
CREATE TABLE Client (
    ID SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    phoneNumber VARCHAR(20),
    isProfessional BOOLEAN
);

CREATE TABLE Project (
    ID SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    profitMargin DOUBLE PRECISION,
    totalCost DOUBLE PRECISION,
    status VARCHAR(50),
    clientID INTEGER,
    FOREIGN KEY (clientID) REFERENCES Client(ID)
);

CREATE TABLE Component (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    unitCost DOUBLE PRECISION,
    quantity DOUBLE PRECISION,
    componentType VARCHAR(50),
    taxRate DOUBLE PRECISION
);

CREATE TABLE Labor (
    hourlyRate DOUBLE PRECISION,
    workHours DOUBLE PRECISION,
    workerProductivity DOUBLE PRECISION
) INHERITS (Component);

CREATE TABLE Material (
    transportCost DOUBLE PRECISION,
    qualityCoefficient DOUBLE PRECISION
) INHERITS (Component);