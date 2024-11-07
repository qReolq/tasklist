package qreol.project.tasklist.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import qreol.project.tasklist.config.TestConfig;
import qreol.project.tasklist.domain.exception.ResourceNotFoundException;
import qreol.project.tasklist.domain.user.Role;
import qreol.project.tasklist.domain.user.User;
import qreol.project.tasklist.service.UserService;
import qreol.project.tasklist.web.dto.auth.JwtRequest;
import qreol.project.tasklist.web.dto.auth.JwtResponse;
import qreol.project.tasklist.web.security.JwtTokenProvider;

import java.util.Collections;
import java.util.Set;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthServiceImpl authService;

    @Test
    void login() {
        Long userId = 1L;
        String username = "username";
        String password = "password";
        Set<Role> roles = Collections.emptySet();
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        JwtRequest request = new JwtRequest();
        request.setUsername(username);
        request.setPassword(password);
        User user = new User();
        user.setId(userId);
        user.setUsername(username);
        user.setRoles(roles);

        Mockito.when(userService.getByUsername(username)).thenReturn(user);
        Mockito.when(jwtTokenProvider.createAccessToken(user)).thenReturn(accessToken);
        Mockito.when(jwtTokenProvider.createRefreshToken(user)).thenReturn(refreshToken);
        JwtResponse response = authService.login(request);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword())
        );

        Assertions.assertEquals(username, response.getUsername());
        Assertions.assertEquals(userId, response.getId());
        Assertions.assertNotNull(response.getAccessToken());
        Assertions.assertNotNull(response.getRefreshToken());
    }

    @Test
    void loginWithIncorrectUsername() {
        String username = "username";
        String password = "password";
        JwtRequest request = new JwtRequest();
        request.setUsername(username);
        request.setPassword(password);

        Mockito.when(userService.getByUsername(username))
                .thenThrow(ResourceNotFoundException.class);
        Mockito.verifyNoInteractions(jwtTokenProvider);
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> authService.login(request));
    }

    @Test
    void refresh() {
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        String newRefreshToken = "newRefreshToken";
        JwtResponse response = new JwtResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(newRefreshToken);

        Mockito.when(jwtTokenProvider.refreshUserToken(refreshToken)).thenReturn(response);
        JwtResponse testResponse = jwtTokenProvider.refreshUserToken(refreshToken);

        Mockito.verify(jwtTokenProvider).refreshUserToken(refreshToken);
        Assertions.assertEquals(response, testResponse);

    }

}
