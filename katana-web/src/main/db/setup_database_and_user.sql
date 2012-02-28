CREATE DATABASE katana;

CREATE USER 'katana_app'@'%.%.%.%' IDENTIFIED BY 'katana_app';

GRANT ALL PRIVILEGES ON katana.* TO 'katana_app'@'%.%.%.%' IDENTIFIED BY 'katana_app' WITH GRANT OPTION;
FLUSH PRIVILEGES;
