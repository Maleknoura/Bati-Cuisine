
CREATE TABLE Client (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) unique NOT NULL,
   adress VARCHAR(255) NOT NULL,
    numberPhone VARCHAR(20),
    isProfessionel BOOLEAN NOT NULL
);


CREATE TABLE Quote (
    id SERIAL PRIMARY KEY,
    estimatedAmount DOUBLE PRECISION NOT NULL,
    issueDate DATE NOT NULL,
    validityDate DATE NOT NULL,
    isAccepted BOOLEAN NOT NULL
);




CREATE TYPE STATUS AS ENUM ('InProgress', 'Completed', 'Cancelled');


CREATE TABLE Project (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
   profitMargin DOUBLE PRECISION NOT NULL,
   totalCostt DOUBLE PRECISION NOT NULL,
    status STATUS NOT NULL,
    quoteId INTEGER,
    clientId INTEGER,
    FOREIGN KEY (quoteId) REFERENCES Quote(id) ON DELETE SET NULL,
    FOREIGN KEY (clientId) REFERENCES Client(id) ON DELETE SET NULL
);



CREATE TABLE Component (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    unitCost DOUBLE PRECISION NOT NULL,
    quantity DOUBLE PRECISION NOT NULL,
    componentType VARCHAR(50) NOT NULL,
    taxRate DOUBLE PRECISION NOT NULL,
    projectId INTEGER,
    FOREIGN KEY (projectId) REFERENCES Project(id) ON DELETE SET NULL
);


CREATE TABLE Labor (
    id INTEGER PRIMARY KEY REFERENCES Component(id),
    hourly_rate DOUBLE PRECISION NOT NULL,
    work_hours DOUBLE PRECISION NOT NULL,
    worker_productivity DOUBLE PRECISION NOT NULL
)INHERITS (Component);


CREATE TABLE Material (
    id INTEGER PRIMARY KEY REFERENCES Component(id),
    transportCost DOUBLE PRECISION NOT NULL,
    qualityCoefficient DOUBLE PRECISION NOT NULL
)INHERITS (Component);