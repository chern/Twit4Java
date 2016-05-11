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
 * ComposeTweetFrame
 * 
 * @author William Chern
 * @version 5/10/2016
 */
public class ComposeTweetFrame
{
    // instance fields
    private JFrame fr;
    private JPanel jp;
    private Font defaultUIFont;
    private JTextField newTweetTextField;
    private JButton tweetButton;
    private String tweetString;
    
    public ComposeTweetFrame() {
        // initialize instance fields
        fr = new JFrame("Compose Tweet");
        newTweetTextField = new JTextField();
        tweetButton = new JButton("Tweet");
        defaultUIFont = new Font("Arial", Font.PLAIN, 14);
    }
    
    public void displayTweetWindow() {
        fr.setSize(300, 120); // invoke setSize mutator method on JFrame object reference by fr, set window dimensions to 300 x 120 px
        fr.setResizable(false); // do not allow resizing of window.
        
        JPanel mainPanel = new JPanel(new GridLayout(2, 1));
        
        newTweetTextField.setFont(defaultUIFont);
        tweetButton.setFont(new Font("Arial", Font.BOLD, 16));
        
        mainPanel.add(newTweetTextField);
        mainPanel.add(tweetButton);
        
        class TweetButtonListener implements ActionListener
        {
            public void actionPerformed(ActionEvent e) {
                tweetString = newTweetTextField.getText();
                
                if ((tweetString==null) || (tweetString.length() > 140)) {
                    // System.out.println("Tweet cannot exceed 140 characters!");
                    
                    JOptionPane characterLengthWarningPane = new JOptionPane();
                    characterLengthWarningPane.showMessageDialog(null, "Tweet cannot exceed 140 characters!", "Tweet Length", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        
        tweetButton.addActionListener(new TweetButtonListener());
        
        fr.add(mainPanel);
        fr.setVisible(true);
    }
}
