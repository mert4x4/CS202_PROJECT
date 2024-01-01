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

    public int register(String username, String password, String userType) throws Exception {
        DBConnection dbConnection = new DBConnection();
        Connection c = dbConnection.getConnection();

        int newUserId = getMaxUserId(c) + 1;


        String insertSql = "INSERT INTO User_ (userID, password, name, user_type) VALUES (?, ?, ?, ?)";
        try (PreparedStatement insertPstmt = c.prepareStatement(insertSql)) {
            insertPstmt.setInt(1, newUserId);
            insertPstmt.setString(2, password);
            insertPstmt.setString(3, username);
            insertPstmt.setString(4, userType);

            int affectedRows = insertPstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            return newUserId; // Return the generated user ID
        } catch (SQLException e) {
            throw new Exception("Error registering user: " + e.getMessage());
        }
    }

    private int getMaxUserId(Connection c) throws SQLException {
        String maxUserIdSql = "SELECT MAX(userID) FROM User_";
        try (Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(maxUserIdSql)) {
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0;
            }
        }
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


    public String getUserTypeFromId(int userId) throws SQLException {
        DBConnection dbConnection = new DBConnection();
        Connection c = dbConnection.getConnection();

        String sql = "SELECT user_type FROM User_ WHERE userID = ?";
        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("user_type");
            }
        }

        throw new SQLException("User type not found for userID: " + userId);
    }

}
