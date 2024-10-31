package com.tasc.hongquan.gomsuserver.cookie;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginCookie {
    private String uuid;
    private String role;
    private String jwt;
}
