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
import java.awt.image.BufferedImage;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Calendar;
/**
 * MainPanel
 * 
 * @author William Chern
 * @version 1.0
 */
public class MainFrame
{
    public final Font defaultUIFont = new Font("Arial", Font.PLAIN, 14);

    private JFrame fr;

    private String newTweetString;
    private JButton tweetButton;
    private JTextField newTweetTextField;

    private JLabel currentUserHandle;
    private JLabel currentUserAccountImage;
    private Twitter twitter;
    private User user;
    private URL url;

    public MainFrame() {
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
    }

    public void displayInterface() {
        fr.setSize(1100, 500);
        // fr.setResizable(false);
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
                    tweetDataList.add(new TweetData(statusList.get(i).getUser().getScreenName(), statusList.get(i).getText(), profileImg, statusList.get(i).getRetweetCount(), statusList.get(i).getFavoriteCount()));
                } catch(MalformedURLException te) {
                }
            }
        } 
        catch (TwitterException te) {
            System.out.println("COULD NOT GET TIMELINE STATUS");
        }
        
        overallP.add(centerPanel, BorderLayout.CENTER);
    }

    private void addRightPanel(JPanel overallP) {
        JPanel rightPanel = new JPanel();
    }
}
