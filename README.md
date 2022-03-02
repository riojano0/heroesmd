# Heroes 

ABM to manage Heroes

# Where see the API documentation?

The most updated version is on swagger

```{host}/swagger-ui/```

# How to generate jar

```mvn clean compile package```

# How to run
Check on your target folder what is the correct version

```java -jar heroesmdjar-0.1-SNAPSHOT.jar```

# How to generate Dockerfile

```docker build . -t heroesmd```

# How to run Docker Image

```docker run --name heroesmd-image -h heroesmd heroesmd```
