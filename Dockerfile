FROM postgres:12.20-alpine3.20

# Defina variáveis de ambiente no Dockerfile
ENV POSTGRES_USER=docker
ENV POSTGRES_PASSWORD=docker
ENV POSTGRES_DB=restaurant_management_system

# Crie um volume persistente para o banco de dados
VOLUME /var/lib/postgresql/data

# Exponha a porta padrão do PostgreSQL
EXPOSE 5432:5432