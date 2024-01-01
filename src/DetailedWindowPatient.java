import javax.swing.*;
import java.sql.SQLException;

public class DetailedWindowPatient {
    JFrame window;
    JLabel infoLabel;

    int userID;

    DataHandler handler;

    DetailedWindowPatient(){
        handler = new DataHandler();
    }

    public void createWindow(int userID){

        this.userID = userID;

        window = new JFrame();
        window.setSize(640, 480);


        try {
            infoLabel = new JLabel("Account Type: " + handler.getUserTypeFromId(userID) + " AccountID: " + userID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        infoLabel.setBounds(50, 100, 800, 30);
        infoLabel.setVisible(true);

        window.setLayout(null);
        window.add(infoLabel);
        window.setVisible(true);

        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

}
