package com.github.rameshl.stockapp.endpoints.api.stocks;

import com.github.rameshl.stockapp.endpoints.api.BaseApiEndpoint;
import com.github.rameshl.stockapp.model.Stock;
import com.github.rameshl.stockapp.services.StockService;
import com.github.rameshl.stockapp.utils.Preconditions;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * stock-app Created by ramesh on 2019-03-20.
 */
@Path("/api/stocks")
public class StocksEndpoint extends BaseApiEndpoint {

    private final StockService stockService;

    public StocksEndpoint() {
        this(new StockService()); // can be via DI
    }

    public StocksEndpoint(StockService stockService) {
        this.stockService = stockService;
    }

    @GET
    public Response fetchStocks() {
        List<Stock> stocks = stockService.getAllStocks();
        return Response.ok(stocks).build();
    }

    @GET
    @Path("/{id}")
    public Response fetchStockById(@PathParam("id") long stockId) {

        Stock stock = stockService.getStock(stockId);
        if (stock == null)
            throw new NotFoundException("no stock found for id");

        return Response.ok(stock).build();
    }

    @POST
    public Response createNewStock(Stock stock) throws IllegalArgumentException {

        stock = stockService.createStock(stock);

        return Response.status(Response.Status.CREATED).entity(stock).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateStockPrice(@PathParam("id") long stockId, Stock stock) {

        Preconditions.checkArgument(stock == null, "invalid request payload");

        stock = stockService.updateStockPrice(stockId, stock.getCurrentPrice());
        return Response.ok(stock).build();
    }
}
