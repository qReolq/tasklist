package qreol.project.tasklist.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import qreol.project.tasklist.domain.task.Task;
import qreol.project.tasklist.domain.user.User;
import qreol.project.tasklist.repository.mapper.mappers.TaskMapper;
import qreol.project.tasklist.repository.mapper.mappers.UserMapper;
import qreol.project.tasklist.service.TaskService;
import qreol.project.tasklist.service.UserService;
import qreol.project.tasklist.web.dto.task.TaskDTO;
import qreol.project.tasklist.web.dto.user.UserDTO;
import qreol.project.tasklist.web.validation.flags.OnCreate;
import qreol.project.tasklist.web.validation.flags.OnUpdate;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;
    private final TaskService taskService;

    private final UserMapper userMapper;
    private final TaskMapper taskMapper;


    @PutMapping
    public HttpEntity<UserDTO> update(@Validated(OnUpdate.class) @RequestBody UserDTO dto) {
        User updatedUser = userService.update(userMapper.toEntity(dto));

        return new HttpEntity<>(userMapper.toDTO(updatedUser));
    }

    @GetMapping("/{id}")
    public HttpEntity<UserDTO> getById(@PathVariable Long id) {
        User user = userService.getById(id);

        return new HttpEntity<>(userMapper.toDTO(user));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        userService.delete(id);
    }


    @GetMapping("/{id}/tasks")
    public HttpEntity<List<TaskDTO>> getTasksByUserId(@PathVariable("id") Long id) {
        List<TaskDTO> tasks = taskService.getAllByUserId(id)
                .stream().map(taskMapper::toDTO).toList();

        return new HttpEntity<>(tasks);
    }

    @PostMapping("/{id}/tasks")
    public HttpEntity<TaskDTO> createTask(@PathVariable("id") Long id,
                                          @Validated(OnCreate.class) @RequestBody TaskDTO dto) {

        Task task = taskService.create(taskMapper.toEntity(dto), id);

        return new HttpEntity<>(taskMapper.toDTO(task));
    }

}
