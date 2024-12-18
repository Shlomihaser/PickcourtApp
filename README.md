# Pickcourt

Pickcourt is an Android application for renting sports courts by the hour, offering a seamless experience for users to find and book courts for various sports like tennis, soccer, basketball, and padel.

## ✨ Features

- 🔐 User authentication using Firebase Authentication (guest, Google, regular user)
- 🏟️ Court listing and search functionality for various sports
- 🖼️ Detailed court view with images, description, and booking options
- ❤️ Favorite courts functionality
- 👤 User profile management
- 💳 Payment card management
- 📅 Booking history
- 🔑 Password reset option

## 🛠️ Technologies Used

- Android SDK
- Firebase (Authentication and Realtime Database)
- Material Design
- Dynamic UI components

## 🏗️ Project Structure

### Activities
- **LoginActivity**: Handles user login
- **SignUpActivity**: Manages user registration
- **SplashActivity**: Displays splash animation on app launch
- **MainDashActivity**: Main dashboard of the application

### Fragments
- **HomeFragment**: Main landing page for users
- **CourtsFragment**: Displays a list of available courts
- **CourtInfoFragment**: Shows detailed information about a specific court
- **BookingFragment**: Handles the court reservation process
- **ReservationsFragment**: Displays user's booking history
- **FavoritesFragment**: Shows user's favorite courts
- **PaymentsFragment**: Manages payment methods
- **AboutFragment**: Provides information about the app
- **ContactUsFragment**: Allows users to contact support

### Database
- Firebase Realtime Database with collections for sports and courts

## 🔥 Firebase Integration

Pickcourt leverages Firebase for:

- 👤 User Authentication (email/password, Google, guest)
- 💾 Realtime Database for storing court information and user data
- 💳 storage of payment information

## 🚀 Getting Started

To get started with Pickcourt:

1. Clone this repository
2. Open the project in Android Studio
3. Set up a Firebase project and add the `google-services.json` file to the app directory
4. Build and run the application on an emulator or physical device

## 📸 Screenshots

![Home Screen](path/to/screenshot1.png)
*Home Screen*

![Court Listing](path/to/screenshot2.png)
*Court Listing*

![Booking Screen](path/to/screenshot3.png)
*Booking Screen*

![Favorites](path/to/screenshot4.png)
*Favorites*


