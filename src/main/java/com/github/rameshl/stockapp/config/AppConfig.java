package com.github.rameshl.stockapp.config;

import com.github.rameshl.stockapp.endpoints.api.stocks.StocksEndpoint;
import com.github.rameshl.stockapp.utils.GenericExceptionMapper;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

/**
 * stock-app Created by ramesh on 2019-03-20.
 */
public class AppConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {

        Set<Class<?>> classes = new HashSet<>();

        // jackson
        classes.add(JacksonObjectResolver.class);

        //apis
        classes.add(StocksEndpoint.class);

        // exception handlers
        classes.add(GenericExceptionMapper.class);

        return classes;
    }
}
