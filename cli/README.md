Java PFB CLI
===

## Available Commands
- hello
- getNumber5
- --version
- --help

## Local Run and Development
**Suggested workflow:**

Run the following command to build the library and CLI jar and execute a command:
```shell
./gradlew run --args=<command>
```

**Alternative:**

First, run "jar" gradle task in cli project.
```shell
./gradlew :cli:jar
```

Then, you can use the CLI with the following command:
```shell
java -cp "cli/build/libs/java-pfb-cli-<VERSION>.jar" bio.terra.pfb.JavaPfbCommand <command>
```
An alias can be created to simplify this command:
```shell
alias pfb='java -cp "cli/build/libs/java-pfb-cli-<VERSION>.jar" bio.terra.pfb.JavaPfbCommand'
```
Example usage of the alias:
```shell
pfb --version
```

## Run from Jar Downloaded from Github Release
1) Download Jar from latest Github release.
2) Note the location of the downloaded jar and run the jar with the following command:
```shell
java -cp "<REPLACE WITH PATH TO JAR>" bio.terra.pfb.JavaPfbCommand <command>
```
Example path to jar: /Downloads/java-pfb-cli-0.1.0-20230724.180554-1-sources.jar

## Upgrade the CLI

Github release containing the CLI jar is automatically generated via Github actions when a change is pushed to main. 


