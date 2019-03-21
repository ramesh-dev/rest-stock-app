package com.github.rameshl.stockapp.config;

import com.github.rameshl.stockapp.endpoints.api.stocks.StocksEndpoint;
import com.github.rameshl.stockapp.utils.GenericExceptionMapper;

import org.jboss.resteasy.plugins.interceptors.CorsFilter;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

/**
 * stock-app Created by ramesh on 2019-03-20.
 */
public class AppConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {

        Set<Class<?>> classes = new HashSet<>(getProviders());

        //apis
        classes.add(StocksEndpoint.class);

        return classes;
    }

    @Override
    public Set<Object> getSingletons() {

        Set<Object> singletons = new HashSet<>();

        CorsFilter cors = new CorsFilter();
        cors.getAllowedOrigins().add("*");
        cors.setCorsMaxAge(1728000);
        cors.setAllowedMethods("OPTIONS, GET, POST, DELETE, PUT");
        cors.setAllowCredentials(false);

        singletons.add(cors);

        return singletons;
    }

    public Set<Class<?>> getProviders() {

        Set<Class<?>> classes = new HashSet<>();

        // jackson
        classes.add(JacksonObjectResolver.class);

        // exception handlers
        classes.add(GenericExceptionMapper.class);

        return classes;
    }
}
