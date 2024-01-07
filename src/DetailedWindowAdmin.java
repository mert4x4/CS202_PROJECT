import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class DetailedWindowAdmin {

    private JFrame window;
    private int userID;
    private DataHandler handler;
    private JLabel infoLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JComboBox<String> accountTypeComboBox;
    private JComboBox<String> departmentComboBox;
    private JTextField usernameTextField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JList<String> arrayListDisplay;
    private JButton showStatsButton;

    public DetailedWindowAdmin() {
        handler = new DataHandler();
    }

    public void createWindow(int userID) {
        this.userID = userID;

        window = new JFrame();
        window.setSize(1280, 720);

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
        accountTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show departmentComboBox only when "Doctor" is selected
                String selectedAccountType = (String) accountTypeComboBox.getSelectedItem();
                departmentComboBox.setVisible("Doctor".equals(selectedAccountType));
            }
        });

        // Create and set up the JComboBox for department selection
        String[] departments = {"Ortopedi", "Radyoloji", "KBB"};
        departmentComboBox = new JComboBox<>(departments);
        departmentComboBox.setBounds(250, 100, 150, 30);
        departmentComboBox.setVisible(false);

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



        showStatsButton = new JButton("Show Stats");
        showStatsButton.setBounds(400, 250, 100, 30);
        showStatsButton.addActionListener(e -> showAllStats());
        showStatsButton.setVisible(true);

        // Create and set up the JList
        arrayListDisplay = new JList<>();
        arrayListDisplay.setBounds(50, 300, 900, 700);
        arrayListDisplay.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        window.setLayout(null);
        window.setVisible(true);

        window.add(infoLabel);
        window.add(accountTypeComboBox);
        window.add(departmentComboBox);
        window.add(usernameLabel);
        window.add(usernameTextField);
        window.add(passwordLabel);
        window.add(passwordField);
        window.add(registerButton);
        window.add(arrayListDisplay);
        window.add(showStatsButton);

        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void registerButtonClicked() {
        String selectedAccountType = (String) accountTypeComboBox.getSelectedItem();
        String username = usernameTextField.getText();
        String password = new String(passwordField.getPassword());
        String selectedDepartment = (String) departmentComboBox.getSelectedItem();

        if (!(usernameTextField.getText().trim().isEmpty() || passwordField.getText().trim().isEmpty())) {
            if (selectedAccountType != null && username != null && password != null) {
                try {
                    if ("Doctor".equals(selectedAccountType)) {
                        try {
                            int userID = handler.register(username, password, selectedAccountType);
                            handler.registerDoctorAndMakeAvailable(selectedDepartment, userID);
                            JOptionPane.showMessageDialog(window, "Registered!");
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(window, "Error: " + "there is already an account with this username", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    if ("Nurse".equals(selectedAccountType)) {
                        try {
                            int userID = handler.register(username, password, selectedAccountType);
                            handler.registerNurseAndMakeAvailable(userID);
                            JOptionPane.showMessageDialog(window, "Registered!");
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(window, "Error: " + "there is already an account with this username", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(window, "Error: " + "register error", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(window, "ERROR!", "username or password cannot be empty", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(window, "Error: " + "you need a username and password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void showAllStats(){
        ArrayList<String> displayList = new ArrayList<>();
        displayList.add("Patients stats -> date interval is 2020-12-20 -- 2025-12-20");
        for (String data : handler.getPatientStatsByDept()) {
            displayList.add(data);
        }
        displayList.add("------------------------------------------------------------");

        displayList.add("Rooms booked to Appointment ratio for each department.");
        for (String data : handler.getRoomBookingRatioByDepartment()) {
            displayList.add(data);
        }
        displayList.add("------------------------------------------------------------");

        displayList.add("# of assigned unique nurses to rooms booked ratio for each department.");
        for (String data : handler.getDepartmentNurseRoomRatio()) {
            displayList.add(data);
        }


        arrayListDisplay.setListData(displayList.toArray(new String[0]));
    }






}
