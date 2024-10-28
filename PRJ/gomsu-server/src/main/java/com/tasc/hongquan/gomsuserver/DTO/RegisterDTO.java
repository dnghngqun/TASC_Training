package com.tasc.hongquan.gomsuserver.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {
    private String email;
    private String password;
    private String name;
    private String phone_number;

}
