#!/bin/bash

## postgres init script (create user for tests)

set -e
set -u

export PGPASSWORD=$POSTGRES_PASSWORD;

echo "  Creating user $APP_TEST_DB_USER and database $APP_TEST_DB_NAME"
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
  CREATE USER $APP_TEST_DB_USER WITH PASSWORD '$APP_TEST_DB_PASS';
  CREATE DATABASE $APP_TEST_DB_NAME;
  GRANT ALL PRIVILEGES ON DATABASE $APP_TEST_DB_NAME TO $APP_TEST_DB_USER;
EOSQL
