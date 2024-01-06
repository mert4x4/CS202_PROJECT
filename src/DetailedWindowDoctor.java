import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class DetailedWindowDoctor {
    private JFrame window;
    private JLabel infoLabel;
    private int userID;
    private DataHandler handler;
    private JList<String> arrayListDisplay;
    private JButton buttonListAppointments;
    private JButton buttonAssign;
    private JComboBox<String> comboBoxAvailableRooms;
    private JComboBox<String> comboBoxAvailableNurses;
    private ArrayList<AppointmentInfo> currentAppointmentInfo;

    public DetailedWindowDoctor() {
        handler = new DataHandler();
    }

    public void createWindow(int userID) {
        this.userID = userID;

        window = new JFrame();
        window.setSize(800, 500);
        window.setLayout(null);

        try {
            infoLabel = new JLabel("Account Type: " + handler.getUserTypeFromId(userID) + " AccountID: " + userID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        infoLabel.setBounds(50, 30, 800, 30);

        arrayListDisplay = new JList<>();
        arrayListDisplay.setBounds(50, 100, 660, 300);
        arrayListDisplay.setVisible(true);

        buttonListAppointments = new JButton("List Appointments");
        buttonListAppointments.setBounds(50, 80, 150, 20);
        buttonListAppointments.setVisible(true);
        buttonListAppointments.addActionListener(e -> listDoctorAppointments());

        buttonAssign = new JButton("Assign");
        buttonAssign.setBounds(600, 80, 150, 20);
        buttonAssign.setVisible(true);
        buttonAssign.addActionListener(e -> assignRoomAndNurse());

        String[] roomOptions = {"Select a room"};
        comboBoxAvailableRooms = new JComboBox<>(roomOptions);
        comboBoxAvailableRooms.setBounds(220, 80, 150, 30);
        comboBoxAvailableRooms.setVisible(true);

        comboBoxAvailableNurses = new JComboBox<>(roomOptions);
        comboBoxAvailableNurses.setBounds(380, 80, 150, 30);
        comboBoxAvailableNurses.setVisible(true);

        arrayListDisplay.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    handleListSelection();
                }
            }
        });



        window.add(buttonAssign);
        window.add(infoLabel);
        window.add(arrayListDisplay);
        window.add(buttonListAppointments);
        window.add(comboBoxAvailableRooms);
        window.add(comboBoxAvailableNurses);

        window.setVisible(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void listDoctorAppointments() {
        currentAppointmentInfo = handler.getDoctorAppointments(userID);
        displayAppointments(currentAppointmentInfo);
    }

    private void displayAppointments(ArrayList<AppointmentInfo> appointmentInfoList) {
        ArrayList<String> displayList = new ArrayList<>();

        for (AppointmentInfo appointmentInfo : appointmentInfoList) {
            displayList.add(appointmentInfo.getInfoText());
        }

        arrayListDisplay.setListData(displayList.toArray(new String[0]));
    }

    private void handleListSelection() {
        int selectedIndex = arrayListDisplay.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < currentAppointmentInfo.size()) {
            AppointmentInfo selectedAppointment = currentAppointmentInfo.get(selectedIndex);
            updateAvailableRooms(selectedAppointment.slotID);
            updateAvailableNurses(selectedAppointment.slotID);
        }
    }

    private void updateAvailableRooms(int slotID) {
        try {
            ArrayList<RoomInfo> availableRooms = handler.getAvailableRoomsByTimeslot(slotID);

            comboBoxAvailableRooms.removeAllItems();
            for (RoomInfo room : availableRooms) {
                comboBoxAvailableRooms.addItem(room.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateAvailableNurses(int slotID) {
        try {
            ArrayList<NurseInfo> availableNurses = handler.getAvailableNursesByTimeslot(slotID);

            comboBoxAvailableNurses.removeAllItems();
            for (NurseInfo nurse : availableNurses) {
                comboBoxAvailableNurses.addItem(nurse.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void assignRoomAndNurse(){

            Integer nurseID = handler.getAvailableNursesByTimeslot(currentAppointmentInfo.get(arrayListDisplay.getSelectedIndex()).slotID)
                    .get(comboBoxAvailableNurses.getSelectedIndex()).nurseID;

            Integer roomID = handler.getAvailableRoomsByTimeslot(currentAppointmentInfo.get(arrayListDisplay.getSelectedIndex()).slotID)
                    .get(comboBoxAvailableRooms.getSelectedIndex()).roomID;

            System.out.println(roomID);System.out.println(nurseID);
            try{
                int appointmentID = currentAppointmentInfo.get(arrayListDisplay.getSelectedIndex()).appointmentID;
                handler.assignNurseAndRoom(appointmentID, nurseID, roomID);
            }
            catch (Exception e){
                throw new RuntimeException("could not inster! :(((((");
            }
    }


}
