
docker run -d --name mysql_container -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=alertasolodb -e MYSQL_USER=alertauser -e MYSQL_PASSWORD=senha123 -p 3306:3306 -v mysql_data:/var/lib/mysql mysql:8.0