
/**
 * Tweet
 * 
 * @author William Chern
 * @version 5/11/2016
 */
public class Tweet
{
    private String userHandle;
    private String tweetText;
    
    public Tweet(String u, String t) {
        userHandle = u;
        tweetText = t;
    }
    
    public String getUser() {
        return userHandle;
    }
    
    public String getTweetText() {
        return tweetText;
    }
}
