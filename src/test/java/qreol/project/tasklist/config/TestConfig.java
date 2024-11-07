package qreol.project.tasklist.config;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import qreol.project.tasklist.repository.TaskRepository;
import qreol.project.tasklist.repository.UserRepository;
import qreol.project.tasklist.service.AuthService;
import qreol.project.tasklist.service.ImageService;
import qreol.project.tasklist.service.UserService;
import qreol.project.tasklist.service.impl.AuthServiceImpl;
import qreol.project.tasklist.service.impl.TaskImageServiceImpl;
import qreol.project.tasklist.service.impl.TaskServiceImpl;
import qreol.project.tasklist.service.impl.UserServiceImpl;
import qreol.project.tasklist.service.props.JwtProperties;
import qreol.project.tasklist.service.props.MinioProperties;
import qreol.project.tasklist.web.security.JwtTokenProvider;
import qreol.project.tasklist.web.security.JwtUserDetailsService;

@TestConfiguration
@RequiredArgsConstructor
@ActiveProfiles("junit")
public class TestConfig {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final AuthenticationManager authenticationManager;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Bean
    @Primary
    public JwtProperties jwtProperties() {
        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setSecret("YXNkYXNkU0FEc2FkamRmZ3Nsa0pBRExLSkFTRGxuZmdua");
        return jwtProperties;
    }

    @Bean
    @Primary
    public JwtUserDetailsService userDetailsService() {
        return new JwtUserDetailsService(userService());
    }

    @Bean
    public MinioClient minioClient() {
        return Mockito.mock(MinioClient.class);
    }

    @Bean
    public MinioProperties minioProperties() {
        MinioProperties minioProperties = new MinioProperties();
        minioProperties.setBucket("images");
        return minioProperties;
    }

    @Bean
    @Primary
    public ImageService imageService() {
        return new TaskImageServiceImpl(minioClient(), minioProperties());
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(jwtProperties(), userService(), userDetailsService());
    }

    @Bean
    @Primary
    public UserService userService() {
        return new UserServiceImpl(userRepository, passwordEncoder);
    }
    @Bean
    @Primary
    public TaskServiceImpl taskService() {
        return new TaskServiceImpl(imageService(), taskRepository, userService());
    }

    @Bean
    @Primary
    public AuthService authService() {
        return new AuthServiceImpl(authenticationManager, userService(), jwtTokenProvider());
    }

}
