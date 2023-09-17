package qreol.project.tasklist.repository.mapper.row_mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import qreol.project.tasklist.domain.user.Role;
import qreol.project.tasklist.domain.user.User;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserRowMapper {

    private final TaskRowMapper taskRowMapper;

    public User mapRow(SqlRowSet rs) throws SQLException {
        while (rs.next()) {
            User user = new User();
            Set<Role> roles = new HashSet<>();

            user.setId(rs.getLong("user_id"));
            user.setName(rs.getString("user_name"));
            user.setUsername(rs.getString("user_username"));
            user.setPassword(rs.getString("user_password"));
            user.setCreatedAt(Objects.requireNonNull(rs.getTimestamp("user_created_at")).toLocalDateTime());

            rs.beforeFirst();
            while (rs.next()) {
                roles.add(Role.valueOf(rs.getString("user_role_role")));
            }

            user.setRoles(roles);

            rs.beforeFirst();

            user.setTasks(taskRowMapper.mapRows(rs));

            return user;
        }


        return null;
    }
}
