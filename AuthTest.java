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

        ConfigurationBuilder cb = new ConfigurationBuilder();

        //the following is set without accesstoken- desktop client
        cb.setDebugEnabled(true);
        cb.setOAuthConsumerKey("LqFDdgq7SurJdoQeAtBiDmC8p");
        cb.setOAuthConsumerSecret("GDdnMzYJyLddVFgdeXET9I0sHzQFYMGgozIrcTiJzDcTSflogo");
        cb.setOAuthAccessToken("729714377562030082-FtbarB7pQ6BbMK8589vPpoiVgFBMV0i");
        cb.setOAuthAccessTokenSecret("o1GgPnihASydGh7jgMQEFncw7DQp0hGfHC9BcvpXL4A0a");

        try {
            TwitterFactory tf = new TwitterFactory(cb.build());
            Twitter twitter = tf.getInstance();

            try {
                System.out.println("-----");

                // get request token.
                // this will throw IllegalStateException if access token is already available
                // this is oob, desktop client version
                RequestToken requestToken = twitter.getOAuthRequestToken(); 

                System.out.println("Got request token.");
                System.out.println("Request token: " + requestToken.getToken());
                System.out.println("Request token secret: " + requestToken.getTokenSecret());
                
                System.out.println("|-----");

                AccessToken accessToken = null;

                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

                while (accessToken == null) {
                    System.out.println("Open the following URL and grant access to your account:");
                    System.out.println(requestToken.getAuthorizationURL());
                    System.out.print("Enter the PIN(if available) and hit enter after you granted access.[PIN]:");
                    String pin = br.readLine();

                    try {
                        if (pin.length() > 0) {
                            accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                        } else {
                            accessToken = twitter.getOAuthAccessToken(requestToken);
                        }
                    } catch (TwitterException te) {
                        if (401 == te.getStatusCode()) {
                            System.out.println("Unable to get the access token.");
                        } else {
                            te.printStackTrace();
                        }
                    }
                }
                System.out.println("Got access token.");
                System.out.println("Access token: " + accessToken.getToken());
                System.out.println("Access token secret: " + accessToken.getTokenSecret());

            } catch (IllegalStateException ie) {
                // access token is already available, or consumer key/secret is not set.
                if (!twitter.getAuthorization().isEnabled()) {
                    System.out.println("OAuth consumer key/secret is not set.");
                    System.exit(-1);
                }
            }
            /*
            Status status = twitter.updateStatus(testStatus);

            System.out.println("Successfully updated the status to [" + status.getText() + "].");

            System.out.println("ready exit");

            System.exit(0);
            */
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());
            System.exit(-1);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Failed to read the system input.");
            System.exit(-1);
        }
    }
}
