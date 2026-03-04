

# Ocean View Resort Reservation Management System

## Project Overview

The **Ocean View Resort Reservation Management System** is a distributed web-based application developed to manage hotel reservation operations efficiently. The system digitalizes key processes such as reservation management, guest registration, billing calculation, and user authentication.

The application was developed as part of the **CIS6003 Advanced Programming module** to demonstrate the implementation of software engineering concepts including object-oriented design, distributed application architecture, database integration, automated testing, and version control.

The system replaces manual reservation processes with a structured and automated solution, reducing booking conflicts, improving data accuracy, and enhancing operational efficiency.

---

## System Features

The system provides several core functionalities to support reservation management:

* **User Authentication**
  Secure login system to control system access.

* **Add New Reservation**
  Register new guests and store reservation details including reservation number, guest information, room type, and stay duration.

* **View Reservation Details**
  Retrieve and display stored reservation information.

* **Billing Calculation**
  Automatically calculate the total cost of stay based on room type and number of nights.

* **Reporting and Data Management**
  Manage reservation records and maintain data consistency through database integration.

---

## System Architecture

The application follows a **three-tier architecture** to ensure modularity and maintainability.

### Presentation Layer

Handles the user interface and interaction with the system through web pages and forms.

### Business Logic Layer

Processes application logic such as reservation validation, billing calculations, and system rules.

### Data Access Layer

Manages communication with the database using JDBC for storing and retrieving reservation data.

---

## Technologies Used

| Technology    | Purpose                      |
| ------------- | ---------------------------- |
| Java          | Core application development |
| Java Servlets | Server-side request handling |
| JDBC          | Database connectivity        |
| MySQL         | Database management system   |
| HTML / CSS    | User interface design        |
| Apache Tomcat | Application server           |
| Git           | Version control              |
| GitHub        | Repository hosting           |

---

## Project Structure

```
Ocean_View_Resort
│
├── src/                # Application source code
├── test/               # Automated test classes
├── web/                # Web resources and UI components
├── .gitignore          # Files excluded from version control
└── README.md           # Project documentation
```

---

## How to Run the Project

1. Clone the repository

```bash
git clone https://github.com/AadilAR/Ocean_View_Resort.git
```

2. Open the project in your IDE (IntelliJ / Eclipse / NetBeans)

3. Configure the MySQL database connection

4. Deploy the project using **Apache Tomcat**

5. Run the application through the web browser

---

## Version Control

The project uses **Git** for version control and **GitHub** for repository management.
All development progress, updates, and improvements are tracked through commits, ensuring traceability of system evolution.

Repository link:
[https://github.com/AadilAR/Ocean_View_Resort](https://github.com/AadilAR/Ocean_View_Resort)

---

## Author

Aadil A R
CIS6003 Advanced Programming
ICBT Campus

---
