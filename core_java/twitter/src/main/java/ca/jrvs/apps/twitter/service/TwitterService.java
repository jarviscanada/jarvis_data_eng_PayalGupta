package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.model.Tweet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TwitterService implements Service{

    private CrdDao dao;
    public TwitterService(CrdDao dao) {
        this.dao = dao;
    }

    @Override
    public Tweet postTweet(Tweet tweet) {
        validatePostTweet(tweet);
        return (Tweet) dao.create(tweet);
    }

    private void validatePostTweet(Tweet tweet){
        String text = tweet.getText();
        Double lon = tweet.getCoordinates().getCoordinates().get(0);
        Double lat = tweet.getCoordinates().getCoordinates().get(1);

        //validate tweet length
        if (text.length() > 140 || text.length() < 1 ){
            throw new IllegalArgumentException("Length of the tweet cannot exceed 140 characters and cannot be blank");
        }
        //validate latitude and longitude
        if(lon < -180 || lon >180 ){
            throw new IllegalArgumentException("The value of the longitude is invalid, must be in the range -180 to 180");
        }
        if(lat < -90 || lat > 90 ){
            throw new IllegalArgumentException("The value of the latitude is invalid, must be in the range -90 to 90");
        }
    }
    @Override
    public Tweet showTweet(String id, String[] fields) {
        if(!validateID(id)){
            throw new IllegalArgumentException("The id must be numerical");
        }
        return  (Tweet) dao.findById(id);
    }

    @Override
    public List<Tweet> deleteTweets(String[] ids) {
        List<Tweet> deleteTweets = new ArrayList<Tweet>();
        Arrays.stream(ids).forEach(id -> {
            if(!validateID(id)){
                throw new IllegalArgumentException("Status ID" + id + " must be numerical");
            }
            deleteTweets.add( (Tweet) dao.deleteById(id));
        });
        return deleteTweets;
    }
    private boolean validateID(String id){
        return id.matches("\\d+");
    }
}
