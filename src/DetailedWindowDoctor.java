import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class DetailedWindowDoctor {
    private String currentMode = "none"; // listRooms, listAppointments, listAvailability, none

    private JFrame window;
    private JLabel infoLabel;
    private int userID;
    private DataHandler handler;
    private JList<String> arrayListDisplay;
    private JButton buttonListAppointments;
    private JButton buttonAssign;
    private JButton buttonListRooms;
    private JComboBox<String> comboBoxAvailableRooms;
    private JComboBox<String> comboBoxAvailableNurses;
    private JButton buttonListAvailability;
    private JButton buttonSetAvailabilityTrue;
    private JButton buttonSetAvailabilityFalse;
    private ArrayList<AppointmentInfo> currentAppointmentInfo;

    public DetailedWindowDoctor() {
        handler = new DataHandler();
    }

    public void createWindow(int userID) {
        this.userID = userID;

        window = new JFrame();
        window.setSize(900, 500);
        window.setLayout(null);

        try {
            infoLabel = new JLabel("Account Type: " + handler.getUserTypeFromId(userID) + " AccountID: " + userID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        infoLabel.setBounds(50, 30, 800, 30);

        arrayListDisplay = new JList<>();
        JScrollPane scrollPane = new JScrollPane(arrayListDisplay);
        scrollPane.setBounds(50, 100, 660, 300);

        buttonListAppointments = new JButton("List Appointments");
        buttonListAppointments.setBounds(50, 80, 150, 20);
        buttonListAppointments.addActionListener(e -> listDoctorAppointments());

        buttonAssign = new JButton("Assign");
        buttonAssign.setBounds(750, 100, 100, 30);
        buttonAssign.addActionListener(e -> assignRoomAndNurse());

        buttonListRooms = new JButton("List Rooms");
        buttonListRooms.setBounds(215, 80, 150, 20);
        buttonListRooms.addActionListener(e -> listRooms());

        buttonListAvailability = new JButton("List Availability");
        buttonListAvailability.setBounds(400, 80, 150, 20);
        buttonListAvailability.addActionListener(e -> listAvailability());

        String[] roomOptions = {"Select a room"};
        comboBoxAvailableRooms = new JComboBox<>(roomOptions);
        comboBoxAvailableRooms.setBounds(750, 200, 100, 30);

        comboBoxAvailableNurses = new JComboBox<>(roomOptions);
        comboBoxAvailableNurses.setBounds(750, 250, 100, 30);

        buttonSetAvailabilityTrue = new JButton("Set Availability True");
        buttonSetAvailabilityTrue.setBounds(scrollPane.getX() + scrollPane.getWidth() + 10, 120, 180, 20);
        buttonSetAvailabilityTrue.addActionListener(e -> setAvailability(true));

        buttonSetAvailabilityFalse = new JButton("Set Availability False");
        buttonSetAvailabilityFalse.setBounds(scrollPane.getX() + scrollPane.getWidth() + 10, 150, 180, 20);
        buttonSetAvailabilityFalse.addActionListener(e -> setAvailability(false));

        arrayListDisplay.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    if (currentMode.equals("listAppointments"))
                        handleListSelection();
                }
            }
        });

        window.add(buttonAssign);
        window.add(infoLabel);
        window.add(scrollPane);
        window.add(buttonListAppointments);
        window.add(comboBoxAvailableRooms);
        window.add(comboBoxAvailableNurses);
        window.add(buttonListRooms);
        window.add(buttonListAvailability);
        window.add(buttonSetAvailabilityTrue);
        window.add(buttonSetAvailabilityFalse);

        window.setVisible(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        updateButtonVisibility();
    }
    private void listRooms() {
        currentMode = "listRooms";
        updateButtonVisibility();
        displayRooms(handler.getAllRoomAvailabilityDetails());
    }

    private void listDoctorAppointments() {
        currentMode = "listAppointments";
        updateButtonVisibility();
        currentAppointmentInfo = handler.getDoctorAppointments(userID);
        displayAppointments(currentAppointmentInfo);
    }

    private void displayAppointments(ArrayList<AppointmentInfo> appointmentInfoList) {
        ArrayList<String> displayList = new ArrayList<>();

        for (AppointmentInfo appointmentInfo : appointmentInfoList) {
            displayList.add(appointmentInfo.getInfoText());
            System.out.println(appointmentInfo.getInfoText());
        }

        arrayListDisplay.setListData(displayList.toArray(new String[0]));
    }

    private void displayRooms(ArrayList<RoomInfo> roomInfoList) {
        ArrayList<String> displayList = new ArrayList<>();

        for (RoomInfo roomInfo : roomInfoList) {
            displayList.add(roomInfo.getDetailedRoomInfo());
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

    private void assignRoomAndNurse() {
        try {
            Integer nurseID = handler.getAvailableNursesByTimeslot(currentAppointmentInfo.get(arrayListDisplay.getSelectedIndex()).slotID)
                    .get(comboBoxAvailableNurses.getSelectedIndex()).nurseID;

            Integer roomID = handler.getAvailableRoomsByTimeslot(currentAppointmentInfo.get(arrayListDisplay.getSelectedIndex()).slotID)
                    .get(comboBoxAvailableRooms.getSelectedIndex()).roomID;
            System.out.println(roomID);
            System.out.println(nurseID);
            try {
                int appointmentID = currentAppointmentInfo.get(arrayListDisplay.getSelectedIndex()).appointmentID;
                int slotID = currentAppointmentInfo.get(arrayListDisplay.getSelectedIndex()).slotID;
                handler.assignNurseAndRoom(appointmentID, nurseID, roomID);

                updateAvailableRooms(slotID);
                updateAvailableNurses(slotID);
                JOptionPane.showMessageDialog(window, "roomID: " + roomID + "nurseID: " + nurseID,  "Assigned!", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(window, "Error: " + "nurse and room is already assigned...", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(window, "Error: " + "there must be both nurse and room to assign", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayAvailability(ArrayList<AppointmentInfo> appointmentInfoList) {
        ArrayList<String> displayList = new ArrayList<>();

        for (AppointmentInfo appointmentInfo : appointmentInfoList) {
            displayList.add(appointmentInfo.getAvailabilityText());
        }

        arrayListDisplay.setListData(displayList.toArray(new String[0]));
    }


    private void listAvailability() {
        currentMode = "listAvailability";
        updateButtonVisibility();
        displayAvailability(handler.getDoctorAvailability(userID));
        currentAppointmentInfo = handler.getDoctorAvailability(userID);
    }

    private void setAvailability(boolean isAvailable) {

        if (arrayListDisplay.getSelectedIndex() != -1) {
            AppointmentInfo data = currentAppointmentInfo.get(arrayListDisplay.getSelectedIndex());
            try {
                handler.setAvailabilityByDoctorIdAndSlotId(userID, data.slotID, isAvailable);
            }
            catch (Exception e){
                JOptionPane.showMessageDialog(window, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        listAvailability();
    }


    public void updateButtonVisibility(){
        if(currentMode.equals("listRooms")){
            comboBoxAvailableRooms.setVisible(false);
            comboBoxAvailableNurses.setVisible(false);
            buttonAssign.setVisible(false);
            buttonSetAvailabilityTrue.setVisible(false);
            buttonSetAvailabilityFalse.setVisible(false);
        }
        if(currentMode.equals("listAppointments")){
            buttonListAppointments.setVisible(true);
            comboBoxAvailableRooms.setVisible(true);
            comboBoxAvailableNurses.setVisible(true);
            buttonAssign.setVisible(true);
            buttonSetAvailabilityTrue.setVisible(false);
            buttonSetAvailabilityFalse.setVisible(false);
        }
        if(currentMode.equals("listAvailability")){
            comboBoxAvailableRooms.setVisible(false);
            comboBoxAvailableNurses.setVisible(false);
            buttonAssign.setVisible(false);
            buttonSetAvailabilityTrue.setVisible(true);
            buttonSetAvailabilityFalse.setVisible(true);
        }
        if(currentMode.equals("none")){
            buttonAssign.setVisible(false);
            buttonSetAvailabilityTrue.setVisible(false);
            buttonSetAvailabilityFalse.setVisible(false);
            comboBoxAvailableRooms.setVisible(false);
            comboBoxAvailableNurses.setVisible(false);
        }

    }

}
