package model;

public class ChatContentOld {
    private int id;
    private int boxId;
    private String message;
    private java.sql.Timestamp time;
    private int sender;

    public ChatContentOld() {}

    public ChatContentOld(int t_id, int t_box, String t_message, java.sql.Timestamp t_time, int sender) {
        setId(t_id);
        setBoxId(t_box);
        setMessage(t_message);
        setTime(t_time);
        setSender(sender);
    }

    public void setBoxId(int boxId) {
        this.boxId = boxId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public void setTime(java.sql.Timestamp time) {
        this.time = time;
    }

    public int getBoxId() {
        return boxId;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public int getSender() {
        return sender;
    }

    public java.sql.Timestamp getTime() {
        return time;
    }

    public static ChatContentOld fromResultSet(java.sql.ResultSet rs) throws java.sql.SQLException {
        return new ChatContentOld(rs.getInt("id"),
         rs.getInt("chatBoxID"),
         rs.getString("message"),
         rs.getTimestamp("time"),
         rs.getInt("senderID"));
    }

    public static ChatContentOld fromMessage(String message, int sender, int box) {
        return new ChatContentOld(-1, box, message, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()), sender);
    }
}
