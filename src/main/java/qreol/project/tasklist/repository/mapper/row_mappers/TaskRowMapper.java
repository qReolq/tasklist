package qreol.project.tasklist.repository.mapper.row_mappers;

import lombok.SneakyThrows;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import qreol.project.tasklist.domain.task.Status;
import qreol.project.tasklist.domain.task.Task;
import qreol.project.tasklist.domain.user.Role;
import qreol.project.tasklist.domain.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class TaskRowMapper {


    @SneakyThrows
    public Task mapRow(SqlRowSet rs) throws SQLException {
        Task task = new Task();

        while (rs.next()) {
            task.setId(rs.getLong("task_id"));
            task.setTitle(rs.getString("task_title"));
            task.setDescription(rs.getString("task_description"));
            task.setStatus(Status.valueOf(rs.getString("task_status")));
            task.setCreatedAt(Objects.requireNonNull(rs.getTimestamp("task_created_at")).toLocalDateTime());

            Timestamp expirationDate = rs.getTimestamp("task_expiration_date");

            if (expirationDate != null)
                task.setExpirationDate(expirationDate.toLocalDateTime());

            return task;
        }
        return null;
    }

    @SneakyThrows
    public List<Task> mapRows(SqlRowSet rs) throws SQLException {
        List<Task> tasks = new ArrayList<>();

        while (rs.next()) {
            Task task = new Task();

            task.setId(rs.getLong("task_id"));
            if (!rs.wasNull()) {
                task.setTitle(rs.getString("task_title"));
                task.setDescription(rs.getString("task_description"));
                task.setStatus(Status.valueOf(rs.getString("task_status")));
                task.setCreatedAt(Objects.requireNonNull(rs.getTimestamp("task_created_at")).toLocalDateTime());

                Timestamp expirationDate = rs.getTimestamp("task_expiration_date");

                if (expirationDate != null)
                    task.setExpirationDate(expirationDate.toLocalDateTime());

                tasks.add(task);
            }

        }
        return tasks;
    }

}
