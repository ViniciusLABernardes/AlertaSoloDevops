 docker run -d --name api_java_container --network minha-rede -p 8080:8080 -e MYSQL_URL=jdbc:mysql://mysql_container:3306/alertasolodb alertasolo
