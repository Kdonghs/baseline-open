package com.baseline.file;

import com.baseline.common.errorcode.FileErrorCode;
import com.baseline.common.exception.AlreadyExistException;
import com.baseline.common.exception.BadRequestException;
import com.baseline.common.exception.InternalServerException;
import com.baseline.entity.MemberEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class FileSystemStorageService implements StorageService {

    // Local FS root
    @Value("${spring.servlet.multipart.location}")
    private String rootPath;

    private final FileRepository fileRepository;

    @Override
    public int upload(MultipartFile multipartFile, @Nullable Path filepath, MemberEntity memberEntity) {
        // file validity check
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new BadRequestException(FileErrorCode.EMPTY_FILE);
        }

        String originalFileName = multipartFile.getOriginalFilename();

        // FS 저장
        // 파일명 해싱
        int lastIndex = (originalFileName == null) ? -1 : originalFileName.lastIndexOf(".");

        // 확장자
        String ext = (lastIndex == -1) ? "" : originalFileName.substring(lastIndex + 1);

        // UUID 생성
        String uuidFileName = UUID.randomUUID().toString();

        // 해싱된 파일명에 확장자를 붙임
        String hashedFileNameWithExt = (!ext.strip().isEmpty()) ? uuidFileName + "." + ext : uuidFileName;

        // 업로드할 최종 경로 생성
        Path uploadPath = (filepath != null) ?
                Paths.get(rootPath, filepath.toString()).resolve(hashedFileNameWithExt)
                : Paths.get(rootPath).resolve(hashedFileNameWithExt);

        // 최종 경로에 파일이 이미 존재하는지 확인
        File fileDst = new File(uploadPath.toUri());
        if (fileDst.isFile()) {
            throw new AlreadyExistException(FileErrorCode.ALREADY_EXIST);
        }

        // 저장
        try {
            //디렉토리 생성
            Files.createDirectories(uploadPath);

            //파일 저장
            multipartFile.transferTo(fileDst);
        } catch (IOException e) {
            log.error("Upload failed : " + e);
            throw new InternalServerException(FileErrorCode.UPLOAD_FAILED);
        }

        // DB 저장
        FileEntity fileEntity = new FileEntity();
        fileEntity.setOriginalFileName(originalFileName);
        fileEntity.setFileName(uploadPath.toString());
        fileEntity.setMember(memberEntity);
        fileEntity.setFileExt(ext.toLowerCase());

        fileRepository.save(fileEntity);

        return fileEntity.getId();
    }

    @Override
    public Resource loadAsResource(int fileId) {
        return null;
    }
}
