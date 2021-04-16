package ca.jrvs.apps.twitter;

import ca.jrvs.apps.twitter.controller.Controller;
import ca.jrvs.apps.twitter.controller.TwitterController;
import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.service.TwitterService;
import ca.jrvs.apps.twitter.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TwitterCLIApp {
    public static final String USAGE = "USAGE: TwitterCLIApp post|show|delete [options]";
    private Controller controller;
    @Autowired
    public TwitterCLIApp(Controller controller) {
        this.controller = controller;
    }

    /*public static void main(String[] args) {
        String consumerKey = System.getenv("consumerKey");
        String consumerSecret = System.getenv("consumerSecret");
        String accessToken = System.getenv("accessToken");
        String tokenSecret = System.getenv("tokenSecret");
        //setup dependency
        HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken, tokenSecret);
        //pass dependency
        CrdDao crdDao = new TwitterDao(httpHelper);
        Service service = new TwitterService(crdDao);
        Controller controller = new TwitterController(service);
        TwitterCLIApp app = new TwitterCLIApp(controller);

        app.run(args);
    }*/
        public void run(String[] args) {
            if (args.length == 0) {
                throw new IllegalArgumentException(USAGE);
            } else if (args[0].toLowerCase().equals("post")) {
                printTweet(controller.postTweet(args));
            } else if (args[0].toLowerCase().equals("show")) {
                printTweet(controller.showTweet(args));
            } else if ( args[0].toLowerCase().equals("delete")) {
                controller.deleteTweet(args).forEach(this::printTweet);
            } else {
                throw new IllegalArgumentException(USAGE);
            }
        }
        private void printTweet(Tweet tweet){
            try{
                System.out.println(JsonUtil.toJson(tweet, true, true));
            }catch(JsonProcessingException e){
                throw new RuntimeException("Cannot parse to Json");
            }
        }
    }

