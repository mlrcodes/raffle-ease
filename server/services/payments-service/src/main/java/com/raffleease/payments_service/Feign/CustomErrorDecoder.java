package com.raffleease.payments_service.Feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raffleease.common_models.Exceptions.ApiError;
import com.raffleease.common_models.Exceptions.CustomExceptions.CustomIOException;
import com.raffleease.common_models.Exceptions.CustomExceptions.DataBaseHandlingException;
import com.raffleease.common_models.Exceptions.CustomExceptions.ObjectNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus status = HttpStatus.valueOf(response.status());
        ApiError apiError = null;
        try {
            if (response.body() != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                apiError = objectMapper.readValue(response.body().asInputStream(), ApiError.class);
            }
        } catch (IOException e) {
            return new CustomIOException("Failed to deserialize error response: " + e.getMessage());
        }

        return switch (status) {
            case NOT_FOUND -> {
                assert apiError != null;
                yield new ObjectNotFoundException(apiError.getMessage());
            }
            case INTERNAL_SERVER_ERROR -> {
                assert apiError != null;
                yield new DataBaseHandlingException(apiError.getMessage());
            }
            default -> {
                assert apiError != null;
                yield new Exception("An unexpected error occurred: " + apiError.getMessage());
            }
        };
    }
}