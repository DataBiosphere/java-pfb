Java PFB CLI
===

## Available Commands
- show
- --version
- --help

Examples:
```shell
Usage: pfb show [OPTIONS] COMMAND [ARGS]...

  Show records of the PFB file.

  Specify a sub-command to show other information.

Options:
  -i, --input FILENAME  The PFB file.
  -n, --limit INTEGER   How many records to show, ignored for sub-commands.
                        [default: no limit]

Commands:
  metadata  Show the metadata of the PFB file.
  nodes     Show all the node names in the PFB file.
  schema    Show the schema of the PFB file.

Examples:
  schema:
    pfb show -i data.avro schema
    ./gradlew run --args="show -i data.avro schema"
  nodes:
    pfb show -i data.avro nodes
    ./gradlew run --args="show -i data.avro nodes"
  metadata:
    pfb show -i data.avro metadata
    ./gradlew run --args="show -i data.avro metadata"
  records:
    pfb show -i data.avro -n 5
    ./gradlew run --args="show -i data.avro -n 5"
```

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
java -jar "cli/build/libs/java-pfb-cli-<VERSION>.jar" <command>
```
An alias can be created to simplify this command:
```shell
alias pfb='java -jar "cli/build/libs/java-pfb-cli-<VERSION>.jar"'
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


