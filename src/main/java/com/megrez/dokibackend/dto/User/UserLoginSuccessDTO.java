package com.megrez.dokibackend.dto.User;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserLoginSuccessDTO {
    private Integer id;
    private String token;
}
