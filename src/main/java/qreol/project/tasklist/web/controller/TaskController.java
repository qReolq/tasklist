package qreol.project.tasklist.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import qreol.project.tasklist.domain.task.Task;
import qreol.project.tasklist.repository.mapper.mappers.TaskMapper;
import qreol.project.tasklist.service.TaskService;
import qreol.project.tasklist.web.dto.task.TaskDTO;
import qreol.project.tasklist.web.validation.flags.OnUpdate;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Validated
@Tag(name = "Task", description = "Task API")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get TaskDTO by id")
    public HttpEntity<TaskDTO> getById(@PathVariable("id") Long id) {
        Task task = taskService.getById(id);

        return new HttpEntity<>(taskMapper.toDTO(task));
    }

    @PutMapping()
    @Operation(summary = "Update task")
    public HttpEntity<TaskDTO> update(@Validated(OnUpdate.class) @RequestBody TaskDTO taskDTO) {

        Task updatedTask = taskService.update(taskMapper.toEntity(taskDTO));

        return new HttpEntity<>(taskMapper.toDTO(updatedTask));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task by id")
    public void deleteById(@PathVariable("id") Long id) {
        taskService.delete(id);
    }


}
