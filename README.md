Here is a comprehensive `README.md` structure based on the project plans and phase summaries from your sources. You can copy and paste this directly into your GitHub repository!

***

# LearningApp – Full-Stack Web Application

## Project Overview
LearningApp is a full-stack web application designed to handle end-to-end operations using a modern technology stack. The project follows a standard 3-tier architecture, separating the user interface, business logic, and persistent data storage. Currently, the backend REST API, database foundation, and security layers are 100% complete. 

## 🛠️ Technology Stack
*   **Backend:** Java 24, Spring Boot 4.x
*   **Database:** MySQL 8.0, Spring Data JPA / Hibernate
*   **Security:** Spring Security, stateless JWT (JSON Web Tokens), BCrypt Password Hashing
*   **Build Tool:** Maven 3.9

## ✨ Features Implemented (Backend)

**1. Layered Architecture (Phase 1)**
*   Organized cleanly into **Model, Repository, Service, and Controller** layers to separate routing from business logic.
*   Data is managed via `@Entity` annotations, which automatically generate the `users` and `application_forms` tables in MySQL without manual SQL queries.

**2. Secure Authentication System (Phase 2)**
*   **User Registration & Login:** Users can create accounts and log in securely.
*   **Password Encryption:** All passwords are one-way hashed using BCrypt before being saved to the database.
*   **JWT Protection:** The system uses stateless JWT authentication. Upon a successful login, users receive a token valid for 24 hours. A custom `JwtFilter` intercepts all incoming requests to ensure protected routes are completely locked down. 

**3. Application Form CRUD & Ownership Security (Phase 3)**
*   **Full CRUD:** Complete Create, Read, Update, and Delete operations for user application forms.
*   **Ownership Check (IDOR Prevention):** Strict security checks ensure that users can only read, update, or delete their *own* forms. 
*   **Auto-Populated Fields:** Sensitive fields like the form `id`, `status` (defaulted to PENDING), `submittedDate`, and the `userId` are handled automatically by the server and token, preventing user manipulation.

## 🔌 API Endpoints

**Authentication (Public)**
*   `POST /api/auth/register` - Create a new user account.
*   `POST /api/auth/login` - Login and receive a JWT token.

**Application Forms (Protected - Requires JWT)**
*   `POST /api/forms` - Submit a new application form.
*   `GET /api/forms` - Get a list of all forms belonging to the logged-in user.
*   `GET /api/forms/{id}` - Get details of a specific form.
*   `PUT /api/forms/{id}` - Update an existing form.
*   `DELETE /api/forms/{id}` - Permanently delete a form.

## 🚀 How to Run Locally

### Prerequisites
*   Java 24 installed (`java -version`)
*   Maven 3.9 installed (`mvn -version`)
*   MySQL 8.0 installed and running

### Database Setup
1. Log into your local MySQL instance: `mysql -u root -p`
2. Create the empty database: `CREATE DATABASE learningapp;`
3. Make sure your `application.properties` file in the Spring Boot project contains your correct MySQL credentials (default username: `root`, password: `root1234`).

### Running the Backend
1. Navigate to the backend project folder in your terminal.
2. Run the application using Maven:
   ```bash
   mvn spring-boot:run
   ```
3. The Spring Boot server will start on **port 8080**.

## 🔜 Next Steps
*   **Phase 4:** Build the React frontend on port 3000 to consume these APIs.
*   **Phase 5:** Integrate the frontend and backend, configuring CORS and Axios token interceptors.
*   **Phase 6:** Deploy the application to a live cloud platform (like Render or Railway).
