package com.shan.library.service.intf;

import com.shan.library.entity.file.File;

import java.io.InputStream;
import java.util.Set;

public interface IFileStorageService {

    void saveFile(File file, InputStream inputStream);

    String getFileUrl(String fileName);

    void deleteFile(String fileName);

    void deleteFiles(Set<String> fileNames);
}
