package com.tasc.hongquan.gomsuserver.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
