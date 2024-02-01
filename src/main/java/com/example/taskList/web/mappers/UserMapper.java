package com.example.taskList.web.mappers;

import com.example.taskList.domain.user.User;
import com.example.taskList.web.dto.user.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(UserDto dto);

}
