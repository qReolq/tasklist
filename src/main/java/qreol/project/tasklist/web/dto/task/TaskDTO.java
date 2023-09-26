package qreol.project.tasklist.web.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import qreol.project.tasklist.domain.task.Status;
import qreol.project.tasklist.web.validation.flags.OnCreate;
import qreol.project.tasklist.web.validation.flags.OnUpdate;

import java.time.LocalDateTime;

@Data
@Schema(description = "Task DTO")
public class TaskDTO {

    @Schema(description = "Task id", example = "1")
    @NotNull(message = "Id must be not null", groups = OnUpdate.class)
    private Long id;

    @Schema(description = "Task title", example = "Do homework")
    @NotNull(message = "Title must be not null", groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255, message = "Title length must be smaller than 255 symbols", groups = {OnCreate.class, OnUpdate.class})
    private String title;

    @Schema(description = "Task description", example = "Math, Physics, Literature")
    @Length(max = 255, message = "Title description length must be smaller than 255 symbols", groups = {OnCreate.class, OnUpdate.class})
    private String description;

    @Schema(description = "Task status", example = "TODO")
    private Status status;

    @Schema(description = "Task expiration date", example = "2023-04-31 00:00")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime expirationDate;

    @Schema(description = "Task created at", example = "2023-04-31 00:00")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

}
