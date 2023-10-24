package qreol.project.tasklist.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import qreol.project.tasklist.config.TestConfig;
import qreol.project.tasklist.domain.exception.ResourceNotFoundException;
import qreol.project.tasklist.domain.task.Status;
import qreol.project.tasklist.domain.task.Task;
import qreol.project.tasklist.domain.task.TaskImage;
import qreol.project.tasklist.domain.user.User;
import qreol.project.tasklist.repository.TaskRepository;
import qreol.project.tasklist.service.ImageService;
import qreol.project.tasklist.service.TaskService;
import qreol.project.tasklist.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("junit")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    @MockBean
    private ImageService imageService;


    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @Test
    void getByIdWithNoExistUser() {
        Long id = 1L;
        String exceptionsMessage = "Task not found";

        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.empty());
        ResourceNotFoundException e = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> taskService.getById(id));

        Mockito.verify(taskRepository).findById(id);
        Assertions.assertEquals(e.getMessage(), exceptionsMessage);
    }

    @Test
    void getByIdWithExistUser() {
        Long id = 1L;
        Task task = new Task();
        task.setId(id);

        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.of(task));
        Task testTask = taskService.getById(id);
        Mockito.verify(taskRepository).findById(id);
        Assertions.assertEquals(task, testTask);
    }

    @Test
    void getAllByUserIdValidList() {
        Long id = 1L;
        List<Task> tasks = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            tasks.add(new Task());
        }

        Mockito.when(taskRepository.findAllByUserId(id)).thenReturn(tasks);
        List<Task> testTasks = taskService.getAllByUserId(id);

        Mockito.verify(taskRepository).findAllByUserId(id);
        Assertions.assertEquals(tasks.size(), testTasks.size());
    }

    @Test
    void create() {
        Long userId = 1L;
        Long taskId = 1L;
        User user = new User();
        Task task = new Task();
        user.setId(userId);
        task.setId(taskId);

        Mockito.when(userService.getById(userId)).thenReturn(user);
        taskService.create(task, userId);

        Assertions.assertNotNull(task.getStatus());
        Assertions.assertNotNull(task.getCreatedAt());
        Assertions.assertTrue(user.getTasks().contains(task));
    }

    @Test
    void updateWithStatus() {
        Long id = 1L;
        Task task = new Task();
        task.setId(id);
        task.setDescription("description");
        task.setStatus(Status.IN_PROGRESS);

        Mockito.when(taskRepository.save(task)).thenReturn(task);
        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.of(task));

        Task testTask = taskService.update(task);
        Mockito.verify(taskRepository).save(task);
        Assertions.assertEquals(task, testTask);
    }

    @Test
    void updateWithNullStatus() {
        Long id = 1L;
        Task task = new Task();
        task.setId(id);
        task.setDescription("description");
        task.setCreatedAt(LocalDateTime.now());

        Mockito.when(taskRepository.save(task)).thenReturn(task);
        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.of(task));

        Task testTask = taskService.update(task);
        Mockito.verify(taskRepository).save(task);
        Assertions.assertEquals(Status.TODO, testTask.getStatus());
    }

    @Test
    void delete() {
        Long id = 1L;
        taskService.delete(id);
        Mockito.verify(taskRepository).deleteById(id);
    }

    @Test
    void uploadImage() {
        Long id = 1L;
        String imageName = "imageName";
        TaskImage taskImage = new TaskImage();

        Mockito.when(imageService.upload(taskImage)).thenReturn(imageName);
        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.of(new Task()));

        taskService.uploadImage(id, taskImage);
        Mockito.verify(imageService).upload(taskImage);
    }

}
