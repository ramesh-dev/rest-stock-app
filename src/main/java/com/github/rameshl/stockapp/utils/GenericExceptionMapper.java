package com.github.rameshl.stockapp.utils;

import com.github.rameshl.stockapp.model.ErrorResponse;

import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import lombok.extern.slf4j.Slf4j;

/**
 * stock-app Created by ramesh on 2019-03-20.
 */
@Slf4j
@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable e) {

        Response.Status status = detectStatus(e);
        String msg;

        if (status == Response.Status.INTERNAL_SERVER_ERROR) {

            log.error("Generic Exception : {}", e.getMessage(), e);
            msg = "Something went wrong";

        } else {
            msg = (e.getMessage() != null && !e.getMessage().startsWith("RESTEASY")) ? e.getMessage() : status.toString();
        }

        ErrorResponse errorResponse = new ErrorResponse(status.name().toLowerCase(), msg);

        return Response
                .status(status)
                .entity(errorResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    private Response.Status detectStatus(Throwable e) {

        if (e instanceof NotFoundException) {
            return Response.Status.NOT_FOUND;
        } else if (e instanceof IllegalArgumentException) {
            return Response.Status.BAD_REQUEST;
        } else if (e instanceof NotAllowedException) {
            return Response.Status.METHOD_NOT_ALLOWED;
        }

        return Response.Status.INTERNAL_SERVER_ERROR;
    }
}
