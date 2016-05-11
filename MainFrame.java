import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ImageIcon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

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
    
    public MainFrame() {
        fr = new JFrame("Twit4Java");
        
        newTweetString = "";
        newTweetTextField = new JTextField();
        tweetButton = new JButton("Tweet");
        
        currentUserAccountImage = new JLabel("img");
        currentUserHandle = new JLabel("@Twit4Java");
    }
    
    public void displayInterface() {
        fr.setSize(1100, 500);
        fr.setResizable(false);
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel overallPanel = new JPanel(new BorderLayout(3, 3));
        
        addLeftPanel(overallPanel);
        addCenterPanel(overallPanel);
        addRightPanel(overallPanel);
        
        fr.add(overallPanel);
        fr.setVisible(true);
    }
    
    private void addLeftPanel(JPanel overallP) {
        JPanel leftPanel = new JPanel(new GridLayout(3, 1));
        
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
                
                if ((newTweetString==null) || (newTweetString.length() > 140)) {
                    // System.out.println("Tweet cannot exceed 140 characters!");
                    
                    JOptionPane characterLengthWarningPane = new JOptionPane();
                    characterLengthWarningPane.showMessageDialog(null, "Tweet cannot exceed 140 characters!", "Tweet Length", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        
        tweetButton.addActionListener(new TweetButtonListener());
        
        overallP.add(leftPanel);
    }
    
    private void addCenterPanel(JPanel overallP) {
        JPanel centerPanel = new JPanel(new GridLayout(5, 1));
    }
    
    private void addRightPanel(JPanel overallP) {
    }
}
