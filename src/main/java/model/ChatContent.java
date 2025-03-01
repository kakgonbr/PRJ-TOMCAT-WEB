package model;

public class ChatContent {
    private int id;
    private int boxId;
    private String message;
    private java.sql.Date time;
    private int sender;

    public ChatContent() {}

    public ChatContent(int t_id, int t_box, String t_message, java.sql.Date t_time, int sender) {
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

    public void setTime(java.sql.Date time) {
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

    public java.sql.Date getTime() {
        return time;
    }

    public static ChatContent fromResultSet(java.sql.ResultSet rs) throws java.sql.SQLException {
        return new ChatContent(rs.getInt("id"),
         rs.getInt("chatBoxID"),
         rs.getString("message"),
         rs.getDate("time"),
         rs.getInt("senderID"));
    }

    public static ChatContent fromMessage(String message, int sender, int box) {
        return new ChatContent(-1, box, message, java.sql.Date.valueOf(java.time.LocalDate.now()), sender);
    }
}
