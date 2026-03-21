Recipes Spring Boot Microservices

A microservices-based backend system for managing recipes, ingredients, and related data using Spring Boot 4 and modern cloud-native technologies.

Overview

This project demonstrates a microservices architecture where different services handle specific responsibilities such as recipes and ingredients. Services communicate via REST using OpenFeign and are configured centrally using a Config Server.

Architecture

Recipe Service
  Manages recipes
  Handles relationships with ingredients
Ingredient Service
  Provides ingredient data
  Accessed via Feign Client
Config Server
  Centralized configuration management
PostgreSQL
  Each service has its own database

Tech Stack
  Java 21
  Spring Boot 4
  Spring Cloud (Config Server, OpenFeign)
  Spring Data JPA
  PostgreSQL
  Hibernate
  Maven / Gradle

Getting Started
1. Clone the repository
  git clone https://github.com/cstefanakis/recipes-spring-boot-microservices.git
cd recipes-spring-boot-microservices

Christos Stefanakis
