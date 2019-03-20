package com.github.rameshl.stockapp.endpoints;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.rameshdev.base.JaxrsServerConfig;
import com.github.rameshdev.resteasy.ResteasyServer;
import com.github.rameshdev.resteasy.ResteasyUnit;
import com.github.rameshl.stockapp.config.JacksonObjectResolver;
import com.github.rameshl.stockapp.endpoints.api.stocks.StocksEndpoint;
import com.github.rameshl.stockapp.model.ErrorResponse;
import com.github.rameshl.stockapp.model.Stock;
import com.github.rameshl.stockapp.utils.GenericExceptionMapper;

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
                .withResources(StocksEndpoint.class)
                .withProviders(JacksonObjectResolver.class)
                .withProviders(GenericExceptionMapper.class);

        server = ResteasyUnit.newServerWithConfig(config);
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
    public void testFetchStockById_InvalidID() throws Exception {

        MockHttpResponse response = server.resource(BASE_STOCK_PATH + "/200").get();

        assertEquals(404, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON, response.getOutputHeaders().getFirst("content-type"));

        String stockJson = response.getContentAsString();
        assertNotNull(stockJson);

        ErrorResponse errorResponse = defaultObjectMapper().readValue(stockJson, ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals("not_found", errorResponse.getError());
        assertEquals("no stock found for id", errorResponse.getErrorDesc());
    }

    /**
     * Method: fetchStockById(@PathParam("id") long stockId)
     */
    @Test
    public void testFetchStockById_validID() throws Exception {

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
        ErrorResponse errorResponse = defaultObjectMapper().readValue(response.getContentAsString(), ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals("bad_request", errorResponse.getError());
        assertEquals("invalid stock data", errorResponse.getErrorDesc());

        Stock stock = new Stock();

        /* bad id */
        response = server.resource(BASE_STOCK_PATH).post(MediaType.APPLICATION_JSON, defaultObjectMapper().writeValueAsString(stock));

        assertEquals(400, response.getStatus());
        errorResponse = defaultObjectMapper().readValue(response.getContentAsString(), ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals("bad_request", errorResponse.getError());
        assertEquals("invalid id", errorResponse.getErrorDesc());

        stock.setId(100);

        /* bad name */
        response = server.resource(BASE_STOCK_PATH).post(MediaType.APPLICATION_JSON, defaultObjectMapper().writeValueAsString(stock));

        assertEquals(400, response.getStatus());
        errorResponse = defaultObjectMapper().readValue(response.getContentAsString(), ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals("bad_request", errorResponse.getError());
        assertEquals("invalid name, cannot be blank", errorResponse.getErrorDesc());
    }

    /**
     * Method: createNewStock(Stock stock)
     */
    @Test
    public void testCreateNewStock_withExistingID() throws Exception {

        Stock stock = new Stock();
        stock.setId(1); // exists by default
        stock.setName("test");

        MockHttpResponse response = server.resource(BASE_STOCK_PATH).post(MediaType.APPLICATION_JSON, defaultObjectMapper().writeValueAsString(stock));

        assertEquals(400, response.getStatus());
        ErrorResponse errorResponse = defaultObjectMapper().readValue(response.getContentAsString(), ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals("bad_request", errorResponse.getError());
        assertEquals("stock with id already exists", errorResponse.getErrorDesc());
    }

    /**
     * Method: createNewStock(Stock stock)
     */
    @Test
    public void testCreateNewStock_valid() throws Exception {

        Stock stock = new Stock();
        stock.setId(System.currentTimeMillis());
        stock.setName("test");

        MockHttpResponse response = server.resource(BASE_STOCK_PATH).post(MediaType.APPLICATION_JSON, defaultObjectMapper().writeValueAsString(stock));

        assertEquals(201, response.getStatus());

        String stockJson = response.getContentAsString();
        assertNotNull(stockJson);

        Stock created = defaultObjectMapper().readValue(stockJson, Stock.class);
        assertNotNull(created);
        assertEquals(stock.getId(), created.getId());
        assertEquals(stock.getName(), created.getName());
        assertNotNull(created.getLastUpdate());

        testFetchStockById(stock.getId());
    }

    /**
     * Method: updateStockPrice(@PathParam("id") long stockId, Stock stock)
     */
    @Test
    public void testUpdateStockPrice_InvalidID() throws Exception {

        /* invalid payload */
        MockHttpResponse response = server.resource(BASE_STOCK_PATH + "/" + System.currentTimeMillis())
                .put(MediaType.APPLICATION_JSON, "");

        assertEquals(400, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON, response.getOutputHeaders().getFirst("content-type"));

        String errorJson = response.getContentAsString();
        assertNotNull(errorJson);

        ErrorResponse errorResponse = defaultObjectMapper().readValue(errorJson, ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals("bad_request", errorResponse.getError());
        assertEquals("invalid request payload", errorResponse.getErrorDesc());

        /* non existing id */

        Stock changes = new Stock();
        changes.setCurrentPrice(123.45);

        response = server.resource(BASE_STOCK_PATH + "/" + System.currentTimeMillis())
                .put(MediaType.APPLICATION_JSON, defaultObjectMapper().writeValueAsString(changes));

        assertEquals(404, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON, response.getOutputHeaders().getFirst("content-type"));

        errorJson = response.getContentAsString();
        assertNotNull(errorJson);

        errorResponse = defaultObjectMapper().readValue(errorJson, ErrorResponse.class);
        assertNotNull(errorResponse);
        assertEquals("not_found", errorResponse.getError());
        assertEquals("no stock found for id", errorResponse.getErrorDesc());
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

} 
