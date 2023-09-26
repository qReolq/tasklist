package qreol.project.tasklist.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import qreol.project.tasklist.domain.task.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;

    private String name;

    private String username;

    private String password;

    private Set<Role> roles = new HashSet<>();

    private List<Task> tasks = new ArrayList<>();

    private LocalDateTime createdAt;

}
