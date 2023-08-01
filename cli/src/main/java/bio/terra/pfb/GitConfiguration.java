package bio.terra.pfb;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GitConfiguration {
  private static final String GIT_PROPERTIES_FILE_NAME = "git.properties";
  private static final String CLI_VERSION_BUILD_PROPERTY = "javapfb.cli.version.build";

  public String getCliVersion() {
    return readGitPropertiesValue(CLI_VERSION_BUILD_PROPERTY);
  }

  private String readGitPropertiesValue(String propertyName) {
    String result = "";
    InputStream inputStream = null;
    try {
      Properties prop = new Properties();
      inputStream = getClass().getClassLoader().getResourceAsStream(GIT_PROPERTIES_FILE_NAME);
      if (inputStream != null) {
        prop.load(inputStream);
      } else {
        throw new FileNotFoundException(
            "Property file '" + GIT_PROPERTIES_FILE_NAME + "' not found in the classpath");
      }
      return prop.getProperty(propertyName);
    } catch (IOException e) {
      System.out.println("Encountered exception while reading git properties: " + e);
    } finally {
      try {
        if (inputStream != null) {
          inputStream.close();
        }
      } catch (IOException e) {
        System.out.println(e);
      }
    }
    return result;
  }
}
