package com.tasc.hongquan.gomsuserver.DTO;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForgotResponse {
    private String name;
    private String phoneNumber;
    private String userId;

}
