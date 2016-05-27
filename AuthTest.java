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
import java.util.GregorianCalendar;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
/**
 * Write a description of class AuthTest here.
 * 
 * @author Nishan D'Souza
 * @version N/A
 */
public class AuthTest
{
    /*
     * This class is a means of testing OAUth, one of the hardest things we've worked with. It uses various keys to authenticate a user and allow us to do various twitter commands
     */
    public static void main(String[] args) {
        String testStatus="Hello from twitter4j";//saving various consumer and access keys, secrets, and tokens for Oauth
        String consumerKey = "LqFDdgq7SurJdoQeAtBiDmC8p";
        String consumerSecret = "GDdnMzYJyLddVFgdeXET9I0sHzQFYMGgozIrcTiJzDcTSflogo";
        String accessToken = "729714377562030082-FtbarB7pQ6BbMK8589vPpoiVgFBMV0i";
        String accessSecret = "o1GgPnihASydGh7jgMQEFncw7DQp0hGfHC9BcvpXL4A0a";
        ConfigurationBuilder cb = new ConfigurationBuilder();//constructs a configuration builder, a class which is used to configure the twitter api
        cb.setDebugEnabled(true);
        cb.setOAuthConsumerKey(consumerKey);
        cb.setOAuthConsumerSecret(consumerSecret);
        cb.setOAuthAccessToken(accessToken);
        cb.setOAuthAccessTokenSecret(accessSecret);//invokes setter methods on the ConfigurationBuilder to allow it to configure a twitter object
        TwitterFactory tf = new TwitterFactory(cb.build());//constructs a new TwitterFactory with a parameter of the return of the invoked method, .build()
        Twitter twitter = tf.getInstance();//invokes the getInstance on the TwitterFactory and sets the return to a new Twitter object referenced by twitter
        System.setProperty("twitter4j.http.useSSL", "false");//THIS THING TOOK SO LONG HARDEST PART HANDS DOWN. So apparently twitter doesn't support ssl and instead wants https
        AccessToken a = new AccessToken(accessToken, accessSecret);//I therefore needed to invoke the setProperty on the System itself to change the useSLL property to false
        twitter.setOAuthAccessToken(a);//Creates a new AccessToken object which contains the token and secret, and invokes the setOAuthAccessToken method to allow our twitter object to do commands
        
        GregorianCalendar calendar = new GregorianCalendar();//constructs a new GregorianCalendar object with the reference calendar
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);//user getter methods from the calendar to obtain current time
        int second = calendar.get(Calendar.SECOND);
        try {
            Status status = twitter.updateStatus("" + " " + hour + ":" + minute + ":" + second);//invokes the updateStatus on the twitter object to make the user post a tweet, with a given status
            System.out.println("Successfully updated the status to [" + status.getText() + "].");
        }
        catch(TwitterException te) {//everything I do with Twitter4J may crash, so try and catch statements are needed to make sure the whole program doesn't break
            System.out.println("FAIL!");
        }
    }
}
