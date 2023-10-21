package qreol.project.tasklist.repository.mappers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import qreol.project.tasklist.domain.task.TaskImage;
import qreol.project.tasklist.web.dto.task.TaskImageDTO;

@Component
@RequiredArgsConstructor
public class TaskImageMapper {

    private final ModelMapper mapper;

    public TaskImageDTO toDTO(TaskImage taskImage) {
        return mapper.map(taskImage, TaskImageDTO.class);
    }

    public TaskImage toEntity(TaskImageDTO taskImageDTO) {
        return mapper.map(taskImageDTO, TaskImage.class);
    }

}
