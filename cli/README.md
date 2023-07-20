Current usage of CLI:
First, run "jar" gradle task in cli project.
```shell
./gradlew :cli:jar
```

Then, you can use the CLI with the following command:
```shell
java -cp "cli/build/libs/java-pfb-cli.jar" bio.terra.pfb.JavaPfbCommand <command>
```
Available Commands:
- hello
- --version
- --help