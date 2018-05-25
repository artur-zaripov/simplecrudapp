# Simple CRUD app
Demo application with CRUD operations powered by Spring Boot.

## Required tools
1.	Apache Maven (http://maven.apache.org);
2.	Docker (http://docker.com).

## Hot to build and run
Build command (make sure that Docker is running):

**mvn clean package docker:build**

Run command:

**docker-compose up**

*Note:* if you are Windows user and have error *./run.sh: not found* - check line endings 
 in the *src/main/docker/run.sh* - depending on your git configuration they may be automatically
 changed from Unix-style [*\n*] (which is correct) to Windows-style [*\r\n*].
 
## API documentation
Is available via:

**http://localhost:8080/swagger-ui.html**