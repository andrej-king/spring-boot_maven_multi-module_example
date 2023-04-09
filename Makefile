init: docker-down \
	docker-build docker-up \
	app-init

down: docker-down
down-clear: docker-down-clear

# docker run
docker-up:
	docker compose up -d

# docker down, remove old containers
docker-down:
	docker compose down --remove-orphans

# docker down, remove old containers, remove volumes
docker-down-clear:
	docker compose down -v --remove-orphans

# build docker images
docker-build:
	docker compose build

# force re-build docker images
docker-build-force:
	docker compose build --no-cache

# app init commands
app-init:

# generate JWT (rsa) pem key pair (main api)
generate-jwt-rsa-keypair:
	# create rsa key pair
	openssl genrsa -out ./var/tmp/keypair.pem 2048
	# extract public key
	openssl rsa -in ./var/tmp/keypair.pem -pubout -out ./api-main/src/main/resources/certs/public.pem
	# create private key in PKCS#8 format
	openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in ./var/tmp/keypair.pem -out ./api-main/src/main/resources/certs/private.pem

# generate JWT (rsa) pem key pair (admin api)
generate-jwt-rsa-keypair-admin:
	# create rsa key pair
	openssl genrsa -out ./var/tmp/keypair.pem 2048
	# extract public key
	openssl rsa -in ./var/tmp/keypair.pem -pubout -out ./api-admin/src/main/resources/certs/public.pem
	# create private key in PKCS#8 format
	openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in ./var/tmp/keypair.pem -out ./api-admin/src/main/resources/certs/private.pem

# run admin api module
admin-api:
	sh -c './mvnw --projects api-admin --also-make spring-boot:run'

# run main api module
main-api:
	sh -c './mvnw --projects api-main --also-make spring-boot:run'
