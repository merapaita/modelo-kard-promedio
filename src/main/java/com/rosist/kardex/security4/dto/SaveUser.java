package com.rosist.kardex.security4.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SaveUser implements Serializable {

    @Size(min = 4)
    private String name;
    @Size(min = 4)
    private String username;
    @Size(min = 8)
    private String password;
    @Size(min = 8, message = "clave repetida deve ser como minimo 8 digitos")
    private String repeatedPassword;
}
