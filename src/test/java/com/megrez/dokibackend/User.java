package com.megrez.dokibackend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String address;
    private String email;
    private String info;
    private name name;
}

@AllArgsConstructor
@NoArgsConstructor
@Data
class name {
    private String fn;
    private String ln;
}
