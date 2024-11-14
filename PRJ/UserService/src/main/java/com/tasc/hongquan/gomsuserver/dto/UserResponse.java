package com.tasc.hongquan.gomsuserver.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private String id;
    private String email;
    private String phone_number;
    private String full_name;
    private String address;
}
