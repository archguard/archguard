# !!Experiment project 

# CodeDB for searching code snippets 

Todos:

- [ ] align APIs
  - test cli: `java -jar/scanner_cli.jar --language=Kotlin --features=apicalls --output=http --output=json --path=. --server-url=http://localhost:8084`
- [ ] new features
  - [ ] search by file name
  - [ ] search by ast
  - [ ] search by regex
- [ ] feeder  

## What is it?

CodeDB is a tool for searching code snippets. 
It is a command line tool that can be used to search code snippets from a database of code snippets.
It is written in Kotlin and uses MongoDB as the database backend.

## How to use it?

### Install

1. setup JDK 17
2. install MongoDB and start it
   - follow: [https://www.mongodb.com/docs/manual/installation/](https://www.mongodb.com/docs/manual/installation/)
3. start spring boot application
   - `./gradlew bootRun`
