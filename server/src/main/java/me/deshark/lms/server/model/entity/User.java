package me.deshark.lms.server.model.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class User {

    private Long id;

    @NotNull
    @Length(min = 2, max = 16)
    private String username;

    @NotNull
    @Length(min = 6, max = 32)
    private String password;

    @NotNull
    private String role;
}
