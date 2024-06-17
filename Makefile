COMPOSE_FILE = docker-compose.yml
APP = marketplace-be
DATABASE_NAME = postgres-db

start:
	docker compose -f $(COMPOSE_FILE) up -d --build
dev:
	docker compose -f $(COMPOSE_FILE) up --build
up:
	docker compose -f $(COMPOSE_FILE) up -d

down:
	docker compose -f $(COMPOSE_FILE) down --remove-orphans

build:
	docker compose -f $(COMPOSE_FILE) build

logs:
	docker compose -f $(COMPOSE_FILE) logs -f

exec:
	docker compose -f $(COMPOSE_FILE) exec $(DATABASE_NAME) <command>

test:
	docker-compose run --rm $(App) mvn clean test


ps:
	docker compose -f $(COMPOSE_FILE) ps

clean:
	docker system prune --volumes --force

update:
	docker compose pull $(APP)

rebuild: down build

restart: update down start

default: up

# .PHONY: up down build logs exec ps clean reset:db default