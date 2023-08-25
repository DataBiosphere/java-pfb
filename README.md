# Java-PFB

A Java implementation of the [pyPFB](https://github.com/uc-cdis/pypfb) library that includes a CLI and a Java library. 

As discussed in the docs provided by [pyPFB](https://github.com/uc-cdis/pypfb/blob/master/docs/index.md#introduction), PFB files are avro files with a particular required schema. Given the specific [PFB schema](library/src/main/avro/pfbSchema.avdl), the [gradle avro plugin](https://github.com/davidmc24/gradle-avro-plugin) automatically generates class files on project build. Together with the [Avro Java library](https://avro.apache.org/docs/1.11.1/getting-started-java/), we are able to deserialize the PFB files and get the relevant peices of data: The schema, metadata, nodes, and table row data. 

## Getting Started
See the [library README](library/README.md) for more details on how to reference the lbirary in your project.

The CLI is a wrapper around the library. See the [CLI README](cli/README.md) for more information.

## Developer Information

### Publishing

See [library](library/README.md) and [cli](cli/README.md) readmes for more details. 

### Running SourceClear locally

[SourceClear](https://srcclr.github.io) is a static analysis tool that scans a project's Java
dependencies for known vulnerabilities. If you get a build failure due a SourceClear error and want
to debug the problem locally, you need to get the API token from vault before running the gradle
task.

```shell
export SRCCLR_API_TOKEN=$(vault read -field=api_token secret/secops/ci/srcclr/gradle-agent)
./gradlew srcclr
```

Results of the scan are uploaded to [Veracode](https://sca.analysiscenter.veracode.com/workspaces/jppForw/projects/768265/issues). You can request an account to view results from #dsp-infosec-champions. 

### Running SonarQube locally

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
