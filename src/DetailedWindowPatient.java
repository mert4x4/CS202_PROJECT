import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DetailedWindowPatient {
    JFrame window;
    JLabel infoLabel;
    JLabel expertiseLabel;
    JLabel startDateLabel;
    JLabel endDateLabel;
    JLabel startTimeLabel;
    JLabel endTimeLabel;
    JComboBox<String> expertiseComboBox;
    JSpinner startDateSpinner;
    JSpinner endDateSpinner;
    JFormattedTextField startTimeField;
    JFormattedTextField endTimeField;
    JButton printButton;
    JList<String> arrayListDisplay;

    int userID;

    DataHandler handler;

    public DetailedWindowPatient() {
        handler = new DataHandler();
    }

    public void createWindow(int userID) {
        this.userID = userID;

        window = new JFrame();
        window.setSize(640, 480);

        try {
            infoLabel = new JLabel("Account Type: " + handler.getUserTypeFromId(userID) + " AccountID: " + userID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        infoLabel.setBounds(50, 50, 800, 30);
        infoLabel.setVisible(true);

        expertiseLabel = new JLabel("Expertise:");
        expertiseLabel.setBounds(50, 100, 80, 30);
        expertiseLabel.setVisible(true);

        String[] expertiseOptions = {"Option 1", "Option 2", "Option 3"};
        expertiseComboBox = new JComboBox<>(expertiseOptions);
        expertiseComboBox.setBounds(140, 100, 150, 30);
        expertiseComboBox.setVisible(true);

        startDateLabel = new JLabel("Start Date:");
        startDateLabel.setBounds(50, 150, 80, 30);
        startDateLabel.setVisible(true);

        startDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor startDateEditor = new JSpinner.DateEditor(startDateSpinner, "dd-MMM-yyyy");
        startDateSpinner.setEditor(startDateEditor);
        startDateSpinner.setBounds(140, 150, 150, 30);
        startDateSpinner.setVisible(true);

        endDateLabel = new JLabel("End Date:");
        endDateLabel.setBounds(300, 150, 80, 30);
        endDateLabel.setVisible(true);

        endDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor endDateEditor = new JSpinner.DateEditor(endDateSpinner, "dd-MMM-yyyy");
        endDateSpinner.setEditor(endDateEditor);
        endDateSpinner.setBounds(390, 150, 150, 30);
        endDateSpinner.setVisible(true);

        startTimeLabel = new JLabel("Start Time:");
        startTimeLabel.setBounds(50, 200, 80, 30);
        startTimeLabel.setVisible(true);

        try {
            MaskFormatter startTimeFormatter = new MaskFormatter("##:##:##");
            startTimeField = new JFormattedTextField(startTimeFormatter);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        startTimeField.setBounds(140, 200, 150, 30);
        startTimeField.setVisible(true);

        endTimeLabel = new JLabel("End Time:");
        endTimeLabel.setBounds(300, 200, 80, 30);
        endTimeLabel.setVisible(true);

        try {
            MaskFormatter endTimeFormatter = new MaskFormatter("##:##:##");
            endTimeField = new JFormattedTextField(endTimeFormatter);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        endTimeField.setBounds(390, 200, 150, 30);
        endTimeField.setVisible(true);

        printButton = new JButton("Print Selected Data");
        printButton.setBounds(160, 250, 150, 30);
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printSelectedData();
            }
        });

        JButton searchByExpertiseButton = new JButton("Search by Expertise");
        searchByExpertiseButton.setBounds(160, 300, 150, 30);
        searchByExpertiseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchByExpertise();
            }
        });

        JButton searchForDateButton = new JButton("Search for Date");
        searchForDateButton.setBounds(320, 250, 150, 30);
        searchForDateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchForDate();
            }
        });

        JButton searchForDateTimeButton = new JButton("Search for Date and Time");
        searchForDateTimeButton.setBounds(320, 300, 150, 30);
        searchForDateTimeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchForDateTime();
            }
        });

        arrayListDisplay = new JList<>();
        arrayListDisplay.setBounds(50, 350, 260, 150);
        arrayListDisplay.setVisible(true);

        window.setLayout(null);
        window.add(infoLabel);
        window.add(expertiseLabel);
        window.add(expertiseComboBox);
        window.add(startDateLabel);
        window.add(startDateSpinner);
        window.add(endDateLabel);
        window.add(endDateSpinner);
        window.add(startTimeLabel);
        window.add(startTimeField);
        window.add(endTimeLabel);
        window.add(endTimeField);
        window.add(printButton);
        window.add(searchByExpertiseButton);
        window.add(searchForDateButton);
        window.add(searchForDateTimeButton);
        window.add(arrayListDisplay);
        window.setVisible(true);

        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        updateExpertiseComboBox();
    }

    private void printSelectedData() {
        String selectedExpertise = (String) expertiseComboBox.getSelectedItem();
        Date selectedStartDate = (Date) startDateSpinner.getValue();
        Date selectedEndDate = (Date) endDateSpinner.getValue();

        java.sql.Date sqlStartDate = new java.sql.Date(selectedStartDate.getTime());
        java.sql.Date sqlEndDate = new java.sql.Date(selectedEndDate.getTime());

        String formattedStartDate = new SimpleDateFormat("dd-MMM-yyyy").format(selectedStartDate);
        String formattedEndDate = new SimpleDateFormat("dd-MMM-yyyy").format(selectedEndDate);
        String selectedStartTime = startTimeField.getText();
        String selectedEndTime = endTimeField.getText();

        try {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

            // Convert selectedStartTime and selectedEndTime to java.util.Date
            Date parsedStartTime = timeFormat.parse(selectedStartTime);
            Date parsedEndTime = timeFormat.parse(selectedEndTime);

            // Convert to java.sql.Time
            java.sql.Time sqlStartTime = new java.sql.Time(parsedStartTime.getTime());
            java.sql.Time sqlEndTime = new java.sql.Time(parsedEndTime.getTime());

            System.out.println("Selected Expertise: " + selectedExpertise);
            System.out.println("Start Date: " + formattedStartDate);
            System.out.println("End Date: " + formattedEndDate);
            System.out.println("Start Time: " + sqlStartTime);
            System.out.println("End Time: " + sqlEndTime);

            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add("Expertise: " + selectedExpertise);
            arrayList.add("Start Date: " + formattedStartDate);
            arrayList.add("End Date: " + formattedEndDate);
            arrayList.add("Start Time: " + sqlStartTime);
            arrayList.add("End Time: " + sqlEndTime);

            for (AppointmentInfo i : handler.getDoctorsAvailableBetweenTime(
                    sqlStartDate,
                    sqlEndDate,
                    sqlStartTime,
                    sqlEndTime
            )) {
                System.out.println(i.getInfoText());
            }

            arrayListDisplay.setListData(arrayList.toArray(new String[0]));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void searchByExpertise() {
        // Implement search by expertise logic here
        // Use expertiseComboBox.getSelectedItem() to get the selected expertise
        // Update arrayListDisplay accordingly
    }

    private void searchForDate() {
        // Implement search for date logic here
        // Use startDateSpinner.getValue() to get the selected start date
        // Update arrayListDisplay accordingly
    }

    private void searchForDateTime() {
        String selectedExpertise = (String) expertiseComboBox.getSelectedItem();
        Date selectedStartDate = (Date) startDateSpinner.getValue();
        Date selectedEndDate = (Date) endDateSpinner.getValue();

        java.sql.Date sqlStartDate = new java.sql.Date(selectedStartDate.getTime());
        java.sql.Date sqlEndDate = new java.sql.Date(selectedEndDate.getTime());

        String formattedStartDate = new SimpleDateFormat("dd-MMM-yyyy").format(selectedStartDate);
        String formattedEndDate = new SimpleDateFormat("dd-MMM-yyyy").format(selectedEndDate);
        String selectedStartTime = startTimeField.getText();
        String selectedEndTime = endTimeField.getText();

        try {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

            // Convert selectedStartTime and selectedEndTime to java.util.Date
            Date parsedStartTime = timeFormat.parse(selectedStartTime);
            Date parsedEndTime = timeFormat.parse(selectedEndTime);

            // Convert to java.sql.Time
            java.sql.Time sqlStartTime = new java.sql.Time(parsedStartTime.getTime());
            java.sql.Time sqlEndTime = new java.sql.Time(parsedEndTime.getTime());

            // Create a list to store the AppointmentInfo objects
            ArrayList<AppointmentInfo> appointmentInfoList = new ArrayList<>();

            for (AppointmentInfo i : handler.getDoctorsAvailableBetweenTime(
                    sqlStartDate,
                    sqlEndDate,
                    sqlStartTime,
                    sqlEndTime
            )) {
                // Add each AppointmentInfo object to the list
                appointmentInfoList.add(i);
            }

            // Create a list to store the getInfoText strings
            ArrayList<String> displayList = new ArrayList<>();

            // Extract getInfoText strings from AppointmentInfo objects
            for (AppointmentInfo appointmentInfo : appointmentInfoList) {
                displayList.add(appointmentInfo.getInfoText());
            }

            // Set the list data to the JList
            arrayListDisplay.setListData(displayList.toArray(new String[0]));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void updateExpertiseComboBox() {
        try {
            ArrayList<String> departments = handler.getAllDepartments();
            String[] expertiseOptions = departments.toArray(new String[0]);

            expertiseComboBox.removeAllItems();
            for (String department : expertiseOptions) {
                expertiseComboBox.addItem(department);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
