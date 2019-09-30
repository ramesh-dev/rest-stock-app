package com.github.rameshl.stockapp.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * stock-app Created by ramesh on 2019-03-20.
 */

@Data
@NoArgsConstructor
public class ErrorResponse implements Serializable {

    private String error;

    private String errorDesc;

    public ErrorResponse(String error, String errorDesc) {
        this.error = error;
        this.errorDesc = errorDesc;
    }
}
