package ca.jrvs.apps.twitter.controller;

import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.util.TweetUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TwitterControllerUnitTest {

    @Mock
    Service service;

    @InjectMocks
    TwitterController controller;

    @Test
    public void postTweet() {
        when(service.postTweet(any())).thenReturn(TweetUtil.buildTweet("test", 50, 0));
        try{
            controller.postTweet(new String[]{"post", "50:0.0"});
            fail();
        }catch(IllegalArgumentException e){
            assertTrue(true);
        }
        try{
            controller.postTweet(new String[]{"post", "test text", "50.0:0.0"});
        }catch(IllegalArgumentException e){
            assertTrue(true);
        }
        try{
            controller.postTweet(new String[]{"post", "test text", "50.0"});
            fail();
        }catch(IllegalArgumentException e){
            assertTrue(true);
        }
    }

    @Test
    public void showTweet() {
        when(service.postTweet(any())).thenReturn(TweetUtil.buildTweet("test", 50, 0));
        try{
            controller.showTweet(new String[]{"id"});
            fail();
        }catch (IllegalArgumentException e){
            assertTrue(true);
        }
        try{
            controller.showTweet(new String[]{"get","id", "text"});
        }catch(IllegalArgumentException e){
            assertTrue(true);
        }
    }

    @Test
    public void deleteTweet() {
        when(service.postTweet(any())).thenReturn(TweetUtil.buildTweet("test", 50, 0));
        try{
            controller.deleteTweet(new String[]{"id1, id2"});
        }catch(IllegalArgumentException e){
            assertTrue(true);
        }

        try{
            controller.deleteTweet(new String[]{"id1"});
            fail();
        }catch(IllegalArgumentException e){
            assertTrue(true);
        }
    }
}