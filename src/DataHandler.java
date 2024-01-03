import java.sql.*;
import java.util.ArrayList;


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

    public ArrayList<String> getAllDepartments() throws SQLException {
        ArrayList<String> departmentList = new ArrayList<>();

        DBConnection dbConnection = new DBConnection();
        Connection c = dbConnection.getConnection();

        String sql = "SELECT DISTINCT department FROM Doctor";
        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String department = rs.getString("department");
                departmentList.add(department);
            }
        }

        return departmentList;
    }


    public ArrayList<AppointmentInfo> getDoctorIdsByDepartment(String departmentName) {
        ArrayList<AppointmentInfo> appointments = new ArrayList<>();

        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getConnection();

        if (connection != null) {
            String sql = "CALL get_available_doctors_by_dept(?)";

            try (CallableStatement callableStatement = connection.prepareCall(sql)) {
                callableStatement.setString(1, departmentName);

                ResultSet resultSet = callableStatement.executeQuery();

                while (resultSet.next()) {
                    AppointmentInfo appointment = new AppointmentInfo();
                    appointment.doctorID = resultSet.getInt("doctorID");
                    appointment.slotID = resultSet.getInt("slotID");
                    appointment.startTime = resultSet.getTime("start_time");
                    appointment.endTime = resultSet.getTime("end_time");
                    appointment.day = resultSet.getDate("slot_day");
                    appointment.available = resultSet.getBoolean("available");


                    appointments.add(appointment);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return appointments;
    }

    public ArrayList<AppointmentInfo> getDoctorsAvailableOnXYDay(java.sql.Date startDate, java.sql.Date endDate) {
        ArrayList<AppointmentInfo> appointments = new ArrayList<>();

        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getConnection();

        if (connection != null) {
            String sql = "CALL get_doctors_available_between_dayXY(?,?)";

            try (CallableStatement callableStatement = connection.prepareCall(sql)) {
                callableStatement.setDate(1, startDate);
                callableStatement.setDate(2, endDate);

                ResultSet resultSet = callableStatement.executeQuery();

                while (resultSet.next()) {
                    AppointmentInfo appointment = new AppointmentInfo();
                    appointment.doctorID = resultSet.getInt("doctorID");
                    appointment.slotID = resultSet.getInt("slotID");
                    appointment.startTime = resultSet.getTime("start_time");
                    appointment.endTime = resultSet.getTime("end_time");
                    appointment.day = resultSet.getDate("slot_day");
                    appointment.available = resultSet.getBoolean("available");


                    appointments.add(appointment);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return appointments;
    }

    public ArrayList<AppointmentInfo> getDoctorsAvailableBetweenTime(java.sql.Date startDate, java.sql.Date endDate, java.sql.Time start_time, java.sql.Time end_time) {
        ArrayList<AppointmentInfo> appointments = new ArrayList<>();

        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getConnection();

        if (connection != null) {
            String sql = "CALL doctors_available_AtoB_XtoY(?,?,?,?)";

            try (CallableStatement callableStatement = connection.prepareCall(sql)) {
                callableStatement.setDate(1, startDate);
                callableStatement.setDate(2, endDate);
                callableStatement.setTime(3, start_time);
                callableStatement.setTime(4, end_time);


                ResultSet resultSet = callableStatement.executeQuery();

                while (resultSet.next()) {
                    AppointmentInfo appointment = new AppointmentInfo();
                    appointment.doctorID = resultSet.getInt("doctorID");
                    appointment.slotID = resultSet.getInt("slotID");
                    appointment.startTime = resultSet.getTime("start_time");
                    appointment.endTime = resultSet.getTime("end_time");
                    appointment.day = resultSet.getDate("slot_day");
                    appointment.available = resultSet.getBoolean("available");


                    appointments.add(appointment);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return appointments;
    }


    public ArrayList<AppointmentInfo> getPatientAppointments(int patientID) {
        ArrayList<AppointmentInfo> appointments = new ArrayList<>();

        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getConnection();

        if (connection != null) {
            String sql = "CALL get_appointment_by_userID(?)";

            try (CallableStatement callableStatement = connection.prepareCall(sql)) {
                callableStatement.setInt(1, patientID);

                ResultSet resultSet = callableStatement.executeQuery();

                while (resultSet.next()) {
                    AppointmentInfo appointment = new AppointmentInfo();
                    appointment.doctorID = resultSet.getInt("doctorID");
                    appointment.slotID = resultSet.getInt("slotID");
                    appointment.startTime = resultSet.getTime("start_time");
                    appointment.endTime = resultSet.getTime("end_time");
                    appointment.day = resultSet.getDate("slot_day");
                    appointment.available = resultSet.getBoolean("available");
                    appointment.appointmentID = resultSet.getInt("appointmentID");

                    appointments.add(appointment);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return appointments;
    }

    public void makeAppointment(int patientID, int doctorID, int slotID) throws Exception {
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getConnection();

        if (connection != null) {
            // Use a CallableStatement to call the stored procedure
            String sql = "{CALL MakeAppointment(?, ?, ?)}";
            try (CallableStatement callableStatement = connection.prepareCall(sql)) {
                callableStatement.setInt(1, doctorID);
                callableStatement.setInt(2, slotID);
                callableStatement.setInt(3, patientID);

                // Execute the stored procedure
                callableStatement.execute();

                // You can also retrieve any OUT parameters or result sets here if needed
            } catch (SQLException e) {
                // Handle SQL exceptions
                throw new Exception("Error making appointment: " + e.getMessage());
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void cancelAppointment(int appointmentID) throws Exception {
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.getConnection();

        if (connection != null) {
            // Use a CallableStatement to call the stored procedure
            String sql = "{CALL CancelAppointment(?)}";
            try (CallableStatement callableStatement = connection.prepareCall(sql)) {
                callableStatement.setInt(1, appointmentID);
                // Execute the stored procedure
                callableStatement.execute();
            } catch (SQLException e) {
                // Handle SQL exceptions
                throw new Exception("Error making appointment: " + e.getMessage());
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }





}
