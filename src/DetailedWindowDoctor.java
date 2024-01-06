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

public class DetailedWindowDoctor {
    String lastAction = ""; //expertise,day,time

    private JFrame window;
    private JLabel infoLabel;
    private int userID;
    private DataHandler handler;

    public DetailedWindowDoctor() {
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

        // Set layout and add the label before making the window visible
        window.setLayout(null);
        window.add(infoLabel);

        window.setVisible(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }





}
