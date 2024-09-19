
CREATE TABLE Client (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) unique NOT NULL,
   address ARCHAR(255) NOT NULL,
    numberPhone VARCHAR(20),
    isProfessionel BOOLEAN NOT NULL
);
CREATE TABLE Project (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
   profitMargin DOUBLE PRECISION NOT NULL,
   totalCost DOUBLE PRECISION NOT NULL,
    status STATUS NOT NULL,
    clientId INTEGER,
    FOREIGN KEY (clientId) REFERENCES Client(id) ON DELETE SET NULL
);

CREATE TABLE Quote (
    id SERIAL PRIMARY KEY,
    estimatedAmount DOUBLE PRECISION NOT NULL,
    issueDate DATE NOT NULL,
    validityDate DATE NOT NULL,
    isAccepted BOOLEAN NOT NULL
         ProjectId INTEGER,
            FOREIGN KEY (ProjectId) REFERENCES Project(id) ON DELETE SET NULL

);


CREATE TYPE STATUS AS ENUM ('IN_PROGRESS', 'COMPLETED', 'CANCELLED');
CREATE TYPE componentType AS ENUM ('LABOR', 'Material');


CREATE TABLE Component (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    componentType componentType NOT NULL;
    unitCost DOUBLE PRECISION NOT NULL,
    quantity DOUBLE PRECISION NOT NULL,
    taxRate DOUBLE PRECISION NOT NULL,
    projectId INTEGER,
    FOREIGN KEY (projectId) REFERENCES Project(id) ON DELETE SET NULL
);


CREATE TABLE Labor (
    id INTEGER PRIMARY KEY REFERENCES Component(id),
    hourly_rate DOUBLE PRECISION NOT NULL,
    worker_productivity DOUBLE PRECISION NOT NULL
)INHERITS (Component);


CREATE TABLE Material (
    id INTEGER PRIMARY KEY REFERENCES Component(id),
    transportCost DOUBLE PRECISION NOT NULL,
    qualityCoefficient DOUBLE PRECISION NOT NULL
)INHERITS (Component);