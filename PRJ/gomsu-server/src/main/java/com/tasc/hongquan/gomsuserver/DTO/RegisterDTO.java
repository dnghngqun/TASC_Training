package com.tasc.hongquan.gomsuserver.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterDTO {
    private String email;
    private String password;
    private String fullName;
    private String phone_number;

}
