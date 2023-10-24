package qreol.project.tasklist.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import qreol.project.tasklist.config.TestConfig;
import qreol.project.tasklist.domain.exception.ResourceNotFoundException;
import qreol.project.tasklist.domain.user.User;
import qreol.project.tasklist.repository.TaskRepository;
import qreol.project.tasklist.repository.UserRepository;

import java.util.Optional;

@SpringBootTest
@ActiveProfiles("junit")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
@EnableAutoConfiguration(exclude = {RedisAutoConfiguration.class,
        RedisRepositoriesAutoConfiguration.class})
public class UserServiceImplTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private TaskRepository taskRepository;

    @Autowired
    private UserServiceImpl userService;

    @Test
    void getByIdWithExistUser() {
        Long id = 1L;
        User user = new User();
        user.setId(id);

        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));
        User testUser = userService.getById(id);

        Mockito.verify(userRepository).findById(id);
        Assertions.assertEquals(user, testUser);
    }

    @Test
    void getByUsernameWithExistUser() {
        String username = "name";
        User user = new User();
        user.setUsername(username);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        User testUser = userService.getByUsername(username);

        Mockito.verify(userRepository).findByUsername(username);
        Assertions.assertEquals(user, testUser);
    }

    @Test
    void getByUsernameWithNoExistUser() {
        String username = "name";
        String exceptionMessage = "User not found";
        User user = new User();
        user.setUsername(username);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        ResourceNotFoundException e = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userService.getByUsername(username));

        Mockito.verify(userRepository).findByUsername(username);
        Assertions.assertEquals(e.getMessage(), exceptionMessage);
    }

    @Test
    void getByIdWithNoExistUser() {
        Long id = 1L;
        String exceptionMessage = "User not found";
        User user = new User();
        user.setId(id);

        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());
        ResourceNotFoundException e = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userService.getById(id));

        Mockito.verify(userRepository).findById(id);
        Assertions.assertEquals(e.getMessage(), exceptionMessage);
    }

    @Test
    void update() {
        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        String password = "password";
        user.setPassword(password);

        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(passwordEncoder.encode(password))
                .thenReturn("encodePassword");

        userService.update(user);
        Mockito.verify(passwordEncoder).encode(password);
        Mockito.verify(userRepository).save(user);
    }

    @Test
    void create() {
        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        String password = "password";

        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(passwordEncoder.encode(password))
                .thenReturn("encodePassword");
        User testUser = userService.create(user);

        Mockito.verify(userRepository).save(user);

        Assertions.assertNotNull(testUser.getRoles());
        Assertions.assertNotNull(testUser.getCreatedAt());
    }

    @Test
    void delete() {
        Long id = 1L;
        userService.delete(id);
        Mockito.verify(userRepository).deleteById(id);
    }

    @Test
    void isTaskOwner() {
        Long userId = 1L;
        Long taskId = 1L;

        Mockito.when(userRepository.isTaskOwner(userId, taskId)).thenReturn(true);
        boolean isOwner = userService.isTaskOwner(userId, taskId);

        Assertions.assertTrue(isOwner);
    }

}
