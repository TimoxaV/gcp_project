package gcp.project.cloud.dto;

import lombok.Data;

@Data
public class StorageDto {
    private String bucketName;
    private String objectName;
}
