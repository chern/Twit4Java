import javax.swing.ImageIcon;

/**
 * TweetData
 * 
 * @author William Chern
 * @version 5/11/2016
 */
public class TweetData
{
    private String userHandle;
    private String tweetText;
    private ImageIcon userIcon;
    private int retweetCount;
    private int favoriteCount;
    
    private int numTweetsCount;
    private int followersCount;
    private int followingCount;
    
    public TweetData(String u, String t, ImageIcon i, int r, int f, int numTweets, int followers, int following) {
        userHandle = u;
        tweetText = t;
        userIcon = i;
        retweetCount = r;
        favoriteCount = f;
        numTweetsCount = numTweets;
        followersCount = followers;
        followingCount = following;
    }
    
    public String getUserHandle() {
        return userHandle;
    }
    
    public String getTweetText() {
        return tweetText;
    }
    
    public ImageIcon getUserIcon() {
        return userIcon;
    }
    
    public int getRetweetCount() {
        return retweetCount;
    }
    
    public int getFavoriteCount() {
        return favoriteCount;
    }
    
    
}
