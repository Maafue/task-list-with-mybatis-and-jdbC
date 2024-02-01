package com.example.taskList.domain.user;

import com.example.taskList.domain.task.Task;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class User {

    private Long id;
    private String name;
    private String username;
    private String password;
    private String passwordConfirmation;

//    @Column(name = "role")
//    @ElementCollection(fetch = FetchType.EAGER)
//    @CollectionTable(name = "users_roles")
//    @Enumerated(value = EnumType.STRING)
    private Set<Role> roles;
    private List<Task> tasks;

}

