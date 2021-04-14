package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.TweetUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TwitterServiceIntTest {
    Service service;
    Tweet postTweet;
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

        String hashTag = "#abc";
        String text = "@someonesometext " + hashTag + " " + System.currentTimeMillis();

        Tweet tweet = TweetUtil.buildTweet(text, -1d, 1d);
        postTweet = service.postTweet(tweet);
    }

        @Test
        public void postTweet(){
            Double lat = 1d;
            Double lon = -1d;
            assertNotNull(postTweet);
            assertEquals(lon, postTweet.getCoordinates().getCoordinates().get(0));
            assertEquals(lat, postTweet.getCoordinates().getCoordinates().get(1));
    }
        @Test
    public void showTweet(){
            Tweet showTweet = service.showTweet(postTweet.getId_str(), new String []{});
            assertEquals(postTweet.getId_str(), showTweet.getId_str());
            assertNotNull(showTweet);
        }

        @Test
        public void deleteTweets(){
            String[] ids = {postTweet.getId_str()};
            List<Tweet> tweets = service.deleteTweets(ids);
            assertEquals(postTweet.getId_str(), tweets.get(0).getId_str());
        }
    }

