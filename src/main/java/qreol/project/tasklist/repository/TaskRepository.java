package qreol.project.tasklist.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import qreol.project.tasklist.domain.task.Task;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    Optional<Task> findById(Long id);

    List<Task> findAllByUserId(Long userId);

    void assignToUserById(Long taskId, Long userId);

    void update(Task task);

    Long create(Task task);

    void delete(Long id);

}
