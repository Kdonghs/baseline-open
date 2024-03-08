package com.baseline.file;

import lombok.Data;

public class FileDto {
    private FileDto() {}

    @Data
    public static class Simple {
        private Integer fileId;
        private String originalFileName;
        private Integer memberId;
    }
}
