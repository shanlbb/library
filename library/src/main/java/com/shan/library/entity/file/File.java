package com.shan.library.entity.file;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "files")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class File {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "original_name", nullable = false)
    private String originalName;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "size", nullable = false)
    private Long size;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public File() {}

    public File(String originalName, String type, Long size, LocalDateTime createdAt) {
        this.originalName = originalName;
        this.type = type;
        this.size = size;
        this.createdAt = createdAt;
    }
}
