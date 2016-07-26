# dopidx

## Building
```
mvn package
```

## Running
```
java -jar target/dopidx-1.0.0-SNAPSHOT-jar-with-dependencies.jar 
```

For alternate logging:
```
java -Djava.util.logging.config.file=logging.properties -jar target/dopidx-1.0.0-SNAPSHOT-jar-with-dependencies.jar 
```

Logging levels can be varied by modifying logging.properties and setting dopidx.server.Worker.level to one of INFO, FINE or FINER.

## Runtime Requirements
Java 8

## Design Rationale
I selected Java as the language because I could leverage my experience and it also allowed me to complete this exercise in the time I had available.

The Worker Thread Pool pattern was selected for handling the client connections in order to support many concurrent connections while preventing the server from being overwhelmed and run out of resources.

The Command pattern was selected for acting on the message because it is a data driven behavioral pattern and provides a model to determine what logic to invoke based on the command received in the message.

The MessageParser uses a regex for splitting the message into its parts because it allowed for more elegant querying of the individual parts of the message over simply splitting the string on the |.  The dependencies were simply being read into a List, so split was chosen over a regex.

An in-memory persistence was selected due to unknown system requirements and the limitation of no third party libraries.  If the state of the index needs to be maintained across runs, a new implementation of the Index interface may easily be created to support file system or database persistence.

While not required, and this project was simple enough, use of some type of dependency injection like Spring or Guice would have allowed for dependencies to be injected into the Command implementations and not have to be passed along from the Server and Worker classes.

Finally, the assumption is that the packages dependencies do not change after being indexed.
