package qreol.project.tasklist.repository;

import qreol.project.tasklist.domain.user.Role;
import qreol.project.tasklist.domain.user.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    void update(User user);

    Long create(User user);

    void delete(Long id);

    void insertUserRole(Long userId, Role role);

    boolean isTaskOwner(Long userId, Long taskId);

}
