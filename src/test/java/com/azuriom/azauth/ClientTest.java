package com.azuriom.azauth;

import com.azuriom.azauth.exception.AuthException;
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
class ClientTest {

    private final AuthClient client = new AuthClient(System.getenv("AUTH_TEST_URL"));
    private final String testEmail = System.getenv("AUTH_TEST_EMAIL");
    private final String testPassword = System.getenv("AUTH_TEST_PASSWORD");

    @Test
    void testAuthenticateAndLogout() {
        AuthResult<User> result = assertDoesNotThrow(() -> client.login(this.testEmail, this.testPassword));
        assertTrue(result.isSuccess());

        User user = result.getSuccessResult();
        assertEquals(this.testEmail, user.getEmail());
        assertNotNull(user.getAccessToken());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getRole());
        assertNotNull(user.getRole().getColor());

        User verifyResult = assertDoesNotThrow(() -> client.verify(user.getAccessToken()));

        assertEquals(user, verifyResult);
        assertDoesNotThrow(() -> client.logout(user.getAccessToken()));
        assertThrows(AuthException.class, () -> client.verify(user.getAccessToken()));
    }

    @Test
    void testAuthenticateWithType() {
        AuthResult<CustomUser> result = assertDoesNotThrow(() ->
                client.login(this.testEmail, this.testPassword, CustomUser.class));

        assertTrue(result.isSuccess());
        assertEquals(this.testEmail, result.getSuccessResult().email);
    }

    static class CustomUser {
        private String email;
        private String username;
        private String accessToken;
    }
}
