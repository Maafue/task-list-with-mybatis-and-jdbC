package com.example.taskList.repository.impl;

import com.example.taskList.domain.exception.ResourceMappingException;
import com.example.taskList.domain.task.Task;
import com.example.taskList.repository.DataSourceConfig;
import com.example.taskList.repository.TaskRepository;
import com.example.taskList.repository.mappers.TaskRowMaper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

    private final DataSourceConfig dataSourceConfig;

    private final String FIND_BY_ID = """
            select t.id as task_id,
                t.title as task_title,
                t.description as task_description,
                t.expiration_date as task_expiration_date,
                t.status as task_status
            from tasks t
            where id = :id""";

    private final String FIND_ALL_BY_USER_ID = """
            select t.id as task_id,
                t.title as task_title,
                t.description as task_description,
                t.expiration_date as task_expiration_date,
                t.status as task_status
            from tasks t
                join users_tasks ut on t.id = ut.task_id
            where ut.user_id = :id""";


    private final String ASSIGN = """
            insert into users_tasks (task_id, user_id)
            values (:task_id, :user_id)""";

    private final String UPDATE = """
            update tasks
            set title = :title,
                description = :description,
                expiration_date = :expiration_date,
                status = :status
            where id = :id
            """;

    private final String CREATE = """
            insert into tasks (title, description, expiration_date, status)
            values (:title, :description, :expiration_date, :status)
            """;

    private final String DELETE = """
            DELETE FROM tasks
            where id = :id""";

    @Override
    public Optional<Task> findById(Long id) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_BY_ID);
            statement.setLong(1, id);

            try (ResultSet rs = statement.executeQuery()){
                return Optional.ofNullable(TaskRowMaper.mapRow(rs));
            }

        } catch (SQLException throwables) {
            throw new ResourceMappingException("Error while finding user by id.");
        }
    }

    @Override
    public List<Task> findAllByUserId(Long userID) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_ALL_BY_USER_ID);
            statement.setLong(1, userID);

            try (ResultSet rs = statement.executeQuery()){
                return TaskRowMaper.mapRows(rs);
            }

        } catch (SQLException throwables) {
            throw new ResourceMappingException("Error while finding all by user id.");
        }
    }

    @Override
    public void assignToUserById(Long taskId, Long userId) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(ASSIGN);
            statement.setLong(1, taskId);
            statement.setLong(2, userId);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Error while assign to user.");
        }
    }

    @Override
    public void update(Task task) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setString(1, task.getTitle());

            if (task.getDescription() == null) {
                statement.setNull(2, Types.VARCHAR);
            } else {
                statement.setString(2, task.getDescription());
            }

            if (task.getExpirationDate() == null) {
                statement.setNull(3, Types.TIMESTAMP);
            } else {
                statement.setTimestamp(3, Timestamp.valueOf(task.getExpirationDate()));
            }
            statement.setString(4, task.getStatus().name());
            statement.setLong(5, task.getId());

            statement.executeUpdate();

        } catch (SQLException throwables) {
            throw new ResourceMappingException("Error while updating task.");
        }

    }

    @Override
    public void create(Task task) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, task.getTitle());

            if (task.getDescription() == null) {
                statement.setNull(2, Types.VARCHAR);
            } else {
                statement.setString(2, task.getDescription());
            }

            if (task.getExpirationDate() == null) {
                statement.setNull(3, Types.TIMESTAMP);
            } else {
                statement.setTimestamp(3, Timestamp.valueOf(task.getExpirationDate()));
            }
            statement.setString(4, task.getStatus().name());
            statement.executeUpdate();

            try (ResultSet rs = statement.getResultSet()) {
                rs.next();
                task.setId(rs.getLong(1));
            }
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Error while creating task.");
        }
    }

    @Override
    public void delete(Long id) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE);
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Error while deleting task.");
        }
    }
}
