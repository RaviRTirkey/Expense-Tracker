# 📱 Expense Tracker Android App  

A modern and user-friendly **Expense Tracker** built with **Kotlin**, **MVVM Architecture**, **Room Database**, and **Firebase Authentication**.  
This app helps users **manage their expenses**, categorize transactions, view **monthly reports**, and search for expenses efficiently.  

---

## 🚀 Features  

- **User Authentication**  
  - Firebase Email/Password login and signup  
  - Session management with SharedPreferences  

- **Expense Management**  
  - Add, edit, and delete transactions  
  - Category selection with icons  
  - Date picker integration  

- **Filtering & Search**  
  - Month-wise filtering  
  - Real-time search functionality  

- **Expense Reports**  
  - Category-wise total spending  
  - Monthly summary calculation  

- **UI & UX**  
  - Material UI components  
  - Smooth navigation using Jetpack Navigation  
  - Responsive layout for all devices  

---

## 🛠 Tech Stack  

| Layer              | Technology Used |
|--------------------|-----------------|
| **Language**       | Kotlin |
| **Database**       | Room (SQLite) |
| **Authentication** | Firebase Auth |
| **Architecture**   | MVVM |
| **UI Framework**   | Material Components |
| **Navigation**     | Jetpack Navigation Component |
| **Session**        | SharedPreferences |


---

## 📸 Screenshots  

## 📸 Screenshots of UI

<p align="center">
  <img src="https://github.com/RaviRTirkey/Expense-Tracker/blob/master/screenshots/launch.png" alt="Launch Screen" width="250"/>
  <img src="https://github.com/RaviRTirkey/Expense-Tracker/blob/master/screenshots/signup.png" alt="Signup Screen" width="250"/>
  <img src="https://github.com/RaviRTirkey/Expense-Tracker/blob/master/screenshots/signin.png" alt="Sign in Screen" width="250"/>
</p>

<p align="center">
  <img src="https://github.com/RaviRTirkey/Expense-Tracker/blob/master/screenshots/home.jpg" alt="Home Screen" width="250"/>
  <img src="https://github.com/RaviRTirkey/Expense-Tracker/blob/master/screenshots/add.png" alt="Add Expense Screen" width="250"/>
  <img src="https://github.com/RaviRTirkey/Expense-Tracker/blob/master/screenshots/edit.png" alt="Edit Expense Screen" width="250"/>
</p>

<p align="center">
  <img src="https://github.com/RaviRTirkey/Expense-Tracker/blob/master/screenshots/report.jpg" alt="Report Screen" width="250"/>
  <img src="https://github.com/RaviRTirkey/Expense-Tracker/blob/master/screenshots/setting.jpg" alt="Settings Screen" width="250"/>
</p>


---

## 📂 Project Structure  


    com.example.expensetracker
    │── room
    │ ├── Transaction.kt
    │ ├── TransactionDAO.kt
    │ ├── TransactionDatabase.kt
    │ ├── TransactionRepository.kt
    │
    │── viewModel
    │ ├── TransactionViewModel.kt
    │ ├── TransactionViewModelFactory.kt
    │
    │── ui
    │ ├── HomeScreen.kt
    │ ├── ExpenseReport.kt
    │ ├── LogInPage.kt
    │ ├── SignupScreen.kt
    │ ├── Settings.kt
    │
    │── adapters
    │ ├── TransactionAdapter.kt
    │ ├── CategoryTotalAdapter.kt
    │
    │── utils
    │ ├── SharedPreferencesManager.kt
    │
    │── ThisApplication.kt
    │── MainActivity.kt


---

## ⚙️ Installation  

1. **Clone the repository**  
   ```bash
   git clone https://github.com/yourusername/ExpenseTracker.git

2. **Open in Android Studio**

3. **Sync Gradle files**

4. **Setup Firebase**

    - Create a Firebase project at Firebase Console

    - Enable Email/Password Authentication

    - Download google-services.json and place it in the app/ folder

5. **Run the app on an emulator or device**

---

## 🤝 Contributing

1. **Fork the repo**

2. **Create a new branch**
    ```bash
    git checkout -b feature-name

3. **Commit changes**
    ```bash
    git commit -m "Add new feature"

4. **Push the branch**
    ```bash
    git push origin feature-name

5. **Create a Pull Request**
