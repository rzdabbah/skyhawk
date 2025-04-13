#!/bin/bash
set -e

echo "Starting database initialization..."

# Wait for PostgreSQL to be ready
echo "Waiting for PostgreSQL to be ready..."
until pg_isready -h $POSTGRES_HOST -U $POSTGRES_USER; do
  echo "PostgreSQL is unavailable - sleeping"
  sleep 2
done
echo "PostgreSQL is ready!"

# Create database and grant privileges
echo "Creating database nba_stats..."
PGPASSWORD=$POSTGRES_PASSWORD psql -h $POSTGRES_HOST -U $POSTGRES_USER -d $POSTGRES_DB <<-EOSQL
    CREATE DATABASE nba_stats;
    GRANT ALL PRIVILEGES ON DATABASE nba_stats TO postgres;
EOSQL
echo "Database created successfully!"

# Run schema script
echo "Creating schema..."
PGPASSWORD=$POSTGRES_PASSWORD psql -h $POSTGRES_HOST -U $POSTGRES_USER -d nba_stats -f /docker-entrypoint-initdb.d/02-schema.sql
echo "Schema created successfully!"

# Run data script
echo "Loading initial data..."
PGPASSWORD=$POSTGRES_PASSWORD psql -h $POSTGRES_HOST -U $POSTGRES_USER -d nba_stats -f /docker-entrypoint-initdb.d/03-data.sql
echo "Data loaded successfully!"

echo "Database initialization completed!" 