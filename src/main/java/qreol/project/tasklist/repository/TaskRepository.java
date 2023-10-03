package qreol.project.tasklist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import qreol.project.tasklist.domain.task.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = """
            SELECT * FROM tasks t JOIN users u on u.id = t.user_id
            """, nativeQuery = true)
    List<Task> findAllByUserId(@Param("userId") Long userId);

}
