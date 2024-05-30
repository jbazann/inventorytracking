# TODO passwords
FROM postgres:13.14-bullseye
ENV POSTGRES_DB="inventorytracking"
ENV POSTGRES_PASSWORD="supersecure"
COPY setup.sql /docker-entrypoint-initdb.d/0.sql
COPY generated/ddl.sql /docker-entrypoint-initdb.d/1.sql

CMD ["postgres"]
