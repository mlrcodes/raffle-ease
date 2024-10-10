package com.raffleease.raffles_service.Exceptions.CustomExceptions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@Getter
public class DataBaseHandlingException extends RuntimeException{
    private final String msg;
}