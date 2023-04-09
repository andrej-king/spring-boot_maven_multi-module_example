ARG BASE_VERSION='6.16'
FROM dpage/pgadmin4:${BASE_VERSION}

USER root

# Create password files with permissions
COPY ./init.sh /init.sh
RUN chmod +x /init.sh

# Create empty servers.json file with permissions
RUN touch /pgadmin4/servers.json
RUN chown pgadmin:root /pgadmin4/servers.json

USER pgadmin

ENTRYPOINT /init.sh
