package com.tasc.hongquan.gomsuserver.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterDTO {
    @NotNull(message = "Email is required")
    @Size(min = 6, max = 100, message = "Email must be between 6 and 100 characters")
    private String email;
    @NotNull(message = "Password is required")
    private String password;
    @NotNull(message = "Full name is required")
    private String fullName;
    @NotNull(message = "Phone number is required")
    private String phone_number;

}
