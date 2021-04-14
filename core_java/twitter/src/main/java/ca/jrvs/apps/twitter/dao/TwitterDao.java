package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.JsonUtil;
import com.google.gdata.util.common.base.PercentEscaper;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Repository
public class TwitterDao implements CrdDao<Tweet, String>{
    //URI constants
    private static final String API_BASE_URI = "https://api.twitter.com";
    private static final String POST_PATH = "/1.1/statuses/update.json";
    private static final String SHOW_PATH = "/1.1/statuses/show.json";
    private static final String DELETE_PATH = "/1.1/statuses/destroy";

    //URI symbols
    private static final String QUERY_SYM = "?";
    private static final String AMPERSAND = "&";
    private static final String EQUAL = "=";

    //Response code
    private static final int HTTP_OK = 200;

    private HttpHelper httpHelper;

    @Autowired
    public TwitterDao(HttpHelper httpHelper) {

        this.httpHelper = httpHelper;
    }

    @Override
    public Tweet create(Tweet tweet) {
        URI uri;
        try{
            String text = tweet.getText();
            List<Double> coordinates = tweet.getCoordinates().getCoordinates();
            PercentEscaper percentEscaper = new PercentEscaper("", false);
            uri = new URI(API_BASE_URI + POST_PATH + QUERY_SYM + "status"+ EQUAL + percentEscaper.escape(text) +
                    AMPERSAND + "long" + EQUAL + coordinates.get(0) + AMPERSAND + "lat" + EQUAL +coordinates.get(1));
            HttpResponse response = httpHelper.httpPost(uri);
            return parseResponseBody(response, HTTP_OK);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Tweet findById(String s) {
        URI uri;
        try {
            uri = new URI(API_BASE_URI + SHOW_PATH + QUERY_SYM + "id" + EQUAL + s);
            HttpResponse response = httpHelper.httpGet(uri);
            return parseResponseBody(response, HTTP_OK);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Could not execute tweet for the id");
        }

    }

    @Override
    public Tweet deleteById(String s) {
        URI uri;
        try {
            uri = new URI(API_BASE_URI + DELETE_PATH + "/" + s + ".json");
            HttpResponse response = httpHelper.httpPost(uri);
            return parseResponseBody(response, HTTP_OK);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Could not execute tweet for the id");
        }
    }

    Tweet parseResponseBody(HttpResponse response, Integer expectedStatusCode){
        Tweet tweet = null;

        int status = response.getStatusLine().getStatusCode();
        if(status != expectedStatusCode) {
            try {
                System.out.println(EntityUtils.toString(response.getEntity()));
            } catch (IOException e) {
                System.out.println(("Response has no entity"));
            }
            throw new RuntimeException("unexpected HTTP status:"+ status);
        }

        if(response.getEntity() == null){
            throw new RuntimeException("Empty response body");
        }

        //convert response entity to str
        String jsonStr;
        try{
            jsonStr = EntityUtils.toString(response.getEntity());
        }catch(IOException e){
            throw new RuntimeException("Failed to convert entity to string", e);
        }

        //Convert JSON string to Tweet object
        try{
            tweet = JsonUtil.toObjectFromJson(jsonStr, Tweet.class);
            System.out.println(tweet);
        }catch(IOException e){
            throw new RuntimeException("Unable to convert Json str to object",e);
        }
        return tweet;
    }
}
