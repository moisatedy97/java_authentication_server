FROM maven:3.8.3-openjdk-17

WORKDIR /mist_backend
COPY . .

CMD mvn spring-boot:run