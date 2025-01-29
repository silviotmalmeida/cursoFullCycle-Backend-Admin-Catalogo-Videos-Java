#!/bin/bash

echo "Limpando o BD..."
docker exec -it backend-admin-video-catalog-app bash -c "gradle flywayClean"
sudo chmod 777 -R app
