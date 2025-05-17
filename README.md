# Gyms Finder App

A mobile application for searching, viewing, posting, and commenting on gyms, built with Jetpack Compose and Firebase. Users can register, log in, post gym information, view gyms on a map, and leave comments.

## Features

- User Registration & Login: Register with email, set your own username, and securely log in.
- Post Gyms: All users can add gyms with name, address (auto-located via Geocoder), and image.
- View Gyms List: Browse all gyms, tap any to view details and comments.
- Google Map Integration: View all gyms on a map, markers show location and info.
- Gym Details: See gym images, address, poster, and real-time comments.
- Commenting System: Users can leave and view comments under each gym.
- Real-Time Updates: All data changes (gyms & comments) instantly reflected via Firestore listeners.

## Screenshots
![59e24bdac1dff167a970ee9d792c020](https://github.com/user-attachments/assets/b11a1d71-47e7-46b9-bb99-3dd7859af3c5)
![d4e2bb30a776c380358f6129fd0a70f](https://github.com/user-attachments/assets/29503702-6772-4b1b-a38c-71033b61b66c)
![4e39fea1b98a421c84af4c5501ab41b](https://github.com/user-attachments/assets/931d0d3b-5ff6-4e0a-9df0-a76c7954786c)



## Tech Stack

- Kotlin & Jetpack Compose
- Google Maps Compose
- Firebase Authentication
- Firebase Firestore (Cloud Database)
- Firebase Storage (Image uploads, if used)
- Coil (Image loading)
- Coroutines

## Project Structure

Gyms/
├── MainActivity.kt
├── GymListScreen.kt
├── GymDetailScreen.kt
├── AddGymDialog.kt
├── GymMapScreen.kt
├── LoginScreen.kt
├── models/
│ ├── Gym.kt
│ ├── Comment.kt
│ └── User.kt
├── ui/theme/
│ └── GymsTheme.kt
└── ...


## Getting Started

1. **Clone this repository**

    ```bash
    git clone https://github.com/yourusername/Gyms.git
    cd Gyms
    ```

2. **Set up Firebase**

   - Create a project on [Firebase Console](https://console.firebase.google.com/)
   - Enable Authentication (Email/Password)
   - Create Firestore Database
   - Download your `google-services.json` and add it to your app's `/app` folder

3. **API Keys**

   - Set up Google Maps API Key and add it to your `AndroidManifest.xml`

4. **Build and Run**

   - Open with Android Studio
   - Run on emulator or device

## Contribution & Version Control

- All commits follow sprint-based workflow:
  - Sprint 2: Integrated Google Map API, added map view.
  - Sprint 3: Added gym detail page, comment feature, and search.
- Project managed via Trello (see board for sprints/tasks)
- Version control: [GitHub Repository](https://github.com/hw8853815/Gyms))

## License

MIT License

## Contact

Maintainer: [hanwu hanwu8853815@gmail.com]  
GitHub: (https://github.com/hw8853815/Gyms)
