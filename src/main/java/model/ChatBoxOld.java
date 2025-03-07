package model;

public class ChatBoxOld {
    private int id;
    private int user1;
    private int user2;
    private String username1;
    private String username2;

    public ChatBoxOld() {}

    public ChatBoxOld(int t_id, int t_user1, int t_user2) {
        setId(t_id);
        setUser1(t_user1);
        setUser2(t_user2);
    }

    public ChatBoxOld(int t_id, int t_user1, int t_user2, String t_username1, String t_username2) {
        setId(t_id);
        setUser1(t_user1);
        setUser2(t_user2);
        setUsername1(t_username1);
        setUsername2(t_username2);
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
