#!/bin/bash

## postgres init script (create user)

set -e
set -u

export PGPASSWORD=$POSTGRES_PASSWORD;

echo "  Creating user $APP_DB_USER and database $APP_DB_NAME"
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
  CREATE USER $APP_DB_USER WITH PASSWORD '$APP_DB_PASS';
  CREATE DATABASE $APP_DB_NAME;
  GRANT ALL PRIVILEGES ON DATABASE $APP_DB_NAME TO $APP_DB_USER;
EOSQL
