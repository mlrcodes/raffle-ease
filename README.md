# Docker command to create Associations Service:
- docker create --name associations-service --network configs-net --network associations-net -p 8081:8081 -e SPRING_PROFILES_ACTIVE= -e associations_db_user= -e associations_db_password= associations-service

# Docker command to create Addociations DB:
- sudo docker run --name associations-db --network associations-net -e POSTGRES_USER= -e POSTGRES_PASSWORD= -e POSTGRES_DB= -e PGDATA=/var/lib/postgresql/data -v associations-data:/var/lib/postgresql/data -p 5433:5432 postgres
