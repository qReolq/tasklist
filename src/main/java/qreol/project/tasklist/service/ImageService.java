package qreol.project.tasklist.service;

import qreol.project.tasklist.domain.task.TaskImage;

public interface ImageService {
    String upload(TaskImage image);
}
