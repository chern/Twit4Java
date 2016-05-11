import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.conf.Configuration;
import twitter4j.media.ImageUpload;
import twitter4j.media.ImageUploadFactory;
import twitter4j.media.MediaProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Write a description of class AuthTest here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class AuthTest
{
    public static void main(String[] args) {
        String testStatus="Hello from twitter4j";
        String consumerKey = "LqFDdgq7SurJdoQeAtBiDmC8p";
        String consumerSecret = "GDdnMzYJyLddVFgdeXET9I0sHzQFYMGgozIrcTiJzDcTSflogo";
        String accessToken = "729714377562030082-FtbarB7pQ6BbMK8589vPpoiVgFBMV0i";
        String accessSecret = "o1GgPnihASydGh7jgMQEFncw7DQp0hGfHC9BcvpXL4A0a";
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);
        cb.setOAuthConsumerKey(consumerKey);
        cb.setOAuthConsumerSecret(consumerSecret);
        cb.setOAuthAccessToken(accessToken);
        cb.setOAuthAccessTokenSecret(accessSecret);
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        System.setProperty("twitter4j.http.useSSL", "false");
        AccessToken a = new AccessToken(accessToken, accessSecret);
        twitter.setOAuthAccessToken(a);
        try {
            Status status = twitter.updateStatus("HI");
            System.out.println("Successfully updated the status to [" + status.getText() + "].");
        }
        catch(TwitterException te) {
            System.out.println("FAIL!");
            te.printStackTrace();
        }
    }
}
