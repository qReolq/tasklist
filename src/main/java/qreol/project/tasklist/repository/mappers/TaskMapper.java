package qreol.project.tasklist.repository.mappers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import qreol.project.tasklist.domain.task.Task;
import qreol.project.tasklist.web.dto.task.TaskDTO;

@Component
@RequiredArgsConstructor
public class TaskMapper {

    private final ModelMapper mapper;

    public TaskDTO toDTO(Task task) {
        return mapper.map(task, TaskDTO.class);
    }

    public Task toEntity(TaskDTO taskDTO) {
        return mapper.map(taskDTO, Task.class);
    }

}
