package com.github.rameshl.stockapp.endpoints;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.rameshdev.base.JaxrsServerConfig;
import com.github.rameshdev.resteasy.ResteasyServer;
import com.github.rameshdev.resteasy.ResteasyUnit;
import com.github.rameshl.stockapp.config.AppConfig;
import com.github.rameshl.stockapp.endpoints.api.stocks.StocksEndpoint;
import com.github.rameshl.stockapp.model.ErrorResponse;
import com.github.rameshl.stockapp.model.Stock;

import org.jboss.resteasy.mock.MockHttpResponse;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.core.MediaType;

import static com.github.rameshl.stockapp.utils.Utils.defaultObjectMapper;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * StocksEndpoint Integration Test.
 *
 * @author <Ramesh Lingappa>
 * @version 1.0
 * @since <pre>Mar 20, 2019</pre>
 */
public class StocksEndpointTestIT {

    private static final String BASE_STOCK_PATH = "/api/stocks";
    private ResteasyServer server;

    @Before
    public void before() {

        JaxrsServerConfig config = JaxrsServerConfig.empty()
                // with stock endpoint
                .withResources(StocksEndpoint.class)

                // available providers
                .withProviders(new AppConfig().getProviders().toArray(new Class<?>[0]));

        server = ResteasyUnit.newServerWithConfig(config);
    }

    /**
     * Method: fetchStockById(@PathParam("id") long stockId)
     */
    @Test
    public void testFetchStockById_InvalidID() throws Exception {

        MockHttpResponse response = server.resource(BASE_STOCK_PATH + "/" + System.currentTimeMillis()).get();

        assertEquals(404, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON, response.getOutputHeaders().getFirst("content-type"));

        checkErrorResponse(response.getContentAsString(), "not_found", "no stock found for id");
    }

    /**
     * Method: fetchStocks()
     */
    @Test
    public void testFetchStocks() throws Exception {

        MockHttpResponse response = server.resource(BASE_STOCK_PATH).get();

        assertEquals(200, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON, response.getOutputHeaders().getFirst("content-type"));

        String stockJson = response.getContentAsString();
        assertNotNull(stockJson);

        List<Stock> stocks = defaultObjectMapper().readValue(stockJson, new TypeReference<List<Stock>>() {
        });

        assertNotNull(stocks);
        assertTrue(stocks.size() > 0);
    }

    /**
     * Method: fetchStockById(@PathParam("id") long stockId)
     */
    @Test
    public void testFetchStockById_valid() throws Exception {

        testFetchStockById(1);

        testFetchStockById(2);
    }

    /**
     * Method: createNewStock(Stock stock)
     */
    @Test
    public void testCreateNewStock_InvalidStock() throws Exception {

        /* bad payload */
        MockHttpResponse response = server.resource(BASE_STOCK_PATH).post(MediaType.APPLICATION_JSON, "");

        assertEquals(400, response.getStatus());
        checkErrorResponse(response.getContentAsString(), "bad_request", "invalid stock data");

        Stock stock = new Stock();

        /* bad name */
        response = server.resource(BASE_STOCK_PATH).post(MediaType.APPLICATION_JSON, defaultObjectMapper().writeValueAsString(stock));

        assertEquals(400, response.getStatus());
        checkErrorResponse(response.getContentAsString(), "bad_request", "invalid name, cannot be blank");
    }

    /**
     * Method: createNewStock(Stock stock)
     */
    @Test
    public void testCreateNewStock_valid() throws Exception {

        Stock stock = new Stock();
        stock.setName("test");

        MockHttpResponse response = server.resource(BASE_STOCK_PATH).post(MediaType.APPLICATION_JSON, defaultObjectMapper().writeValueAsString(stock));

        assertEquals(201, response.getStatus());

        String stockJson = response.getContentAsString();
        assertNotNull(stockJson);

        Stock created = defaultObjectMapper().readValue(stockJson, Stock.class);
        assertNotNull(created);
        assertTrue(created.getId() > 0);
        assertEquals(stock.getName(), created.getName());
        assertNotNull(created.getLastUpdate());

        //fetch stock and confirm
        testFetchStockById(created.getId());
    }

    /**
     * Method: updateStockPrice(@PathParam("id") long stockId, Stock stock)
     */
    @Test
    public void testUpdateStockPrice_Invalid() throws Exception {

        /* invalid payload */
        MockHttpResponse response = server.resource(BASE_STOCK_PATH + "/" + System.currentTimeMillis())
                .put(MediaType.APPLICATION_JSON, "");

        assertEquals(400, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON, response.getOutputHeaders().getFirst("content-type"));

        checkErrorResponse(response.getContentAsString(), "bad_request", "invalid request payload");


        /* non existing id */

        Stock changes = new Stock();
        changes.setCurrentPrice(123.45);

        response = server.resource(BASE_STOCK_PATH + "/" + System.currentTimeMillis())
                .put(MediaType.APPLICATION_JSON, defaultObjectMapper().writeValueAsString(changes));

        assertEquals(404, response.getStatus());

        checkErrorResponse(response.getContentAsString(), "not_found", "no stock found for id");
    }

    /**
     * Method: updateStockPrice(@PathParam("id") long stockId, Stock stock)
     */
    @Test
    public void testUpdateStockPrice_valid() throws Exception {

        Stock changes = new Stock();
        changes.setCurrentPrice(123.45);

        MockHttpResponse response = server.resource(BASE_STOCK_PATH + "/1")
                .put(MediaType.APPLICATION_JSON, defaultObjectMapper().writeValueAsString(changes));

        assertEquals(200, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON, response.getOutputHeaders().getFirst("content-type"));

        String stockJson = response.getContentAsString();
        assertNotNull(stockJson);

        Stock stock = defaultObjectMapper().readValue(stockJson, Stock.class);
        assertNotNull(stock);
        assertEquals(changes.getCurrentPrice(), stock.getCurrentPrice(), 0);
    }

    /* test helpers */

    private void testFetchStockById(long id) throws IOException {

        MockHttpResponse response = server.resource(BASE_STOCK_PATH + "/" + id).get();

        assertEquals(200, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON, response.getOutputHeaders().getFirst("content-type"));

        String stockJson = response.getContentAsString();
        assertNotNull(stockJson);

        Stock stock = defaultObjectMapper().readValue(stockJson, Stock.class);
        assertNotNull(stock);
        assertEquals(id, stock.getId());
    }

    private void checkErrorResponse(String json, String error, String desc) throws IOException {

        assertNotNull(json);

        ErrorResponse errorResponse = defaultObjectMapper().readValue(json, ErrorResponse.class);

        assertNotNull(errorResponse);
        assertEquals(error, errorResponse.getError());
        assertEquals(desc, errorResponse.getErrorDesc());
    }
} 
