Java PFB CLI
===

## Available Commands
- hello
- getNumber5
- --version
- --help

## Local Run and Development
First, run "jar" gradle task in cli project.
```shell
./gradlew :cli:jar
```

Then, you can use the CLI with the following command:
```shell
java -cp "cli/build/libs/java-pfb-cli.jar" bio.terra.pfb.JavaPfbCommand <command>
```
An alias can be created to simplify this command:
```shell
pfb='java -cp "cli/build/libs/java-pfb-cli.jar" bio.terra.pfb.JavaPfbCommand'
```
Example usage of the alias:
```shell
pfb --version
```


## Run from Artifactory Published Jar
1) Download Jar from artifactory: Search artifacts for "java-pfb-cli" on [artifactory](https://broadinstitute.jfrog.io/) and download the latest version of the jar. 
2) Note the location of the downloaded jar and run the jar with the following command:
```shell
java -cp "<REPLACE WITH PATH TO JAR>" bio.terra.pfb.JavaPfbCommand <command>
```
Example path to jar: /Downloads/java-pfb-cli-0.1.0-20230724.180554-1-sources.jar

## Publishing the CLI
We're publishing this in a similar method to the library. See more details in the [main README](../README.md#publishing-the-library-and-cli).
To just publish the CLI, you can use the following commands:
```shell
export ARTIFACTORY_USERNAME=$(vault read -field=username secret/dsp/accts/artifactory/dsdejenkins)
export ARTIFACTORY_PASSWORD=$(vault read -field=password secret/dsp/accts/artifactory/dsdejenkins)
./gradlew :cli:artifactoryPublish
```

