package com.tasc.hongquan.paymentservice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "email_retry_queue")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailRetryQueue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @Column(name = "email_to")
    private String emailTo;

    @Size(max = 255)
    @Column(name = "subject")
    private String subject;

    @Lob
    @Column(name = "body")
    private String body;

    @Size(max = 50)
    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

}