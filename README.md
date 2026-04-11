# &#127968; House Price Prediction System

An intelligent, pure-Java web application that utilizes a custom-built Multiple Linear Regression engine to predict real estate prices based on property features. 

Built completely from scratch using **Java Servlets, JDBC, and MySQL**, without the reliance on external massive frameworks (like Spring Boot) or Python ML libraries. It strictly uses the mathematical **Normal Equation** via Gaussian Elimination to compute AI training coefficients (Slope & Intercept).

## &#10024; Features

### Machine Learning Engine
- Custom-built **Multiple Linear Regression** algorithm.
- Variables analyzed: Area (Sq. Ft.), Bedrooms, Bathrooms, and Age of the property.
- Admins can retrain the model on the fly as new properties are added. 
- Auto-calculates **R&sup2; accuracy** and **Mean Squared Error (MSE)**.

### Secure Role-Based Dashboards
- **Admin Panel:** Complete CRUD management of States, Cities, Locations, and Properties with cascading AJAX dropdowns.
- **User Panel:** Predict house prices and view detailed historical search history. Custom user-profile management.

### Ultra-Premium UI
- Implemented an overriding UI system utilizing modern **Glassmorphism** layouts.
- Deep customizations of Bootstrap core components with frosted glass cards, dynamic background gradients, and glowing focus animations. 

## &#9881;&#65039; Tech Stack
- **Backend:** Java 17, Java Servlets (Jakarta EE)
- **Database:** MySQL 8+
- **Frontend:** HTML5, CSS3, JavaScript (Fetch API), Bootstrap 5
- **Server:** Apache Tomcat 10
- **Build/IDE:** Eclipse IDE

## &#128640; How to Run Locally (Eclipse IDE Setup)

Follow these steps to run the application on any PC using Eclipse IDE and Apache Tomcat:

1. **Clone or Download the Repository**
   ```bash
   git clone https://github.com/yourusername/HousePricePrediction.git
   ```

2. **Database Setup (MySQL)**
   - Open **MySQL Workbench** (or your preferred MySQL client).
   - Create a connection using your local root credentials.
   - Run the provided `complete_database.sql` script. This will automatically generate the `house_price_prediction_system` schema, establish all tables with cascading foreign key relationships, and insert the default login credentials.

3. **Configure Database Credentials in the Code**
   - Open the project in Eclipse.
   - Navigate to `src/main/java/edu/omkar/dbconfig/DBConfig.java`.
   - Update the connection string with your local MySQL username and password (e.g., `root`, `YourPassword`).
   - Note: Also quickly verify `RegisterServlet.java` and `UserProfileServlet.java` inside the `controller` package if they contain direct JDBC credentials.

4. **Deploy on Eclipse with Apache Tomcat**
   - In Eclipse, go to **File &rarr; Import &rarr; Existing Maven Projects** (or Dynamic Web Project) and select the cloned folder.
   - Set up your Apache Tomcat server in Eclipse (Window &rarr; Preferences &rarr; Server &rarr; Runtime Environments &rarr; Add Tomcat 10+).
   - Right-click your project folder in the Project Explorer.
   - Select **Run As &rarr; Run on Server**.
   - Select your Tomcat server and click Finish!

## &#128272; Default Login Credentials
- **Admin:** `omkar` / `omkar`
- **User:** Register a custom account or use `rohan` / `rohan@gmail.com`

---
**Developed by Omkar**  
*A comprehensive exercise showcasing pure Java problem-solving, mathematics for Machine Learning, low-level web architecture, and modern Glassmorphism UI.*
