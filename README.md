# Java-PFB

A java implementation of the [pyPFB](https://github.com/uc-cdis/pypfb) library that includes a CLI and a java library. 

The CLI is a wrapper around the library. See the [CLI README](cli/README.md) for more information.


## Publishing
See [library](library/README.md) and [cli](cli/README.md) readmes for more details. 


## Running SourceClear locally

[SourceClear](https://srcclr.github.io) is a static analysis tool that scans a project's Java
dependencies for known vulnerabilities. If you get a build failure due a SourceClear error and want
to debug the problem locally, you need to get the API token from vault before running the gradle
task.

```shell
export SRCCLR_API_TOKEN=$(vault read -field=api_token secret/secops/ci/srcclr/gradle-agent)
./gradlew srcclr
```

Results of the scan are uploaded to [Veracode](https://sca.analysiscenter.veracode.com/workspaces/jppForw/projects/768265/issues). You can request an account to view results from #dsp-infosec-champions. 

## Running SonarQube locally

[SonarQube](https://www.sonarqube.org) is a static analysis code that scans code for a wide
range of issues, including maintainability and possible bugs. If you get a build failure due to
SonarQube and want to debug the problem locally, you need to get the the sonar token from vault
before runing the gradle task.

```shell
export SONAR_TOKEN=$(vault read -field=sonar_token secret/secops/ci/sonarcloud/java-pfb)
./gradlew sonar
```

Unlike SourceClear, running this task produces no output unless your project has errors. To always
generate a report, run using `--info`:

```shell
./gradlew sonar --info
```

We run the scans for two projects: [java-pfb](https://sonarcloud.io/project/overview?id=DataBiosphere_java-pfb) and [java-pfb-cli](https://sonarcloud.io/project/overview?id=DataBiosphere_java-pfb-cli). The results are uploaded to the sonarcloud dashbaord. 
