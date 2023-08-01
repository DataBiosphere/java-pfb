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

**Alternative (more manual workflow):**

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
alias pfb='java -cp "cli/build/libs/java-pfb-cli<VERSION>.jar" bio.terra.pfb.JavaPfbCommand'
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

## Upgrade the CLI

1. Update java-pfb library version in the CLI's build.gradle to the latest tagged version.
2. Bump the version of the CLI in PfbVersion.java using semantic versioning.
```java
private static final String CLI_VERSION = "0.1.0";
```
3. Make a PR with the version updates and merge it to main.
4. Publish the CLI jar file to artifactory. We're publishing this in a similar method to the library. See more details in the [main README](../README.md#publishing-the-library-and-cli).
```shell
export ARTIFACTORY_USERNAME=$(vault read -field=username secret/dsp/accts/artifactory/dsdejenkins)
export ARTIFACTORY_PASSWORD=$(vault read -field=password secret/dsp/accts/artifactory/dsdejenkins)
./gradlew :cli:artifactoryPublish
```


