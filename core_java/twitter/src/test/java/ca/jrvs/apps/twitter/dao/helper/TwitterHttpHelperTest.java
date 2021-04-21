package ca.jrvs.apps.twitter.dao.helper;

import com.google.gdata.util.common.base.PercentEscaper;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.*;

public class TwitterHttpHelperTest {

    @Test
    public void httpPost() throws Exception{
        HttpHelper httpHelper = new TwitterHttpHelper();
        String status = "today is a good day";
        PercentEscaper percentEscaper = new PercentEscaper("", false);
        HttpResponse response = httpHelper.httpPost(new URI("https://api.twitter.com/1.1/statuses/update.json?status=" + percentEscaper.escape(status)));
        System.out.println(EntityUtils.toString((response.getEntity())));
    }

    @Test
    public void httpGet() throws Exception{
        HttpHelper httpHelper = new TwitterHttpHelper();
        HttpResponse response = httpHelper.httpGet(new URI("https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=BillGates"));
        System.out.println(EntityUtils.toString((response.getEntity())));
    }
}