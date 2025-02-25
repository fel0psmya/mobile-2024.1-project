<h3 align="center">My Gaming Database: V 0.3.0</h3>



---

<p align="center"> Carlos Eduardo, Felipe da S. M.
    <br> 
</p>

## 📝 Table of Contents

- [About](#about)
- [Getting Started](#getting_started)
- [ScreenShots](#screenshots)
- [Video demo](#Video)
- [Tasks Development](#deployment)
- [Authors](#authors)

## 🧐 About <a name = "about"></a>

MyGamingDatabase is a game library management app inspired by MyAnimeList. It was developed with the goal of offering gamers a simple and efficient platform to organize their game libraries, track their progress, and discover new titles in a practical way. Aimed at both enthusiasts and casual gamers, the app aims to centralize relevant information about games and facilitate the management of personalized lists.
Here's the English version in the format of a README:

### **Key Features**
### 🏁Sprint 1

#### 1. **Game List Management**
   - **Favorite games list**: Store your favorite games for easy access.
   - **Add games**: Include games with details such as name, description, platforms, and release date.
   - **Favorite games**: Mark games as favorites for quick access.

#### 2. **Game Search and Discovery**
   - **Search bar**: Search for games by name with auto-filtered results.
   - **Future integration with IGDB API**: Get detailed information about games, including screenshots, trailers, and community reviews.

#### 3. **Recent Search History** *(Not Implemented Yet)*
   - A dynamic list of recently searched games for quick access.

#### 4. **Game Details**
   - A dedicated screen showing complete game information, including name, cover image, description, platforms, and release date.
   - Option to favorite a game directly from the details screen.

#### 5. **Intuitive and Responsive Interface**
   - Smooth navigation between screens with a bottom navigation bar.
   - Modern theme using Jetpack Compose and Material 3, supporting both light and dark modes.

#### 6. **Expanded Image View**
   - Ability to expand the game cover image to full screen for better viewing.
### 🏁Sprint 2
#### 7. **List Management Enhancements**
   - **Tabbed Interface**: Combines "Favorites" and "My List" into a unified screen with two tabs.
     - **Favorites Tab**: Displays a grid of games marked as favorites. Includes a button on each game cover for quick removal from favorites.
     - **My List Tab**: Displays a grid of games added to the user's list, with a dropdown menu for editing or removing games. Supports filtering games by status ("Playing", "Want to Play", "Droped", "Completed").

#### 8. **Notifications and Reminders**
   - **Custom Notifications**: Allows users to set reminders for specific games.
   - **Recurring Alerts**: Configurable weekly notifications for games on the "Playing" or "Wanted" list.

#### 9. **Smooth Animations**
   - **Fade-in Effects**: Game cards on the Home Screen feature fade-in animations for a polished experience.
   - **Interactive Elements**: Buttons include scale and color animations for enhanced feedback during user interactions.
   - **Slide Transitions**: Smooth slide animations when navigating between screens, adding a dynamic and seamless feel to the user experience.

#### 10. **Enhanced Filtering and Sorting**
   - **Status-Based Sorting**: Organizes games in "My List" by their status, making it easier to manage "Playing", "Wanted", "Droped" and "Completed" games.

#### 11. **Enhanced Settings Screen**
   - **Theme Persistence**: The application theme (light or dark mode) is now saved using Jetpack DataStore, ensuring user preferences are retained across sessions.
   - **Clear Favorites Option**: Easily clear the entire favorites list with a single action, confirmed via an alert dialog.
   - **Clear List Option**: Allows users to clear their entire game list, providing a streamlined way to manage their library.
   - **User-Friendly Interface**: Simplified controls for toggling between light and dark modes and managing lists efficiently.

#### 12. **Loading Indicator in Game Details Screen**
   - **Progress Indicator**: A loading animation is displayed when fetching detailed game information, providing feedback to users during data retrieval.
   - **Seamless Experience**: Ensures smooth transitions by indicating the loading state before the details are fully displayed.
   - **Consistent Design**: The indicator integrates seamlessly with the overall visual style of the application.

---

---

### **Future Expansions**
   - **Enhanced Game Details**: Integration of IGDB API to include screenshots, trailers, and community reviews directly in the details screen.
   - **Advanced Social Features**: 
     - User profiles showcasing created lists, favorite games, and completed games.
     - Sharing capabilities for lists with friends or the broader community.
   - **Community Reviews and Ratings**: Enable users to write and share reviews for games they have played, with ratings visible to others.
   - **Achievements and Badges**: Reward users with badges for milestones such as completing games or adding a certain number of favorites.
   - **Management Screen**: A dedicated screen for managing notifications, including options to edit or remove them.



---

### **Screen Structure**
### 🏁Sprint 1

#### 1. **Home Screen**
   - Displays the search bar and a list of filtered games.
   - Highlights recently added games.
   - Bottom navigation bar with "Home" and "Favorites" options.

#### 2. **Favorites Screen**
   - A list of games marked as favorites, organized for easy access.

#### 3. **Game Details Screen**
   - Shows detailed information about the selected game, including description, platforms, and an option to favorite or remove the game from the list.

#### 4. **Image Expansion Screen**
   - Shows the game cover in full-screen mode with an option to close the view.

#### 5. **Settings Screen**
   - Displays settings for toggling between light and dark modes, as well as clearing the favorites list.

#### 6. **Help Screen**
   - Provides helpful information and answers to frequently asked questions.
### 🏁Sprint 2

#### 7. **Favorites/My List Screen**
   - Redesigned with a **TabRow** to switch between "Favorites" and "My List."
   - **Favorites Tab**:
     - Displays a grid of favorited games with quick access to remove them.
   - **My List Tab**:
     - Shows games added by the user, with dropdown options for editing status or removing them.
     - Includes filters to display only games marked as "Playing", "Wanted", "Droped" or "Completed".

---

### **Future Expansions**
   - Screen for user-created game lists.
   - Game status options: "Playing," "Wanted," and "Completed."
   - User-written reviews for played games.
   - Integration with IGDB API for more accurate game data.
   - Social features, such as sharing lists with friends and user profiles with created lists and played games.
   - Personalized notifications for upcoming game releases added to the "Wanted" list.

---

**MyGamingDatabase** aims to be the ultimate solution for gamers who want to manage and organize their gaming passion in a simple, efficient, and enjoyable way, providing a personalized experience for every user.

## 🏁 Getting Started <a name = "getting_started"></a>

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Requirements

- **JDK**: Install JDK 11 or higher. Kotlin runs on the JVM, so it’s required for compiling and running Kotlin code.
  - [Download JDK](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)

- **IDE**: Use IntelliJ IDEA (recommended for Kotlin) or Android Studio for Android projects.
  - Download IntelliJ IDEA: [IntelliJ IDEA](https://www.jetbrains.com/idea/)
  - Download Android Studio: [Android Studio](https://developer.android.com/studio)

- **Gradle**: Gradle is the recommended build tool for Kotlin projects.
  - Install Gradle: [Gradle Download](https://gradle.org/install/)

### Running the Project

1. **Using an IDE**: Simply open the project in your IDE and run it.
2. **Using Gradle**: 
   - To build the project:  
     `gradle build`
   - To run the project:  
     `gradle run`
3. **Using the Command Line**:
   - Compile the Kotlin code:  
     `kotlinc MyProject.kt -include-runtime -d MyProject.jar`
   - Run the project:  
     `java -jar MyProject.jar`

## 📸Screenshots Evolution<a name = "screenshots"></a>
### 🏁Sprint 1
- <a href="https://drive.google.com/file/d/1YL1TqQFe8c_KNNowKjyO2G-wQeR9gpv_/view?usp=drivesdk">To see the images click here! </a>

### 🏁Sprint 2
- <a href="https://github.com/fel0psmya/mobile-2024.1-project/tree/main/Screenshots/2nd%20Delivery">To see the images click here!</a>

## 🎈Video demo <a name="Video"></a>
### 🏁Sprint 1
- <a href="https://github.com/fel0psmya/mobile-2024.1-project/tree/main/Screenshots/1st%20Delivery">To watch the video click here !</a>

### 🏁Sprint 2
- <a href="https://drive.google.com/file/d/17t_LahBxm9gcY35ldpBFQIWylDbvES45/view?usp=drivesdk">To watch the video click here !</a>

## 🚀 Tasks Development Roadmap <a name = "deployment"></a>
### 🚩Sprint 1:
| **Contributor** | **Tasks Completed** |
|-----------------|---------------------|
| **Felipe**      | Project setup and creation/implementation of the following files: GameData.kt (game model), BottomNavBar.kt, DrawerContent.kt (side menu), TopBar.kt, FavoritesScreen.kt, HomeScreen.kt, GameDetailsScreen.kt, and SettingsScreen.kt (without the "clear favorites" function implementation). |
| **Eduardo**     | Implemented the HelpScreen.kt. <br> Created the "clear favorites" functionality and simulated logout on the "logout" button in the side menu. <br> Modified app colors via Theme.kt. <br> Added a folder with PSD files related to the app icon. |
| **Eduardo**     | Created APK and adjusted padding on cards in HomeScreen, FavoritesScreen, and GameDetailsScreen. <br> Implemented functionality to return to the home screen when clicking on the app icon in the top bar. |
| **Felipe**      | Created the screenshots folder and added app screenshots. |
| **Felipe**      | Generated the APK of the app. |
| **Eduardo**     | Documentation and README. |

### 🚩Sprint 2:
| **Responsible**    | **Tasks Completed** |
|--------------------|--------------------|
| **Eduardo**  | Updated version from 1.0 to 0.1.0 in README.md and DrawerContent.kt at 1st delivery |
| **Felipe**      | Implemented game list and favorites management/alert dialogs |
| **Felipe**      | Implemented 'My List' screen, adjustments to dropdown buttons, alert dialogs, bottom navigation bar, drawer, and system theme support |
| **Eduardo**  | Implemented scheduled reminder (using AlarmManager with BroadcastReceivers) to play a specific game |
| **Eduardo**  | Implemented theme preference with JetPack DataStore |
| **Felipe**      | Implemented animations on buttons (color and size of elements), on the BottomNavBar and when opening screens |
| **Felipe**      | Rebuilt debug APK |
| **Eduardo**  | Removed 'Presentation Videos' folder and added '2nd Delivery' folder inside 'Screenshots' |
| **Eduardo**  | Added screenshots of 2nd delivery to documentation |



## ✍️ Authors <a name = "authors"></a>

- [@Felipe Maia](https://github.com/fel0psmya) - 514914
- [@Carlos Eduardo](https://github.com/EduardoNunexx) - 473825

