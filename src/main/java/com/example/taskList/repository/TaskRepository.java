package com.example.taskList.repository;

import com.example.taskList.domain.task.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    Optional<Task> findById(Long id);

    List<Task> findAllByUserId(Long userID);

    void assignToUserById(Long taskId, Long userId);

    void update (Task task);

    void create (Task task);

    void delete (Long Id);

}
