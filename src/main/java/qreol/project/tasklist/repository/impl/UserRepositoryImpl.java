package qreol.project.tasklist.repository.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import qreol.project.tasklist.domain.exception.ResourceMappingException;
import qreol.project.tasklist.domain.user.Role;
import qreol.project.tasklist.domain.user.User;
import qreol.project.tasklist.repository.UserRepository;
import qreol.project.tasklist.repository.mapper.row_mappers.UserRowMapper;

import javax.sql.DataSource;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

//@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;
    private final SimpleJdbcInsert simpleJdbcInsert;

    @PostConstruct
    public void init() {
        simpleJdbcInsert.withTableName("users").usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<User> findById(Long id) {
        final String FIND_BY_ID = """
                SELECT
                    u.id as user_id,
                    u.name as user_name,
                    u.username as user_username,
                    u.password as user_password,
                    u.created_at as user_created_at,
                    ur.role user_role_role,
                    t.id as task_id,
                    t.title as task_title,
                    t.description as task_description,
                    t.status as task_status,
                    t.expiration_date as task_expiration_date,
                    t.created_at as task_created_at
                FROM users u
                         LEFT JOIN tasks t on u.id = t.user_id
                         LEFT JOIN users_roles ur on u.id = ur.user_id
                WHERE u.id = ?
                """;

        Optional<User> user = Optional.empty();
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(FIND_BY_ID, id);
            user = Optional.ofNullable(userRowMapper.mapRow(rowSet));
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while mapping user by id");
        } catch (NullPointerException e) {
            return user;
        }

        return user;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        final String FIND_BY_USERNAME = """
                SELECT
                    u.id as user_id,
                    u.name as user_name,
                    u.username as user_username,
                    u.password as user_password,
                    u.created_at as user_created_at,
                    ur.role user_role_role,
                    t.id as task_id,
                    t.title as task_title,
                    t.description as task_description,
                    t.status as task_status,
                    t.expiration_date as task_expiration_date,
                    t.created_at as task_created_at
                FROM users u
                         LEFT JOIN tasks t on u.id = t.user_id
                         LEFT JOIN users_roles ur on u.id = ur.user_id
                WHERE u.username = ?
                """;

        Optional<User> user = Optional.empty();
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(FIND_BY_USERNAME, username);
            user = Optional.ofNullable(userRowMapper.mapRow(rowSet));
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while mapping user by username");
        } catch (NullPointerException e) {
            return user;
        }

        return user;
    }

    @Override
    public void update(User user) {
        final String UPDATE = """
                UPDATE users
                SET name = ?,
                    username = ?,
                    password = ?
                WHERE id = ?
                """;

        jdbcTemplate.update(UPDATE, user.getName(), user.getUsername(), user.getPassword(), user.getId());
    }

    @Override
    public Long create(User user) {
        final Map<String, Object> param = new HashMap<>();
        param.put("name", user.getName());
        param.put("username", user.getUsername());
        param.put("password", user.getPassword());
        param.put("created_at", Timestamp.valueOf(user.getCreatedAt()));

        return simpleJdbcInsert.executeAndReturnKey(param).longValue();
    }

    @Override
    public void delete(Long id) {
        final String DELETE = """
                DELETE FROM users where id = ?
                """;
        jdbcTemplate.update(DELETE, id);
    }

    @Override
    public void insertUserRole(Long userId, Role role) {
        final String INSERT_USER_ROLE = """
                INSERT INTO users_roles(user_id, role)
                values (?, ?)
                """;
        jdbcTemplate.update(INSERT_USER_ROLE, userId, role.name());
    }

    @Override
    public boolean isTaskOwner(Long userId, Long taskId) {
        final String IS_TASK_OWNER = """
                SELECT exists(
                               SELECT 1
                               FROM tasks
                               WHERE user_id = ?
                                 AND tasks.id = ?
                           )
                """;

        return Boolean.TRUE.equals(jdbcTemplate.query(IS_TASK_OWNER,
                (ResultSet rs) -> rs.getBoolean(1), userId, taskId));
    }
}
