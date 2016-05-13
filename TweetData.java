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
    
    public TweetData(String u, String t, ImageIcon i, int r, int f) {
        userHandle = u;
        tweetText = t;
        userIcon = i;
        retweetCount = r;
        favoriteCount = f;
    }
    
    public String getUser() {
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
