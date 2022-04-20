package com.azuriom.azauth;

import com.azuriom.azauth.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariables;

import static org.junit.jupiter.api.Assertions.*;

@EnabledIfEnvironmentVariables({
        @EnabledIfEnvironmentVariable(named = "AUTH_TEST_URL", matches = ".+"),
        @EnabledIfEnvironmentVariable(named = "AUTH_TEST_EMAIL", matches = ".+"),
        @EnabledIfEnvironmentVariable(named = "AUTH_TEST_PASSWORD", matches = ".+"),
})
class AuthenticatorTest {

    private final AzAuthenticator authenticator = new AzAuthenticator(System.getenv("AUTH_TEST_URL"));
    private final String testEmail = System.getenv("AUTH_TEST_EMAIL");
    private final String testPassword = System.getenv("AUTH_TEST_PASSWORD");

    @Test
    void testAuthenticateAndLogout() {
        User user = assertDoesNotThrow(() -> authenticator.authenticate(this.testEmail, this.testPassword));

        assertEquals(this.testEmail, user.getEmail());

        assertNotNull(user.getAccessToken());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getRole());
        assertNotNull(user.getRole().getColor());
        assertNotNull(user.getUuid());

        User verifiedUser = assertDoesNotThrow(() -> authenticator.verify(user.getAccessToken()));

        assertEquals(user, verifiedUser);

        assertDoesNotThrow(() -> authenticator.logout(user.getAccessToken()));

        assertThrows(AuthenticationException.class, () -> authenticator.verify(user.getAccessToken()));
    }

    @Test
    void testAuthenticateWithType() {
        CustomUser user = assertDoesNotThrow(() ->
                authenticator.authenticate(this.testEmail, this.testPassword, CustomUser.class));

        assertEquals(this.testEmail, user.email);
    }

    static class CustomUser {
        private String email;
        private String username;
        private String accessToken;
    }
}
