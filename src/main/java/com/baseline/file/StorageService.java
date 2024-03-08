package com.baseline.file;

import com.baseline.entity.MemberEntity;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import java.nio.file.Path;

public interface StorageService {
    int upload(MultipartFile file, @Nullable Path filepath, MemberEntity memberEntity);
    Resource loadAsResource(int fileId);
}
