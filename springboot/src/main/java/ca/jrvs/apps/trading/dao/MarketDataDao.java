package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import ca.jrvs.apps.trading.util.JsonUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public class MarketDataDao implements CrudRepository<IexQuote, String> {

    private static final String IEX_BATCH_PATH = "/stock/market/batch?symbols=%s&types=quote&token=";
    private static String IEX_BATCH_URL;

    private Logger logger = LoggerFactory.getLogger(MarketDataDao.class);
    private HttpClientConnectionManager httpClientConnectionManager;

    @Autowired
    public MarketDataDao(HttpClientConnectionManager httpClientConnectionManager, MarketDataConfig marketDataConfig) {
        this.httpClientConnectionManager = httpClientConnectionManager;
        IEX_BATCH_URL = marketDataConfig.getHost() + IEX_BATCH_PATH + marketDataConfig.getToken();
    }

    @Override
    public <S extends IexQuote> S save(S s) {
       throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public <S extends IexQuote> Iterable<S> saveAll(Iterable<S> iterable) {
       throw  new UnsupportedOperationException("Not implemented");
    }

    /**
     *Get an IexQuote
     * @param ticker
     * @throws DataRetrievalFailureException if a HTTP request failed
     * @return
     */
    @Override
    public Optional<IexQuote> findById(String ticker) {
        Optional <IexQuote> iexQuote;
        List<IexQuote> quotes = findAllById(Collections.singletonList(ticker));

        if (quotes.size() == 0){
            return Optional.empty();
        }else if (quotes.size() == 1){
            iexQuote = Optional.of(quotes.get(0));
        }else {
            throw new DataRetrievalFailureException("Unexpected number of Quotes");
        }
        return iexQuote;
    }

    @Override
    public boolean existsById(String s) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Iterable<IexQuote> findAll() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public List<IexQuote> findAllById(Iterable<String> tickers) {
        tickers.forEach(ticker -> {if (ticker.length() < 2 || ticker.length() >5 || ticker.matches(".*\\d+.*")) {
            throw new IllegalArgumentException("Incorrect ticker format");
        }
        });

        String ids = StreamSupport.stream(tickers.spliterator(), false)
                .collect(Collectors.joining(","));

        String uri = String.format(IEX_BATCH_URL, ids);

        String response = executeHttpGet(uri).orElseThrow(()-> new IllegalArgumentException("Invalid ticker"));

        JSONObject iexQuotesJson = new JSONObject(response);
        if(iexQuotesJson.length() == 0){
            throw new IllegalArgumentException("Invalid ticker");
        }

        List<IexQuote> iexQuotes = new ArrayList<>();

        for (String ticker : tickers){
            try{
                iexQuotes.add(JsonUtil.toObjectFromJson(iexQuotesJson.getJSONObject(ticker)
                        .getJSONObject("quote").toString(), IexQuote.class));
            }catch(IOException e){
                throw new RuntimeException("Unable to read response", e);
            }
        }
        return iexQuotes;
    }

    @Override
    public long count() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void deleteById(String s) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void delete(IexQuote iexQuote) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void deleteAll(Iterable<? extends IexQuote> iterable) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Execute a get and return http entity/body as a string
     *
     * @param url resource url
     * @return http response or Optional.empty for 404 response
     * @throws DataRetrievalFailureException if HTTP failed or status code is unexpected
     */
    private Optional<String> executeHttpGet(String url){
        HttpGet httpGet = new HttpGet(URI.create(url));
        HttpResponse response;
        try {
            response = getHttpClient().execute(httpGet);
        } catch(IOException e){
                throw new DataRetrievalFailureException("Unable to receive http response", e);
            }

            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode == 404){
                return Optional.empty();
            }else if (statusCode == 200) {
                try {
                    return Optional.of(EntityUtils.toString(response.getEntity()));
                } catch (IOException e) {
                    throw new DataRetrievalFailureException("Unable to read response", e);
                }
            }else {
                throw new DataRetrievalFailureException("Status: " +  statusCode);
            }

    }

    /**
     * Borrow a HTTP client from the httpClientConnectionManager
     * @return a httpClient
     */

    private CloseableHttpClient getHttpClient(){
        return HttpClients.custom()
                .setConnectionManager(httpClientConnectionManager)
                .setConnectionManagerShared(true)
                .build();
    }
}
