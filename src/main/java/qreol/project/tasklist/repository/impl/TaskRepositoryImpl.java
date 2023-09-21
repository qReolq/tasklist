package qreol.project.tasklist.repository.impl;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import qreol.project.tasklist.domain.exception.ResourceMappingException;
import qreol.project.tasklist.domain.task.Task;
import qreol.project.tasklist.domain.user.User;
import qreol.project.tasklist.repository.TaskRepository;
import qreol.project.tasklist.repository.mapper.row_mappers.TaskRowMapper;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

//@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

    private final JdbcTemplate jdbcTemplate;
    private final TaskRowMapper taskRowMapper;
    private final SimpleJdbcInsert simpleJdbcInsert;

    @PostConstruct
    public void init() {
        simpleJdbcInsert.withTableName("tasks").usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<Task> findById(Long id) {
        String FIND_BY_ID = """
                SELECT t.id as task_id,
                       t.title as task_title,
                       t.description as task_description,
                       t.status as task_status,
                       t.expiration_date as task_expiration_date,
                       t.created_at as task_created_at
                FROM tasks t
                WHERE t.id = ?
                """;


        Optional<Task> task = Optional.empty();
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(FIND_BY_ID, id);
            task = Optional.of(taskRowMapper.mapRow(rowSet));

        } catch (SQLException e) {
            throw new ResourceMappingException("Error while mapping task by id");
        } catch (NullPointerException e) {
            return task;
        }
        return task;
    }

    @Override
    public List<Task> findAllByUserId(Long userId) {
        String FIND_ALL_BY_USER_ID = """
                SELECT t.id as task_id,
                       t.title as task_title,
                       t.description as task_description,
                       t.status as task_status,
                       t.expiration_date as task_expiration_date,
                       t.created_at as task_created_at
                FROM tasks t
                WHERE t.user_id = ?
                """;

        List<Task> tasks = null;

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(FIND_ALL_BY_USER_ID, userId);
            tasks = taskRowMapper.mapRows(rowSet);

        } catch (SQLException | NullPointerException e) {
            throw new ResourceMappingException("Error while mapping task by id");
        }


        return tasks;
    }

    @Override
    public void assignToUserById(Long taskId, Long userId) {
        final String ASSIGN = """
                UPDATE tasks SET user_id = ? WHERE id = ?
                """;

        jdbcTemplate.update(ASSIGN, userId, taskId);
    }

    @Override
    public void update(Task task) {
        final String UPDATE = """
                UPDATE tasks
                SET title = ?,
                    description = ?,
                    status = ?,
                    expiration_date = ? where id = ?
                """;
        jdbcTemplate.update(UPDATE, task.getTitle(), task.getDescription(),
                task.getStatus().name(), task.getExpirationDate(), task.getId());
    }

    @Override
    public Long create(Task task) {
        final Map<String, Object> param = new HashMap<>();
        param.put("title", task.getTitle());
        param.put("description", task.getDescription());
        param.put("status", task.getStatus().name());

        if (task.getExpirationDate() != null)
            param.put("expiration_date", Timestamp.valueOf(task.getExpirationDate()));

        param.put("created_at", Timestamp.valueOf(task.getCreatedAt()));

        return simpleJdbcInsert.executeAndReturnKey(param).longValue();
    }

    @Override
    public void delete(Long id) {
        final String DELETE = """
                DELETE FROM tasks where id = ?
                """;

        jdbcTemplate.update(DELETE, id);
    }
}
