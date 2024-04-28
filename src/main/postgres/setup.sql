CREATE ROLE inv_system WITH
    NOSUPERUSER
    LOGIN
    PASSWORD 'TEMP' /* TODO passwords */
    ;

CREATE DATABASE inventorydb;
