Java PFB Library
===

## Publishing the Library
See more details in the [main README](../README.md#publishing-the-library-and-cli).
To just publish the library, you can use the following commands:
```shell
export ARTIFACTORY_USERNAME=$(vault read -field=username secret/dsp/accts/artifactory/dsdejenkins)
export ARTIFACTORY_PASSWORD=$(vault read -field=password secret/dsp/accts/artifactory/dsdejenkins)
./gradlew :library:artifactoryPublish
```