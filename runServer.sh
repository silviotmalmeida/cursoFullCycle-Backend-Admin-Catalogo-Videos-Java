#!/bin/bash

echo "Iniciando o servidor..."
docker exec -it backend-admin-video-catalog-app bash -c "gradle bootRun"
sudo chmod 777 -R app
