# Heroes 

ABM to manage Heroes

# What is needed to be configured to work?

Please note that to access the full ABM you will need to provide two environment variables for use all the crud operations 

```
HEROES-USER=
HEROES-PASSWORD=
```

This will be your credentials to use with basic


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

# See demo on Heroku

[HeroesMd - Swagger on Heroku](https://heroesmd.herokuapp.com/swagger-ui/)

Disclaimer: Remember that the instances from Heroku take a while to start the first time