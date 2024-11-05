package com.tasc.hongquan.gomsuserver.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "Users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "user_id", nullable = false, columnDefinition = "CHAR(36)")
    private String userId = UUID.randomUUID().toString();

    @Size(max = 255, min = 6, message = "Email is not valid")
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Size(max = 255, min = 6, message = "Password is not valid")
    @Column(name = "password", nullable = false)
    private String password;

    @Size(max = 20)
    @Column(name = "phone_number", nullable = true, length = 20)
    private String phoneNumber;

    @Size(max = 255)
    @NotNull
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Lob
    @Column(name = "address")
    private String address;

    @Size(max = 255)
    @Column(name = "provider")
    private String provider;

    @Size(max = 255)
    @Column(name = "provider_id")
    private String providerId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    @NotNull
    @ColumnDefault("current_timestamp()")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @ColumnDefault("current_timestamp()")
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

}