FROM postgres:12-alpine
RUN mkdir -p /docker-entrypoint-initdb.d/
COPY ./setup/ddl.sql /docker-entrypoint-initdb.d/ddl.sql
ENV POSTGRES_USER='stonks'
ENV POSTGRES_PASSWORD='stonks'
ENV POSTGRES_DB='stonks'
EXPOSE 5432