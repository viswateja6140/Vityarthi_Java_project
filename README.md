# Media Catalog – Movies & Shows Management System
A Java console-based application to manage Movies and TV Shows with features such as watchlist tracking, progress tracking, search, update, and CSV data persistence.

---

## Features

### Movies
- Add new movies  
- Track whether a movie is watched or not  
- Add movies to a watchlist  
- Update movie details  
- Search movies by title or genre  
- Save all movie data into `movies.csv`  

### TV Shows
- Add new shows  
- Track watch progress (Season and Episode)  
- Add shows to watchlist  
- Update show details and progress  
- Search shows by title or genre  
- Save all show data into `shows.csv`

### Watchlist
- Display all movies and shows currently in the watchlist  

### CSV Persistence
All movies and shows are automatically loaded from:
- `movies.csv`
- `shows.csv`

and saved back when the user selects the “Save & Exit” option.

---

## Project Structure
JavaProject_MediaCatalog/
 ├─ src/
 │  ├─ models/
 │  │  ├─ Media.java
 │  │  ├─ Movie.java
 │  │  └─ Show.java
 │  ├─ services/
 │  │  └─ Catalog.java
 │  └─ app/
 │     └─ MediaApp.java
 ├─ movies.csv
 ├─ shows.csv
 ├─ README.md
 └─ STATEMENT.md

 ---

## How to Compile and Run

### 1. Navigate to the project folder
### 2. Compile the project
        javac -d out "src/models/.java" "src/services/.java" "src/app/*.java"
### 3. Run the application
        java -cp out app.MediaApp


---

## Technologies Used
- Java (Core Java)
- OOP: Inheritance, Polymorphism, Encapsulation
- File I/O using CSV format
- Collections Framework (ArrayList)

---

## Future Enhancements
- JSON or database-based storage  
- Sorting and filtering features  
- Ratings and reviews  
- Graphical User Interface (Swing/JavaFX)  
- Exporting reports  

---

## Author

Adhikari Viswa Teja (24MIM10138)
