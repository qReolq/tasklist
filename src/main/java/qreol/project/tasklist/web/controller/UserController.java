package qreol.project.tasklist.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.validation.BindingResult;
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
import qreol.project.tasklist.web.validation.validators.UserValidator;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
@Tag(name = "User", description = "User API")
public class UserController {

    private final UserService userService;
    private final TaskService taskService;

    private final UserMapper userMapper;
    private final TaskMapper taskMapper;
    private final UserValidator userValidator;

    @PutMapping
    @Operation(summary = "Update user")
    public HttpEntity<UserDTO> update(@Validated(OnUpdate.class) @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        userValidator.validateUpdate(userDTO, bindingResult);

        User updatedUser = userService.update(userMapper.toEntity(userDTO));

        return new HttpEntity<>(userMapper.toDTO(updatedUser));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get UserDTO by id")
    public HttpEntity<UserDTO> getById(@PathVariable Long id) {
        User user = userService.getById(id);

        return new HttpEntity<>(userMapper.toDTO(user));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by id")
    public void deleteById(@PathVariable Long id) {
        userService.delete(id);
    }


    @GetMapping("/{id}/tasks")
    @Operation(summary = "Get all user tasks by id")
    public HttpEntity<List<TaskDTO>> getTasksByUserId(@PathVariable("id") Long id) {
        List<TaskDTO> tasks = taskService.getAllByUserId(id)
                .stream().map(taskMapper::toDTO).toList();

        return new HttpEntity<>(tasks);
    }

    @PostMapping("/{id}/tasks")
    @Operation(summary = "Add task to user")
    public HttpEntity<TaskDTO> createTask(@PathVariable("id") Long id,
                                          @Validated(OnCreate.class) @RequestBody TaskDTO taskDTO) {

        Task task = taskService.create(taskMapper.toEntity(taskDTO), id);

        return new HttpEntity<>(taskMapper.toDTO(task));
    }

}
