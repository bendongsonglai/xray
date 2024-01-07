FROM mysql:5.6

ADD ./database/bkris231121.sql /docker-entrypoint-initdb.d
# FROM maven:3.9.2-openjdk-11 # for Java 11
FROM maven:3-openjdk-11

COPY . .
RUN mvn clean install

CMD mvn spring-boot:run