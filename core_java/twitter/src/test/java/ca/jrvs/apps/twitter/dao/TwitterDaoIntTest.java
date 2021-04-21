package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.TweetUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TwitterDaoIntTest {
    TwitterDao dao;

    @Before
    public void setup(){
        String consumerKey = System.getenv("consumerKey");
        String consumerSecret = System.getenv("consumerSecret");
        String accessToken = System.getenv("accessToken");
        String tokenSecret = System.getenv("tokenSecret");
        //setup dependency
        HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken, tokenSecret);
        //pass dependency
        this.dao = new TwitterDao(httpHelper);
    }

    @Test
    public void create() {
        String hashTag = "#abc";
        String text = "@someonesometext " + hashTag + " " + System.currentTimeMillis();
        Double lat = 1d;
        Double lon = -1d;
        Tweet postTweet = TweetUtil.buildTweet(text, lon, lat);
        Tweet tweet = dao.create(postTweet);

        assertNotNull(postTweet);
        assertEquals(text, tweet.getText());
        assertNotNull(text, tweet.getCoordinates());
        assertEquals(2, tweet.getCoordinates().getCoordinates().size());
        assertEquals(lon, tweet.getCoordinates().getCoordinates().get(0));
        assertEquals(lat, tweet.getCoordinates().getCoordinates().get(1));
        assertTrue(hashTag.contains(tweet.getEntities().getHashtags().get(0).getText()));

    }

    @Test
    public void findById() {
       String id_str = "1381670036759252993";
       Tweet findTweet = dao.findById(id_str);
       assertNotNull(findTweet);
       assertEquals(id_str, findTweet.getId_str());
    }

    @Test
    public void deleteById() {
        String id_str = "1381670036759252993";
        Tweet deleteTweet = dao.deleteById(id_str);
        assertEquals(id_str, deleteTweet.getId_str());

    }
}