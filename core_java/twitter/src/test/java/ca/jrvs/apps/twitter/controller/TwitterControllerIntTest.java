package ca.jrvs.apps.twitter.controller;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.service.TwitterService;
import ca.jrvs.apps.twitter.util.TweetUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TwitterControllerIntTest {
    Service service;
    Controller controller;
    String text;

    @Before
    public void setup() {
        String consumerKey = System.getenv("consumerKey");
        String consumerSecret = System.getenv("consumerSecret");
        String accessToken = System.getenv("accessToken");
        String tokenSecret = System.getenv("tokenSecret");
        //setup dependency
        HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken, tokenSecret);
        //pass dependency
        CrdDao crdDao = new TwitterDao(httpHelper);
        service = new TwitterService(crdDao);
        controller = new TwitterController(service);

        String hashTag = "#abc";
        text = "@someonesometext " + hashTag + " " + System.currentTimeMillis();

    }

    @Test
    public void postTweet(){
        Tweet tweet = service.postTweet(TweetUtil.buildTweet("text", 50, 50));
        Tweet postTweet = controller.postTweet(new String[]{"post", text, "50:50"});
        assertEquals(tweet.getCoordinates().getCoordinates().get(0),
                postTweet.getCoordinates().getCoordinates().get(0));

        assertEquals(tweet.getCoordinates().getCoordinates().get(1),
                postTweet.getCoordinates().getCoordinates().get(1));

        assertEquals(text, postTweet.getText());

        assertNotNull(postTweet);
    }

    @Test
    public void showTweet(){
        Tweet tweet = service.postTweet(TweetUtil.buildTweet(text, 50, 50));
        Tweet showTweet = controller.showTweet(new String[]{"get", tweet.getId_str(), "fields"});

        assertNotNull(showTweet);
        assertEquals(tweet.getText(), showTweet.getText());

    }

    @Test
    public void deleteTweet(){
        Tweet tweet = service.postTweet(TweetUtil.buildTweet(text, 50, 50));
        Tweet tweet2 = service.postTweet(TweetUtil.buildTweet("tweet", 30, 30));
        String ids = tweet.getId_str() + tweet2.getId_str();

        List<Tweet> deleteTweets = controller.deleteTweet(new String[]{"delete", ids});
        assertNotNull(deleteTweets);

    }

}