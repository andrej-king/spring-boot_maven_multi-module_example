package com.example.persistence.model.common;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public class BaseModel implements Serializable {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    protected Long id = null;

    @Setter(AccessLevel.NONE)
    @Column(nullable = false, updatable = false, unique = true)
    @ColumnDefault("gen_random_uuid()")
    protected UUID uuid = UUID.randomUUID();

    @CreatedDate
    @Setter(AccessLevel.NONE)
    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @ColumnDefault("now()")
    protected Instant createdAt = Instant.now();

//    @LastModifiedDate
    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @ColumnDefault("now()")
    protected Instant updatedAt = Instant.now();
}
