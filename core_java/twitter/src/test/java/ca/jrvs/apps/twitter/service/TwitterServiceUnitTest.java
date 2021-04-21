package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.TweetUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TwitterServiceUnitTest {

        @Mock
        CrdDao dao;

        @InjectMocks
        TwitterService service;

    @Test
    public void postTweet() {
        when(dao.create(any())).thenReturn(new Tweet());
        service.postTweet(TweetUtil.buildTweet("test", 50.0, 0.0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void postTweetNullText() {
        when(dao.create(any())).thenReturn(new Tweet());
        service.postTweet(TweetUtil.buildTweet("", 50.0, 0.0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void postTweetLonExceptionTest() {
        when(dao.create(any())).thenReturn(new Tweet());
        service.postTweet(TweetUtil.buildTweet("test", 190, 0.0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void postTweetLatExceptionTest() {
        when(dao.create(any())).thenReturn(new Tweet());
        service.postTweet(TweetUtil.buildTweet("test", 180, 100));
    }

    @Test
    public void showTweetPositive() {
        when(dao.create(any())).thenReturn(new Tweet());
        service.showTweet("123456", new String[]{});
    }

    @Test(expected = IllegalArgumentException.class)
    public void showTweetNegative() {
        when(dao.create(any())).thenReturn(new Tweet());
        service.showTweet("123456f", new String[]{});
    }

    @Test
    public void deleteTweets() {
        String [] idList = {"12345", "12347884375375"};
        when(dao.create(any())).thenReturn(new Tweet());
        service.deleteTweets(idList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteTweetsException() {
        String [] idList = {"12345#", "12347884375375"};
        when(dao.create(any())).thenReturn(new Tweet());
        service.deleteTweets(idList);
    }
}