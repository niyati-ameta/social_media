package com.intuit.socialmedia.posts.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.intuit.socialmedia.posts.constant.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "post")
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id; //UUID.randomUUID gives version 4

    @NotNull
    @Size(max = 1000)
    private String description;
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> mediaPaths;

    @Column(columnDefinition = "timestamptz")
    @CreationTimestamp
    private OffsetDateTime createdOn;

    @Column(columnDefinition = "timestamptz")
    @UpdateTimestamp
    private OffsetDateTime updatedOn;

    private String createdBy; // userId
    @Enumerated(EnumType.ORDINAL)
    private Status status;
}
