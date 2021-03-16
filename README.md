Restaurant Reviews App
===============================

# Table of Contents

* [Description](#description)
* [Tech Stack](#tech-stack)
* [Running Application](#run-the-application)
* [Project Architecture](#project-instructions)
* [Development Environment](#project-instructions)
* [Deployment](#deployment)
    * [Kubernetes](#kubernetes)
    * [UAT](#uat)
* [Future Plans](#future-plans)

## Description

* This project is Restaurant Review app, where users can register, create restaurants and review restaurants by voting
  and writing comments

## Tech Stack

* Backend
    * Gradle
    * Spring boot
    * Postgresql
    * Spring data
    * H2
    * Checkstyle
    * PMD
    * Jococo
    * SpringDoc (swagger)
* Frontend
    * angular
    * metronic (template)
* Deployment
    * Docker (single build, and multistage build)
    * Jenkins
    * Terraform

## Run the Application

### Credentials
| Email           | password  | Role              |
| -----           | --------  | ----              |
| admin@app.com   | admin123  | admin             |
| user1@app.com   | user123   | regular user      |
| owner1@app.com  | owner123  | restaurant owner  |

### Live demo
application is already deployed to http://tisserv.net:8978/

### Run for UAT (simple run)

you need to have docker engine running on linux or windows or docker machine with linux compability

```
 sh deploy/uat/deploy.sh
```
This command will build application and run (both frontend and backend) in port 8978

navigate to http://localhost:8978 to open url

### Run in local machine
Backend:

prerequisites:
* you need to have jdk 11 on your local machine
```
./gradlew bootRun
```
Frontend:

prerequisites:
* you need to have nodejs v15.11.0
```
cd ui
npm install
npm run start
```

## Project Architecture



## Deployment

### Kubernetes
  
For kubernetes deployment terraform + Jenkins is used.

UI and API is deployed with seperate deployments

you can check /infra folder for terraform files

### UAT

UAT deployment is single deployment, more suitable for uat/qa testing in feature branchs, you just need to checkout to feature branch and call following command 

```sh deploy/uat/deploy.sh```

you can also adjust deploy.sh file

```
deploy/uat/
docker build ../.. -f Dockerfile -t restaurant-app
docker kill restaurant-app
docker rm restaurant-app
docker run -d --name restaurant-app -p 8978:80 restaurant-app
```

## Future plans

* Current architecture is monolith, in future it can be changed to microservices architecture for better development and to improve scalability
* When review is added/modified/deleted it is immediately calculated, instead we can use Kafka or similar mechanism for doing it in background
  * While using Kafka fail tolerance should be handled better as if computation is not done properly, the message which is responsible for computing it will kept in kafka and recomputed
  * We can have different scaling logic between review computers and review apis
  * We can have different aggregation even analytic aggregation with the data in Kafka
* We can use terraform to fully automatize application deployment to kubernetes even fully automize deploying to cloud
* For optimistic lock we can use redlock in future for better resource locking and to move locking manager logic outside of database
* We can use mongodb for better performance, in our application relational database is not very usefull as our application is not using joins, grouping, etc.
* We can use refresh token blacklisting to invalidate refresh tokens in case of logout
