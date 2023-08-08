package bio.terra.pfb;

import java.io.IOException;
import java.util.Properties;
import picocli.CommandLine;

// Read properties from cli/build/resources/main/git.properties file
// Enables automatically pulling library version from git.properties to use as CLI version
// Note: Generate the git.properties file by running the generateGitProperties Gradle Command
public class GitConfiguration {
  private final Properties properties;
  private static final String GIT_PROPERTIES_FILE_NAME = "git.properties";
  private static final String CLI_VERSION_BUILD_PROPERTY = "javapfb.cli.version.build";

  public GitConfiguration() {
    properties = loadProperties();
  }

  Properties loadProperties() {
    try (var inputStream =
        getClass().getClassLoader().getResourceAsStream(GIT_PROPERTIES_FILE_NAME)) {
      var props = new Properties();
      props.load(inputStream);
      return props;
    } catch (NullPointerException | IOException e) {
      throw new CommandLine.InitializationException("Failed to load git properties", e);
    }
  }

  public String getCliVersion() {
    return properties.getProperty(CLI_VERSION_BUILD_PROPERTY);
  }
}
