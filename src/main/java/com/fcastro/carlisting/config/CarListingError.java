package com.fcastro.carlisting.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CarListingError {

    private Long timestamp;
    private int status;
    private String errorType;
    private String errorMessage;
    private String errorDetail;
    private String path;
}
