CREATE TABLE users (

                       id CHAR(36) PRIMARY KEY,

                       email VARCHAR(255) NOT NULL UNIQUE,

                       password_hash VARCHAR(255) NOT NULL,

                       rol VARCHAR(50) NOT NULL,

                       estado BOOLEAN DEFAULT TRUE,

                       fecha_creacion DATETIME,

                       ultimo_acceso DATETIME

);