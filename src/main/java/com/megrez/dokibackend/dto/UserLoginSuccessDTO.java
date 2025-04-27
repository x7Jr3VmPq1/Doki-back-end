package com.megrez.dokibackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
@AllArgsConstructor
public class UserLoginSuccessDTO {
    private Integer id;
    private String token;
}
