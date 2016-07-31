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
import java.awt.Insets;
/*
 * username - nishand@gmail.com or Twit4Java (email preferred)
 * 
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
    private ImageIcon favoriteHover;
    private ImageIcon retweet;
    private ImageIcon retweetHover;

    private JLabel profileViewUserAccountImage;
    private JLabel profileViewUserHandle;
    private JLabel profileViewNumTweetsLabel;
    private JLabel profileViewNumFollowersLabel;
    private JLabel profileViewNumFollowingLabel;
    
    private JLabel profileTweet1;
    private JLabel profileTweet2;
    private JLabel profileTweet3;
    private JLabel profileTweet4;
    private JLabel tweetTitleLabel;
    
    private JPanel rightPanel;
    public MainFrame () {
        favoritePic = createImageIcon("heart_button_default.png"); // invoke createImageIcon method (defined below) to assign ImageIcon object reference from the path
        favoriteHover = createImageIcon("heart_button_hover.png");
        retweet = createImageIcon("retweet_button_default.png");
        retweetHover = createImageIcon("retweet_button_hover.png");
        fr = new JFrame("Twit4Java"); // initialize JFrame object reference

        // OAuth authentication
        String consumerKey = "LqFDdgq7SurJdoQeAtBiDmC8p";//saving various consumer and access keys, secrets, and tokens for Oauth
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
        twitter = tf.getInstance();//invokes the getInstance on the TwitterFactory and sets the return to a new Twitter object referenced by twitter
        System.setProperty("twitter4j.http.useSSL", "false");//THIS THING TOOK SO LONG HARDEST PART HANDS DOWN. So apparently twitter doesn't support ssl and instead wants https
        AccessToken a = new AccessToken(accessToken, accessSecret);//I therefore needed to invoke the setProperty on the System itself to change the useSLL property to false
        twitter.setOAuthAccessToken(a);//Creates a new AccessToken object which contains the token and secret, and invokes the setOAuthAccessToken method to allow our twitter object to do commands

        newTweetString = "";

        currentUserAccountImage = new JLabel();
        try {
            user = twitter.showUser(twitter.getId());//gets the current user
        }
        catch (TwitterException te) {}

        String u = user.getProfileImageURL();
        try {
            url = new URL(u);//gets the url for the user's profile pic
        }
        catch (MalformedURLException mu){}

        overallPanel = new JPanel(new BorderLayout());

        currentUserAccountImage.setIcon(new ImageIcon(url));//invokes setIcon with new image icon from the url
        newTweetTextField = new JTextField();
        currentUserHandle = new JLabel("@Twit4Java");
        tweetButton = new JButton("Tweet");

        profileViewUserAccountImage = new JLabel();
        profileViewUserHandle = new JLabel("@user");
        profileViewNumTweetsLabel = new JLabel("[X] tweets");
        profileViewNumFollowersLabel = new JLabel("[X] followers");
        profileViewNumFollowingLabel = new JLabel("[X] following");
        profileTweet1 = new JLabel("");
        profileTweet2 = new JLabel("");
        profileTweet3 = new JLabel("");
        profileTweet4 = new JLabel("");
        tweetTitleLabel = new JLabel("User Tweets:");
    }

    public void displayInterface () {
        fr.setSize(1300, 650); // invoke setSize mutator method on JFrame object referenced by fr
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
        
        JPanel botHeadingPanel = new JPanel(new GridLayout(1,2));
        JLabel botHeadingLabel = new JLabel("T4J Bot");
        JLabel botHeadingHelpPanel = new JLabel();
        
        JTextField botText = new JTextField();
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
        
        
        /*
         * Bot Outline
         * 1. Create UI (Similar to tweet functionality above)
         * 2. Add a help button w/instructions
         * 3. Create series of switches to check if a real instruction is given
         * 4. Implement logic into each switch statement
         */
    }

    private void addCenterPanel () {
        JPanel centerPanel = new JPanel(new GridBagLayout()); // declare and initialize new centerPanel object reference
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
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
                    long userID = statusList.get(i).getUser().getId();
                    tweetDataList.add(new TweetData("@" + statusList.get(i).getUser().getScreenName(), statusList.get(i).getText(), profileImg, statusList.get(i).getRetweetCount(), statusList.get(i).getFavoriteCount(), statusList.get(i).getUser().getStatusesCount(), statusList.get(i).getUser().getFollowersCount(), statusList.get(i).getUser().getFriendsCount(), tweetID, userID));
                } catch(MalformedURLException te) {
                }
            }

            for (int i=0; i<5; i++) {
                switch(i) {
                    case 0:
                    constraints.anchor = GridBagConstraints.WEST;
                    constraints.weightx = 1;
                    constraints.gridx = 0;
                    constraints.gridy = 0;
                    break;
                    case 1:
                    constraints.anchor = GridBagConstraints.WEST;
                    constraints.weightx = 1;
                    constraints.gridx = 0;
                    constraints.gridy = 1;
                    break;
                    case 2:
                    constraints.anchor = GridBagConstraints.WEST;
                    constraints.weightx = 1;
                    constraints.gridx = 0;
                    constraints.gridy = 2;
                    break;
                    case 3:
                    constraints.anchor = GridBagConstraints.WEST;
                    constraints.weightx = 1;
                    constraints.gridx = 0;
                    constraints.gridy = 3;
                    break;
                    case 4:
                    constraints.anchor = GridBagConstraints.WEST;
                    constraints.weightx = 1;
                    constraints.gridx = 0;
                    constraints.gridy = 4;
                    break;
                    default: break;
                }
                constraints.anchor = GridBagConstraints.WEST;
                constraints.weightx = 1;
                constraints.insets = new Insets(30,30,30,30);
                displayTweet(tweetDataList.get(i), centerPanel, constraints, rightPanel); // invoke displayTweet method to display Tweet and its data and add to center Timeline panel
            }
        } 
        catch (TwitterException te) {
            System.out.println("COULD NOT GET TIMELINE STATUS");
        }
        overallPanel.add(centerPanel, BorderLayout.CENTER); // add centerPanel to the overall panel in the center
    }

    private void addRightPanel () {
        rightPanel = new JPanel(new GridBagLayout()); // declare and initialize new rightPanel object reference
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1;
        gbc.insets = new Insets(12, 12, 12, 12);
        
        // set font to default UI fonts
        profileViewUserHandle.setFont(defaultUIFontBold);
        profileViewNumTweetsLabel.setFont(defaultUIFont);
        profileViewNumFollowersLabel.setFont(defaultUIFont);
        profileViewNumFollowingLabel.setFont(defaultUIFont);
        tweetTitleLabel.setFont(defaultUIFont);
        profileTweet1.setFont(defaultUIFont);
        profileTweet2.setFont(defaultUIFont);
        profileTweet3.setFont(defaultUIFont);
        profileTweet4.setFont(defaultUIFont);
        
        // add profileView components to rightPanel
        gbc.gridx = 0; 
        gbc.gridy = 0;
        rightPanel.add(profileViewUserAccountImage, gbc);
        gbc.gridx = 0; 
        gbc.gridy = 1;
        rightPanel.add(profileViewUserHandle, gbc);
        gbc.gridx = 0; 
        gbc.gridy = 2;
        rightPanel.add(profileViewNumTweetsLabel, gbc);
        gbc.gridx = 0; 
        gbc.gridy = 3;
        rightPanel.add(profileViewNumFollowersLabel, gbc);
        gbc.gridx = 0; 
        gbc.gridy = 4;
        rightPanel.add(profileViewNumFollowingLabel, gbc);
        gbc.gridx = 0; 
        gbc.gridy = 5;
        rightPanel.add(tweetTitleLabel, gbc);
        gbc.gridx = 0; 
        gbc.gridy = 6;
        rightPanel.add(profileTweet1, gbc);
        gbc.gridx = 0; 
        gbc.gridy = 7;
        rightPanel.add(profileTweet2, gbc);
        gbc.gridx = 0; 
        gbc.gridy = 8;
        rightPanel.add(profileTweet3, gbc);
        gbc.gridx = 0; 
        gbc.gridy = 9;
        rightPanel.add(profileTweet4, gbc);
        
        rightPanel.setBorder(new TitledBorder(new EtchedBorder(), "Profile")); // set border of entire right panel, make it visible (etched)
        overallPanel.add(rightPanel, BorderLayout.EAST); // add rightPanel to overallPanel on the right (east) side
    }

    private void displayTweet(TweetData t, JPanel p, GridBagConstraints constraints, JPanel profilePanel) {
        JPanel tweetPanel = new JPanel(new GridBagLayout()); // declare and initialize new JPanel to hold components for tweet and its data
        JPanel tweetTextPanel = new JPanel(new GridBagLayout()); // declare and initialize new JPanel specifically to hold text and username of Tweet
        GridBagConstraints textConstraints = new GridBagConstraints();
        textConstraints.anchor = GridBagConstraints.WEST;
        textConstraints.weightx = 1;
        GridBagConstraints gbc = new GridBagConstraints();//in order to lay the tweets correctly, we use GridBagLayout to fix spacing between text and images

        JLabel userIconImage = new JLabel(); // declare and initialize new JLabel to hold account avatar of user
        ImageIcon uImageIcon = t.getUserIcon();
        userIconImage.setIcon(uImageIcon);

        JLabel userHandleLabel = new JLabel(t.getUserHandle()); // declare and initialize new JLabel to display user's handle
        userHandleLabel.setFont(defaultUIFontBold);

        int spaces2add = 110 - (t.getTweetText().length());
        String tweetText = t.getTweetText();    
        boolean secondLine = false;
        String secondText = "";
        if(tweetText.length() > 120) {
            secondText = tweetText.substring(105);
            tweetText = tweetText.substring(0,105);
            secondLine = true;
        }
        else secondLine = false;

        JLabel tweetTextLabel = new JLabel(tweetText); // declare and initialize new JLabel with tweetText passed in as a parameter in the JLabel constructor
        tweetTextLabel.setFont(defaultUIFont);
        JLabel secondTextLabel = new JLabel(secondText);
        secondTextLabel.setFont(defaultUIFont);
        // add user handle and tweet text itself to the tweetTextPanel within a panel
        textConstraints.gridx = 0;
        textConstraints.gridy = 0;
        tweetTextPanel.add(userHandleLabel, textConstraints);
        textConstraints.gridx = 0;
        textConstraints.gridy = 1;
        tweetTextPanel.add(tweetTextLabel, textConstraints);
        if(secondLine == true) {
            textConstraints.gridx = 0;
            textConstraints.gridy = 2;
            tweetTextPanel.add(secondTextLabel, textConstraints);
        }

        // Retweet and Favorite buttons: declare, initialize, set icons, and add to dedicated actionPanel
        JLabel retweetLabel = new JLabel();
        retweetLabel.setIcon(retweet);

        class RetweetListener implements MouseListener {
            // implementations of mouseClicked, mouseEntered, mouseExited, mousePressed, mouseReleased methods defined in MouseListener interface
            public void mouseClicked (MouseEvent e) {
            }

            public void mouseEntered (MouseEvent e) {
                retweetLabel.setIcon(retweetHover);
            }

            public void mouseExited (MouseEvent e) {
                retweetLabel.setIcon(retweet);
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

        JLabel favoriteLabel = new JLabel();
        favoriteLabel.setIcon(favoritePic);

        class FavoriteListener implements MouseListener {
            // implementations of mouseClicked, mouseEntered, mouseExited, mousePressed, mouseReleased methods defined in MouseListener interface
            public void mouseClicked (MouseEvent e) {
            }

            public void mouseEntered (MouseEvent e) {
                favoriteLabel.setIcon(favoriteHover);
            }

            public void mouseExited (MouseEvent e) {
                favoriteLabel.setIcon(favoritePic);
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

        retweetLabel.addMouseListener(new RetweetListener());
        favoriteLabel.addMouseListener(new FavoriteListener());

        JPanel actionPanel = new JPanel(new GridBagLayout());
        GridBagConstraints actionConstraints = new GridBagConstraints();
        actionConstraints.anchor = GridBagConstraints.WEST;
        actionConstraints.gridx = 0;
        actionConstraints.gridy = 0;
        actionPanel.add(favoriteLabel, actionConstraints);
        actionConstraints.gridx = 1;
        actionConstraints.gridy = 0;
        actionPanel.add(retweetLabel, actionConstraints);

        if(secondLine == true) {
            textConstraints.gridx = 0;
            textConstraints.gridy = 3;
        }
        else {
            textConstraints.gridx = 0;
            textConstraints.gridy = 2;
        }
        tweetTextPanel.add(actionPanel, textConstraints);

        gbc.anchor = GridBagConstraints.WEST;//using GridBagContraints values, anchors the text and images in the Layout
        gbc.gridx = 0;//sets the x and y of the grid to 0,0 the upper left corner
        gbc.gridy = 0;
        tweetPanel.add(userIconImage, gbc);//adds profile pic to the position in the gridbag layout

        gbc.gridx = 1;//sets the x and y to 1,0 in the upper right corner
        gbc.gridy = 0;
        gbc.gridwidth = 4;//sets the width of this to 4, 4 times that of the picture
        tweetPanel.add(tweetTextPanel, gbc);//adds the text for the tweet in the upper right in the gridbaglayout
        p.add(tweetPanel, constraints);

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
                profileViewNumTweetsLabel.setText(t.getUserNumTweets() + " Tweets");//uses various accessor methods of tweetData to set the text of the labels to those values
                profileViewNumFollowersLabel.setText(t.getUserFollowers() + " Followers");
                profileViewNumFollowingLabel.setText(t.getUserFollowing() + " Following");

                ResponseList<Status> userTweets;
                ArrayList<TweetData> userTweetDataList = new ArrayList<TweetData>();
                try {
                    userTweets = twitter.getUserTimeline(t.getUserID());
                    for(int i = 0; i < 4; i++) {
                        String text = userTweets.get(i).getText();
                        userTweetDataList.add(new TweetData(t.getUserHandle(), text, t.getUserIcon(), userTweets.get(i).getRetweetCount(), userTweets.get(i).getFavoriteCount(), t.getUserNumTweets(), t.getUserFollowers(), t.getUserFollowing(), t.getID(), t.getUserID()));
                    }
                } catch(TwitterException te) {}

                for(int i = 0; i < 4; i++) {
                    switch(i) {
                        case 0: displayUserTweets(userTweetDataList.get(0), profileTweet1);
                        case 1: displayUserTweets(userTweetDataList.get(1), profileTweet2);
                        case 2: displayUserTweets(userTweetDataList.get(2), profileTweet3);
                        case 3: displayUserTweets(userTweetDataList.get(3), profileTweet4);
                        default: break;
                    }
                }
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
        URL imgURL = getClass().getResource(path);//constructs a url using the path parameter variable
        if (imgURL != null) {
            return new ImageIcon(imgURL);//if the image is not null, a newly contructed ImageIcon with the url as the parameter is returned
        } else {
            System.err.println("Could not find file: " + path);
            return null;
        }
    }

    public void displayUserTweets(TweetData t, JLabel tweetTextLabel) {
        String text = t.getTweetText();
        String shortText = " '" + text.substring(0,35) + "'";
        tweetTextLabel.setText(shortText);

        class tweetTextListener implements MouseListener {
            public void mouseClicked (MouseEvent e) {
            }
            public void mouseEntered (MouseEvent e) {
            }
            public void mouseExited (MouseEvent e) {
            }
            public void mousePressed (MouseEvent e) {
                JFrame tweetFr = new JFrame();
                tweetFr.setSize(825, 100);
                
                JPanel completeTweetPanel = new JPanel(new GridBagLayout());
                GridBagConstraints completeGBC = new GridBagConstraints();
                completeGBC.anchor = GridBagConstraints.WEST;
                completeGBC.weightx = 1;
                completeGBC.insets = new Insets(0,0,0,0);
                
                JLabel userIcon = new JLabel();
                userIcon.setIcon(t.getUserIcon());
                completeGBC.gridx = 0;
                completeGBC.gridy = 0;
                completeTweetPanel.add(userIcon, completeGBC);
                
                JPanel tweetP = new JPanel(new GridBagLayout());
                GridBagConstraints bigPC = new GridBagConstraints();
                bigPC.anchor = GridBagConstraints.WEST;
                bigPC.weightx = 1;
                
                JLabel handleLabel = new JLabel(t.getUserHandle());
                handleLabel.setFont(defaultUIFontBold);
                JLabel textLabel = new JLabel(t.getTweetText());
                textLabel.setFont(defaultUIFont);
                
                JPanel buttonPanel = new JPanel(new GridBagLayout());
                GridBagConstraints buttonC = new GridBagConstraints();
                buttonC.anchor = GridBagConstraints.WEST;
                buttonC.weightx = 1;
                
                ImageIcon favoriteIcon = createImageIcon("heart_button_default.png");
                JLabel favoriteLabel = new JLabel();
                favoriteLabel.setIcon(favoriteIcon);
                buttonC.gridx = 0;
                buttonC.gridy = 0;
                buttonPanel.add(favoriteLabel, buttonC);
                ImageIcon retweetIcon = createImageIcon("retweet_button_default.png");
                JLabel retweetLabel = new JLabel();
                retweetLabel.setIcon(retweetIcon);
                buttonC.gridx = 1;
                buttonC.gridy = 0;
                buttonPanel.add(retweetLabel, buttonC);
                                
                bigPC.gridx = 0;
                bigPC.gridy = 0;
                tweetP.add(handleLabel, bigPC);
                bigPC.gridx = 0;
                bigPC.gridy = 1;
                tweetP.add(textLabel, bigPC);
                bigPC.gridx = 0;
                bigPC.gridy = 2;

                class retweetLabelListener implements MouseListener {
                    public void mouseClicked (MouseEvent e) {
                    }
                    public void mouseEntered (MouseEvent e) {
                        retweetLabel.setIcon(retweetHover);
                    }
                    public void mouseExited (MouseEvent e) {
                        retweetLabel.setIcon(retweet);
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
                retweetLabel.addMouseListener(new retweetLabelListener());
               
                class favoriteLabelListener implements MouseListener {
                    // implementations of mouseClicked, mouseEntered, mouseExited, mousePressed, mouseReleased methods defined in MouseListener interface
                    public void mouseClicked (MouseEvent e) {
                    }
                    public void mouseEntered (MouseEvent e) {
                        favoriteLabel.setIcon(favoriteHover);
                    }
                    public void mouseExited (MouseEvent e) {
                        favoriteLabel.setIcon(favoritePic);
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
                favoriteLabel.addMouseListener(new favoriteLabelListener());
                
                tweetP.add(buttonPanel, bigPC);
                
                completeGBC.gridx = 1;
                completeGBC.gridy = 0;
                completeTweetPanel.add(tweetP, completeGBC);
                
                tweetFr.add(completeTweetPanel);
                completeTweetPanel.setBorder(new TitledBorder(new EtchedBorder(), t.getUserHandle() + "'s tweet"));
                tweetFr.setVisible(true);
            }
            public void mouseReleased (MouseEvent e) {
            }
        }
        tweetTextLabel.addMouseListener(new tweetTextListener());
        
    }
}
