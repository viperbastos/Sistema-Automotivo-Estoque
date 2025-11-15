CREATE DATABASE sistema_automotivo;
USE sistema_automotivo;
SHOW DATABASES;
USE sistema_automotivo;
DELETE FROM veiculo WHERE placa = 'ABC-1234';
DELETE FROM veiculo;



CREATE TABLE marca (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL
);

CREATE TABLE modelo (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    id_marca INT NOT NULL,
    FOREIGN KEY (id_marca) REFERENCES marca(id)
);

CREATE TABLE veiculo (
    placa VARCHAR(10) PRIMARY KEY,
    ano INT,
    preco DOUBLE,
    status VARCHAR(60status),status
    id_modelo INT NOT NULL,
    FOREIGN KEY (id_modelo) REFERENCES modelo(id)
);

