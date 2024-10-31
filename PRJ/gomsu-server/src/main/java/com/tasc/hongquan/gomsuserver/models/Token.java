package com.tasc.hongquan.gomsuserver.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Builder
@Table(name = "Tokens")
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    @Id
    @Column(name = "token_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Lob
    @Column(name = "token")
    private String token;

    @NotNull
    @Lob
    @Column(name = "token_type", nullable = false)
    private String tokenType;

    @Size(max = 255)
    @Column(name = "provider")
    private String provider;

    @NotNull
    @ColumnDefault("current_timestamp()")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @ColumnDefault("'0000-00-00 00:00:00'")
    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @ColumnDefault("0")
    @Column(name = "is_revoked")
    private Boolean isRevoked;


}