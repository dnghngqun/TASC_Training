package com.tasc.hongquan.gomsuserver.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "Comments")
public class Comment {
    @Id
    @Column(name = "comment_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Size(max = 255)
    @Column(name = "email")
    private String email;

    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "content")
    private String content;

    @NotNull
    @ColumnDefault("current_timestamp()")
    @Column(name = "time_comment", nullable = false)
    private Instant timeComment;

    @NotNull
    @ColumnDefault("current_timestamp()")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

}