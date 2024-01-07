public class AppointmentInfo {
    int doctorID;
    int slotID;
    java.sql.Time startTime;
    java.sql.Time endTime;
    java.sql.Date day;
    int appointmentID;

    boolean available;

    String doctorName;
    String patientName;

    public String getInfoText(){
        return (doctorID+" Doctor name: "+ doctorName + "Patient Name: "+ patientName + " " + slotID + " " + day + " " + startTime + " " + endTime + " " + available);
    }

    public String getAvailabilityText(){
        return (doctorID+" Doctor name: "+ doctorName + " slotID" + slotID + " day: " + day + " start: " + startTime + " end: " + endTime + " available: " + available);
    }

}
