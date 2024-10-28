package com.tasc.hongquan.gomsuserver.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailCustom {
    private UUID id;
    private String email;
    private String phoneNumber;
    private String fullName;
    private String address;
    private String provider;
    private String providerId;
}
