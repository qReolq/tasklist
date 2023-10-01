package qreol.project.tasklist.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qreol.project.tasklist.domain.exception.ResourceNotFoundException;
import qreol.project.tasklist.domain.task.Status;
import qreol.project.tasklist.domain.task.Task;
import qreol.project.tasklist.repository.TaskRepository;
import qreol.project.tasklist.service.TaskService;
import qreol.project.tasklist.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    @Cacheable(value = "TaskService::getById", key = "#id")
    public Task getById(Long id) {
        return taskRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    @Override
    public List<Task> getAllByUserId(Long id) {
        return taskRepository.findAllByUserId(id);
    }

    @Override
    @Transactional
    @CachePut(value = "TaskService::getById", key = "#task.getId()")
    public Task update(Task task) {
        if (task.getStatus() == null) {
            task.setStatus(Status.TODO);
        }
        taskRepository.update(task);

        return task;
    }

    @Override
    @Transactional
    @Cacheable(value = "TaskService::getById", key = "#task.getId()")
    public Task create(Task task, Long userId) {
        enrichTask(task);

        Long taskId = taskRepository.create(task);
        taskRepository.assignToUserById(taskId, userId);

        task.setId(taskId);
        return task;
    }

    @Override
    @Transactional
    @CacheEvict(value = "TaskService::getById", key = "#id")
    public void delete(Long id) {
        taskRepository.delete(id);
    }

    private void enrichTask(Task task) {
        task.setStatus(Status.TODO);
        task.setCreatedAt(LocalDateTime.now());
    }

}
