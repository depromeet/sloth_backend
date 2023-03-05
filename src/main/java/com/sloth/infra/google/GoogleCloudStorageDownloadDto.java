package com.sloth.infra.google;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GoogleCloudStorageDownloadDto {
    private String bucketName;
    private String downloadFileName;
    private String localFileLocation;
}

