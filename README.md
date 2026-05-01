# Ndejje Coursework Tracker 🎓

A robust Android application designed to help Ndejje University students and coordinators manage academic assignments and coursework efficiently. Built with modern Android development practices, this app provides a seamless experience for tracking deadlines, organizing tasks, and facilitating communication between students and coordinators.

## 🚀 Key Features

### For Students 👨‍🎓
- **Personal Dashboard:** View a summary of upcoming deadlines and task progress.
- **Assignment Management:** Add, edit, and delete personal assignments.
- **Progress Tracking:** Mark assignments as completed and archive old tasks.
- **Smart Filtering:** Search and filter coursework by course code or completion status.
- **Notifications:** Receive timely reminders for approaching deadlines.
- **Dark Mode Support:** Switch between light and dark themes for better readability.

### For Coordinators 👩‍🏫
- **Broadcast System:** Post assignments and announcements to all students.
- **Course Management:** Add and manage different course units within the system.
- **Global Dashboard:** Oversee task distribution and managed content.

## 🛠 Tech Stack

- **Jetpack Compose:** For building a modern, declarative UI.
- **Hilt (Dagger):** Dependency injection for better modularity and testability.
- **Room Persistence:** Local database for storing assignments and courses.
- **DataStore:** Type-safe data storage for user preferences (login state, theme settings).
- **WorkManager:** Background processing for deadline notifications.
- **Kotlin Coroutines & Flow:** For reactive and asynchronous programming.
- **Material 3:** Modern design system for a consistent and accessible UI.

## 📁 Project Structure

```text
com.courseworktracker
├── data         # DataStore for user preferences
├── di           # Hilt modules for Dependency Injection
├── model        # Data classes and Entities (Assignment, Course)
├── repository   # Room DB, DAOs, and Repositories
├── ui.theme     # Compose theme, colors, and typography
├── view         # UI Components (Screens and reusable Composables)
├── viewmodel    # Business logic and UI state management
└── worker       # Background workers for notifications
```

## 🛠 Installation & Setup

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/Lubs/Ndejje-Coursework-Tracker.git
    ```
2.  **Open in Android Studio:**
    Ensure you are using the latest version of Android Studio (Ladybug or newer recommended).
3.  **Build System:**
    - Gradle: 9.4.1+
    - AGP (Android Gradle Plugin): 9.2.0+
    - Kotlin: 2.2.10
4.  **Run the app:**
    Select a device or emulator and click the 'Run' button.

## 📖 How to Use

### Authentication
- **Login/Register:** Users can create an account as either a Student or a Coordinator.
- **Coordinator Access:** Use "coordinator" in your email during registration to gain access to the Coordinator Dashboard.

### Managing Coursework (Student)
- Click the **"+"** button on the Home Screen to add a new assignment.
- Tap an assignment to **Edit** or **Delete** it.
- Swipe or check the box to mark a task as **Complete**.

### Coordinating (Coordinator)
- Access the **Coordinator Dashboard** to broadcast new assignments to all students.
- Manage the list of active courses available in the system.

## 📄 License

This project was developed as part of the Mobile Programming Group 5 KLA Capstone Project. All rights reserved.
