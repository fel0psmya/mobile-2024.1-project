<h3 align="center">My Gaming Database: V 0.1.0</h3>



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

---

### **Screen Structure**

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
<div style="display: flex; width:20vw; gap:5px; height: 30vh;">
  <img src="https://raw.githubusercontent.com/fel0psmya/mobile-2024.1-project/e4e4fefc3645741e205a0689c364234edb64f832/Screenshots/Entrega%201/FavoritesScreen.jpg" style="margin-right: 10px;">
  <img src="https://raw.githubusercontent.com/fel0psmya/mobile-2024.1-project/e4e4fefc3645741e205a0689c364234edb64f832/Screenshots/Entrega%201/GameDetailsScreen.jpg">
  
  <img src="https://raw.githubusercontent.com/fel0psmya/mobile-2024.1-project/e4e4fefc3645741e205a0689c364234edb64f832/Screenshots/Entrega%201/HelpScreen.jpg">
  <img src="https://raw.githubusercontent.com/fel0psmya/mobile-2024.1-project/e4e4fefc3645741e205a0689c364234edb64f832/Screenshots/Entrega%201/HomeScreen.jpg">
  <img src="https://raw.githubusercontent.com/fel0psmya/mobile-2024.1-project/e4e4fefc3645741e205a0689c364234edb64f832/Screenshots/Entrega%201/SettingsScreen.jpg">
</div>

### 🏁Sprint 2
<div style="display: flex; width:20vw; gap:5px; height: 30vh;">
  <img src="https://github.com/fel0psmya/mobile-2024.1-project/blob/main/Screenshots/2nd%20Delivery/ListScreen%20-%20List%20Tab.jpg?raw=true" style="margin-right: 10px;">
  <img src="https://github.com/fel0psmya/mobile-2024.1-project/blob/main/Screenshots/2nd%20Delivery/GameDetailsScreen.jpg?raw=true">
  <img src="https://github.com/fel0psmya/mobile-2024.1-project/blob/main/Screenshots/2nd%20Delivery/HelpScreen.jpg?raw=true">
  <img src="https://github.com/fel0psmya/mobile-2024.1-project/blob/main/Screenshots/2nd%20Delivery/HomeScreen.jpg?raw=true">
  <img src="https://github.com/fel0psmya/mobile-2024.1-project/blob/main/Screenshots/2nd%20Delivery/Drawer.jpg?raw=true">
  <img src="https://github.com/fel0psmya/mobile-2024.1-project/blob/main/Screenshots/2nd%20Delivery/SettingsScreen.jpg?raw=true">
</div>

## 🎈Video demo <a name="Video"></a>
<a href="https://drive.google.com/file/d/17t_LahBxm9gcY35ldpBFQIWylDbvES45/view?usp=drivesdk">To watch the videolick here !</a>

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

