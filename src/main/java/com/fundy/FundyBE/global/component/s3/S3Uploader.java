package com.fundy.FundyBE.global.component.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fundy.FundyBE.global.exception.customException.S3UploadException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class S3Uploader {
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadProfileImage(MultipartFile multipartFile)  {
        try {
            return uploadImage(multipartFile, UploadDirectory.PROFILE_IMAGE);
        } catch (IOException e) {
            throw S3UploadException.withCustomMessage("업로드에 실패하였습니다");
        }
    }

    private String uploadImage(MultipartFile multipartFile, UploadDirectory uploadDirectory) throws IOException {
        validateIsImage(multipartFile);

        String path = generatePath(multipartFile, uploadDirectory);

        ObjectMetadata metadata = getObjectMetadata(multipartFile);

        amazonS3Client.putObject(new PutObjectRequest(bucket, path, multipartFile.getInputStream(), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, path).toString();
    }

    private void validateIsImage(MultipartFile multipartFile) {
        String[] parts = multipartFile.getContentType().split("/");
        String mainType = parts[0];

        if (!mainType.equals("image")) {
            throw S3UploadException.withCustomMessage("이미지가 아닙니다");
        }
    }

    private String generatePath(MultipartFile multipartFile, UploadDirectory uploadDirectory) {
        Date now = new Date();
        return uploadDirectory.getValue() + now.getTime() + multipartFile.getOriginalFilename();
    }

    private ObjectMetadata getObjectMetadata(MultipartFile multipartFile) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(multipartFile.getContentType());
        metadata.setContentLength(multipartFile.getSize());
        return metadata;
    }

    private enum UploadDirectory {
        PROFILE_IMAGE("user/profile/");
        private String value;
        UploadDirectory(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
    }
}
