Java PFB Library
===
## Referencing the Library
The library is published to a central Maven repository. To use the library, add the following to your build.gradle:
```groovy
implementation "bio.terra:java-pfb-library:<Tagged Version>"
```
You can find the latest tagged version on [Github](https://github.com/DataBiosphere/java-pfb/tags). 

## Publishing the Library
JFrog Artifactory is used to publish libraries to a central Maven repository. The library version number is the version in settings.gradle. We use github actions to bump the version and publish to Artifactory.

**The Publishing Procedure**

1) After PR is merged to main branch:
   a) A Github Action bumps the minor version in settings.gradle and tags the release in github
   b) A Github Action publishes to libs-release-local in Artifactory
2) To bump major version, we need manually update version in settings.gradle value first then create the release.

**Running Publish Locally**
```shell
export ARTIFACTORY_USERNAME=$(vault read -field=username secret/dsp/accts/artifactory/dsdejenkins)
export ARTIFACTORY_PASSWORD=$(vault read -field=password secret/dsp/accts/artifactory/dsdejenkins)
./gradlew artifactoryPublish
```