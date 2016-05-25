/*
 * username - nishand@gmail.com or Twit4Java (email preferred)
 * password - nishanandchern (yeah i know, very unique)
 */
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ImageIcon;

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
    public final Font defaultUIFont = new Font("Arial", Font.PLAIN, 14);
    public final Font defaultUIFontBold = new Font("Arial", Font.BOLD, 15);

    private JFrame fr;

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

    public MainFrame() {
        favoritePic = createImageIcon("heart_button_default.png");
        retweet = createImageIcon("retweet_button_default.png");
        fr = new JFrame("Twit4Java");
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

        ImageIcon img = new ImageIcon(url);
        currentUserAccountImage.setIcon(img);
        newTweetTextField = new JTextField();
        currentUserHandle = new JLabel("@Twit4Java");
        tweetButton = new JButton("Tweet");

        profileViewUserAccountImage = new JLabel();
        profileViewUserHandle = new JLabel("@user");
        profileViewNumTweetsLabel = new JLabel("[X] tweets");
        profileViewNumFollowersLabel = new JLabel("[X] followers");
        profileViewNumFollowingLabel = new JLabel("[X] following");
    }

    public void displayInterface() {
        fr.setSize(1200, 650);
        fr.setResizable(false);
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel overallPanel = new JPanel(new BorderLayout());

        addLeftPanel(overallPanel);
        addCenterPanel(overallPanel);
        addRightPanel(overallPanel);

        fr.add(overallPanel);
        fr.setVisible(true);
    }

    private void addLeftPanel(JPanel overallP) {
        JPanel leftPanel = new JPanel(new GridLayout(8, 1));

        newTweetTextField.setFont(defaultUIFont);
        tweetButton.setFont(new Font("Arial", Font.BOLD, 16));

        leftPanel.add(newTweetTextField);
        leftPanel.add(tweetButton);

        JPanel currentUserAccountPanel = new JPanel(new BorderLayout(2, 2));
        currentUserHandle.setFont(defaultUIFontBold);
        currentUserAccountPanel.add(currentUserHandle, BorderLayout.EAST);
        currentUserAccountPanel.add(currentUserAccountImage, BorderLayout.WEST);

        leftPanel.add(currentUserAccountPanel);

        class TweetButtonListener implements ActionListener
        {
            public void actionPerformed(ActionEvent e) {
                newTweetString = newTweetTextField.getText();

                if ((newTweetString==null) || (newTweetString.length() > 131)) {
                    // System.out.println("Tweet cannot exceed 140 characters!");

                    JOptionPane characterLengthWarningPane = new JOptionPane();
                    characterLengthWarningPane.showMessageDialog(null, "Tweet cannot exceed 140 characters!", "Tweet Length", JOptionPane.WARNING_MESSAGE);
                }
                else {
                    GregorianCalendar calendar = new GregorianCalendar();
                    int hour = calendar.get(Calendar.HOUR);
                    int minute = calendar.get(Calendar.MINUTE);
                    int second = calendar.get(Calendar.SECOND);
                    try {
                        Status status = twitter.updateStatus(newTweetString + " " + hour + ":" + minute + ":" + second);
                        newTweetTextField.setText("");
                    }
                    catch (TwitterException te) {}
                }
            }
        }

        tweetButton.addActionListener(new TweetButtonListener());

        overallP.add(leftPanel, BorderLayout.WEST);
    }

    private void addCenterPanel(JPanel overallP) {
        JPanel centerPanel = new JPanel(new GridLayout(5, 1));
        ResponseList<Status> statusList;
        ArrayList<TweetData> tweetDataList = new ArrayList<TweetData>();
        try {
            statusList = twitter.getHomeTimeline();
            // System.out.println("@" + statusList.get(0).getUser().getScreenName() + " — " + statusList.get(0).getText());

            for (int i=0; i<5; i++) {
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
                displayTweet(tweetDataList.get(i), centerPanel);
            }
        } 
        catch (TwitterException te) {
            System.out.println("COULD NOT GET TIMELINE STATUS");
        }
        overallP.add(centerPanel, BorderLayout.CENTER);
    }

    private void addRightPanel(JPanel overallP) {
        JPanel rightPanel = new JPanel(new GridLayout(8, 1));

        rightPanel.add(profileViewUserAccountImage);
        rightPanel.add(profileViewUserHandle);
        rightPanel.add(profileViewNumTweetsLabel);
        rightPanel.add(profileViewNumFollowersLabel);
        rightPanel.add(profileViewNumFollowingLabel);

        overallP.add(rightPanel, BorderLayout.EAST);
    }

    private void displayTweet(TweetData t, JPanel p) {
        JPanel tweetPanel = new JPanel(new GridBagLayout());
        JPanel tweetTextPanel = new JPanel(new GridLayout(3, 1));
        GridBagConstraints gbc = new GridBagConstraints();
        JLabel userIconImage = new JLabel();
        ImageIcon uImageIcon = t.getUserIcon();
        userIconImage.setIcon(uImageIcon);

        JLabel userHandleLabel = new JLabel(t.getUserHandle());
        userHandleLabel.setFont(defaultUIFontBold);

        int spaces2add = 110 - (t.getTweetText().length());
        String tweetText = t.getTweetText();
        if(spaces2add>30) {
            for(int i = 0; i < spaces2add; i++) {
                tweetText = tweetText + " ";
            }
        }

        JLabel tweetTextLabel = new JLabel(tweetText);
        tweetTextLabel.setFont(defaultUIFont);
        tweetTextPanel.add(userHandleLabel);
        tweetTextPanel.add(tweetTextLabel);

        class RetweetListener implements MouseListener {
            public void mouseClicked (MouseEvent e) {
            }

            public void mouseEntered (MouseEvent e) {
            }

            public void mouseExited (MouseEvent e) {
            }

            public void mousePressed (MouseEvent e) {
                // retweet logic
                try {
                twitter.retweetStatus(t.getID());
                System.out.println("Successfully rt'd");
            } catch(TwitterException te) {}
            }

            public void mouseReleased (MouseEvent e) {
            }
        }

        class FavoriteListener implements MouseListener {
            public void mouseClicked (MouseEvent e) {
            }

            public void mouseEntered (MouseEvent e) {
            }

            public void mouseExited (MouseEvent e) {
            }

            public void mousePressed (MouseEvent e) {
                // favorite logic
                try {
                    Status rT = twitter.createFavorite(t.getID());
                    System.out.println("Successfully fav'd");
                } catch(TwitterException te) {}
            }

            public void mouseReleased (MouseEvent e) {
            }
        }

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
            public void mouseClicked (MouseEvent e) {
            }

            public void mouseEntered (MouseEvent e) {
            }

            public void mouseExited (MouseEvent e) {
            }

            public void mousePressed (MouseEvent e) {
                profileViewUserAccountImage.setIcon(t.getUserIcon());
                profileViewUserHandle.setText(t.getUserHandle());

                profileViewNumTweetsLabel.setText(t.getUserNumTweets() + " Tweets");
                profileViewNumFollowersLabel.setText(t.getUserFollowers() + " Followers");
                profileViewNumFollowingLabel.setText(t.getUserFollowing() + " Following");
            }

            public void mouseReleased (MouseEvent e) {
            }
        }

        userHandleLabel.addMouseListener(new UserLabelListener());
    }

    public ImageIcon createImageIcon(String path) {
        URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
