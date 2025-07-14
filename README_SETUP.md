# Personal Finance App - Complete Setup Guide

This guide will help you set up both the Spring Boot backend and Flutter frontend for the Personal Finance App.

## 🗄️ Database Setup (MySQL)

### 1. Create Database
```sql
CREATE DATABASE personal_finance;
USE personal_finance;
```

### 2. Create Tables
```sql
-- Users table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- Categories table
CREATE TABLE categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

-- Expenses table
CREATE TABLE expenses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    amount DOUBLE NOT NULL,
    category VARCHAR(50) NOT NULL,
    date DATE NOT NULL,
    description VARCHAR(100) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Budgets table
CREATE TABLE budgets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    month VARCHAR(50) NOT NULL,
    amount DOUBLE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

## 🚀 Spring Boot Backend Setup

### 1. Prerequisites
- Java 17 or higher
- Maven
- MySQL Server

### 2. Configuration
Update `personal-finance-app/src/main/resources/application.properties`:
```properties
# Update these values with your MySQL credentials
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
```

### 3. Run the Backend
```bash
cd personal-finance-app
mvn clean install
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### 4. Test API Endpoints
```bash
# Test endpoint
curl http://localhost:8080/api/auth/test

# Register a user
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"testpass"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"testpass"}'
```

## 📱 Flutter Frontend Setup

### 1. Prerequisites
- Flutter SDK
- Android Studio / VS Code
- Android Emulator or Physical Device

### 2. Install Dependencies
```bash
cd expense_tracker
flutter pub get
```

### 3. Update Backend URL
In `expense_tracker/lib/services/auth_service.dart`, update the base URL:
```dart
static const String baseUrl = 'http://10.0.2.2:8080/api'; // For Android Emulator
// OR
static const String baseUrl = 'http://localhost:8080/api'; // For iOS Simulator
// OR
static const String baseUrl = 'http://YOUR_IP:8080/api'; // For Physical Device
```

### 4. Run the Flutter App
```bash
flutter run
```

## 🔄 Complete Flow

### 1. User Registration
1. User opens Flutter app
2. Clicks "Sign Up"
3. Enters username and password
4. Flutter sends POST request to `/api/auth/register`
5. Spring Boot validates and saves to MySQL
6. Returns JWT token and user info
7. Flutter stores token and navigates to home

### 2. User Login
1. User enters username and password
2. Flutter sends POST request to `/api/auth/login`
3. Spring Boot validates against MySQL
4. Returns JWT token and user info
5. Flutter stores token and navigates to home

## 📁 Project Structure

```
Personal Finance App/
├── personal-finance-app/          # Spring Boot Backend
│   ├── src/main/java/com/personalfinance/app/
│   │   ├── controller/
│   │   │   └── AuthController.java
│   │   ├── service/
│   │   │   └── AuthService.java
│   │   ├── repository/
│   │   │   └── UserRepository.java
│   │   ├── model/
│   │   │   └── User.java
│   │   ├── dto/
│   │   │   ├── AuthRequest.java
│   │   │   └── AuthResponse.java
│   │   ├── config/
│   │   │   └── SecurityConfig.java
│   │   └── util/
│   │       └── JwtUtil.java
│   └── src/main/resources/
│       └── application.properties
└── expense_tracker/               # Flutter Frontend
    ├── lib/
    │   ├── models/
    │   │   └── user.dart
    │   ├── services/
    │   │   └── auth_service.dart
    │   ├── providers/
    │   │   └── auth_provider.dart
    │   ├── screens/
    │   │   ├── splash_screen.dart
    │   │   ├── login_screen.dart
    │   │   ├── register_screen.dart
    │   │   └── home_screen.dart
    │   └── main.dart
    └── pubspec.yaml
```

## 🔧 Troubleshooting

### Backend Issues
1. **Database Connection Error**: Check MySQL credentials in `application.properties`
2. **Port Already in Use**: Change port in `application.properties`
3. **JWT Error**: Check JWT secret in `application.properties`

### Frontend Issues
1. **Network Error**: Check backend URL in `auth_service.dart`
2. **Build Error**: Run `flutter clean` and `flutter pub get`
3. **Emulator Issues**: Use `10.0.2.2` instead of `localhost` for Android

### Common Commands
```bash
# Backend
mvn clean install
mvn spring-boot:run

# Frontend
flutter clean
flutter pub get
flutter run
```

## 🎯 Next Steps

After successful setup, you can:
1. Add expense management features
2. Implement category management
3. Add budget tracking
4. Create analytics and reporting
5. Add role-based access control

## 📞 Support

If you encounter any issues:
1. Check the console logs for both backend and frontend
2. Verify database connection
3. Ensure all dependencies are installed
4. Check network connectivity between frontend and backend 