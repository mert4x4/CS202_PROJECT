public class RoomInfo {
    public String roomType;
    public int roomID;
    public Boolean available;
    public java.sql.Date slot_day;
    public java.sql.Time start_time;
    public java.sql.Time end_time;
    Integer slotID;
    @Override
    public String toString() {
        return roomID + " " + roomType;
    }

    public String getDetailedRoomInfo(){
        return "Room Type:" + roomType + " roomID:" + roomID + " slot_day:" + slot_day + " start_time:" + start_time + " end_time" + end_time;
    }
}
