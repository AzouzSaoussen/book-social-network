# Full-Stack Book Sharing Application

A comprehensive full-stack web application for sharing, borrowing, and managing books. This project was built using **Spring Boot** for the backend and **Angular** for the frontend, with **JWT-based authentication** and **role-based access control**. The infrastructure is managed via **Docker Compose**, and the project follows a **mono repo structure** for both frontend and backend code.

## Table of Contents
- [About the Project](#about-the-project)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Running the Application](#running-the-application)
- [Usage](#usage)
- [API Documentation](#api-documentation)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## About the Project

This project is designed to manage a book-sharing system where users can borrow, share, and return books. It includes user authentication and role-based access control for various user types (e.g., admin, user). The system also supports book transaction histories, feedback, and other core operations related to managing books.

## Features

- User registration, login, and account activation
- JWT-based authentication and role-based access control (Admin/User)
- Book management (add, update, borrow, return, share, archive books)
- Feedback system for books
- File upload for book cover images
- Auditing for tracking book-related actions
- OpenAPI documentation for the backend API

## Technologies Used

### Backend
- [Spring Boot](https://spring.io/projects/spring-boot) - Backend framework
- [Spring Security](https://spring.io/projects/spring-security) - Security management (JWT and roles)
- [Hibernate](https://hibernate.org/) - ORM for database management
- [PostgreSQL](https://www.postgresql.org/) - Database
- [Docker](https://www.docker.com/) & [Docker Compose](https://docs.docker.com/compose/) - Containerization

### Frontend
- [Angular](https://angular.io/) - Frontend framework
- [Bootstrap](https://getbootstrap.com/) - UI components and layout
- [JWT](https://jwt.io/) - Token-based authentication

## Getting Started

### Prerequisites
Make sure you have the following installed:
- [Java 17+](https://adoptopenjdk.net/) 
- [Node.js](https://nodejs.org/en/) and [NPM](https://www.npmjs.com/)
- [Docker](https://www.docker.com/get-started)

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/book-sharing-app.git
   cd book-sharing-app

