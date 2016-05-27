import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.ResponseList;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.conf.Configuration;
import twitter4j.media.ImageUpload;
import twitter4j.media.ImageUploadFactory;
import twitter4j.media.MediaProvider;
import twitter4j.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import java.net.URL;
import java.net.MalformedURLException;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.FlowLayout;
/*
 * username - nishand@gmail.com or Twit4Java (email preferred)
 * password - nishanandchern (yeah i know, very unique)
 */
/**
 * MainPanel
 * 
 * @author William Chern & Nishan D'Souza
 * @version 1.0
 */
public class MainFrame
{
    private final Font defaultUIFont = new Font("Arial", Font.PLAIN, 14); // the default UI font (Arial) for Twit4Java
    private final Font defaultUIFontBold = new Font("Arial", Font.BOLD, 15); // the default BOLD style UI font

    // instance field variables
    private JFrame fr;
    private JPanel overallPanel;

    private String newTweetString;
    private JButton tweetButton;
    private JTextField newTweetTextField;

    private JLabel currentUserHandle;
    private JLabel currentUserAccountImage;

    private Twitter twitter;
    private User user;
    private URL url;

    private ImageIcon favoritePic;
    private ImageIcon retweet;

    private JLabel profileViewUserAccountImage;
    private JLabel profileViewUserHandle;
    private JLabel profileViewNumTweetsLabel;
    private JLabel profileViewNumFollowersLabel;
    private JLabel profileViewNumFollowingLabel;

    public MainFrame () {
        // initialization of instance field variables
        favoritePic = createImageIcon("heart_button_default.png"); // invoke createImageIcon method (defined below) to assign ImageIcon object reference from the path
        retweet = createImageIcon("retweet_button_default.png");
        fr = new JFrame("Twit4Java"); // initialize JFrame object reference

        // OAuth authentication
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
        twitter = tf.getInstance();
        System.setProperty("twitter4j.http.useSSL", "false");
        AccessToken a = new AccessToken(accessToken, accessSecret);
        twitter.setOAuthAccessToken(a);

        newTweetString = "";

        currentUserAccountImage = new JLabel();
        try {
            user = twitter.showUser(twitter.getId());
        }
        catch (TwitterException te) {}

        String u = user.getProfileImageURL();
        try {
            url = new URL(u);
        }
        catch (MalformedURLException mu){}

        overallPanel = new JPanel(new BorderLayout());

        currentUserAccountImage.setIcon(new ImageIcon(url));
        newTweetTextField = new JTextField();
        currentUserHandle = new JLabel("@Twit4Java");
        tweetButton = new JButton("Tweet");

        profileViewUserAccountImage = new JLabel();
        profileViewUserHandle = new JLabel("@user");
        profileViewNumTweetsLabel = new JLabel("[X] tweets");
        profileViewNumFollowersLabel = new JLabel("[X] followers");
        profileViewNumFollowingLabel = new JLabel("[X] following");
    }

    public void displayInterface () {
        fr.setSize(1250, 650); // invoke setSize mutator method on JFrame object referenced by fr
        fr.setResizable(false); // do not allow resizing of window
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // automatically reset JVM upon closing

        // invoke methods to add left (new Tweet), center (Timeline), and right (Profile view) panels
        addLeftPanel();
        addCenterPanel();
        addRightPanel();

        // invoke mutator methods on fr to add overallPanel and display it in the window frame
        fr.add(overallPanel);
        fr.setVisible(true);
    }

    private void addLeftPanel () {
        JPanel leftPanel = new JPanel(new GridLayout(10, 1)); // declare and initialize new leftPanel object reference

        // set font for New Tweet controls
        newTweetTextField.setFont(defaultUIFont);
        tweetButton.setFont(new Font("Arial", Font.BOLD, 16));
        // add New Tweet controls to leftPanel
        leftPanel.add(newTweetTextField);
        leftPanel.add(tweetButton);

        JPanel currentUserAccountPanel = new JPanel(new BorderLayout(2, 2)); // initialize panel for displaying information about the current user's account
        currentUserHandle.setFont(defaultUIFontBold);
        // add current user info to currentUserAccountPanel
        currentUserAccountPanel.add(currentUserHandle, BorderLayout.EAST);
        currentUserAccountPanel.add(currentUserAccountImage, BorderLayout.WEST);

        // add currentUserAccountPanel to the entire leftPanel; will be below New Tweet controls
        leftPanel.add(currentUserAccountPanel);

        class TweetButtonListener implements ActionListener
        {
            public void actionPerformed (ActionEvent e) { // implement actionPerformed() method defined in ActionListener interface
                newTweetString = newTweetTextField.getText(); // get text from the New Tweet JTextField
                if (newTweetString.length() > 131) {
                    // if user's new tweet exceeds 140 characters (after Twit4Java's added timestamp), show pop-up warning (and prevent tweeting)
                    JOptionPane characterLengthWarningPane = new JOptionPane();
                    characterLengthWarningPane.showMessageDialog(null, "Tweet cannot exceed 140 characters!", "Tweet Length", JOptionPane.WARNING_MESSAGE);
                }
                else if (newTweetString==null) {
                    // if user's new tweet is null, display a warning (and prevent tweeting)
                    JOptionPane nullTweetWarningPane = new JOptionPane();
                    nullTweetWarningPane.showMessageDialog(null, "You must enter some text to tweet!", "Tweet Length", JOptionPane.WARNING_MESSAGE);
                }
                else {
                    // Twit4Java appends a timestamp at the end of new tweet and then tweets the user's text
                    GregorianCalendar calendar = new GregorianCalendar();
                    try {
                        Status status = twitter.updateStatus(newTweetString + " " + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND));
                        newTweetTextField.setText(""); // clear the new tweet JTextField
                    }
                    catch (TwitterException te) {}
                }
            }
        }

