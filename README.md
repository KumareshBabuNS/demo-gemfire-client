#Gemfire Client

To build:
```
./mvnw clean package -DskipTests
```

If you run this app in PCF, make sure you have User-provided services for Gemfire Locator, the name of variables are:

LOCATOR_HOST

LOCATOR_PORT