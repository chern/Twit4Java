import javax.swing.ImageIcon;

/**
 * TweetData
 * 
 * @author William Chern & Nishan D'Souza
 * @version 5/11/2016
 */
public class TweetData
/*
 * This class is a means of storing twitter data in a more efficient manner, making it easy to layout things in the mainframe class
 */
{
    private String userHandle;
    private String tweetText;
    private ImageIcon userIcon;
    private int retweetCount;
    private int favoriteCount;    
    private int numTweetsCount;
    private int followersCount;
    private int followingCount;
    private long tweetID;
    // various private instance fields to hold the values of the twitter object
    public TweetData(String u, String t, ImageIcon i, int r, int f, int numTweets, int followers, int following, long id) {
        userHandle = u;
        tweetText = t;
        userIcon = i;
        retweetCount = r;
        favoriteCount = f;
        numTweetsCount = numTweets;
        followersCount = followers;
        followingCount = following;
        tweetID = id;
    }
    // various accessor methods for the TweetDate class
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
    
    public int getUserNumTweets() {
        return numTweetsCount;
    }
    
    public int getUserFollowers() {
        return followersCount;
    }
    
    public int getUserFollowing() {
        return followingCount;
    }
    public long getID() {
        return tweetID;
    }
}
