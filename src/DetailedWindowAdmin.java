import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;

public class DetailedWindowAdmin {

    private JFrame window;
    private int userID;
    private DataHandler handler;
    private JLabel infoLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JComboBox<String> accountTypeComboBox;
    private JTextField usernameTextField;
    private JPasswordField passwordField;
    private JButton registerButton;

    public DetailedWindowAdmin() {
        handler = new DataHandler();
    }

    public void createWindow(int userID) {
        this.userID = userID;

        window = new JFrame();
        window.setSize(800, 500);

        try {
            infoLabel = new JLabel("Account Type: " + handler.getUserTypeFromId(userID) + " AccountID: " + userID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        infoLabel.setBounds(50, 50, 800, 30);
        infoLabel.setVisible(true);

        // Create and set up the JComboBox for account type
        String[] accountTypes = {"Doctor", "Nurse", "Patient"};
        accountTypeComboBox = new JComboBox<>(accountTypes);
        accountTypeComboBox.setBounds(50, 100, 150, 30);

        // Create and set up the JTextField for username
        usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 150, 100, 30);
        usernameTextField = new JTextField();
        usernameTextField.setBounds(150, 150, 150, 30);

        // Create and set up the JPasswordField for password
        passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 200, 100, 30);
        passwordField = new JPasswordField();
        passwordField.setBounds(150, 200, 150, 30);

        // Create and set up the JButton for registration
        registerButton = new JButton("Register");
        registerButton.setBounds(50, 250, 100, 30);
        registerButton.addActionListener(e -> registerButtonClicked());

        window.setLayout(null);
        window.setVisible(true);

        window.add(infoLabel);
        window.add(accountTypeComboBox);
        window.add(usernameLabel);
        window.add(usernameTextField);
        window.add(passwordLabel);
        window.add(passwordField);
        window.add(registerButton);

        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }


    private void registerButtonClicked() {
        String selectedAccountType = (String) accountTypeComboBox.getSelectedItem();
        String username = usernameTextField.getText();
        String password = passwordField.getText();

        if (selectedAccountType != null && username != null && password != null) {
            try {
                if(selectedAccountType.equals("Doctor")){
                    try{
                        int userID = handler.register(username,password,selectedAccountType);
                        handler.registerDoctorAndMakeAvailable("kbb",userID);
                        JOptionPane.showMessageDialog(window, "Registered!");
                    }
                    catch (Exception e){
                        JOptionPane.showMessageDialog(window, "Error: " + "there is already an account with this username", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(window, "Error: " + "register error", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            throw new RuntimeException("You need to enter a username and a password");
        }
    }

}
