# Stock App

A sample java REST backend + React frontend application for performing CRUD operations on stocks 

## Backend 

### Tools 

- Java 
- Gradle
- Resteasy 
- AppEngine (as servlet container)

### How to run 

Before running, please make sure gradle is installed in your machine, if not use `./gradlew` instead of `gradle` to run the application.

Clone the application to local machine, and switch to the project directory
```
git clone {repo_url}
```

From the root folder, run 
```
gradle appengineRun
```
app will be running on port `3000`


## Frontend 

Front-end is built as an separate application, and its under `frontend` folder

### Tools 

- React
- Npm
- Webpack 

### How to run 

Switch to frontend folder
```
cd frontend/
```

Install dependencies  
```
npm install
```

**Running application**
```
npm run start
```
app will be running on port `3001`