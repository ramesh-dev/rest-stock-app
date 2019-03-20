package com.github.rameshl.stockapp.services;

import com.github.rameshl.stockapp.model.Stock;
import com.github.rameshl.stockapp.utils.Preconditions;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.NotFoundException;

/**
 * stock-app Created by ramesh on 2019-03-20.
 */
public class StockService {

    private static final Map<Long, Stock> stocks = new HashMap<>();

    static {
        loadStocks();
    }

    /* Load sample stocks */
    private static void loadStocks() {

        stocks.put(1L, new Stock(1, "Google", 30.0, new Date()));
        stocks.put(2L, new Stock(2, "Apple", 50.0, new Date()));
        stocks.put(3L, new Stock(3, "Facebook", 35.0, new Date()));
        stocks.put(4L, new Stock(4, "Microsoft", 40.0, new Date()));
        stocks.put(5L, new Stock(5, "Github", 25.0, new Date()));
    }

    public List<Stock> getAllStocks() {
        return new ArrayList<>(stocks.values());
    }

    public Stock getStock(long stockId) throws IllegalArgumentException {

        Preconditions.checkArgument(stockId <= 0, "invalid stock id");
        return stocks.get(stockId);
    }

    public Stock createStock(Stock stock) throws IllegalArgumentException {

        validateStock(stock);

        Preconditions.checkArgument(getStock(stock.getId()) != null, "stock with id already exists");

        return saveStock(stock);
    }

    public Stock updateStockPrice(long id, double price) throws IllegalArgumentException, NotFoundException {

        Stock stock = getStock(id);

        if (stock == null)
            throw new NotFoundException("no stock found for id");

        Preconditions.checkArgument((stock.getCurrentPrice() == price), "no changes to save");

        stock.setCurrentPrice(price);

        return saveStock(stock);
    }

    private Stock saveStock(Stock stock) throws IllegalArgumentException {

        validateStock(stock);

        stock.setLastUpdate(new Date());

        stocks.put(stock.getId(), stock);

        return stock;
    }

    private void validateStock(Stock stock) throws IllegalArgumentException {

        Preconditions.checkArgument(stock == null, "invalid stock data");
        Preconditions.checkArgument(stock.getId() <= 0, "invalid id");
        Preconditions.checkArgument(stock.getName() == null || stock.getName().trim().isEmpty(), "invalid name, cannot be blank");

        Preconditions.checkArgument(stock.getCurrentPrice() < 0, "stock price cannot be negative");
    }
}
