package qreol.project.tasklist.domain.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import qreol.project.tasklist.domain.user.User;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    private Long id;

    private String title;

    private String description;

    private Status status;

    private LocalDateTime expirationDate;

    private LocalDateTime createdAt;


}
