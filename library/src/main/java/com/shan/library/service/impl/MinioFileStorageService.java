package com.shan.library.service.impl;

import com.shan.library.entity.file.File;
import com.shan.library.service.intf.IFileStorageService;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.RemoveObjectsArgs;
import io.minio.Result;
import io.minio.http.Method;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MinioFileStorageService implements IFileStorageService {

    private final MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String bucketName;

    public MinioFileStorageService(@Value("${minio.url}") String url,
                                   @Value("${minio.access.key}") String accessKey,
                                   @Value("${minio.secret.key}") String secretKey) {
        this.minioClient = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }

    @Override
    @SneakyThrows
    public void saveFile(File file, InputStream inputStream) {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(file.getId().toString())
                        .stream(inputStream, file.getSize(), -1)
                        .contentType(file.getType())
                        .build()
        );
    }

    @Override
    @SneakyThrows
    public String getFileUrl(String fileName) {
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .expiry(1, TimeUnit.HOURS)
                        .method(Method.GET)
                        .build()
        );
    }

    @Override
    @SneakyThrows
    public void deleteFile(String fileName) {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .build()
        );
    }

    @Override
    @SneakyThrows
    public void deleteFiles(Set<String> fileNames) {
        Iterable<Result<DeleteError>> results =  minioClient.removeObjects(
                RemoveObjectsArgs.builder()
                        .bucket(bucketName)
                        .objects(fileNames.stream()
                                .map(DeleteObject::new)
                                .collect(Collectors.toList()))
                        .build()
        );
        for (Result<DeleteError> result : results) {
            DeleteError deleteError = result.get();
            log.error("Error in deleting object: {}, {}", deleteError.objectName(), deleteError.message());
        }
    }
}
