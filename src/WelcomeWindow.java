import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



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

        window.setSize(640, 480);

        usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 50, 80, 30);
        usernameLabel.setVisible(true);

        usernameField = new JTextField();
        usernameField.setBounds(150, 50, 220, 30);
        usernameField.setVisible(true);

        passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 100, 80, 30);
        passwordLabel.setVisible(true);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 100, 220, 30);
        passwordField.setVisible(true);

        loginButton = new JButton("Login");
        loginButton.setBounds(150, 150, 100, 40);
        loginButton.setVisible(true);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DataHandler handler = new DataHandler();
                try {
                    int userId = handler.login(usernameField.getText(), passwordField.getText());
                    System.out.println(userId);
                    window.dispose();
                    if(handler.getUserTypeFromId(userId).equals("Patient")){
                        DetailedWindowPatient detailedWindowPatient = new DetailedWindowPatient();
                        detailedWindowPatient.createWindow(userId);
                    }
                    if(handler.getUserTypeFromId(userId).equals("Doctor")){
                        DetailedWindowDoctor detailedWindowDoctor = new DetailedWindowDoctor();
                        detailedWindowDoctor.createWindow(userId);
                    }
                    if(handler.getUserTypeFromId(userId).equals("Nurse")){
                        NurseWindow nurseWindow = new NurseWindow();
                        nurseWindow.createWindow(userId);
                    }
                    if(handler.getUserTypeFromId(userId).equals("Manager")){
                        DetailedWindowAdmin detailedWindowAdmin = new DetailedWindowAdmin();
                        detailedWindowAdmin.createWindow(userId);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(window, ex.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });



        registerButton = new JButton("Register");
        registerButton.setBounds(270, 150, 100, 40);
        registerButton.setVisible(true);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DataHandler handler = new DataHandler();
                try {
                    handler.register(usernameField.getText(),passwordField.getText(),"Patient"); //paswordları hashlemeyi unutmayak

                    int userId = handler.login(usernameField.getText(), passwordField.getText());
                    System.out.println(userId);
                    window.dispose();
                    DetailedWindowPatient detailedWindowPatient = new DetailedWindowPatient();
                    detailedWindowPatient.createWindow(userId);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(window, ex.getMessage(), "Register Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        window.setLayout(null);


        window.add(usernameLabel);
        window.add(usernameField);
        window.add(passwordLabel);
        window.add(passwordField);
        window.add(loginButton);
        window.add(registerButton);

        window.setVisible(true);

        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

}
