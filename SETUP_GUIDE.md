# 🚀 Junk'd - Complete Setup Guide

This guide will help you set up and run the complete Junk'd waste management system with Android app, Java backend, and Python AI API.

## 📋 Prerequisites

- **Java Development Kit (JDK) 17+**
- **Android Studio** (for Android app)
- **Maven 3.6+** (for Java backend)
- **Python 3.8+** (for AI API)
- **Git** (for version control)

## 🏗️ Project Structure

```
Junk'd_Project/
├── Android_App/
│   ├── app/src/main/java/com/example/junk_app/
│   │   ├── MainActivity.java
│   │   └── NetworkHelper.java
│   └── app/src/main/res/layout/activity_main.xml
├── Junk_Backend/
│   ├── src/main/java/com/example/junk/
│   │   ├── JunkApplication.java
│   │   ├── controller/WasteAnalysisController.java
│   │   ├── service/WasteAnalysisService.java
│   │   ├── dto/WasteAnalysisRequest.java
│   │   ├── dto/WasteAnalysisResponse.java
│   │   └── config/WebConfig.java
│   ├── src/main/resources/application.properties
│   └── pom.xml
└── Python_AI_API/
    ├── app.py
    └── requirements.txt
```

## 🐍 Step 1: Set Up Python AI API

### 1.1 Create Virtual Environment
```bash
cd Python_AI_API
python -m venv venv
source venv/bin/activate  # On Windows: venv\Scripts\activate
```

### 1.2 Install Dependencies
```bash
pip install -r requirements.txt
```

### 1.3 Run the AI API
```bash
python app.py
```

The AI API will start on `http://localhost:5000`

### 1.4 Test the AI API
```bash
# Health check
curl http://localhost:5000/health

# Demo analysis
curl http://localhost:5000/demo
```

## ☕ Step 2: Set Up Java Backend

### 2.1 Navigate to Backend Directory
```bash
cd Junk_Backend
```

### 2.2 Build the Project
```bash
mvn clean install
```

### 2.3 Run the Backend
```bash
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### 2.4 Test the Backend
```bash
# Health check
curl http://localhost:8080/api/waste/health

# Demo analysis
curl -X POST http://localhost:8080/api/waste/demo
```

## 📱 Step 3: Set Up Android App

### 3.1 Open Android Studio
1. Open Android Studio
2. Select "Open an existing project"
3. Navigate to the `Android_App` folder
4. Wait for Gradle sync to complete

### 3.2 Add Required Dependencies
Add these to your `app/build.gradle`:

```gradle
dependencies {
    implementation 'com.squareup.okhttp3:okhttp:4.11.0'
    implementation 'androidx.cardview:cardview:1.0.0'
    // ... other existing dependencies
}
```

### 3.3 Add Permissions
Add these to your `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.INTERNET" />
```

### 3.4 Update Network Configuration
For Android 9+ (API level 28+), add network security config to allow HTTP traffic:

1. Create `res/xml/network_security_config.xml`:
```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">10.0.2.2</domain>
        <domain includeSubdomains="true">192.168.1.0/24</domain>
        <domain includeSubdomains="true">localhost</domain>
    </domain-config>
</network-security-config>
```

2. Add to `AndroidManifest.xml` in `<application>` tag:
```xml
<application
    android:networkSecurityConfig="@xml/network_security_config"
    ... >
```

## 🔧 Step 4: Configure Network URLs

### 4.1 For Android Emulator
In `NetworkHelper.java`, use:
```java
private static final String BASE_URL = "http://10.0.2.2:8080/api";
```

### 4.2 For Real Device
1. Find your computer's IP address:
   - Windows: `ipconfig`
   - Mac/Linux: `ifconfig`
2. Update `NetworkHelper.java`:
```java
private static final String BASE_URL = "http://YOUR_IP_ADDRESS:8080/api";
```

## 🧪 Step 5: Testing the Complete System

### 5.1 Start All Services
1. Start Python AI API: `python app.py` (Port 5000)
2. Start Java Backend: `mvn spring-boot:run` (Port 8080)
3. Run Android App from Android Studio

### 5.2 Test Flow
1. Open the Android app
2. Click "Camera" or "Gallery" to select an image
3. Click "Analyze Waste"
4. Check the results displayed in the app

### 5.3 Monitor Logs
- **Python AI API**: Check console output
- **Java Backend**: Check Spring Boot logs
- **Android App**: Check Android Studio Logcat

## 🔍 Troubleshooting

### Common Issues

#### 1. Network Connection Failed
- Ensure all services are running
- Check firewall settings
- Verify IP addresses and ports
- Test with `curl` commands

#### 2. Image Upload Issues
- Check file size limits (max 10MB)
- Verify image format (JPEG, PNG)
- Check Android permissions

#### 3. AI API Not Responding
- Ensure Python virtual environment is activated
- Check if port 5000 is free
- Verify all dependencies are installed

#### 4. Database Issues
- H2 database is used by default (in-memory)
- Access H2 console at `http://localhost:8080/api/h2-console`
- Username: `sa`, Password: (empty)

## 📊 API Endpoints

### Java Backend (`http://localhost:8080/api`)
- `POST /waste/analyze` - Analyze waste image
- `POST /waste/analyze/upload` - Upload file for analysis
- `GET /waste/leaderboard` - Get leaderboard
- `GET /waste/user/{userId}/stats` - Get user statistics
- `GET /waste/health` - Health check

### Python AI API (`http://localhost:5000`)
- `POST /analyze` - Analyze image with AI
- `GET /health` - Health check
- `GET /demo` - Demo analysis
- `GET /model/info` - Model information

## 🚀 Production Deployment

### Java Backend
```bash
# Build JAR
mvn clean package

# Run with production profile
java -jar target/junk-backend-1.0.0.jar --spring.profiles.active=prod
```

### Python AI API
```bash
# Install production server
pip install gunicorn

# Run with Gunicorn
gunicorn --bind 0.0.0.0:5000 app:app
```

## 📝 Additional Features to Implement

1. **User Authentication** - JWT tokens, login/signup
2. **Real AI Model** - Replace mock with TensorFlow/PyTorch
3. **Database Integration** - PostgreSQL/MySQL for production
4. **File Storage** - AWS S3 or Google Cloud Storage
5. **Push Notifications** - Firebase Cloud Messaging
6. **Analytics Dashboard** - Admin panel for insights
7. **Reward System** - Integration with payment gateways

## 🆘 Getting Help

- Check the logs for detailed error messages
- Verify all dependencies are installed
- Ensure correct IP addresses and ports
- Test each component individually before integration

## 🎉 Success!

If everything is working correctly, you should see:
- ✅ Python AI API responding to requests
- ✅ Java Backend processing and forwarding requests
- ✅ Android app displaying analysis results
- ✅ Coins and scores being calculated

Your Junk'd waste management system is now ready to transform recycling into a gamified experience! 🌱♻️
