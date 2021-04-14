package ca.jrvs.apps.twitter.dao.helper;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.net.URI;

@Component
public class TwitterHttpHelper implements HttpHelper{

    private OAuthConsumer consumer;
    private HttpClient httpClient;

    public TwitterHttpHelper() {
        String CONSUMER_KEY = System.getenv("consumerKey");
        String CONSUMER_SECRET = System.getenv("consumerSecret");
        String ACCESS_TOKEN = System.getenv("accessToken");
        String TOKEN_SECRET = System.getenv("tokenSecret");
        consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
        consumer.setTokenWithSecret(ACCESS_TOKEN, TOKEN_SECRET);
        httpClient = HttpClientBuilder.create().build();
    }

    public TwitterHttpHelper(String consumerKey, String consumerSecret, String accessToken,
                             String tokenSecret) {
        consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
        consumer.setTokenWithSecret(accessToken, tokenSecret);
        httpClient = HttpClientBuilder.create().build();
    }

    /**
     * To send post request to the uri
     * @param uri
     * @return
     */
    @Override
    public HttpResponse httpPost(URI uri) {
       try {
           HttpPost request = new HttpPost(uri);
           consumer.sign(request);
           return httpClient.execute(request);
       }catch(OAuthException | IOException e){
           throw new RuntimeException("post method did not execute successfully",e);
       }
    }

    /**
     * To send the get request to the uri
     * @param uri
     * @return
     */
    @Override
    public HttpResponse httpGet(URI uri) {
        try {
            HttpGet request = new HttpGet(uri);
            consumer.sign(request);
            return httpClient.execute(request);
        } catch (OAuthException | IOException e) {
            throw new RuntimeException("get method did not execute successfully", e);
        }
    }
}
