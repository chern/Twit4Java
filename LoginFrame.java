import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.ImageIcon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * LoginFrame
 * Window for logging in to Twitter
 * 
 * @author William Chern
 * @version 1.0, 5/3/2016
 */
public class LoginFrame
{
    // instance fields
    
    private JFrame fr;
    
    private Font defaultUIFont;
    
    private JLabel loginTitle;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    
    public LoginFrame () {
        fr = new JFrame("Sign in to Twitter");
        loginTitle = new JLabel("Sign in to Twitter");
        usernameField = new JTextField("email");
        passwordField = new JPasswordField("password");
        loginButton = new JButton("Sign in");
        
        defaultUIFont = new Font("Arial", Font.PLAIN, 14);
    }
    
    public void displayLoginWindow() {
        fr.setSize(300, 225);
        fr.setResizable(false); // invoke setResizable mutator method on JFrame object referenced by fr
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // invoke setDefaultCloseOperation mutator method on JFrame object referenced by fr
        
        JPanel mainPanel = new JPanel (new GridLayout(4, 1));
        
        loginTitle.setHorizontalAlignment(JLabel.CENTER);
        loginTitle.setFont(new Font("Arial", Font.BOLD, 24));
        loginTitle.setForeground(new Color(0, 149, 255));
        
        usernameField.setFont(defaultUIFont);
        passwordField.setFont(defaultUIFont);
        loginButton.setFont(defaultUIFont);
        
        mainPanel.add(loginTitle);
        mainPanel.add(usernameField);
        mainPanel.add(passwordField);
        mainPanel.add(loginButton);
        
        fr.add(mainPanel);
        fr.setVisible(true);
    }
    
    private void loginButtonPressed () {
    }
}
