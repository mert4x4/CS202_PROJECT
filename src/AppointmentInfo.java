public class AppointmentInfo {
    int doctorID;
    int slotID;
    java.sql.Time startTime;
    java.sql.Time endTime;
    java.sql.Date day;

    boolean available;



    public String getInfoText(){
        return (doctorID + " " + slotID + " " + day + " " + startTime + " " + endTime + " " + available);
    }

}
