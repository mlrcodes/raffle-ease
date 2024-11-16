package com.raffleease.raffles_service.S3.Services.Impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.raffleease.common_models.Exceptions.CustomExceptions.CustomIOException;
import com.raffleease.common_models.Exceptions.CustomExceptions.S3HandlingException;
import com.raffleease.raffles_service.S3.Services.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.amazonaws.HttpMethod.GET;
import static com.amazonaws.HttpMethod.PUT;

@RequiredArgsConstructor
@Service
public class S3ServiceImpl implements S3Service {
    private final AmazonS3 s3Client;

    @Value("${raffle_ease_bucket}")
    private String bucketName;

    public List<String> generateUploadUrls(List<String> fileNames) {
        return generateSignedUrls(fileNames, PUT);
    }

    public List<String> generateViewUrls(List<String> fileNames) {
        return generateSignedUrls(fileNames, GET);
    }

    private List<String> generateSignedUrls(List<String> fileNames, HttpMethod method) {
        List<String> urls = new ArrayList<>();
        for (String name : fileNames) {
            try {
                GeneratePresignedUrlRequest request =
                        new GeneratePresignedUrlRequest(bucketName, name)
                                .withMethod(method)
                                .withExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000));
                URL url = s3Client.generatePresignedUrl(request);
                urls.add(url.toString());
            } catch (AmazonServiceException e) {
                throw new S3HandlingException("Failed to generate presigned URL for key: " + name);
            }
        }
        return urls;
    }

    @Override
    public void delete(String file) {
        try {
            s3Client.deleteObject(bucketName, file);
        } catch (SdkClientException e) {
            throw new S3HandlingException("Error while deleting S3 object: " + e.getMessage());
        }
    }

    @Override
    public void delete(List<String> fileKeys) {
        try {
            List<DeleteObjectsRequest.KeyVersion> keys = fileKeys.stream()
                    .map(DeleteObjectsRequest.KeyVersion::new)
                    .collect(Collectors.toList());

            DeleteObjectsRequest deleteRequest = new DeleteObjectsRequest(bucketName).withKeys(keys);

            DeleteObjectsResult result = s3Client.deleteObjects(deleteRequest);
        } catch (MultiObjectDeleteException e) {
            e.getErrors().forEach(error ->
                    System.err.println("Failed to delete object: " + error.getKey() + " - " + error.getMessage())
            );
            throw new S3HandlingException("Error while deleting some S3 objects: " + e.getMessage());
        } catch (SdkClientException e) {
            throw new S3HandlingException("Error while deleting S3 objects: " + e.getMessage());
        }
    }

    private File convertMultiPartFileToFile(MultipartFile file, String fileName) {
        File convertedFile = new File(fileName);
        try {
            try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
                fos.write(file.getBytes());
            }
        } catch (IOException exp){
            throw new CustomIOException("Failed to convert multipart file to file: " + exp.getMessage());
        }
        return convertedFile;
    }
}

