# Running the app
* Go to the app dir where pom.xml is
* Build the app and run the tests by running the following command: mvn clean install
* Run the app by running the following command: mvn spring-boot:run -Dspring-boot.run.arguments="[logfile path]"
Where [logfile path] is the complete path to the log file including the file name, eg /var/logs/logfile.txt
  
# Comments
I never had an experience with HSQLDB before, so I didn't figure out how to use it in the file system mode yet, taking
into account the lack of time, so for this challenge I used it in in-memory mode.
