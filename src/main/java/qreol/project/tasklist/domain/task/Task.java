package qreol.project.tasklist.domain.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import qreol.project.tasklist.domain.user.User;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task implements Serializable {

    private Long id;

    private String title;

    private String description;

    private Status status;

    private LocalDateTime expirationDate;

    private LocalDateTime createdAt;


}
