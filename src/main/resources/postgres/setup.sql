CREATE ROLE inv_system WITH
    NOSUPERUSER
    LOGIN
    PASSWORD 'TEMP' /* TODO passwords */
    ;

CREATE SCHEMA IF NOT EXISTS inventory;

GRANT CONNECT ON DATABASE inventorytracking TO inv_system;
GRANT SELECT,INSERT,UPDATE ON ALL TABLES IN SCHEMA inventory TO inv_system;
