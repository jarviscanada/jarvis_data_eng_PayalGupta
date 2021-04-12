package ca.jrvs.apps.twitter.util;

import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Tweet;
import com.google.gdata.util.common.base.PercentEscaper;

import java.util.Arrays;

public class TweetUtil {

    public static Tweet buildTweet(String text, double lon, double lat){
        Tweet tweet = new Tweet();
       // PercentEscaper percentEscaper = new PercentEscaper("", false);
        Coordinates coordinates = new Coordinates();
        coordinates.setCoordinates(Arrays.asList(lon, lat));
        coordinates.setType("Point");

        //tweet.setText(percentEscaper.escape(text));
        tweet.setText(text);
        tweet.setCoordinates(coordinates);
        System.out.println(tweet);
        return tweet;
    }
}
