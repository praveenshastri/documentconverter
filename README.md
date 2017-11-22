## documentconverter
REST Service to convert xlsx to csv file

#### Technologies used
 - Spring boot
 - Apache POI library for reading xlsx files.

### Features
 - Provides api to convert xlsx file to csv file
 - Creates a seperate csv file for every sheet present in xlsx file
  
### Build Requirements
  - Java 8 and up.
  - Maven 
  

### Steps to BUILD and RUN
  1. Clone the project using `git clone` command or download project using direct link
  2. Get into <PROJECT_ROOT_FOLDER>(documentconverter)
  3. Run below command to build executable jar
  ```sh
  mvn clean install
  ```
  4. To start the web service, get into target directory and run below command:
  ```sh
  java -jar documentconverter-0.0.1.jar 
  ```
  
 ### Usage
 - Call http://localhost:8080/upload with file as a parameter
 - csv files are by default created in `output` directory where documentconverter-0.0.1.jar present 
 
  
 - release branch
 - release branch comment 2
