#Docker (Docker version 20.10.21)
You can use application with docker.

Go to project folder and type these*:
- maven clean, install
- docker build -t notepad-backend .
- docker-compose up -d 

*before that follow instructions in frontend repository

#JDK version 17.0.2

1. If you want to run tests, you have to turn Docker because TestContainers are being used.
2. If you want to run application using IDE, you have to comment "API" and "notepad-frontend" services in docker-compose.yml file.
