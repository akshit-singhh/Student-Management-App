# 📱 Student Management App — Android App (V1.0)

[![Platform](https://img.shields.io/badge/platform-Android-green.svg)](#)
[![Build](https://img.shields.io/badge/build-Stable-blue.svg)](#)
[![UI](https://img.shields.io/badge/UI-Material%20Design-orange.svg)](#)
[![Language](https://img.shields.io/badge/Kotlin-100%25-purple.svg)](#)
[![License: MIT](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)

A **modern Android app** for managing student records.  
It allows admins to **log in, view/update profile**, and **manage student information** efficiently with a clean Material Design interface.

<p align="center">
  <img 
       src="https://github.com/user-attachments/assets/ea4514eb-1922-4fc8-9508-dc7e3417b731" 
       alt="App Icon" 
       width="200" 
       height="200" 
       style="border-radius: 20px;"
  />
</p>

<h1 align="center">🎓 Student Management App</h1>

---

## ✨ App Features

| Feature | Description |
|---------|-------------|
| 👤 **Admin Login** | Secure login with email and password. |
| 📝 **Profile Management** | View and update admin name, email, and password. |
| 📊 **Dashboard** | Live total student count, auto-refresh every 15 seconds. |
| 🏫 **Student Management** | Add, edit, and view student records. |
| 🔒 **Session Management** | SharedPreferences for login session persistence. |
| 🚀 **Splash Screen** | Lottie animation with app branding. |
| 🔄 **Auto Refresh** | Dashboard student count updates automatically. |
| 🔔 **Logout** | Secure logout clearing user session. |

---

## 📱 Screenshots

<p align="center">
  <img 
       src="https://github.com/user-attachments/assets/c96ff83b-53c3-47db-a901-06af571ccd6e"
       alt="Login Screen" 
       width="250" 
       style="margin: 10px; border-radius: 10px;" />
  <img 
       src="https://github.com/user-attachments/assets/70c623fb-1cda-4527-8166-4f8826e37dd8"
       alt="Dashboard" 
       width="250" 
       style="margin: 10px; border-radius: 10px;" />
  <img 
       src="https://github.com/user-attachments/assets/bf15618f-d746-456d-a7a8-08e865780147"
       alt="Student List" 
       width="250" 
       style="margin: 10px; border-radius: 10px;" />
</p>

---

## 🧠 Tech Stack

**Language:** Kotlin  
**Architecture:** MVVM (ViewModel + LiveData)  
**UI Framework:** Material Design Components  

**Libraries Used:**
- `Retrofit2` + `Gson` → RESTful API communication  
- `OkHttp3` → HTTP client + logging  
- `Coroutines` → Asynchronous network calls  
- `Lottie` → Splash screen animation  
- `ConstraintLayout` + `Material Components` → UI design  

---

## 🌐 Backend Integration

This Android app communicates with a FastAPI backend built with Python, SQLModel, and MySQL to manage student data securely and efficiently.
Backend Repository: Student Management Backend

- The app uses Retrofit2 to call REST API endpoints exposed by the backend.
- Endpoints include:
- POST /login — Admin login
- GET /students — Fetch all students
- POST /students — Add a new student
- PUT /students/{id} — Update student details
- GET /profile & PUT /profile — Admin profile operations

## ⚡ Setup Note

Make sure the backend is running locally or on a server, and the app’s BASE_URL points to the correct backend URL. Example in ApiClient.kt:
**Example API Endpoints:**

```
object ApiClient {
    private const val BASE_URL = "http://192.168.1.100:8000/" // Update with your backend IP
    val apiService: ApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
}

```
This ensures that all app requests are correctly routed to your backend API.

| Endpoint | Description |
|----------|-------------|
| `/login` | Authenticate admin login |
| `/students` | Fetch list of students |
| `/students/{id}` | Get, update, or delete a student |
| `/students/add` | Add a new student record |
| `/profile` | Fetch or update admin profile |

---

## 📦 App Setup Guide

### ✅ Step 1 — Project Configuration

- Open in **Android Studio Flamingo or newer**  
- Minimum SDK: **24 (Android 7.0)**  
- Target SDK: **34**  
- Language: **Kotlin**

### ✅ Step 2 — Install Required Dependencies

Add these in your **app-level `build.gradle`**:

```gradle
// AndroidX Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // RecyclerView + CardView
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation("androidx.cardview:cardview:1.0.0")

    // Retrofit + Gson
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // OkHttp Logging
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // Lottie Animation
    implementation("com.airbnb.android:lottie:6.1.0")
```
# 👨‍💻 Developer Info

**Developed by Akshit Singh**  

- 💻 GitHub: [@akshit-singhh](https://github.com/akshit-singhh)  
- 📧 Email: akshitsingh658@gmail.com  
- 🔗 LinkedIn: [linkedin.com/in/akshit-singhh](https://www.linkedin.com/in/akshit-singhh)

---

# ⭐ Support

If you like this project, please **star this repository 🌟** and consider contributing ideas or improvements!
