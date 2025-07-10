Java PFB Library
===
## Referencing the Library
The library is published to a central Maven repository. To use the library, add the following to your build.gradle:
```groovy
implementation "bio.terra:java-pfb-library:<Tagged Version>"
```
You can find the latest tagged version on [Github](https://github.com/DataBiosphere/java-pfb/tags). 

## Publishing the Library
Google Artifact Registry (GAR) is used to publish libraries to a central Maven repository. The library version number is the version in settings.gradle. We use github actions to bump the version and publish to GAR.

**The Publishing Procedure**

1) After PR is merged to main branch:
   1) A Github Action bumps the minor version in settings.gradle and tags the release in github 
   2) Github Action publishes to libs-release-standard in GAR
2) To bump major version, we need manually update version in settings.gradle value first then create the release.

**Running Publish Locally**
You'll need to have permission under your @broadinstitute account to publish to the registry.  If you don't, contact DevOps.
```shell
export GOOGLE_CLOUD_PROJECT=dsp-artifact-registry
export GAR_LOCATION=us-central1
export GAR_REPOSITORY_ID=libs-release-standard
gcloud auth login <you>@broadinstitute.org
./gradlew publish
```