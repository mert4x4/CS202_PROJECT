import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;




public class NurseWindow {
    String lastAction = ""; //expertise,day,time
    private JFrame window;
    private JLabel infoLabel;
    private JLabel startDateLabel;
    private JLabel endDateLabel;
    private JLabel startTimeLabel;
    private JLabel endTimeLabel;
    private JSpinner startDateSpinner;
    private JSpinner endDateSpinner;
    private JFormattedTextField startTimeField;
    private JFormattedTextField endTimeField;
    private JList<String> arrayListDisplay;
    private int userID;
    private DataHandler handler;




    private ArrayList<AppointmentInfo> currentAppointmentInfo;


/*
   public NurseWindow() {
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




       JButton searchForDateButton = new JButton("Search Room for Date");
       searchForDateButton.setBounds(210, 250, 150, 30);
       searchForDateButton.addActionListener(e -> searchForDate());




       JButton searchForDateTimeButton = new JButton("Search Room for Date and Time");
       searchForDateTimeButton.setBounds(370, 250, 180, 30);
       searchForDateTimeButton.addActionListener(e -> searchForDateTime());


       JButton listAssignedRooms = new JButton("List Assigned Rooms");
       listAssignedRooms.setBounds(560, 250, 150, 30);
       listAssignedRooms.addActionListener(e -> listAssignedRooms());


       arrayListDisplay = new JList<>();
       arrayListDisplay.setBounds(50, 330, 660, 300);
       arrayListDisplay.setVisible(true);


       window.setLayout(null);
       window.add(infoLabel);
       window.add(startDateLabel);
       window.add(startDateSpinner);
       window.add(endDateLabel);
       window.add(endDateSpinner);
       window.add(startTimeLabel);
       window.add(startTimeField);
       window.add(endTimeLabel);
       window.add(endTimeField);
       window.add(searchForDateButton);
       window.add(searchForDateTimeButton);
       window.add(listAssignedRooms);
       window.add(arrayListDisplay);
       window.setVisible(true);


       window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


   }












   //BURDAYIM BUNLARI ROOM ICIN DUZENLE
   private void searchForDate() {
       lastAction = "day";
       currentAppointmentInfo = new ArrayList<AppointmentInfo>();




       Date selectedStartDate = (Date) startDateSpinner.getValue();
       Date selectedEndDate = (Date) endDateSpinner.getValue();




       java.sql.Date sqlStartDate = new java.sql.Date(selectedStartDate.getTime());
       java.sql.Date sqlEndDate = new java.sql.Date(selectedEndDate.getTime());




       ArrayList<AppointmentInfo> appointmentInfoList = new ArrayList<>();




       for (AppointmentInfo i : handler.getDoctorsAvailableOnXYDay(sqlStartDate, sqlEndDate)) {
           appointmentInfoList.add(i);
           currentAppointmentInfo.add(i);
       }




       displayAppointments(appointmentInfoList);
       cancelButton.setVisible(false);
   }


   private void searchForDateTime() {
       lastAction = "time";
       currentAppointmentInfo = new ArrayList<AppointmentInfo>();




       String selectedExpertise = (String) expertiseComboBox.getSelectedItem();
       Date selectedStartDate = (Date) startDateSpinner.getValue();
       Date selectedEndDate = (Date) endDateSpinner.getValue();




       java.sql.Date sqlStartDate = new java.sql.Date(selectedStartDate.getTime());
       java.sql.Date sqlEndDate = new java.sql.Date(selectedEndDate.getTime());




       String selectedStartTime = startTimeField.getText();
       String selectedEndTime = endTimeField.getText();




       try {
           SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
           Date parsedStartTime = timeFormat.parse(selectedStartTime);
           Date parsedEndTime = timeFormat.parse(selectedEndTime);




           java.sql.Time sqlStartTime = new java.sql.Time(parsedStartTime.getTime());
           java.sql.Time sqlEndTime = new java.sql.Time(parsedEndTime.getTime());




           ArrayList<AppointmentInfo> appointmentInfoList = new ArrayList<>();




           for (AppointmentInfo i : handler.getDoctorsAvailableBetweenTime(sqlStartDate, sqlEndDate, sqlStartTime, sqlEndTime)) {
               appointmentInfoList.add(i);
               currentAppointmentInfo.add(i);
           }




           displayAppointments(appointmentInfoList);




       } catch (ParseException e) {
           e.printStackTrace();
       }
       cancelButton.setVisible(false);
   }


   private void listAppointments() {
       ArrayList<AppointmentInfo> appointmentInfoList = handler.getPatientAppointments(userID);
       currentAppointmentInfo = appointmentInfoList;
       displayAppointments(appointmentInfoList);
       cancelButton.setVisible(true);
   }


   private void listAssignedRooms(){


       ArrayList<AppointmentInfo> appointmentInfoList = handler.getPatientAppointments(userID);
       currentAppointmentInfo = appointmentInfoList;
       displayAppointments(appointmentInfoList);
       cancelButton.setVisible(true);


   }


   private void displayAppointments(ArrayList<AppointmentInfo> appointmentInfoList) {
       ArrayList<String> displayList = new ArrayList<>();




       for (AppointmentInfo appointmentInfo : appointmentInfoList) {
           displayList.add(appointmentInfo.getInfoText());
       }




       arrayListDisplay.setListData(displayList.toArray(new String[0]));
   }




*/
}





