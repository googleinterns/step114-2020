package com.google.edith;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import org.junit.Test;
import org.junit.Before;
import java.io.IOException;
import java.security.GeneralSecurityException;
import javax.mail.MessagingException;
import org.mockito.MockitoAnnotations;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import java.util.Map;
import java.util.HashMap;
import static org.junit.Assert.assertTrue;

public class EmailServiceTest {
    /**
  private final UserService userService = UserServiceFactory.getUserService();
  private final EmailService emailService = new EmailService();
  private Map<String, Object> myMap = new HashMap<String, Object>() {{
        put("com.google.appengine.api.users.UserService.user_id_key", "12345");
    }};
  private final LocalServiceTestHelper loggedInTestHelper =
      new LocalServiceTestHelper(new LocalUserServiceTestConfig())
      .setEnvAttributes(myMap)
      .setEnvIsLoggedIn(true)
      .setEnvAuthDomain("gmail")
      .setEnvIsAdmin(true)
      .setEnvEmail("livseibert@gmail.com");

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testSendEmail() throws IOException, GeneralSecurityException, MessagingException {
    loggedInTestHelper.setUp();
    assertTrue(userService.isUserLoggedIn());
    String subject = "test subject";
    String body = "test body";
    emailService.sendEmail(subject, body);
  }*/
}
