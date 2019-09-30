package com.github.rameshl.stockapp.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * stock-app Created by ramesh on 2019-03-20.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock implements Serializable {

    private static final long serialVersionUID = 2756042766800287823L;

    private long id;

    private String name;

    private double currentPrice;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SS'Z'", timezone = "GMT")
    private Date lastUpdate;
}