        tweetButton.addActionListener(new TweetButtonListener()); // add new instance of TweetButtonListener to the TweetButton
        leftPanel.setBorder(new TitledBorder(new EtchedBorder())); // set border of entire left panel, make it visible (etched)
        overallPanel.add(leftPanel, BorderLayout.WEST); // add the leftPanel to the overall panel on the left (west) side
    }

    private void addCenterPanel () {
        JPanel centerPanel = new JPanel(new GridLayout(5, 1)); // declare and initialize new centerPanel object reference
        ResponseList<Status> statusList; // List to hold latest Timeline data retrieved from Twitter
        ArrayList<TweetData> tweetDataList = new ArrayList<TweetData>(); // ArrayList to hold data for the latest 5 tweets from Timeline
        try {
            statusList = twitter.getHomeTimeline(); // invoke getHomeTimeline accessor method and retreive latest Timeline tweets
            // System.out.println("@" + statusList.get(0).getUser().getScreenName() + " — " + statusList.get(0).getText());

            for (int i=0; i<5; i++) {
                // add data for the latest 5 tweets from Timeline to new instances of TweetData class
                User u = statusList.get(i).getUser();
                String strUrl = u.getProfileImageURL();
                URL picURL;
                ImageIcon profileImg;
                try{
                    picURL = new URL(strUrl);
                    profileImg = new ImageIcon(picURL);
                    long tweetID = statusList.get(i).getId();
                    tweetDataList.add(new TweetData("@" + statusList.get(i).getUser().getScreenName(), statusList.get(i).getText(), profileImg, statusList.get(i).getRetweetCount(), statusList.get(i).getFavoriteCount(), statusList.get(i).getUser().getStatusesCount(), statusList.get(i).getUser().getFollowersCount(), statusList.get(i).getUser().getFriendsCount(), tweetID));
                } catch(MalformedURLException te) {
                }
            }

            for (int i=0; i<5; i++) {
                displayTweet(tweetDataList.get(i), centerPanel); // invoke displayTweet method to display Tweet and its data and add to center Timeline panel
            }
        } 
        catch (TwitterException te) {
            System.out.println("COULD NOT GET TIMELINE STATUS");
        }
        overallPanel.add(centerPanel, BorderLayout.CENTER); // add centerPanel to the overall panel in the center
    }

    private void addRightPanel () {
        JPanel rightPanel = new JPanel(new GridLayout(8, 1)); // declare and initialize new rightPanel object reference

        // set font to default UI fonts
        profileViewUserHandle.setFont(defaultUIFontBold);
        profileViewNumTweetsLabel.setFont(defaultUIFont);
        profileViewNumFollowersLabel.setFont(defaultUIFont);
        profileViewNumFollowingLabel.setFont(defaultUIFont);

        // add profileView components to rightPanel
        rightPanel.add(profileViewUserAccountImage);
        rightPanel.add(profileViewUserHandle);
        rightPanel.add(profileViewNumTweetsLabel);
        rightPanel.add(profileViewNumFollowersLabel);
        rightPanel.add(profileViewNumFollowingLabel);

        rightPanel.setBorder(new TitledBorder(new EtchedBorder(), "Profile")); // set border of entire right panel, make it visible (etched)

        overallPanel.add(rightPanel, BorderLayout.EAST); // add rightPanel to overallPanel on the right (east) side
    }

    private void displayTweet(TweetData t, JPanel p) {
        JPanel tweetPanel = new JPanel(new GridBagLayout()); // declare and initialize new JPanel to hold components for tweet and its data
        JPanel tweetTextPanel = new JPanel(new GridLayout(3, 1)); // declare and initialize new JPanel specifically to hold text and username of Tweet
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel userIconImage = new JLabel(); // declare and initialize new JLabel to hold account avatar of user
        ImageIcon uImageIcon = t.getUserIcon();
        userIconImage.setIcon(uImageIcon);

        JLabel userHandleLabel = new JLabel(t.getUserHandle()); // declare and initialize new JLabel to display user's handle
        userHandleLabel.setFont(defaultUIFontBold);

        int spaces2add = 110 - (t.getTweetText().length());
        String tweetText = t.getTweetText();
        if(spaces2add>30) {
            for(int i = 0; i < spaces2add; i++) {
                tweetText = tweetText + " ";
            }
        }

        JLabel tweetTextLabel = new JLabel(tweetText); // declare and initialize new JLabel with tweetText passed in as a parameter in the JLabel constructor
        tweetTextLabel.setFont(defaultUIFont);
        // add user handle and tweet text itself to the tweetTextPanel within a panel
        tweetTextPanel.add(userHandleLabel);
        tweetTextPanel.add(tweetTextLabel);

        class RetweetListener implements MouseListener {
            // implementations of mouseClicked, mouseEntered, mouseExited, mousePressed, mouseReleased methods defined in MouseListener interface
            public void mouseClicked (MouseEvent e) {
            }

            public void mouseEntered (MouseEvent e) {
            }

            public void mouseExited (MouseEvent e) {
            }

            public void mousePressed (MouseEvent e) {
                // retweet logic
                try {
                    twitter.retweetStatus(t.getID()); // invoke retweetStatus on twitter object reference, pass in ID of the tweet as parameter
                    System.out.println("Successfully retweeted");
                }
                catch (TwitterException te) {}
            }

            public void mouseReleased (MouseEvent e) {
            }
        }

        class FavoriteListener implements MouseListener {
            // implementations of mouseClicked, mouseEntered, mouseExited, mousePressed, mouseReleased methods defined in MouseListener interface
            public void mouseClicked (MouseEvent e) {
            }

            public void mouseEntered (MouseEvent e) {
            }

            public void mouseExited (MouseEvent e) {
            }

            public void mousePressed (MouseEvent e) {
                // favorite logic
                try {
                    Status rT = twitter.createFavorite(t.getID()); // invoke createFavorite, pass in ID of the tweet as parameter
                    System.out.println("Successfully favorited");
                }
                catch (TwitterException te) {}
            }

            public void mouseReleased (MouseEvent e) {
            }
        }

        // Retweet and Favorite buttons: declare, initialize, set icons, and add to dedicated actionPanel
        JLabel favoriteLabel = new JLabel();
        favoriteLabel.setIcon(favoritePic);
        favoriteLabel.addMouseListener(new FavoriteListener());
        JLabel retweetLabel = new JLabel();
        retweetLabel.setIcon(retweet);
        retweetLabel.addMouseListener(new RetweetListener());
        JPanel actionPanel = new JPanel(new GridLayout(1,2));
        actionPanel.add(favoriteLabel);
        actionPanel.add(retweetLabel);
        tweetTextPanel.add(actionPanel);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        tweetPanel.add(userIconImage, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        tweetPanel.add(tweetTextPanel, gbc);
        p.add(tweetPanel);

        class UserLabelListener implements MouseListener {
            // implementations of mouseClicked, mouseEntered, mouseExited, mousePressed, mouseReleased methods defined in MouseListener interface
            public void mouseClicked (MouseEvent e) {
            }

            public void mouseEntered (MouseEvent e) {
            }

            public void mouseExited (MouseEvent e) {
            }

            public void mousePressed (MouseEvent e) {
                // when mouse is pressed on component, it will take that tweet's user and display basic info on the profile view on the right side
                // updates components of Profile view with info about user
                profileViewUserAccountImage.setIcon(t.getUserIcon());
                profileViewUserHandle.setText(t.getUserHandle());

                profileViewNumTweetsLabel.setText(t.getUserNumTweets() + " Tweets");
                profileViewNumFollowersLabel.setText(t.getUserFollowers() + " Followers");
                profileViewNumFollowingLabel.setText(t.getUserFollowing() + " Following");
            }

            public void mouseReleased (MouseEvent e) {
            }
        }

        // add a new UserLabelListener to both the userHandleLabel and userIconImage (if either is pressed, Profile view on the right will update with info about user)
        UserLabelListener uLL = new UserLabelListener();
        userHandleLabel.addMouseListener(uLL);
        userIconImage.addMouseListener(uLL);
    }

    public ImageIcon createImageIcon (String path) {
        URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Could not find file: " + path);
            return null;
        }
    }
}
