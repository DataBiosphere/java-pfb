package scripts.testscripts;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import bio.terra.javapfb.api.PublicApi;
import bio.terra.testrunner.runner.TestScript;
import bio.terra.testrunner.runner.config.TestUserSpecification;
import com.google.api.client.http.HttpStatusCodes;
import scripts.client.JavaPfbClient;

public class GetVersion extends TestScript {
  @Override
  public void userJourney(TestUserSpecification testUser) throws Exception {
    JavaPfbClient client = new JavaPfbClient(server);
    var publicApi = new PublicApi(client);

    var versionProperties = publicApi.getVersion();

    // check the response code
    assertThat(client.getStatusCode(), is(HttpStatusCodes.STATUS_CODE_OK));

    // check the response body
    assertThat(versionProperties.getGitHash(), notNullValue());
    assertThat(versionProperties.getGitTag(), notNullValue());
    assertThat(versionProperties.getGithub(), notNullValue());
    assertThat(versionProperties.getBuild(), notNullValue());
  }
}
