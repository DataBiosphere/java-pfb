Java PFB CLI
===

## Available Commands
- hello
- getNumber5
- --version
- --help

## Local Run and Development

### Option 1 - Run via Gradle (Suggested workflow)

Run the following command to build the library and CLI jar and execute a command:
```shell
./gradlew run --args=<command>
```

### Option 2: Assemble distribution zip and run
Run the following command to build the library and CLI jar:
```shell
./gradlew :cli:assembleDist
```
Unzip the distribution
```shell
unzip cli/build/distributions/cli-<VERSION>.zip
```
Run commands
```shell
./cli-<VERSION>/bin/cli <command>
```

### Option 3: Build and run jar
Run the following command to build the library and CLI jar:
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

# Download and run the CLI
## Option 1 - Run from Jar Downloaded from Github Release
1) Download Jar from latest Github release.
2) Note the location of the downloaded jar and run the jar with the following command:
```shell
java -cp "<PATH-TO-JAR>" bio.terra.pfb.JavaPfbCommand <command>
```
## Option 2 - Run from Distribution Downloaded from Github Release
1) Download Distribution zip from latest Github release.
2) Unzip the distribution
```shell
unzip <PATH-TO-ZIP>/cli-<VERSION>.zip
```
3) Note the location of the unzipped distribution and run the CLI with the following command:
```shell
./cli-<VERSION>/bin/cli --version
```

## Upgrade the CLI

Github release containing the CLI jar is automatically generated via Github actions when a change is pushed to main. 


