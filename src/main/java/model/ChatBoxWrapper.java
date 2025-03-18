package model;

public class ChatBoxWrapper implements java.io.Serializable {
    private int id;
    private int user1;
    private int user2;
    private String username1;
    private String username2;

    public ChatBoxWrapper() {}

    public ChatBoxWrapper(ChatBox box) {
        setId(box.getId());
        setUser1(box.getUser1().getId());
        setUser2(box.getUser2().getId());
        setUsername1(box.getUser1().getUsername());
        setUsername2(box.getUser2().getUsername());
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername1(String username1) {
        this.username1 = username1;
    }

    public void setUsername2(String username2) {
        this.username2 = username2;
    }

    public void setUser1(int user1) {
        this.user1 = user1;
    }

    public void setUser2(int user2) {
        this.user2 = user2;
    }

    public int getId() {
        return id;
    }

    public int getUser1() {
        return user1;
    }

    public int getUser2() {
        return user2;
    }

    public String getUsername1() {
        return username1;
    }

    public String getUsername2() {
        return username2;
    }

    public static ChatBoxOld fromResultSet(java.sql.ResultSet rs) throws java.sql.SQLException {
        return new ChatBoxOld(rs.getInt("id"),
         rs.getInt("user1"),
         rs.getInt("user2"),
         rs.getString("username1"),
         rs.getString("username2"));
    }
}
