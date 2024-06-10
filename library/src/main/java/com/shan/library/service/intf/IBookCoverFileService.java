package com.shan.library.service.intf;

import com.shan.library.dto.FileDTO;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface IBookCoverFileService {

    void upload(@NonNull MultipartFile multipartFile, @NonNull UUID bookId, @NonNull UUID userId);

    FileDTO getByBookId(@NonNull UUID bookId);

    void deleteById(@NonNull UUID id, @NonNull UUID userId);

    void deleteByBookId(@NonNull UUID bookId, @NonNull UUID userId);
}
