#!/bin/bash

echo "Executando os testes..."
docker exec -it backend-admin-video-catalog-app bash -c "gradle test"
docker exec -it backend-admin-video-catalog-app bash -c "gradle test"
docker exec -it backend-admin-video-catalog-app bash -c "gradle test"
docker exec -it backend-admin-video-catalog-app bash -c "gradle test"
docker exec -it backend-admin-video-catalog-app bash -c "gradle test"
sudo chmod 777 -R app
