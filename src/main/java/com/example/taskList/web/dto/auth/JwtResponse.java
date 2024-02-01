package com.example.taskList.web.dto.auth;

import lombok.Data;

@Data
public class JwtResponse {

    private Long id;
    private String Username;
    private String  accessToken;
    private String refreshToken;

}
