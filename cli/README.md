Current usage of CLI:
First, run "jar" gradle task in cli project.
```shell
./gradlew :cli:jar
```

Then, you can use the CLI with the following command:
```shell
java -cp "java-pfb-cli/build/libs/java-pfb-cli.jar" JavaPfbCommand <command> <args>
```
Only command right now: "hello"