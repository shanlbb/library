package com.shan.library.dto;

import com.shan.library.entity.file.File;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class FileDTO {
    private UUID id;

    private String originalName;

    private String type;

    private Long size;

    private LocalDateTime createdAt;

    private String fileUrl;

    public FileDTO(File file, String fileUrl) {
        this.id = file.getId();
        this.originalName = file.getOriginalName();
        this.type = file.getType();
        this.size = file.getSize();
        this.createdAt = file.getCreatedAt();
        this.fileUrl = fileUrl;
    }
}
