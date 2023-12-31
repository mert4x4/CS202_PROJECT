import javax.swing.*;
import java.awt.*;

public class WelcomeWindow {
    JFrame window;
    JButton loginButton;
    JButton registerButton;
    JTextField usernameField;
    JPasswordField passwordField;
    JLabel usernameLabel;
    JLabel passwordLabel;

    public void createWindow() {
        window = new JFrame();
        window.setVisible(true);
        window.setSize(640, 480);

        usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 50, 80, 30);

        usernameField = new JTextField();
        usernameField.setBounds(150, 50, 220, 30);

        passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 100, 80, 30);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 100, 220, 30);

        loginButton = new JButton("Login");
        loginButton.setBounds(150, 150, 100, 40);

        registerButton = new JButton("Register");
        registerButton.setBounds(270, 150, 100, 40);

        window.setLayout(null);

        window.add(usernameLabel);
        window.add(usernameField);
        window.add(passwordLabel);
        window.add(passwordField);
        window.add(loginButton);
        window.add(registerButton);
    }

}
