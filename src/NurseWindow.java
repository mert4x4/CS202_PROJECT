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
    private JList<String> arrayListDisplay;
    private int userID;
    private DataHandler handler;
    private JButton buttonListRooms;

    private JButton buttonListAssignedRooms;
    private ArrayList<AppointmentInfo> currentAppointmentInfo;

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

        buttonListAssignedRooms = new JButton("List Assigned Rooms");
        buttonListAssignedRooms.setBounds(50, 80, 150, 20); // Adjusted button position
        buttonListAssignedRooms.addActionListener(e -> listAssignedRooms());

        buttonListRooms = new JButton("List Rooms");
        buttonListRooms.setBounds(215, 80, 150, 20);
        buttonListRooms.addActionListener(e -> listRooms());

        arrayListDisplay = new JList<>();
        JScrollPane scrollPane = new JScrollPane(arrayListDisplay);
        scrollPane.setBounds(50, 100, 660, 300);

        window.setLayout(null);
        window.add(infoLabel);
        window.add(buttonListRooms);
        window.add(buttonListAssignedRooms);
        window.add(scrollPane); // Add the JScrollPane instead of JList directly
        window.setVisible(true);

        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }


    public void listRooms(){
        displayRooms(handler.getAllRoomAvailabilityDetails());
    }

    private void displayRooms(ArrayList<RoomInfo> roomInfoList) {
        ArrayList<String> displayList = new ArrayList<>();

        for (RoomInfo roomInfo : roomInfoList) {
            displayList.add(roomInfo.getDetailedRoomInfo());
        }
        arrayListDisplay.setListData(displayList.toArray(new String[0]));
    }


    private  void listAssignedRooms(){
        displayRooms(handler.getAssignedRoomsOfNurse(userID));
    }

    private void displayAssignedRooms(ArrayList<RoomInfo> roomInfoList) {
        ArrayList<String> displayList = new ArrayList<>();

        for (RoomInfo roomInfo : roomInfoList) {
            displayList.add(roomInfo.getDetailedRoomInfo());
        }
        arrayListDisplay.setListData(displayList.toArray(new String[0]));
    }
}





