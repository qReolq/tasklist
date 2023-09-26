package qreol.project.tasklist.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import qreol.project.tasklist.domain.user.Role;
import qreol.project.tasklist.domain.user.User;

import java.util.Optional;

@Mapper
public interface UserRepository {

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String email);

    void update(User user);

    @Select(" INSERT INTO users (name, username, password, created_at)\n" +
            " values (#{name},#{username},#{password},#{createdAt}) RETURNING id")
    @Options(flushCache = Options.FlushCachePolicy.TRUE, useGeneratedKeys = true, keyProperty = "id", keyColumn="id")
    Long create(User user);

    void delete(Long id);

    void insertUserRole(@Param("userId") Long userId, @Param("role") Role role);

    boolean isTaskOwner(@Param("userId") Long userId, @Param("taskId") Long taskId);

}
