package com.raffleease.raffles_service.S3.Services;

import java.util.List;

public interface S3Service {
    void delete(String file);

    void delete (List<String> fileKey);

    public List<String> generateUploadUrls(List<String> fileNames);

    public List<String> generateViewUrls(List<String> fileNames);
}