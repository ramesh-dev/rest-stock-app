package com.github.rameshl.stockapp.utils;

/**
 * stock-app Created by ramesh on 2019-03-20.
 */
public class Preconditions {

    public static void checkArgument(boolean expression, String message) throws IllegalArgumentException {
        if (expression)
            throw new IllegalArgumentException(String.valueOf(message));
    }
}
