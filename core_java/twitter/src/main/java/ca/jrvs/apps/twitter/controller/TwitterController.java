package ca.jrvs.apps.twitter.controller;

import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.util.TweetUtil;
import org.springframework.util.StringUtils;

import java.util.List;

public class TwitterController implements Controller{

    private static final String COORD_SEP = ":";
    private static final String COMMA = ",";
    private Service service;

    public TwitterController(Service service) {
        this.service = service;
    }

    @Override
    public Tweet postTweet(String[] args) {
        if(args.length != 3){
            throw new IllegalArgumentException("USAGE: TwitterCLIApp post\"tweet_text\"\"latitude:longitude\"");
        }

        String tweetText = args[1];
        String coord = args[2];
        String[] coordArray = coord.split(COORD_SEP);
        if(coordArray.length != 2 || StringUtils.isEmpty(tweetText)) {
            throw new IllegalArgumentException("Invalid location format\nUSAGE: TwitterCLIApp post\"tweet_text\"\"latitude:longitude\"");
        }
        Double lat = null;
        Double lon = null;
        try {
            lat = Double.parseDouble(coordArray[0]);
            lon = Double.parseDouble(coordArray[1]);
        }catch(Exception e){
            throw new IllegalArgumentException("Cannot parse arguments to double",e);
        }

        Tweet postTweet = TweetUtil.buildTweet(tweetText, lon, lat);
        return service.postTweet(postTweet);
    }

    @Override
    public Tweet showTweet(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("USAGE: TwitterCLIApp show\"tweet_id\"\"[field1, field2]\"");
        }
        String id = args[1];
        String[] fields;
        if( args.length > 2){
            fields = args[2].split(COMMA);
            return service.showTweet(id, fields);
        }
        return service.showTweet(id, null);
    }

    @Override
    public List<Tweet> deleteTweet(String[] args) {
        if(args.length < 2){
            throw new IllegalArgumentException("USAGE: TwitterCLIApp delete\"ids");
        }
        String[] ids = args[1].split(COMMA);
        return service.deleteTweets(ids);
    }
}
