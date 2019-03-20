package com.github.rameshl.stockapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import static com.github.rameshl.stockapp.utils.Utils.defaultObjectMapper;

/**
 * stock-app Created by ramesh on 2019-03-20.
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class JacksonObjectResolver implements ContextResolver<ObjectMapper> {

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return defaultObjectMapper();
    }
}
