package qreol.project.tasklist.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import qreol.project.tasklist.domain.task.Task;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TaskRepository {

    Optional<Task> findById(Long id);

    List<Task> findAllByUserId(Long userId);

    void assignToUserById(@Param("taskId") Long taskId, @Param("userId") Long userId);

    void update(Task task);

    @Select(" INSERT INTO tasks (title, description, status, expiration_date, created_at)\n" +
            " values (#{title},#{description},#{status},#{expirationDate},#{createdAt}) RETURNING id")
    @Options(flushCache = Options.FlushCachePolicy.TRUE, useGeneratedKeys = true, keyProperty = "id", keyColumn="id")
    Long create(Task task);

    void delete(Long id);

}
