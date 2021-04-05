package ru.mechtatell.Models.DTO;

import lombok.Data;

@Data
public class AuthRequestDTO {
    private String login;
    private String password;
}
