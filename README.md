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

### UAT

## Future plans

