package qreol.project.tasklist.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qreol.project.tasklist.domain.exception.ResourceNotFoundException;
import qreol.project.tasklist.domain.user.Role;
import qreol.project.tasklist.domain.user.User;
import qreol.project.tasklist.repository.UserRepository;
import qreol.project.tasklist.service.UserService;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Cacheable(value = "UserService::getById", key = "#id")
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Cacheable(value = "UserService::getByUsername", key = "#username")
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Transactional
    @Caching(put = {
            @CachePut(value = "UserService::getById", key = "#user.getId()"),
            @CachePut(value = "UserService::getByUsername", key = "#user.getUsername()")
    })
    public User update(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.update(user);
        return user;
    }

    @Override
    @Transactional
    @Caching(cacheable = {
            @Cacheable(value = "UserService::getById", key = "#user.getId()"),
            @Cacheable(value = "UserService::getByUsername", key = "#user.getUsername()")
    })
    public User create(User user) {
        enrichUser(user);

        Long userId = (userRepository.create(user));
        userRepository.insertUserRole(userId, Role.ROLE_USER);

        user.setId(userId);
        return user;
    }

    @Override
    @Transactional
    @CacheEvict(value = "UserService::getById", key = "#id")
    public void delete(Long id) {
        userRepository.delete(id);
    }

    @Override
    @Cacheable(value = "UserService::isTaskOwner", key = "#userId + '.' + #taskId")
    public boolean isTaskOwner(Long userId, Long taskId) {
        return userRepository.isTaskOwner(userId, taskId);
    }

    private void enrichUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(Role.ROLE_USER));
        user.setCreatedAt(LocalDateTime.now());
    }

}
