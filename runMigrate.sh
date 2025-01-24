#!/bin/bash

echo "Executando as migrations..."
docker exec -it backend-admin-video-catalog-app bash -c "gradle flywayMigrate"
sudo chmod 777 -R app
