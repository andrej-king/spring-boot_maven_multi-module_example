#!/usr/bin/env sh

## pgadmin init script

set -e
set -u

#####> pgadmin file #####
# Create pgpass file
echo "$DB_HOST:$DB_PORT:$APP_DB_NAME:$APP_DB_USER:$APP_DB_PASS" >> /var/lib/pgadmin/pgpass

# Set permissions for pgpass file
chmod 600 /var/lib/pgadmin/pgpass
chown pgadmin:root /var/lib/pgadmin/pgpass
#####< pgadmin file #####


#####> test pgadmin file #####
# Create test-pgpass file
echo "$DB_HOST:$DB_PORT:$APP_TEST_DB_NAME:$APP_TEST_DB_USER:$APP_TEST_DB_PASS" >> /var/lib/pgadmin/test-pgpass

# Set permissions for test-pgpass file
chmod 600 /var/lib/pgadmin/test-pgpass
chown pgadmin:root /var/lib/pgadmin/test-pgpass
#####< test pgadmin file #####


#####> main postgres pgadmin file #####
# Create postgres-pgpass file
echo "$DB_HOST:$DB_PORT:*:$POSTGRES_USER:$POSTGRES_PASSWORD" >> /var/lib/pgadmin/postgres-pgpass

# Set permissions for postgres-pgpass file
chmod 600 /var/lib/pgadmin/postgres-pgpass
chown pgadmin:root /var/lib/pgadmin/postgres-pgpass
#####< main postgres pgadmin file #####


#####> servers.json file #####
# Create servers.json
tee /pgadmin4/servers.json >/dev/null <<EOF
{
    "Servers": {
        "1": {
            "Name": "Local $APP_DB_USER",
            "Group": "Servers",
            "Host": "$DB_HOST",
            "Port": $DB_PORT,
            "MaintenanceDB": "$APP_DB_NAME",
            "Username": "$APP_DB_USER",
            "SSLMode": "prefer",
            "PassFile": "/var/lib/pgadmin/pgpass"
        },
        "2": {
            "Name": "Local $APP_TEST_DB_USER",
            "Group": "Servers",
            "Host": "$DB_HOST",
            "Port": $DB_PORT,
            "MaintenanceDB": "$APP_TEST_DB_NAME",
            "Username": "$APP_TEST_DB_USER",
            "SSLMode": "prefer",
            "PassFile": "/var/lib/pgadmin/test-pgpass"
        },
        "3": {
            "Name": "Local $POSTGRES_USER",
            "Group": "Servers",
            "Host": "$DB_HOST",
            "Port": $DB_PORT,
            "MaintenanceDB": "$POSTGRES_DB",
            "Username": "$POSTGRES_USER",
            "SSLMode": "prefer",
            "PassFile": "/var/lib/pgadmin/postgres-pgpass"
        }
    }
}
EOF

# Set permissions for servers.json file
chown pgadmin:root /pgadmin4/servers.json
#####< servers.json file #####


exec /entrypoint.sh "$@"
