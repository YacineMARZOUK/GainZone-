package org.example.gainzone.dto.request;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String username;
    private String email;
    private String password;
    private String name;
    private String lastName;
    private String phone;
    private String role;
}
