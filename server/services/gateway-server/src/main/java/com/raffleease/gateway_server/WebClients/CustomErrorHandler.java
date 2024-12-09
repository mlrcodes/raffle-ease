package com.raffleease.gateway_server.WebClients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raffleease.common_models.Exceptions.ApiError;
import com.raffleease.common_models.Exceptions.CustomExceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class CustomErrorHandler implements ResponseErrorHandler {
    private final ObjectMapper objectMapper;

    public CustomErrorHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        ApiError apiError;
        try {
            apiError = objectMapper.readValue(response.getBody(), ApiError.class);
        } catch (IOException e) {
            throw new CustomIOException("Failed to deserialize error response: " + e.getMessage());
        }

        HttpStatus status = getStatus(response);

        switch (status) {
            case UNAUTHORIZED:
                throw new AuthorizationException(apiError != null ? apiError.getMessage() : "Authorization error occurred");
            case INTERNAL_SERVER_ERROR:
                throw new ConflictException(apiError != null ? apiError.getMessage() : "An unexpected error occurred");
            default:
                throw new RuntimeException("An unexpected error occurred");
        }
    }

    private HttpStatus getStatus(ClientHttpResponse response) throws IOException {
        HttpStatus status;
        try {
            HttpStatusCode statusCode = response.getStatusCode();
            status = HttpStatus.resolve(statusCode.value());
            if (status == null) {
                throw new CustomIOException("Unknown HTTP status code: " + statusCode);
            }
        } catch (IOException e) {
            throw new CustomIOException("Error occurred while retrieving the response status: " + e.getMessage());
        } catch (IllegalArgumentException exp) {
            throw new IllegalArgumentException("Invalid response status value: " + response.getStatusText());
        }
        return status;
    }
}
