import java.sql.*;



public class DataHandler {

    public int login(String username, String password) throws Exception {
        DBConnection dbConnection = new DBConnection();
        Connection c = dbConnection.getConnection();

        if(!isUsernameExists(username)){
            throw new Exception("Username does not exist");
        }

        if(!isPasswordAndUsernameCorrect(username,password)){
            throw new Exception("Password is incorrect");
        }

        return getUserId(username);
    }


    private boolean isUsernameExists(String username){
        DBConnection dbConnection = new DBConnection();
        Connection c = dbConnection.getConnection();

        String sql = "SELECT * FROM User_ WHERE name = ?";
        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isPasswordAndUsernameCorrect(String username, String password){
        DBConnection dbConnection = new DBConnection();
        Connection c = dbConnection.getConnection();

        String sql = "SELECT * FROM User_ WHERE name = ? AND password = ?";
        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private int getUserId(String username) {
        DBConnection dbConnection = new DBConnection();
        Connection c = dbConnection.getConnection();

        String sql = "SELECT userID FROM User_ WHERE name = ?";
        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("userID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
